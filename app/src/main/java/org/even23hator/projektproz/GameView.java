package org.even23hator.projektproz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.SurfaceView;

import org.even23hator.projektproz.gamelogic.CardType;
import org.even23hator.projektproz.ui.BitmapType;
import org.even23hator.projektproz.ui.ScreenManager;

import java.util.EnumMap;



public class GameView extends SurfaceView {
    private Resources res;

    private EnumMap<BitmapType, Bitmap> bitmaps;
    private EnumMap<CardType, Bitmap> cardImages;

    public GameView(Context context) {
        super(context);
        setFocusable(true);

        res = getContext().getResources();
        bitmaps = new EnumMap<>(BitmapType.class);
        cardImages = new EnumMap<>(CardType.class);
        loadBitmaps();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return ScreenManager.getInstance().onTouchEvent(event);
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

        i = R.drawable.background;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Background, b);

        i = R.drawable.heart1;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Heart, b);

        i = R.drawable.heartempty1;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.EmptyHeart, b);

        i = R.drawable.bar;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Bar, b);

        i = R.drawable.menu;
        b = BitmapFactory.decodeResource(res, i);
        bitmaps.put(BitmapType.Menu, b);

        i = R.drawable.opponent;
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
