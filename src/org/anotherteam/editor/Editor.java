package org.anotherteam.editor;

import imgui.ImGui;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.editor.level.TileViewer;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.screen.GameScreen;

public final class Editor {

    private static Editor instance;

    private final Game game;

    private final ImGuiRender imGui;

    private final TileViewer tileViewer;

    private final EditorCameraController editorCameraController;

    public Editor(Game game) {
        instance = this;

        this.game = game;
        imGui = new ImGuiRender(GameScreen.getWindow().getHandler(), "#version 430 core", this);

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
    public void update(float dt) {
        ImGui.beginMainMenuBar();

        tileViewer.update();
        Console.update();

        ImGui.endMainMenuBar();
    }

    /**
     * Render imgui frame
     */
    public void render(float dt) {
        if (Game.STATE_MANAGER.getState() != GameState.ON_EDITOR) return;
        editorCameraController.handle(dt);

        imGui.imgui(dt);
    }

    public void destroy() {
        imGui.destroy();
    }

    public static void switchPlayStopMode() {
        if (Game.STATE_MANAGER.getState() == GameState.ON_EDITOR) {
            Game.DEBUG_MODE = false;
            Game.STATE_MANAGER.setState(GameState.ON_LEVEL);

            restoreGameView();
            Game.LEVEL_MANAGER.loadLevel(Game.START_LEVEL_NAME); // TODO
        } else {
            Game.DEBUG_MODE = true;
            Game.STATE_MANAGER.setState(GameState.ON_EDITOR);

            restoreGameView();
            Game.LEVEL_MANAGER.loadLevel(Game.START_LEVEL_NAME); // TODO
        }

        GameLogger.sendMessage("Current state: " + Game.STATE_MANAGER.getState());
    }

    private static void restoreGameView() {
        GameScreen.WIDTH = GameScreen.DEFAULT_WIDTH;
        GameScreen.HEIGHT = GameScreen.DEFAULT_HEIGHT;
        GameScreen.RENDER_WIDTH = GameScreen.getWindow().getWidth();
        GameScreen.RENDER_HEIGHT = GameScreen.getWindow().getHeight();
        GameScreen.POSITION.set(0, 0);

        Game.getGameRender().updateFrames(GameScreen.WIDTH, GameScreen.HEIGHT);
    }

    public static Editor getInstance() {
        return instance;
    }
}
