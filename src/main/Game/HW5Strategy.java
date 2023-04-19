package main.Game;
import main.Play.StrategyParser;
import main.Game.Strategy.Decision;
import main.Game.Strategy.StatResult;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.*;


public class HW5Strategy {


    //private final Player dealer;

    //private final Player me;

    //private final int stratNum;

    private final static StrategyParser strategyParser = StrategyParser.strategyParser;

    private Deck deck;


    private Decision makeDecision(Hand hand) throws Exception {
        //return this.makeDecisionStatBest(hand, this.deck, this.dealer).decision;
        return null;
    }


    public StatResult makeDecisionStatBest(GameState gameState) throws Exception {
        // Assumes remainingCards comprises all possible cards that could be drawn by the player

        StatResult cacheIfPresent = GameCache.cache.getIfPresent(gameState);

        if (cacheIfPresent != null) {
            //System.out.println("Cache hit");
            return cacheIfPresent;
        }
        //System.out.println("Cache miss");

        Hand hand = gameState.getMyHand();
        Deck remainingCards = gameState.getDeck();
        Player dealer = new Player(gameState.getDealerHand());

        // check if bust/finalHand hand, Deck remainingCards, Player dealer

        Map<Decision, Float> decisions = new HashMap<>();

        if (hitAllowed(hand)) {
            decisions.put(Decision.HIT, simulateHit(hand, remainingCards, dealer));
        }

        decisions.put(Decision.STAY, simulateStay(hand, remainingCards, dealer));

        if (surrenderAllowed(hand)) {
            decisions.put(Decision.SURRENDER, simulateSurrender(hand, remainingCards, dealer));
        }

        if (doubleAllowed(hand)) {
            decisions.put(Decision.DOUBLE, simulateDouble(hand, remainingCards, dealer));
        }

        if (splitAllowed(hand)) {
            decisions.put(Decision.SPLIT, simulateSplit(hand, remainingCards, dealer));
        }

        Decision decision = null;
        Float expectedPayoff = null;
        for (Decision choice: Decision.values()) {
            if (decisions.containsKey(choice)) {
                if (choice.equals(Decision.DOUBLE)) {
                    //System.out.println(choice + " " + decisions.get(choice));
                }
                if (decision == null) {
                    decision = choice;
                    expectedPayoff = decisions.get(decision);
                } else if (decisions.get(choice) > expectedPayoff) { // maintains tiebreaker order
                    decision = choice;
                    expectedPayoff = decisions.get(decision);
                }
            }
        }

        StatResult result = new StatResult(decision, expectedPayoff);
        GameCache.cache.put(gameState, result);

        return result;

    }

    private Boolean doubleAllowed(Hand hand) throws Exception {
        return hand.getSize() == 2 && !hand.isDoubled() && !hand.isBlackJack() && hand.getHardValue() <= 11;
    }

    private Boolean hitAllowed(Hand hand) throws Exception {
        return !hand.isFinal() && !hand.isBlackJack();
    }

    private Boolean surrenderAllowed(Hand hand) {
        return !hand.isBust() && !hand.isDoubled() && hand.getCards().size() == 2;
    }

    private Boolean splitAllowed(Hand hand) {
        return !hand.isDoubled() && hand.getSize() == 2 && (hand.getCards().get(0).getRank().equals(hand.getCards().get(1).getRank()));
    }

    private Float simulateHit(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        // simulate each card draw, call makeDecisionStatBest again
        //System.out.println("SIMULATING HIT: " + hand.toString());
        Float avgPayoff = 0.0F;
        Deck copyRemaining = new Deck(remainingCards);
        for (var card: remainingCards.getDeck()) {

            copyRemaining.removeCard(card);
            Hand curHand = new Hand(hand.getCards());
            curHand.addCard(card);

            /**
             * Checking terminal steps here
             */

            // check if hand was a bust
            if (curHand.isBust()) {
                copyRemaining.addCard(card);
                if (curHand.isDoubled()) {
                    avgPayoff -= 2;
                    continue;
                }
                avgPayoff -= 1;
                continue;
            }

            // Hit resulted in a non-bust non-blackjack hand. Defer to overseer function for payoff
            // Find out ideal expected payout of perfect strategy from here

            GameState nextState = new GameState(curHand, dealer.getHand(), copyRemaining);
            //System.out.println("HIT called StatBest");
            StatResult nextResult = makeDecisionStatBest(nextState);

            avgPayoff += nextResult.expectedPayout;

            copyRemaining.addCard(card);
        }

        return avgPayoff / remainingCards.getDeck().size();
    }

