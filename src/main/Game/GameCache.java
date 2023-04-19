package main.Game;
import main.Game.Strategy.StatResult;

import com.google.common.cache.*;

public class GameCache<GameState> {

    public static Cache<GameState, StatResult> cache = CacheBuilder.newBuilder().build();


    public void resetCache() {
        this.cache = CacheBuilder.newBuilder().build();
    }
}
