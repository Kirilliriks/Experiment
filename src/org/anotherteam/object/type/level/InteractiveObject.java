package org.anotherteam.object.type.level;

import lombok.NonNull;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.GameObject;
import org.joml.Vector2i;

public abstract class InteractiveObject extends StaticObject {

    public InteractiveObject(Vector2i position, @NonNull Room room) {
        super(position, room, null);
    }

    public abstract void interactBy(@NonNull GameObject gameObject);
}
