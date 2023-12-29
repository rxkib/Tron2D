/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rakib
 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import res.ResourceLoader;

public class Game {
    private final HashMap<String, HashMap<Integer, GameLevel>> gm;
    private GameLevel gameLevel = null;
    private final Map<String, LevelItem> colorMappings = initializeColorMappings();

    public Game() {
        gm = new HashMap<>();
        readLvl();
    }

    public void load(GameID gameID) {
        if (gameID != null && gm.containsKey(gameID.mode)) {
            gameLevel = new GameLevel(gm.get(gameID.mode).get(gameID.lvl));
        }
    }

    public void printGameLvl() {
        if (gameLevel != null) {
            gameLevel.printLvl();
        }
    }
       
    public void ColorToField(int x, int y, String stg) {
    LevelItem item = colorMappings.getOrDefault(stg, LevelItem.DEFAULT);
    gameLevel.level[x][y] = item;
    }
    
    public boolean isValidPos1() {
        return gameLevel != null && gameLevel.isValidPos1();
    }

    public boolean isValidPos2() {
        return gameLevel != null && gameLevel.isValidPos2();
    }

    public boolean move1(Direction d1) {
        return gameLevel != null && gameLevel.player1step(d1);
    }

    public boolean move2(Direction d2) {
        return gameLevel != null && gameLevel.player2step(d2);
    }

    public Collection<String> getDifficulties() {
        return gm.keySet();
    }

    public Collection<Integer> getLevelsOfDifficulty(String difficulty) {
        return gm.getOrDefault(difficulty, new HashMap<>()).keySet();
    }

    public boolean isLvlLoaded() {
        return gameLevel != null;
    }

    public int getLvlRows() {
        return gameLevel != null ? gameLevel.rows : 0;
    }

    public int getLvlCols() {
        return gameLevel != null ? gameLevel.cols : 0;
    }

    public LevelItem getItem(int row, int col) {
        return gameLevel != null ? gameLevel.level[row][col] : null;
    }

    public GameID getID() {
        return gameLevel != null ? gameLevel.gameID : null;
    }

    public Position getPlayer1Pos() {
        return gameLevel != null ? new Position(gameLevel.player1.getX(), gameLevel.player1.getY()) : null;
    }

    public Position getPlayer2Pos() {
        return gameLevel != null ? new Position(gameLevel.player2.getX(), gameLevel.player2.getY()) : null;
    }

    private void readLvl() {
    try (InputStream is = ResourceLoader.loadResource("res/levels.txt");
         Scanner v = new Scanner(is)) {

        String line = readNxt(v);
        while (!line.isEmpty()) {
            GameID id = readID(line);
            if (id != null) {
                ArrayList<String> gameLevelRows = new ArrayList<>();
                line = readNxt(v);
                while (!line.isEmpty() && line.charAt(0) != ';') {
                    gameLevelRows.add(line);
                    line = readNxt(v);
                }
                addNewLvl(new GameLevel(gameLevelRows, id));
            } else {
                line = readNxt(v);
            }
        }
    } catch (Exception e) {
        System.out.println("Problem occurred while reading levels: " + e.getMessage());
    }
}


    private void addNewLvl(GameLevel gameLevel) {
        gm.computeIfAbsent(gameLevel.gameID.mode, k -> new HashMap<>())
                 .put(gameLevel.gameID.lvl, gameLevel);
    }

    private String readNxt(Scanner sc) {
    String l;
    do {
        l = sc.hasNextLine() ? sc.nextLine() : "";
    } while (l.trim().isEmpty() && sc.hasNextLine());
    return l;
}


    private GameID readID(String line) {
    line = line.trim();
    if (line.isEmpty() || line.charAt(0) != ';') {
        return null;
    }

    try (Scanner s = new Scanner(line)) {
        s.next(); // Skip the ';' character

        if (!s.hasNext()) {
            return null; // Check if there's a difficulty string
        }
        String mode = s.next().toUpperCase();

        if (!s.hasNextInt()) {
            return null; // Check if there's an integer ID following
        }
        int id = s.nextInt();

        return new GameID(mode, id);
    }
}

    
    private Map<String, LevelItem> initializeColorMappings() {
    Map<String, LevelItem> mappings = new HashMap<>();

    mappings.put("WALL", LevelItem.WALL);
    mappings.put("EMPTY", LevelItem.EMPTY);

    mappings.put("RTopToLeft", LevelItem.RTopToLeft);
    mappings.put("RTopToRight", LevelItem.RTopToRight);
    mappings.put("RDownToLeft", LevelItem.RDownToLeft);
    mappings.put("RDownToRight", LevelItem.RDownToRight);

    mappings.put("CTopToLeft", LevelItem.CTopToLeft);
    mappings.put("CTopToRight", LevelItem.CTopToRight);
    mappings.put("CDownToLeft", LevelItem.CDownToLeft);
    mappings.put("CDownToRight", LevelItem.CDownToRight);

    mappings.put("GTopToLeft", LevelItem.GTopToLeft);
    mappings.put("GTopToRight", LevelItem.GTopToRight);
    mappings.put("GDownToLeft", LevelItem.GDownToLeft);
    mappings.put("GDownToRight", LevelItem.GDownToRight);

    mappings.put("MTopToLeft", LevelItem.MTopToLeft);
    mappings.put("MTopToRight", LevelItem.MTopToRight);
    mappings.put("MDownToLeft", LevelItem.MDownToLeft);
    mappings.put("MDownToRight", LevelItem.MDownToRight);

    mappings.put("MHorizontal", LevelItem.MHorizontal);
    mappings.put("MVertical", LevelItem.MVertical);

    mappings.put("GHorizontal", LevelItem.GHorizontal);
    mappings.put("GVertical", LevelItem.GVertical);

    mappings.put("CHorizontal", LevelItem.CHorizontal);
    mappings.put("CVertical", LevelItem.CVertical);

    mappings.put("RHorizontal", LevelItem.RHorizontal);
    mappings.put("RVertical", LevelItem.RVertical);
    
    mappings.put("DEFAULT", LevelItem.DEFAULT);
     
    return mappings;
    }
}

