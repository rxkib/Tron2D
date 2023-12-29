/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rakib
 */
import java.util.ArrayList;

public class GameLevel {
    public final GameID gameID;
    public final int rows, cols;
    public final LevelItem[][] level;
    public Position player1 = new Position(0, 0);
    public Position player2 = new Position(0, 0);
    
    /**
     * Associating keys from the map with different obstacles and creating them.
     * @param gameLevelRows
     * @param gameID 
     */
    public GameLevel(ArrayList<String> gameLevelRows, GameID gameID) {
        this.gameID = gameID;
        this.cols = calcMaxCol(gameLevelRows);
        this.rows = gameLevelRows.size();
        this.level = new LevelItem[rows][cols];

        initializeLevel(gameLevelRows);
    }
    
    
    public GameLevel(GameLevel gl) {
        gameID = gl.gameID;
        rows = gl.rows;
        cols = gl.cols;
        level = new LevelItem[rows][cols];
        player1 = new Position(gl.player1.getX(), gl.player1.getY());
        player2 = new Position(gl.player2.getX(), gl.player2.getY());
        
        for (int i = 0; i < rows; i++) {
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
    }
// setting up the level layout and player positions based on the characters in the strings
    private void initializeLevel(ArrayList<String> gameLevelRows) {
        for (int i = 0; i < rows; i++) {
            String s = gameLevelRows.get(i);
            for (int j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                setItem(i, j, c);
            }
            fillRow(i, s.length());
        }
    }

    private void setItem(int i, int j, char c) {
        switch (c) {
            case '@' -> player1 = new Position(j, i);
            case '%' -> player2 = new Position(j, i);
            case '#' -> {
                level[i][j] = LevelItem.WALL; return;
            }
        }
        level[i][j] = LevelItem.EMPTY;
    }
// Fills the remaining cells in a row with EMPTY if the row string is shorter than the maximum number of columns.
    private void fillRow(int row, int startCol) {
        for (int j = startCol; j < cols; j++) {
            level[row][j] = LevelItem.EMPTY;
        }
    }
//Determines the maximum length of the row strings, setting the width of the level
    private int calcMaxCol(ArrayList<String> gameLevelRows) {
        int maxCols = 0;
        for (String s : gameLevelRows) {
            maxCols = Math.max(maxCols, s.length());
        }
        return maxCols;
    }


    /**
     * The position of Player1
     * @return 
     */
    public boolean isValidPos1(){

        return (player1.getX() >= 0 && player1.getY() >= 0 && player1.getX() < cols && player1.getY() < rows && level[player1.getY()][player1.getX()]==LevelItem.EMPTY);
    }
    
    /**
     * The position of Player2
     * @return 
     */
    public boolean isValidPos2(){

        return (player2.getX() >= 0 && player2.getY() >= 0 && player2.getX() < cols && player2.getY() < rows && level[player2.getY()][player2.getX()]==LevelItem.EMPTY);
    }

    /**
     * Moving Player1 in the direction
     * @param d1
     * @return 
     */
    public boolean player1step(Direction d1){
        Position curr = player1;
        Position next = curr.fix(d1);
        player1 = next;
        return true;
    }
    /**
     * Moving Player1 in the direction
     * @param d2
     * @return 
     */
    public boolean player2step(Direction d2){
        Position curr = player2;
        Position next = curr.fix(d2);
        player2 = next;
        return true;
    }
    
    
    /**
     * Printing the levels of the game.
     */
    public void printLvl() {
    int player1X = player1.getX(), player1Y = player1.getY();
    int player2X = player2.getX(), player2Y = player2.getY();
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (i == player1Y && j == player1X) {
                builder.append('@');
            } else if (i == player2Y && j == player2X) {
                builder.append('%');
            } else {
                builder.append(level[i][j].orientation);
            }
        }
        builder.append('\n');
    }
    System.out.print(builder.toString());
   }
}