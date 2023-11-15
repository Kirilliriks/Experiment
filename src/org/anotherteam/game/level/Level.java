package org.anotherteam.game.level;

import lombok.Getter;
import lombok.Setter;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.frame.RenderFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Level {

    public static final String LEVEL_FILE_EXTENSION = "hgl";

    @NotNull
    public static Level empty() {
        final var level = new Level("Empty");
        level.addRoom(Room.createEmpty());
        return level;
    }

    private final List<Room> rooms = new ArrayList<>();

    @Setter
    private String name;
    private Room currentRoom = null;

    public Level(String name) {
        this.name = name;
    }

    public void start() {
        if (currentRoom == null) {
            throw new IllegalStateException("Prepare level without current room");
        }

        currentRoom.start();
    }

    public void update(float dt) {
        currentRoom.update(dt);
    }

    public void render(GameRender gameRender, @NotNull RenderFrame windowFrame) {
        gameRender.render(windowFrame, currentRoom);
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

    @Nullable
    public Room getRoom(String name) {
        for (final Room room : rooms) {
            if (!room.getName().equals(name)) {
                continue;
            }

            return room;
        }
        return null;
    }
}
