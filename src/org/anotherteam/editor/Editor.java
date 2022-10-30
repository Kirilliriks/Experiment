package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.editor.level.TileViewer;
import org.anotherteam.screen.GameScreen;

public final class Editor {

    private final Game game;

    private final ImGuiLayer imGui;

    private final TileViewer tileViewer;

    private final EditorCameraController editorCameraController;

    public Editor(Game game) {
        this.game = game;
        imGui = new ImGuiLayer(GameScreen.window.getHandler(), "#version 430 core", this);

        tileViewer = new TileViewer();
        editorCameraController = new EditorCameraController();
    }

    public void init() {
        Game.DEBUG_MODE = true;
        Game.STATE_MANAGER.setState(GameState.ON_EDITOR);
    }

    /**
     * Draw and update editor elements
     */
    public void draw(float dt) {
        tileViewer.imgui();
        GameViewWindow.imgui(GameScreen.windowFrame);
        Console.imgui();


        editorCameraController.handle(dt);
    }

    /**
     * Render imgui frame
     */
    public void render(float dt) {
        imGui.imgui(dt);
    }

    public void destroy() {
        imGui.destroy();
    }
}
