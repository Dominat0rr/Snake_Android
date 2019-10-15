package com.domain.snake.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.domain.snake.enums.TileType;

public class SnakeView extends View {
    private Paint mPaint = new Paint();
    private TileType snakeViewMap[][];

    /**
     * Constructor for SnakeView object
     * @param context
     * @param attrs
     */
    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSnakeViewMap(TileType[][] map) {
        this.snakeViewMap = map;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (snakeViewMap != null) {
            float tileSizeX = canvas.getWidth() / snakeViewMap.length;
            float tileSizeY = canvas.getHeight() / snakeViewMap[0].length;
            float circleSize = Math.min(tileSizeX, tileSizeY) / 2;

            for (int i = 0; i < snakeViewMap.length; i++) {
                for (int j = 0; j < snakeViewMap[i].length; j++) {
                    switch (snakeViewMap[i][j]) {
                        case Nothing:
                            mPaint.setColor(Color.WHITE);
                            break;
                        case Wall:
                            mPaint.setColor(Color.GRAY);
                            break;
                        case SnakeHead:
                            mPaint.setColor(Color.BLACK);
                            break;
                        case SnakeBody:
                            mPaint.setColor(Color.GRAY);
                            break;
                        case Food:
                            mPaint.setColor(Color.RED);
                            break;
                    }

                    canvas.drawCircle(i * tileSizeX + tileSizeX / 2f + circleSize / 2,
                            j * tileSizeY + tileSizeY / 2f + circleSize / 2,
                            circleSize, mPaint);
                }
            }
        }
    }
}
