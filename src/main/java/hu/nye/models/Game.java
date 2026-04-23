package hu.nye.models;

import hu.nye.highscore.HighscoreDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private GameBoard board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private boolean saveGame;
    private String saveFile;

    private final HighscoreDatabase highscoreDatabase;

    public Game(int row, int col, String name, boolean save, String saveFile) {
        this.board = new GameBoard(row, col);
        this.player1 = new Player(name, 'X');
        this.player2 = new Player("Opponent", 'O');
        this.currentPlayer = this.player1;
        this.saveGame = save;
        this.saveFile = saveFile;
        this.highscoreDatabase = new HighscoreDatabase();
    }

    public Game(String name, String filename, boolean save, String saveFile) {
        this.player1 = new Player(name, 'X');
        this.player2 = new Player("Opponent", 'O');
        this.currentPlayer = this.player1;
        this.board = this.loadFromFile(filename);
        this.saveGame = save;
        this.saveFile = saveFile;
        this.highscoreDatabase = new HighscoreDatabase();
    }

    /**
     * Elinditja és kezeli a játékmenetet
     * A játékosok felváltva tesznek lépéseket, amíg a valamelyik nem nyer vagy a játék véget nem ér
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The game started!");

        this.board.printBoard();
        if (this.board.checkWin(this.currentPlayer.getDisc())) {
            System.out.println(this.currentPlayer.getName() + " wins!");
            if (this.currentPlayer == this.player1) {
                this.highscoreDatabase.createOrUpadteHighscore(this.player1.getName());
            }
        } else {
            while (true) {
                System.out.println(this.currentPlayer.getName() + "'s turn (" + this.currentPlayer.getDisc() + ")");
                int column = -1;

                if (this.currentPlayer == this.player1) {
                    System.out.print("Enter column (1-" + (this.board.getColumns()) + "): ");
                    try {
                        column = scanner.nextInt();
                    } catch (InputMismatchException exc) {
                        System.out.println("Invalid input. Please enter a valid number!");
                        scanner.next();
                    }
                } else {
                    column = new Random().nextInt(this.board.getColumns());
                    System.out.println("Opponent chooses column " + (column + 1));
                }

                if (this.board.placeDisc(column, this.currentPlayer.getDisc())) {
                    if (this.saveGame) {
                        this.saveBoardToFile(this.saveFile);
                    }
                    this.board.printBoard();
                    if (this.board.checkWin(this.currentPlayer.getDisc())) {
                        System.out.println(this.currentPlayer.getName() + " wins!");
                        if (this.currentPlayer == this.player1) {
                            this.highscoreDatabase.createOrUpadteHighscore(this.player1.getName());
                        }
                        break;
                    }
                    switchPlayer();
                } else {
                    System.out.println("Invalid move, try again.");
                }
            }
        }
        scanner.close();
    }

    /**
     * Átváltja az aktuális játékost
     */
    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.player1) ? this.player2 : this.player1;
    }

    /**
     * Bekéri és érvényesíti a játék méreteit (sorok vagy oszlopok száma) a felhasználótól.
     *
     * @param sc             A {@link Scanner} objektum a felhasználói bemenet olvasásához.
     * @param dimensionName  A méret típusa, például "sorok" vagy "oszlopok".
     * @param min            A méret minimális értéke.
     * @param max            A méret maximális értéke.
     * @return               A felhasználó által megadott érvényes méret.
     */
    public static int getValidDimension(Scanner sc, String dimensionName, int min, int max) {
        int dimension;
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("Number of ")
                .append(dimensionName)
                .append(" (")
                .append(min)
                .append(" <= ")
                .append(dimensionName)
                .append(" <= ")
                .append(max)
                .append("): ");

        String promptMessage = messageBuilder.toString();

        while (true) {
            System.out.print(promptMessage);
            dimension = sc.nextInt();

            if (dimension >= min && dimension <= max) {
                break;
            } else {
                System.out.println("Invalid input. " + dimensionName + " must be between " + min + " and " + max + ".");
            }
        }
        return dimension;
    }

    /**
     * Egy játéktábla betöltése egy megadott fájlból.
     *
     * @param filename A fájl neve, amelyből a táblát be kell tölteni.
     * @return A betöltött játéktábla, vagy null, ha a betöltés sikertelen.
     */
    public GameBoard loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            char[][] matrix = new char[lines.size()][];
            int singleLineSize = lines.get(0).toCharArray().length;
            for (int row = 0; row < lines.size(); row++) {
                char[] line = lines.get(row).toCharArray();
                matrix[row] = line;
            }
            this.board = new GameBoard(lines.size(), singleLineSize);
            this.board.loadGameboard(matrix);
            return this.board;
        } catch (IOException e) {
            System.out.println("Failed to open the file: " + e.getMessage());
            return null;
        }
    }

    /**
     * A játéktábla aktuális állapotának mentése egy megadott fájlba.
     *
     * @param filename A fájl neve, amelybe a táblát menteni kell.
     */
    public void saveBoardToFile(String filename) {
        try {
            Files.write(Paths.get(filename), this.saveBoardToString().getBytes());
            System.out.println("The board has been successfully saved");
        } catch (IOException e) {
            System.out.println("Failed to save the board to file: " + filename);
        }
    }

    /**
     * A játéktábla állapotának szöveges reprezentációját adja vissza.
     *
     * @return A játéktábla szöveges formában.
     */
    public String saveBoardToString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : this.getBoard().getBoard()) {
            for (char cell : row) {
                sb.append(cell);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Kinyomtatja a jelenlegi legjobb eredményeket.
     */
    public void printHighscore() {
        this.highscoreDatabase.printHighscore();
    }

    public GameBoard getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isSaveGame() {
        return saveGame;
    }

    public void setSaveGame(boolean saveGame) {
        this.saveGame = saveGame;
    }
}
