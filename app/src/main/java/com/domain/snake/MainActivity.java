package com.domain.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.domain.snake.engine.GameEngine;
import com.domain.snake.enums.Direction;
import com.domain.snake.enums.GameState;
import com.domain.snake.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GameEngine gameEngine;
    private SnakeView snakeView;
    Handler handler = new Handler(Looper.getMainLooper());
    //private final long updateDelay = 140;
    private long updateDelay = 250;
    private int prevScore = 0;

    private float prevX;
    private float prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameEngine = new GameEngine();
        gameEngine.initGame();

        snakeView = (SnakeView) findViewById(R.id.snakeView);
        snakeView.setOnTouchListener(this);

        startUpdateHandler();
    }

    private void startUpdateHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();

                if (gameEngine.getCurrentGameState() == GameState.Running) {
                    handler.postDelayed(this, updateDelay);
                    updateDelay();
                }
                else if (gameEngine.getCurrentGameState() == GameState.Lost) {
                    onGameLost();
                }

                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        }, updateDelay);
    }

    private void onGameLost() {
        Toast.makeText(this, "You lost! Score: " + gameEngine.getScore(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Score: " + gameEngine.getScore(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Press to restart", Toast.LENGTH_LONG).show();
    }

    private void updateDelay() {
        if (gameEngine.getScore() > 0 && prevScore != gameEngine.getScore() && gameEngine.getScore() % 5 == 0) {
            setUpdateDelay(getUpdateDelay() - 15);
            prevScore = gameEngine.getScore();
        }
    }

    private long getUpdateDelay() {
        return updateDelay;
    }

    private void setUpdateDelay(long updateDelay) {
        this.updateDelay = updateDelay;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevX = motionEvent.getX();
                prevY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (gameEngine.getCurrentGameState() == GameState.Lost) {
                    this.recreate();
                }
                float newX = motionEvent.getX();
                float newY = motionEvent.getY();

                // Calculate where we swiped
                if (Math.abs(newX - prevX) > Math.abs(newY - prevY)) {
                    // LEFT - RIGHT direction
                    if (newX > prevX) {
                        // RIGHT
                        gameEngine.updateDirection(Direction.RIGHT);
                    }
                    else {
                        // LEFT
                        gameEngine.updateDirection(Direction.LEFT);
                    }
                }
                else {
                    // UP - DOWN direction
                    if (newY > prevY) {
                        // DOWN
                        gameEngine.updateDirection(Direction.DOWN);
                    }
                    else {
                        // UP
                        gameEngine.updateDirection(Direction.UP);
                    }
                }
                break;
        }
        return true;
    }
}
