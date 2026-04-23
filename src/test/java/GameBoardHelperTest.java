import static org.junit.jupiter.api.Assertions.*;

import hu.nye.ai.GameBoardHelper;
import hu.nye.models.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class GameBoardHelperTest {

    private GameBoard board;

    @BeforeEach
    void setUp() {
        board = new GameBoard(6, 7);
    }

    @Test
    void testGetAvailableColumnsEmptyBoard() {
        List<Integer> available = GameBoardHelper.getAvailableColumns(board);
        assertEquals(7, available.size(), "Üres táblán minden oszlop elérhető.");
        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6), available);
    }

    @Test
    void testGetAvailableColumnsOneColumnFull() {
        // Az 1-es oszlopot (0-indexelt) teletöltjük
        for (int i = 0; i < 6; i++) {
            board.placeDisc(1, 'X');
        }
        List<Integer> available = GameBoardHelper.getAvailableColumns(board);
        assertEquals(6, available.size(), "Egy teli oszloppal eggyel kevesebb elérhető.");
        assertFalse(available.contains(0), "A teli oszlop nem szerepelhet a listában.");
    }

    @Test
    void testGetAvailableColumnsFullBoard() {
        // Minden oszlopot teletöltünk
        for (int col = 1; col <= 7; col++) {
            for (int row = 0; row < 6; row++) {
                board.placeDisc(col, 'X');
            }
        }
        List<Integer> available = GameBoardHelper.getAvailableColumns(board);
        assertTrue(available.isEmpty(), "Teli táblán nincs elérhető oszlop.");
    }

    @Test
    void testCloneBoardCreatesDeepCopy() {
        board.placeDisc(4, 'X');
        board.placeDisc(4, 'O');

        GameBoard clone = GameBoardHelper.cloneBoard(board);

        // A klón tartalma megegyezik az eredetivel
        assertArrayEquals(board.getBoard()[5], clone.getBoard()[5]);
        assertArrayEquals(board.getBoard()[4], clone.getBoard()[4]);

        // A klón módosítása NEM hat az eredetire
        clone.placeDisc(1, 'O');
        assertEquals('-', board.getBoard()[5][0],
                "A klón módosítása nem szabad, hogy az eredetit érintse.");
        assertEquals('O', clone.getBoard()[5][0]);
    }

    @Test
    void testCloneBoardDimensions() {
        GameBoard clone = GameBoardHelper.cloneBoard(board);
        assertEquals(board.getRows(), clone.getRows());
        assertEquals(board.getColumns(), clone.getColumns());
    }
}
