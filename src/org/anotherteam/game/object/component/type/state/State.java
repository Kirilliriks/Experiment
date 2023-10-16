package org.anotherteam.game.object.component.type.state;

import lombok.Getter;
import org.anotherteam.game.object.component.type.sprite.animation.AnimationData;

@Getter
public final class State {

    private final String name;
    private final AnimationData animationData;

    public State(String name, AnimationData animationData) {
        this.name = name;
        this.animationData = animationData;
    }

    public AnimationData getAnimation() {
        return animationData;
    }
}
