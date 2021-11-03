package org.anotherteam.editor.object.newobject;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.editor.screen.DraggedGameObject;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.object.prefab.ColliderPrefab;
import org.anotherteam.object.prefab.EntityPrefab;
import org.anotherteam.object.prefab.Prefab;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.exception.LifeException;
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
        typeMenu.setInverted(true);
        typeMenu.setColor(100, 100, 100, 255);
        typeMenu.setStartOffset(Label.DEFAULT_TEXT_OFFSET, 0);
        typeMenu.addButton("Entity");
        typeMenu.addButton("Item");
        typeMenu.addButton("Light");
        typeMenu.addButton("Collider");
        generatePrefabMenu(typeMenu.getButton(0), EntityPrefab.values());
        generatePrefabMenu(typeMenu.getButton(3), ColliderPrefab.values());
        typeMenu.setClicked(typeMenu.getButton(0));
    }

    public void generatePrefabMenu(@NotNull SwitchButton button, Prefab[] prefabs) {
        val spriteMenu = new SpriteMenu(0, -typeMenu.getHeight(), width, height - typeMenu.getHeight(), this);
        spriteMenu.setVisible(false);
        spriteMenu.setOffsetIcon(8);
        spriteMenu.setInverted(true);
        for (val value : prefabs) {
            val object = GameObject.create(0, 0, value.getPrefabClass()); // TODO delete new game object instancing
            val sprite = object.hasComponent(SpriteController.class) ?
                    object.getComponent(SpriteController.class).getSprite() :
                    AssetData.EDITOR_NULL_ICON_ATLAS.getSprite(0, 0);
            if (sprite == null) throw new LifeException("Prefab " + value.getPrefabClass().getSimpleName() + " don't have sprite");
            val spriteButton = spriteMenu.addButton(sprite);
            spriteButton.setOnClick(()-> {
                draggedGameObject = new DraggedGameObject(sprite, GameObject.create(0, 0, value.getPrefabClass()));;
                GameScreen.draggedThing = draggedGameObject;
            });
        }
        button.setOnClick(()-> spriteMenu.setVisible(true));
        button.setAfterClick(()-> spriteMenu.setVisible(false));
    }

    @Override
    public void update(float dt) {
        if (draggedGameObject != null) {
            if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                val gameObject = draggedGameObject.getGameObject();
                gameObject.setPosition(GameScreen.inGameMouseX(), GameScreen.inGameMouseY());
                Game.game.getCurrentRoom().addObject(gameObject);
                GameScreen.draggedThing = null;
                draggedGameObject = null;
            }
            if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                GameScreen.draggedThing = null;
                draggedGameObject = null;
            }
            return;
        }
        if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) { // TODO REMOVE OBJECT FROM ROOM
            GameScreen.draggedThing = null;
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        if (draggedGameObject != null) {
            val x  = (int) Input.getMouseX();
            val y  = (int) Input.getMouseY();
            draggedGameObject.getGameObject().setPosition(x, y);
            draggedGameObject.getGameObject().render(editorBatch);
        }
    }
}