    private Float simulateDouble(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        //System.out.println("SIMULATING DOUBLE: " + hand.toString());


        float avgPayoff = 0.0F;
        Deck copyRemaining = new Deck(remainingCards);
        for (Card card: remainingCards.getDeck()) {
            copyRemaining.removeCard(card);
            Hand curHand = new Hand(hand.getCards());
            curHand.addCard(card);
            curHand.setDoubled(true);
            curHand.setFinal(true);
            //System.out.println("simulateDouble called StatBest");
            //System.out.println(curHand);
            avgPayoff += simulateRestOfGame(curHand, copyRemaining, dealer);
            //System.out.println(curHand);

            copyRemaining.addCard(card);

        }
        var x = avgPayoff / remainingCards.getDeck().size();
        //System.out.println(x);
        return avgPayoff / remainingCards.getDeck().size();
    }

    private Float simulateSplit(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        // Split hand into two hands. Obtain best decision for first hand, do it, then given the remaining cards
        // obtain best decision for second hand. Payout is then the sum of both payouts.
        //System.out.println("SIMULATING SPLIT: " + hand.toString());
        Float avgPayoff = 0.0F;
        int toRemove = 0;
        Deck copyRemaining = new Deck(remainingCards);
        // Probably a way to optimize it by iterating through a diagonal because of the symmetry here
        for (int i = 0; i < remainingCards.getDeck().size(); i++) {
            for (int j = 0; j < remainingCards.getDeck().size(); j++) {
                if (i == j) {
                    toRemove++;
                    continue;
                }

                Hand hand1 = new Hand(List.of(hand.getCards().get(0)));
                Hand hand2 = new Hand(List.of(hand.getCards().get(1)));

                Card card1 = remainingCards.getDeck().get(i);
                Card card2 = remainingCards.getDeck().get(j);

                hand1.addCard(card1);
                hand2.addCard(card2);

                copyRemaining.removeCard(card1);
                copyRemaining.removeCard(card2);

                GameState gameState1 = new GameState(hand1, dealer.getHand(), copyRemaining);
                GameState gameState2 = new GameState(hand1, dealer.getHand(), copyRemaining);
                //System.out.println("simulateSplit called StatBest");
                avgPayoff += makeDecisionStatBest(gameState1).expectedPayout;
                //System.out.println("simulateSplit called StatBest");
                avgPayoff += makeDecisionStatBest(gameState2).expectedPayout;

                copyRemaining.addCard(card1);
                copyRemaining.addCard(card2);
            }
        }

        return avgPayoff / (remainingCards.getDeck().size() * remainingCards.getDeck().size() - toRemove);
    }
    private Float simulateSurrender(Hand hand, Deck remainingCards, Player dealer) {
        // Terminal function; recursion ends here
        //System.out.println("SIMULATING SURRENDER: " + hand.toString());
        return -0.5F;
    }

    private Float simulateStay(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        // Terminal step. Recursion ends here

        //System.out.println(x + " Hand is: " + hand);
        //System.out.println("Player's hand is: " + hand + " Dealer's hand is: " + dealer.getHand() + " with payoff: " + x);
        return simulateRestOfGame(hand, remainingCards, new Player(new Hand(dealer.getHand().getCards())));
    }

    private Float simulateRestOfGame(Hand hand, Deck remainingCards, Player dealer) throws Exception {

        Deck copyRemaining = new Deck(remainingCards);

        if (this.makeDecisionDealer(dealer.getHand())) {
            return this.calculatePayoff(new Player(hand), dealer);
        }

        Float avgPayoff = 0.0F;

        for (var card: remainingCards.getDeck()) {
            Player copyDealer = new Player(new Hand(dealer.getHand().getCards()));
            copyRemaining = new Deck(remainingCards);
            copyRemaining.removeCard(card);
            copyDealer.getHand().addCard(card);

            //System.out.println(copyDealer.getHand());

            avgPayoff += simulateRestOfGame(hand, copyRemaining, copyDealer);

            //CHECK THIS!!
            copyRemaining.addCard(card);
        }
        //System.out.println(avgPayoff / remainingCards.getDeck().size());
        return avgPayoff / remainingCards.getDeck().size();
    }

