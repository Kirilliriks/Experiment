package org.anotherteam.editor.level.selector;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.data.FileLoader;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.level.Level;
import org.anotherteam.util.Color;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class LevelSelector extends GUIElement {

    private final SwitchMenu selector;

    private final SwitchMenu downButtons;
    private final Button createEmptyButton;
    private final Button saveLevelButton;

    public LevelSelector(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        selector = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE,
                width - Editor.DEFAULT_BORDER_SIZE * 2, height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        selector.setInverted(true);
        selector.setColor(Color.BLUE);
        fillButtons();

        downButtons = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, Editor.DEFAULT_BORDER_SIZE, 0, 0, SwitchMenu.Type.HORIZONTAL, this);
        createEmptyButton = new Button("Create empty level", 0, 0, downButtons);
        createEmptyButton.setOnClick(()-> selector.addButton("Empty.hgl", ()-> Game.getInstance().setLevel(Level.createEmpty())));
        downButtons.addButton(createEmptyButton);
        saveLevelButton = new Button("Save level", 40, 0, downButtons);
        saveLevelButton.setOnClick(()-> FileLoader.saveLevel(Game.getInstance().gameLevel));
        downButtons.addButton(saveLevelButton);
    }

    public void fillButtons() {
        val files = new File("levels/").listFiles();
        if (files == null) throw new LifeException("Level's not found");

        for (val file : files) {
            selector.addButton(file.getName(),
                    ()-> Game.getInstance().setLevel(FileLoader.loadLevel(file.getName())));
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
    }
}
