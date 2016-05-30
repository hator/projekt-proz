package org.even23hator.projektproz.message;

import android.util.Log;
import android.view.View;

import org.even23hator.projektproz.GameView;

import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Queue;
import java.util.Set;

/**
 * Created by hator on 17.04.16.
 */
public class MessageRouter extends Observable {
    private static final MessageRouter ourInstance = new MessageRouter();
    private Runnable postUpdateHook;
    private View postUpdateView;

    public static MessageRouter getInstance() {
        return ourInstance;
    }

    private final EnumMap<MessageType,Set<IMessageListener>> listeners = new EnumMap<>(MessageType.class);
    private final Queue<Message> messageQueue = new ArrayDeque<>();

    public void registerListener(IMessageListener listener, MessageType type) {
        synchronized(listeners) {
            Set<IMessageListener> bucket = listeners.get(type);
            if (bucket == null) {
                bucket = new HashSet<>();
                listeners.put(type, bucket);
            }
            bucket.add(listener);
        }
    }

    public void registerListenerAny(IMessageListener listener) {
        for(MessageType type : MessageType.values()) {
            registerListener(listener, type);
        }
    }

    // FIXME add way to unregisterListeners

    public void routeMessage(Message message) {
        synchronized (messageQueue) {
            messageQueue.add(message);
        }
    }

    public void update() {
        boolean wasMessages = false;
        Message message;
        while(true) {
            synchronized (messageQueue) {
                message = messageQueue.poll();
            }
            if(message == null) {
                break;
            }
            wasMessages = true;
            synchronized (listeners) {
                Log.d("XD", String.valueOf(listeners.size()));
                for (IMessageListener listener : listeners.get(message.type)) {
                    Log.d("XD", "listener " + listener.toString());
                    listener.onMessage(message);
                }
                Log.d("XD", "Done");
            }

            if(wasMessages && postUpdateHook != null) {
                postUpdateHook.run();
            }
        }
    }


    public void setPostUpdateHook(Runnable runnable) {
        postUpdateHook = runnable;
    }

    public void reset() {
        listeners.clear();
        messageQueue.clear();
    }
}
