package org.anotherteam.editor.object.newobject;

import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.object.objectedit.GameObjectEditor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.editor.screen.DraggedGameObject;
import org.anotherteam.level.room.object.prefab.RoomPrefab;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.level.room.object.prefab.ColliderPrefab;
import org.anotherteam.level.room.object.prefab.EntityPrefab;
import org.anotherteam.object.prefab.Prefab;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class PrefabObjectMenu extends GUIElement {

    private final SwitchMenu typeMenu;

    private SpriteMenu selectedMenu;

    private DraggedGameObject draggedGameObject;

    private GameObject selected;

    public PrefabObjectMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        width = (int)(Editor.inst().getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        // TODO maybe add createPrefabList button?
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

        final var addPrefabButton = new TextButton("Add new prefab", 0, 0, this);
        addPrefabButton.setOnClick((click) -> addPrefab());

        final var removePrefabButton = new TextButton("Remove prefab", addPrefabButton.getWidth() + Editor.DEFAULT_BORDER_SIZE, 0, this);
        removePrefabButton.setOnClick((click) -> removePrefab());
    }

    private void addPrefab() {
        final var object = new GameObject(0, 0);

        final Sprite sprite = getObjectsSprite(object);
        final var spriteButton = selectedMenu.addButton(sprite);

        spriteButton.setOnClick((left)-> {
            if (!spriteButton.isClicked()) {
                selected = new GameObject(0, 0);
                GameObjectEditor.editor().setEditObject(selected);
                return;
            }

            draggedGameObject = new DraggedGameObject(sprite, new GameObject(0, 0));
            GameScreen.draggedThing = draggedGameObject;
        });
    }

    private void removePrefab() {
        selectedMenu.removeLastButton();
    }

    public void generatePrefabMenu(@NotNull SwitchButton button, Prefab[] prefabs) {
        final var spriteMenu = new SpriteMenu(0, -typeMenu.getHeight(), width, height - typeMenu.getHeight() - Editor.DEFAULT_OFFSET_SIZE, this);
        spriteMenu.setVisible(false);
        spriteMenu.setOffsetIcon(8);
        spriteMenu.setInvertedY(true);

        for (final var value : prefabs) {
            final var object = GameObject.create(value.getPrefabClass());

            final var sprite = getObjectsSprite(object);
            final var spriteButton = spriteMenu.addButton(sprite);

            spriteButton.setOnClick((left)-> {
                if (!spriteButton.isClicked()) {
                    selected = GameObject.create(value.getPrefabClass());
                    GameObjectEditor.editor().setEditObject(selected);
                    return;
                }

                draggedGameObject = new DraggedGameObject(sprite, GameObject.create(value.getPrefabClass()));
                GameScreen.draggedThing = draggedGameObject;
            });
        }

        button.setOnClick((left)-> {
            spriteMenu.setVisible(true);
            selectedMenu = spriteMenu;
        });
        button.setAfterClick((left)-> spriteMenu.setVisible(false));

        if (selectedMenu == null) selectedMenu = spriteMenu;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (!visible) {
            draggedGameObject = null;
        }
    }

    @Override
    public void update(float dt) {
        if (draggedGameObject != null) {
            if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                final var x = GameScreen.inGameMouseX();
                final var y = GameScreen.inGameMouseY();
                if (x < 0 || y < 0) {
                    GameScreen.draggedThing = null;
                    draggedGameObject = null;
                    addPrefab(); // TODO add prefab if not contains from DraggedGameObject maybe?
                    return;
                }

                final var gameObject = draggedGameObject.getGameObject();
                gameObject.setPosition(x, y);

                Game.LEVEL_MANAGER.getCurrentRoom().addObject(gameObject);

                GameScreen.draggedThing = null;
                draggedGameObject = null;
            } else if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                GameScreen.draggedThing = null;
                draggedGameObject = null;
            }

            return;
        }

        if (Input.isAnyButtonPressed()) {
            final var currentRoom = Game.LEVEL_MANAGER.getCurrentRoom();
            for (final var gameObject : currentRoom.getGameObjects()) {
                if (!gameObject.getCollider().isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY())) continue;

                if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                    currentRoom.rewoveObject(gameObject);
                } else if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                    draggedGameObject = new DraggedGameObject(getObjectsSprite(gameObject), gameObject);
                    GameScreen.draggedThing = draggedGameObject;
                    currentRoom.rewoveObject(gameObject);
                }

                break;
            }
        }
    }

    @NotNull
    public Sprite getObjectsSprite(@NotNull GameObject gameObject) {
        return gameObject.hasComponent(SpriteController.class) ?
                gameObject.getComponent(SpriteController.class).getTextureSprite() :
                AssetData.EDITOR_NULL_ICON_ATLAS.getTextureSprite(0, 0);
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);

        if (GameScreen.draggedThing == null || draggedGameObject == null) return;
        final var x  = (int) Input.getMouseX();
        final var y  = (int) Input.getMouseY();

        if (GameScreen.inGameWindowMouseX() != -1 && GameScreen.inGameWindowMouseY() != -1) return;
        draggedGameObject.getGameObject().setPosition(x, y);
        draggedGameObject.getGameObject().draw(editorBatch, false);
    }
}
