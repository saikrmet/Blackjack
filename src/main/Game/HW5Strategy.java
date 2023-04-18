package main.Game;
import com.google.common.base.Preconditions;
import main.Play.StrategyParser;
import main.Game.Strategy.Decision;

import java.util.*;


public class HW5Strategy {

    public class StatResult {
        Decision decision;
        Double expectedPayout;

        public StatResult(Decision decision, Double expectedPayout) {
            this.decision = decision;
            this.expectedPayout = expectedPayout;
        }

        public void printResults() {
            System.out.println("Decision = " + decision.toString());
            System.out.println("Expected Payout = " + expectedPayout);
        }

    }

    private final Player dealer;

    private final Player me;

    private final int stratNum;

    private final static StrategyParser strategyParser = StrategyParser.strategyParser;

    private Deck deck;


    public HW5Strategy(List<Player> players, int stratNum, StrategyParser strategyParser, Deck deck) {
        Preconditions.checkNotNull(players);

        this.dealer = players.get(0);
        this.me = players.get(1);
        this.stratNum = stratNum;
        //this.strategyParser = strategyParser;
        this.deck = deck;

    }


    private List<Hand> doSplit(Hand hand) throws Exception {
        List<Hand> retVal = new ArrayList<>();
        if (this.makeDecision(hand) == Decision.SPLIT) {
            //System.out.println("splitishere");
            List<Card> hand1 = new ArrayList<>();
            List<Card> hand2 = new ArrayList<>();
            hand1.add(hand.getCards().get(0));
            hand2.add(hand.getCards().get(1));
            Card drawnCard1 = this.deck.getRandomCard();
            hand1.add(drawnCard1);
            this.deck.removeCard(drawnCard1);
            Card drawnCard2 = this.deck.getRandomCard();
            hand2.add(drawnCard2);
            this.deck.removeCard(drawnCard2);
            retVal.addAll(doSplit(new Hand(hand1)));
            retVal.addAll(doSplit(new Hand(hand2)));
        } else {
            retVal.add(hand);
        }
        return retVal;
    }

    private Decision makeDecision(Hand hand) throws Exception {
        if (this.stratNum == 1) {
            return this.makeDecisionHW1(hand);
        } else if (this.stratNum == 2) {
            return this.makeDecisionHW2(hand);
        } else if (this.stratNum == 3) {
            return this.makeDecisionStatBest(hand, this.deck, this.dealer).decision;
        }
        else {
            throw new Exception("Not a valid HW strategy number");
        }
    }

    boolean first = true;

    public StatResult makeDecisionStatBest(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        // Assumes remainingCards comprises all possible cards that could be drawn by the player
        Map<String, Double> decisions = new HashMap<>();

        if (hitAllowed(hand)) {
            decisions.put("hit", simulateHit(hand, remainingCards, dealer));
        }

        decisions.put("stay", simulateStay(hand, remainingCards, dealer));

        if (surrenderAllowed(hand)) {
            decisions.put("surrender", simulateSurrender(hand, remainingCards, dealer));
        }

        if (doubleAllowed(hand)) {
            decisions.put("double", simulateDouble(hand, remainingCards, dealer));
        }

        if (splitAllowed(hand)) {
            decisions.put("split", simulateSplit(hand, remainingCards, dealer));
        }

        String decision = null;
        Double expectedPayoff = null;
        List<String> choices = List.of("hit", "stay", "surrender", "double", "split");


        for (var choice: choices) {
            if (decisions.containsKey(choice)) {
                if (choice.equals("double")) {
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

        // decision will never be null; we can always hit
        assert decision != null;

        first = false;

        Decision finalDecision = parseDecision(decision);

        return new StatResult(finalDecision, expectedPayoff);

    }

    private Decision parseDecision(String decision) {
        return switch (decision) {
            case "hit" -> Decision.HIT;
            case "stay" -> Decision.STAY;
            case "surrender" -> Decision.SURRENDER;
            case "double" -> Decision.DOUBLE;
            case "split" -> Decision.SPLIT;
            default -> null;
        };
    }

    private Boolean doubleAllowed(Hand hand) {
        return hand.getSize() == 2 && !hand.isDoubled();
    }

    private Boolean hitAllowed(Hand hand) {
        return !hand.isDoubled();
    }

    private Boolean surrenderAllowed(Hand hand) {
        return !hand.isBust() && !hand.isDoubled();
    }

    private Boolean splitAllowed(Hand hand) {
        return !hand.isDoubled() && hand.getSize() == 2 && (hand.getCards().get(0).getRank().equals(hand.getCards().get(1).getRank()));
    }

    private Double simulateHit(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        // simulate each card draw, call makeDecisionStatBest again
        //System.out.println("SIMULATING HIT: " + hand.toString());
        Double avgPayoff = 0.0;
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
                    avgPayoff += -2;
                    continue;
                }
                avgPayoff += -1;
                continue;
            }

            // check if hand is blackjack
            if (curHand.isBlackJack()) {
                curHand.setFinal(true);
                avgPayoff += simulateStay(curHand, new Deck(copyRemaining), dealer);
                copyRemaining.addCard(card);
                continue;
            }

            // Hit resulted in a non-bust non-blackjack hand. Defer to overseer function for payoff
            // Find out ideal expected payout of perfect strategy from here
            StatResult nextResult = makeDecisionStatBest(curHand, copyRemaining, dealer);

            avgPayoff += nextResult.expectedPayout;

            copyRemaining.addCard(card);
        }

        return avgPayoff / remainingCards.getDeck().size();
    }

