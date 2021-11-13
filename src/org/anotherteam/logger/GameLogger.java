package org.anotherteam.logger;

import org.anotherteam.editor.Editor;

public final class GameLogger {

    public static void sendMessage(String text) {
        Editor.sendLogMessage(text);
    }
}
