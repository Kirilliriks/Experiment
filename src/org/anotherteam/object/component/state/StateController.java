package org.anotherteam.object.component.state;

import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.sprite.SpriteController;
import org.jetbrains.annotations.NotNull;

public final class StateController extends Component {

    private SpriteController sprite;
    private State defaultState;
    private State state;

    public StateController() {
        defaultState = null;
        state = null;
    }

    public void setDefaultState(State defaultState) {
        this.defaultState = defaultState;
    }

    @Override
    public void init() {
        setDefaultState();
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
        this.state = state;

        state.onStart(this);
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
}
