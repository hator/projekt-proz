package org.even23hator.projektproz.gamelogic;

import java.util.Vector;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Hand {
    private static final int MAX_CARDS = 4;
    private Vector<ICard> cardsInHand;
    public Hand() {
        cardsInHand = new Vector<>(MAX_CARDS);
    }

    public void drawCard() {
    }
}
