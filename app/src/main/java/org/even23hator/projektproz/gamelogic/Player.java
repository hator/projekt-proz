package org.even23hator.projektproz.gamelogic;

import android.util.Log;

import org.even23hator.projektproz.message.Message;


/**
 * Created by Kamil on 2016-04-17.
 */
public class Player {
    public static final int MAX_HP = 3;
    private static final double INIT_ACC = 5;
    private static final double INIT_DODGE = 4;

    private int hp;
    private double accuracy;
    private double dodge;
    private Deck deck;
    private Hand hand;

    public Player () {
        hp = MAX_HP;
        accuracy = INIT_ACC;
        dodge = INIT_DODGE;
        deck = new Deck(this);
        hand = new Hand(this);
    }

    public void setHp(int _hp) {
        if(!(_hp > MAX_HP))
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

    public double getDodge() {
        return dodge;
    }

    public void setDodge(double dodge) {
        this.dodge = dodge;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Hand getHand() {
        return hand;
    }
}
