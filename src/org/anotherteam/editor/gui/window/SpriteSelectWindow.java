package org.anotherteam.editor.gui.window;

import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpriteSelectWindow extends DialogWindow {

    private final List<Texture> textures;

    public SpriteSelectWindow(int width, int height) {
        super(width, height);

        textures = new ArrayList<>();

        final var files = new File(AssetData.ENTITY_PATH).listFiles();
        if (files == null) throw new LifeException("Entities atlases not found");

        for (final var file : files) {
            final var texture = AssetData.getTexture(AssetData.ENTITY_PATH + file.getName());

            textures.add(texture);
            setWidth(texture.getWidth());
            setHeight(texture.getHeight());
            break;
        }
    }


    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);

        editorBatch.draw(textures.get(0), getPosX(), getPosY());
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (Input.isKeyPressed(Input.KEY_ESCAPE)) {
            Editor.closeWindow();
        }
    }
}
