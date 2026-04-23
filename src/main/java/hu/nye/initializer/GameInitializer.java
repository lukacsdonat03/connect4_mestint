package hu.nye.initializer;

import hu.nye.highscore.HighscoreDatabase;
import hu.nye.models.Game;

import java.util.Scanner;

public class GameInitializer {

    private final Scanner sc;

    public GameInitializer(Scanner sc) {
        this.sc = sc;
    }

    public Game initializeGame() {
        String playerName = promptForPlayerName();
        Game game = loadGameOrSetupNewGame(playerName);
        return game;
    }

    private void menu() {
        System.out.println("\t MENU");
        System.out.println("Play game (p)");
        System.out.println("Highscore (h)");
        System.out.println("Exit (e)");
    }

    public void startMenu() {
        String option;
        do {
            menu();
            option = sc.nextLine().trim().toLowerCase();

            switch (option) {
                case "h":
                    HighscoreDatabase hdb = new HighscoreDatabase();
                    hdb.printHighscore();
                    break;
                case "p":
                    String playerName = promptForPlayerName();
                    Game game = loadGameOrSetupNewGame(playerName);
                    game.start();
                    return;
                case "e":
                    System.out.println("Exiting the game. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 'p' to play, 'h' for highscore, or 'e' to exit.");
            }
        } while (!option.equals("e") );
    }

    private String promptForPlayerName() {
        System.out.print("Type the name of the player: ");
        return sc.nextLine();
    }

    public Game loadGameOrSetupNewGame(String playerName) {
        boolean save = false;
        String saveFile = "";
        System.out.print("Do you want to create a save for the game? (yes/no): ");
        String saveResponse = sc.nextLine().trim().toLowerCase();

        if (saveResponse.equals("yes")) {
            System.out.print("Enter the file name: ");
            saveFile = sc.nextLine();
            save = true;
        }

        System.out.print("Do you want to load a game from a file? (yes/no): ");
        String loadResponse = sc.nextLine().trim().toLowerCase();

        if (loadResponse.equals("yes")) {
            System.out.print("Enter the filename: ");
            String filename = sc.nextLine();
            Game loadedGame = new Game(playerName, filename, save, saveFile);

            if (loadedGame.getBoard() != null) {
                return loadedGame;
            } else {
                System.out.println("The given file was not found or could not be loaded.");
            }
        }

        int rows = Game.getValidDimension(sc, "rows", 4, 12);
        int cols = Game.getValidDimension(sc, "columns", 4, rows);
        return new Game(rows, cols, playerName, save, saveFile);
    }
}
