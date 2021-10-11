package org.anotherteam.object.type.level;

import lombok.NonNull;
import org.anotherteam.level.Level;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.state.StateController;
import org.joml.Vector2i;

public abstract class InteractiveObject extends StaticObject {

    public InteractiveObject(Vector2i position, @NonNull Level level, String texturePath) {
        super(position, level, null);
    }

    public abstract void interactBy(@NonNull GameObject gameObject);
}
