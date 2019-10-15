package com.domain.snake.classes;

import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;

    /**
     * Constructor for Coordinate object
     * @param x
     * @param y
     */
    public Coordinate(int x, int y) {
        setX(x);
        setY(y);
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Setters
    public void setX(int x) throws IllegalArgumentException {
        if (isValidX(x)) this.x = x;
        else throw new IllegalArgumentException("x cannot be negative");
    }

    public void setY(int y) throws IllegalArgumentException {
        if (isValidY(y)) this.y = y;
        else throw new IllegalArgumentException("y cannot be negative");
    }

    // isValid
    private static boolean isValidX(int x) {
        return x >= 0;
    }

    private static boolean isValidY(int y) {
        return y >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
