package main.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Deck {

    private List<Card> deck;

    private Random random;



    public Deck() {
        this.deck = this.initDeck();
        this.random = new Random();
    }

    public Deck(Deck deck) {
        this.deck = new ArrayList<>(deck.getDeck());
        this.random = deck.getRandom();
    }

    private List<Card> getDeck() {
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

    public void setSeed(long seed) {
        this.random.setSeed(seed);
    }

}
