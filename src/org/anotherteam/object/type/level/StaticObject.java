package org.anotherteam.object.type.level;

import lombok.NonNull;
import org.anotherteam.level.Level;
import org.anotherteam.object.GameObject;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public abstract class StaticObject extends GameObject {

    public StaticObject(Vector2i position, @NonNull Level level, @Nullable String texturePath){
        super(position, level);
        if (texturePath != null && !texturePath.isEmpty()) this.sprite.loadSprite(texturePath);
        this.drawPriority = 0;
    }
}
