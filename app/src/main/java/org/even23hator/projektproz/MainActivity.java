package org.even23hator.projektproz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.even23hator.projektproz.gamelogic.GameState;
import org.even23hator.projektproz.gamelogic.Player;
import org.even23hator.projektproz.message.MessageRouter;
import org.even23hator.projektproz.message.MessageType;
import org.even23hator.projektproz.ui.ScreenManager;


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

        //gameState = new GameState(getGameState().getActive());
        if(RemoteMessagePassingService.isServer()) {
            Player first = (Math.random() > 0.5) ? MainActivity.getGameState().getPlayerMe() : MainActivity.getGameState().getPlayerOther();
            MessageRouter.getInstance().routeMessage(new org.even23hator.projektproz.message.Message(MessageType.FirstPlayer, first, null));
        }
        gameView = new GameView(this);
        gameThread = new GameThread(this);
        setContentView(gameView);
        Log.d("Main", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Intent intent = new Intent(this, RemoteMessagePassingService.class);
        //bindService(intent, LoginActivity.getConnection(), Context.BIND_AUTO_CREATE);

        Log.d("Main", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameThread.setRunning(true);

        gameThread.start();
        Log.d("Main", "onResume");
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
        Log.d("Main", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if(LoginActivity.isServiceBound()) {
            unbindService(LoginActivity.getConnection());
            LoginActivity.setServiceBound(false);
        }*/
        Log.d("Main", "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenManager.getInstance().reset();
        MessageRouter.getInstance().reset();
        gameState.reset();
        Log.d("Main", "onDestroy");
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
