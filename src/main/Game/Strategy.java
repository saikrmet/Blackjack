package main.Game;
import com.google.common.base.Preconditions;
import com.opencsv.exceptions.CsvValidationException;
import main.Play.StrategyParser;

import java.io.IOException;
import java.util.*;


public class Strategy {

    public enum Decision {
        STAY,
        HIT,
        SURRENDER,
        SPLIT,
        DOUBLE
    }

    private final Player dealer;

    private final Player me;


    public Strategy(List<Player> players) {
        Preconditions.checkNotNull(players);

        this.dealer = players.get(0);
        this.me = players.get(1);
    }


    public Decision makeDecision(StrategyParser strategyParser) throws Exception {
        Hand myHand = me.getHand();
        List<Card.Rank> hardHand = List.of(Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING);
        if (myHand.isPair()) {
            if (hardHand.contains(myHand.getCards().get(0).getRank())) {
                return Objects.requireNonNull(strategyParser.getPairsMap().get(Card.Rank.TEN))
                        .get(dealer.getHand().getCards().get(0).getRank());
            } else {
                return this.pairStrat(strategyParser);
            }
        } else if (me.getHand().isSoft()){
            return this.softStrat(strategyParser);
        } else {
            return this.hardStrat(strategyParser);
        }
    }


    private Decision pairStrat(StrategyParser strategyParser) throws IOException, CsvValidationException {
        return Objects.requireNonNull(strategyParser.getPairsMap().get(me.getHand().getCards().get(0).getRank()))
                .get(dealer.getHand().getCards().get(0).getRank());
    }

    private Decision softStrat(StrategyParser strategyParser) throws Exception {
        List<Decision> decisions = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(
                strategyParser.getSoftMap().get(me.getHand().getSoftValue()))
                .get(dealer.getHand().getCards().get(0).getRank())));

        if (decisions.size() == 2 && me.getHand().getSize() != 2) {
            return decisions.get(1);
        }
        return decisions.get(0);
    }

    private Decision hardStrat(StrategyParser strategyParser) throws IOException, CsvValidationException {
        List<Decision> decisions = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(
                strategyParser.getHardMap().get(me.getHand().getHardValue()))
                .get(dealer.getHand().getCards().get(0).getRank())));

        if (decisions.size() == 2 && me.getHand().getSize() != 2) {
            return decisions.get(1);
        }
        return decisions.get(0);
    }
}
