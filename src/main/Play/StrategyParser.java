package main.Play;


//Will parse the input file and generate the Immutable maps for each

import java.io.*;
import java.io.IOException;
import java.util.*;

import com.google.common.collect.ImmutableMap;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import main.Game.Card.Rank;
import main.Game.Card;
import main.Game.Strategy;

public class StrategyParser {

    /**
     * Initializes the strategyRankMap, a map that maps appropriate strings to the card rank
     * they represent.
     */
    private final ImmutableMap<String, Card.Rank> strategyRankMap;

    /**
     * Initializes the strategyRankMap, a map that maps appropriate strings to the decision
     * they represent.
     */
    private final ImmutableMap<String, Strategy.Decision> decisionMap;

    /**
     * Holds the pairs strategy map, mapping Card Rank to a map that maps the dealer's card to
     * the appropriate choice to make.
     */
    private ImmutableMap<Card.Rank, Map<Card.Rank, Strategy.Decision>> pairsMap;

    /**
     * Holds soft strategy map. Used when one's hand contains a soft value. Maps a hand's soft value
     * to a map that maps the dealer's card's rank to the appropriate choice to make.
     */
    private  ImmutableMap<Integer, Map<Card.Rank, List<Strategy.Decision>>> softMap;

    /**
     * Holds hard strategy map. Used when one's hand contains a soft value. Maps a hand's hard value
     * to a map that maps the dealer's card's rank to the appropriate choice to make. Is only used
     * when one's hand does not have a pair or a soft value.
     */
    private ImmutableMap<Integer, Map<Card.Rank, List<Strategy.Decision>>> hardMap;

    public static StrategyParser strategyParser;

