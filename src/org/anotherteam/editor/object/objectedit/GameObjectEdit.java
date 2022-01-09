package org.anotherteam.editor.object.objectedit;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.text.input.InputLabel;
import org.anotherteam.editor.gui.window.SaveLevelDialog;
import org.anotherteam.level.Level;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.FileUtils;

public final class GameObjectEdit extends GUIElement {

    private GameObject editObject;

    private final SwitchMenu componentSelector;

    private final InputLabel nameInputLabel;

    public GameObjectEdit(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);

        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);

        componentSelector = new SwitchMenu(width / 2.0f, -Editor.DEFAULT_BORDER_SIZE,
                (int)(width / 2.0f - Editor.DEFAULT_BORDER_SIZE), height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        componentSelector.setInvertedY(true);

        nameInputLabel = new InputLabel("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
        nameInputLabel.setAfterUnFocus(() -> editObject.setName(nameInputLabel.getInputText()));
        setEditObject(null);

        inverted = true;
    }

    public void setEditObject(GameObject gameObject) {
        editObject = gameObject;

        if (editObject == null) {
            nameInputLabel.setInputText("SELECT OBJECT");
            nameInputLabel.setLock(true);
            return;
        }
        nameInputLabel.setInputText(editObject.getName());
        nameInputLabel.setLock(false);
        fillComponentSelector();
    }

    private void fillComponentSelector() {
        componentSelector.clearChild();

        for (final Component component : editObject.getComponents().values()) {
            final SwitchButton btn = componentSelector.addButton(component.getClass().getSimpleName(),
                    ()-> {

                    });
        }
    }

    @Override
    public void update(float dt) {
        if (!Input.isAnyButtonPressed()) return;

        if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
            setEditObject(null);
            return;
        }

        val currentRoom = Game.levelManager.getCurrentRoom();
        for (val gameObject : currentRoom.getGameObjects()) {
            if (!gameObject.getCollider().isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY())) continue;

            if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                setEditObject(gameObject);
            }
            return;
        }
    }
}
