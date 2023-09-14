package org.anotherteam.game.object.prefab;

import org.anotherteam.game.object.GameObject;

public interface Prefab {

    Class<? extends GameObject> getPrefabClass();
}
