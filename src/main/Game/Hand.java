package main.Game;

import java.util.*;

import com.google.common.collect.ImmutableMap;
import main.Game.Card.*;

public class Hand {

    private Set<Card> cards;

    private boolean hasAce;

    private int hardValue;

    private int softValue;

    private final ImmutableMap<Rank, Integer> rankMap = initializeRankMap();

    public Hand(Set<Card> cards) {
        this.cards = cards;
        this.hardValue = this.computeHardValue();
        this.hasAce = this.determineAce();
        this.softValue = this.computeSoftValue();
    }

    public Integer getHardValue() {
        return this.hardValue;
    }

    public Integer getSoftValue() {
        return this.softValue;
    }

    public boolean playHit() {
        if (this.computeHardValue() > 11) {
            return false;
        }
        return this.computeSoftValue() <= 17;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    private int computeHardValue() {
        int sum = 0;
        for (Card card : this.cards) {
            sum += rankMap.get(card.getRank());
        }
        return sum;
    }

    private int computeSoftValue() {
        if (!this.hasAce) {
            return computeHardValue();
        }
        return computeHardValue() + 10;
    }

    private boolean determineAce() {
        for (Card card : this.cards) {
            if (card.getRank().equals(Rank.ACE)) {
                return true;
            }
        }
        return false;
    }

    private ImmutableMap<Rank, Integer> initializeRankMap() {
        Map<Rank, Integer> rankMap = new HashMap<>();
        int rankVal = 2;
        for (Rank rank : Rank.values()) {
            rankMap.put(rank, rankVal);
            rankVal += 1;
        }
        return ImmutableMap.copyOf(rankMap);
    }



}
