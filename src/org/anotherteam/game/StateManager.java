package org.anotherteam.game;

import org.anotherteam.game.Game;
import org.anotherteam.game.GameState;
import org.anotherteam.manager.AbstractManager;
import org.jetbrains.annotations.NotNull;

public final class StateManager extends AbstractManager {

    private final Game game;
    private GameState state;

    public StateManager(Game game, GameState state) {
        this.game = game;
        setState(state);
    }

    public void setState(GameState state) {
        this.state = state;

        if (state == GameState.ON_LEVEL) {
            Game.LEVEL_MANAGER.getCurrent().prepare();
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
