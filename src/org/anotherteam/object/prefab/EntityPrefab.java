package org.anotherteam.object.prefab;

import org.anotherteam.object.GameObject;
import org.anotherteam.object.type.entity.player.Player;

public enum EntityPrefab implements Prefab {
    PLAYER(Player.class);

    Class<? extends GameObject> clazz;
    EntityPrefab(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends GameObject> getPrefabClass() {
        return clazz;
    }
}
