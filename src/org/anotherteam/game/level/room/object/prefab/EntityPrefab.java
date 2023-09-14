package org.anotherteam.game.level.room.object.prefab;

import lombok.RequiredArgsConstructor;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.level.room.object.entity.Player;
import org.anotherteam.game.object.prefab.Prefab;

@RequiredArgsConstructor
public enum EntityPrefab implements Prefab {
    PLAYER(new Player());

    final GameObject gameObject;

    @Override
    public GameObject getPrefab() {
        return gameObject;
    }
}
