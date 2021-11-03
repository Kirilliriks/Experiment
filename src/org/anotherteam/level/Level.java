package org.anotherteam.level;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.object.entity.Player;
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
        this.gameRender = Game.game.gameRender;
        this.rooms = new ArrayList<>();
        currentRoom = null;
    }

    public String getName() {
        return name;
    }

    public void addRoom(@NotNull Room room) {
        if (currentRoom == null) currentRoom = room;
        rooms.add(room);
    }

    public void update(float dt) {
        currentRoom.update(dt);
    }

    public void render(@NotNull RenderBatch windowBatch) {
        gameRender.render(windowBatch, this);
    }

    @NotNull
    public Room getCurrentRoom() {
        return currentRoom;
    }

    @NotNull
    public List<Room> getRooms() {
        return rooms;
    }

    public static Level createEmpty() {
        val level = new Level("Empty");
        val room = new Room(0, 0, "Empty");
        room.addObject(new Player(0, 0));
        level.addRoom(room);
        return level;
    }
}