    static {
        try {
            strategyParser = new StrategyParser();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public StrategyParser() throws IOException, CsvValidationException {
        this.strategyRankMap = this.initStratRankMap();
        this.decisionMap = this.initDecisionMap();
        this.pairsMap = this.initPairsMap();
        this.softMap = this.initSoftMap();
        this.hardMap = this.initHardMap();
    }


    /**
     * Creates the immutable mapping of each string to its rank.
     * @return the immutable mapping of each string to its rank.
     */
    private ImmutableMap<String, Card.Rank> initStratRankMap() {
        Map<String, Card.Rank> strategyRankMap = new HashMap<>();
        strategyRankMap.put("A", Rank.ACE);
        strategyRankMap.put("2", Rank.TWO);
        strategyRankMap.put("3", Rank.THREE);
        strategyRankMap.put("4", Rank.FOUR);
        strategyRankMap.put("5", Rank.FIVE);
        strategyRankMap.put("6", Rank.SIX);
        strategyRankMap.put("7", Rank.SEVEN);
        strategyRankMap.put("8", Rank.EIGHT);
        strategyRankMap.put("9", Rank.NINE);
        strategyRankMap.put("10", Rank.TEN);
        strategyRankMap.put("J", Rank.JACK);
        strategyRankMap.put("Q", Rank.QUEEN);
        strategyRankMap.put("K", Rank.KING);

        return ImmutableMap.copyOf(strategyRankMap);
    }

    /**
     * Creates the immutable mapping of each string to its decision.
     * @return the immutable mapping of each string to its decision.
     */
    private ImmutableMap<String, Strategy.Decision> initDecisionMap() {
        Map<String, Strategy.Decision> decisionMap = new HashMap<>();
        decisionMap.put("SURRENDER", Strategy.Decision.SURRENDER);
        decisionMap.put("STAY", Strategy.Decision.STAY);
        decisionMap.put("SPLIT", Strategy.Decision.SPLIT);
        decisionMap.put("DOUBLE", Strategy.Decision.DOUBLE);
        decisionMap.put("HIT", Strategy.Decision.HIT);

        return ImmutableMap.copyOf(decisionMap);
    }


    /**
     * Returns the pairs strategy mapping. If it has not been initialized yet, this method
     * initializes the mapping and returns it.
     * @return the pairs strategy mapping.
     * @throws IOException if the pairs map CSV cannot be read.
     */
    public ImmutableMap<Card.Rank, Map<Card.Rank, Strategy.Decision>> getPairsMap() throws
            IOException, CsvValidationException {

        if (pairsMap == null) {
            pairsMap = this.initPairsMap();
            return pairsMap;
        }

        return pairsMap;

    }

    /**
     * Returns the soft value strategy mapping. If it has not been initialized yet, this method
     * initializes the mapping and returns it.
     * @return the soft value strategy mapping.
     * @throws IOException if the pairs map CSV cannot be read.
     */
    public ImmutableMap<Integer, Map<Card.Rank, List<Strategy.Decision>>> getSoftMap() throws
            IOException, CsvValidationException {

        if (softMap == null) {
            softMap = this.initSoftMap();
            return softMap;
        }

        return softMap;
    }

    /**
     * Returns the hard value strategy mapping. If it has not been initialized yet, this method
     * initializes the mapping and returns it.
     * @return the hard value strategy mapping.
     * @throws IOException if the pairs map CSV cannot be read.
     */
    public ImmutableMap<Integer, Map<Card.Rank, List<Strategy.Decision>>> getHardMap() throws
            IOException, CsvValidationException {

        if (hardMap == null) {
            hardMap = this.initHardMap();
            return hardMap;
        }

        return hardMap;
    }

    /**
     * Reads the given CSV file. Parses it into a list of Strings[], where every String[] is a parsed line
     * from input file split on commas.
     * @param filePath - Path to the file to be read.
     * @return A List of String arrays. Each entry in the list is a line in the input file split on commas
     * @throws IOException - If reading the file fails.
     * @throws CsvValidationException - If the file is not in a supported CSV format.
     */
    private List<String[]> readFile(String filePath) throws IOException, CsvValidationException{
        CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).build();
        List<String[]> possibilities = new ArrayList<>();
        String[] line;
        while ((line = reader.readNext()) != null) {
            possibilities.add(line);
        }
        reader.close();
        return possibilities;
    }

    /**
     * Initializes the pairs strategy mapping.
     * @return The pairs strategy mapping. Maps the player's pairs' rank to a map that maps the dealer's
     * card's rank to the decision that should be made.
     * @throws IOException - If the input filepath cannot be read.
     * @throws CsvValidationException - If the input file is not in a valid CSV format.
     */
    private ImmutableMap<Card.Rank, Map<Card.Rank, Strategy.Decision>> initPairsMap() throws IOException,
            CsvValidationException {
        String filePath = "src/test/CSVFiles/Strategies/Wiki Strategy (Simplified) - pairs.csv";
        List<String[]> possibilities = this.readFile(filePath);
        List<String> dealerPos = Arrays.stream(possibilities.get(0)).toList();
        List<List<String>> mePos = new ArrayList<>();

        for (int i = 1; i < possibilities.size(); i++) {
            mePos.add(Arrays.stream(possibilities.get(i)).toList());
        }

        Map<Card.Rank, Map<Card.Rank, Strategy.Decision>> tempPairsMap = new HashMap<>();
        for (List<String> pairPos : mePos) {
            Card.Rank pairedCard = strategyRankMap.get(pairPos.get(0).substring(0,pairPos.get(0).indexOf("+")));
            Map<Card.Rank, Strategy.Decision> dealerToDecision = new HashMap<>();
            for (int i = 1; i < dealerPos.size(); i++) {
                Strategy.Decision decision = decisionMap.get(pairPos.get(i));
                dealerToDecision.put(strategyRankMap.get(dealerPos.get(i)), decision);
            }
            tempPairsMap.put(pairedCard, dealerToDecision);
        }
        return ImmutableMap.copyOf(tempPairsMap);
    }

    /**
     * Initializes the soft value strategy mapping.
     * @return The soft value strategy mapping. Maps the player's hand's soft value to a map that maps the dealer's
     * card's rank to the decision that should be made.
     * @throws IOException - If the input filepath cannot be read.
     * @throws CsvValidationException - If the input file is not in a valid CSV format.
     */
    private ImmutableMap<Integer, Map<Card.Rank, List<Strategy.Decision>>> initSoftMap() throws
            IOException, CsvValidationException {
        String filePath = "src/test/CSVFiles/Strategies/Wiki Strategy (Simplified) - soft.csv";
        List<String[]> possibilities = this.readFile(filePath);
        List<String> dealerPos = Arrays.stream(possibilities.get(0)).toList();
        List<List<String>> mePos = new ArrayList<>();

        for (int i = 1; i < possibilities.size(); i++) {
            mePos.add(Arrays.stream(possibilities.get(i)).toList());
        }

        Integer softValue = 21;
        Map<Integer, Map<Card.Rank, List<Strategy.Decision>>> tempSoftMap = new HashMap<>();
        for (List<String> softValuePos : mePos) {
            Map<Card.Rank, List<Strategy.Decision>> dealerToDecision = new HashMap<>();
            for (int i = 1; i < dealerPos.size(); i++) {
                List<String> decisionsStr = Arrays.stream(softValuePos.get(i).split("/")).toList();
                List<Strategy.Decision> decisions = new ArrayList<>();
                for (String decision : decisionsStr) {
                    decisions.add(decisionMap.get(decision));
                }
                dealerToDecision.put(strategyRankMap.get(dealerPos.get(i)), decisions);
            }
            tempSoftMap.put(softValue, dealerToDecision);
            softValue--;
        }
        return ImmutableMap.copyOf(tempSoftMap);
    }

    /**
     * Initializes the hard value strategy mapping.
     * @return The hard value strategy mapping. Maps the player's hand's hard value to a map that maps the dealer's
     * card's rank to the decision that should be made.
     * @throws IOException - If the input filepath cannot be read.
     * @throws CsvValidationException - If the input file is not in a valid CSV format.
     */
    private ImmutableMap<Integer, Map<Card.Rank, List<Strategy.Decision>>> initHardMap() throws IOException,
            CsvValidationException {
        String filePath = "src/test/CSVFiles/Strategies/Wiki Strategy (Simplified) - hard.csv";
        List<String[]> possibilities = this.readFile(filePath);
        List<String> dealerPos = Arrays.stream(possibilities.get(0)).toList();
        List<List<String>> mePos = new ArrayList<>();

        for (int i = 1; i < possibilities.size(); i++) {
            mePos.add(Arrays.stream(possibilities.get(i)).toList());
        }

        Map<Integer, Map<Card.Rank, List<Strategy.Decision>>> tempHardMap = new HashMap<>();
        for (List<String> hardValuePos : mePos) {
            Integer hardValue = Integer.parseInt(hardValuePos.get(0));
            Map<Card.Rank, List<Strategy.Decision>> dealerToDecision = new HashMap<>();
            for (int i = 1; i < dealerPos.size(); i++) {
                List<String> decisionsStr = Arrays.stream(hardValuePos.get(i).split("/")).toList();
                List<Strategy.Decision> decisions = new ArrayList<>();
                for (String decision : decisionsStr) {
                    decisions.add(decisionMap.get(decision));
                }
                dealerToDecision.put(strategyRankMap.get(dealerPos.get(i)), decisions);
            }
            tempHardMap.put(hardValue, dealerToDecision);
        }
        return ImmutableMap.copyOf(tempHardMap);
    }
}
