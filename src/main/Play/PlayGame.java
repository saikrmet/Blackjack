package main.Play;

import main.Game.*;

import java.util.*;

public class PlayGame {


    private final int stratNum;
    private final int numGames;

    private Deck deck;


    public PlayGame(int numGames, int stratNum) {
        this.numGames = numGames;
        this.stratNum = stratNum;
    }

    public void printMetrics() throws Exception {
        List<Float> metrics = this.run();
        System.out.printf("HW%d Avg: %.3f\n", this.stratNum, metrics.get(0));
        System.out.printf("HW%d Max Gain: %.3f\n", this.stratNum, metrics.get(1));
        System.out.printf("HW%d Max Loss: %.3f\n", this.stratNum, metrics.get(2));
    }


    private List<Float> run() throws Exception {
        //List<Float> payoffOutcomes = new ArrayList<>();
        float totalPayoff = 0.0F;
        float minPayoff = 0.0F;
        Float maxPayoff = 0.0F;
        List<Player> initialGameState;
        Player me;
        Player dealer;
        StrategyParser strategyParser = new StrategyParser();

        for (int i = 0; i < this.numGames; i++) {
            initialGameState = this.generateRandomGame();
            dealer = initialGameState.get(0);
            me = initialGameState.get(initialGameState.size() - 1);
            Strategy strategy = new Strategy(List.of(dealer, me), this.stratNum, strategyParser, this.deck);
            Float retPayoff = strategy.getPayoff();
            if (retPayoff > maxPayoff) {
                maxPayoff = retPayoff;
            }
            if (retPayoff < minPayoff) {
                minPayoff = retPayoff;
            }
            totalPayoff += retPayoff;
            //payoffOutcomes.add(strategy.getPayoff());
        }

        List<Float> returnOutcomes = new ArrayList<>();
        returnOutcomes.add(totalPayoff / this.numGames);
        returnOutcomes.add(maxPayoff);
        returnOutcomes.add(minPayoff);
//        returnOutcomes.add(payoffOutcomes.stream().reduce(0.0F, Float::sum) / this.numGames);
//        returnOutcomes.add(Collections.max(payoffOutcomes));
//        returnOutcomes.add(Collections.min(payoffOutcomes));

        return returnOutcomes;
    }


    private List<Player> generateRandomGame() throws Exception {
        List<Player> players = new ArrayList<>();
        Deck deck = new Deck();

        Card dealerCard = deck.getRandomCard();
        deck.removeCard(dealerCard);
        List<Card> dealerCards = new ArrayList<>();
        dealerCards.add(dealerCard);
        players.add(new Player(new Hand(dealerCards)));

        int numPlayers = getRandomNumPlayers();

        for (int i = 0; i < numPlayers + 1; i++) {
            List<Card> currCards = new ArrayList<>();
            Card card1 = deck.getRandomCard();
            currCards.add(card1);
            deck.removeCard(card1);

            Card card2 = deck.getRandomCard();
            currCards.add(card2);
            deck.removeCard(card2);

            players.add(new Player(new Hand(currCards)));
        }
        this.deck = deck;
        return players;
    }



    private int getRandomNumPlayers() {
        Random random = new Random();
        return random.nextInt(0, 4);
    }


}
