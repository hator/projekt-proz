package org.even23hator.projektproz;

import android.app.Activity;
import android.util.Log;

import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;

/**
 * Created by Kamil on 2016-05-30.
 *
 * Klasa licznika wykorzystywanego przy sprawdzaniu połączenia.
 * Wiadomości z drugiego urządzenia powodują ponowne nastawienie licznika.
 * W momencie gdy czas dojdzie do zera, zamyka główne okno gry (Main Activity) i wraca do ekranu startowego (loginActivity).
 */
public class ConnectionTimer implements IMessageListener {
    private long timer; // milliseconds

    public ConnectionTimer() {
        timer = 4000;
        MessageRouter.getInstance().registerListener(this, MessageType.RemotePlayerConnected);
    }

    @Override
    public void onMessage(Message message) {
        if(message.isRemote()) {
            setTimer(4000);
            Log.d("Timer", "Set");
        }
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public void update(long dt, Activity activity) {
        if(timer <= 0) {
            Log.d("Timer", "Timeout");
            activity.finish();
        }
        else {
            timer -= dt;
        }
    }
}
