package org.anotherteam;

import org.anotherteam.manager.AbstractManager;
import org.jetbrains.annotations.NotNull;

public final class StateManager extends AbstractManager {

    private GameState state;

    public StateManager(GameState firstState) {
        this.state = firstState;
    }

    public void setState(GameState state) {
        this.state = state;
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
