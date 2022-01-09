package org.anotherteam.object.component.collider;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
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
    public void instanceBy(Component component) {
        val col = (Collider) component;
        firstBound.set(col.getFirstBound());
        secondBound.set(col.getSecondBound());
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

    @NonNull
    public Vector2i getPosition() {
        return new Vector2i(objectPosition.x, objectPosition.y).add(offSet);
    }

    @NonNull
    public Vector2i getFirstBound() {
        return firstBound;
    }

    @NonNull
    public Vector2i getSecondBound() {
        return secondBound;
    }

    public boolean isIntersect(@NonNull Collider aabb){
        return  (!((aabb.getPosition().x + aabb.getSecondBound().x) < objectPosition.x + firstBound.x ||
                (objectPosition.x + secondBound.x) < aabb.getPosition().x  + aabb.getFirstBound().x));
    }

    public abstract void debugRender(@NonNull DebugBatch debugBatch);

    public void debugRender(@NonNull DebugBatch debugBatch, Color color){
        val x = objectPosition.x + offSet.x;
        val y = objectPosition.y + offSet.y;
        val v1 = new Vector2f(x +  firstBound.x - 1, y +  firstBound.y);
        val v2 = new Vector2f(x +  firstBound.x, y + secondBound.y);
        val v3 = new Vector2f(x + secondBound.x, y + secondBound.y);
        val v4 = new Vector2f(x + secondBound.x, y +  firstBound.y);
        debugBatch.drawLine(v1, v2, color);
        debugBatch.drawLine(v2, v3, color);
        debugBatch.drawLine(v3, v4, color);
        debugBatch.drawLine(v4, v1, color);
    }
}
