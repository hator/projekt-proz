package org.even23hator.projektproz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.ui.ScreenManager;


public class GameView extends SurfaceView {

    private final GameThread gameThread;

    public GameView(Context context, GameThread gameThread) {
        super(context);
        setFocusable(true);

        this.gameThread = gameThread;

        setWillNotDraw(false);

        MessageRouter.getInstance().setPostUpdateHook(new Runnable() {
            @Override
            public void run() {
                GameView.this.postInvalidate();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return ScreenManager.getInstance().onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // background
        canvas.drawColor(Color.argb(255, 15, 128, 255));

        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(45);

        canvas.drawText("delta " + gameThread.dt / 1000000.f + "ms", 20, 40, paint);
        canvas.drawText("FPS " + gameThread.fps, 20, 100, paint);
        canvas.drawText(MainActivity.getGameState().getInfo(), 20, 160, paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(0, 600, 1920, 600, paint);
        canvas.drawLine(1000, 0, 1000, 600, paint);
        canvas.drawLine(1250, 600, 1250, 1080, paint);

        //Draw cards in hand
        ScreenManager.getInstance().draw(canvas);

        paint.setColor(Color.BLUE);
        canvas.drawRect(1005, 0, 1920, 595, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(75);
        canvas.drawText("OPPONENT HP=" + MainActivity.getGameState().getPlayerOther().getHp(), 1050, 180, paint);

        paint.setColor(Color.GREEN);
        canvas.drawRect(1255, 605, 1920, 1080, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        canvas.drawText("MENU", 1350, 750, paint);

        if(!MainActivity.getGameState().getPlayerOther().getAlive()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(200);
            canvas.drawText("YOU WIN!!!", 350, 400, paint);
        }
    }
}
