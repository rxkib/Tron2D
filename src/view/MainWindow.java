/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author rakib
 */
import db.DataBaseSQL;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import model.Direction;
import model.Game;
import model.GameID;
import model.Position;

public class MainWindow extends JFrame{
    private Game game = null;
    private Board board;
    
    private JLabel gameStat;   
    private JFrame startMenu;
    
    private JTextField textFieldP1;
    private JTextField textFieldP2;

    private boolean isGamePaused = false;
    
    private int currentLevel = 1;
    private final int maxLevel = 10;
    private int player1Score;
    private int player2Score;
    
    //GameRelatedVariables
    private Timer timer;
    private long elapsedTime=0;
    private long startTime;
    private String Player1Name = "";
    private String Player2Name = "";
    private Color Player1Color = Color.WHITE;
    private Color Player2Color = Color.WHITE;
    private Direction D1 = Direction.UP;
    private Direction D2 = Direction.UP;
    private Direction D1prev = Direction.UP;
    private Direction D2prev = Direction.UP;
    
    private JButton doneButton;
    private JButton leaderBoardButton;
    
    private JRadioButton b1, b2, b3, b4, b5, b6, b7, b8;

    private DataBaseSQL dbSQL;
    
    
    public MainWindow() throws IOException{
        initializeDatabase();
        gameStartingMenu();
        initializeGame();
        initializeWindow();
        initializeMenuBar();
        initializeGameStatLabel();

        try {
            initializeBoard();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to initialize game board.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        setFocusable(true);
        requestFocusInWindow();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);
                if (!game.isLvlLoaded()) return;

                int keyCode = ke.getKeyCode();
                
                // Handling the pause functionality
                if (keyCode == KeyEvent.VK_SPACE) {
                    isGamePaused = !isGamePaused;
                    return; // Skip the rest of the key handling when pausing/unpausing
                }

                if (isGamePaused) {
                    return; // Skip processing other keys when the game is paused
                }

                Direction newD1 = D1, newD2 = D2;

                switch (keyCode) {
                    case KeyEvent.VK_LEFT -> newD1 = Direction.LEFT;
                    case KeyEvent.VK_RIGHT -> newD1 = Direction.RIGHT;
                    case KeyEvent.VK_UP -> newD1 = Direction.UP;
                    case KeyEvent.VK_DOWN -> newD1 = Direction.DOWN;
                    case KeyEvent.VK_A -> newD2 = Direction.LEFT;
                    case KeyEvent.VK_D -> newD2 = Direction.RIGHT;
                    case KeyEvent.VK_W -> newD2 = Direction.UP;
                    case KeyEvent.VK_S -> newD2 = Direction.DOWN;
                    case KeyEvent.VK_ESCAPE -> { 
                        game.load(game.getID());
                        return;
                    }
                }

                // Preventing opposite direction change for player 1
                if (!isOpp(D1prev, newD1)) {
                    D1prev = D1;
                    D1 = newD1;
                }

                // Preventing opposite direction change for player 2
                if (!isOpp(D2prev, newD2)) {
                    D2prev = D2;
                    D2 = newD2;
                }

                board.repaint();
            }

