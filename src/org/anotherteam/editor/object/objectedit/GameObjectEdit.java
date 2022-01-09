package org.anotherteam.editor.object.objectedit;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.text.input.InputLabel;
import org.anotherteam.object.GameObject;
import org.anotherteam.screen.GameScreen;

public final class GameObjectEdit extends GUIElement {

    private GameObject editObject;

    private final InputLabel nameInputLabel;

    public GameObjectEdit(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);

        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);

        nameInputLabel = new InputLabel("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
        nameInputLabel.setAfterUnFocus(() -> editObject.setName(nameInputLabel.getInputText()));
        setEditObject(null);

        inverted = true;
    }

    private void setEditObject(GameObject gameObject) {
        editObject = gameObject;

        if (editObject == null) {
            nameInputLabel.setInputText("SELECT OBJECT");
            nameInputLabel.setLock(true);
            return;
        }
        nameInputLabel.setInputText(editObject.getName());
        nameInputLabel.setLock(false);
    }

    @Override
    public void update(float dt) {
        if (Input.isAnyButtonPressed()) {
            val currentRoom = Game.levelManager.getCurrentRoom();
            for (val gameObject : currentRoom.getGameObjects()) {
                if (!gameObject.getCollider().isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY())) continue;

                if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                    setEditObject(gameObject);
                }
                return;
            }
            setEditObject(null);
        }
    }
}
