package org.even23hator.projektproz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import org.even23hator.projektproz.gamelogic.CardType;
import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.ui.BitmapType;
import org.even23hator.projektproz.ui.ScreenManager;

import java.util.EnumMap;


public class GameView extends SurfaceView {

    //private final GameThread gameThread;

    private Resources res;

    private EnumMap<BitmapType, Bitmap> bitmaps;
    private EnumMap<CardType, Bitmap> cardImages;

    public GameView(Context context) {
        super(context);
        setFocusable(true);

        //this.gameThread = gameThread;

        setWillNotDraw(false);

        MessageRouter.getInstance().setPostUpdateHook(new Runnable() {
            @Override
            public void run() {
                GameView.this.postInvalidate();
            }
        });

        res = getContext().getResources();
        bitmaps = new EnumMap<>(BitmapType.class);
        cardImages = new EnumMap<>(CardType.class);
        loadBitmaps();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(MainActivity.getGameState().getActive() == MainActivity.getGameState().getPlayerMe()) {
            invalidate();
            return ScreenManager.getInstance().onTouchEvent(event);
        }
        else {
            return true;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();

        // background
        Rect r = new Rect(0, 0, 1920, 1080);
        canvas.drawBitmap(getBitmaps().get(BitmapType.Background), null, r, paint);

        // Draw players
        r = new Rect(1500, 25, 1920, 600);
        canvas.drawBitmap(getBitmaps().get(BitmapType.Opponent), null, r, paint);

        r = new Rect(300, 0, 800, 670);
        canvas.drawBitmap(getBitmaps().get(BitmapType.Me), null, r, paint);

        // Draw HP and cards
        ScreenManager.getInstance().draw(canvas);

        // Black Lines
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(0, 670, 1920, 670, paint);
        canvas.drawLine(900, 0, 1200, 670, paint);
        canvas.drawLine(1200, 670, 1200, 1080, paint);

        if(!MainActivity.getGameState().getPlayerOther().getAlive()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText("YOU WON!!!", 1280, 900, paint);
        }

        if(!MainActivity.getGameState().getPlayerMe().getAlive()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText("YOU LOST!!!", 1280, 900, paint);
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(75);
        canvas.drawText("Messages", 1280, 740, paint);

        paint.setTextSize(50);
        if(MainActivity.getGameState().getActive() == MainActivity.getGameState().getPlayerOther()) {
            canvas.drawText("Waiting for opponent...", 1280, 800, paint);
        }
        else {
            canvas.drawText("It's your turn.", 1280, 800, paint);
        }
    }

    public void loadBitmaps() {
        int i = R.drawable.cardshoot1;
        Bitmap b = BitmapFactory.decodeResource(res, i);
        cardImages.put(CardType.Shoot, b);

        i = R.drawable.cardaim1;
        b = BitmapFactory.decodeResource(res, i);
        cardImages.put(CardType.Aim, b);

        i = R.drawable.cardheal;
        b = BitmapFactory.decodeResource(res, i);
        cardImages.put(CardType.Heal, b);

        i = R.drawable.background2;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Background, b);

        i = R.drawable.heart1;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Heart, b);

        i = R.drawable.heartempty1;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.EmptyHeart, b);

        i = R.drawable.opponent2;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Opponent, b);

        i = R.drawable.me;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Me, b);
    }

    public EnumMap<BitmapType, Bitmap> getBitmaps() {
        return bitmaps;
    }

    public EnumMap<CardType, Bitmap> getCardImages() {
        return cardImages;
    }
}
