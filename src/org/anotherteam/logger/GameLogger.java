package org.anotherteam.logger;

import org.anotherteam.game.Game;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GameLogger {

    public static final int MAX_SIZE = 16;

    public static final Queue<String> STRINGS = new ArrayDeque<>();

    public static final Logger logger = Logger.getLogger(Game.GAME_NAME);

    public static void log(String text) {
        if (STRINGS.size() >= MAX_SIZE) {
            STRINGS.remove();
        }
        STRINGS.add(text);
        logger.log(Level.INFO, text);
    }
}
