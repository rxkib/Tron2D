package db;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DataBaseSQL {

    private Connection conn;

    public DataBaseSQL() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/trondb?user=root&password=r@kib2684R&serverTimezone=UTC");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void upsertWinnerScore(String playerName) {
        String sql = "INSERT INTO trondb.tronlb (PlayerName, PlayerScore) VALUES (?, 1) " +
                     "ON DUPLICATE KEY UPDATE PlayerScore = PlayerScore + 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Upsert error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void leaderboard() {
        String sql = "SELECT * FROM trondb.tronlb ORDER BY PlayerScore DESC LIMIT 10";
        try (Statement statement = conn.createStatement();
             ResultSet rset = statement.executeQuery(sql)) {
            String[][] results = new String[10][3];
            int i = 0;
            while (rset.next()) {
                results[i][0] = Integer.toString(i + 1);
                results[i][1] = rset.getString("PlayerName");
                results[i][2] = rset.getString("PlayerScore");
                i++;
            }
            showLeaderBoard(results);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Leaderboard retrieval error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showLeaderBoard(String[][] scores) {
        String[] tableContent = {"Position", "PlayerName", "PlayerScore"};
        JTable board = new JTable(scores, tableContent);
        JOptionPane.showMessageDialog(null, new JScrollPane(board), "Best Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

