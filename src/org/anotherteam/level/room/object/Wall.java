package org.anotherteam.level.room.object;

import lombok.NonNull;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.object.type.level.StaticObject;
import org.joml.Vector2i;

public final class Wall extends StaticObject {

    private final Collider collider;

    public Wall(Vector2i position, @NonNull Room room) {
        super(position, room, null);
        collider = new Collider(position);
        collider.setBounds(-8, 16, 8, 16);
        collider.setSolid(true);
        addComponent(collider);
    }
}
