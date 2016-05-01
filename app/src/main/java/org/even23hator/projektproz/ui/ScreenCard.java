package org.even23hator.projektproz.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.even23hator.projektproz.ui.IScreenObject;

/**
 * Created by hator on 01.05.16.
 */
public class ScreenCard implements IScreenObject {
    public static final int CARD_W = 280;
    public static final int CARD_H = 370;

    private int x, y;

    public ScreenCard(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawRect(x, y, x+CARD_W, y + CARD_H, paint);

        paint.setStrokeWidth(0);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(x + 10, y + 115, x+CARD_W-10, y+CARD_H-10, paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(x + 10, y + 10, x+CARD_W-10, y+110, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText("CARD_NAME", x+15, y+70, paint);
        canvas.drawText("EFFECT", x+15, y+180, paint);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public Rect getBounds() {
        return new Rect(x, y, x+CARD_W, y+CARD_H);
    }
}