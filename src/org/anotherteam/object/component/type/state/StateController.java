package org.anotherteam.object.component.type.state;

import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.jetbrains.annotations.NotNull;

public final class StateController extends Component {

    private String stateName;
    private String defaultStateName;

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

        if (state.getAnimation() == null) {
            sprite.stopAnimation();
            return;
        }
        sprite.setAnimation(state.getAnimation());
    }

    public void setDefaultState() {
        setState(defaultState);
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
