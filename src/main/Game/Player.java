package main.Game;

import java.io.*;
import java.util.*;
import main.Game.*;

/**
 * A representation of a BlackJack player.
 */
public class Player {
    /**
     * The hand of the player.
     */
    private Hand hand;

    /**
     * Constructor for a player.
     * @param cards the hand of the player.
     */
    public Player(List<Card> cards) {
        this.hand = new Hand(cards);
    }

    /**
     * Returns the hand of the player.
     * @return the hand of the player.
     */
    public Hand getHand() {
        return this.hand;
    }

}
