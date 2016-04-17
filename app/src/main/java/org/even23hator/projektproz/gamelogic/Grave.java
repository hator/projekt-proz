package org.even23hator.projektproz.gamelogic;

import java.util.Vector;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Grave {
    protected Vector<ICard> usedCards;

    public Grave() {
        usedCards = new Vector<>();
    }

    public void addCard(ICard card) {
        usedCards.add(card);
    }
}
