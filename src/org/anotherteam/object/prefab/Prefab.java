package org.anotherteam.object.prefab;

import org.anotherteam.object.GameObject;

public interface Prefab {

    Class<? extends GameObject> getPrefabClass();
}
