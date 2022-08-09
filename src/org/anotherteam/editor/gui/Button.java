package org.anotherteam.editor.gui;

import org.anotherteam.util.Color;

public abstract class Button extends GUIElement {

    protected boolean clicked;
    protected Click onClick;
    protected Click afterClick;

    protected boolean lock;

    protected float releaseTime;
    protected float timeToRelease;

    public Button(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        setColor(Color.VOID);
        releaseTime = 0.2f;
        timeToRelease = 0.0f;
    }

    @Override
    public abstract void update(float dt);

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void setAfterClick(Click afterClick) {
        this.afterClick = afterClick;
    }

    public void setOnClick(Click click) {
        this.onClick = click;
    }

    public void setClicked(boolean clicked) {
        setClicked(clicked, true);
    }

    public void setClicked(boolean clicked, boolean left) {
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void runClick() {
        runClick(null);
    }

    public void runClick(ClickInfo info) {
        if (onClick == null) return;

        if (info == null) {
            onClick.run(new ClickInfo());
            return;
        }

        onClick.run(info);
    }

    public void runAfterClick() {
        runAfterClick(null);
    }

    public void runAfterClick(ClickInfo info) {
        if (afterClick == null) return;

        if (info == null) {
            afterClick.run(new ClickInfo());
            return;
        }

        afterClick.run(info);
    }

    public static class ClickInfo {
        public final boolean left;

        public ClickInfo() {
            this(true);
        }

        public ClickInfo(boolean left) {
            this.left = left;
        }

        public boolean isLeft() {
            return left;
        }

        public boolean isRight() {
            return !left;
        }
    }

    public interface Click {

        void run(ClickInfo info);
    }
}
