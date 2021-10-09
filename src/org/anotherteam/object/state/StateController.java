package org.anotherteam.object.state;

import lombok.NonNull;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.sprite.ObjectSprite;

public class StateController {

    protected final GameObject gameObject;
    protected final ObjectSprite sprite;
    protected State state;
    protected State lastState;

    public StateController(@NonNull GameObject gameObject, @NonNull ObjectSprite sprite, @NonNull State state){
        this.gameObject = gameObject;
        this.sprite = sprite;
        setState(state);
        lastState = null;
    }

    public void setState(@NonNull State state) {
        lastState = this.state;
        this.state = state;
        this.state.onStart(this);
        if (state.getAnimation() == null) {
            sprite.stopAnimation();
            return;
        }
        sprite.setAnimation(state.getAnimation());
    }

    public void onAnimationEnd(){
        state.onEnd(this);
    }

    @NonNull
    public GameObject getGameObject() {
        return gameObject;
    }

    @NonNull
    public ObjectSprite getSprite() {
        return sprite;
    }

    @NonNull
    public State getState() {
        return state;
    }

    //@Nullable
    public State getLastState() {
        return lastState;
    }
}
