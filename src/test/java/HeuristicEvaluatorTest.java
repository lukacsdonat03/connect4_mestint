import static org.junit.jupiter.api.Assertions.*;

import hu.nye.ai.HeuristicEvaluator;
import hu.nye.models.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeuristicEvaluatorTest {

    private HeuristicEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new HeuristicEvaluator('O', 'X');
    }

    @Test
    void testEmptyBoardScoreIsZero() {
        GameBoard board = new GameBoard(6, 7);
        int score = evaluator.evaluate(board);
        assertEquals(0, score, "Üres tábla értéke 0 kell legyen.");
    }

    @Test
    void testAiWinningPositionHighScore() {
        GameBoard board = new GameBoard(6, 7);
        // 4 AI korong vízszintesen
        board.placeDisc(1, 'O');
        board.placeDisc(2, 'O');
        board.placeDisc(3, 'O');
        board.placeDisc(4, 'O');

        int score = evaluator.evaluate(board);
        assertTrue(score > 90000, "AI nyerő pozíció nagyon magas pontszámot kell adjon. Kapott: " + score);
    }

    @Test
    void testPlayerWinningPositionLowScore() {
        GameBoard board = new GameBoard(6, 7);
        // 4 ellenfél korong vízszintesen
        board.placeDisc(1, 'X');
        board.placeDisc(2, 'X');
        board.placeDisc(3, 'X');
        board.placeDisc(4, 'X');

        int score = evaluator.evaluate(board);
        assertTrue(score < -90000, "Ellenfél nyerő pozíció nagyon alacsony pontszámot kell adjon. Kapott: " + score);
    }

    @Test
    void testThreeAiDiscsWithEmptyIsPositive() {
        GameBoard board = new GameBoard(6, 7);
        board.placeDisc(1, 'O');
        board.placeDisc(2, 'O');
        board.placeDisc(3, 'O');
        // 4. mező üres → +50 pont az ablaktól

        int score = evaluator.evaluate(board);
        assertTrue(score > 0, "3 AI korong + 1 üres pozitív pontszámot kellene adjon. Kapott: " + score);
    }

    @Test
    void testThreePlayerDiscsWithEmptyIsNegative() {
        GameBoard board = new GameBoard(6, 7);
        // Szélre tesszük, hogy a középső bónusz ne zavarjon
        board.placeDisc(1, 'X');
        board.placeDisc(2, 'X');
        board.placeDisc(3, 'X');
        // 4. mező üres → -80 pont az ablaktól

        int score = evaluator.evaluate(board);
        assertTrue(score < 0, "3 ellenfél korong + 1 üres negatív pontszámot kell adjon. Kapott: " + score);
    }

    @Test
    void testDefensivePriorityHigherThanOffensive() {
        // Az aszimmetria: |−80| > |+50|, tehát a védekezés fontosabb
        GameBoard boardAttack = new GameBoard(6, 7);
        boardAttack.placeDisc(1, 'O');
        boardAttack.placeDisc(2, 'O');
        boardAttack.placeDisc(3, 'O');

        GameBoard boardDefend = new GameBoard(6, 7);
        boardDefend.placeDisc(1, 'X');
        boardDefend.placeDisc(2, 'X');
        boardDefend.placeDisc(3, 'X');

        int attackScore = evaluator.evaluate(boardAttack);
        int defendScore = evaluator.evaluate(boardDefend);

        assertTrue(Math.abs(defendScore) > Math.abs(attackScore),
                "A védekezés (blokkolás) fontosabb kell legyen mint a támadás. " +
                "attack=" + attackScore + ", defend=" + defendScore);
    }

    @Test
    void testEvaluateWindowFourAi() {
        char[] window = {'O', 'O', 'O', 'O'};
        int score = evaluator.evaluateWindow(window);
        assertEquals(100000, score);
    }

    @Test
    void testEvaluateWindowFourPlayer() {
        char[] window = {'X', 'X', 'X', 'X'};
        int score = evaluator.evaluateWindow(window);
        assertEquals(-100000, score);
    }

    @Test
    void testEvaluateWindowMixed() {
        char[] window = {'O', 'X', 'O', '-'};
        int score = evaluator.evaluateWindow(window);
        assertEquals(0, score, "Vegyes ablak (mindkét fél korongja) 0 pontot kell adjon.");
    }
}
