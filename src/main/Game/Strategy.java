package main.Game;
import com.google.common.base.Preconditions;
import main.Play.StrategyParser;

import java.util.*;


public class Strategy {

    public enum Decision {
        STAY,
        HIT,
        SURRENDER,
        SPLIT,
        DOUBLE
    }

    private Player dealer;

    private Player me;


    public Strategy(List<Player> players) {
        Preconditions.checkNotNull(players);

        this.dealer = players.get(0);
        this.me = players.get(1);


    }


    public Decision makeDecision() {
        if (me.getHand().getSize() == 2) {

        }
        return null;
    }


    private Decision pairStrat() {
        return null;
    }

    private Decision softStrat() {
        return null;
    }

    private Decision hardStrat() {
        return null;
    }
}
