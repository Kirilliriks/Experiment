package org.anotherteam.game.object.component.type.collider;

import org.anotherteam.debug.DebugBatch;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class AABB extends Component {

    protected final Vector2i firstBound;
    protected final Vector2i secondBound;
    protected final Vector2i offSet;

    protected Vector2i objectPosition;

    public AABB() {
        this.firstBound = new Vector2i(0, 0);
        this.secondBound = new Vector2i(0, 0);
        this.offSet = new Vector2i(0, 0);
        objectPosition = null;
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        objectPosition = ownerObject.getPosition();
    }

    public void setBounds(int width, int height) {
        setBounds(-width / 2, 0, width / 2, height);
    }

    public void setBounds(int firstX, int firstY, int secondX, int secondY) {
        setFirstBound(firstX, firstY);
        setSecondBound(secondX, secondY);
    }

    public void setFirstBound(int x, int y) {
        firstBound.set(x, y);
    }

    public void setSecondBound(int x, int y) {
        secondBound.set(x, y);
    }

    public void setOffSet(int x, int y) {
        offSet.set(x, y);
    }

    @NotNull
    public Vector2i getPosition() {
        return new Vector2i(objectPosition.x, objectPosition.y).add(offSet);
    }

    @NotNull
    public Vector2i getFirstBound() {
        return firstBound;
    }

    @NotNull
    public Vector2i getSecondBound() {
        return secondBound;
    }

    public boolean isIntersect(@NotNull Collider aabb){
        return  (!((aabb.getPosition().x + aabb.getSecondBound().x) < objectPosition.x + firstBound.x ||
                (objectPosition.x + secondBound.x) < aabb.getPosition().x  + aabb.getFirstBound().x));
    }

    // Debug
    public abstract void debugRender(boolean inEditor, @NotNull DebugBatch debugBatch);

    public void debugRender(boolean inEditor, @NotNull DebugBatch debugBatch, Color color) {
        final int x = objectPosition.x + offSet.x;
        final int y = objectPosition.y + offSet.y;

        var v1 = new Vector2f(x + firstBound.x, y + firstBound.y);
        var v2 = new Vector2f(x + firstBound.x, y + secondBound.y);
        var v3 = new Vector2f(x + secondBound.x, y + secondBound.y);
        var v4 = new Vector2f(x + secondBound.x, y + firstBound.y);

        if (!inEditor) {
            GameScreen.toWindowPos(v1);
            GameScreen.toWindowPos(v2);
            GameScreen.toWindowPos(v3);
            GameScreen.toWindowPos(v4);
        }

        debugBatch.drawLine(v1, v2, color);
        debugBatch.drawLine(v2, v3, color);
        debugBatch.drawLine(v3, v4, color);
        debugBatch.drawLine(v4, v1, color);
    }
}
