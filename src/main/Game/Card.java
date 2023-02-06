package main.Game;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.security.*;
import java.util.*;

/**
 * A representation of a card in a standard deck.
 */
public final class Card {
    /**
     * The rank of the card.
     */
    private final Rank rank;
    /**
     * The suit of the card.
     */
    private final Suit suit;

    /**
     * The four suits of a deck.
     */
    public enum Suit {
        SPADES,
        CLUBS,
        DIAMONDS,
        HEARTS
    }

    /**
     * All ranks of a deck excluding Jokers.
     */
    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        TEN, JACK, QUEEN, KING
    }

    /**
     * A mapping of each rank to its value in BlackJack.
     */
    private static final ImmutableMap<Rank, Integer> rankValueMap = initializeRankValueMap();

    /**
     * Constructor for a card.
     * @param rank the rank of the card.
     * @param suit the suit of the card.
     */
    public Card(Rank rank, Suit suit){
        this.rank = Preconditions.checkNotNull(rank);
        this.suit = Preconditions.checkNotNull(suit);
    }

    /**
     * Gets the suit of the card.
     * @return the suit of the card.
     */
    public Suit getSuit() {
        return this.suit;
    }

    /**
     * Gets the rank of the card.
     * @return the rank of the card.
     */
    public Rank getRank() {
        return this.rank;
    }

    /**
     * Gets the rank value of the card
     * @return the integer value of the rank of the card.
     */
    public Integer getRankValue() {
        return rankValueMap.get(this.rank);
    }

    /**
     * Returns the string representation of the card.
     * @return the string representation of the card.
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    /**
     * Checks whether another object is equivalent to this card.
     * @param o the object we're checking.
     * @return a boolean indicating whether the object is equivalent to the card or not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        Card card = (Card) o;
        return (rank == card.rank) && (suit == card.suit);
    }

    /**
     * Returns the hash code of the card.
     * @return the hash code of the card.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.rank, this.suit);
    }

    /**
     * Initializes the rank map that maps card ranks to their BlackJack values.
     * @return the rank map that maps card ranks to their BlackJack values.
     */
    private static ImmutableMap<Rank, Integer> initializeRankValueMap() {
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


}
