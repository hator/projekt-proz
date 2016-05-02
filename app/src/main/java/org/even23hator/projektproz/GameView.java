package org.even23hator.projektproz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.even23hator.projektproz.ui.InputManager;


public class GameView extends SurfaceView {

    public GameView(Context context) {
        super(context);
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return InputManager.getInstance().onTouchEvent(event);
    }
}
