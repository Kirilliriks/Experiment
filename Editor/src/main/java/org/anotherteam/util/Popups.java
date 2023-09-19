package org.anotherteam.util;

import org.anotherteam.level.LoadWindow;

public final class Popups {

    public static final LoadWindow LOAD_WINDOW = new LoadWindow();

    public static void update() {
        if (LOAD_WINDOW.isSelected() && !LOAD_WINDOW.isOpened()) {
            LOAD_WINDOW.call();
        }

        LOAD_WINDOW.update();
    }
}
