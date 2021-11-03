package org.anotherteam.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.val;
import org.anotherteam.data.deserialization.LevelDeserializer;
import org.anotherteam.data.deserialization.room.RoomDeserializer;
import org.anotherteam.data.deserialization.room.gameobject.ComponentDeserializer;
import org.anotherteam.data.deserialization.room.gameobject.GameObjectDeserializer;
import org.anotherteam.data.deserialization.room.tile.TileDeserializer;
import org.anotherteam.editor.Editor;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileLoader {

    public static final Gson LEVEL_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Tile.class, new TileDeserializer())
            .registerTypeAdapter(Component.class, new ComponentDeserializer())
            .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
            .registerTypeAdapter(Room.class, new RoomDeserializer())
            .registerTypeAdapter(Level.class, new LevelDeserializer())
            .create();

    public static Level loadLevel(String levelName) {
        String inFile = "";
        try {
            val finalName = levelName  + (levelName.split("\\.").length > 1 ? "" : "." + Level.LEVEL_FILE_EXTENSION);
            inFile = new String(Files.readAllBytes(Paths.get("levels/" + finalName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inFile.isEmpty()) Editor.sendLogMessage("Level " + levelName + " is empty");
        else Editor.sendLogMessage("Level " + levelName + " loaded");
        return LEVEL_GSON.fromJson(inFile, Level.class);
    }

    public static void saveLevel(Level level) {
        try {
            val writer = new FileWriter("levels/" + level.getName()  + "." + Level.LEVEL_FILE_EXTENSION);
            writer.write(LEVEL_GSON.toJson(level));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        Editor.sendLogMessage("Level " + level.getName() + " saved");
    }
}
