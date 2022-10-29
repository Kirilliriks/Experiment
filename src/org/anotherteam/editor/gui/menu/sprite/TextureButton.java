package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.Input;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class TextureButton extends DrawableButton {

    private final Texture texture;

    public TextureButton(@NotNull Texture texture, float x, float y, SpriteMenu ownerElement) {
        super(x, y, ownerElement);
        this.texture = texture;
        width = texture.getWidth();
        height = texture.getHeight();
        setColor(Color.BLACK);
    }

    @Override
    public void setClicked(boolean clicked) {
        if (clicked && onClick != null) runClick(new ClickInfo());
        this.clicked = clicked;
    }

    @Override
    public void setClicked(boolean clicked, boolean left) {
        if (clicked && onClick != null) runClick(new ClickInfo(left));
        this.clicked = clicked;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (!isMouseOnWidget()) return;

        final boolean left = Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON);
        final boolean right = Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON);
        if (!left && !right) return;

        menu.setClicked(this, left);
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        editorBatch.draw(texture, getPosX(), getPosY(), texture.getWidth(), texture.getHeight());
    }

    @Override
    public void drawPreview(@NotNull EditorBatch editorBatch) {
        editorBatch.draw(texture, getPosX() + texture.getWidth(), getPosY() - texture.getHeight() * PREVIEW_SCALE,
                texture.getWidth() * PREVIEW_SCALE, texture.getHeight() * PREVIEW_SCALE);
    }
}
