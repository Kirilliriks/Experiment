package org.anotherteam.editorold.gui.window;

import org.anotherteam.editorold.gui.GUIElement;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.Nullable;

public abstract class DialogWindow extends GUIElement {
    public static final Color DEFAULT_COLOR = new Color(120, 120, 150);

    private Runnable afterClose;

    public DialogWindow(int width, int height) {
        this(GameScreen.window.getWidth() / 2, (int) (GameScreen.window.getHeight() * 0.7f), width, height);
    }

    public DialogWindow(int x, int y, int width, int height) {
        super(x, y, null);
        setWidth(width);
        setHeight(height);
        color.set(DEFAULT_COLOR);
        afterClose = null;
    }

    public void setOnAfterClose(@Nullable Runnable afterClose) {
        this.afterClose = afterClose;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        offset.x = -width / 2f;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        offset.y = -height / 2f;
    }

    @Override
    public void update(float dt) {
        updateElements(dt);
    }

    @Override
    public void destroy() {
        if (afterClose != null) {
            afterClose.run();
        }

        super.destroy();
    }
}
