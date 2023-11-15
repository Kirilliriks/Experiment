package org.anotherteam.level;

import imgui.ImGui;
import org.anotherteam.Editor;
import org.anotherteam.dragged.DraggedGameObject;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.game.level.room.object.Prefabs;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.type.sprite.SpriteComponent;
import org.anotherteam.game.object.prefab.Prefab;
import org.anotherteam.input.Input;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.DraggedObject;
import org.anotherteam.screen.Screen;
import org.anotherteam.util.EditorInput;
import org.anotherteam.widget.Widget;

public final class PrefabViewer extends Widget {

    public PrefabViewer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        super.update();

        ImGui.begin("Prefab viewer");
        if (isClicked()) {
            Editor.getInstance().setMode(Editor.Mode.GAME_OBJECT);
        }

        for (final Prefab prefab : Prefabs.VALUES) {
            final SpriteComponent controller = prefab.gameObject().getComponent(SpriteComponent.class);
            if (controller == null) {
                continue;
            }

            ImGui.spacing();

            final Sprite sprite = controller.getTextureSprite();
            if (!ImGui.imageButton(sprite.getTexture().getId(), sprite.getWidth(), sprite.getHeight(), sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1())) {
                continue;
            }

            Screen.setDraggedObject(new DraggedGameObject(prefab.copy()));
        }

        ImGui.end();

        if (Editor.getInstance().getMode() != Editor.Mode.GAME_OBJECT) {
            return;
        }

        final DraggedObject draggedObject = Screen.getDraggedObject();
        if (EditorInput.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
            final Room room = Editor.getInstance().getGame().getLevelManager().getCurrentRoom();

            if (draggedObject == null) {
                for (final GameObject gameObject : room.getGameObjects()) {
                    if (gameObject.getCollider().isOnMouse(Screen.inGameMouseX(), Screen.inGameMouseY())) {
                        room.rewoveObject(gameObject);
                        Screen.setDraggedObject(new DraggedGameObject(gameObject));
                        return;
                    }
                }
                return;
            }

            if (!(draggedObject instanceof DraggedGameObject draggedGameObject)) {
                return;
            }

            room.addObject(draggedGameObject.getGameObject());

            Screen.setDraggedObject(null);
        }
    }
}
