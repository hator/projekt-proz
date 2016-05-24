package org.even23hator.projektproz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.ui.ScreenManager;
import org.even23hator.projektproz.ui.ScreenCard;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

/**
 * Created by hator on 23.04.16.
 */
public class GameThread extends Thread implements IMessageListener {
    public static final int TARGET_FPS = 60;
    public static final int TIMESTEP_NS = 1000000000 / TARGET_FPS;

    private volatile boolean running;
    public volatile double fps;
    public volatile long dt;

    private ScreenCard[] cards;

    public GameThread() {
        super();
        this.running = true;

        cards = new ScreenCard[4];
        for(int i=0; i < cards.length; ++i) {
            cards[i] = new ScreenCard(50 + i*ScreenCard.CARD_W, 650, MainActivity.getGameState().getPlayerMe().getHand().getCard(i));
            ScreenManager.getInstance().addObject(cards[i]);
        }

        MessageRouter.getInstance().registerListenerAny(this);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long currentTime;
        long elapsedTime;
        long startTime = SystemClock.elapsedRealtimeNanos();
        long lastFrameTime = startTime;
        long frames = 0;
        dt = 0;
        fps = 0;


        while (running) {
            currentTime = SystemClock.elapsedRealtimeNanos();
            elapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            ++frames;
            fps = (double)(frames) / (currentTime - startTime) * 1e9;

            // NOTE(hator): for game mechanics update we use fixed timestep for simulation stability
            //      we accumulate time in dt and use it up for updates
            dt += elapsedTime;
            //Log.d("GameThread", "elapsed " + elapsedTime / 1e6f + ", delta " + dt / 1e6f);
            while(dt >= TIMESTEP_NS) {
                dt -= TIMESTEP_NS;
                update(TIMESTEP_NS);
            }

            // NOTE(hator): if we render faster then target fps then we can sleep for a while
            //      so we don't block the cpu (improves battery life etc.)
            long sleepTime = (TIMESTEP_NS - dt) / 1000000;
            if(sleepTime > 0) {
                SystemClock.sleep(sleepTime);
            }
        }
    }

    private void update(long dt) {
        MessageRouter.getInstance().update();
    }


    @Override
    public void onMessage(Message message) {
        MainActivity.getGameState().setInfo(message.toString());
    }
}
