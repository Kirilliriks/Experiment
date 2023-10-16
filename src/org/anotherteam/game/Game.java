package org.anotherteam.game;

import lombok.Getter;
import org.anotherteam.core.Core;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

@Getter
public final class Game implements Core {

    public static final String GAME_NAME = "Experiment";
    public static final String START_LEVEL_NAME = "StartLevel";
    public static boolean DEBUG_MODE = false;

    private final GameRender render;
    private final LevelManager levelManager;
    private final StateManager stateManager;

    public Game(@NotNull Window window) {
        window.setFullscreen(false);

        GameScreen.init(window);

        render = new GameRender(this);

        levelManager = new LevelManager(this);
        stateManager = new StateManager(this, GameState.ON_MENU);

        DebugBatch.GLOBAL = new DebugBatch(GameScreen.WINDOW_CAMERA);
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
}
