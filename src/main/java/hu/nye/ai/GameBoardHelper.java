package hu.nye.ai;

import hu.nye.models.GameBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Segédosztály a játéktábla állapotának lekérdezéséhez.
 */
public class GameBoardHelper {

    /**
     * Visszaadja azoknak az oszlopoknak a listáját, amelyekbe még lehet korongot helyezni.
     */
    public static List<Integer> getAvailableColumns(GameBoard board) {
        List<Integer> available = new ArrayList<>();
        for (int col = 0; col < board.getColumns(); col++) {
            if (!board.isColumnFull(col)) {
                available.add(col);
            }
        }
        return available;
    }

    /**
     * Mély másolatot készít a játéktábláról.
     * Azért szükséges, mert a minimax algoritmus szimulált lépéseket végez,
     * és nem szeretnénk az eredeti táblát módosítani.
     */
    public static GameBoard cloneBoard(GameBoard board) {
        GameBoard clone = new GameBoard(board.getRows(), board.getColumns());
        char[][] originalMatrix = board.getBoard();
        char[][] cloneMatrix = new char[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            System.arraycopy(originalMatrix[i], 0, cloneMatrix[i], 0, board.getColumns());
        }
        clone.loadGameboard(cloneMatrix);
        return clone;
    }
}
