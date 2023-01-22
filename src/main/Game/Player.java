package main.Game;

import java.io.*;
import java.util.*;
import main.Game.*;

public class Player {
    private Hand hand;

    public Player(Set<Card> cards) {
        this.hand = new Hand(cards);
    }

    public Hand getHand() {
        return this.hand;
    }

}
