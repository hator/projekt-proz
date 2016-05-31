package org.even23hator.projektproz.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.even23hator.projektproz.MainActivity;
import org.even23hator.projektproz.gamelogic.Player;

/**
 * Created by Kamil on 2016-05-22.
 *
 * Klasa opisująca pasek życia gracza.
 * Składa się on z mniejszcych elementów (serca).
 * Obiekt ten prezentuje na ekranie faktyczny stan życia i zmienia się w trakcie trwania rozgrywki.
 * W przeciwieństwie do ScreenCard jego naciśniecie nie pociąga za sobą akcji.
 *
 */
public class ScreenHPBar implements IScreenObject{
    private static int HP_S = 100;
    private Player player;
    private Bitmap fullHP, emptyHP;

    private int x, y;

    public ScreenHPBar(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
        fullHP = MainActivity.getGameView().getBitmaps().get(BitmapType.Heart);
        emptyHP = MainActivity.getGameView().getBitmaps().get(BitmapType.EmptyHeart);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        Rect r;
        int i;
        for(i = 0; i < player.getHp(); ++i) {
            r = new Rect(x + i * 105, y, x + HP_S + i * 105, y + HP_S);
            canvas.drawBitmap(fullHP, null, r, paint);
        }
        for (; i < Player.MAX_HP; ++i) {
            r = new Rect(x + i * 105, y, x + HP_S + i * 105, y + HP_S);
            canvas.drawBitmap(emptyHP, null, r, paint);
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public Rect getBounds() {
        return new Rect(x, y, x + 310, y + 100);
    }

    @Override
    public void onClick() {

    }
}
