package main.Game;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.security.*;
import java.util.*;

public final class Card {
    private final Rank rank;
    private final Suit suit;

    public enum Suit {
        SPADES,
        CLUBS,
        DIAMONDS,
        HEARTS;
    }

    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        TEN, JACK, QUEEN, KING;
    }


    public Card(Rank rank, Suit suit){
        this.rank = Preconditions.checkNotNull(rank);
        this.suit = Preconditions.checkNotNull(suit);
    }

    public Suit getSuit() {
        return this.suit;
    }

    public Rank getRank() {
        return this.rank;
    }

    @Override
    public final String toString() {
        return rank + " of " + suit;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(this.rank, this.suit);
    }


}
