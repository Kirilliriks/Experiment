package org.anotherteam;

import org.anotherteam.debug.DebugBatch;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game {
    public static final String START_LEVEL_NAME = "StartLevel";
    public static boolean DebugMode;

    public static LevelManager levelManager;
    public static StateManager stateManager;
    public static GameRender gameRender;

    public Game(@NotNull Window window) {
        window.setFullscreen(true);

        levelManager = new LevelManager();
        stateManager = new StateManager(GameState.ON_EDITOR);
        GameScreen.init(window);
        gameRender = new GameRender();

        DebugMode = false;
        DebugBatch.global = new DebugBatch(GameScreen.windowCamera);

        GameScreen.RENDER_WIDTH = window.getWidth();
        GameScreen.RENDER_HEIGHT = window.getHeight();
    }

    public void init() {
        levelManager.loadLevel(START_LEVEL_NAME);
    }

    public void update(float dt) {
        if (stateManager.getState() != GameState.ON_EDITOR)
            levelManager.update(dt);
    }

    public void render(float dt) {
        levelManager.renderLevel(GameScreen.windowBatch);

        if (stateManager.getState() == GameState.ON_EDITOR || DebugMode) {
            //DebugRender.global.draw();
        }
    }

    public void destroy() { }

    public static GameRender getGameRender() {
        return gameRender;
    }
}
