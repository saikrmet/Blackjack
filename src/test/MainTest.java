package test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import main.Play.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class MainTest {

    private static final String userDir = System.getProperty("user.dir");

    private static final String[] fileContentsArray
            = new String[]{",1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n",
            ",1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n,1F0C8,,,,,,,1F0B5,1F0D1\n"};

    private static final String[] expectedContentsArray
            = new String[]{"STAY,1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\n",
            "STAY,1F0C8,,,,,,,1F0B6,1F0A2,1F0D7\nHIT,1F0C8,,,,,,,1F0B5,1F0D1\n"};


    @AfterAll
    static void cleanUp() throws IOException {
        for (int i = 0; i < 2; i++) {
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

        String fileContents = Files.readString(Paths.get(filePath));
        //assertEquals(expectedContents, fileContents);
    }


    private static void writeFileContents(int fileNum) throws IOException {
        String filePath = userDir + "/src/test/CSVFiles/tests/f" + fileNum + ".csv";
        String fileContents = fileContentsArray[fileNum];
        FileWriter writer = new FileWriter(filePath);
        writer.write(fileContents);
        writer.close();

    }

}
