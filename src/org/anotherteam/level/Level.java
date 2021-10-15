package org.anotherteam.level;

import com.google.gson.Gson;
import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.data.level.LevelDeserializer;
import org.anotherteam.data.level.room.RoomDeserializer;
import org.anotherteam.data.level.room.gameobject.ComponentDeserializer;
import org.anotherteam.data.level.room.gameobject.GameObjectDeserializer;
import org.anotherteam.data.level.room.tile.TileDeserializer;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Level {
    private final String name;
    private final GameRender gameRender;
    private final List<Room> rooms;

    private Room currentRoom;

    public Level(String name) {
        this.name = name;
        this.gameRender = Game.getInstance().getGameRender();
        this.rooms = new ArrayList<>();
        currentRoom = null;
    }

    public String getName() {
        return name;
    }

    public void addRoom(@NotNull Room room) {
        if (rooms.size() == 0) currentRoom = room;
        rooms.add(room);
    }

    public void update(float dt) {
        currentRoom.update(dt);
    }

    public void render(@NotNull RenderBatch windowBatch) {
        gameRender.render(windowBatch, this);
    }

    @NotNull
    public List<Room> getRooms() {
        return rooms;
    }

    public void clear() {
        rooms.clear();
    }
}
