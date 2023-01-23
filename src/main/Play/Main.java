package main.Play;

import java.io.IOException;

/**
 * Main class for running BlackJack.
 */
public class Main {
    /**
     * Main function that runs BlackJack.
     * @param args the filepath
     * @throws Exception if the file cannot be read.
     */
    public static void main(String[] args) throws Exception {
        playGame(args);
    }

    public static void playGame(String[] args) throws Exception {
        String filePath = args[0];
        Parser parser = new Parser();
        parser.readFile(filePath);
        parser.play();
    }
}
