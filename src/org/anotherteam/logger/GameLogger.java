package org.anotherteam.logger;

import java.util.ArrayDeque;
import java.util.Queue;

public final class GameLogger {

    public static final int MAX_SIZE = 16;

    public static final Queue<String> STRINGS = new ArrayDeque<>();

    public static void log(String text) {
        if (STRINGS.size() >= MAX_SIZE) {
            STRINGS.remove();
        }
        STRINGS.add(text);
        System.out.println("[INFO] " + text);
    }
}
