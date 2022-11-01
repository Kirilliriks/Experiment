package org.anotherteam.editor;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
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

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(GameScreen.window.getWidth(), GameScreen.window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        final int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Dockspace
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
        imGui.imgui(dt);
    }

    public void destroy() {
        imGui.destroy();
    }
}
