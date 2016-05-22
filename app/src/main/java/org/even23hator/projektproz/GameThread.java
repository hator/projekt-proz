package org.even23hator.projektproz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import org.even23hator.projektproz.gamelogic.Deck;
import org.even23hator.projektproz.gamelogic.Hand;
import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;
import org.even23hator.projektproz.ui.BitmapType;
import org.even23hator.projektproz.ui.ScreenHPBar;
import org.even23hator.projektproz.ui.ScreenManager;
import org.even23hator.projektproz.ui.ScreenCard;

import java.util.Vector;

/**
 * Created by hator on 23.04.16.
 */
public class GameThread extends Thread {
    public static final int TARGET_FPS = 60;
    public static final int TIMESTEP_NS = 1000000000 / TARGET_FPS;

    private volatile boolean running;
    private GameView gameView;
    private double fps;
    private long dt;

    private Vector<ScreenCard> cards;
    private ScreenHPBar oppHP, myHP;

    public GameThread(GameView gameView) {
        super();
        this.gameView = gameView;
        this.running = true;

        // Create ScreenCards
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

        // Register Listeners
        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                for (ScreenCard card : cards) {
                    card.setWasClicked(false);
                }
            }
        }, MessageType.UnclickCard);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                ScreenCard temp = null;
                for(int i = 0; i < cards.size(); ++i) {
                    if(cards.get(i).isWasClicked()) {
                        temp = cards.get(i);
                        ScreenManager.getInstance().removeObject(temp);
                        MainActivity.getGameState().getPlayerMe().getHand().removeCard(i);
                        break;
                    }
                }
                cards.remove(temp);
            }
        }, MessageType.DisCard);
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

            // NOTE(hator): drawing needs to be done by the UI thread so we post draw method to our view
            gameView.post(new Runnable() {
                public void run() {
                    draw();
                }
            });

            // NOTE(hator): if we render faster then target fps then we can sleep for a while
            //      so we don't block the cpu (improves battery life etc.)
            long sleepTime = (TIMESTEP_NS - dt) / 1000000;
            if(sleepTime > 0) {
                SystemClock.sleep(sleepTime);
            }

            // Replacing hand after playing 2 cards
            if(cards.size() == 2) {
                ScreenCard temp;
                for(int i = cards.size() - 1; i >= 0; --i) {
                    temp = cards.get(i);
                    ScreenManager.getInstance().removeObject(temp);
                    MainActivity.getGameState().getPlayerMe().getHand().removeCard(i);
                    cards.remove(temp);
                }
                for(int i=0; i < Hand.MAX_CARDS; ++i) {
                    if(MainActivity.getGameState().getPlayerMe().getDeck().getCards().size() == 0) {
                        MainActivity.getGameState().getPlayerMe().setDeck(new Deck(MainActivity.getGameState().getPlayerMe()));
                    }
                    MainActivity.getGameState().getPlayerMe().drawCard();
                    cards.addElement(new ScreenCard(20 + i * ScreenCard.CARD_W, 690, MainActivity.getGameState().getPlayerMe().getHand().getCard(i)));
                    ScreenManager.getInstance().addObject(cards.get(i));
                }
            }
        }
    }

    private void update(long dt) {

    }


    private void draw() {
        SurfaceHolder surfaceHolder = gameView.getHolder();

        synchronized (surfaceHolder) {
            if(!surfaceHolder.getSurface().isValid()) {
                return;
            }

            Canvas canvas = surfaceHolder.lockCanvas(null);

            Paint paint = new Paint();

            // background
            Rect r = new Rect(0, 0, 1920, 1080);
            canvas.drawBitmap(gameView.getBitmaps().get(BitmapType.Background), null, r, paint);

            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(45);

            canvas.drawText("delta " + dt / 1000000.f + "ms", 20, 40, paint);
            canvas.drawText("FPS " + fps, 20, 100, paint);
            canvas.drawText(MainActivity.getGameState().getInfo(), 20, 160, paint);

            // Draw players
            r = new Rect(1500, 25, 1920, 600);
            canvas.drawBitmap(gameView.getBitmaps().get(BitmapType.Opponent), null, r, paint);

            r = new Rect(300, 0, 800, 670);
            canvas.drawBitmap(gameView.getBitmaps().get(BitmapType.Me), null, r, paint);

            // Draw HP and cards
            ScreenManager.getInstance().draw(canvas);

            // Black Lines
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(10);
            canvas.drawLine(0, 670, 1920, 670, paint);
            canvas.drawLine(900, 0, 1200, 670, paint);
            canvas.drawLine(1200, 670, 1200, 1080, paint);

            r = new Rect(1450, 700, 1720, 780);
            canvas.drawBitmap(gameView.getBitmaps().get(BitmapType.Menu), null, r, paint);

            if(!MainActivity.getGameState().getPlayerOther().getAlive()) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(200);
                canvas.drawText("YOU WON!!!", 350, 400, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
