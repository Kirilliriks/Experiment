package org.anotherteam.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.anotherteam.data.deserialization.LevelDeserializer;
import org.anotherteam.data.deserialization.room.RoomDeserializer;
import org.anotherteam.data.deserialization.room.gameobject.ComponentDeserializer;
import org.anotherteam.data.deserialization.room.gameobject.GameObjectDeserializer;
import org.anotherteam.data.deserialization.room.tile.TileDeserializer;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.system.MemoryUtil.memSlice;

@UtilityClass
public final class FileUtils {

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
            final String finalName = levelName  + (levelName.split("\\.").length > 1 ? "" : "." + Level.LEVEL_FILE_EXTENSION);
            final Path path = Paths.get("levels/" + finalName);

            if (!path.toFile().isFile()) {
                GameLogger.sendMessage("Level " + levelName + " not found");
                return Level.createEmpty();
            }

            inFile = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inFile.isEmpty()) {
            GameLogger.sendMessage("Level " + levelName + " is empty");
        }  else {
            GameLogger.sendMessage("Level " + levelName + " loaded");
        }
        return LEVEL_GSON.fromJson(inFile, Level.class);
    }

    public static void saveEditableLevel(@NotNull Level level) {
        try {
            val writer = new FileWriter("levels/" + level.getName()  + "." + Level.LEVEL_FILE_EXTENSION);
            writer.write(LEVEL_GSON.toJson(level));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        GameLogger.sendMessage("Level " + level.getName() + " saved");
    }

    public static void deleteLevel(@NotNull Level level) {
        val file = new File("levels/" + level.getName()  + "." + Level.LEVEL_FILE_EXTENSION);
        if (!file.isFile())
            throw new LifeException("Not find level: " + level.getName());
        if (!file.delete())
            throw new LifeException("Can't delete level: " + level.getName());
        GameLogger.sendMessage("Level " + level.getName() + " deleted");
    }

    /**
     * Change level FILE name to new name, but not rename level object
     * @param newName new file name
     * @param level level
     */
    public static void renameLevel(String newName, @NotNull Level level) {
        val oldPath = Paths.get("levels/" + level.getName()  + "." + Level.LEVEL_FILE_EXTENSION);
        val newPath = Paths.get("levels/" + newName  + "." + Level.LEVEL_FILE_EXTENSION);
        try {
            Files.move(oldPath, newPath);
        } catch (IOException e) {
            throw new LifeException("Can't rename level: " + level.getName());
        }
    }

    public static String getNameFromFile(String fileName) {
        val str = fileName.split("\\.");
        return str[str.length - 2];
    }

    public static String loadAsString(String fileName) {
        val builder = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        val path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (val fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) { }
            }
        } else {
            try {
                val source = PixmapUtil.class.getClassLoader().getResourceAsStream(resource);
                if (source == null) throw new LifeException("Null input");

                val rbc = Channels.newChannel(source);
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (rbc.read(buffer) != -1) {
                    if (buffer.remaining() != 0) continue;

                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                }
            } catch (Exception e) {
                throw new LifeException("Bad file loading");
            }
        }

        return memSlice(buffer.flip());
    }

    @NotNull
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        val newBuffer = BufferUtils.createByteBuffer(newCapacity);
        newBuffer.put(buffer.flip());
        return newBuffer;
    }
}
