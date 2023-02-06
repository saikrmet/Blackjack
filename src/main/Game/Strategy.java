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
                return this.hardStrat(strategyParser);
            } else {
                return this.pairStrat(strategyParser);
            }
        } else if (me.getHand().getHasAce()){
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
        int decisionInd = (me.getHand().getSize() == 2) ? (0) : (1);

        return Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(strategyParser.getSoftMap()
                        .get(me.getHand().getSoftValue()))
                .get(dealer.getHand().getCards().get(0).getRank())).get(decisionInd));
    }

    private Decision hardStrat(StrategyParser strategyParser) throws IOException, CsvValidationException {
        int decisionInd = (me.getHand().getSize() == 2) ? (0) : (1);

        return Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(strategyParser.getHardMap()
                        .get(me.getHand().getHardValue()))
                .get(dealer.getHand().getCards().get(0).getRank())).get(decisionInd));
    }
}
