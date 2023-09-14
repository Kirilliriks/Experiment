package org.anotherteam.game.object.component.type.state;

import org.anotherteam.game.object.component.type.sprite.animation.AnimationData;

public abstract class State {

    private final AnimationData animationData;

    public State(AnimationData animationData){
        this.animationData = animationData;
    }

    public AnimationData getAnimation() {
        return animationData;
    }
}
