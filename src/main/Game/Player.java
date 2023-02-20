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
    public Hand getHand() throws Exception {
        if (this.hands.size() == 1) {
            return this.hands.get(0);
        } else {
            throw new Exception("Player has more than one hand");
        }
    }

    /**
     * Returns the hands of the player.
     * @return the hands of the player.
     */
    public List<Hand> getHands() {
        return this.hands;
    }



}
