package org.even23hator.projektproz;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import org.even23hator.projektproz.gamelogic.Deck;
import org.even23hator.projektproz.gamelogic.Hand;
import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;
import org.even23hator.projektproz.ui.ScreenHPBar;
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
import java.util.Vector;

/**
 * Created by hator on 23.04.16.
 *
 * Wątek zawierający główną pętlę gry.
 *
 * Inicjalizuje wszystkie niezbędne zmienne, a następnie uruchamia pętlę gry.
 *
 * Pętla gry zawiera mechanizm stabilizacji częstotliwości odświerzania. Korzysta ze stałego kroku
 * całkowania (fixed timestep).
 */
public class GameThread extends Thread implements IMessageListener {
    public static final int TARGET_FPS = 60;
    public static final int TIMESTEP_NS = 1000000000 / TARGET_FPS;

    private volatile boolean running;
    public volatile double fps;
    public volatile long dt;

    private ConnectionTimer timer;
    private Activity activity;

    private Vector<ScreenCard> cards;
    private ScreenHPBar oppHP, myHP;

    public GameThread(MainActivity activity) {
        super();
        this.running = true;

        timer = new ConnectionTimer();
        this.activity = activity;

        cards = new Vector<>();
        for(int i=0; i < Hand.MAX_CARDS; ++i) {
            cards.addElement(new ScreenCard(20 + i*ScreenCard.CARD_W, 690, MainActivity.getGameState().getPlayerMe().getHand().getCard(i)));
            ScreenManager.getInstance().addObject(cards.get(i));
        }

        // Create HPBars
        oppHP = new ScreenHPBar(1090, 25, MainActivity.getGameState().getPlayerOther());
        ScreenManager.getInstance().addObject(oppHP);

        myHP = new ScreenHPBar(800, 550, MainActivity.getGameState().getPlayerMe());
        ScreenManager.getInstance().addObject(myHP);

        MessageRouter.getInstance().registerListenerAny(this);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                for (ScreenCard card : cards) {
                    card.setWasClicked(false);
                }
                ScreenManager.getInstance().getSelectedCard().setWasClicked(true);
            }
        }, MessageType.UnclickCard);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                ScreenCard temp = null;
                synchronized(ScreenManager.getInstance().getObjects()) {
                    for (int i = 0; i < cards.size(); ++i) {
                        if (cards.get(i).isWasClicked()) {
                            temp = cards.get(i);
                            ScreenManager.getInstance().removeObject(temp);
                            message.sender.getHand().removeCard(i);
                            break;
                        }
                    }
                    cards.remove(temp);
                }
            }
        }, MessageType.DisCard);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message.sender != null) {
                    MainActivity.getGameState().setActive(message.sender);
                    Log.d("First", message.sender.toString());
                }
            }
        }, MessageType.FirstPlayer);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message.isRemote()) {
                    MainActivity.getGameState().setActive(message.target);
                    Log.d("Change on me", message.sender.toString());
                }
                else {
                    MainActivity.getGameState().setActive(message.target);
                    Log.d("Change on other", message.sender.toString());
                }
            }
        }, MessageType.ChangeTurn);
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

            // Replacing hand after playing 2 cards
            if(cards.size() == 2) {
                synchronized(ScreenManager.getInstance().getObjects()) {
                    ScreenCard temp;
                    for (int i = cards.size() - 1; i >= 0; --i) {
                        temp = cards.get(i);
                        ScreenManager.getInstance().removeObject(temp);
                        MainActivity.getGameState().getPlayerMe().getHand().removeCard(i);
                        cards.remove(temp);
                    }
                    for (int i = 0; i < Hand.MAX_CARDS; ++i) {
                        if (MainActivity.getGameState().getPlayerMe().getDeck().getCards().size() == 0) {
                            MainActivity.getGameState().getPlayerMe().setDeck(new Deck(MainActivity.getGameState().getPlayerMe()));
                        }
                        MainActivity.getGameState().getPlayerMe().drawCard();
                        cards.addElement(new ScreenCard(20 + i * ScreenCard.CARD_W, 690, MainActivity.getGameState().getPlayerMe().getHand().getCard(i)));
                        ScreenManager.getInstance().addObject(cards.get(i));
                    }
                }
            }

            if(MainActivity.getGameState().getPlayerMe().getAlive() == false || MainActivity.getGameState().getPlayerOther().getAlive() == false) {
                MainActivity.getGameState().setActive(null);
            }
        }
    }

    private void update(long dt) {
        MessageRouter.getInstance().update();
        timer.update(dt / 1000000, activity);
    }

    @Override
    public void onMessage(Message message) {
        MainActivity.getGameState().setInfo(message.toString());
    }
}
