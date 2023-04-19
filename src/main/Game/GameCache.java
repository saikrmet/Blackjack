package main.Game;
import main.Game.Strategy.StatResult;

import com.google.common.cache.*;

public class GameCache<GameState> {

    public Cache<GameState, StatResult> cache = CacheBuilder.newBuilder().build();

    public void GameCache() {
        //this.cacheMap = new HashMap<>();
    }

    public void resetCache() {
        this.cache = CacheBuilder.newBuilder().build();
    }
}
