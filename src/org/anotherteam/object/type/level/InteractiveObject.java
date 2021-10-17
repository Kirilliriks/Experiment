package org.anotherteam.object.type.level;

import lombok.NonNull;
import org.anotherteam.object.GameObject;

public abstract class InteractiveObject extends GameObject {

    public InteractiveObject(int x, int y) {
        super(x, y);
    }

    public abstract void interactBy(@NonNull GameObject gameObject);
}
