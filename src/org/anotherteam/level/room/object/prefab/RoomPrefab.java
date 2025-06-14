package org.anotherteam.level.room.object.prefab;

import org.anotherteam.level.room.object.room.*;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.prefab.Prefab;

public enum RoomPrefab implements Prefab {
    TEST_CUPBOARD(TestCupboard.class),
    BED_218(Bed218.class),
    LAMP_128(Lamp218.class),
    LIGHT_TEST(LightTest.class),
    DUCT_CORRIDOR_ROOM(DuctCorridorRoom.class),
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
