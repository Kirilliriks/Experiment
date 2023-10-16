package org.anotherteam.game.object.prefab;

import org.anotherteam.game.object.GameObject;

public record Prefab(GameObject gameObject) {

    public GameObject copy() {
        final GameObject result = gameObject.copy();
        result.prepare();
        return result;
    }
}
