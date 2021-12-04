package org.anotherteam.level.room.object.prefab;

import org.anotherteam.level.room.object.room.DoorCorridor;
import org.anotherteam.level.room.object.room.TestCupboard;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.prefab.Prefab;

public enum RoomPrefab implements Prefab {
    TEST_CUPBOARD(TestCupboard.class),
    DOOR_CORRIDOR(DoorCorridor.class);

    final Class<? extends GameObject> clazz;
    RoomPrefab(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends GameObject> getPrefabClass() {
        return clazz;
    }
}
