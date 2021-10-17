package org.anotherteam.object.component.state.type;

import org.anotherteam.object.component.sprite.animation.AnimationData;

public abstract class PlayerState extends EntityState {

    public static final Walk WALK = new Walk();
    public static final Idle IDLE = new Idle();
    public static final EntityState DEFAULT = IDLE;

    public PlayerState(AnimationData animationData, boolean canWalk) {
        super(animationData, canWalk);
    }

    private static final class Walk extends PlayerState {
        public Walk() {
            super(new AnimationData(0, 0.2f, 0, 7, false), true);
        }
    }

    private static final class Idle extends PlayerState {
        public Idle() {
            super(new AnimationData(1, 0.5f, 0, 4, false), true);
        }
    }
}
