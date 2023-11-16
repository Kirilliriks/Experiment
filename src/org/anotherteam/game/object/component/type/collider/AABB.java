package org.anotherteam.game.object.component.type.collider;

import org.anotherteam.debug.DebugBatch;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.game.object.component.type.StaticComponent;
import org.anotherteam.screen.Screen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class AABB extends StaticComponent {

    protected final Vector2i min;
    protected final Vector2i max;
    protected final Vector2i offSet;

    protected Vector2i objectPosition;

    public AABB() {
        this.min = new Vector2i();
        this.max = new Vector2i();
        this.offSet = new Vector2i();
        objectPosition = null;
    }

    @Override
    public void init(Component component) {
        if (component instanceof AABB aabb) {
            setBounds(
                    aabb.min.x, aabb.min.y,
                    aabb.max.x, aabb.max.y
            );
        }
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        objectPosition = ownerObject.getPosition();
    }

    public void setBounds(int width, int height) {
        final boolean plusX = width % 2 != 0;
        setBounds(-width / 2, 0, width / 2 + (plusX ? 1 : 0), height);
    }

    public void setBounds(int minX, int minY, int maxX, int maxY) {
        setMin(minX, minY);
        setMaxB(maxX, maxY);
    }

    public void setMin(int x, int y) {
        min.set(x, y);
    }

    public void setMaxB(int x, int y) {
        max.set(x, y);
    }

    public void setOffSet(int x, int y) {
        offSet.set(x, y);
    }

    @NotNull
    public Vector2i getPosition() {
        return new Vector2i(objectPosition.x, objectPosition.y).add(offSet);
    }

    @NotNull
    public Vector2i getMin() {
        return min;
    }

    @NotNull
    public Vector2i getMax() {
        return max;
    }

    public boolean isIntersect(@NotNull Collider aabb){
        return  (!((aabb.getPosition().x + aabb.getMax().x) < objectPosition.x + min.x ||
                (objectPosition.x + max.x) < aabb.getPosition().x  + aabb.getMin().x));
    }

    // Debug
    public abstract void debugRender(boolean inEditor, @NotNull DebugBatch debugBatch);

    public void debugRender(boolean inEditor, @NotNull DebugBatch debugBatch, Color color) {
        final int x = objectPosition.x + offSet.x;
        final int y = objectPosition.y + offSet.y;

        var v1 = new Vector2f(x + min.x, y + min.y);
        var v2 = new Vector2f(x + min.x, y + max.y);
        var v3 = new Vector2f(x + max.x, y + max.y);
        var v4 = new Vector2f(x + max.x, y + min.y);

        if (!inEditor) {
            Screen.toWindowPos(v1);
            Screen.toWindowPos(v2);
            Screen.toWindowPos(v3);
            Screen.toWindowPos(v4);
        }

        debugBatch.drawLine(v1, v2, color);
        debugBatch.drawLine(v2, v3, color);
        debugBatch.drawLine(v3, v4, color);
        debugBatch.drawLine(v4, v1, color);
    }
}
