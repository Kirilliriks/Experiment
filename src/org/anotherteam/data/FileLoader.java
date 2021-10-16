package org.anotherteam.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.val;
import org.anotherteam.data.level.LevelDeserializer;
import org.anotherteam.data.level.room.RoomDeserializer;
import org.anotherteam.data.level.room.gameobject.ComponentDeserializer;
import org.anotherteam.data.level.room.gameobject.GameObjectDeserializer;
import org.anotherteam.data.level.room.tile.TileDeserializer;
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

    public static Level loadLevel(String levelName) {
        val gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Tile.class, new TileDeserializer())
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .registerTypeAdapter(Room.class, new RoomDeserializer())
                .registerTypeAdapter(Level.class, new LevelDeserializer())
                .create();
        String inFile = "";
        try {
            val finalName = levelName  + (levelName.split("\\.").length > 1 ? "" : ".hgl");
            inFile = new String(Files.readAllBytes(Paths.get("levels/" + finalName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inFile.isEmpty()) Editor.sendLogMessage("Level " + levelName + " is empty");
        else Editor.sendLogMessage("Level " + levelName + " loaded");
        return gson.fromJson(inFile, Level.class);
    }

    public static void saveLevel(Level level) {
        val gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Tile.class, new TileDeserializer())
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .registerTypeAdapter(Room.class, new RoomDeserializer())
                .registerTypeAdapter(Level.class, new LevelDeserializer())
                .create();
        try {
            val writer = new FileWriter("levels/" + level.getName()  + ".hgl");
            writer.write(gson.toJson(level));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        Editor.sendLogMessage("Level " + level.getName() + " saved");
    }
}
