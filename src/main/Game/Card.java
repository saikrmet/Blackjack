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
        HEARTS;
    }

    /**
     * All ranks of a deck excluding Jokers.
     */
    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        TEN, JACK, QUEEN, KING;
    }

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
     * Returns the string representation of the card.
     * @return the string representation of the card.
     */
    @Override
    public final String toString() {
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


}
