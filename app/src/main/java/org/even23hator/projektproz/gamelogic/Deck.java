package org.even23hator.projektproz.gamelogic;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by Kamil on 2016-04-17.
 *
 * Klasa reprezentująca talię gracza. Zawiera karty, które mogą być dobierane do ręki.
 * Zrealizowano to przy pomocy vectora CardType.
 * W momencie tworzenia do talii dodawany jest stały komplet kart, które następnie są tasowane.
 */
public class Deck {
    private Vector<CardType> cards;
    private Player player;


    public Deck(Player _player) {
        cards = new Vector<>();
        player = _player;
        for(int i=0; i<6; i++) {
            cards.addElement(CardType.Shoot);
        }
        for(int i=0; i<3; i++) {
            cards.addElement(CardType.Heal);
        }
        for(int i=0; i<3; i++) {
            cards.addElement(CardType.Aim);
        }
        shuffleDeck();
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

    public Vector<CardType> getCards() {
        return cards;
    }
}

