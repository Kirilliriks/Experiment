package org.anotherteam.level.room.object.collider;

import org.anotherteam.object.type.level.StaticObject;

public final class Wall extends StaticObject {

    public Wall(int x, int y) {
        super(x, y);
        collider.setBounds(0, 0, 2, DEFAULT_SIZE.y);
        collider.setSolid(true);
    }
}
