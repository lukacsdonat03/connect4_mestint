package hu.nye.ai;

import hu.nye.models.GameBoard;

/**
 * Heurisztikus kiértékelő függvény a Connect4 játéktáblához.
 *
 */
public class HeuristicEvaluator {

    private static final int SCORE_FOUR = 100000;
    private static final int SCORE_THREE = 50;
    private static final int SCORE_TWO = 10;
    private static final int SCORE_OPP_THREE = -80;
    private static final int SCORE_OPP_FOUR = -100000;
    private static final int CENTER_BONUS = 30;

    private final char aiDisc;
    private final char playerDisc;

    /**
     * Létrehozza a kiértékelőt
     */
    public HeuristicEvaluator(char aiDisc, char playerDisc) {
        this.aiDisc = aiDisc;
        this.playerDisc = playerDisc;
    }

    /**
     * Kiértékeli a teljes játéktáblát az AI szemszögéből.
     * Magasabb érték = jobb pozíció az AI-nak.
     *
     * @return A tábla heurisztikus értéke (int).
     */
    public int evaluate(GameBoard board) {
        int score = 0;
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getColumns();

        // 1) Középső oszlop bónusz
        score += evaluateCenterColumn(grid, rows, cols);

        // 2) Minden 4-es ablak kiértékelése mind a 4 irányban
        score += evaluateAllWindows(grid, rows, cols);

        return score;
    }

    /**
     * Középső oszlop bónusz számítása.
     */
    public int evaluateCenterColumn(char[][] grid, int rows, int cols) {
        int centerCol = cols / 2;
        int centerCount = 0;
        for (int row = 0; row < rows; row++) {
            if (grid[row][centerCol] == aiDisc) {
                centerCount++;
            }
        }
        return centerCount * CENTER_BONUS;
    }

    /**
     * Végigmegy az összes lehetséges 4 hosszú ablakon mind a 4 irányban,
     * és összegzi a pontszámot.
     */
    public int evaluateAllWindows(char[][] grid, int rows, int cols) {
        int score = 0;

        // Vízszintes ablakok
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                char[] window = {grid[row][col], grid[row][col + 1],
                        grid[row][col + 2], grid[row][col + 3]};
                score += evaluateWindow(window);
            }
        }

        // Függőleges ablakok
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row <= rows - 4; row++) {
                char[] window = {grid[row][col], grid[row + 1][col],
                        grid[row + 2][col], grid[row + 3][col]};
                score += evaluateWindow(window);
            }
        }

        // Átlós ablakok
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                char[] window = {grid[row][col], grid[row + 1][col + 1],
                        grid[row + 2][col + 2], grid[row + 3][col + 3]};
                score += evaluateWindow(window);
            }
        }

        // Átlós ablakok lentről fel
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                char[] window = {grid[row][col], grid[row - 1][col + 1],
                        grid[row - 2][col + 2], grid[row - 3][col + 3]};
                score += evaluateWindow(window);
            }
        }

        return score;
    }


    public int evaluateWindow(char[] window) {
        int aiCount = 0;
        int playerCount = 0;
        int emptyCount = 0;

        for (char cell : window) {
            if (cell == aiDisc) {
                aiCount++;
            } else if (cell == playerDisc) {
                playerCount++;
            } else {
                emptyCount++;
            }
        }

        // Csak akkor pontozunk, ha az ablakban CSAK az egyik fél korongjai vannak (+ üres helyek)
        if (aiCount == 4) {
            return SCORE_FOUR;
        } else if (aiCount == 3 && emptyCount == 1) {
            return SCORE_THREE;
        } else if (aiCount == 2 && emptyCount == 2) {
            return SCORE_TWO;
        } else if (playerCount == 3 && emptyCount == 1) {
            return SCORE_OPP_THREE;
        } else if (playerCount == 4) {
            return SCORE_OPP_FOUR;
        }

        return 0;
    }
}
