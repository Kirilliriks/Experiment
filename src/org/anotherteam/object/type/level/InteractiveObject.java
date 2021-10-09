package org.anotherteam.object.type.level;

import lombok.NonNull;
import org.anotherteam.level.Level;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.state.StateController;
import org.joml.Vector2i;

public abstract class InteractiveObject extends StaticObject {

    protected StateController stateController;

    public InteractiveObject(Vector2i position, @NonNull Level level, String texturePath) {
        super(position, level, texturePath);
        collider.setInteract(true);
    }

    @Override
    public void onAnimationEnd() {
        stateController.onAnimationEnd();
    }

    public abstract void interact(@NonNull GameObject gameObject);
}
