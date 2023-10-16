package org.anotherteam.level;

import imgui.ImGui;
import org.anotherteam.Editor;
import org.anotherteam.dragged.DraggedGameObject;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.game.level.room.object.Prefabs;
import org.anotherteam.game.object.component.type.sprite.SpriteComponent;
import org.anotherteam.game.object.prefab.Prefab;
import org.anotherteam.input.Input;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.GameScreen;
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

            GameScreen.setDraggedThing(new DraggedGameObject(prefab.copy()));
        }

        ImGui.end();

        if (EditorInput.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
            if (!(GameScreen.getDraggedThing() instanceof DraggedGameObject draggedGameObject)) {
                return;
            }

            final Room room = Editor.getInstance().getGame().levelManager.getCurrentRoom();
            room.addObject(draggedGameObject.getGameObject());

            GameScreen.setDraggedThing(null);
        }
    }
}
