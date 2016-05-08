package org.even23hator.projektproz.gamelogic;

/**
 * Created by hator on 20.03.16.
 */
public class GameState {
    private Player[] players;
    private int turnNr;
    private int turnPlayer;

    public GameState() {
        turnNr = 0;
        turnPlayer = 0;
        players = new Player[2];
        players[0] = new Player();
        players[1] = new Player();
    }

    public void nextTurnNr() {
        ++turnNr;
    }

    public int getTurnNr() {
        return turnNr;
    }

    public void nextTurnPlayer() {
        turnPlayer  = (++turnPlayer) % 2;
    }

    public int getTurnPlayer() {
        return turnPlayer;
    }

    public Player getPlayer(int a) {
        if(a < 2 && a >= 0)
            return players[a];
        else
            return null;
    }

}
