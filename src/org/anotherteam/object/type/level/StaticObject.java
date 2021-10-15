package org.anotherteam.object.type.level;

import lombok.NonNull;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.GameObject;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public abstract class StaticObject extends GameObject {

    public StaticObject(Vector2i position, @NonNull Room room, @Nullable Texture texture){
        super(position, room);
    }
}
