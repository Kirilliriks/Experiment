package org.anotherteam.level.room.object;

import lombok.val;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.object.type.level.StaticObject;

public final class Wall extends StaticObject {

    public Wall(int x, int y) {
        super(x, y);
        val collider = new Collider();
        collider.setBounds(-8, 16, 8, 16);
        collider.setSolid(true);
        addComponent(collider);
    }
}
