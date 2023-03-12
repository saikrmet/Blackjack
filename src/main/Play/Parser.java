package main.Play;

import java.io.*;
import java.util.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import main.Game.*;
import main.Game.Card.Suit;
import main.Game.Card.Rank;
import main.Game.Strategy;

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
     * The number of games to run for each hand.
     */
    private Float numGames;

    /**
     * A mapping of each possible input letter in the unicode representation of a card
     * to the suit it represents.
     */
    private static final ImmutableMap<Character, Card.Suit> suitMap = ImmutableMap.of(
            'A', Suit.SPADES, 'B', Suit.HEARTS, 'C', Suit.DIAMONDS, 'D', Suit.CLUBS,
            'a', Suit.SPADES, 'b', Suit.HEARTS, 'c', Suit.DIAMONDS, 'd', Suit.CLUBS);
    /**
     * A mapping of each possible input character in the unicode representation of a card
     * to the rank it represents.
     */
    private static final ImmutableMap<Character, Card.Rank> rankMap = initRankMap();

    /**
     * Reads the CSV file and defines the listOfGames.
     * @param filepath the path to the CSV file.
     * @throws IOException if the file cannot be read.
     */
    public void readFile(String filepath) throws IOException {
        //CSVReader csvReader = new CSVReaderBuilder(new FileReader(filepath)).build();
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        this.filePath = filepath;
        //Adds each line (each game) of the file as a new element in an ArrayList.
        List<String> listOfGames = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null){
//            System.out.println(line);
            listOfGames.add(line);
        }
        reader.close();
        this.listOfGames = listOfGames;
    }

    /**
     * Makes decision whether to hit or stay for each game and writes it to the CSV file.
     * @param strategyParser the parser for the strategy containing relevant maps
     * @throws Exception if input is invalid
     */
    public void play(StrategyParser strategyParser, int numGames) throws Exception {
        this.numGames = (float) numGames;
        List<Card> cardsInGame;
        StringBuilder output = new StringBuilder();

        System.out.println("Playing...");

        //Makes decision whether to hit or stay for each game.
        int counter = 0; // skip all odd lines as per CSV guidelines
        Random rand = new Random();
        for (String strGame: this.listOfGames) {
//            System.out.println(strGame);
            counter++;

            if (counter % 2 == 0) {
                output.append(strGame).append("\n");
                continue;
            }
            // Making Deck
            Deck gameDeck = new Deck();
            gameDeck.setSeed(rand.nextLong());

            // Giving players their cards and removing cards in play from deck
            cardsInGame = this.generateGameState(strGame, gameDeck);

            //Generate dealer's hand
            List<Card> dealerHand = new ArrayList<>();
            dealerHand.add(cardsInGame.get(2));
            Player dealer = new Player(new Hand(dealerHand));

            //Generate my hand
            Player me = new Player(new Hand(Preconditions.checkNotNull(cardsInGame.subList(11, cardsInGame.size()))));

            Float aggPayoff1 = 0.0F;
            Float aggPayoff2 = 0.0F;

            for (int i = 0; i < this.numGames; i++) {
                // Running both strategies
                Strategy strategy1 = new Strategy(List.of(dealer, me), 1, strategyParser, gameDeck);
                Strategy strategy2 = new Strategy(List.of(dealer, me), 2, strategyParser, new Deck(gameDeck));
                Float retPayoff1 = strategy1.getPayoff();
                Float retPayoff2 = strategy2.getPayoff();
                aggPayoff1 += retPayoff1;
                aggPayoff2 += retPayoff2;

            }
            System.out.print(aggPayoff1 + "    ");
            System.out.println(aggPayoff2);
            aggPayoff1 /= this.numGames;
            aggPayoff2 /= this.numGames;

            String agg1Str = String.format("%.5f", aggPayoff1);
            String agg2Str = String.format("%.5f", aggPayoff2);

            output.append(agg1Str).append(',').append(agg2Str).append(strGame.substring(1)).append("\n");

        }
        this.writeFile(output.toString());
    }


    /**
     * Takes the string representation of a game and changes it to an ArrayList of Cards.
     * @param strGame the string representation of a Blackjack game (array of strings representing
     * potential cards)
     * @return the ArrayList of Cards representing the current game state.
     * @throws Exception if the game is invalid (i.e. not enough cards).
     */
    private List<Card> generateGameState(String strGame, Deck deck) throws Exception {
        String[] cards = strGame.split(",");

        if (cards.length != 13) {
//            System.err.println(strGame);
//            System.err.println(cards[0]);
//            System.err.println(cards.length);
            System.out.println(cards.length);
            System.out.println(Arrays.toString(cards));
            throw new Exception("Invalid CSV format");

        }

        List<Card> cardsInGame = new ArrayList<>();
        //convert unicode representations to actual cards with ranks and suits.
        for (String card: cards) {
            card = card.strip();
            if (checkValidCard(card)) {
                Card.Suit cardSuit = suitMap.get(card.charAt(3));
                Card.Rank cardRank = rankMap.get(card.charAt(4));
                Card newCard = new Card(cardRank, cardSuit);
                deck.removeCard(newCard);
                cardsInGame.add(newCard);
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
     * Writes a string to a file.
     * @param contents the string to be written to the file.
     * @throws IOException if the file path is invalid.
     */
    private void writeFile(String contents) throws IOException {

        System.out.println("Writing to output file...");

        FileWriter writer = new FileWriter(this.filePath.substring(0, this.filePath.length() - 4)
                + "-SOLVED.csv");
        writer.write(contents.trim());
        writer.close();
    }

    /**
     * Creates the immutable mapping of each unicode character to its rank.
     * @return the immutable mapping of each unicode character to its rank.
     */
    private static ImmutableMap<Character, Card.Rank> initRankMap() {
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
