package org.even23hator.projektproz.gamelogic;

import org.even23hator.projektproz.ui.ScreenCard;

import java.util.Vector;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Hand {
    public static final int MAX_CARDS = 4;
    private Vector<CardType> cardsInHand;
    private Player player;

    public Hand(Player _player) {
        cardsInHand = new Vector<>(MAX_CARDS);
        player = _player;
        for(int i = 0; i < MAX_CARDS; i++) {
            this.addCard(player.getDeck().drawCard());
        }
    }

    public void addCard(CardType card) {
        cardsInHand.add(card);
    }

    public void removeCard(int i) {
        cardsInHand.remove(i);
    }

    public CardType getCard(int index) {
        return cardsInHand.get(index);
    }
}