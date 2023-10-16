package org.anotherteam;

import imgui.ImGui;
import imgui.flag.ImGuiDockNodeFlags;
import lombok.Getter;
import org.anotherteam.core.Core;
import org.anotherteam.game.Game;
import org.anotherteam.game.GameState;
import org.anotherteam.input.Input;
import org.anotherteam.level.EditorCameraController;
import org.anotherteam.level.PrefabViewer;
import org.anotherteam.level.TileViewer;
import org.anotherteam.render.ImGuiRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.EditorInput;
import org.anotherteam.util.FileUtils;
import org.anotherteam.util.Popups;
import org.anotherteam.widget.Console;

@Getter
public final class Editor implements Core {

    @Getter
    private static Editor instance;

    public static void switchPlayMode() {
        final boolean onEditor = instance.game.getStateManager().getState() == GameState.ON_EDITOR;

        Game.DEBUG_MODE = !onEditor;

        resetGameView();
        instance.game.getLevelManager().load(Game.START_LEVEL_NAME);

        if (onEditor) {
            instance.game.getStateManager().setState(GameState.ON_LEVEL);
        } else {
            instance.game.getStateManager().setState(GameState.ON_EDITOR);
        }
    }

    private static void resetGameView() {
        GameScreen.WIDTH = GameScreen.DEFAULT_WIDTH;
        GameScreen.HEIGHT = GameScreen.DEFAULT_HEIGHT;
        GameScreen.RENDER_WIDTH = GameScreen.getWindow().getWidth();
        GameScreen.RENDER_HEIGHT = GameScreen.getWindow().getHeight();

        instance.game.getRender().updateFrames(GameScreen.WIDTH, GameScreen.HEIGHT);
    }

    private final Window window;
    private final Game game;

    private final ImGuiRender imGui;

    private final TileViewer tileViewer;
    private final PrefabViewer prefabViewer;
    private final Console console;

    private final EditorCameraController editorCameraController;

    public Editor(Window window) {
        instance = this;
        this.window = window;

        game = new Game(window);
        imGui = new ImGuiRender(GameScreen.getWindow().getHandler(), "#version 430 core", this);
        tileViewer = new TileViewer(GameScreen.getWindow().getWidth() / 2 - 320, GameScreen.getWindow().getHeight() / 2 - 200, 640, 400);
        prefabViewer = new PrefabViewer(GameScreen.getWindow().getWidth() / 2 - 320, GameScreen.getWindow().getHeight() / 2 - 200, 640, 400);
        console = new Console(GameScreen.getWindow().getWidth() / 2 - 320, GameScreen.getWindow().getHeight() / 2 - 200, 640, 400);
        editorCameraController = new EditorCameraController();
    }

    @Override
    public void init() {
        game.init();

        Game.DEBUG_MODE = true;
        game.getStateManager().setState(GameState.ON_EDITOR);
    }

    @Override
    public void update(float dt) {
        game.update(dt);
    }

    @Override
    public void render(float dt) {
        game.render(dt);

        if (game.getStateManager().getState() != GameState.ON_EDITOR) {
            if (EditorInput.isKeyPressed(Input.KEY_ESCAPE)) {
                switchPlayMode();
            }
            return;
        }
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

        ImGui.dockSpaceOverViewport(ImGui.getMainViewport(), ImGuiDockNodeFlags.PassthruCentralNode);
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("Menu")) {

            if (ImGui.menuItem("Open tile viewer")) {
                tileViewer.reset();
            }

            if (ImGui.menuItem("Open console")) {
                console.reset();
            }

            if (ImGui.menuItem("Exit")) {
                game.getStateManager().setState(GameState.ON_CLOSE_GAME);
            }

            ImGui.endMenu();
        }

        ImGui.dummy(8.0f, 00.0f);

        if (ImGui.beginMenu("Level [" + game.getLevelManager().getCurrent().getName() + "]")) {

            if (ImGui.menuItem("Save (CTRL+S)")) {
                FileUtils.saveEditorLevel(game.getLevelManager().getCurrent());
            }

            if (ImGui.menuItem("Load")) {
                Popups.LOAD_WINDOW.setSelected(true);
            }

            if (ImGui.menuItem("Cancel changes")) {
                game.getLevelManager().load(game.getLevelManager().getCurrent().getName());
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

        Popups.update();

        tileViewer.update();
        prefabViewer.update();
        console.update();
    }

    @Override
    public boolean needClose() {
        return game.needClose();
    }
}
