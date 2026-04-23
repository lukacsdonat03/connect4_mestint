import static org.junit.jupiter.api.Assertions.*;

import hu.nye.models.Game;
import hu.nye.models.GameBoard;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class GameTest {

    @Test
    void testSwitchPlayer() {
        Game game = new Game(6, 7, "John Doe", false, "");
        assertEquals(game.getPlayer1(), game.getCurrentPlayer());
        game.switchPlayer();
        assertEquals(game.getPlayer2(), game.getCurrentPlayer());
    }

    @Test
    void testGetValidDimensionValidInput() {
        Scanner scanner = new Scanner("5\n");
        int result = Game.getValidDimension(scanner, "rows", 4, 10);
        assertEquals(5, result);
    }

    @Test
    void testGetValidDimensionInvalidInput() {
        Scanner scanner = new Scanner("15\n5\n");
        int result = Game.getValidDimension(scanner, "rows", 4, 10);
        assertEquals(5, result);
    }

    @Test
    void testLoadFromFileValid() throws IOException {
        String testFilePath = "test_game_board.txt";
        Files.write(Paths.get(testFilePath), "XOX\nOXO\n".getBytes());

        Game game = new Game(6, 7, "John Doe", false, "");
        GameBoard board = game.loadFromFile(testFilePath);
        assertNotNull(board);
        assertEquals('X', board.getBoard()[0][0]);

        Files.delete(Paths.get(testFilePath));
    }

    @Test
    void testLoadFromFileInvalid() {
        Game game = new Game(6, 7, "John Doe", false, "");
        GameBoard board = game.loadFromFile("non_existent_file.txt");
        assertNull(board);
    }

    @Test
    void testSaveBoardToString() {
        Game game = new Game(6, 7, "John Doe", false, "");
        game.getBoard().placeDisc(1, 'X');
        String boardString = game.saveBoardToString();
        assertTrue(boardString.contains("X"));
    }
}
