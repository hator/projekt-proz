package org.even23hator.projektproz.gamelogic;

/**
 * Created by Kamil on 2016-04-17.
 */
public class Player {
    private int hp;
    private boolean alive;
    private Hand hand;
    private static final int MAX_HP = 3;

    public Player (int a) {
        hp = a;
        alive = true;
    }
    public void setHp(int a) {
        hp = a;
    }

    public int getHp() {
        return hp;
    }

    public void setAlive(boolean b) {
        alive = b;
    }

    public boolean getAlive() {
        return alive;
    }
}
