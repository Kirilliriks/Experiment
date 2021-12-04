package org.anotherteam.level.room.object.prefab;

import org.anotherteam.level.room.object.collider.Wall;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.prefab.Prefab;

public enum ColliderPrefab implements Prefab {
    WALL(Wall.class);

    final Class<? extends GameObject> clazz;
    ColliderPrefab(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends GameObject> getPrefabClass() {
        return clazz;
    }
}