            private boolean isOpp(Direction oldDir, Direction newDir) {
                return (oldDir == Direction.LEFT && newDir == Direction.RIGHT) ||
                       (oldDir == Direction.RIGHT && newDir == Direction.LEFT) ||
                       (oldDir == Direction.UP && newDir == Direction.DOWN) ||
                       (oldDir == Direction.DOWN && newDir == Direction.UP);
            }
        });

        setResizable(false);
        setLocationRelativeTo(null);
        game.load(new GameID("EASY", currentLevel));
        board.refresh();
        pack();
        
        leaderBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dbSQL.leaderboard();
                
            }
        
        });
    }
    
    
    private void initializeDatabase() {
        try {
            dbSQL = new DataBaseSQL();
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
    
    
    private void gameStartingMenu(){
        startMenu = new JFrame("Game Start Menu");
        startMenu.setLayout(new BorderLayout());
        startMenu.setSize(700, 400);
        startMenu.setResizable(false);
        
        // Panel for player input fields and labels
        JPanel temp = new JPanel();
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
        temp.setBackground(new Color(173, 216, 230)); // Black background



       
       // Adding Player 1 components
        JLabel player1Label = new JLabel("First player's name:");
        player1Label.setFont(new Font("Arial", Font.BOLD, 14));
        textFieldP1 = new JTextField(20);
        temp.add(player1Label);
        temp.add(textFieldP1);
    
        JPanel player1ColorPanel = new JPanel();
        player1ColorPanel.setBorder(BorderFactory.createTitledBorder("Player 1 Color"));
    
        b1 = new JRadioButton("Red");
        b2 = new JRadioButton("Green");
        b3 = new JRadioButton("Magenta");
        b4 = new JRadioButton("Cyan");
        ButtonGroup bg = new ButtonGroup();
        bg.add(b1);bg.add(b2);bg.add(b3);bg.add(b4);
        
        temp.add(b1);
        temp.add(b2);
        temp.add(b3);
        temp.add(b4);
        
        
        // Adding Player 2 components
        JLabel player2Label = new JLabel("Second player's name:");
        player2Label.setFont(new Font("Arial", Font.BOLD, 14));
        textFieldP2 = new JTextField(20);
        temp.add(player2Label);
        temp.add(textFieldP2);
    
        JPanel player2ColorPanel = new JPanel();
        player2ColorPanel.setBorder(BorderFactory.createTitledBorder("Player 2 Color"));

        b5 = new JRadioButton("Red");
        b6 = new JRadioButton("Green");
        b7 = new JRadioButton("Magenta");
        b8 = new JRadioButton("Cyan");
        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(b5);bg2.add(b6);bg2.add(b7);bg2.add(b8);

        temp.add(b5);
        temp.add(b6);
        temp.add(b7);
        temp.add(b8);
        
        
    // Buttons Panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(new Color(173, 216, 230));
    doneButton = new JButton("Start");
    leaderBoardButton = new JButton("LeaderBoard");

    // Styling buttons
    doneButton.setFont(new Font("Arial", Font.PLAIN, 12));
    leaderBoardButton.setFont(new Font("Arial", Font.PLAIN, 12));

    buttonPanel.add(doneButton);
    buttonPanel.add(leaderBoardButton);

    // Adding panels to the frame
    startMenu.add(temp, BorderLayout.CENTER);
    startMenu.add(buttonPanel, BorderLayout.SOUTH);

    startMenu.setVisible(true);
        
        
         doneButton.addActionListener(e -> startGame());
    }
    
    
    private void initializeGame() {
        game = new Game();
    }
    
    private void initializeWindow() {
        setTitle("Tron - The Classic Arcade Game");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("res/bgPattern.jpg");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        setLayout(new BorderLayout(0, 10));
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Tron Game");
        JMenu menuGameLevel = new JMenu("All levels");
        JMenu menuGameScale = new JMenu("Zoom");

        lvlMenuItems(menuGameLevel);
        scalingItems(menuGameScale, 1.0, 2.0, 0.5);

        JMenuItem menuGameExit = new JMenuItem("Exit");
        menuGameExit.addActionListener(e -> System.exit(0));

        menuGame.add(menuGameLevel);
        menuGame.add(menuGameScale);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);

        menuBar.add(menuGame);
        setJMenuBar(menuBar);
    }
    
    private void lvlMenuItems(JMenu menu) {
    for (String difficulty : game.getDifficulties()) {
        JMenu difficultyMenu = new JMenu(difficulty);
        for (Integer level : game.getLevelsOfDifficulty(difficulty)) {
            JMenuItem item = new JMenuItem("Level " + level);
            item.addActionListener(e -> {
                game.load(new GameID(difficulty, level));
                board.refresh();
                pack();
            });
            difficultyMenu.add(item);
        }
        menu.add(difficultyMenu);
    }
}

    
    private void scalingItems(JMenu menu, double from, double to, double by) {
    for (double scale = from; scale <= to; scale += by) {
        final double fixedScale = scale; // effectively final for use in lambda
        JMenuItem item = new JMenuItem(fixedScale + "x");
        item.addActionListener(e -> {
            if (board.fixScale(fixedScale)) {
                pack();
            }
        });
        menu.add(item);
    }
}

    private void initializeGameStatLabel() {
        gameStat = new JLabel("Game Started");
        // Additional styling for the label can be done here
        add(gameStat, BorderLayout.NORTH);
    }

    private void initializeBoard() throws IOException {
        board = new Board(game);
        add(board, BorderLayout.CENTER);
    }

     
    private long elapsedTimeM(){
        return System.currentTimeMillis();
    }
    
    // Method to start the game timer
    private void startGameTimer() {
       startTime = System.currentTimeMillis();
       timer = new Timer(16, e -> {
            gameStat.setText(elapsedTimeM() - startTime + " ms");
    });
    startTime = System.currentTimeMillis();
    timer.start();
    }   
     
    
      
    // Method to handle game start logic
    private void startGame() {
    if ((b5.isSelected() || b6.isSelected() || b7.isSelected() || b8.isSelected()) && (b1.isSelected() || b2.isSelected() || b3.isSelected() || b4.isSelected())) {
        Player1Name = textFieldP1.getText();
        Player2Name = textFieldP2.getText();
        setPlayerColors();

        if (Player1Color != Player2Color) {
            startGameTimer();
            if (currentLevel <= maxLevel) {
                game.load(new GameID("EASY", currentLevel)); // Load the current level
                board.refresh();
                setVisible(true);
                startMenu.setVisible(false);
                new Timer(800, taskPerformer).start();
            } else {
                showLeaderboard(); // Show leaderboard if max level exceeded
            }
        }
    }
}

// Method to set player colors based on selection
    private void setPlayerColors() {
        Player2Color = getColorFromSelection(new JRadioButton[]{b1, b2, b3, b4});
        Player1Color = getColorFromSelection(new JRadioButton[]{b5, b6, b7, b8});
}

// Helper method to get color from radio button selection
    private Color getColorFromSelection(JRadioButton[] buttons) {
        for (JRadioButton button : buttons) {
            if (button.isSelected()) {
                switch (button.getText()) {
                    case "Red" -> {
                        return Color.RED;
                    }
                    case "Green" -> {
                        return Color.GREEN;
                    }
                    case "Magenta" -> {
                        return Color.MAGENTA;
                    }
                    case "Cyan" -> {
                        return Color.CYAN;
                    }
            }   
        }
    }
    return null; // Default case if no selection is made
}

    /**
     * This function will be called again and again every 1 sec and it will move the players to their corresponding direction. It will also 
     * choose the right name of the image which will be showed on the map.
     */
    ActionListener taskPerformer = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        updateGame();
    }
    };
    
    private void updateGame() {
            if (isGamePaused) {
            return; // Skip updating the game when paused
            }

            elapsedTime++;
            //oneSecPassed()
            try{
                // Set the player images to the current direction
        board.setP1IMG(D1);
        board.setP2IMG(D2);

        // Retrieve and update positions of players
        updatePlayerPositions();

        // Refresh the board
        board.refresh();
    } catch (Exception e) {
        System.out.println("Something went wrong! " + e.getMessage());
    }
    }
    
    private void updatePlayerPositions() {
         
        Position p1 = game.getPlayer1Pos();
        Position p2 = game.getPlayer2Pos();

        if (!board.isValidPosP1()) {
            declareWinner(Player2Name, Player1Name);
        return;
         }

        if (!board.isValidPosP2()) {
            declareWinner(Player1Name, Player2Name);
        return;
        }

        updatePlayerDirectionAndColor(D1prev, D1, p1, Player1Color);
        updatePlayerDirectionAndColor(D2prev, D2, p2, Player2Color);

        D1prev = D1;
        D2prev = D2;

       game.move1(D1);
       game.move2(D2);
    }
            
    
    private void declareWinner(String winner, String loser) {
    // Increment the score of the winner
    if (winner.equals(Player1Name)) {
        player1Score++;
    } else if (winner.equals(Player2Name)) {
        player2Score++;
    }

    // Print scores for debugging
    System.out.println("Player 1 Score: " + player1Score + ", Player 2 Score: " + player2Score);

    // Determine the winner's current score
    int winnerScore = winner.equals(Player1Name) ? player1Score : player2Score;

// Update the winner's cumulative score in the database
try {
    dbSQL.upsertWinnerScore(winner);
    System.out.println("Updated " + winner + " with an additional point");
} catch (Exception e) {
    System.out.println("Exception during DB upsert: " + e);
}


    // Check if the final level is reached
    if (currentLevel >= maxLevel) {
        showLeaderboard();
    } else {
        // Advance to the next level
        JOptionPane.showMessageDialog(MainWindow.this, "Congratulations! " + winner + " has won! Moving to next level.", "Level Completed", JOptionPane.INFORMATION_MESSAGE);
        currentLevel++;
        game.load(new GameID("EASY", currentLevel));
        board.refresh();
    }
}

    
    private void showLeaderboard() {
    // Display leaderboard here, using player1Score and player2Score
    String leaderboardMessage = "Final Scores\n" + Player1Name + ": " + player1Score + "\n" + Player2Name + ": " + player2Score;
    JOptionPane.showMessageDialog(MainWindow.this, leaderboardMessage, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    dbSQL.leaderboard();

    // Optionally, you can decide to close the database connection here, if it's no longer needed
    dbSQL.closeConnection();

    // Exit after showing both leaderboards
    System.exit(0);
}

private void updatePlayerDirectionAndColor(Direction oldDirection, Direction newDirection, Position position, Color color) {
    String colorDirection = getColorDirection(oldDirection, newDirection, color);
    if (colorDirection != null) {
        game.ColorToField(position.getY(), position.getX(), colorDirection);
    }
}


private String getColorDirection(Direction oldDir, Direction newDir, Color color) {
    if (oldDir == null || newDir == null) return null;

    String prefix = getColorPrefix(color);
    if (prefix == null) return null;
    
    // Mapping of old and new directions to LevelItem enum names
    Map<Pair<Direction, Direction>, String> directionMap = new HashMap<>();
    directionMap.put(new Pair<>(Direction.UP, Direction.LEFT), prefix + "DownToLeft");
    directionMap.put(new Pair<>(Direction.UP, Direction.RIGHT), prefix + "DownToRight");
    directionMap.put(new Pair<>(Direction.DOWN, Direction.LEFT), prefix + "TopToLeft");
    directionMap.put(new Pair<>(Direction.DOWN, Direction.RIGHT), prefix + "TopToRight");
    directionMap.put(new Pair<>(Direction.LEFT, Direction.UP), prefix + "TopToRight");
    directionMap.put(new Pair<>(Direction.LEFT, Direction.DOWN), prefix + "DownToRight");
    directionMap.put(new Pair<>(Direction.RIGHT, Direction.UP), prefix + "TopToLeft");
    directionMap.put(new Pair<>(Direction.RIGHT, Direction.DOWN), prefix + "DownToLeft");
    directionMap.put(new Pair<>(Direction.UP, Direction.UP), prefix + "Vertical");
    directionMap.put(new Pair<>(Direction.DOWN, Direction.DOWN), prefix + "Vertical");
    directionMap.put(new Pair<>(Direction.LEFT, Direction.LEFT), prefix + "Horizontal");
    directionMap.put(new Pair<>(Direction.RIGHT, Direction.RIGHT), prefix + "Horizontal");

    return directionMap.get(new Pair<>(oldDir, newDir));
}

    private String getColorPrefix(Color color) {
        if (color == Color.RED) return "R";
        if (color == Color.GREEN) return "G";
        if (color == Color.CYAN) return "C";
        if (color == Color.MAGENTA) return "M";
        return null;
    }   
}