package org.even23hator.projektproz.gamelogic;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Player {
    private static final int MAX_HP = 3;

    private int hp;
    private Hand hand;
    private Deck deck;

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
        this.hp--;
        if(!getAlive()) {
            this.onDeath();
        }
    }

    private void onDeath() {
        // TODO implement observer
    }
}
