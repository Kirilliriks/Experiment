package org.anotherteam.game.level.room;

import lombok.Getter;
import lombok.Setter;
import org.anotherteam.game.Game;
import org.anotherteam.game.level.room.object.Prefabs;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.type.player.PlayerController;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.*;

@Getter
@Setter
public final class Room {

    private final Map<Vector2i, Tile> tiles;
    private final List<GameObject> gameObjects; // TODO make Nested List to fast DrawPriority sorting

    private String name;
    private GameObject player;

    public Room(String name) {
        this.name = name;

        tiles = new HashMap<>();
        gameObjects = new ArrayList<>();
        player = null;
    }

    public void start() {
        for (final var object : gameObjects) {
            object.start();
        }
    }

    public void update(float dt) {
        for (final var object : gameObjects){
            object.update(dt);
        }
    }

    public void draw(@NotNull RenderBatch renderBatch, boolean height) {
        for (final var tile : tiles.values()) {
            tile.draw(renderBatch, height);
        }

        for (final var gameObject : gameObjects) {
            gameObject.draw(renderBatch, height);
        }
    }

    public void debugDraw(@NotNull RenderBatch renderBatch) {
        if (!Game.DEBUG) return;

        for (final var gameObject : gameObjects) {
            gameObject.debugDraw(renderBatch, false);
        }
    }

    public void addObject(@NotNull GameObject object) {
        final var playerController = object.getComponent(PlayerController.class);
        if (playerController != null) {
            player = object;
        }

        object.setRoom(this);

        gameObjects.add(object);
        gameObjects.sort(Comparator.comparingInt(GameObject::getRenderPriority));
    }

    public void rewoveObject(@NotNull GameObject object) {
        gameObjects.remove(object);
        object.setRoom(null);
    }

    public GameObject getPlayer() {
        if (player == null) {
            addObject(Prefabs.PLAYER.copy());
        }

        return player;
    }

    @NotNull
    public Collection<Tile> getTiles() {
        return tiles.values();
    }

    public void setTile(@NotNull Tile tile) {
        tiles.put(tile.getPosition(), tile);
    }

    public Tile getTile(int x, int y) {
        return tiles.get(new Vector2i(x, y));
    }

    public void removeTile(int x, int y) {
        tiles.remove(new Vector2i(x, y));
    }

    @NotNull
    public static Room createEmpty() {
        final var room = new Room("EmptyRoom");
        room.addObject(Prefabs.PLAYER.copy());
        return room;
    }
}
