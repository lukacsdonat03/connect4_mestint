package hu.nye;

import hu.nye.initializer.GameInitializer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Keep this open throughout the app's lifecycle
        GameInitializer initializer = new GameInitializer(scanner);
        initializer.startMenu();
        scanner.close();
    }
}