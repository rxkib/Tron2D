/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rakib
 */
public enum Direction { 
// 4 instances of enum here represent movement directions on a 2D grid or map, 
 //where x and y are the horizontal and vertical movements, respectively.
    DOWN(0, 1),
    LEFT(-1, 0),
    UP(0, -1),
    RIGHT(1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}


