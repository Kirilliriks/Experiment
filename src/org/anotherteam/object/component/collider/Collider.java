package org.anotherteam.object.component.collider;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.type.level.InteractiveObject;
import org.anotherteam.render.GameRender;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class Collider extends AABB {

    private final InteractAABB interactAABB;

    private boolean solid;
    private boolean interact;

    public Collider(@NonNull Vector2i position){
        super(position);
        solid = false;
        interactAABB = new InteractAABB(this);
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        ownerObject.addComponent(interactAABB);
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setInteract(boolean interact) {
        this.interact = interact;
    }

    public void setInteractBounds(int firstX, int firstY, int secondX, int secondY) {
        interactAABB.setBounds(firstX, firstY, secondX, secondY);
    }

    @NotNull
    private InteractAABB getInteractAABB() {
        return interactAABB;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isCollide(@NonNull Collider aabb, @NonNull Vector2f moveVector){
        if (!aabb.isSolid()) return false;
        if ((aabb.getPosition().x + aabb.getSecondBound().x) < position.x + firstBound.x + moveVector.x ||
                (position.x + secondBound.x + moveVector.x) < aabb.getPosition().x  + aabb.getFirstBound().x) return false;
        return true;
    }

    public boolean isCanInteract(@NonNull Collider collider){
        if (!interact) return false;
        return interactAABB.isInteract(collider);
    }

    public void checkInteract() {
        for (val object : ownerObject.getLevel().getGameObjects()) {
            if (!(object instanceof InteractiveObject)) continue;
            val collider = object.getComponent(Collider.class);
            if (collider == null) continue;
            if (!isCanInteract(collider)) continue;
            ((InteractiveObject)ownerObject).interactBy(object);
        }
    }

    public boolean checkCollide(@NonNull Vector2f moveVector) {
        for (val object : ownerObject.getLevel().getGameObjects()) {
            val collider = object.getComponent(Collider.class);
            if (collider == null) continue;
            if (!isCollide(collider, moveVector)) continue;
            return true;
        }
        return false;
    }

    public void setFlip(boolean flip) {
        interactAABB.flip(flip);
    }

    @Override
    public void debugRender(@NonNull GameRender gameRender) {
        interactAABB.debugRender(gameRender);
        super.debugRender(gameRender, Color.RED);
    }

    private static class InteractAABB extends AABB {

        private final Collider ownerCollider;

        public InteractAABB(@NonNull Collider ownerCollider){
            super(ownerCollider.position);
            this.ownerCollider = ownerCollider;
            setBounds(ownerCollider.firstBound.x, ownerCollider.firstBound.y, ownerCollider.secondBound.x, ownerCollider.secondBound.y);
        }

        public boolean isInteract(@NonNull Collider otherCollider){
            val otherInteractAABB = otherCollider.getInteractAABB();
            if ((otherCollider.position.x + otherCollider.secondBound.x) < otherInteractAABB.position.x + otherInteractAABB.offSet.x + firstBound.x ||
                    (otherInteractAABB.position.x + otherInteractAABB.offSet.x + otherInteractAABB.secondBound.x) < otherCollider.position.x  + otherCollider.firstBound.x) return false;
            return true;
        }

        public void flip(boolean flip) {
            if (flip) setOffSet(ownerCollider.getFirstBound().x - getSecondBound().x, 0);
            else setOffSet(ownerCollider.getSecondBound().x, 0);
        }

        @Override
        public void debugRender(@NonNull GameRender gameRender) {
            super.debugRender(gameRender, Color.RED);
        }
    }
}
