package org.anotherteam.game.level.room.object.prefab;

import lombok.RequiredArgsConstructor;
import org.anotherteam.game.level.room.object.room.*;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.prefab.Prefab;

@RequiredArgsConstructor
public enum RoomPrefab implements Prefab {
    TEST_CUPBOARD(new TestCupboard()),
    BED_218(new Bed218()),
    LAMP_128(new Lamp218()),
    LIGHT_TEST(new LightTest()),
    DUCT_CORRIDOR_ROOM(new DuctCorridorRoom()),
    DOOR_CORRIDOR(new DoorCorridor());

    final GameObject gameObject;

    @Override
    public GameObject getPrefab() {
        return gameObject;
    }
}
