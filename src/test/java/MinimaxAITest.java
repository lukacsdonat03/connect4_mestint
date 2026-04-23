import static org.junit.jupiter.api.Assertions.*;

import hu.nye.ai.MinimaxAI;
import hu.nye.models.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinimaxAITest {

    private MinimaxAI ai;

    @BeforeEach
    void setUp() {
        ai = new MinimaxAI('O', 'X', 6);
    }

    @Test
    void testAiChoosesWinningMove() {
        GameBoard board = new GameBoard(6, 7);
        // AI-nak 3 korongja van egymás mellett, a 4. helyre kell tennie
        board.placeDisc(1, 'O');
        board.placeDisc(2, 'O');
        board.placeDisc(3, 'O');
        // A 4-es oszlopba (0-indexelt: 3) kell tennie

        int bestCol = ai.getBestColumn(board);
        assertEquals(3, bestCol, "Az AI-nak a nyerő oszlopot kell választania (0-indexelt: 3).");
    }

    @Test
    void testAiBlocksOpponentWin() {
        GameBoard board = new GameBoard(6, 7);
        // Ellenfélnek 3 korongja van, az AI-nak blokkolnia kell
        board.placeDisc(1, 'X');
        board.placeDisc(2, 'X');
        board.placeDisc(3, 'X');
        // A 4-es oszlopba (0-indexelt: 3) kell blokkolnia

        int bestCol = ai.getBestColumn(board);
        assertEquals(3, bestCol, "Az AI-nak blokkolnia kell az ellenfelet (0-indexelt: 3).");
    }

    @Test
    void testAiChoosesValidColumnOnEmptyBoard() {
        GameBoard board = new GameBoard(6, 7);

        int bestCol = ai.getBestColumn(board);
        assertTrue(bestCol >= 0 && bestCol < 7,
                "Az AI-nak érvényes oszlopot kell választania. Kapott: " + bestCol);
    }

    @Test
    void testAiPrefersCenter() {
        GameBoard board = new GameBoard(6, 7);

        int bestCol = ai.getBestColumn(board);
        // Az AI a középső oszlopot (3) preferálja a center bónusz miatt
        assertEquals(3, bestCol, "Üres táblán az AI a középső oszlopot kellene válassza.");
    }

    @Test
    void testAiChoosesOnlyAvailableColumn() {
        GameBoard board = new GameBoard(6, 7);
        // Töltsük fel az összes oszlopot kivéve a 5. oszlopot (0-indexelt: 4)
        for (int col = 1; col <= 7; col++) {
            if (col != 5) {
                for (int row = 0; row < 6; row++) {
                    board.placeDisc(col, (row % 2 == 0) ? 'X' : 'O');
                }
            }
        }

        int bestCol = ai.getBestColumn(board);
        assertEquals(4, bestCol, "Ha csak egy oszlop elérhető, azt kell választania.");
    }

    @Test
    void testAiPrefersWinOverBlock() {
        GameBoard board = new GameBoard(6, 7);
        // AI-nak is 3-as sora van, és az ellenfélnek is
        // De az AI nyerhet → a nyerés fontosabb mint a blokkolás
        board.placeDisc(1, 'O');
        board.placeDisc(2, 'O');
        board.placeDisc(3, 'O');
        // AI nyerhet a 4-es oszloppal

        board.placeDisc(5, 'X');
        board.placeDisc(6, 'X');
        board.placeDisc(7, 'X');
        // Ellenfél nyerhet a 4-es oszloppal is, DE az AI-nak előnye van

        int bestCol = ai.getBestColumn(board);
        assertEquals(3, bestCol, "Az AI-nak a saját nyerését kell preferálnia a blokkolás helyett.");
    }

    @Test
    void testMinimaxReturnsZeroForDraw() {
        GameBoard board = new GameBoard(4, 4);
        // Döntetlen pozíció: nincs 4-es sor sehol
        char[][] drawBoard = {
                {'X', 'X', 'O', 'O'},
                {'O', 'O', 'X', 'X'},
                {'X', 'X', 'O', 'O'},
                {'O', 'O', 'X', 'X'}
        };
        board.loadGameboard(drawBoard);

        int score = ai.minimax(board, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        assertEquals(0, score, "Döntetlen pozíció értéke 0 kell legyen.");
    }
}
