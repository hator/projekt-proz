package org.even23hator.projektproz.message;

/**
 * Created by hator on 17.04.16.
 */
public enum MessageType {
    PlayCardShoot(false),
    PlayCardAim(false),


    RemotePlayerConnected(false),
    CommunicationError(true);


    private boolean internal;

    MessageType(boolean _internal) {
        this.internal = _internal;
    }

    public boolean isInternal() {
        return internal;
    }

}
