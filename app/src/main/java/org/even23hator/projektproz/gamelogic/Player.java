package org.even23hator.projektproz.gamelogic;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Player {
    private static final int MAX_HP = 3;

    private int hp;
    private Hand hand;
    private Deck deck;
    private Grave grave;

    public Player () {
        hp = MAX_HP;
    }
    public void setHp(int _hp) {
        hp = _hp;
    }

    public int getHp() {
        return hp;
    }

    public boolean getAlive() {
        return hp > 0;
    }

    public void damage(int amount) {
        this.hp -= amount;
        if(!getAlive()) {
            this.onDeath();
        }
    }

    private void onDeath() {
        // TODO implement observer
    }

    public void drawCard() {
        hand.addCard(deck.drawCard());
        // TODO if fail move cards from Grave to Deck or kill
    }

    public void playCard(ICard card, Player target) {
        card.playCard(this, target);
    }

    public void disCard(ICard card) {
        hand.disCard(card);
        grave.addCard(card);
    }

}
