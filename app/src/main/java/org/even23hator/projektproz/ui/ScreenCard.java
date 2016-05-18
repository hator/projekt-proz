package org.even23hator.projektproz.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

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

    public ScreenCard(int x, int y, CardType _card) {
        this.x = x;
        this.y = y;
        card = _card;
        state = MainActivity.getGameState();
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        if(wasClicked) {paint.setColor(Color.RED);}
        else {paint.setColor(Color.BLACK);}

        paint.setStrokeWidth(10);
        canvas.drawRect(x, y, x + CARD_W, y + CARD_H, paint);

        paint.setStrokeWidth(0);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(x + 10, y + 115, x + CARD_W - 10, y + CARD_H - 10, paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(x + 10, y + 10, x + CARD_W - 10, y + 110, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText(card.toString(), x + 15, y + 70, paint);
        canvas.drawText(wasClicked ? "Clicked" : "EFFECT", x+15, y+180, paint);
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
        if(wasClicked) {
            MessageRouter.getInstance().routeMessage(new Message(messageTypeFromCardType(this.card), state.getPlayerMe(), state.getPlayerOther()));
            MessageRouter.getInstance().routeMessage(new Message(MessageType.DisCard, state.getPlayerMe(), state.getPlayerOther()));
            wasClicked = false;
        }
        else {
            MessageRouter.getInstance().routeMessage(new Message(MessageType.UnclickCard, state.getPlayerMe(), state.getPlayerOther()));
            wasClicked = true;
        }
    }

    private MessageType messageTypeFromCardType(CardType card) {
        switch (card) {
            case Shoot:
                return MessageType.PlayCardShoot;
            case Aim:
                return MessageType.PlayCardAim;
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
