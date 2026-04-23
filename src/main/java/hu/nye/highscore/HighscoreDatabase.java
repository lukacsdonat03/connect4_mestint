package hu.nye.highscore;

import java.sql.*;

public class HighscoreDatabase {

    private static final String URL = "jdbc:sqlite:highscores.db";

    public HighscoreDatabase() {
        try(Connection conn = DriverManager.getConnection(this.getDatabaseUrl())){
            if(conn != null){
                String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS highscores (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        player_name TEXT NOT NULL,
                        wins INTEGER NOT NULL
                    );
                    """;
                Statement stmt = conn.createStatement();
                stmt.execute(createTableSQL);
                System.out.println("Highscores table created or already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to initialize database: "+ e.getMessage());
        }
    }

    /**
     * Létrehozza egy játékos nevét az adatbázishoz, vagy frissíti a győzelmeinek számát, ha már létezik.
     *
     * @param playerName A játékos neve, akinek az eredményét hozzá kell adni vagy frissíteni kell.
     */
    public void createOrUpadteHighscore(String playerName){
        String checkSql = "SELECT wins FROM highscores WHERE player_name = ?";
        String updateSql = "UPDATE highscores SET wins = wins+1 where player_name = ?";
        String insertSql = "INSERT INTO highscores(player_name, wins) VALUES (?,1)";

        try (Connection conn = DriverManager.getConnection(this.getDatabaseUrl());
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Check if the player already exists
            checkStmt.setString(1, playerName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                updateStmt.setString(1, playerName);
                updateStmt.executeUpdate();
                System.out.println("Incremented win count for player: " + playerName);
            } else {
                insertStmt.setString(1, playerName);
                insertStmt.executeUpdate();
                System.out.println("Added new player with 1 win: " + playerName);
            }

        } catch (SQLException e) {
            System.out.println("Failed to add or update highscore: " + e.getMessage());
        }
    }

    /**
     * Kilistázza a ranglistát az adatbázisból a győzelmek száma alapján csökkenő sorrendben.
     *
     * A ranglista tartalmazza a játékos nevét és a győzelmeinek számát.
     */
    public void printHighscore(){
        String query = "SELECT player_name, wins FROM highscores ORDER BY wins DESC";
        try (Connection conn = DriverManager.getConnection(this.getDatabaseUrl());
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Highscores:");
            System.out.println("Name | wins");
            while (rs.next()) {
                System.out.println(rs.getString("player_name") + " | " + rs.getInt("wins"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve highscores: " + e.getMessage());
        }
    }


    protected String getDatabaseUrl() {
        return URL;
    }
}
