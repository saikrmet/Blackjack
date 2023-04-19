package main.Play;

import java.io.*;
import java.util.*;

import com.google.common.base.Preconditions;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import main.Game.*;
import main.Game.Card.Suit;
import main.Game.Card.Rank;
import main.Game.Strategy;

import static main.Play.StrategyParser.strategyParser;

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

            counter++;

            if (counter % 2 == 0) {
                output.append(strGame).append("\n");
                continue;
            }

            //System.out.println(strGame);

            // Making Deck
            Deck gameDeck = new Deck();

            // Giving players their cards and removing cards in play from deck
            GameState gameState = this.generateGameState(strGame, gameDeck);

            Float aggPayoff1 = 0.0F;
            Float aggPayoff2 = 0.0F;

            //HW5Strategy IdealStrategy = new HW5Strategy();
            //IdealStrategy.makeDecisionStatBest()

            for (int i = 0; i < this.numGames; i++) {
                gameDeck.setSeed(rand.nextLong());
                // Running both strategies
                Player copyMe = new Player(new Hand(gameState.getMyHand().getCards()));
                Player copyDealer = new Player(new Hand(gameState.getDealerHand().getCards()));

                HW5Strategy IdealStrategy = new HW5Strategy();


                /**
                 * 1. Parse the cards -> Get player hand, get other player's cards, get dealer's card
                 * 2. Make a gameState from this information -> get player's hand, remainingCards will be
                 * complete deck - player's cards - dealer's card - other players' cards, dealer will just be dealer
                 * 3. pass in gameState to IdealStrategy.getPayOff(gameState)
                 * 4. Store payoff
                 */

                Strategy WikiStrategy = new Strategy(List.of(copyDealer, copyMe), "Wiki", strategyParser, new Deck(gameDeck));
                Float retPayoff1 = IdealStrategy.getPayoff(gameState);
                //System.out.println("Ideal: " + retPayoff1);
                Float retPayoff2 = WikiStrategy.getPayoff();
                //System.out.println("strategy 2: " + retPayoff2);
                aggPayoff1 += retPayoff1;
                aggPayoff2 += retPayoff2;
            }
            //System.out.print(aggPayoff1 + "    ");
            //System.out.println(aggPayoff2);
            aggPayoff1 /= this.numGames;
            aggPayoff2 /= this.numGames;

            String agg1Str = String.format("%.5f", aggPayoff1);
            String agg2Str = String.format("%.5f", aggPayoff2);

            output.append(agg1Str).append(',').append(agg2Str).append(strGame.substring(1)).append("\n");
            System.out.println("Ideal: " + agg1Str + " Wiki: " + agg2Str + " Hand: " + gameState.getMyHand());
            GameCache.resetCache();

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
    private GameState generateGameState(String strGame, Deck deck) throws Exception {
        String[] cards = strGame.split(",");

        if (cards.length != 13) {
//            System.err.println(strGame);
//            System.err.println(cards[0]);
//            System.err.println(cards.length);
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

        List<Card> dealerCard = new ArrayList<>();
        dealerCard.add(cardsInGame.get(2));
        Hand dealer = new Hand(dealerCard);
        Hand me = new Hand(Preconditions.checkNotNull(cardsInGame.subList(11, cardsInGame.size())));

        return new GameState(me, dealer, deck);
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


    public void makeStatBestDecisionTester(StrategyParser stratParser) throws Exception {
        Deck testDeck = new Deck();
        Hand playerHand = new Hand(List.of(testDeck.getAndRemoveRandomCard(), testDeck.getAndRemoveRandomCard()));
        Hand dealer = new Hand(List.of(testDeck.getAndRemoveRandomCard()));
//
        //Strategy IdealStrat = new Strategy(List.of(new Player(dealer),new Player(playerHand)), 3, stratParser, testDeck);
//        System.out.println(playerHand);
//        System.out.println(dealer);
        HW5Strategy IdealStrat = new HW5Strategy();
        Strategy.StatResult result;
//        result.printResults();

        testDeck = new Deck();
        Card card1 = new Card(Rank.TWO, Suit.SPADES);
        Card card2 = new Card(Rank.FIVE, Suit.DIAMONDS);
        Card card3 = new Card(Rank.NINE, Suit.CLUBS);

        List<Card> otherCards = new ArrayList<>();
        otherCards.add(new Card(Rank.FIVE, Suit.CLUBS));
        otherCards.add(new Card(Rank.EIGHT, Suit.SPADES));
        otherCards.add(new Card(Rank.SEVEN, Suit.CLUBS));
        otherCards.add(new Card(Rank.JACK, Suit.CLUBS));
        otherCards.add(new Card(Rank.ACE, Suit.DIAMONDS));
        otherCards.add(new Card(Rank.QUEEN, Suit.DIAMONDS));
        otherCards.add(new Card(Rank.THREE, Suit.SPADES));
        otherCards.add(new Card(Rank.KING, Suit.SPADES));

        playerHand = new Hand(List.of(card1, card2));
        testDeck.removeCard(card1);
        testDeck.removeCard(card2);
        dealer = new Hand(List.of(card3));
        testDeck.removeCard(card3);
        System.out.println(playerHand);
        System.out.println(dealer);

        for (Card card : otherCards) {
            testDeck.removeCard(card);
        }




        GameState state = new GameState(playerHand, dealer, testDeck);
        result = IdealStrat.makeDecisionStatBest(state);
        result.printResults();

        Strategy WikiStrategy;
        Float avgPayoffWiki = 0.0F;
        Float avgPayoffBest = 0.0F;
        int numIters = 1000;
        for (int i = 0; i < numIters; i++) {
            WikiStrategy = new Strategy(List.of(new Player(new Hand(dealer.getCards())), new Player(new Hand(playerHand.getCards()))), "Wiki",
                    strategyParser, new Deck(testDeck));
            Random rand = new Random();
            testDeck.setSeed(rand.nextLong());
            var x = WikiStrategy.getPayoff();
            //System.out.println(x);
            avgPayoffWiki += x;
        }

        avgPayoffWiki = avgPayoffWiki / numIters;
        avgPayoffBest = avgPayoffBest / numIters;

        System.out.println("Wiki strategy average payoff:" + avgPayoffWiki);

    }


}
