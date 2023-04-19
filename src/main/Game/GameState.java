package main.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

public class GameState {

        private Hand myHand;

//        public Map<Integer, Integer> handMap;

        public Map<Integer, Integer> deckMap;
//        public Map<Integer, Integer> dealerHandMap;
        private Hand dealerHand;
        private Deck deck;


        public GameState(Hand _myHand, Hand _dealerHand, Deck _newDeck) {
                this.myHand = _myHand;
//                this.handMap = createHandMap(_myHand.getCards());
                this.dealerHand = _dealerHand;
                this.deckMap = createHandMap(_newDeck.getDeck());
//                this.dealerHandMap = createHandMap(_dealerHand.getCards());
                this.deck = _newDeck;
                //this.strategy = _strategy;
        }

        public boolean equals(Object o) { 
                if (o == this)
                        return true;
                if (!(o instanceof GameState))
                        return false;

                GameState other = (GameState)o;

//                boolean handsEqual = this.handMap.equals(other.handMap);
//
//                boolean dealerEquals = this.dealerHandMap.equals(other.dealerHandMap);
//
                boolean remainingCardsEqual = this.deckMap.equals(other.deckMap);

                boolean handsEqual = (this.myHand == null && other.myHand == null) || (this.myHand != null && this.myHand.equals(other.myHand));
//
                boolean dealerEquals = (this.dealerHand == null && other.dealerHand == null) || (this.dealerHand != null && this.dealerHand.equals(other.dealerHand));
//
//                boolean handsDeck = (this.deck == null && other.deck == null) || (this.deck != null && this.deck.equals(other.deck));
//

                return handsEqual && true && dealerEquals;
        }

        // Maybe need a better hashcode
        public final int hashCode() {
                return Objects.hashCode(this.myHand);
                //return Objects.hashCode(this.myHand, this.dealerHand, this.deckMap);
                // return 1000 * this.myHands.hashCode() + 100 * this.dealerCard.hashCode() + 10 * this.deck.hashCode() + this.strategy.hashCode();
        }


        private Map<Integer, Integer> createHandMap(List<Card> cards) {
                Map<Integer, Integer> hand1Map = new HashMap<>();
                for (var card: cards) {
                        if (hand1Map.containsKey(card.getRankValue())) {
                                hand1Map.put(card.getRankValue(), hand1Map.get(card.getRankValue()) + 1);
                        } else {
                                hand1Map.put(card.getRankValue(), 1);
                        }
                }
                return hand1Map;
        }

        public Hand getMyHand() {
                return this.myHand;
        }

        public Hand getDealerHand() {
                return this.dealerHand;
        }

        public Deck getDeck() {
                return this.deck;
        }

        //        public Card getDealerCard() {
//                return this.dealerCard;
//        }
//
//        public Deck getNewDeck() {
//                return this.deck;
//        }
//
//        public Strategy getStrategy() {
//                return this.strategy;
//        }
//
//        public void setMyHands(List<Hand> myHands) {
//                this.myHands = myHands;
//        }
}