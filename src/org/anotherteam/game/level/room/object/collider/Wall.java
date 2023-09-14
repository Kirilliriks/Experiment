package org.anotherteam.game.level.room.object.collider;

import org.anotherteam.game.object.type.level.StaticObject;

public final class Wall extends StaticObject {

    public Wall(int x, int y) {
        super(x, y);
        getCollider().setBounds(2, 32);
        getCollider().setSolid(true);
    }
}
