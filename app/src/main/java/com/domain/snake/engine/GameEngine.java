package com.domain.snake.engine;

import com.domain.snake.classes.Coordinate;
import com.domain.snake.enums.Direction;
import com.domain.snake.enums.GameState;
import com.domain.snake.enums.TileType;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private static final int WIDTH = 28;
    private static final int HEIGHT = 42;

    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snake = new ArrayList<>();
    private List<Coordinate> foods = new ArrayList<>();
    private Coordinate food = null;

    private Direction currentDirection = Direction.RIGHT;
    private GameState currentGameState = GameState.Running;

    private boolean multifoods = false;
    private int score = 0;

    private boolean increaseTail = false;

    /**
     * Constructor for GameEngine object
     */
    public GameEngine() {
        initGame();
    }

    /**
     * Initialize the game
     */
    public void initGame() {
        AddSnake();
        AddWalls();
        AddFood();
    }

    private void AddSnake() {
        snake.clear();

        snake.add(new Coordinate(WIDTH / 2, HEIGHT / 2));
        for (int i = 1; i < 5; i++) {
            snake.add(new Coordinate(WIDTH / 2 - i, HEIGHT / 2));
        }
    }

    private void AddWalls() {
        // Top and bottom walls
        for (int i = 0; i < WIDTH; i++) {
            walls.add(new Coordinate(i, 0));
            walls.add(new Coordinate(i, HEIGHT - 1));
        }

        // Left and right walls
        /*
            Starting from 1, so i skip the walls being set already and Top and left bottom
                -> else you set the "same" wall twice (which isn't dramatic, but yea)
         */
        for (int i = 1; i < HEIGHT; i++) {
            walls.add(new Coordinate(0, i));
            walls.add(new Coordinate(WIDTH - 1, i));
        }
    }

    private void AddFood() {
        Coordinate coordinate = null;

        boolean added = false;

        while (!added) {
            int rx = (int) (Math.random() * ((WIDTH - 3) + 1)) + 1;
            int ry = (int) (Math.random() * ((HEIGHT - 3) + 1)) + 1;
            System.out.println("random x: " + rx);
            System.out.println("random y: " + ry);

            coordinate = new Coordinate(rx, ry);
            boolean collision = false;
            for (Coordinate s: snake) {
                if (s.equals(coordinate)) {
                    collision = true;
                    break;
                }
            }

            if (multifoods) {
                for (Coordinate food : foods) {
                    if (food.equals(coordinate)) {
                        collision = true;
                        break;
                    }
                }
            }

            added = !collision;
        }

        if (multifoods) {
            foods.add(coordinate);
        }
        else {
            food = new Coordinate(coordinate.getX(), coordinate.getY());
        }
    }

    public void update() {
        switch(currentDirection) {
            case UP:
                updateSnake(0, -1);
                break;
            case RIGHT:
                updateSnake(1, 0);
                break;
            case DOWN:
                updateSnake(0, 1);
                break;
            case LEFT:
                updateSnake(-1, 0);
                break;
        }

        collisionCheck();
    }

    private void wallCollision() {
        for (Coordinate w : walls) {
            if (snake.get(0).equals(w)) {
                currentGameState = GameState.Lost;
                return;
            }
        }
    }

    private void selfCollision() {
//        for (Coordinate snakeBody : snake) {
//            if (snakeBody.equals(getSnakeHead())) {
//                currentGameState = GameState.Lost;
//                return;
//            }
//        }

        for (int i = 1; i < snake.size(); i++) {
            if (getSnakeHead().equals(snake.get(i))) {
                currentGameState = GameState.Lost;
                return;
            }
        }
    }

    private void foodCollision() {
        // Multi foods ingame
        if (multifoods) {
            Coordinate foodToRemove = null;
            for (Coordinate food : foods) {
                if (getSnakeHead().equals(food)) {
                    foodToRemove = food;
                }
            }
            if (foodToRemove != null) {
                score++;
                increaseTail = true;
                foods.remove(foodToRemove);
                AddFood();
            }
        }
        else {
            // Single food in game
            if (getSnakeHead().equals(food)) {
                score++;
                increaseTail = true;
                food = null;
                AddFood();
            }
        }
    }

    private void collisionCheck() {
        wallCollision();
        selfCollision();
        foodCollision();
    }

    private void updateSnake(int x, int y) {
        if (increaseTail) {
            int newX = snake.get(snake.size() - 1).getX();
            int newY = snake.get(snake.size() - 1).getY();

            snake.add(new Coordinate(newX, newY));
            increaseTail = false;
        }

        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
        }

        snake.get(0).setX(snake.get(0).getX() + x);
        snake.get(0).setY(snake.get(0).getY() + y);
    }

    public void updateDirection(Direction direction) {
        if (Math.abs(direction.ordinal() - currentDirection.ordinal()) % 2 == 1) {
            currentDirection = direction;
        }
    }

    public TileType[][] getMap() {
        TileType[][] map = new TileType[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = TileType.Nothing;
            }
        }

        // Snake head & body
        for (Coordinate s: snake) {
            map[s.getX()][s.getY()] = TileType.SnakeBody;
        }
        map[snake.get(0).getX()][snake.get(0).getY()] = TileType.SnakeHead;

        // Food(s)
        map[food.getX()][food.getY()] = TileType.Food;

        // Walls
        for (Coordinate wall: walls) {
            map[wall.getX()][wall.getY()] = TileType.Wall;
        }

        return map;
    }

    private Coordinate getSnakeHead() {
        return snake.get(0);
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public int getScore() {
        return score;
    }

    public boolean getMultifoods() {
        return multifoods;
    }
}
