package org.even23hator.projektproz.gamelogic;

/**
 * Created by hator on 20.03.16.
 */
public class GameState {
    private Player playerMe, playerOther;
    private int turnNr;
    private boolean myTurn;
    private String info;
    private CardActions cardActions;

    public GameState() {
        turnNr = 0;
        myTurn = true; // TODO depends on who begins
        playerMe = new Player();
        playerOther = new Player();
        info = new String("Duel started.");
        cardActions = new CardActions();
    }

    public void nextTurnNr() {
        ++turnNr;
    }

    public int getTurnNr() {
        return turnNr;
    }

    public Player getTurnPlayer() {
        if(myTurn)
            return playerMe;
        return playerOther;
    }

    public Player getPlayerMe() {
        return playerMe;
    }

    public Player getPlayerOther() {
        return playerOther;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
