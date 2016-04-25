package org.even23hator.projektproz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by hator on 23.04.16.
 */
public class GameThread extends Thread {
    public static final int TARGET_FPS = 60;
    public static final int TIMESTEP_NS = 1000000000 / TARGET_FPS;

    private volatile boolean running;
    private GameView gameView;
    private long elapsedTime;
    private double fps;
    private long frames;
    private long dt;

    public GameThread(GameView gameView) {
        super();
        this.gameView = gameView;
        this.running = true;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long currentTime;
        long startTime = SystemClock.elapsedRealtimeNanos();
        long lastFrameTime = startTime;
        dt = 0;
        fps = 0;
        frames = 0;

        while (running) {
            currentTime = SystemClock.elapsedRealtimeNanos();
            elapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            ++frames;
            fps = (double)(frames) / (currentTime - startTime) * 1e9;

            // NOTE(hator): for game mechanics update we use fixed timestep for simulation stability
            //      we accumulate time in dt and use it up for updates
            dt += elapsedTime;
            Log.d("GameThread", "elapsed " + elapsedTime/1e6f + ", delta " + dt/1e6f);
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

            // background
            canvas.drawColor(Color.argb(255, 15, 128, 255));

            Paint paint = new Paint();
            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(45);

            canvas.drawText("delta " + dt/1000000.f + "ms", 20, 40, paint);
            canvas.drawText("FPS " + fps, 20, 100, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
