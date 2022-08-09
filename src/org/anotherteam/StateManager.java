package org.anotherteam;

import org.anotherteam.manager.AbstractManager;
import org.jetbrains.annotations.NotNull;

public final class StateManager extends AbstractManager {

    private final Game game;
    private GameState state;

    public StateManager(Game game, GameState firstState) {
        this.game = game;
        this.state = firstState;
    }

    public void setState(GameState state) {
        this.state = state;

        if (state == GameState.ON_LEVEL) {
            Game.LEVEL_MANAGER.getCurrentLevel().prepare();
        }
    }

    @NotNull
    public GameState getState() {
        return state;
    }

    @Override
    public void update(float delta) { }

    @Override
    public void clear() { }
}
