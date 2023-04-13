package main.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Deck {

    private List<Card> deck;

    private Random random;

    private Long seed;

    public Deck() {
        this.deck = this.initDeck();
        this.random = new Random();
        this.seed = null;
    }

    public Deck(Deck deck) {
        this.deck = new ArrayList<>();
        for (var card: deck.getDeck()) {
            this.deck.add(new Card(card.getRank(), card.getSuit()));
        }
        this.random = new Random();
        if (deck.getSeed() != null) {
            this.random.setSeed(deck.getSeed());
        }
    }

    public List<Card> getDeck() {
        return this.deck;
    }


    private Random getRandom() {
        return this.random;
    }


    public void removeCard(Card card) throws Exception {

        if (this.deck.contains(card)) {
            deck.remove(card);
        } else {
            throw new Exception("Card is not in deck");
        }

    }

    public void addCard(Card card) {
        this.deck.add(card);
    }


    private List<Card> initDeck() {
        List<Card> deck = new ArrayList<>();
        for (Card.Rank rank : Card.Rank.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    public Card getRandomCard() {
        int randomCard = this.random.nextInt(this.deck.size());
        return this.deck.get(randomCard);
    }

    public Long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        this.random.setSeed(seed);
    }

}
