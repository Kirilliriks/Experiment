package org.anotherteam.game.object.component.type.state.type;

import org.anotherteam.game.object.component.type.sprite.animation.AnimationData;
import org.anotherteam.game.object.component.type.state.State;


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
