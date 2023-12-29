/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author rakib
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;
import res.ResourceLoader;
import model.Direction;

public class Board extends JPanel {
    private Game game;
    private Image p1, p2;
    private final int tile = 32;
    private double scale;
    private int new_size;
    private Map<LevelItem, Image> levelImages;
    
    /**
     * Initialize the Board
     * @param g the game we will play
     * @throws IOException potential error
     */
    public Board(Game g) throws IOException{
        this.game = g;
        setDoubleBuffered(true);
        scale = 1.0;
        new_size = (int)(scale * tile);
        initializeImages();
    }
    
    private void initializeImages() throws IOException {
    levelImages = new HashMap<>();
    
    levelImages.put(LevelItem.WALL, ResourceLoader.loadImage("res/wall.jpg"));
    levelImages.put(LevelItem.EMPTY, ResourceLoader.loadImage("res/bgPattern.jpg"));

    levelImages.put(LevelItem.RTopToLeft, ResourceLoader.loadImage("res/bgPatternRTopToLeft.jpg"));
    levelImages.put(LevelItem.RTopToRight, ResourceLoader.loadImage("res/bgPatternRTopToRight.jpg"));
    levelImages.put(LevelItem.RDownToLeft, ResourceLoader.loadImage("res/bgPatternRDownToLeft.jpg"));
    levelImages.put(LevelItem.RDownToRight, ResourceLoader.loadImage("res/bgPatternRDownToRight.jpg"));

    levelImages.put(LevelItem.CTopToLeft, ResourceLoader.loadImage("res/bgPatternCTopToLeft.jpg"));
    levelImages.put(LevelItem.CTopToRight, ResourceLoader.loadImage("res/bgPatternCTopToRight.jpg"));
    levelImages.put(LevelItem.CDownToLeft, ResourceLoader.loadImage("res/bgPatternCDownToLeft.jpg"));
    levelImages.put(LevelItem.CDownToRight, ResourceLoader.loadImage("res/bgPatternCDownToRight.jpg"));

    levelImages.put(LevelItem.MTopToLeft, ResourceLoader.loadImage("res/bgPatternMTopToLeft.jpg"));
    levelImages.put(LevelItem.MTopToRight, ResourceLoader.loadImage("res/bgPatternMTopToRight.jpg"));
    levelImages.put(LevelItem.MDownToLeft, ResourceLoader.loadImage("res/bgPatternMDownToLeft.jpg"));
    levelImages.put(LevelItem.MDownToRight, ResourceLoader.loadImage("res/bgPatternMDownToRight.jpg"));

    levelImages.put(LevelItem.GTopToLeft, ResourceLoader.loadImage("res/bgPatternGTopToLeft.jpg"));
    levelImages.put(LevelItem.GTopToRight, ResourceLoader.loadImage("res/bgPatternGTopToRight.jpg"));
    levelImages.put(LevelItem.GDownToLeft, ResourceLoader.loadImage("res/bgPatternGDownToLeft.jpg"));
    levelImages.put(LevelItem.GDownToRight, ResourceLoader.loadImage("res/bgPatternGDownToRight.jpg"));

    levelImages.put(LevelItem.RHorizontal, ResourceLoader.loadImage("res/bgPatternRHorizontal.jpg"));
    levelImages.put(LevelItem.RVertical, ResourceLoader.loadImage("res/bgPatternRVertical.jpg"));

    levelImages.put(LevelItem.GHorizontal, ResourceLoader.loadImage("res/bgPatternGHorizontal.jpg"));
    levelImages.put(LevelItem.GVertical, ResourceLoader.loadImage("res/bgPatternGVertical.jpg"));

    levelImages.put(LevelItem.CHorizontal, ResourceLoader.loadImage("res/bgPatternCHorizontal.jpg"));
    levelImages.put(LevelItem.CVertical, ResourceLoader.loadImage("res/bgPatternCVertical.jpg"));

    levelImages.put(LevelItem.MHorizontal, ResourceLoader.loadImage("res/bgPatternMHorizontal.jpg"));
    levelImages.put(LevelItem.MVertical, ResourceLoader.loadImage("res/bgPatternMVertical.jpg"));
    

    // Load player images for different directions
    p1 = ResourceLoader.loadImage("res/playerUp.jpg");  // Default image
    p2 = ResourceLoader.loadImage("res/playerUp2.jpg");  // Default image
}

    /**
     * The scale we are playing.
     * @param scale the size of the multiple.
     * @return 
     */
    public boolean fixScale(double scale){
        this.scale = scale;
        new_size = (int)(scale * tile);
        return refresh();
    }
    /**
     * Getter
     * @return 
     */
    public int getNew_size(){
        return new_size;
    }
    /**
     * Check if Player 1's position is valid
     * @return 
     */
    public boolean isValidPosP1(){
        return game.isValidPos1();
    }
    /**
     * Check if Player 2's position is valid
     * @return 
     */
    public boolean isValidPosP2(){
        return game.isValidPos2();
    }
    
    /**
     * It refreshes the map.
     * @return 
     */
    public boolean refresh() {
    if (!game.isLvlLoaded()) {
        return false;
    }

    int width = game.getLvlCols() * new_size;
    int height = game.getLvlRows() * new_size;
    Dimension dim = new Dimension(width, height);

    setPreferredSize(dim);
    setMaximumSize(dim);
    setSize(dim);
    repaint();

    return true;
}

    
    private Image getPlayerIMG(Direction direction) throws IOException {
        return switch (direction) {
            case LEFT -> ResourceLoader.loadImage("res/playerLeft.jpg");
            case RIGHT -> ResourceLoader.loadImage("res/playerRight.jpg");
            case UP -> ResourceLoader.loadImage("res/playerUp.jpg");
            case DOWN -> ResourceLoader.loadImage("res/playerDown.jpg");
            default -> null;
        };
    }

    public void setP1IMG(Direction d1) throws IOException {
        p1 = getPlayerIMG(d1);
    }

    public void setP2IMG(Direction d2) throws IOException {
        p2 = getPlayerIMG(d2);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);  // Call superclass method for proper painting
    if (!game.isLvlLoaded()) return;

    Graphics2D gr = (Graphics2D) g;
    int w = game.getLvlCols();
    int h = game.getLvlRows();
    Position plr1 = game.getPlayer1Pos();
    Position plr2 = game.getPlayer2Pos();

    for (int r = 0; r < h; r++) {
        for (int s = 0; s < w; s++) {
            Image img = null;
            LevelItem item = game.getItem(r, s);
            

            if (item != null) {
                img = levelImages.get(item);
            }

            if (plr1 != null && plr1.getX() == s && plr1.getY() == r) img = p1;
            if (plr2 != null && plr2.getX() == s && plr2.getY() == r) img = p2;
            
            if (img != null) {
                gr.drawImage(img, s * new_size, r * new_size, new_size, new_size, this);
            }
        }
    }
  }
}
