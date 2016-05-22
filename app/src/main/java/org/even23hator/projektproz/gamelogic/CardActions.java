package org.even23hator.projektproz.gamelogic;

import android.util.Log;

import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;

/**
 * Created by hator on 18.05.16.
 */
public class CardActions {
    public CardActions() {
        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                playShoot(message.sender, message.target);
            }
        }, MessageType.PlayCardShoot);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                playAim(message.sender);
            }
        }, MessageType.PlayCardAim);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                playHeal(message.sender);
            }
        }, MessageType.PlayCardHeal);
    }

    public void playShoot(Player caster, Player target) {
        // TODO random change connected to caster.accuracy and target.evasion
        double hitChance = Math.random() * caster.getAccuracy();
        double dodgeChance = Math.random() * target.getDodge();
        Log.d("CardShoot", "hitChance: " + hitChance + ", dodgeChance: " + dodgeChance);

        if(hitChance > dodgeChance) {
            target.damage(1);
        }
    }

    public void playAim(Player caster) {
        caster.setAccuracy(caster.getAccuracy() + 1);
    }
    public void playHeal(Player caster) {
        caster.setHp(caster.getHp() + 1);
    }
}