    private Boolean makeDecisionDealer(Hand hand) throws Exception {
        //System.out.println("DEAKLER HAND IS: " + hand);
        //System.out.println("STAY");
        //return Decision.STAY;
        //return Decision.HIT;
        if (hand.isSoft() && hand.getSoftValue() >= 17) {
            //System.out.println("STAY");
            return true;
            //return Decision.STAY;
        } else return hand.getHardValue() > 17;
    }

    // Calculate payoff for a final player hand and given dealer hand
    private Float calculatePayoff(Player me, Player dealer) throws Exception {

        float myPayoff = 0f;
        for (Hand myHand : me.getHands()) {
            int factor = 1;
            if (myHand.isDoubled()) {
                factor = 2;
            }
            if (myHand.isSurrender()) {
                myPayoff -= 0.50f;
            } else {
                if (myHand.isBlackJack()) {
                    if (dealer.getHand().isBlackJack()) {
                        myPayoff += 0;
                    } else {
                        myPayoff += 1.5;
                    }
                } else if (myHand.isBust()) {
                    myPayoff -= factor;
                } else if (dealer.getHand().isBlackJack()) {
                    myPayoff -= 1;
                } else if (dealer.getHand().isBust()) {
                    myPayoff += factor;
                } else {
                    int myValue;
                    int dealerValue;
                    if (myHand.isSoft()) {
                        myValue = myHand.getSoftValue();
                    } else {
                        myValue = myHand.getHardValue();
                    }
                    if (dealer.getHand().isSoft()) {
                        dealerValue = dealer.getHand().getSoftValue();
                    } else {
                        dealerValue = dealer.getHand().getHardValue();
                    }
                    if (myValue > dealerValue) {
                        myPayoff += factor;
                    } else if (myValue == dealerValue) {
                        myPayoff += 0;
                    } else {
                        myPayoff -= factor;
                    }
                }
            }
        }

//        System.out.println("Payoff with player hand:");
//        System.out.println(me.getHand());
//        System.out.println("dealer hand:");
//        System.out.println(dealer.getHand());
//        System.out.println("was: " + myPayoff);
        return myPayoff;
    }

    public Float getPayoff(GameState gameState) throws Exception {
        //System.out.println("===== new getPayoff =====");

        var playerHand = new Hand(gameState.getMyHand().getCards());
        var dealerHand = new Hand(gameState.getDealerHand().getCards());
        var remainingCards = new Deck(gameState.getDeck());

        float splitFactor = 0.0F;

        while (!playerHand.isFinal()) {
            var copyGameState = new GameState(new Hand(playerHand.getCards()), new Hand(dealerHand.getCards()),
                    new Deck(remainingCards));
            var nextDecision = this.makeDecisionStatBest(copyGameState).decision;
            //System.out.println(nextDecision);

            switch (nextDecision) {
                case HIT:
                    playerHand.addCard(remainingCards.getAndRemoveRandomCard());
                    if (playerHand.isBust()) {
                        playerHand.setFinal(true);
                    }
                    break;
                case STAY:
                    playerHand.setFinal(true);
                    break;
                case SURRENDER:
                    playerHand.setSurrender(true);
                    playerHand.setFinal(true);
                    break;
                case DOUBLE:
                    playerHand.addCard(remainingCards.getAndRemoveRandomCard());
                    playerHand.setDoubled(true);
                    playerHand.setFinal(true);
                    break;
                case SPLIT:
                    //System.out.println("in split");
                    var playerHand1 = new Hand(List.of(playerHand.getCards().get(0)));
                    var playerHand2 = new Hand(List.of(playerHand.getCards().get(1)));

                    playerHand = playerHand1;

                    playerHand.addCard(remainingCards.getAndRemoveRandomCard());
                    playerHand2.addCard(remainingCards.getAndRemoveRandomCard());

//                    var gameState1 = new GameState(new Hand(playerHand1.getCards()), new Hand(dealerHand.getCards()),
//                            new Deck(remainingCards));
                    var gameState2 = new GameState(playerHand2, new Hand(dealerHand.getCards()),
                            new Deck(remainingCards));

                    splitFactor += this.getPayoff(gameState2);
                    break;
            }
        }

        while (!dealerHand.isFinal() && !this.makeDecisionDealer(dealerHand)) {
            dealerHand.addCard(remainingCards.getAndRemoveRandomCard());
            if (dealerHand.isBust()) {
                dealerHand.setFinal(true);
            }
        }

        return this.calculatePayoff(new Player(playerHand), new Player(dealerHand)) + splitFactor;

    }


}
