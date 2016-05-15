package org.even23hator.projektproz.gamelogic;

import android.util.Log;

import org.even23hator.projektproz.MainActivity;

/**
 * Created by hator on 17.04.16.
 */
public class CardShoot implements ICard {
    @Override
    public void playCard(Player caster, Player target) {
        // TODO random change connected to caster.accuracy and target.evasion
        double hitChance = Math.random() * caster.getAccuracy();
        double dodgeChance = Math.random() * target.getDodge();
        Log.d("CardShoot", "hitChance: " + hitChance + ", dodgeChance: " + dodgeChance);

        if(hitChance > dodgeChance) {
            MainActivity.getGameState().setInfo(new String("Przeciwnik trafiony."));
            target.damage(1);
        }
        else {
            MainActivity.getGameState().setInfo(new String("MISSED!"));
        }
    }
}
