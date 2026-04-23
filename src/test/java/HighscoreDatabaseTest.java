import hu.nye.highscore.HighscoreDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class HighscoreDatabaseTest {

    private static final String TEST_DB_URL = "jdbc:sqlite:test_highscores.db";
    private HighscoreDatabase highscoreDatabase;

    @BeforeEach
    void setUp() {
        highscoreDatabase = new HighscoreDatabase() {
            @Override
            protected String getDatabaseUrl() {
                return TEST_DB_URL; // Override to use test database
            }
        };
    }

    @AfterEach
    void tearDown() {
        File testDbFile = new File("test_highscores.db");
        if (testDbFile.exists()) {
            assertTrue(testDbFile.delete(), "Failed to delete test database file.");
        }
    }

    @Test
    void testTableCreation() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='highscores';");

            assertTrue(rs.next(), "Highscores table should exist.");
        } catch (Exception e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }

    @Test
    void testCreateOrUpdateHighscore_NewPlayer() {
        highscoreDatabase.createOrUpadteHighscore("John");

        try (Connection conn = DriverManager.getConnection(TEST_DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT player_name, wins FROM highscores WHERE player_name='John';");

            assertTrue(rs.next(), "New player should be added to the database.");
            assertEquals("John", rs.getString("player_name"));
            assertEquals(1, rs.getInt("wins"), "New player should start with 1 win.");
        } catch (Exception e) {
            fail("Database query failed: " + e.getMessage());
        }
    }

    @Test
    void testCreateOrUpdateHighscore_ExistingPlayer() {
        highscoreDatabase.createOrUpadteHighscore("John");
        highscoreDatabase.createOrUpadteHighscore("John");

        try (Connection conn = DriverManager.getConnection(TEST_DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT player_name, wins FROM highscores WHERE player_name='John';");

            assertTrue(rs.next(), "Existing player should still be in the database.");
            assertEquals("John", rs.getString("player_name"));
            assertEquals(2, rs.getInt("wins"), "Win count should be incremented.");
        } catch (Exception e) {
            fail("Database query failed: " + e.getMessage());
        }
    }

    @Test
    void testPrintHighscore() {
        highscoreDatabase.createOrUpadteHighscore("Alice");
        highscoreDatabase.createOrUpadteHighscore("Bob");
        highscoreDatabase.createOrUpadteHighscore("Alice");

        highscoreDatabase.printHighscore();

        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT player_name, wins FROM highscores ORDER BY wins DESC;")) {

            assertTrue(rs.next());
            assertEquals("Alice", rs.getString("player_name"));
            assertEquals(2, rs.getInt("wins"));

            assertTrue(rs.next());
            assertEquals("Bob", rs.getString("player_name"));
            assertEquals(1, rs.getInt("wins"));
        } catch (Exception e) {
            fail("Database query failed: " + e.getMessage());
        }
    }
}
