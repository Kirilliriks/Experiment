package org.anotherteam.editor.gui.menu.sprite;

import lombok.val;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpriteMenu extends GUIElement {

    private static final int ICON_SIZE = 32;

    private List<SpriteButton> buttons;

    private int offsetIcon;
    private int sizeX, sizeY;

    public SpriteMenu(float x, float y, int width, int height, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        buttons = new ArrayList<>();
    }

    public void setOffsetIcon(int offsetIcon) {
        this.offsetIcon = offsetIcon;
        sizeX = (width + offsetIcon) / ICON_SIZE;
        sizeY = (height + offsetIcon) / ICON_SIZE;
    }

    public void addButton(@NotNull Sprite sprite) {
        val index = buttons.size();
        if (index >= sizeX * sizeY) throw new LifeException("Need create pages mechanic");

        val spriteButton = new SpriteButton(sprite, 0, 0, this);

        int y = index % sizeX;
        int x = index / sizeY;

        spriteButton.setPos(x * ICON_SIZE                + (x + 1) * offsetIcon,
                            height - (y + 1) * ICON_SIZE - (y + 1) * offsetIcon);
        buttons.add(spriteButton);
    }
}
