package org.even23hator.projektproz.message;

/**
 * Created by hator on 17.04.16.
 *
 * Typ wiadomości.
 *
 * Poza samym typem, zawiera również informację czy dany typ wiadomości ma być traktowany jako wewnętrzny.
 */
public enum MessageType {
    PlayCardShoot(true),
    HitCardShoot(false),
    MissedCardShoot(false),

    PlayCardAim(false),
    PlayCardHeal(false),

    UnclickCard(true),
    DisCard(true),


    RemotePlayerConnected(false),
    CommunicationError(true),
    FirstPlayer(false),
    ChangeTurn(false);


    private boolean internal;

    MessageType(boolean _internal) {
        this.internal = _internal;
    }

    public boolean isInternal() {
        return internal;
    }

}
