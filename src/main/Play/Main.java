package main.Play;

import main.Game.HW5Strategy;

import java.io.IOException;

/**
 * Main class for running BlackJack.
 */
public class Main {
    public static final String HW4_FILE = "./src/test/CSVFiles/tests/hw4.csv";

    /**
     * Main function that runs BlackJack.
     * @param args the filepath
     * @throws Exception if the file cannot be read.
     */
    public static void main(String[] args) throws Exception {

        Parser parser = new Parser();
        parser.readFile(HW4_FILE);
        StrategyParser strategyParser = new StrategyParser();
        parser.makeStatBestDecisionTester(strategyParser);


        //playGame(args);
    }

    public static void playGame(String[] args) throws Exception {
//        String filePath = args[0];
//        Parser parser = new Parser();
//        StrategyParser strategyParser = new StrategyParser();
//        parser.readFile(filePath);
//        parser.play(strategyParser);
//        System.out.println("All operations successful. Results at " + filePath.substring(0, filePath.length() - 4)
//                + "-SOLVED.csv");

//        PlayGame playGameHW1 = new PlayGame(100000000, 1);
//        PlayGame playGameHW2 = new PlayGame(100000000, 2);
//        playGameHW1.printMetrics();
//        playGameHW2.printMetrics();
        Parser parser = new Parser();
        parser.readFile(HW4_FILE);
        StrategyParser stratParser = new StrategyParser();
        //HW5Strategy hw5Strategy = new HW5Strategy()
        System.out.println("Running Main...");
        parser.play(stratParser, 1000000);
        System.out.println("Done...");


    }
}
