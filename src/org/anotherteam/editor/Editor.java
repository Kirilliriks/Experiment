package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.screen.GameScreen;

public final class Editor {

    private final Game game;

    private final ImGuiMain imGui;

    public Editor(Game game) {
        this.game = game;
        imGui = new ImGuiMain(GameScreen.window.getHandler(), "#version 430 core", this);
    }

    public void imgui() {
        GameViewWindow.imgui(GameScreen.windowFrame.texture.getId());
    }

    public void draw() {
        imGui.imgui();
    }
}
