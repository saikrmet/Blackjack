package main.Play;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        String filePath = args[0];
        Parser parser = new Parser();
        parser.readFile(filePath);
        parser.play();
    }
}
