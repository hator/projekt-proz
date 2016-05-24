package org.even23hator.projektproz.ui;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.even23hator.projektproz.MainActivity;
import org.even23hator.projektproz.message.Message;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hator on 01.05.16.
 */
public class ScreenManager {
    private static ScreenManager instance = null;
    public static ScreenManager getInstance() {
        if(instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }


    private List<IScreenObject> objects;


    private ScreenManager() {
        objects = new ArrayList<>();
    }

    public void addObject(IScreenObject obj) {
        objects.add(obj);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == event.ACTION_DOWN) {
            // FIXME remove
            MessageRouter.getInstance().routeMessage(new Message(MessageType.PlayCardShoot,
                    MainActivity.getGameState().getPlayerMe(),
                    MainActivity.getGameState().getPlayerOther()
                    ));

            // TODO(hator): this can be done much faster (in fact logarithmic complexity) if using
            //              space partitioning eg. QuadTree
            for (IScreenObject obj : objects) {
                if(obj.getBounds().contains((int)event.getX(), (int)event.getY())) {
                    obj.onClick();
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        for(IScreenObject so : objects) {
            so.draw(canvas);
        }
    }
}