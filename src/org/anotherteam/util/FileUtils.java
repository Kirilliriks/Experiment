package org.anotherteam.util;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.system.MemoryUtil.memSlice;

public final class FileUtils {
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
            try (
                val source = PixmapUtil.class.getClassLoader().getResourceAsStream(resource);
                val rbc = Channels.newChannel(source)
            ) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (rbc.read(buffer) != -1) {
                    if (buffer.remaining() != 0) continue;

                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                }
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
