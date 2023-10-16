package org.anotherteam.game.object.component.type.state.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.anotherteam.game.object.component.type.state.State;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public final class StateSet {

    private final String name;
    private final State defaultState;
    private final Map<String, State> values = new HashMap<>();

    public State get(String state) {
        return values.get(state);
    }

    public StateSet addState(State state) {
        values.put(state.getName(), state);
        return this;
    }
}
