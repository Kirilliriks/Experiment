package org.anotherteam.object.type.entity;

import org.anotherteam.object.sprite.animation.AnimationData;
import org.anotherteam.object.state.State;


public abstract class EntityState extends State {

    private final boolean canWalk;

    public EntityState(AnimationData animationData, boolean canWalk) {
        super(animationData);
        this.canWalk = canWalk;
    }

    public boolean isCanWalk() {
        return canWalk;
    }
}
