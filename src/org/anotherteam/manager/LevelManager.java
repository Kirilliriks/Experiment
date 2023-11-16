package org.anotherteam.manager;

import org.anotherteam.game.Game;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.util.FileUtils;
import org.anotherteam.game.level.Level;
import org.anotherteam.game.level.room.Room;
import org.jetbrains.annotations.NotNull;

public final class LevelManager extends AbstractManager {

    private final Game game;
    private final GameRender gameRender;
    private Level level = null;

    public LevelManager(Game game) {
        this.game = game;

        gameRender = game.getRender();
    }

    /**
     * Save only on play, not on editor!
     */
    public void saveLevel() {
        // TODO save
    }

    @NotNull
    public Level set(@NotNull Level level) {
        this.level = level;
        return this.level;
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
    public Level getLevel() {
        return level;
    }

    @NotNull
    public Room getRoom() {
        return getLevel().getRoom();
    }

    public void render(@NotNull RenderFrame windowFrame) {
        level.render(gameRender, windowFrame);
    }

    @Override
    public void update(float delta) {
        level.update(delta);
    }

    @Override
    public void clear() {

    }
}
