package main.Play;

import java.io.*;
import java.util.*;

import com.google.common.collect.ImmutableMap;
import main.Game.*;
import main.Game.Card.Suit;
import main.Game.Card.Rank;

public class Parser {

    private String filePath;

    private List<String> listOfGames;

    private final ImmutableMap<Character, Card.Suit> suitMap = ImmutableMap.of(
            'A', Suit.SPADES, 'B', Suit.HEARTS, 'C', Suit.DIAMONDS, 'D', Suit.CLUBS);

    private final ImmutableMap<Character, Card.Rank> rankMap = this.initRankMap();


    public void readFile(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        this.filePath = filepath;

        ArrayList<String> listOfGames = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null){
            listOfGames.add(line);
        }
        reader.close();
        this.listOfGames = listOfGames;
    }

    public void play() throws Exception {
        ArrayList<Card> cardsInGame;
        StringBuilder output = new StringBuilder();
        for (String strGame : this.listOfGames) {
            cardsInGame = this.generateGameState(strGame);

            //Player dealer = new Player(new HashSet<>(Collections.singleton(cardsInGame.get(0))));
            Player me = new Player(new HashSet<>(cardsInGame.subList(8, cardsInGame.size())));

            output.append(generateOutputLine(me.getHand(), strGame));
        }
        this.writeFile(output.toString());
    }

    private ArrayList<Card> generateGameState(String strGame) throws Exception {
        String[] cards = strGame.split(",");
        if (cards.length < 10) {
            throw new Exception();
        }

        ArrayList<Card> cardsInGame = new ArrayList<>();

        for (String card: cards) {
            if (checkValidCard(card)) {
                Card.Suit cardSuit = suitMap.get(card.charAt(3));
                Card.Rank cardRank = rankMap.get(card.charAt(4));
                cardsInGame.add(new Card(cardRank, cardSuit));
            }
        }
        return cardsInGame;
    }

    private boolean checkValidCard(String card) {
        //invalid unicode string
        return (card.length() == 5) && (card.startsWith("1F0")) && (suitMap.containsKey(card.charAt(3))) &&
                (rankMap.containsKey(card.charAt(4)));
    }

    private String generateOutputLine(Hand hand, String strGame) {
        String decision;
        if (hand.playHit()) {
            decision = "HIT";
        } else {
            decision = "STAY";
        }
        return decision + strGame + "\n";

    }

    private void writeFile(String contents) throws IOException {
        FileWriter writer = new FileWriter(this.filePath);
        writer.write(contents);
        writer.close();
    }

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

        return ImmutableMap.copyOf(rankMap);
    }



}
