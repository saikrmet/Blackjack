package main.Game;

import java.util.*;

import com.google.common.collect.ImmutableMap;
import main.Game.Card.*;

/**
 * A representation of a BlackJack hand.
 */
public class Hand {

    /**
     * The cards in the player's hand.
     */
    private List<Card> cards;

    /**
     * A boolean indicating whether or not the player has a soft value.
     */
    private boolean isSoft;
    /**
     * The hard value of the player's hand.
     */
    private int hardValue;

    /**
     * Constructor for a hand.
     * @param cards the cards in the player's hand.
     */
    public Hand(List<Card> cards) {
        this.cards = cards;
        this.hardValue = this.computeHardValue();
        this.isSoft = this.determineSoft();
    }

    public List<Card> getCards() {
        return this.cards;
    }

    /**
     * Returns the hard value of the hand.
     * @return the hard value of the hand.
     */
    public Integer getHardValue() {
        return this.computeHardValue();
    }

    /**
     * Returns the soft value of the hand.
     * @return the soft value of the hand.
     */
    public Integer getSoftValue() throws Exception {
        return this.computeSoftValue();
    }


    /**
     * Returns the size of the hand.
     * @return the size of the hand.
     */
    public Integer getSize() { return this.cards.size();}

//    /**
//     * Naive strategy that determines whether the player hits or not.
//     * @return a boolean indicating whether the player hits or not.
//     */
//    public boolean playHit() throws Exception {
//        if (this.isSoft) {
//            return this.computeSoftValue() <= 17;
//        } else {
//            return !(this.computeHardValue() > 11);
//        }
//    }

    /**
     * Adds a card to the player's hand.
     * @param card the card to be added.
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * Computes the hard value of the hand based on the cards.
     * @return the hard value of the hand.
     */
    private int computeHardValue() {
        Integer sum = 0;
        for (Card card : this.cards) {
            sum += card.getRankValue();
        }
        return sum;
    }

    /**
     * Computes the soft value of the hand based on the cards.
     * @return the soft value of the hand.
     */
    private int computeSoftValue() throws Exception {
        if (!this.isSoft) {
            throw new Exception("Hand has no soft value");
        }
        return this.computeHardValue() + 10;
    }

    /**
     * Determines whether a player has an Ace in his hand or not.
     * @return a boolean indicating whether a player has an Ace in his hand or not.
     */
    private boolean determineAce() {
        for (Card card : this.cards) {
            if (card.getRank().equals(Rank.ACE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether a player's hand has a soft value or not.
     * @return a boolean indicating whether a player's hand has a soft value or not.
     */
    private boolean determineSoft() {
        return this.determineAce() && (this.hardValue < 12);
    }


    /**
     * Determines whether a player's hand is a pair or not.
     * @return a boolean indicating whether a player's hand is a pair or not.
     */
    public boolean isPair() {
        if (this.getSize() == 2) {
            return (this.cards.get(0).getRank().equals(this.cards.get(1).getRank()));
        }else{
            return false;
        }
    }

    public boolean isSoft() {
        return this.isSoft;
    }

    /**
     * Returns the string representation of the card.
     * @return the string representation of the card.
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : this.cards) {
            sb.append(card.toString()).append(" + ");
        }
        return sb.substring(0, sb.length() - 3);
    }

}
