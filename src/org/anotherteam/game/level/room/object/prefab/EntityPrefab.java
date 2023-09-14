package org.anotherteam.game.level.room.object.prefab;

import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.level.room.object.entity.Player;
import org.anotherteam.game.object.prefab.Prefab;

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
