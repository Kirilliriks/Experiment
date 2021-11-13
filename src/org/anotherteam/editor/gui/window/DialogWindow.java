package org.anotherteam.editor.gui.window;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.Nullable;

public abstract class DialogWindow extends GUIElement {
    public static final Color DEFAULT_COLOR = new Color(120, 120, 150, 255);

    private Runnable afterClose;

    public DialogWindow(int width, int height) {
        super(GameScreen.window.getWidth() / 2.0f, GameScreen.window.getHeight() * 0.8f, null);
        this.width = width;
        this.height = height;
        pos.x -= width / 2.0f;
        pos.y -= height / 2.0f;
        color.set(DEFAULT_COLOR);
        afterClose = null;
    }

    public void setOnAfterClose(@Nullable Runnable afterClose) {
        this.afterClose = afterClose;
    }

    @Override
    public void destroy() {
        if (afterClose != null)
            afterClose.run();
        super.destroy();
    }
}
