package org.even23hator.projektproz;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.even23hator.projektproz.gamelogic.Player;
import org.even23hator.projektproz.message.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by hator on 20.05.16.
 */
public class RemoteMessagePassingService extends Service {
    public static final String TAG = "RemoteMessagePassing";
    public static final String INTENT_ACTIVITY_MESSENGER = "ActivityMessenger";
    public static final int MSG_CONNECT_TO = 1;
    public static final int MSG_CANCEL_WAITING = 2;

    public static final int PORT = 1337;

    private Messenger messenger;

    private CreateConnectionTask createConnectionTask;
    private Messenger activityMessenger;

    private volatile Socket socket;
    private Thread receivingThread;
    private Thread transmittingThread;

    private MessageHandler messageHandler;


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread(TAG + "Thread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        messageHandler = new MessageHandler(thread.getLooper());
        messenger = new Messenger(messageHandler);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        activityMessenger = intent.getParcelableExtra(INTENT_ACTIVITY_MESSENGER);
        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroy");
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_CONNECT_TO:
                    createConnectionTask = new CreateConnectionTask();
                    createConnectionTask.execute((String) msg.obj);
                    break;
                case MSG_CANCEL_WAITING:
                    createConnectionTask.cancel(true);
                    Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    class CreateConnectionTask extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... params) {
            String remoteIp = params[0];

            Socket socket;
            if (remoteIp == null) {
                //Server
                try {
                    ServerSocket ss = new ServerSocket(PORT);
                    ss.setReuseAddress(true);
                    ss.setSoTimeout(30000); // 30 seconds
                    socket = ss.accept();
                } catch (SocketTimeoutException e) {
                    reportToActivity(-2);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    reportToActivity(-1);
                    return null;
                }
            } else {
                //Client
                socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress(remoteIp, PORT), 5000); // 5 seconds
                } catch (IOException e) {
                    e.printStackTrace();
                    reportToActivity(-3);
                    return null;
                }
            }

            try {
                receivingThread = new Thread(new ReceivingRunnable(socket.getInputStream()));
                transmittingThread = new Thread(new TransmittingRunnable(socket.getOutputStream()));

                receivingThread.start();
                transmittingThread.start();

                RemoteMessagePassingService.this.socket = socket;

                reportToActivity(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void reportToActivity(int i) {
            try {
                activityMessenger.send(Message.obtain(null, i));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }


    private class ReceivingRunnable implements Runnable {
        private DataInputStream in;

        ReceivingRunnable(InputStream _in) {
            in = new DataInputStream(new BufferedInputStream(_in));
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                org.even23hator.projektproz.message.Message message = receiveFromRemote();
                Log.d("XD", "received messageQQQ");
                if(message == null) {
                    // error
                    Log.d("XD", "null message");
                    break;
                }
                MessageRouter.getInstance().routeMessage(message);
            }
        }

        private org.even23hator.projektproz.message.Message receiveFromRemote() {
            try {
                MessageType type = MessageType.values()[in.readInt()];
                Player sender = playerNumberIncoming(in.readByte());
                Player target = playerNumberIncoming(in.readByte());
                return new org.even23hator.projektproz.message.Message(type, sender, target, true);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class TransmittingRunnable implements Runnable, IMessageListener {
        private final ArrayBlockingQueue<org.even23hator.projektproz.message.Message> messageQueue = new ArrayBlockingQueue<>(16);
        private DataOutputStream out;

        TransmittingRunnable(OutputStream _out) {
            out = new DataOutputStream(new BufferedOutputStream(_out));

            for(MessageType type : MessageType.values()) {
                if(!type.isInternal()) {
                    MessageRouter.getInstance().registerListener(this, type);
                }
            }
        }

        public void run() {
            this.onMessage(new org.even23hator.projektproz.message.Message(MessageType.RemotePlayerConnected, null, null));

            org.even23hator.projektproz.message.Message message;
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    message = messageQueue.take();
                    transmitMessage(message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void transmitMessage(org.even23hator.projektproz.message.Message message) {
            try {
                out.writeInt(message.type.ordinal());
                out.writeByte(playerNumberOutgoing(message.sender));
                out.writeByte(playerNumberOutgoing(message.target));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(org.even23hator.projektproz.message.Message message) {
            if(!message.isInternal()) {
                messageQueue.add(message);
            }
        }
    }

    private Player playerNumberIncoming(byte b) {
        switch(b) {
            case 1:
                return MainActivity.getGameState().getPlayerOther();
            case 2:
                return MainActivity.getGameState().getPlayerMe();
            default:
                return null;
        }
    }

    private int playerNumberOutgoing(Player player) {
        if(player == MainActivity.getGameState().getPlayerMe()) {
            return 1;
        }
        if(player == MainActivity.getGameState().getPlayerOther()) {
            return 2;
        }
        return 0; // eg. null
    }
}
