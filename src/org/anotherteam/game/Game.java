package org.anotherteam.game;

import org.anotherteam.core.Core;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game implements Core {

    public static final String GAME_NAME = "Experiment";
    public static final String START_LEVEL_NAME = "StartLevel";
    public static boolean DEBUG_MODE = false;

    public static LevelManager LEVEL_MANAGER;
    public static StateManager STATE_MANAGER;
    public static GameRender GAME_RENDER;

    public Game(@NotNull Window window) {
        window.setFullscreen(false);

        LEVEL_MANAGER = new LevelManager();
        STATE_MANAGER = new StateManager(this, GameState.ON_MENU);

        GameScreen.init(window);
        GAME_RENDER = new GameRender();

        DebugBatch.GLOBAL = new DebugBatch(GameScreen.WINDOW_CAMERA);

        GameScreen.RENDER_WIDTH = window.getWidth();
        GameScreen.RENDER_HEIGHT = window.getHeight();
    }

    @Override
    public void init() {
        LEVEL_MANAGER.load(START_LEVEL_NAME);
    }

    @Override
    public void update(float dt) {
        if (Game.STATE_MANAGER.getState() == GameState.ON_EDITOR) return;

        LEVEL_MANAGER.update(dt);
    }

    @Override
    public void render(float dt) {
        LEVEL_MANAGER.render(GameScreen.getWindowFrame());
    }

    @Override
    public void destroy() {
    }

    public static GameRender getGameRender() {
        return GAME_RENDER;
    }
}
