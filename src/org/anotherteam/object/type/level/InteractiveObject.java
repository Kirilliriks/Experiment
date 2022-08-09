package org.anotherteam.object.type.level;

import org.anotherteam.object.GameObject;
import org.jetbrains.annotations.NotNull;

public abstract class InteractiveObject extends GameObject {

    public InteractiveObject(int x, int y) {
        super(x, y);
    }

    public abstract void interactBy(@NotNull GameObject gameObject);
}
