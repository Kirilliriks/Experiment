package org.anotherteam.game.level.room.object;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.type.player.PlayerController;
import org.anotherteam.game.object.component.type.sprite.SpriteComponent;
import org.anotherteam.game.object.component.type.state.StateComponent;
import org.anotherteam.game.object.prefab.Prefab;
import org.anotherteam.render.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

public final class Prefabs {

    public static final List<Prefab> VALUES = new ArrayList<>();
    public static final GameObject PLAYER = add(makePlayer());

    static {
        initStatics();
        initEntity();
    }

    private static GameObject add(GameObject gameObject) {
        VALUES.add(new Prefab(gameObject));
        return gameObject;
    }

    public static void initStatics() {
        final var wall = add(new GameObject("wall", 0, 0));
        wall.getCollider().setBounds(2, 32);
        wall.getCollider().setSolid(true);

        makeStatic("bed218").getComponent(SpriteComponent.class).setSpriteAtlas(
                AssetData.ROOM_OBJECTS_PATH + "BED_218.png", 31, 12
        );

        makeStatic("door_corridor").getComponent(SpriteComponent.class).setSpriteAtlas(
                AssetData.ROOM_OBJECTS_PATH + "door_corridor_room.png", 21, 30
        );

        makeStatic("door_corridor_room").getComponent(SpriteComponent.class).setSpriteAtlas(
                AssetData.ROOM_OBJECTS_PATH + "duct_corridor_room.png", 31, 49
        );

        makeStatic("lamp218").getComponent(SpriteComponent.class).setSpriteAtlas(
                AssetData.ROOM_OBJECTS_PATH + "LAMP_218.png", 12, 18
        );

        makeStatic("light_test").getComponent(SpriteComponent.class).setSpriteAtlas(
                AssetData.ROOM_OBJECTS_PATH + "LIGHT_TEST.png", 32, 32
        );

        makeStatic("test_cupboard").getComponent(SpriteComponent.class).setSpriteAtlas(
                AssetData.ROOM_OBJECTS_PATH + "cupboard_test.png", 20, 36
        );
    }

    public static void initEntity() {

    }

    private static GameObject makeStatic(String name) {
        final var staticObject = add(new GameObject(name, 0, 0));
        final var spriteComponent = new SpriteComponent();
        staticObject.addComponent(spriteComponent);
        return staticObject;
    }

    private static GameObject makeEntity(String name, String states) {
        final var entity = add(new GameObject(name, 0, 0));

        final var spriteComponent = new SpriteComponent();
        spriteComponent.setDrawPriority(1);
        entity.addComponent(spriteComponent);

        final var stateComponent = new StateComponent();
        stateComponent.setStates(states);
        entity.addComponent(stateComponent);
        return entity;
    }

    private static GameObject makePlayer() {
        final var player = new GameObject("player", 0, 0);

        final var spriteComponent = new SpriteComponent();
        spriteComponent.setDrawPriority(1);
        spriteComponent.setSpriteAtlas(
                AssetData.ENTITY_PATH + "testPlayerAtlas.png", Sprite.DEFAULT_SIZE.x, Sprite.DEFAULT_SIZE.y
        );
        player.addComponent(spriteComponent);

        final var stateComponent = new StateComponent();
        stateComponent.setStates("player");
        player.addComponent(stateComponent);

        player.getCollider().setBounds(-6, 0, 6, 32);
        player.getCollider().setInteractBounds(0, 0, 16, 32);
        player.getTransform().setSpeed(25);
        player.addComponent(new PlayerController());

        return player;
    }
}
