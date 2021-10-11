package org.anotherteam.object.component.state.player;

import org.anotherteam.object.component.sprite.animation.AnimationData;
import org.anotherteam.object.type.entity.EntityState;


public final class PlayerState {

    public static Walk WALK = new Walk();
    public static Idle IDLE = new Idle();
    public static EntityState DEFAULT = IDLE;

    private static class Walk extends EntityState {
        public Walk() {
            super(new AnimationData(1, 0.2f, 0, 7, false), true);
        }
    }

    private static class Idle extends EntityState {
        public Idle() {
            super(new AnimationData(0, 0.5f, 0, 4, false), true);
        }
    }
}
