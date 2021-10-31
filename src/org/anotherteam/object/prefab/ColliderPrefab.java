package org.anotherteam.object.prefab;

import org.anotherteam.level.room.object.Wall;
import org.anotherteam.object.GameObject;

public enum ColliderPrefab implements Prefab {
    WALL(Wall.class);

    Class<? extends GameObject> clazz;
    ColliderPrefab(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends GameObject> getPrefabClass() {
        return clazz;
    }
}
