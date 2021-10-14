package org.anotherteam.editor.gui.barmenu;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class SwitchButton extends Button {

    private SwitchMenu switchMenu;

    public SwitchButton(String text, float x, float y, int height) {
        super(text, x, y, height);
        clicked = false;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Override
    public void setOwner(@NotNull GUIElement ownerElement) {
        super.setOwner(ownerElement);
        if (!(ownerElement instanceof SwitchMenu))
            throw new LifeException("BarButton only for SwitchMenu");
        switchMenu = (SwitchMenu) ownerElement;
    }

    @Override
    public void update(float dt) {
        if (clicked) return;
        color.r = 0;
        if (!mouseOnWidget()) return;
        if (!Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) return;
        switchMenu.setClicked(this);
        clicked = true;
        color.r = 255;
        if (runnable != null) runnable.run();
    }
}
