package org.anotherteam.object.component.state.type;

import org.anotherteam.object.component.sprite.animation.AnimationData;
import org.anotherteam.object.component.state.State;


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
