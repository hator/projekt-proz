package org.even23hator.projektproz.message;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * Created by hator on 17.04.16.
 */
public class MessageRouter extends Observable {
    private static MessageRouter ourInstance = new MessageRouter();

    public static MessageRouter getInstance() {
        return ourInstance;
    }


    private EnumMap<MessageType,Set<IMessageListener>> listeners;

    private MessageRouter() {
        listeners = new EnumMap<>(MessageType.class);
    }

    public void registerListener(IMessageListener listener, MessageType type) {
        Set<IMessageListener> bucket = listeners.get(type);
        if(bucket == null) {
            bucket = new HashSet<>();
            listeners.put(type, bucket);
        }
        bucket.add(listener);
    }

    // FIXME add way to unregisterListeners

    public void routeMessage(Message message) {
        for(IMessageListener listener : listeners.get(message.type)) {
            listener.onMessage(message);
        }
    }

}
