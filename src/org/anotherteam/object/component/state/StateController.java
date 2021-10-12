package org.anotherteam.object.component.state;

import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.sprite.SpriteController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StateController extends Component {

    private final State defaultState;
    private SpriteController sprite;
    private State state;
    private State lastState;

    public StateController(@NotNull State defaultState) {
        lastState = null;
        this.defaultState = defaultState;
        this.state = defaultState;
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
    }

    @Override
    public void setDependencies() {
        if (sprite != null) return;
        sprite = getDependsComponent(SpriteController.class);
    }

    public void setState(@NotNull State state) {
        lastState = this.state;
        this.state = state;
        this.state.onStart(this);
        if (state.getAnimation() == null) {
            sprite.stopAnimation();
            return;
        }
        sprite.setAnimation(state.getAnimation());
    }

    public void setDefaultState() {
        setState(defaultState);
    }

    public void onAnimationEnd(){
        state.onEnd(this);
    }

    @NotNull
    public SpriteController getSprite() {
        return sprite;
    }

    @NotNull
    public State getState() {
        return state;
    }

    @Nullable
    public State getLastState() {
        return lastState;
    }
}
