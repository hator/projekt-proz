package org.even23hator.projektproz.ui;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by hator on 01.05.16.
 *
 * Interfejs pozwalający na opisanie obiektu niestatycznego przeznaczonego do wyświetlania na ekranie.
 * Zaliczją się do nich: ScreenCard oraz ScreenHPBar.
 */
public interface IScreenObject {
    void draw(Canvas canvas);
    void update(float dt);
    Rect getBounds();
    void onClick();
}
