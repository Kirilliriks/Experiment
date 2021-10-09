package org.anotherteam.physic;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.object.GameObject;
import org.anotherteam.render.GameRender;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class AABB {

    protected final GameObject gameObject;

    protected final Vector2i position;
    protected final Vector2i offSet;
    protected final Vector2i firstBound;
    protected final Vector2i secondBound;

    public AABB(@NonNull Vector2i position, @NonNull GameObject gameObject) {
        this.gameObject = gameObject;
        this.position = position;
        this.offSet = new Vector2i(0, 0);
        this.firstBound = new Vector2i(0, 0);
        this.secondBound = new Vector2i(0, 0);
    }

    public void setBounds(int firstX, int firstY, int secondX, int secondY) {
        setFirstBound(firstX, firstY);
        setSecondBound(secondX, secondY);
    }

    public void setFirstBound(int x, int y) {
        this.firstBound.set((int)(x * gameObject.getScale()), (int)(y * gameObject.getScale()));
    }

    public void setSecondBound(int x, int y) {
        this.secondBound.set((int)(x * gameObject.getScale()), (int)(y * gameObject.getScale()));
    }

    public void setOffSet(int x, int y) {
        this.offSet.set(x, y);
    }

    /**
     * @return возвращает смещенную позицию
     */
    @NonNull
    public Vector2i getPosition() {
        return new Vector2i(position.x, position.y).add(offSet);
    }

    @NonNull
    public Vector2i getFirstBound() {
        return firstBound;
    }

    @NonNull
    public Vector2i getSecondBound() {
        return secondBound;
    }

    /**
     * @return вернёт true если коллайдеры пересекаются
     */
    public boolean isIntersect(@NonNull Collider aabb){
        if ((aabb.getPosition().x + aabb.getSecondBound().x) < position.x + firstBound.x ||
                (position.x + secondBound.x) < aabb.getPosition().x  + aabb.getFirstBound().x) return false;
        return true;
    }

    public abstract void debugRender(@NonNull GameRender gameRender);

    public void debugRender(@NonNull GameRender gameRender, int TODOcolor){
        val v1 = new Vector2f(position.x + offSet.x + firstBound.x, position.y + offSet.y);
        val v2 = new Vector2f(position.x + offSet.x + firstBound.x, position.y + offSet.y + firstBound.y);
        val v3 = new Vector2f(position.x + offSet.x + secondBound.x, position.y + offSet.y + secondBound.y);
        val v4 = new Vector2f(position.x + offSet.x + secondBound.x, position.y + offSet.y);
        //gameRender.drawDebugLine(v1, v2, color);
        //gameRender.drawDebugLine(v2, v3, color);
        //gameRender.drawDebugLine(v3, v4, color);
        //gameRender.drawDebugLine(v4, v1, color);
    }
}
