package org.anotherteam.game;

import org.anotherteam.core.Core;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game implements Core {

    private static Game instance;

    public static final String GAME_NAME = "Experiment";
    public static final String START_LEVEL_NAME = "StartLevel";
    public static boolean DEBUG_MODE = false;

    public final LevelManager levelManager;
    public final StateManager stateManager;
    public final GameRender render;

    public Game(@NotNull Window window) {
        instance = this;

        window.setFullscreen(false);

        levelManager = new LevelManager(this);
        stateManager = new StateManager(this, GameState.ON_MENU);

        GameScreen.init(window);
        render = new GameRender(this);

        DebugBatch.GLOBAL = new DebugBatch(GameScreen.WINDOW_CAMERA);

        GameScreen.RENDER_WIDTH = window.getWidth();
        GameScreen.RENDER_HEIGHT = window.getHeight();
    }

    @Override
    public void init() {
        levelManager.load(START_LEVEL_NAME);
    }

    @Override
    public void update(float dt) {
        if (stateManager.getState() == GameState.ON_EDITOR) return;

        levelManager.update(dt);
    }

    @Override
    public void render(float dt) {
        levelManager.render(GameScreen.getWindowFrame());
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean needClose() {
        return stateManager.getState() == GameState.ON_CLOSE_GAME;
    }

    public static GameRender getRender() {
        return instance.render;
    }
}
