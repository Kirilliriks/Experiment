package org.anotherteam.editor.object.objectedit;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.gui.text.input.InputLabel;
import org.anotherteam.editor.gui.window.ComponentSelectWindow;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.screen.GameScreen;

public final class GameObjectEdit extends GUIElement {

    private GameObject editObject;

    private final SwitchMenu componentSelector;
    private Component selectedComponent;

    private final InputLabel nameInputLabel;

    private final Button addComponentButton;
    private final Button removeComponentButton;

    public GameObjectEdit(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);

        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);

        componentSelector = new SwitchMenu(width / 2.0f, -Editor.DEFAULT_BORDER_SIZE,
                (int)(width / 2.0f - Editor.DEFAULT_BORDER_SIZE), height - Editor.DEFAULT_BORDER_SIZE * 4,
                SwitchMenu.Type.DOUBLE, this);
        componentSelector.setInvertedY(true);

        nameInputLabel = new InputLabel("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
        nameInputLabel.setAfterUnFocus(() -> editObject.setName(nameInputLabel.getInputText()));

        addComponentButton = new TextButton("Add new component", width - componentSelector.getWidth(), Editor.DEFAULT_BORDER_SIZE * 0.8f, this);
        addComponentButton.setOnClick(() -> {
            final ComponentSelectWindow componentSelectWindow = new ComponentSelectWindow(300, 200, editObject);
            Editor.callWindow(componentSelectWindow);
            fillComponentSelector();
        });

        removeComponentButton = new TextButton("Remove component", width - componentSelector.getWidth() + addComponentButton.getWidth() + Editor.DEFAULT_BORDER_SIZE, Editor.DEFAULT_BORDER_SIZE * 0.8f, this);
        removeComponentButton.setOnClick(() -> {
            if (selectedComponent == null) return;

            editObject.removeComponent(selectedComponent.getClass());
            removeComponentButton.setLock(true);
            fillComponentSelector();
        });

        inverted = true;

        setEditObject(null);
    }

    public void setEditObject(GameObject gameObject) {
        editObject = gameObject;
        selectedComponent = null;
        fillComponentSelector();

        if (editObject == null) {
            nameInputLabel.setInputText("SELECT OBJECT");
            nameInputLabel.setLock(true);
            addComponentButton.setLock(true);
            removeComponentButton.setLock(true);
            return;
        }
        nameInputLabel.setInputText(editObject.getName());
        nameInputLabel.setLock(false);
        addComponentButton.setLock(false);
    }

    private void fillComponentSelector() {
        componentSelector.clearChild();

        if (editObject == null) return;
        for (final Component component : editObject.getComponents().values()) {
            final SwitchButton btn = componentSelector.addButton(component.getClass().getSimpleName(),
                    ()-> {
                        removeComponentButton.setLock(false);
                        selectedComponent = component;
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
