package org.even23hator.projektproz;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceView;

import org.even23hator.projektproz.ui.ScreenManager;


public class GameView extends SurfaceView {

    public GameView(Context context) {
        super(context);
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return ScreenManager.getInstance().onTouchEvent(event);
    }
}
