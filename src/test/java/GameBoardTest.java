import static org.junit.jupiter.api.Assertions.*;

import hu.nye.models.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBoardTest {

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(6, 7); // Standard Connect4 pálya
    }

    @Test
    void testConstructorAndBoardInitialization() {
        assertEquals(6, gameBoard.getRows());
        assertEquals(7, gameBoard.getColumns());

        char[][] board = gameBoard.getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                assertEquals('-', cell, "Board cells should initially be empty ('-').");
            }
        }
    }

    @Test
    void testPlaceDiscValidMove() {
        assertTrue(gameBoard.placeDisc(1, 'X'), "Placing a disc in a valid column should succeed.");
        assertEquals('X', gameBoard.getBoard()[5][0], "Disc should appear in the lowest empty row of the column.");
    }

    @Test
    void testPlaceDiscInvalidMove() {
        assertFalse(gameBoard.placeDisc(0, 'X'), "Placing a disc in an invalid column (0) should fail.");
        assertFalse(gameBoard.placeDisc(8, 'X'), "Placing a disc in an invalid column (8) should fail.");
    }

    @Test
    void testPlaceDiscColumnFull() {
        for (int i = 0; i < 6; i++) {
            assertTrue(gameBoard.placeDisc(1, 'X'), "Placing discs in a valid column should succeed until full.");
        }
        assertFalse(gameBoard.placeDisc(1, 'X'), "Placing a disc in a full column should fail.");
    }

    @Test
    void testCheckWinHorizontal() {
        gameBoard.placeDisc(1, 'X');
        gameBoard.placeDisc(2, 'X');
        gameBoard.placeDisc(3, 'X');
        gameBoard.placeDisc(4, 'X');
        assertTrue(gameBoard.checkWin('X'), "Four consecutive discs horizontally should be a win.");
    }

    @Test
    void testCheckWinVertical() {
        for (int i = 0; i < 4; i++) {
            gameBoard.placeDisc(1, 'X');
        }
        assertTrue(gameBoard.checkWin('X'), "Four consecutive discs vertically should be a win.");
    }

    @Test
    void testCheckWinDiagonal() {
        gameBoard.placeDisc(1, 'X');
        gameBoard.placeDisc(2, 'O');
        gameBoard.placeDisc(2, 'X');
        gameBoard.placeDisc(3, 'O');
        gameBoard.placeDisc(3, 'O');
        gameBoard.placeDisc(3, 'X');
        gameBoard.placeDisc(4, 'O');
        gameBoard.placeDisc(4, 'O');
        gameBoard.placeDisc(4, 'O');
        gameBoard.placeDisc(4, 'X');
        assertTrue(gameBoard.checkWin('X'), "Four consecutive discs diagonally should be a win.");
    }

    @Test
    void testLoadGameboard() {
        char[][] matrix = {
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', 'X', '-', '-', '-', '-'},
                {'-', '-', 'X', '-', '-', '-', '-'},
                {'-', '-', 'X', '-', '-', '-', '-'},
                {'-', '-', 'X', '-', '-', '-', '-'}
        };
        gameBoard.loadGameboard(matrix);

        assertArrayEquals(matrix, gameBoard.getBoard(), "Game board should match the loaded matrix.");
        assertTrue(gameBoard.checkWin('X'), "Loaded board should detect a vertical win for 'X'.");
    }

    @Test
    void testRemoveDiscValid() {
        gameBoard.placeDisc(1, 'X');
        gameBoard.placeDisc(1, 'O');
        assertTrue(gameBoard.removeDisc(1), "Removing a disc from a non-empty column should succeed.");
        assertEquals('-', gameBoard.getBoard()[4][0], "The topmost disc should be removed.");
        assertEquals('X', gameBoard.getBoard()[5][0], "The bottom disc should remain.");
    }

    @Test
    void testRemoveDiscEmptyColumn() {
        assertFalse(gameBoard.removeDisc(1), "Removing a disc from an empty column should fail.");
    }

    @Test
    void testRemoveDiscInvalidColumn() {
        assertFalse(gameBoard.removeDisc(0), "Removing from column 0 should fail.");
        assertFalse(gameBoard.removeDisc(8), "Removing from column 8 should fail.");
    }

    @Test
    void testIsColumnFullFalse() {
        assertFalse(gameBoard.isColumnFull(0), "Empty column should not be full.");
    }

    @Test
    void testIsColumnFullTrue() {
        for (int i = 0; i < 6; i++) {
            gameBoard.placeDisc(1, 'X');
        }
        assertTrue(gameBoard.isColumnFull(0), "Column with 6 discs should be full.");
    }

    @Test
    void testIsColumnFullInvalid() {
        assertTrue(gameBoard.isColumnFull(-1), "Invalid column returns true (treated as full).");
        assertTrue(gameBoard.isColumnFull(7), "Invalid column returns true (treated as full).");
    }

    @Test
    void testIsFullFalse() {
        assertFalse(gameBoard.isFull(), "Empty board should not be full.");
    }

    @Test
    void testIsFullTrue() {
        for (int col = 1; col <= 7; col++) {
            for (int row = 0; row < 6; row++) {
                gameBoard.placeDisc(col, 'X');
            }
        }
        assertTrue(gameBoard.isFull(), "Board with all cells filled should be full.");
    }
}
