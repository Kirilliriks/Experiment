package org.anotherteam.object.type.level;

import lombok.NonNull;
import org.anotherteam.level.Level;
import org.anotherteam.object.GameObject;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public abstract class StaticObject extends GameObject {

    public StaticObject(Vector2i position, @NonNull Level level, @Nullable Texture texture){
        super(position, level);
    }
}
