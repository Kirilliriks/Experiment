package org.anotherteam.editorold.gui.menu.sprite;

import org.anotherteam.Input;
import org.anotherteam.editorold.render.EditorBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class SpriteButton extends DrawableButton {

    private final Sprite sprite;

    public SpriteButton(@NotNull Sprite sprite, float x, float y, SpriteMenu ownerElement) {
        super(x, y, ownerElement);
        this.sprite = sprite;
        width = SpriteMenu.ICON_SIZE;
        height = SpriteMenu.ICON_SIZE;
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
        editorBatch.draw(sprite, getPosX(), getPosY(), SpriteMenu.ICON_SIZE, SpriteMenu.ICON_SIZE);
    }

    @Override
    public void drawPreview(@NotNull EditorBatch editorBatch) {
        editorBatch.draw(sprite, getPosX() + SpriteMenu.ICON_SIZE, getPosY() - SpriteMenu.ICON_SIZE * PREVIEW_SCALE,
                SpriteMenu.ICON_SIZE * PREVIEW_SCALE, SpriteMenu.ICON_SIZE * PREVIEW_SCALE);
    }
}
