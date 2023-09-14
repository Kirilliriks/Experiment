package org.anotherteam.game.level.room.object.collider;

import org.anotherteam.game.object.type.level.StaticObject;

public final class Wall extends StaticObject {

    public Wall() {
        super(0, 0);
        collider.setBounds(2, 32);
        collider.setSolid(true);
    }
}
