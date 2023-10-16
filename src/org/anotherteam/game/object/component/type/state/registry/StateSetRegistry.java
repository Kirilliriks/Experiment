package org.anotherteam.game.object.component.type.state.registry;

import org.anotherteam.game.object.component.type.sprite.animation.AnimationData;
import org.anotherteam.game.object.component.type.state.State;

import java.util.HashMap;
import java.util.Map;

public final class StateSetRegistry {

    public static final Map<String, StateSet> VALUES = new HashMap<>();

    static {
        final var playerSet = new StateSet("player", new State("idle", new AnimationData(1, 0.5f, 0, 4, false)))
                .addState(
                        new State("walk", new AnimationData(0, 0.2f, 0, 7, false))
                );

        VALUES.put(playerSet.getName(), playerSet);
    }

    public static StateSet get(String name) {
        return VALUES.get(name);
    }
}
