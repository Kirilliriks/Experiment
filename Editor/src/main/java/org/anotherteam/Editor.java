package org.anotherteam;

import imgui.ImGui;
import org.anotherteam.core.Core;
import org.anotherteam.game.Game;
import org.anotherteam.game.GameState;
import org.anotherteam.level.EditorCameraController;
import org.anotherteam.level.LoadWindow;
import org.anotherteam.level.TileViewer;
import org.anotherteam.render.ImGuiRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.FileUtils;
import org.anotherteam.util.Popups;
import org.anotherteam.widget.Console;

public final class Editor implements Core {

    private static Editor instance;

    private final Window window;
    private final Game game;

    private final ImGuiRender imGui;

    private final TileViewer tileViewer;
    private final LoadWindow loadWindow;
    private final Console console;

    private final EditorCameraController editorCameraController;

    public Editor(Window window) {
        instance = this;
        this.window = window;
        this.game = new Game(window);

        imGui = new ImGuiRender(GameScreen.getWindow().getHandler(), "#version 430 core", this);
        tileViewer = new TileViewer(GameScreen.getWindow().getWidth() / 2 - 320, GameScreen.getWindow().getHeight() / 2 - 200, 640, 400);
        loadWindow = new LoadWindow();
        console = new Console(GameScreen.getWindow().getWidth() / 2 - 320, GameScreen.getWindow().getHeight() / 2 - 200, 640, 400);
        editorCameraController = new EditorCameraController();
    }

    @Override
    public void init() {
        game.init();

        Game.DEBUG_MODE = true;
        Game.STATE_MANAGER.setState(GameState.ON_EDITOR);
    }

    @Override
    public void update(float dt) {
        game.update(dt);
    }

    /**
     * Render imgui frame
     */
    @Override
    public void render(float dt) {
        game.render(dt);

        if (Game.STATE_MANAGER.getState() != GameState.ON_EDITOR) return;
        editorCameraController.handle(dt);

        imGui.render(dt);
    }

    @Override
    public void destroy() {
        game.destroy();
        imGui.destroy();
    }

    public void imgui(float dt) {
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

            if (ImGui.menuItem("Exit")) {
                Game.STATE_MANAGER.setState(GameState.ON_CLOSE_GAME);
            }

            ImGui.endMenu();
        }

        ImGui.dummy(8.0f, 00.0f);

        if (ImGui.beginMenu("Level")) {

            if (ImGui.menuItem("Save (CTRL+S)")) {
                FileUtils.saveEditorLevel(Game.LEVEL_MANAGER.getCurrent());
            }

            if (ImGui.menuItem("Load")) {
                Popups.LOAD_LEVEL = true;
            }

            if (ImGui.menuItem("Cancel changes")) {
                Game.LEVEL_MANAGER.load(Game.LEVEL_MANAGER.getCurrent().getName());
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

        if (Popups.LOAD_LEVEL) {
            loadWindow.call();
        }

        loadWindow.update();
        tileViewer.update();
        console.update();
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
