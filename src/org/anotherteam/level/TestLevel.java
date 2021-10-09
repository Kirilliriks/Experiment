package org.anotherteam.level;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.data.TextureData;
import org.anotherteam.level.global.Wall;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.type.entity.player.Player;
import org.joml.Vector2i;

public final class TestLevel extends Level {


    public TestLevel(Game main) {
        super(main);
    }

    @Override
    public void load() {
        val testRoom = new Room(new Vector2i(10, 20), new Vector2i(9,4));
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 4; y++) {
                testRoom.addTile(x,y, new Tile(x,y, 64, TextureData.TEST_TEXTURE));
            }
        }
        addRoom(testRoom);
        addObject(new Player(new Vector2i(26, 29), this));
        addObject(new Wall(new Vector2i(6, 29), this));
        addObject(new Wall(new Vector2i(150, 29), this));
    }
}
