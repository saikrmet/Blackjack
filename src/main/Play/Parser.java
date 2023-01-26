package main.Play;

import java.io.*;
import java.util.*;

import com.google.common.collect.ImmutableMap;
import main.Game.*;
import main.Game.Card.Suit;
import main.Game.Card.Rank;

/**
 * A representation of a CSV File Parser.
 */
public class Parser {
    /**
     * The path to the CSV file.
     */
    private String filePath;
    /**
     * A list of all the games played, in string form.
     */
    private List<String> listOfGames;
    /**
     * A mapping of each possible input letter in the unicode representation of a card
     * to the suit it represents.
     */
    private final ImmutableMap<Character, Card.Suit> suitMap = ImmutableMap.of(
            'A', Suit.SPADES, 'B', Suit.HEARTS, 'C', Suit.DIAMONDS, 'D', Suit.CLUBS,
            'a', Suit.SPADES, 'b', Suit.HEARTS, 'c', Suit.DIAMONDS, 'd', Suit.CLUBS);
    /**
     * A mapping of each possible input character in the unicode representation of a card
     * to the rank it represents.
     */
    private final ImmutableMap<Character, Card.Rank> rankMap = this.initRankMap();

    /**
     * Reads the CSV file and defines the listOfGames.
     * @param filepath the path to the CSV file.
     * @throws IOException if the file cannot be read.
     */
    public void readFile(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        this.filePath = filepath;
        //Adds each line (each game) of the file as a new element in an ArrayList.
        ArrayList<String> listOfGames = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null){
            listOfGames.add(line);
        }
        reader.close();
        this.listOfGames = listOfGames;
    }

    /**
     * Makes decision whether to hit or stay for each game and writes it to the CSV file.
     * @throws Exception if the game is invalid (i.e. not enough cards).
     */
    public void play() throws Exception {
        ArrayList<Card> cardsInGame;
        StringBuilder output = new StringBuilder();

        //Makes decision whether to hit or stay for each game.
        int counter = 0; // skip all odd lines as per CSV guidelines
        for (String strGame : this.listOfGames) {
            
            counter ++;

            if (counter % 2 == 0) {
                output.append(strGame + "\n");
                continue;
            }

            cardsInGame = this.generateGameState(strGame);

            //Player dealer = new Player(new HashSet<>(Collections.singleton(cardsInGame.get(0))));
            Player me = new Player(new HashSet<>(cardsInGame.subList(8, cardsInGame.size())));
            output.append(generateOutputLine(me.getHand(), strGame));
        }
        this.writeFile(output.toString());
    }


    /**
     * Takes the string representation of a game and changes it to an ArrayList of Cards.
     * @param strGame the string representation of a Blackjack game
     * @return the ArrayList of Cards representing the current game state.
     * @throws Exception if the game is invalid (i.e. not enough cards).
     */
    private ArrayList<Card> generateGameState(String strGame) throws Exception {
        String[] cards = strGame.split(",");

        if (cards.length < 10) {
            throw new Exception();
        }

        ArrayList<Card> cardsInGame = new ArrayList<>();
        //convert unicode representations to actual cards with ranks and suits.
        for (String card: cards) {
            card = card.strip();
            if (checkValidCard(card)) {
                Card.Suit cardSuit = suitMap.get(card.charAt(3));
                Card.Rank cardRank = rankMap.get(card.charAt(4));
                cardsInGame.add(new Card(cardRank, cardSuit));
            } else {
                cardsInGame.add(null); // add null to indicate no card present
            }
        }
        return cardsInGame;
    }

    /**
     * Checks whether a card is valid or not.
     * @param card the string representation of a card.
     * @return a boolean indicating whether the unicode representation of a card is valid or not.
     */
    private boolean checkValidCard(String card) {
        //invalid unicode string
        return (card.length() == 5) && ((card.startsWith("1F0")) || card.startsWith("1f0")) &&
                (suitMap.containsKey(card.charAt(3))) && (rankMap.containsKey(card.charAt(4)));
    }

    /**
     * Returns the string representation of a game, but with the HIT or STAY decision included.
     * @param hand the player's hand.
     * @param strGame the string representation of a game without the decision included.
     * @return the string representation of a game, but with the HIT or STAY decision included
     */
    private String generateOutputLine(Hand hand, String strGame) {
        String decision;
        if (hand.playHit()) {
            decision = "HIT";
        } else {
            decision = "STAY";
        }
        return decision + strGame + "\n";
    }

    /**
     * Writes a string to a file.
     * @param contents the string to be written to the file.
     * @throws IOException if the file path is invalid.
     */
    private void writeFile(String contents) throws IOException {
        FileWriter writer = new FileWriter(this.filePath);
        writer.write(contents);
        writer.close();
    }

    /**
     * Creates the immutable mapping of each unicode character to its rank.
     * @return the immutable mapping of each unicode character to its rank.
     */
    private ImmutableMap<Character, Card.Rank> initRankMap() {
        Map<Character, Card.Rank> rankMap = new HashMap<>();
        rankMap.put('1', Rank.ACE);
        rankMap.put('2', Rank.TWO);
        rankMap.put('3', Rank.THREE);
        rankMap.put('4', Rank.FOUR);
        rankMap.put('5', Rank.FIVE);
        rankMap.put('6', Rank.SIX);
        rankMap.put('7', Rank.SEVEN);
        rankMap.put('8', Rank.EIGHT);
        rankMap.put('9', Rank.NINE);
        rankMap.put('A', Rank.TEN);
        rankMap.put('B', Rank.JACK);
        rankMap.put('D', Rank.QUEEN);
        rankMap.put('E', Rank.KING);
        rankMap.put('a', Rank.TEN);
        rankMap.put('b', Rank.JACK);
        rankMap.put('d', Rank.QUEEN);
        rankMap.put('e', Rank.KING);

        return ImmutableMap.copyOf(rankMap);
    }



}
