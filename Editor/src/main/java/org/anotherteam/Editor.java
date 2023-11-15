package org.anotherteam;

import imgui.ImGui;
import imgui.flag.ImGuiDockNodeFlags;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.anotherteam.core.Core;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.dragged.DraggedHighliter;
import org.anotherteam.game.Game;
import org.anotherteam.game.GameState;
import org.anotherteam.game.data.AssetData;
import org.anotherteam.input.Input;
import org.anotherteam.level.camera.EditorCameraController;
import org.anotherteam.level.PrefabViewer;
import org.anotherteam.level.TileViewer;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.ImGuiRender;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.Screen;
import org.anotherteam.util.EditorInput;
import org.anotherteam.util.FileUtils;
import org.anotherteam.widget.popup.Popups;
import org.anotherteam.widget.Console;

@Getter
public final class Editor implements Core {

    public static final String GLSL_VERSION = "#version 430 core";
    public static final Texture EDITOR_HIGHLITER_TEXTURE = AssetData.getTexture(AssetData.ASSETS_PATH + "editor_highliter_texture.png");

    @Getter
    private static Editor instance;

    public static void switchPlayMode() {
        reloadLevel();
        resetGameView();

        instance.play = !instance.play;

        if (instance.play) {
            instance.getGame().start();
        }

        Game.DEBUG = !instance.play;
    }

    private static void resetGameView() {
        Screen.width = Screen.DEFAULT_WIDTH;
        Screen.height = Screen.DEFAULT_HEIGHT;

        instance.getGame().getRender().updateFrames(Screen.width, Screen.height);
    }

    public static void saveLevel() {
        FileUtils.saveLevel(instance.getGame().getLevelManager().getCurrent());
    }

    public static void reloadLevel() {
        final LevelManager levelManager = instance.getGame().getLevelManager();
        levelManager.load(levelManager.getCurrent().getName());
    }

    private final Window window;
    private final Game game;
    private final ImGuiRender imGui;
    private final TileViewer tileViewer;
    private final PrefabViewer prefabViewer;
    private final Console console;
    private final EditorCameraController editorCameraController;

    private Mode mode = Mode.VIEW;
    private boolean play = false;

    public Editor(Window window) {
        this.window = window;
        instance = this;

        game = new Game(window);
        imGui = new ImGuiRender(Screen.getWindow().getHandler(), GLSL_VERSION, this);
        tileViewer = new TileViewer(Screen.getWindow().getWidth() / 2 - 320, Screen.getWindow().getHeight() / 2 - 200, 640, 400);
        prefabViewer = new PrefabViewer(Screen.getWindow().getWidth() / 2 - 320, Screen.getWindow().getHeight() / 2 - 200, 640, 400);
        console = new Console(Screen.getWindow().getWidth() / 2 - 320, Screen.getWindow().getHeight() / 2 - 200, 640, 400);
        editorCameraController = new EditorCameraController();

        DebugBatch.GLOBAL = new DebugBatch(Screen.WINDOW_CAMERA);
    }

    @Override
    public void prepare() {
        game.prepare();

        Game.DEBUG = true;
    }

    @Override
    public void update(float dt) {
        if (play) {
            game.update(dt);
        }

        if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
            if (Screen.getDraggedObject() instanceof DraggedHighliter) {
                return;
            }

            Screen.setDraggedObject(null);
            Input.MOUSE_RIGHT_BUTTON.block();
        }
    }

    @Override
    public void render(float dt) {
        game.render(dt);

        if (play) {
            if (EditorInput.isKeyPressed(Input.KEY_ESCAPE)) {
                switchPlayMode();
            }
            return;
        }

        final RenderFrame windowFrame = Screen.getMainFrame();
        windowFrame.renderBatch.begin(false);
        windowFrame.renderBatch.drawText("[TEST]", 320, 200);
        windowFrame.renderBatch.end();

        editorCameraController.handle(dt);

        imGui.render(dt);
    }

    @Override
    public void destroy() {
        game.destroy();
        imGui.destroy();
    }

    public void imgui(float dt) {
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
                game.setState(GameState.ON_CLOSE_GAME);
            }

            ImGui.endMenu();
        }

        ImGui.dummy(8.0f, 00.0f);

        if (ImGui.beginMenu("Level [" + game.getLevelManager().getCurrent().getName() + "]")) {

            if (ImGui.menuItem("New")) {
                saveLevel();
                game.getLevelManager().setEmpty();
            }

            if (ImGui.menuItem("Save (CTRL+S)")) {
                saveLevel();
            }

            if (ImGui.menuItem("Load")) {
                saveLevel();
                Popups.LOAD_LEVEL.setSelected(true);
            }

            if (ImGui.menuItem("Rename")) {
                Popups.RENAME_LEVEL.setSelected(true);
            }

            if (ImGui.menuItem("Delete")) {
                final String levelName = instance.getGame().getLevelManager().getCurrent().getName();
                game.getLevelManager().setEmpty();
                FileUtils.deleteLevel(levelName);
            }

            ImGui.endMenu();
        }

        ImGui.dummy(8.0f, 00.0f);
        if (ImGui.beginMenu("[MODE " + mode.getDescription() + "]")) {

            for (final Mode md : Mode.values()) {
                if (ImGui.menuItem(md.description)) {
                    mode = md;
                }
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

        if (EditorInput.isKeyDown(Input.KEY_LEFT_CTRL) && EditorInput.isKeyPressed(Input.KEY_S)) {
            saveLevel();
        }

        Popups.update();

        tileViewer.update();
        prefabViewer.update();
        console.update();
    }

    @Override
    public boolean needClose() {
        return game.needClose();
    }

    public void setMode(Mode mode) {
        if (this.mode == mode) {
            return;
        }

        this.mode = mode;

        Screen.setDraggedObject(null);
    }

    @RequiredArgsConstructor
    @Getter
    public enum Mode {
        VIEW("VIEW"),
        TILE("TILE"),
        GAME_OBJECT("GAME OBJECT");

        private final String description;
    }
}
