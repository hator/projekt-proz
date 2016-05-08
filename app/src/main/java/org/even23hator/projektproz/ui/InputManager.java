package org.even23hator.projektproz.ui;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hator on 01.05.16.
 */
public class InputManager {
    private static InputManager instance = null;
    public static InputManager getInstance() {
        if(instance == null) {
            instance = new InputManager();
        }
        return instance;
    }


    private List<IScreenObject> objects;


    private InputManager() {
        objects = new ArrayList<>();
    }

    public void addObject(IScreenObject obj) {
        objects.add(obj);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == event.ACTION_DOWN) {
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
}
