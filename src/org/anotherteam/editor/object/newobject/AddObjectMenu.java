package org.anotherteam.editor.object.newobject;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.editor.screen.DraggedGameObject;
import org.anotherteam.level.room.object.prefab.RoomPrefab;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.level.room.object.prefab.ColliderPrefab;
import org.anotherteam.level.room.object.prefab.EntityPrefab;
import org.anotherteam.object.prefab.Prefab;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class AddObjectMenu extends GUIElement {

    private final SwitchMenu typeMenu;

    private DraggedGameObject draggedGameObject;

    public AddObjectMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        typeMenu = new SwitchMenu(0, 0, width, TextMenu.Type.HORIZONTAL, this);
        typeMenu.setInvertedY(true);
        typeMenu.setColor(100, 100, 100, 255);
        typeMenu.setStartOffset(Label.DEFAULT_TEXT_OFFSET, 0);
        typeMenu.addButton("Entity");
        typeMenu.addButton("Item");
        typeMenu.addButton("Room object");
        typeMenu.addButton("Light");
        typeMenu.addButton("Collider");
        generatePrefabMenu(typeMenu.getButton(0), EntityPrefab.values());
        generatePrefabMenu(typeMenu.getButton(2), RoomPrefab.values());
        generatePrefabMenu(typeMenu.getButton(4), ColliderPrefab.values());
        typeMenu.setClicked(typeMenu.getButton(0));
    }

    public void generatePrefabMenu(@NotNull SwitchButton button, Prefab[] prefabs) {
        val spriteMenu = new SpriteMenu(0, -typeMenu.getHeight(), width, height - typeMenu.getHeight(), this);
        spriteMenu.setVisible(false);
        spriteMenu.setOffsetIcon(8);
        spriteMenu.setInvertedY(true);
        for (val value : prefabs) {
            val object = GameObject.create(0, 0, value.getPrefabClass()); // TODO delete new game object instancing
            val sprite = getObjectSprite(object);
            val spriteButton = spriteMenu.addButton(sprite);
            spriteButton.setOnClick(()-> {
                draggedGameObject = new DraggedGameObject(sprite, GameObject.create(0, 0, value.getPrefabClass()));
                GameScreen.draggedThing = draggedGameObject;
            });
        }
        button.setOnClick(()-> spriteMenu.setVisible(true));
        button.setAfterClick(()-> spriteMenu.setVisible(false));
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (!visible)
            draggedGameObject = null;
    }

    @Override
    public void update(float dt) {
        if (draggedGameObject != null) {
            if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                val x = GameScreen.inGameMouseX();
                val y = GameScreen.inGameMouseY();
                if (x < 0 || y < 0) return;

                val gameObject = draggedGameObject.getGameObject();
                gameObject.setPosition(x, y);
                Game.levelManager.getCurrentRoom().addObject(gameObject);
                GameScreen.draggedThing = null;
                draggedGameObject = null;
            } else if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                GameScreen.draggedThing = null;
                draggedGameObject = null;
            }
            return;
        }

        if (Input.isAnyButtonPressed()) {
            val currentRoom = Game.levelManager.getCurrentRoom();
            for (val gameObject : currentRoom.getGameObjects()) {
                if (!gameObject.getCollider().isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY())) continue;

                if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                    currentRoom.rewoveObject(gameObject);
                } else if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                    draggedGameObject = new DraggedGameObject(getObjectSprite(gameObject), gameObject);
                    GameScreen.draggedThing = draggedGameObject;
                    currentRoom.rewoveObject(gameObject);
                }
                break;
            }
        }
    }

    @NotNull
    public Sprite getObjectSprite(@NotNull GameObject gameObject) {
        return gameObject.hasComponent(SpriteController.class) ?
                gameObject.getComponent(SpriteController.class).getTextureSprite() :
                AssetData.EDITOR_NULL_ICON_ATLAS.getTextureSprite(0, 0);
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);

        if (GameScreen.draggedThing == null || draggedGameObject == null) return;
        val x  = (int) Input.getMouseX();
        val y  = (int) Input.getMouseY();

        if (GameScreen.inGameWindowMouseX() != -1 && GameScreen.inGameWindowMouseY() != -1) return;
        draggedGameObject.getGameObject().setPosition(x, y);
        draggedGameObject.getGameObject().render(editorBatch);
    }
}
