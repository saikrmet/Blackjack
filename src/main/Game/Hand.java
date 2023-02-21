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

    private boolean isSurrendered;

    private boolean isDoubled;

    private boolean isFinaled;

    /**
     * Constructor for a hand.
     * @param cards the cards in the player's hand.
     */
    public Hand(List<Card> cards) {
        this.cards = cards;
        this.hardValue = this.computeHardValue();
        this.isSoft = this.determineSoft();
        this.isSurrendered = false;
        this.isDoubled = false;
        this.isFinaled = false;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    /**
     * Returns the size of the hand.
     * @return the size of the hand.
     */
    public Integer getSize() { return this.cards.size();}

    /**
     * Adds a card to the player's hand.
     * @param card the card to be added.
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    //    public Card removeCard() {
//        if (this.cards.size() > 0) {
//            Card removedCard = this.cards.get(0);
//            this.cards.remove(0);
//            return removedCard;
//        }
//    }

    /**
     * Returns the hard value of the hand.
     * @return the hard value of the hand.
     */
    public Integer getHardValue() {
        return this.computeHardValue();
    }

    /**
     * Returns the soft value of the hand, if the hand is soft.
     * @return the soft value of the hand
     * @throws Exception if the hand is not soft
     */
    public Integer getSoftValue() throws Exception {
        if (!this.determineSoft()) {
            throw new Exception("Hand is not soft");
        }
        return this.computeSoftValue();
    }

    /**
     * Determines whether a player's hand has an Ace or not.
     * @return a boolean indicating whether a player's hand has an Ace or not.
     */
    public boolean hasAce() {
        return this.determineAce();
    }

    /**
     * Determines whether a player's hand is a pair or not.
     * @return a boolean indicating whether a player's hand is a pair or not.
     */
    public boolean isPair() {
        return this.determinePair();
    }

    /**
     * Determines whether a player's hand is soft or not.
     * @return a boolean indicating whether a player's hand is soft or not.
     */
    public boolean isSoft() {
        return this.determineSoft();
    }

    /**
     * Determines whether a player's hand is bust or not.
     * @return a boolean indicating whether a player's hand is bust or not.
     */
    public boolean isBust() {
        return this.determineBust();
    }

    /**
     * Determines whether a player's hand has a Blackjack or not.
     * @return a boolean indicating whether a player's hand has a Blackjack or not.
     * @throws Exception if a hand considered soft does not have a soft value (**REFER TO determineBlackjack())
     */
    public boolean isBlackJack() throws Exception {
        return this.determineBlackjack();
    }


    /**
     * Determines whether the player's hand has been surrendered or not.
     * @return a boolean indicating whether the player's hand has been surrendered or not.
     */
    public boolean isSurrender() {
        return this.isSurrendered;
    }

    /**
     * Setter for the surrender status of the player's hand.
     * @param surrenderStatus a boolean indicating whether the player's hand has been surrendered or not.
     */
    public void setSurrender(boolean surrenderStatus) {
        this.isSurrendered = surrenderStatus;
    }

    /**
     * Determines whether the player's hand has been doubled or not.
     * @return a boolean indicating whether the player's hand has been doubled or not.
     */
    public boolean isDoubled() {
        return this.isDoubled;
    }

    /**
     * Setter for the doubled status of the player's hand.
     * @param doubledStatus a boolean indicating whether the player's hand has been doubled or not.
     */
    public void setDoubled(boolean doubledStatus) {
        this.isDoubled = doubledStatus;
    }

    /**
     * Determines whether the player's hand is considered final or not (HW3).
     * @return a boolean indicating whether the player's hand is considered final or not.
     */
    public boolean isFinal() {
        return this.isFinaled;
    }

    /**
     * Setter for the final status of the player's hand.
     * @param finalStatus a boolean indicating whether the player's hand is final or not.
     */
    public void setFinal(boolean finalStatus) {
        this.isFinaled = finalStatus;
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


    //Private methods
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
     * Determines whether the hand is a pair.
     * @return boolean indicating whether the hand is a pair.
     */
    private boolean determinePair() {
        if (this.getSize() == 2) {
            return (this.cards.get(0).getRank().equals(this.cards.get(1).getRank()));
        } else {
            return false;
        }
    }

    /**
     * Determines whether a player's hand has a soft value or not.
     * @return a boolean indicating whether a player's hand has a soft value or not.
     */
    private boolean determineSoft() {
        return this.determineAce() && (this.computeHardValue() < 12);
    }

    /**
     * Determines whether a player's hand is bust or not.
     * @return a boolean indicating whether a player's hand is bust or not.
     */
    private boolean determineBust() {
        return this.computeHardValue() > 21;
    }

    /**
     * Determines whether a player's hand has a Blackjack or not.
     * @return a boolean indicating whether a player's hand has a Blackjack or not.
     * @throws Exception if a hand considered soft does not have a soft value (**ALREADY CHECKING)
     */
    private boolean determineBlackjack() throws Exception {
        if (this.determineSoft()) {
            return (this.getSoftValue() == 21 && this.cards.size() == 2);
        }
        return false;
    }


}
