package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.editor.level.TileViewer;
import org.anotherteam.screen.GameScreen;

public final class Editor {

    private final Game game;

    private final ImGuiLayer imGui;

    private final TileViewer tileViewer;

    public Editor(Game game) {
        this.game = game;
        imGui = new ImGuiLayer(GameScreen.window.getHandler(), "#version 430 core", this);

        tileViewer = new TileViewer();
    }

    /**
     * Draw and update editor elements
     */
    public void draw() {
        tileViewer.imgui();
        Console.imgui();
    }

    /**
     * Render imgui frame
     */
    public void render() {
        imGui.imgui();
    }

    public void destroy() {
        imGui.destroy();
    }
}
