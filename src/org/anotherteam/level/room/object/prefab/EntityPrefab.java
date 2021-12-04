package org.anotherteam.level.room.object.prefab;

import org.anotherteam.object.GameObject;
import org.anotherteam.level.room.object.entity.Player;
import org.anotherteam.object.prefab.Prefab;

public enum EntityPrefab implements Prefab {
    PLAYER(Player.class);

    final Class<? extends GameObject> clazz;
    EntityPrefab(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends GameObject> getPrefabClass() {
        return clazz;
    }
}
