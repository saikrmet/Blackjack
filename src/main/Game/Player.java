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
    private List<Hand> hands;


    public Player(Hand hand) {
        this.hands = new ArrayList<>();
        this.hands.add(hand);
    }

    public void setHands(List<Hand> hands) {
        this.hands = hands;
    }

    /**
     * Returns the hand of the player.
     * @return the hand of the player.
     */
    public Hand getHand() {
        return this.hand;
    }

}
