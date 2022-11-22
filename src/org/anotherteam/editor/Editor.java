package org.anotherteam.editor;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.editor.level.TileViewer;
import org.anotherteam.editorold.level.editor.LevelEditor;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.screen.GameScreen;

public final class Editor {

    private static Editor instance;

    private final Game game;

    private final ImGuiLayer imGui;

    private final TileViewer tileViewer;

    private final EditorCameraController editorCameraController;

    public Editor(Game game) {
        instance = this;

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

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(GameScreen.window.getWidth(), GameScreen.window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        final int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        if (ImGui.isWindowFocused()) {
            final ImGuiIO io = ImGui.getIO();
            io.setWantCaptureKeyboard(false);
            io.setWantCaptureMouse(false);
            io.setWantTextInput(false);
            io.setWantSetMousePos(false);
        }

        ImGui.dockSpace(ImGui.getID("Dockspace"));

        tileViewer.imgui();
        GameViewWindow.imgui(GameScreen.windowFrame);
        Console.imgui();

        editorCameraController.handle(dt);

        ImGui.end();
    }

    /**
     * Render imgui frame
     */
    public void render(float dt) {
        if (Game.STATE_MANAGER.getState() != GameState.ON_EDITOR) return;

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
        GameScreen.RENDER_WIDTH = GameScreen.window.getWidth();
        GameScreen.RENDER_HEIGHT = GameScreen.window.getHeight();
        GameScreen.POSITION.set(0, 0);

        Game.getGameRender().updateFrames(GameScreen.WIDTH, GameScreen.HEIGHT);
    }

    public static Editor getInstance() {
        return instance;
    }
}
