package org.anotherteam;

import org.anotherteam.render.window.Window;
import org.anotherteam.util.GLUtils;

public class EditorLauncher {
    public static void main(String[] args) {
        GLUtils.init();

        final var window = new Window(1920, 1080, "Experimental");
        window.create();

        new CoreLauncher(new Editor(window), window);
    }
}