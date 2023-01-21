package org.anotherteam.editor;

import imgui.ImGui;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.editor.level.TileViewer;
import org.anotherteam.editor.render.ImGuiRender;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.screen.GameScreen;

public final class Editor {

    private static Editor instance;

    private final Game game;

    private final ImGuiRender imGui;

    private final TileViewer tileViewer;
    private final Console console;

    private final EditorCameraController editorCameraController;

    public Editor(Game game) {
        instance = this;
        this.game = game;

        imGui = new ImGuiRender(GameScreen.getWindow().getHandler(), "#version 430 core", this);
        tileViewer = new TileViewer(GameScreen.getWindow().getWidth() / 2 - 320, GameScreen.getWindow().getHeight() / 2 - 200, 640, 400);
        console = new Console(GameScreen.getWindow().getWidth() / 2 - 320, GameScreen.getWindow().getHeight() / 2 - 200, 640, 400);
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
        if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
            GameScreen.setDraggedThing(null);
        }

        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("Menu")) {

            if (ImGui.menuItem("Open tile viewer")) {
                tileViewer.reset();
            }

            if (ImGui.menuItem("Open console")) {
                console.reset();
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

        tileViewer.update();
        console.update();
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
        final boolean onEditor = Game.STATE_MANAGER.getState() == GameState.ON_EDITOR;

        if (onEditor) {
            Game.STATE_MANAGER.setState(GameState.ON_LEVEL);
        } else {
            Game.STATE_MANAGER.setState(GameState.ON_EDITOR);
        }

        Game.DEBUG_MODE = !onEditor;

        resetGameView();
        Game.LEVEL_MANAGER.load(Game.START_LEVEL_NAME); // TODO

        GameLogger.log("Current state: " + Game.STATE_MANAGER.getState());
    }

    private static void resetGameView() {
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
