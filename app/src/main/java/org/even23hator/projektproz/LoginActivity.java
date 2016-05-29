package org.even23hator.projektproz;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private boolean serviceBound;
    private Messenger service = null;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder _service) {
            service = new Messenger(_service);
            serviceBound = true;
            Log.d("yolo", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            serviceBound = false;
            Log.d("yolo", "onServiceDisconnected");
        }
    };

    private Messenger receiver = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("yolo", "ConnectMessage");
            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    return;
                case -1:
                    Toast.makeText(LoginActivity.this, "Creating server failed!", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(LoginActivity.this, "No client connected!", Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    Toast.makeText(LoginActivity.this, "Connecting to server failed!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Unknown error!", Toast.LENGTH_SHORT).show();
            }

            progressDialog.cancel();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("Login", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RemoteMessagePassingService.class);
        intent.putExtra(RemoteMessagePassingService.INTENT_ACTIVITY_MESSENGER, receiver);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d("Login", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        /* if(serviceBound) {
            unbindService(connection);
            serviceBound = false;
        }*/
        Log.d("Login", "onStop");
    }

    public void onCreateGameClicked(View view) {
        makeProgressDialog("Waiting for other player");
        startSocketService(null);
    }

    public void onJoinGameClicked(View view) {
        makeProgressDialog("Joining game");
        EditText joinGameIPText = (EditText) findViewById(R.id.joinGameIP);
        String joinIP = joinGameIPText.getText().toString();
        if(!joinIP.isEmpty()) {
            startSocketService(joinIP);
        }
    }

    private void makeProgressDialog(String info) {
        progressDialog = ProgressDialog.show(this, info, "Please wait", true, false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                messageToService(Message.obtain(null, RemoteMessagePassingService.MSG_CANCEL_WAITING));
            }
        });
    }

    private void startSocketService(String joinIP) {
        Message msg = Message.obtain(null, RemoteMessagePassingService.MSG_CONNECT_TO, 0, 0, joinIP);
        Log.d("yolo", "startSocketService");
        messageToService(msg);
    }

    private void messageToService(Message msg) {
        Log.d("yolo", "messageToService");
        if (!serviceBound) return; // FIXME bind service
        msg.replyTo = this.receiver;
        try {
            service.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
