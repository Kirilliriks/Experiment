package org.anotherteam.util;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.level.Level;
import org.anotherteam.logger.GameLogger;
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

public final class FileUtils {

    public static Level loadLevel(String levelName) {
        String inFile;
        try {
            final String finalName = levelName  + (levelName.split("\\.").length > 1 ? "" : "." + Level.LEVEL_FILE_EXTENSION);
            final Path path = Paths.get(AssetData.LEVELS_PATH + finalName);

            if (!path.toFile().isFile()) {
                GameLogger.log("Level " + levelName + " not found");
                return null;
            }

            inFile = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            GameLogger.log("Level " + levelName + " load call error " + e.getMessage());
            return null;
        }

        if (inFile.isEmpty()) {
            GameLogger.log("Level " + levelName + " is empty");
            return null;
        }

        final var level = SerializeUtil.GSON.fromJson(inFile, Level.class);
        GameLogger.log("Level " + level.getName() + " loaded");
        return level;
    }

    public static void saveEditorLevel(@NotNull Level level) {
        try {
            final var writer = new FileWriter(AssetData.LEVELS_PATH + level.getName() + "." + Level.LEVEL_FILE_EXTENSION);
            writer.write(SerializeUtil.GSON.toJson(level));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
            GameLogger.log("Level " + level.getName() + " save call error " + e.getMessage());
            return;
        }

        GameLogger.log("Level " + level.getName() + " saved");


    }

    public static void deleteLevel(@NotNull Level level) {
        final var file = new File(AssetData.LEVELS_PATH + level.getName()  + "." + Level.LEVEL_FILE_EXTENSION);
        if (!file.isFile())
            throw new LifeException("Not find level: " + level.getName());
        if (!file.delete())
            throw new LifeException("Can't delete level: " + level.getName());

        GameLogger.log("Level " + level.getName() + " deleted");
    }

    /**
     * Change level FILE name to new name, but not rename level object
     * @param newName new file name
     * @param level level
     */
    public static void renameLevel(String newName, @NotNull Level level) {
        final var oldPath = Paths.get(AssetData.LEVELS_PATH + level.getName()  + "." + Level.LEVEL_FILE_EXTENSION);
        final var newPath = Paths.get(AssetData.LEVELS_PATH + newName  + "." + Level.LEVEL_FILE_EXTENSION);
        try {
            Files.move(oldPath, newPath);
        } catch (IOException e) {
            throw new LifeException("Can't rename level: " + level.getName());
        }
    }

    public static String getNameFromFile(String fileName) {
        final var str = fileName.split("\\.");
        return str[str.length - 2];
    }

    public static String loadAsString(String fileName) {
        final var builder = new StringBuilder();
        BufferedReader reader;
        try {
            final File file = new File(fileName);
            if (!file.isFile()) {
                throw new LifeException("Shader not found " + fileName);
            }

            final FileReader fileReader = new FileReader(fileName);
            reader = new BufferedReader(fileReader);
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

        final var path = Paths.get(resource);

        if (Files.isReadable(path)) {
            try (final var fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) {}
            } catch (Exception e) {
                throw new LifeException("Bad file loading " + resource);
            }
        } else {
            try {
                final var source = FileUtils.class.getClassLoader().getResourceAsStream(resource);
                if (source == null) {
                    throw new LifeException("Null input " + resource);
                }

                final var rbc = Channels.newChannel(source);
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (rbc.read(buffer) != -1) {
                    if (buffer.remaining() != 0) continue;

                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                }
            } catch (Exception e) {
                throw new LifeException("Bad file loading " + resource);
            }
        }

        return memSlice(buffer.flip());
    }

    @NotNull
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        final var newBuffer = BufferUtils.createByteBuffer(newCapacity);
        newBuffer.put(buffer.flip());
        return newBuffer;
    }
}
