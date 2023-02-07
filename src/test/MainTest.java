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
            = new String[]{
            ",1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n",

            ",1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n,1F0C8,,,,,,,1F0B5,1F0D1\n",

            """
            ,1f0bb,,,,,,,1f0b1,1f0d1,1f0a2,1f0b4,1f0ab
            ==> Dealer_hand={J♥},  My_hand ={A♥, A♣, 2♠, 4♥, J♠},  Other_players_hands=[]
            ,1f0bb,1f0d7,1f0db,,,,,1f0a1,1f0a2,1f0b7,1f0c9
            ==> Dealer_hand={J♥},  My_hand ={A♠, 2♠, 7♥, 9♦},  Other_players_hands=[{7♣, J♣}]
            ,1f0bd,1f0a5,1f0b5,1f0a1,1f0c1,1f0d5,1f0aa,1f0b1,1f0c7
            ==> Dealer_hand={Q♥},  My_hand ={A♥, 7♦},  Other_players_hands=[{5♠, 5♥}, {A♠, A♦}, {5♣, 10♠}]
            ,1f0ba,1f0d3,1f0a8,1f0c5,1f0b9,1f0be,1f0ce,1f0b1,1f0b3,1f0a4,1f0c6,1f0d7
            ==> Dealer_hand={10♥},  My_hand ={A♥, 3♥, 4♠, 6♦, 7♣},  Other_players_hands=[{3♣, 8♠}, {5♦, 9♥}, {K♥, K♦}]
            ,1f0a5,1f0d1,1f0ba,,,,,1f0a2,1f0a4,1f0c5,1f0b8
            ==> Dealer_hand={5♠},  My_hand ={2♠, 4♠, 5♦, 8♥},  Other_players_hands=[{A♣, 10♥}]
            ,1f0a7,1f0ba,1f0ce,1f0b3,1f0c4,,,1f0b2,1f0b5,1f0ca
            ==> Dealer_hand={7♠},  My_hand ={2♥, 5♥, 10♦},  Other_players_hands=[{10♥, K♦}, {3♥, 4♦}]
            ,1f0da,1f0aa,1f0ab,1f0bb,1f0cd,1f0b1,1f0ae,1f0a1,1f0b4
            ==> Dealer_hand={10♣},  My_hand ={A♠, 4♥},  Other_players_hands=[{10♠, J♠}, {J♥, Q♦}, {A♥, K♠}]
            ,1f0be,,,,,,,1f0b2,1f0b5
            ==> Dealer_hand={K♥},  My_hand ={2♥, 5♥},  Other_players_hands=[]
            ,1f0d3,1f0d6,1f0ce,1f0d4,1f0a5,1f0b8,1f0be,1f0b3,1f0c5
            ==> Dealer_hand={3♣},  My_hand ={3♥, 5♦},  Other_players_hands=[{6♣, K♦}, {4♣, 5♠}, {8♥, K♥}]
            ,1f0d5,1f0c8,1f0bd,1f0b7,1f0bb,,,1f0a2,1f0c6
            ==> Dealer_hand={5♣},  My_hand ={2♠, 6♦},  Other_players_hands=[{8♦, Q♥}, {7♥, J♥}]
            ,1f0ca,1f0c7,1f0bb,1f0b4,1f0dd,1f0c3,1f0db,1f0b2,1f0a9
            ==> Dealer_hand={10♦},  My_hand ={2♥, 9♠},  Other_players_hands=[{7♦, J♥}, {4♥, Q♣}, {3♦, J♣}]""",

            """
            ,1f0ae,1f0da,1f0cd,1f0b2,1f0a7,,,1f0cb,1f0db
            ==> Dealer_hand={K♠},  My_hand ={J♢, J♣},  Other_players_hands=[{10♣, Q♢}, {2♡, 7♠}]
            ,1f0ab,,,,,,,1f0a6,1f0d6
            ==> Dealer_hand={J♠},  My_hand ={6♠, 6♣},  Other_players_hands=[]
            ,1f0ae,1f0da,1f0cd,1f0b2,1f0a7,,,1f0ce,1f0de
            ==> Dealer_hand={K♠},  My_hand ={K♢, K♣},  Other_players_hands=[{10♣, Q♢}, {2♡, 7♠}]
            ,1f0a5,,,,,,,1f0a6,1f0d6
            ==> Dealer_hand={5♠},  My_hand ={6♠, 6♣},  Other_players_hands=[]
            ,1f0cd,1f0b8,1f0bd,1f0d8,1f0aa,1f0d4,1f0a8,1f0a3,1f0c3,1f0ba
            ==> Dealer_hand={Q♢},  My_hand ={3♠, 3♢, 10♡},  Other_players_hands=[{8♡, Q♡}, {8♣, 10♠}, {4♣, 8♠}]
            ,1f0d2,1f0b5,1f0da,,,,,1f0b1,1f0a8,1f0d8
            ==> Dealer_hand={2♣},  My_hand ={A♡, 8♠, 8♣},  Other_players_hands=[{5♡, 10♣}]
            ,1f0cb,,,,,,,1f0a1,1f0db
            ==> Dealer_hand={J♢},  My_hand ={A♠, J♣},  Other_players_hands=[]
            ,1f0be,1f0db,1f0ce,,,,,1f0a1,1f0c7
            ==> Dealer_hand={K♡},  My_hand ={A♠, 7♢},  Other_players_hands=[{J♣, K♢}]
            ,1f0b3,1f0db,1f0ce,,,,,1f0a1,1f0c7
            ==> Dealer_hand={3♡},  My_hand ={A♠, 7♢},  Other_players_hands=[{J♣, K♢}]
            ,1f0d1,,,,,,,1f0a1,1f0a5
            ==> Dealer_hand={A♣},  My_hand ={A♠, 5♠},  Other_players_hands=[]
            ,1f0aa,1f0c1,1f0a7,1f0a2,1f0a4,1f0b1,1f0c2,1f0a1,1f0a8,1f0bd
            ==> Dealer_hand={10♠},  My_hand ={A♠, 8♠, Q♡},  Other_players_hands=[{A♢, 7♠}, {2♠, 4♠}, {A♡, 2♢}]
            ,1f0bb,1f0b1,1f0c8,1f0d5,1f0a9,1f0b2,1f0b9,1f0a1,1f0d1
            ==> Dealer_hand={J♡},  My_hand ={A♠, A♣},  Other_players_hands=[{A♡, 8♢}, {5♣, 9♠}, {2♡, 9♡}]
            ,1f0a6,,,,,,,1f0a5,1f0d5
            ==> Dealer_hand={6♠},  My_hand ={5♠, 5♣},  Other_players_hands=[]
            ,1f0a1,1f0b1,1f0c8,1f0d5,1f0a9,1f0b2,1f0b9,1f0a8,1f0d8
            ==> Dealer_hand={A♠},  My_hand ={8♠, 8♣},  Other_players_hands=[{A♡, 8♢}, {5♣, 9♠}, {2♡, 9♡}]
            ,1f0c9,1f0cb,1f0ad,,,,,1f0d2,1f0b5
            ==> Dealer_hand={9♢},  My_hand ={2♣, 5♡},  Other_players_hands=[{J♢, Q♠}]
            ,1f0d1,,,,,,,1f0d6,1f0c9
            ==> Dealer_hand={A♣},  My_hand ={6♣, 9♢},  Other_players_hands=[]
            ,1f0be,,,,,,,1f0a2,1f0b2,1f0c2,1f0bd
            ==> Dealer_hand={K♡},  My_hand ={2♠, 2♡, 2♢, Q♡},  Other_players_hands=[]
            ,1f0d9,1f0c8,1f0da,1f0c4,1f0b7,,,1f0c2,1f0a3,1f0b3,1f0c3
            ==> Dealer_hand={9♣},  My_hand ={2♢, 3♠, 3♡, 3♢},  Other_players_hands=[{8♢, 10♣}, {4♢, 7♡}]
            ,1f0a6,,,,,,,1f0c1,1f0b3,1f0a4
            ==> Dealer_hand={6♠},  My_hand ={A♢, 3♡, 4♠},  Other_players_hands=[]
            ,1f0b6,1f0d3,1f0a9,1f0c6,1f0b9,,,1f0c1,1f0c2
            ==> Dealer_hand={6♡},  My_hand ={A♢, 2♢},  Other_players_hands=[{3♣, 9♠}, {6♢, 9♡}]
            ,1f0a6,1f0c3,1f0c9,1f0c8,1f0bd,,,1f0c1,1f0ca
            ==> Dealer_hand={6♠},  My_hand ={A♢, 10♢},  Other_players_hands=[{3♢, 9♢}, {8♢, Q♡}]
            ,1f0d2,1f0ab,1f0de,,,,,1f0a2,1f0d4,1f0b5
            ==> Dealer_hand={2♣},  My_hand ={2♠, 4♣, 5♡},  Other_players_hands=[{J♠, K♣}]
            ,1f0ba,1f0c7,1f0a9,1f0c4,1f0a7,1f0d3,1f0de,1f0b5,1f0b8,1f0c3
            ==> Dealer_hand={10♡},  My_hand ={5♡, 8♡, 3♢},  Other_players_hands=[{7♢, 9♠}, {4♢, 7♠}, {3♣, K♣}]"""
            };

    private static final String[] expectedContentsArray
            = new String[]{
            "STAY,1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n",

            "STAY,1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\nHIT,1F0C8,,,,,,,1F0B5,1F0D1\n",

            """
            STAY,1f0bb,,,,,,,1f0b1,1f0d1,1f0a2,1f0b4,1f0ab
            ==> Dealer_hand={J♥},  My_hand ={A♥, A♣, 2♠, 4♥, J♠},  Other_players_hands=[]
            STAY,1f0bb,1f0d7,1f0db,,,,,1f0a1,1f0a2,1f0b7,1f0c9
            ==> Dealer_hand={J♥},  My_hand ={A♠, 2♠, 7♥, 9♦},  Other_players_hands=[{7♣, J♣}]
            STAY,1f0bd,1f0a5,1f0b5,1f0a1,1f0c1,1f0d5,1f0aa,1f0b1,1f0c7
            ==> Dealer_hand={Q♥},  My_hand ={A♥, 7♦},  Other_players_hands=[{5♠, 5♥}, {A♠, A♦}, {5♣, 10♠}]
            STAY,1f0ba,1f0d3,1f0a8,1f0c5,1f0b9,1f0be,1f0ce,1f0b1,1f0b3,1f0a4,1f0c6,1f0d7
            ==> Dealer_hand={10♥},  My_hand ={A♥, 3♥, 4♠, 6♦, 7♣},  Other_players_hands=[{3♣, 8♠}, {5♦, 9♥}, {K♥, K♦}]
            STAY,1f0a5,1f0d1,1f0ba,,,,,1f0a2,1f0a4,1f0c5,1f0b8
            ==> Dealer_hand={5♠},  My_hand ={2♠, 4♠, 5♦, 8♥},  Other_players_hands=[{A♣, 10♥}]
            STAY,1f0a7,1f0ba,1f0ce,1f0b3,1f0c4,,,1f0b2,1f0b5,1f0ca
            ==> Dealer_hand={7♠},  My_hand ={2♥, 5♥, 10♦},  Other_players_hands=[{10♥, K♦}, {3♥, 4♦}]
            HIT,1f0da,1f0aa,1f0ab,1f0bb,1f0cd,1f0b1,1f0ae,1f0a1,1f0b4
            ==> Dealer_hand={10♣},  My_hand ={A♠, 4♥},  Other_players_hands=[{10♠, J♠}, {J♥, Q♦}, {A♥, K♠}]
            HIT,1f0be,,,,,,,1f0b2,1f0b5
            ==> Dealer_hand={K♥},  My_hand ={2♥, 5♥},  Other_players_hands=[]
            HIT,1f0d3,1f0d6,1f0ce,1f0d4,1f0a5,1f0b8,1f0be,1f0b3,1f0c5
            ==> Dealer_hand={3♣},  My_hand ={3♥, 5♦},  Other_players_hands=[{6♣, K♦}, {4♣, 5♠}, {8♥, K♥}]
            HIT,1f0d5,1f0c8,1f0bd,1f0b7,1f0bb,,,1f0a2,1f0c6
            ==> Dealer_hand={5♣},  My_hand ={2♠, 6♦},  Other_players_hands=[{8♦, Q♥}, {7♥, J♥}]
            HIT,1f0ca,1f0c7,1f0bb,1f0b4,1f0dd,1f0c3,1f0db,1f0b2,1f0a9
            ==> Dealer_hand={10♦},  My_hand ={2♥, 9♠},  Other_players_hands=[{7♦, J♥}, {4♥, Q♣}, {3♦, J♣}]""",

            """
            STAY,1f0ae,1f0da,1f0cd,1f0b2,1f0a7,,,1f0cb,1f0db
            ==> Dealer_hand={K♠},  My_hand ={J♢, J♣},  Other_players_hands=[{10♣, Q♢}, {2♡, 7♠}]
            HIT,1f0ab,,,,,,,1f0a6,1f0d6
            ==> Dealer_hand={J♠},  My_hand ={6♠, 6♣},  Other_players_hands=[]
            STAY,1f0ae,1f0da,1f0cd,1f0b2,1f0a7,,,1f0ce,1f0de
            ==> Dealer_hand={K♠},  My_hand ={K♢, K♣},  Other_players_hands=[{10♣, Q♢}, {2♡, 7♠}]
            SPLIT,1f0a5,,,,,,,1f0a6,1f0d6
            ==> Dealer_hand={5♠},  My_hand ={6♠, 6♣},  Other_players_hands=[]
            HIT,1f0cd,1f0b8,1f0bd,1f0d8,1f0aa,1f0d4,1f0a8,1f0a3,1f0c3,1f0ba
            ==> Dealer_hand={Q♢},  My_hand ={3♠, 3♢, 10♡},  Other_players_hands=[{8♡, Q♡}, {8♣, 10♠}, {4♣, 8♠}]
            STAY,1f0d2,1f0b5,1f0da,,,,,1f0b1,1f0a8,1f0d8
            ==> Dealer_hand={2♣},  My_hand ={A♡, 8♠, 8♣},  Other_players_hands=[{5♡, 10♣}]
            STAY,1f0cb,,,,,,,1f0a1,1f0db
            ==> Dealer_hand={J♢},  My_hand ={A♠, J♣},  Other_players_hands=[]
            HIT,1f0be,1f0db,1f0ce,,,,,1f0a1,1f0c7
            ==> Dealer_hand={K♡},  My_hand ={A♠, 7♢},  Other_players_hands=[{J♣, K♢}]
            DOUBLE,1f0b3,1f0db,1f0ce,,,,,1f0a1,1f0c7
            ==> Dealer_hand={3♡},  My_hand ={A♠, 7♢},  Other_players_hands=[{J♣, K♢}]
            HIT,1f0d1,,,,,,,1f0a1,1f0a5
            ==> Dealer_hand={A♣},  My_hand ={A♠, 5♠},  Other_players_hands=[]
            STAY,1f0aa,1f0c1,1f0a7,1f0a2,1f0a4,1f0b1,1f0c2,1f0a1,1f0a8,1f0bd
            ==> Dealer_hand={10♠},  My_hand ={A♠, 8♠, Q♡},  Other_players_hands=[{A♢, 7♠}, {2♠, 4♠}, {A♡, 2♢}]
            SPLIT,1f0bb,1f0b1,1f0c8,1f0d5,1f0a9,1f0b2,1f0b9,1f0a1,1f0d1
            ==> Dealer_hand={J♡},  My_hand ={A♠, A♣},  Other_players_hands=[{A♡, 8♢}, {5♣, 9♠}, {2♡, 9♡}]
            DOUBLE,1f0a6,,,,,,,1f0a5,1f0d5
            ==> Dealer_hand={6♠},  My_hand ={5♠, 5♣},  Other_players_hands=[]
            SURRENDER,1f0a1,1f0b1,1f0c8,1f0d5,1f0a9,1f0b2,1f0b9,1f0a8,1f0d8
            ==> Dealer_hand={A♠},  My_hand ={8♠, 8♣},  Other_players_hands=[{A♡, 8♢}, {5♣, 9♠}, {2♡, 9♡}]
            HIT,1f0c9,1f0cb,1f0ad,,,,,1f0d2,1f0b5
            ==> Dealer_hand={9♢},  My_hand ={2♣, 5♡},  Other_players_hands=[{J♢, Q♠}]
            SURRENDER,1f0d1,,,,,,,1f0d6,1f0c9
            ==> Dealer_hand={A♣},  My_hand ={6♣, 9♢},  Other_players_hands=[]
            HIT,1f0be,,,,,,,1f0a2,1f0b2,1f0c2,1f0bd
            ==> Dealer_hand={K♡},  My_hand ={2♠, 2♡, 2♢, Q♡},  Other_players_hands=[]
            HIT,1f0d9,1f0c8,1f0da,1f0c4,1f0b7,,,1f0c2,1f0a3,1f0b3,1f0c3
            ==> Dealer_hand={9♣},  My_hand ={2♢, 3♠, 3♡, 3♢},  Other_players_hands=[{8♢, 10♣}, {4♢, 7♡}]
            STAY,1f0a6,,,,,,,1f0c1,1f0b3,1f0a4
            ==> Dealer_hand={6♠},  My_hand ={A♢, 3♡, 4♠},  Other_players_hands=[]
            DOUBLE,1f0b6,1f0d3,1f0a9,1f0c6,1f0b9,,,1f0c1,1f0c2
            ==> Dealer_hand={6♡},  My_hand ={A♢, 2♢},  Other_players_hands=[{3♣, 9♠}, {6♢, 9♡}]
            STAY,1f0a6,1f0c3,1f0c9,1f0c8,1f0bd,,,1f0c1,1f0ca
            ==> Dealer_hand={6♠},  My_hand ={A♢, 10♢},  Other_players_hands=[{3♢, 9♢}, {8♢, Q♡}]
            HIT,1f0d2,1f0ab,1f0de,,,,,1f0a2,1f0d4,1f0b5
            ==> Dealer_hand={2♣},  My_hand ={2♠, 4♣, 5♡},  Other_players_hands=[{J♠, K♣}]
            HIT,1f0ba,1f0c7,1f0a9,1f0c4,1f0a7,1f0d3,1f0de,1f0b5,1f0b8,1f0c3
            ==> Dealer_hand={10♡},  My_hand ={5♡, 8♡, 3♢},  Other_players_hands=[{7♢, 9♠}, {4♢, 7♠}, {3♣, K♣}]"""
            };


    @AfterAll
    static void cleanUp() throws IOException {
        for (int i = 0; i < 4; i++) {
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

    @Test
    void testCSV3() throws Exception {
        Main.playGame(new String[]{"src/test/CSVFiles/tests/f3.csv"});
        String filePath = userDir + "/src/test/CSVFiles/tests/f3-SOLVED.csv";
        String expectedContents = expectedContentsArray[3];
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
