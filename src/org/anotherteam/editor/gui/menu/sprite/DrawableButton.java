package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.data.AssetData;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.render.EditorBatch;
import org.jetbrains.annotations.NotNull;

public abstract class DrawableButton extends Button {

    public static final int PREVIEW_SCALE = 5;

    protected final SpriteMenu menu;

    public DrawableButton(float x, float y, SpriteMenu ownerElement) {
        super(x, y, ownerElement);
        this.menu = ownerElement;
    }

    public void drawHighlight(@NotNull EditorBatch editorBatch) {
        editorBatch.draw(AssetData.EDITOR_HIGHLITER_TEXTURE, getPosX(), getPosY(), SpriteMenu.ICON_SIZE, SpriteMenu.ICON_SIZE);
    }

    public abstract void drawPreview(@NotNull EditorBatch editorBatch);
}