    private Double simulateStay(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        // Terminal step. Recursion ends here.
        
        Deck copyRemaining = new Deck(remainingCards);
        // Play out dealer's hand:
        Player copyDealer = new Player(new Hand(dealer.getHand().getCards()));
        while (!copyDealer.getHand().isFinal()) {
            Decision decision = this.makeDecisionDealer(copyDealer.getHand());
            switch (decision) {
                case HIT -> {
                    Card drawnCard = copyRemaining.getRandomCard();
                    copyDealer.getHand().addCard(drawnCard);
                    copyRemaining.removeCard(drawnCard);
                }
                case STAY -> {
                    copyDealer.getHand().setFinal(true);
                }
            }
            if (copyDealer.getHand().isBust()) {
                copyDealer.getHand().setFinal(true);
            }
        }
        return Double.valueOf(calculatePayoff(new Player(hand), copyDealer, copyRemaining));
    }

    private Double simulateDouble(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        //System.out.println("SIMULATING DOUBLE: " + hand.toString());

        // This is a terminal step; need to update code to reflect this

        Double avgPayoff = 0.0;
        Deck copyRemaining = new Deck(remainingCards);
        for (var card: remainingCards.getDeck()) {
            copyRemaining.removeCard(card);
            var curHand = new Hand(hand.getCards());
            curHand.addCard(card);
            curHand.setDoubled(true);

            /**
             * Check terminal outcomes
             */

            avgPayoff += simulateStay(curHand, copyRemaining, dealer);

            // System.out.println("average payout is currently: " + avgPayoff);
            // System.out.println(curHand);
            // Player's hand was not Blackjack or bust

            // Find out ideal expected payout of perfect strategy from here
            //StatResult nextResult = makeDecisionStatBest(curHand, copyRemaining, dealer);

//            System.out.println("nextresult was " + nextResult.decision +" " + nextResult.expectedPayout);
//            System.out.println("DEALER HAND: " + dealer.getHand());

            //avgPayoff += nextResult.expectedPayout;

            copyRemaining.addCard(card);

        }

        return avgPayoff / remainingCards.getDeck().size();
    }

    private Double simulateSplit(Hand hand, Deck remainingCards, Player dealer) throws Exception {
        // Split hand into two hands. Obtain best decision for first hand, do it, then given the remaining cards
        // obtain best decision for second hand. Payout is then the sum of both payouts.
        //System.out.println("SIMULATING SPLIT: " + hand.toString());
        Double avgPayoff = 0.0;
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

                avgPayoff += makeDecisionStatBest(hand1, copyRemaining, dealer).expectedPayout;

                copyRemaining.removeCard(card2);

                avgPayoff += makeDecisionStatBest(hand2, copyRemaining, dealer).expectedPayout;

                copyRemaining.addCard(card1);
                copyRemaining.addCard(card2);
            }
        }

        return avgPayoff / (remainingCards.getDeck().size() * remainingCards.getDeck().size() - toRemove);
    }
    private Double simulateSurrender(Hand hand, Deck remainingCards, Player dealer) {
        // Terminal function; recursion ends here
        //System.out.println("SIMULATING SURRENDER: " + hand.toString());
        if (hand.isDoubled()) {
            return -1.0;
        }
        return -0.5;
    }
    private Decision makeDecisionHW1(Hand hand) throws Exception {
        int hardVal = hand.getHardValue();
        if (hardVal > 11) {
            return Decision.STAY;
        } else if (hand.isSoft() && hand.getSoftValue() > 17) {
            return Decision.STAY;
        }
        return Decision.HIT;
    }


    private Decision makeDecisionHW2(Hand hand) throws Exception {
        List<Card.Rank> hardHand = List.of(Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING);
        if (hand.isPair()) {
            if (hardHand.contains(hand.getCards().get(0).getRank())) {
                return Objects.requireNonNull(this.strategyParser.getPairsMap().get(Card.Rank.TEN))
                        .get(this.dealer.getHand().getCards().get(0).getRank());
            } else {
                return this.pairStrat(this.strategyParser, hand);
            }
        } else if (hand.isSoft()){
            return this.softStrat(this.strategyParser, hand);
        } else {
            return this.hardStrat(this.strategyParser, hand);
        }
    }

    private Decision makeDecisionDealer(Hand hand) throws Exception {
        //System.out.println("DEAKLER HAND IS: " + hand);
        if (hand.isSoft() && hand.getSoftValue() >= 17) {
            return Decision.STAY;
        } else if (hand.getHardValue() > 17) {
            return Decision.STAY;
        } else {
            return Decision.HIT;
        }
    }

    // Calculate payoff for a final player hand and given dealer hand
    private Float calculatePayoff(Player me, Player dealer, Deck remainingCards) throws Exception {

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

    private Decision pairStrat(StrategyParser strategyParser, Hand hand) throws Exception {
        return Objects.requireNonNull(this.strategyParser.getPairsMap().get(hand.getCards().get(0).getRank()))
                .get(this.dealer.getHand().getCards().get(0).getRank());
    }

    private Decision softStrat(StrategyParser strategyParser, Hand hand) throws Exception {
        List<Decision> decisions = Objects.requireNonNull(Objects.requireNonNull(this.strategyParser.getSoftMap().
                get(hand.getSoftValue())).get(this.dealer.getHand().getCards().get(0).getRank()));

        if (decisions.size() == 2 && hand.getSize() != 2) {
            return decisions.get(1);
        }
        return decisions.get(0);
    }

    private Decision hardStrat(StrategyParser strategyParser, Hand hand) throws Exception {
        List<Decision> decisions = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(this.strategyParser
                .getHardMap().get(hand.getHardValue())).get(this.dealer.getHand().getCards().get(0).getRank())));

        if (decisions.size() == 2 && hand.getSize() != 2) {
            return decisions.get(1);
        }
        return decisions.get(0);
    }

}
