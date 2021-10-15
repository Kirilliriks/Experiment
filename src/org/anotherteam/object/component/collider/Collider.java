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
    private boolean interactive;

    public Collider() {
        super();
        solid = false;
        interactAABB = new InteractAABB(this);
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        interactAABB.objectPosition = ownerObject.getPosition();
        ownerObject.addComponent(interactAABB);
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public void setInteractBounds(int firstX, int firstY, int secondX, int secondY) {
        interactAABB.setBounds(firstX, firstY, secondX, secondY);
    }

    @NotNull
    public InteractAABB getInteractAABB() {
        return interactAABB;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public boolean isCollide(@NonNull Collider aabb, @NonNull Vector2f moveVector){
        if (!aabb.isSolid()) return false;
        return  (!((aabb.getPosition().x + aabb.getSecondBound().x) < objectPosition.x + firstBound.x + moveVector.x ||
                (objectPosition.x + secondBound.x + moveVector.x) < aabb.getPosition().x  + aabb.getFirstBound().x));
    }

    public boolean isCanInteract(@NonNull Collider collider){
        if (!interactive) return false;
        return interactAABB.isInteract(collider);
    }

    public void checkInteract() {
        for (val object : ownerObject.getRoom().getGameObjects()) {
            if (!(object instanceof InteractiveObject)) continue;
            val collider = object.getComponent(Collider.class);
            if (collider == null) continue;
            if (!isCanInteract(collider)) continue;
            ((InteractiveObject)ownerObject).interactBy(object);
        }
    }

    public boolean checkCollide(@NonNull Vector2f moveVector) {
        for (val object : ownerObject.getRoom().getGameObjects()) {
            val collider = object.getComponent(Collider.class);
            if (collider == null) continue;
            if (!isCollide(collider, moveVector)) continue;
            return true;
        }
        return false;
    }

    public void setFlipX(boolean flip) {
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
            super();
            this.ownerCollider = ownerCollider;
            setBounds(ownerCollider.firstBound.x, ownerCollider.firstBound.y, ownerCollider.secondBound.x, ownerCollider.secondBound.y);
        }

        public boolean isInteract(@NonNull Collider otherCollider){
            val otherInteractAABB = otherCollider.interactAABB;
            return  (!((otherCollider.objectPosition.x + otherCollider.secondBound.x) < otherInteractAABB.objectPosition.x + otherInteractAABB.offSet.x + firstBound.x ||
                    (otherInteractAABB.objectPosition.x + otherInteractAABB.offSet.x + otherInteractAABB.secondBound.x) < otherCollider.objectPosition.x  + otherCollider.firstBound.x));
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
