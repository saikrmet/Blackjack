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
    private Set<Card> cards;
    /**
     * A boolean indicating whether or not the player has an ace.
     */
    private boolean hasAce;
    /**
     * The hard value of the player's hand.
     */
    private int hardValue;
    /**
     * The soft value of the player's hand.
     */
    private int softValue;
    /**
     * A mapping of each rank to its value in BlackJack.
     */
    private final ImmutableMap<Rank, Integer> rankMap = initializeRankMap();

    /**
     * Constructor for a hand.
     * @param cards the cards in the player's hand.
     */
    public Hand(Set<Card> cards) {
        this.cards = cards;
        this.hardValue = this.computeHardValue();
        this.hasAce = this.determineAce();
        this.softValue = this.computeSoftValue();
    }

    /**
     * Returns the hard value of the hand.
     * @return the hard value of the hand.
     */
    public Integer getHardValue() {
        return this.hardValue;
    }

    /**
     * Returns the soft value of the hand.
     * @return the soft value of the hand.
     */
    public Integer getSoftValue() {
        return this.softValue;
    }

    /**
     * Naive strategy that determines whether the player hits or not.
     * @return a boolean indicating whether the player hits or not.
     */
    public boolean playHit() {
        if (this.computeHardValue() > 11) {
            return false;
        }
        return this.computeSoftValue() <= 17;
    }

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
            sum += rankMap.get(card.getRank());
        }
        return sum;
    }

    /**
     * Computes the soft value of the hand based on the cards.
     * @return the soft value of the hand.
     */
    private int computeSoftValue() {
        if (!this.hasAce) {
            return computeHardValue();
        }
        return computeHardValue() + 10;
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
     * Initializes the rank map that maps card ranks to their BlackJack values.
     * @return the rank map that maps card ranks to their BlackJack values.
     */
    private ImmutableMap<Rank, Integer> initializeRankMap() {
        Map<Rank, Integer> rankMap = new HashMap<>();
        int rankVal = 0;
        for (Rank rank : Rank.values()) {
            if (rankVal < 10) {
                rankVal += 1;
            }
            rankMap.put(rank, rankVal);
        }
        return ImmutableMap.copyOf(rankMap);
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
