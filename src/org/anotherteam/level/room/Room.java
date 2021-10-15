package org.anotherteam.level.room;

import lombok.val;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.GameObject;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public final class Room {
    private final static Vector2i DEFAULT_POSITION = new Vector2i(10, 20);

    private final String name;

    private final Tile[] tiles;
    private final List<GameObject> gameObjects;

    private final Vector2i size;

    public Room(int x, int y, String name) {
        this.name = name;
        this.size = new Vector2i(x, y);
        this.tiles = new Tile[size.x * size.y];
        this.gameObjects = new ArrayList<>();
    }

    public void load() { }

    public void update(float dt) {
        for (val object : gameObjects){
            object.update(dt);
        }
    }

    public void drawTexture(@NotNull RenderBatch renderBatch) {
        for (val tile : tiles) {
            renderBatch.draw(tile.getTextureSprite(), DEFAULT_POSITION.x + tile.getPosition().x * Tile.SIZE.x, DEFAULT_POSITION.y + tile.getPosition().y * Tile.SIZE.y);
        }

        for (val gameObject : gameObjects) {
            gameObject.render(renderBatch);
        }
    }

    public void drawHeight(@NotNull RenderBatch renderBatch) {
        for (val tile : tiles) {
            renderBatch.draw(tile.getHeightSprite(), DEFAULT_POSITION.x + tile.getPosition().x * Tile.SIZE.x, DEFAULT_POSITION.y + tile.getPosition().y * Tile.SIZE.y);
        }
    }

    @NotNull
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(@NotNull GameObject object) {
        object.setRoom(this);
        gameObjects.add(object);
        gameObjects.sort(Comparator.comparingInt(GameObject::getRenderPriority));
    }

    public void rewoveObject(@NotNull GameObject object) {
        gameObjects.remove(object);
        object.setRoom(null);
    }

    @NotNull
    public Tile[] getTiles() {
        return tiles;
    }

    public void addTile(@NotNull Tile tile) {
        tiles[tile.getPosition().x + tile.getPosition().y * size.x] =  tile;
    }

    public int getWidth() {
        return size.x;
    }

    public int getHeight() {
        return size.y;
    }

    public String getName() {
        return name;
    }
}
