package test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import main.Play.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class MainTest {

    private static final String userDir = System.getProperty("user.dir");

    private static final String[] fileContentsArray
            = new String[]{",1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n",
            ",1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n,1F0C8,,,,,,,1F0B5,1F0D1\n",

            ",1f0bb,,,,,,,1f0b1,1f0d1,1f0a2,1f0b4,1f0ab\n" +
            "==> Dealer_hand={J♥},  My_hand ={A♥, A♣, 2♠, 4♥, J♠},  Other_players_hands=[]\n" +
            ",1f0bb,1f0d7,1f0db,,,,,1f0a1,1f0a2,1f0b7,1f0c9\n" +
            "==> Dealer_hand={J♥},  My_hand ={A♠, 2♠, 7♥, 9♦},  Other_players_hands=[{7♣, J♣}]\n" +
            ",1f0bd,1f0a5,1f0b5,1f0a1,1f0c1,1f0d5,1f0aa,1f0b1,1f0c7\n" +
            "==> Dealer_hand={Q♥},  My_hand ={A♥, 7♦},  Other_players_hands=[{5♠, 5♥}, {A♠, A♦}, {5♣, 10♠}]\n" +
            ",1f0ba,1f0d3,1f0a8,1f0c5,1f0b9,1f0be,1f0ce,1f0b1,1f0b3,1f0a4,1f0c6,1f0d7\n" +
            "==> Dealer_hand={10♥},  My_hand ={A♥, 3♥, 4♠, 6♦, 7♣},  Other_players_hands=[{3♣, 8♠}, {5♦, 9♥}, {K♥, K♦}]\n" +
            ",1f0a5,1f0d1,1f0ba,,,,,1f0a2,1f0a4,1f0c5,1f0b8\n" +
            "==> Dealer_hand={5♠},  My_hand ={2♠, 4♠, 5♦, 8♥},  Other_players_hands=[{A♣, 10♥}]\n" +
            ",1f0a7,1f0ba,1f0ce,1f0b3,1f0c4,,,1f0b2,1f0b5,1f0ca\n" +
            "==> Dealer_hand={7♠},  My_hand ={2♥, 5♥, 10♦},  Other_players_hands=[{10♥, K♦}, {3♥, 4♦}]\n" +
            ",1f0da,1f0aa,1f0ab,1f0bb,1f0cd,1f0b1,1f0ae,1f0a1,1f0b4\n" +
            "==> Dealer_hand={10♣},  My_hand ={A♠, 4♥},  Other_players_hands=[{10♠, J♠}, {J♥, Q♦}, {A♥, K♠}]\n" +
            ",1f0be,,,,,,,1f0b2,1f0b5\n" +
            "==> Dealer_hand={K♥},  My_hand ={2♥, 5♥},  Other_players_hands=[]\n" +
            ",1f0d3,1f0d6,1f0ce,1f0d4,1f0a5,1f0b8,1f0be,1f0b3,1f0c5\n" +
            "==> Dealer_hand={3♣},  My_hand ={3♥, 5♦},  Other_players_hands=[{6♣, K♦}, {4♣, 5♠}, {8♥, K♥}]\n" +
            ",1f0d5,1f0c8,1f0bd,1f0b7,1f0bb,,,1f0a2,1f0c6\n" +
            "==> Dealer_hand={5♣},  My_hand ={2♠, 6♦},  Other_players_hands=[{8♦, Q♥}, {7♥, J♥}]\n" +
            ",1f0ca,1f0c7,1f0bb,1f0b4,1f0dd,1f0c3,1f0db,1f0b2,1f0a9\n" +
            "==> Dealer_hand={10♦},  My_hand ={2♥, 9♠},  Other_players_hands=[{7♦, J♥}, {4♥, Q♣}, {3♦, J♣}]"};

    private static final String[] expectedContentsArray
            = new String[]{"STAY,1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n",
            "STAY,1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\nHIT,1F0C8,,,,,,,1F0B5,1F0D1\n",

            "STAY,1f0bb,,,,,,,1f0b1,1f0d1,1f0a2,1f0b4,1f0ab\n" +
            "==> Dealer_hand={J♥},  My_hand ={A♥, A♣, 2♠, 4♥, J♠},  Other_players_hands=[]\n" +
            "STAY,1f0bb,1f0d7,1f0db,,,,,1f0a1,1f0a2,1f0b7,1f0c9\n" +
            "==> Dealer_hand={J♥},  My_hand ={A♠, 2♠, 7♥, 9♦},  Other_players_hands=[{7♣, J♣}]\n" +
            "STAY,1f0bd,1f0a5,1f0b5,1f0a1,1f0c1,1f0d5,1f0aa,1f0b1,1f0c7\n" +
            "==> Dealer_hand={Q♥},  My_hand ={A♥, 7♦},  Other_players_hands=[{5♠, 5♥}, {A♠, A♦}, {5♣, 10♠}]\n" +
            "STAY,1f0ba,1f0d3,1f0a8,1f0c5,1f0b9,1f0be,1f0ce,1f0b1,1f0b3,1f0a4,1f0c6,1f0d7\n" +
            "==> Dealer_hand={10♥},  My_hand ={A♥, 3♥, 4♠, 6♦, 7♣},  Other_players_hands=[{3♣, 8♠}, {5♦, 9♥}, {K♥, K♦}]\n" +
            "STAY,1f0a5,1f0d1,1f0ba,,,,,1f0a2,1f0a4,1f0c5,1f0b8\n" +
            "==> Dealer_hand={5♠},  My_hand ={2♠, 4♠, 5♦, 8♥},  Other_players_hands=[{A♣, 10♥}]\n" +
            "STAY,1f0a7,1f0ba,1f0ce,1f0b3,1f0c4,,,1f0b2,1f0b5,1f0ca\n" +
            "==> Dealer_hand={7♠},  My_hand ={2♥, 5♥, 10♦},  Other_players_hands=[{10♥, K♦}, {3♥, 4♦}]\n" +
            "HIT,1f0da,1f0aa,1f0ab,1f0bb,1f0cd,1f0b1,1f0ae,1f0a1,1f0b4\n" +
            "==> Dealer_hand={10♣},  My_hand ={A♠, 4♥},  Other_players_hands=[{10♠, J♠}, {J♥, Q♦}, {A♥, K♠}]\n" +
            "HIT,1f0be,,,,,,,1f0b2,1f0b5\n" +
            "==> Dealer_hand={K♥},  My_hand ={2♥, 5♥},  Other_players_hands=[]\n" +
            "HIT,1f0d3,1f0d6,1f0ce,1f0d4,1f0a5,1f0b8,1f0be,1f0b3,1f0c5\n" +
            "==> Dealer_hand={3♣},  My_hand ={3♥, 5♦},  Other_players_hands=[{6♣, K♦}, {4♣, 5♠}, {8♥, K♥}]\n" +
            "HIT,1f0d5,1f0c8,1f0bd,1f0b7,1f0bb,,,1f0a2,1f0c6\n" +
            "==> Dealer_hand={5♣},  My_hand ={2♠, 6♦},  Other_players_hands=[{8♦, Q♥}, {7♥, J♥}]\n" +
            "HIT,1f0ca,1f0c7,1f0bb,1f0b4,1f0dd,1f0c3,1f0db,1f0b2,1f0a9\n" +
            "==> Dealer_hand={10♦},  My_hand ={2♥, 9♠},  Other_players_hands=[{7♦, J♥}, {4♥, Q♣}, {3♦, J♣}]"};


    @AfterAll
    static void cleanUp() throws IOException {
        for (int i = 0; i < 3; i++) {
            writeFileContents(i);
        }
    }

    @Test
    void testCSV0() throws Exception {
        Main.playGame(new String[]{"src/test/CSVFiles/tests/f0.csv"});
        String filePath = userDir + "/src/test/CSVFiles/tests/f0.csv";
        String expectedContents = expectedContentsArray[0];

        String fileContents = Files.readString(Paths.get(filePath));
        assertEquals(expectedContents, fileContents);
    }

    @Test
    void testCSV1() throws Exception {
        Main.playGame(new String[]{"src/test/CSVFiles/tests/f1.csv"});
        String filePath = userDir + "/src/test/CSVFiles/tests/f1.csv";
        String expectedContents = expectedContentsArray[1];

        String fileContents = Files.readString(Paths.get(filePath));
        assertEquals(expectedContents, fileContents);
    }

    @Test
    void testCSV2() throws Exception {
        Main.playGame(new String[]{"src/test/CSVFiles/tests/f2.csv"});
        String filePath = userDir + "/src/test/CSVFiles/tests/f2.csv";
        String expectedContents = expectedContentsArray[2];
        String fileContents = Files.readString(Paths.get(filePath));
        assertEquals(expectedContents, fileContents);
    }


    private static void writeFileContents(int fileNum) throws IOException {
        String filePath = userDir + "/src/test/CSVFiles/tests/f" + fileNum + ".csv";
        String fileContents = fileContentsArray[fileNum];
        FileWriter writer = new FileWriter(filePath);
        writer.write(fileContents);
        writer.close();

    }

//    private String makeDecision(String filePath) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        //Adds each line (each game) of the file as a new element in an ArrayList.
//        ArrayList<String> listOfGames = new ArrayList<>();
//        String line;
//        int lineNum = 1;
//        while ((line = reader.readLine()) != null){
//            if (lineNum % 2 == 0) {
//                listOfGames.add(line);
//            }
//            lineNum++;
//        }
//        reader.close();
//
//        for (String game : listOfGames) {
//            int start = game.indexOf("My_hand =");
//            start += 9;
//            String resStr = game.substring(start);
//            int end = resStr.indexOf("Other");
//            end -= 4;
//            String finalStr = game.substring(0, end + 1);
//            String[] cards = game.split(",");
//            for (String card : cards) {
//                card = card.strip();
//            }
//        }
//    }

}
