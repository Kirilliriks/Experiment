package org.anotherteam.logger;

import org.anotherteam.Game;
import org.anotherteam.oldeditor.Editor;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class GameLogger {

    public static final Logger logger = Logger.getLogger(Game.GAME_NAME);

    public static void sendMessage(String text) {
        logger.log(Level.INFO, text);
        Editor.sendLogMessage(text);
    }
}
