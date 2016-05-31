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
 *
 * Klasa opisująca obsługę wyświetlania IScreenObject.
 * Zawiera listę wszystkich takich obiektów.
 * Zależnie od ich implementacji funkcji onDraw() rysuje je na ekranie.
 * Odpowiada również za odbieranie onTouchEvent i wywoływanie naciśniętego IScreenObject.
 *
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

    private ScreenCard selectedCard = null;


    private ScreenManager() {
        objects = new ArrayList<>();
    }

    public void addObject(IScreenObject obj) {
        objects.add(obj);
    }

    public void removeObject(IScreenObject obj) {
        objects.remove(obj);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == event.ACTION_DOWN) {
            // TODO(hator): this can be done much faster (in fact logarithmic complexity) if using
            //              space partitioning eg. QuadTree
            for (IScreenObject obj : objects) {
                if(obj.getBounds().contains((int)event.getX(), (int)event.getY())) {
                    obj.onClick();
                    if(obj.getClass() == ScreenCard.class)
                        setSelectedCard((ScreenCard)obj);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        synchronized(objects) {
            for (IScreenObject so : objects) {
                so.draw(canvas);
            }
        }
    }

    public ScreenCard getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(ScreenCard selectedCard) {
        this.selectedCard = selectedCard;
    }

    public List<IScreenObject> getObjects() {
        return objects;
    }

    public void reset() {
        selectedCard = null;
        objects.clear();
    }
}