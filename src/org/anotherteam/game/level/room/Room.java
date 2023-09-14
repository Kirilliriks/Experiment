package org.anotherteam.game.level.room;

import lombok.Getter;
import lombok.Setter;
import org.anotherteam.game.Game;
import org.anotherteam.game.level.room.object.entity.Player;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.game.object.GameObject;
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

    private Player player;

    public Room(String name) {
        this.name = name;

        tiles = new HashMap<>();
        gameObjects = new ArrayList<>();
        player = null;
    }

    public void prepare() {
        for (final var object : gameObjects) {
            object.prepare();
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
        if (!Game.DEBUG_MODE) return;

        for (final var gameObject : gameObjects) {
            gameObject.debugDraw(renderBatch, false);
        }
    }

    public void addObject(@NotNull GameObject object) {
        if (object instanceof Player) {
            player = (Player) object;
        }
        object.setRoom(this);
        gameObjects.add(object);
        gameObjects.sort(Comparator.comparingInt(GameObject::getRenderPriority));
    }

    public void rewoveObject(@NotNull GameObject object) {
        gameObjects.remove(object);
        object.setRoom(null);
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
        room.addObject(new Player());
        return room;
    }
}
