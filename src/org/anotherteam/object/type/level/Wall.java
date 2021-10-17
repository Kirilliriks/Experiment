package org.anotherteam.object.type.level;

import lombok.val;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.collider.Collider;

public final class Wall extends GameObject {

    public Wall(int x, int y, int width) {
        super(x, y);
        val collider = new Collider();
        collider.setBounds(-width / 2, 0, width / 2, 32);
        collider.setSolid(true);
        addComponent(collider);
    }
}
