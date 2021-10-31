package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.editor.gui.GUIElement;

import java.util.List;

public class SpriteMenu extends GUIElement {

    public List<SpriteButton> buttons;

    public SpriteMenu(float x, float y, int width, int height, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
    }
}
