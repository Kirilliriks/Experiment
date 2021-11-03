package org.anotherteam.level.room.object;

import org.anotherteam.object.type.level.StaticObject;

public final class Wall extends StaticObject {

    public Wall(int x, int y) {
        super(x, y);
        collider.setBounds(0, 0, 16, DEFAULT_SIZE.y);
    }
}
