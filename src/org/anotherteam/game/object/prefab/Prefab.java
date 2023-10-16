package org.anotherteam.game.object.prefab;

import org.anotherteam.game.object.GameObject;

public record Prefab(GameObject gameObject) {

    public GameObject copy() {
        return gameObject.copy();
    }
}
