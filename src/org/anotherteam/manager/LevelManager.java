package org.anotherteam.manager;

import org.anotherteam.game.Game;
import org.anotherteam.game.GameState;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.util.FileUtils;
import org.anotherteam.game.level.Level;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

public final class LevelManager extends AbstractManager {

    private final Game game;
    private final GameRender gameRender;
    private Level currentLevel = null;

    public LevelManager(Game game) {
        this.game = game;

        gameRender = game.getRender();
    }

    /**
     * Save only on play, not on editor!
     */
    public void saveCurrent() {
        // TODO save
    }

    @NotNull
    public Level set(@NotNull Level level) {
        currentLevel = level;
        return currentLevel;
    }

    @NotNull
    public Level setEmpty() {
        return set(Level.empty());
    }

    @NotNull
    public Level load(@NotNull String levelName) {
        final Level loadedLevel = FileUtils.loadLevel(levelName);
        if (loadedLevel == null) return setEmpty();

        return set(loadedLevel);
    }

    @NotNull
    public Level getCurrent() {
        return currentLevel;
    }

    @NotNull
    public Room getCurrentRoom() {
        return getCurrent().getCurrentRoom();
    }

    public void render(@NotNull RenderFrame windowFrame) {
        currentLevel.render(gameRender, windowFrame);
    }

    @Override
    public void update(float delta) {
        currentLevel.update(delta);
    }

    @Override
    public void clear() {

    }
}
