package org.anotherteam.editor.object.objectedit;

import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.gui.text.input.InputPart;
import org.anotherteam.editor.gui.window.ComponentSelectWindow;
import org.anotherteam.editor.level.room.RoomEditor;
import org.anotherteam.editor.object.GameObjectMenu;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.screen.GameScreen;

public final class GameObjectEditor extends GUIElement {

    private static GameObjectEditor INSTANCE;

    private final GameObjectMenu gameObjectMenu;

    private GameObject editObject;

    private final SwitchMenu componentSelector;
    private Component selectedComponent;

    private final InputPart nameInputLabel;
    private final InputPart posXInputLabel;
    private final InputPart posYInputLabel;

    private final Button addComponentButton;
    private final Button removeComponentButton;

    public GameObjectEditor(float x, float y, GameObjectMenu gameObjectMenu) {
        super(x, y, gameObjectMenu);
        INSTANCE = this;

        this.gameObjectMenu = gameObjectMenu;

        final var editor = Editor.inst();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);

        componentSelector = new SwitchMenu(width / 2.0f, -Editor.DEFAULT_BORDER_SIZE,
                (int)(width / 2.0f - Editor.DEFAULT_BORDER_SIZE), height - Editor.DEFAULT_BORDER_SIZE * 4,
                SwitchMenu.Type.DOUBLE, this);
        componentSelector.setInvertedY(true);

        nameInputLabel = new InputPart("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
        nameInputLabel.setAfterUnFocus(() -> editObject.setName(nameInputLabel.getValue()));

        posXInputLabel = new InputPart("PosX: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE * 2 - nameInputLabel.getHeight(), this);
        posXInputLabel.setType(InputPart.Type.INTEGER);
        posXInputLabel.setInvertedY(true);
        posXInputLabel.setAfterUnFocus(() -> editObject.getPosition().x = Integer.parseInt(posXInputLabel.getValue()));

        posYInputLabel = new InputPart("PosY: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE * 2 - nameInputLabel.getHeight() - posXInputLabel.getHeight(), this);
        posYInputLabel.setType(InputPart.Type.INTEGER);
        posYInputLabel.setInvertedY(true);
        posYInputLabel.setAfterUnFocus(() -> editObject.getPosition().y = Integer.parseInt(posYInputLabel.getValue()));

        addComponentButton = new TextButton("Add new component", width - componentSelector.getWidth(), Editor.DEFAULT_BORDER_SIZE * 0.8f, this);
        addComponentButton.setOnClick((info) -> {
            final ComponentSelectWindow componentSelectWindow = new ComponentSelectWindow(300, 200, editObject, this);
            Editor.callWindow(componentSelectWindow);
        });

        removeComponentButton = new TextButton("Remove component", width - componentSelector.getWidth() + addComponentButton.getWidth() + Editor.DEFAULT_BORDER_SIZE, Editor.DEFAULT_BORDER_SIZE * 0.8f, this);
        removeComponentButton.setOnClick((info) -> {
            if (selectedComponent == null) return;

            editObject.removeComponent(selectedComponent.getClass());

            removeComponentButton.setLock(true);
            fillComponentSelector();
        });

        inverted = true;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            updateEditObject();
        }
    }

    public void init() {
        setEditObject(null);
    }

    public void setEditObject(GameObject gameObject) {
        editObject = gameObject;
        setComponent(null);
        fillComponentSelector();

        if (editObject == null) {
            nameInputLabel.setValue("SELECT OBJECT");
            nameInputLabel.setLock(true);
            posXInputLabel.setValue("");
            posXInputLabel.setLock(true);
            posYInputLabel.setValue("");
            posYInputLabel.setLock(true);
            addComponentButton.setLock(true);
            removeComponentButton.setLock(true);
            return;
        }

        updateEditObject();
    }

    public void updateEditObject() {
        if (editObject == null) return;

        nameInputLabel.setValue(editObject.getName());
        nameInputLabel.setLock(false);
        posXInputLabel.setValue(String.valueOf(editObject.getPosition().x));
        posXInputLabel.setLock(false);
        posYInputLabel.setValue(String.valueOf(editObject.getPosition().y));
        posYInputLabel.setLock(false);
        addComponentButton.setLock(false);
    }

    public void fillComponentSelector() {
        componentSelector.clearChild();
        if (editObject == null) return;

        for (final Component component : editObject.getComponents()) {
            componentSelector.addButton(component.getClass().getSimpleName(),
                    (info)-> {
                        removeComponentButton.setLock(false);
                        setComponent(component);

                        if (info.isRight()) {
                            gameObjectMenu.chooseComponentEditor();
                        }
                    });
        }
    }

    private void setComponent(Component component) {
        selectedComponent = component;
        gameObjectMenu.getComponentEditor().setComponent(component);
    }

    @Override
    public void update(float dt) {
        if (!Input.isAnyButtonPressed()) return;

        if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
            if (editObject == null) return;
            if (!editObject.getCollider().isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY())) return;

            setEditObject(null);
            return;
        }

        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;

        final Room currentRoom = RoomEditor.editor().getRoom();
        for (final GameObject gameObject : currentRoom.getGameObjects()) {
            if (!gameObject.getCollider().isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY())) continue;

            setEditObject(gameObject);
            return;
        }
    }

    public void onRoomChange() {
        setEditObject(null);
    }

    public static GameObjectEditor editor() {
        return INSTANCE;
    }
}
