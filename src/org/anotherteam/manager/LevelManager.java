package org.anotherteam.manager;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.util.FileUtils;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

public final class LevelManager extends AbstractManager {

    @NotNull
    private Level currentLevel;

    public LevelManager() {
        currentLevel = Level.createEmpty();
    }

    /**
     * Save only on play, not on editor!
     */
    public void saveCurrentLevel() {
        if (Game.STATE_MANAGER.getState() == GameState.ON_EDITOR) throw new LifeException("Trying save playable level when state is ON_EDITOR!");

        FileUtils.saveEditableLevel(currentLevel); // TODO save to game profile directory
    }

    @NotNull
    public Level setLevel(@NotNull Level level) {
        currentLevel = level;
        return currentLevel;
    }

    @NotNull
    public Level setEmptyLevel() {
        return setLevel(Level.createEmpty());
    }

    @NotNull
    public Level loadLevel(@NotNull String levelName) {
        final Level loadedLevel = FileUtils.loadLevel(levelName);
        if (loadedLevel == null) return setEmptyLevel();

        return setLevel(loadedLevel);
    }

    @NotNull
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @NotNull
    public Room getCurrentRoom() {
        return getCurrentLevel().getCurrentRoom();
    }

    public void renderLevel(@NotNull RenderBatch renderBatch) {
        currentLevel.render(renderBatch);
    }

    @Override
    public void update(float delta) {
        currentLevel.update(delta);
    }

    @Override
    public void clear() {

    }
}
