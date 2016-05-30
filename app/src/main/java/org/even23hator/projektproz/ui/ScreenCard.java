package org.even23hator.projektproz.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import org.even23hator.projektproz.MainActivity;
import org.even23hator.projektproz.gamelogic.CardType;
import org.even23hator.projektproz.gamelogic.GameState;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;

/**
 * Created by hator on 01.05.16.
 */
public class ScreenCard implements IScreenObject {
    public static final int CARD_W = 280;
    public static final int CARD_H = 370;

    private int x, y;
    private boolean wasClicked = false;
    private CardType card;
    private GameState state;
    private Bitmap image;

    public ScreenCard(int x, int y, CardType _card) {
        this.x = x;
        this.y = y;
        card = _card;
        state = MainActivity.getGameState();
        image = MainActivity.getGameView().getCardImages().get(_card);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        if(wasClicked) {
            paint.setColor(Color.YELLOW);
            paint.setStrokeWidth(10);
            canvas.drawRect(x, y, x + CARD_W, y + CARD_H, paint);}

        Rect r = new Rect(x + 10, y + 10, x + CARD_W - 10, y + CARD_H - 10);
        canvas.drawBitmap(image, null, r, paint);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public Rect getBounds() {
        return new Rect(x, y, x+CARD_W, y+CARD_H);
    }

    @Override
    public void onClick() {
        Log.d("Click", "Click");
        if(wasClicked) {
            MessageRouter.getInstance().routeMessage(new Message(
                    messageTypeFromCardType(this.card),
                    state.getPlayerMe(),
                    state.getPlayerOther()));

            MessageRouter.getInstance().routeMessage(new Message(
                    MessageType.DisCard,
                    state.getPlayerMe(),
                    state.getPlayerOther()));

            MessageRouter.getInstance().routeMessage(new Message(
                    MessageType.ChangeTurn,
                    state.getPlayerMe(),
                    state.getPlayerOther()));
        }
        else {
            MessageRouter.getInstance().routeMessage(new Message(
                    MessageType.UnclickCard,
                    state.getPlayerMe(),
                    state.getPlayerOther()));
        }
    }

    private MessageType messageTypeFromCardType(CardType card) {
        switch (card) {
            case Shoot:
                return MessageType.PlayCardShoot;
            case Aim:
                return MessageType.PlayCardAim;
            case Heal:
                return MessageType.PlayCardHeal;
        }
        return null;
    }

    public boolean isWasClicked() {
        return wasClicked;
    }

    public void setWasClicked(boolean wasClicked) {
        this.wasClicked = wasClicked;
    }
}
