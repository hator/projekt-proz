package org.even23hator.projektproz.gamelogic;

/**
 * Created by hator on 17.04.16.
 */
public class CardShoot implements ICard {
    @Override
    public void playCard(Player caster, Player target) {
        // TODO random change connected to caster.accuracy and target.evasion
        target.damage(1);
    }
}
