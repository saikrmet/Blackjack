package main.Play;


//Will parse the input file and generate the Immutable maps for each

import com.google.common.collect.ImmutableMap;
import main.Game.Card;
import main.Game.Strategy;

public class StrategyParser {

    private ImmutableMap<Card.Rank, ImmutableMap<Card.Rank, Strategy.Decision>> pairsMap;
}
