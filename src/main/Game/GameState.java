package main.Game;

import java.util.List;

import com.google.common.base.Objects;

public class GameState {

        private List<Hand> myHands;
        private Card dealerCard;
        private Deck deck;
        private Strategy strategy;

        public GameState(List<Hand> _myHands, Card _dealerCard, Deck _newDeck, Strategy _strategy) {
                this.myHands = _myHands;
                this.dealerCard = _dealerCard;
                this.deck = _newDeck;
                this.strategy = _strategy;
        }

        public boolean equals(Object o) { 
                if (o == this)
                        return true;
                if (!(o instanceof GameState))
                        return false;
                GameState other = (GameState)o;
                boolean handsEqual = (this.myHands == null && other.myHands == null) || (this.myHands != null && this.myHands.equals(other.myHands));

                boolean handsDealerCard = (this.dealerCard == null && other.dealerCard == null) || (this.dealerCard != null && this.dealerCard.getRankValue().equals(other.dealerCard.getRankValue()));

                boolean handsDeck = (this.deck == null && other.deck == null) || (this.deck != null && this.deck.equals(other.deck));

                boolean handsStrategy = (this.strategy == null && other.strategy == null) || (this.strategy != null && this.strategy.equals(other.strategy));

                return handsEqual && handsDealerCard && handsDeck && handsStrategy;
        }

        // Maybe need a better hashcode
        public final int hashCode() {
                return Objects.hashCode(this.myHands, this.dealerCard, this.deck, this.strategy);
                // return 1000 * this.myHands.hashCode() + 100 * this.dealerCard.hashCode() + 10 * this.deck.hashCode() + this.strategy.hashCode();

        }

        public List<Hand> getMyHands() {
                return this.myHands;
        }

        public Card getDealerCard() {
                return this.dealerCard;
        }

        public Deck getNewDeck() {
                return this.deck;
        }

        public Strategy getStrategy() {
                return this.strategy;
        }

        public void setMyHands(List<Hand> myHands) {
                this.myHands = myHands;
        }
}