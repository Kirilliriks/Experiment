package org.anotherteam.editor.level.selector;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.data.FileLoader;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.SwitchMenu;
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
        width = editor.getWidth() - Editor.getInstance().getRightBorder();
        height = editor.getHeight() - editor.getDownBorder();
        inverted = true;

        selector = new SwitchMenu(Editor.DBORDER_SIZE, -Editor.DBORDER_SIZE, width - Editor.DBORDER_SIZE, height + Editor.DBORDER_SIZE, SwitchMenu.Type.DOUBLE, this);
        selector.setInverted(true);
        selector.setColor(Color.BLUE);
        fillButtons();

        downButtons = new SwitchMenu(Editor.DBORDER_SIZE, height + Editor.DBORDER_SIZE, 0, 0, SwitchMenu.Type.HORIZONTAL, this);
        createEmptyButton = new Button("Create empty level", Editor.DBORDER_SIZE, height + Editor.DBORDER_SIZE, this);
        createEmptyButton.setOnClick(()-> selector.addButton("Empty.hgl", ()-> Game.getInstance().setLevel(Level.createEmpty())));
        downButtons.addButton(createEmptyButton);
        saveLevelButton = new Button("Save level", Editor.DBORDER_SIZE + 40, height + Editor.DBORDER_SIZE, this);
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
}
