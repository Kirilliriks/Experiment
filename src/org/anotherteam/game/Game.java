package org.anotherteam.game;

import lombok.Getter;
import org.anotherteam.core.Core;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.Screen;
import org.jetbrains.annotations.NotNull;

@Getter
public final class Game implements Core {

    @Getter
    private static Game instance;

    public static final String GAME_NAME = "Experiment";
    public static final String START_LEVEL_NAME = "StartLevel";
    public static boolean DEBUG = false;

    private final GameRender render;
    private final LevelManager levelManager;
    private GameState state = GameState.ON_MENU;

    public Game(@NotNull Window window) {
        instance = this;

        window.setFullscreen(false);

        render = new GameRender(this);
        levelManager = new LevelManager(this);
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
        levelManager.render(Screen.getMainFrame());
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean needClose() {
        return state == GameState.ON_CLOSE_GAME;
    }

    public void start() {
        state = GameState.ON_LEVEL;

        levelManager.getLevel().start();
    }

    public void demo() {
        state = GameState.ON_DEMO;
    }

    public void close() {
        state = GameState.ON_CLOSE_GAME;
    }
}
