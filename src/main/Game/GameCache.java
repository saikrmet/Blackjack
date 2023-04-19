package main.Game;
import main.Game.Strategy.StatResult;

import com.google.common.cache.*;

public class GameCache {

    public static Cache<GameState, StatResult> cache = CacheBuilder.newBuilder().build();


    public static void resetCache() {
        cache = CacheBuilder.newBuilder().build();
    }
}
