package org.anotherteam.manager;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.data.FileLoader;
import org.anotherteam.level.Level;
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
    public void savePlayableLevel() {
        if (Game.game.getGameState() == GameState.ON_EDITOR) throw new LifeException("Trying save playable level when state is ON_EDITOR!");
        saveLevel();
    }

    public void saveLevel() {
        FileLoader.saveLevel(currentLevel);
    }

    public void setLevel(@NotNull Level level) {
        currentLevel = level;
    }

    public void loadLevel(@NotNull String levelName) {
        currentLevel = FileLoader.loadLevel(levelName);
    }

    /**
     * Reset level to first state, only for editor.
     */
    public void resetLevel() {
        currentLevel = FileLoader.loadLevel(currentLevel.getName());
    }

    public void setEmptyLevel() {
        currentLevel = Level.createEmpty();
    }

    @NotNull
    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void updateLevel(float dt) {
        currentLevel.update(dt);
    }

    public void renderLevel(@NotNull RenderBatch renderBatch) {
        currentLevel.render(renderBatch);
    }
}
