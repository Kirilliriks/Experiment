package org.anotherteam.level;

import imgui.ImGui;
import org.anotherteam.game.level.room.object.prefab.RoomPrefab;
import org.anotherteam.game.object.component.type.sprite.SpriteController;
import org.anotherteam.game.object.prefab.Prefab;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.widget.Widget;

public final class PrefabViewer extends Widget {

    public PrefabViewer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        super.update();

        ImGui.begin("Prefab viewer");
        for (final Prefab prefab : RoomPrefab.values()) {
            final SpriteController controller = prefab.getPrefab().getComponent(SpriteController.class);
            if (controller == null) {
                continue;
            }

            final Sprite sprite = controller.getTextureSprite();
            if (!ImGui.imageButton(sprite.getTexture().getId(), sprite.getWidth(), sprite.getHeight(), sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1())) {
                continue;
            }

        }

        ImGui.end();
    }
}
