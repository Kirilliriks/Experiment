package org.anotherteam.object.component.type.state;

import org.anotherteam.object.component.type.sprite.animation.AnimationData;
import org.jetbrains.annotations.NotNull;

public abstract class State {

    private final AnimationData animationData;

    public State(AnimationData animationData){
        this.animationData = animationData;
    }

    public AnimationData getAnimation() {
        return animationData;
    }

    public void onEnd(@NotNull StateController stateController) { }

    public void onStart(@NotNull StateController stateController){ }
}
