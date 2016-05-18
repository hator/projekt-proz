package org.even23hator.projektproz.message;

import org.even23hator.projektproz.gamelogic.Player;

/**
 * Created by hator on 17.04.16.
 */
public class Message {
    public MessageType type;
    public Player sender;
    public Player target;

    public Message(MessageType type, Player sender, Player target) {
        this.type = type;
        this.sender = sender;
        this.target = target;
    }
}
