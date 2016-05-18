package org.even23hator.projektproz.gamelogic;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Deck {
    private Vector<CardType> cards;
    private Player player;


    public Deck(Player _player) {
        cards = new Vector<>();
        player = _player;
        for(int i=0; i<10; i++) {
            cards.addElement(CardType.Shoot);
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public CardType drawCard() {
        if(!cards.isEmpty())
            return cards.remove(0);
        else
            return null;
    }
}

