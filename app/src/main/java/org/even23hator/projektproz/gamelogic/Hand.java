package org.even23hator.projektproz.gamelogic;

import java.util.Vector;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Hand {
    private static final int MAX_CARDS = 4;
    private Vector<ICard> cardsInHand;
    private Player player;

    public Hand(Player _player) {
        cardsInHand = new Vector<>(MAX_CARDS);
        player = _player;
        for(int i = 0; i < MAX_CARDS; i++) {
            this.addCard(player.getDeck().drawCard());
        }
    }

    public boolean addCard(ICard card) {
        if(card == null)
            return false;
        return cardsInHand.add(card);
    }

    public ICard disCard(ICard card) {
        return cardsInHand.remove(cardsInHand.indexOf(card));
    }

    public Player getPlayer() {
        return player;
    }

    public ICard getCard(int index) {
        return cardsInHand.get(index);
    }
}