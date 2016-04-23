package org.even23hator.projektproz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by hator on 23.04.16.
 */
public class GameThread extends Thread {
    private volatile boolean running;
    private GameView gameView;

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
        long lastFrameTime = System.currentTimeMillis();
        while(running) {
            final long dt = lastFrameTime - System.currentTimeMillis();
            lastFrameTime = System.currentTimeMillis();

            update(dt);

            gameView.post(new Runnable() {
                public void run() {
                    draw(dt);
                }
            });
        }
    }

    private void update(long dt) {

    }


    private void draw(long dt) {
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

            canvas.drawText("delta " + dt + "ms", 20, 40, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
