package org.even23hator.projektproz;

import org.even23hator.projektproz.gamelogic.Player;
import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hator on 18.05.16.
 */
public class RemoteMessagePasser implements IMessageListener, Runnable {
    DataInputStream in;
    DataOutputStream out;

    public RemoteMessagePasser(InputStream _in, OutputStream _out) {
        in = new DataInputStream(_in);
        out = new DataOutputStream(new BufferedOutputStream(_out));

        // Register on for all messages
        for(MessageType type : MessageType.values()) {
            MessageRouter.getInstance().registerListener(this, type);
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            out.writeInt(message.type.ordinal());
            out.writeByte(playerToCode(message.sender));
            out.writeByte(playerToCode(message.target));
            out.flush();
        } catch (IOException e) {
            MessageRouter.getInstance().routeMessage(new Message(MessageType.CommunicationError, null, null));
        }
    }

    private int playerToCode(Player player) {
        if(player == MainActivity.getGameState().getPlayerMe())
            return 0;
        return 1;
    }

    public void receiveFromRemote() {
        MessageType type;
        Player sender;
        Player target;
        try {
            type = MessageType.values()[in.readInt()];
            sender = codeToPlayer(in.readChar());
            target = codeToPlayer(in.readChar());

            Message msg = new Message(type, sender, target);
            MessageRouter.getInstance().routeMessage(msg);

        } catch (IOException e) {
            MessageRouter.getInstance().routeMessage(new Message(MessageType.CommunicationError, null, null));
        }
    }

    private Player codeToPlayer(char c) {
        if(c == 0) {
            return MainActivity.getGameState().getPlayerOther();
        } else {
            return MainActivity.getGameState().getPlayerMe();
        }
    }


    @Override
    public void run() {
        while(true) {
            receiveFromRemote();
        }
    }
}
