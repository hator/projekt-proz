package org.even23hator.projektproz.message;

import org.even23hator.projektproz.gamelogic.Player;

/**
 * Created by hator on 17.04.16.
 *
 * Klasa komunikatu.
 *
 * Zawiera typ komunikatu {@link MessageType}, nadawcę oraz odbiorcę, pole remote określające czy
 * komunikat przyszedł z zewnątrz od drugiego gracza.
 */
public class Message {
    public MessageType type;
    public Player sender;
    public Player target;

    private boolean remote;

    public Message(MessageType type, Player sender, Player target) {
        this.type = type;
        this.sender = sender;
        this.target = target;
        this.remote = false;
    }

    public Message(MessageType type, Player sender, Player target, boolean remote) {
        this.type = type;
        this.sender = sender;
        this.target = target;
        this.remote = remote;
    }

    public boolean isInternal() {
        return this.type.isInternal();
    }

    public boolean isRemote() {
        return remote;
    }

    @Override
    public String toString() {
        if(remote) {
            return "RemoteMessage(" + type.toString() + ", " + safeToString(sender) + ", " + safeToString(target) +")";
        }
        return "LocalMessage(" + type.toString() + ", " + safeToString(sender) + ", " + safeToString(target) +")";
    }

    private String safeToString(Object o) {
        if(o == null) return "null";
        return o.toString();
    }
}
