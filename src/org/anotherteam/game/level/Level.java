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
    private Room room = null;

    public Level(String name) {
        this.name = name;
    }

    public void start() {
        if (room == null) {
            throw new IllegalStateException("Prepare level without current room");
        }

        room.start();
    }

    public void update(float dt) {
        room.update(dt);
    }

    public void render(GameRender gameRender, @NotNull RenderFrame windowFrame) {
        gameRender.render(windowFrame, room);
    }

    public void addRoom(@NotNull Room room) {
        if (this.room == null) {
            this.room = room; // TODO change logic
        }

        rooms.add(room);
    }

    public void removeRoom(@NotNull Room room) {
        rooms.remove(room);

        if (this.room == room) {
            this.room = rooms.get(0);
        }
    }

    public void setRoom(String roomName) {
        this.room = getRoom(roomName);
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
