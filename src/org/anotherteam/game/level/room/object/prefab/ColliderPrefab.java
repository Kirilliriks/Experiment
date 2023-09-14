package org.anotherteam.game.level.room.object.prefab;

import lombok.RequiredArgsConstructor;
import org.anotherteam.game.level.room.object.collider.Wall;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.prefab.Prefab;

@RequiredArgsConstructor
public enum ColliderPrefab implements Prefab {
    WALL(new Wall());

    private final GameObject gameObject;

    @Override
    public GameObject getPrefab() {
        return gameObject;
    }
}
