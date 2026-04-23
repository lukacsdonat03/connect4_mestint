package hu.nye.ai;

import hu.nye.models.GameBoard;

import java.util.List;

/**
 * Minimax algoritmus alfa-béta vágással a Connect4 játékhoz.
 */
public class MinimaxAI {

    private final char aiDisc;
    private final char playerDisc;
    private final int maxDepth;
    private final HeuristicEvaluator evaluator;

    /**
     * Létrehozza az AI-t a megadott paraméterekkel.
     *
     * @param aiDisc     Az AI korongja
     * @param playerDisc A játékos korongja
     * @param maxDepth   A keresési mélység
     */
    public MinimaxAI(char aiDisc, char playerDisc, int maxDepth) {
        this.aiDisc = aiDisc;
        this.playerDisc = playerDisc;
        this.maxDepth = maxDepth;
        this.evaluator = new HeuristicEvaluator(aiDisc, playerDisc);
    }

    /**
     * Meghatározza a legjobb oszlopot az AI számára a jelenlegi táblaállapot alapján.
     * Ez a publikus belépési pont — meghívja a minimax algoritmust az összes lehetséges lépésre,
     * és visszaadja a legmagasabb pontszámú oszlopot.
     *
     * @param board A jelenlegi játéktábla.
     * @return A legjobb oszlop indexe (0-indexelt).
     */
    public int getBestColumn(GameBoard board) {
        List<Integer> availableColumns = GameBoardHelper.getAvailableColumns(board);

        if (availableColumns.isEmpty()) {
            return -1;
        }

        int bestScore = Integer.MIN_VALUE;
        int bestCol = availableColumns.get(0);

        for (int col : availableColumns) {
            // Lépés szimulálása: +1 mert a placeDisc 1-indexelt
            board.placeDisc(col + 1, aiDisc);

            // Minimax hívás: a következő szinten a minimalizáló játékos lép
            int score = minimax(board, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            // Lépés visszavonása
            board.removeDisc(col + 1);

            if (score > bestScore) {
                bestScore = score;
                bestCol = col;
            }
        }

        return bestCol;
    }

    /**
     * A minimax rekurzív algoritmus alfa-béta vágással.
     *
     *
     * @param board         A jelenlegi táblaállapot.
     * @param depth         A hátralevő keresési mélység.
     * @param alpha         Az alfa érték (maximalizáló legjobb opciója).
     * @param beta          A béta érték (minimalizáló legjobb opciója).
     * @param isMaximizing  true, ha a maximalizáló (AI) lép; false, ha a minimalizáló (játékos).
     * @return A táblaállapot pontszáma.
     */
    public int minimax(GameBoard board, int depth, int alpha, int beta, boolean isMaximizing) {
        // Terminális állapotok ellenőrzése
        boolean aiWins = board.checkWin(aiDisc);
        boolean playerWins = board.checkWin(playerDisc);
        boolean boardFull = board.isFull();

        if (aiWins) {
            // Mélységgel súlyozzuk: a közelebbi nyerés értékesebb
            return SCORE_WIN + depth;
        }
        if (playerWins) {
            // Mélységgel súlyozzuk: a közelebbi vesztés rosszabb
            return SCORE_LOSS - depth;
        }
        if (boardFull) {
            return 0; // Döntetlen
        }
        if (depth == 0) {
            // Maximális mélység elérve: heurisztikus kiértékelés
            return evaluator.evaluate(board);
        }

        List<Integer> availableColumns = GameBoardHelper.getAvailableColumns(board);

        if (isMaximizing) {
            // AI lép: a lehető legmagasabb pontszámot keresi
            int maxEval = Integer.MIN_VALUE;

            for (int col : availableColumns) {
                board.placeDisc(col + 1, aiDisc);
                int eval = minimax(board, depth - 1, alpha, beta, false);
                board.removeDisc(col + 1);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                // Alfa-béta vágás: a minimalizáló már talált jobb opciót máshol
                if (alpha >= beta) {
                    break;
                }
            }

            return maxEval;
        } else {
            // Játékos lép: a lehető legalacsonyabb pontszámot keresi az AI számára
            int minEval = Integer.MAX_VALUE;

            for (int col : availableColumns) {
                board.placeDisc(col + 1, playerDisc);
                int eval = minimax(board, depth - 1, alpha, beta, true);
                board.removeDisc(col + 1);

                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                // Alfa-béta vágás: a maximalizáló már talált jobb opciót máshol
                if (alpha >= beta) {
                    break;
                }
            }

            return minEval;
        }
    }

    // Nagy pozitív/negatív érték a nyerés/vesztés jelöléséhez
    private static final int SCORE_WIN = 1000000;
    private static final int SCORE_LOSS = -1000000;
}
