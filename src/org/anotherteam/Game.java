package org.anotherteam;

import org.anotherteam.level.Level;
import org.anotherteam.level.TestLevel;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game {
    public static boolean DebugMode;

    private GameState gameState;
    private GameScreen gameScreen;

    public Level currentLevel;

    public Game(@NotNull Window window) {
        this.gameScreen = new GameScreen(window.getWidth(), window.getHeight());

        this.gameState = GameState.ON_LEVEL;
        this.currentLevel = new TestLevel(this);
        DebugMode = false;
        init();
    }

    public void init() { }

    public void update(float dt) {
        currentLevel.update(dt);
    }

    public void render() {
        currentLevel.render();
    }

    @NotNull
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void destroy() { }
}
