package org.even23hator.projektproz.gamelogic;

/**
 * Created by hator on 20.03.16.
 */
public class GameState {
    private Player playerMe;
    private Player playerOther;

    private Player active;
    private String info;
    private CardActions cardActions;

    public GameState() {
        playerMe = new Player();
        playerOther = new Player();
        active = null;
        info = new String("Duel started.");
        cardActions = new CardActions();
    }

    public Player getActive() {
        return active;
    }

    public void setActive(Player active) {
        this.active = active;
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
