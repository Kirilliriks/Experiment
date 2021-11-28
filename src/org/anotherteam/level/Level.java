package org.anotherteam.level;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.level.room.Room;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Level {
    public static final String LEVEL_FILE_EXTENSION = "hgl";

    private final GameRender gameRender;
    private final List<Room> rooms;

    private String name;
    private Room currentRoom;

    public Level(String name) {
        this.name = name;
        this.gameRender = Game.gameRender;
        this.rooms = new ArrayList<>();
        currentRoom = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addRoom(@NotNull Room room) {
        if (currentRoom == null) currentRoom = room; // TODO change logic
        rooms.add(room);
    }

    public void removeRoom(@NotNull Room room) {
        rooms.remove(room);
        if (currentRoom == room)
            currentRoom = rooms.get(0);
    }

    public void setCurrentRoom(@NotNull Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void update(float dt) {
        currentRoom.update(dt);
    }

    public void render(@NotNull RenderBatch windowBatch) {
        gameRender.render(windowBatch, currentRoom);
    }

    @NotNull
    public Room getCurrentRoom() {
        return currentRoom;
    }

    @NotNull
    public List<Room> getRooms() {
        return rooms;
    }

    @NotNull
    public static Level createEmpty() {
        val level = new Level("Empty");
        level.addRoom(Room.createEmpty());
        return level;
    }
}
