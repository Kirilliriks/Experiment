package org.anotherteam.level.room.object.collider;

import org.anotherteam.object.type.level.StaticObject;

public final class Wall extends StaticObject {

    public Wall(int x, int y) {
        super(x, y);
        collider.setBounds(2, 32);
        collider.setSolid(true);
    }
}
