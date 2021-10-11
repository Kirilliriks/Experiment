package org.anotherteam.level;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.type.entity.EntityObject;
import org.anotherteam.object.type.entity.manager.EntityManager;
import org.anotherteam.render.GameRender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Level {

    public final Game main;

    protected final GameRender gameRender;

    protected final EntityManager entityManager;

    @NotNull
    protected final List<Room> rooms;
    @NotNull
    protected final List<GameObject> gameObjects;

    public Level(@NotNull Game game){
        this.main = game;
        this.gameRender = new GameRender(main.getGameScreen(),this);
        this.entityManager = new EntityManager();
        this.rooms = new ArrayList<>();
        this.gameObjects = new ArrayList<>();
        load();
    }

    public abstract void load();

    public void addRoom(@NotNull Room room) {
        rooms.add(room);
    }

    public void addObject(@NotNull GameObject object) {
        if (object instanceof EntityObject entity) entityManager.addEntity(entity);
        gameObjects.add(object);
        gameObjects.sort(Comparator.comparingInt(GameObject::getRenderPriority));
    }

    public void rewoveObject(@NotNull GameObject object) {
        if (object instanceof EntityObject entity) entityManager.removeEntity(entity);
        gameObjects.remove(object);
    }

    public void update(float delta) {
        for (val object : gameObjects){
            object.update(delta);
        }
    }

    public void render() {
        gameRender.render();
    }

    @NotNull
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    @NotNull
    public List<Room> getRooms() {
        return rooms;
    }

    public void clear() {
        for (val room : rooms) {
            room.destroy();
        }
        rooms.clear();
        gameObjects.clear();
    }
}
