package org.anotherteam.level;

import org.anotherteam.Game;
import org.anotherteam.level.room.Room;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Level {

    public final Game game;

    protected final GameRender gameRender;

    @NotNull
    protected final List<Room> rooms;

    protected Room currentRoom;

    public Level(@NotNull Game game){
        this.game = game;
        this.gameRender = game.getGameRender();
        this.rooms = new ArrayList<>();
        currentRoom = null;
        load();
    }

    public void load() { }

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
