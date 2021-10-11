package org.anotherteam.level.global;

import lombok.NonNull;
import org.anotherteam.level.Level;
import org.anotherteam.object.component.collider.ColliderComponent;
import org.anotherteam.object.type.level.StaticObject;
import org.joml.Vector2i;

public final class Wall extends StaticObject {

    private final ColliderComponent collider;

    public Wall(Vector2i position, @NonNull Level level) {
        super(position, level, null);
        collider = new ColliderComponent(position);
        collider.setBounds(-8, 16, 8, 16);
        collider.setSolid(true);
        addComponent(collider);
    }
}
