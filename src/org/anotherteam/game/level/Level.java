package org.anotherteam.game.level;

import org.anotherteam.game.Game;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.frame.RenderFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        this.gameRender = Game.GAME_RENDER;
        this.rooms = new ArrayList<>();
        currentRoom = null;
    }

    public void prepare() {
        if (currentRoom == null) throw new IllegalStateException("Prepare level without current room");

        currentRoom.prepare();
    }

    public void update(float dt) {
        currentRoom.update(dt);
    }

    public void render(@NotNull RenderFrame windowFrame) {
        gameRender.render(windowFrame, currentRoom);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addRoom(@NotNull Room room) {
        if (currentRoom == null) {
            currentRoom = room; // TODO change logic
        }

        rooms.add(room);
    }

    public void removeRoom(@NotNull Room room) {
        rooms.remove(room);

        if (currentRoom == room) {
            currentRoom = rooms.get(0);
        }
    }

    public void setCurrentRoom(String roomName) {
        this.currentRoom = getRoom(roomName);
    }

    @NotNull
    public Room getCurrentRoom() {
        return currentRoom;
    }

    @Nullable
    public Room getRoom(String name) {
        for (final var room : rooms) {
            if (!room.getName().equals(name)) continue;

            return room;
        }
        return null;
    }

    @NotNull
    public List<Room> getRooms() {
        return rooms;
    }

    @NotNull
    public static Level empty() {
        final var level = new Level("Empty");
        level.addRoom(Room.createEmpty());
        return level;
    }
}
