package org.anotherteam.object.component.state;

import lombok.NonNull;
import org.anotherteam.object.component.sprite.animation.AnimationData;

public abstract class State {

    private final AnimationData animationData;

    public State(AnimationData animationData){
        this.animationData = animationData;
    }

    public AnimationData getAnimation() {
        return animationData;
    }

    public void onEnd(@NonNull StateController stateController) { }

    public void onStart(@NonNull StateController stateController){ }
}
