package org.anotherteam.game;

import lombok.Getter;
import org.anotherteam.core.Core;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.Screen;
import org.jetbrains.annotations.NotNull;

@Getter
public final class Game implements Core {

    public static final String GAME_NAME = "Experiment";
    public static final String START_LEVEL_NAME = "StartLevel";
    public static boolean DEBUG = false;

    private final GameRender render;
    private final LevelManager levelManager;
    private GameState state = GameState.ON_MENU;

    public Game(@NotNull Window window) {
        window.setFullscreen(false);

        Screen.init(window);

        render = new GameRender(this);

        levelManager = new LevelManager(this);

        DebugBatch.GLOBAL = new DebugBatch(Screen.WINDOW_CAMERA);
    }

    @Override
    public void prepare() {
        levelManager.load(START_LEVEL_NAME);
    }

    @Override
    public void update(float dt) {
        levelManager.update(dt);
    }

    @Override
    public void render(float dt) {
        levelManager.render(Screen.getWindowFrame());
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean needClose() {
        return state == GameState.ON_CLOSE_GAME;
    }

    public void start() {
        levelManager.getCurrent().start();
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
