package org.even23hator.projektproz;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.even23hator.projektproz.gamelogic.GameState;


public class MainActivity extends Activity {
    private GameThread gameThread;
    private static GameView gameView;
    private static GameState gameState = new GameState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gameView = new GameView(this);
        gameThread = new GameThread();
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameThread.setRunning(true);

        gameThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameThread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                // FIXME takes long time
                gameThread.interrupt();
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static GameView getGameView() {
        return gameView;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
