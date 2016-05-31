package org.even23hator.projektproz.gamelogic;

import android.util.Log;

import org.even23hator.projektproz.message.IMessageListener;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;

/**
 * Created by hator on 18.05.16.
 *
 * Klasa opisuje akcje po zagraniu każdej karty.
 * W konstruktorze rejestruje nasłuchiwaczy, którzy odbierają wiadomości od zagranych ScreenCard.
 * Po nadejściu komunikatu wywołują odpowiednią funkcję, która realizuje dzialanie karty w logice gry. (modyfikacja gameState)
 *
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
                Log.d("Shoot", "hit");
                hitShoot(message.sender, message.target);
            }
        }, MessageType.HitCardShoot);

        MessageRouter.getInstance().registerListener(new IMessageListener() {
            @Override
            public void onMessage(Message message) {
                Log.d("Shoot", "missed");
            }
        }, MessageType.MissedCardShoot);

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
            MessageRouter.getInstance().routeMessage(new Message(
                   MessageType.HitCardShoot,
                    caster,
                    target));
        }
        else {
            MessageRouter.getInstance().routeMessage(new Message(
                    MessageType.MissedCardShoot,
                    caster,
                    target));
        }
    }

    public void hitShoot(Player caster, Player target) {
        target.damage(1);
    }

    public void playAim(Player caster) {
        caster.setAccuracy(caster.getAccuracy() + 1);
    }

    public void playHeal(Player caster) {
        caster.setHp(caster.getHp() + 1);
    }
}
