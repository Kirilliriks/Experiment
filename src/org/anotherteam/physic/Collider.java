package org.anotherteam.physic;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.object.GameObject;
import org.anotherteam.render.GameRender;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class Collider extends AABB {

    private final InteractAABB interactAABB;

    private boolean solid;
    private boolean interact;

    public Collider(@NonNull Vector2i position, @NonNull GameObject gameObject){
        super(position, gameObject);
        solid = false;
        interactAABB = new InteractAABB(this);
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

    public void setFlip(boolean flip) {
        interactAABB.flip(flip);
    }

    @Override
    public void debugRender(@NonNull GameRender gameRender) {
        interactAABB.debugRender(gameRender);
        super.debugRender(gameRender, 0);
    }

    private static class InteractAABB extends AABB {

        private final Collider owner;

        public InteractAABB(@NonNull Collider owner){
            super(owner.position, owner.gameObject);
            this.owner = owner;
            setBounds(owner.firstBound.x, owner.firstBound.y, owner.secondBound.x, owner.secondBound.y);
        }

        public boolean isInteract(@NonNull Collider otherCollider){
            val otherInteractAABB = otherCollider.getInteractAABB();
            if ((otherCollider.position.x + otherCollider.secondBound.x) < otherInteractAABB.position.x + otherInteractAABB.offSet.x + firstBound.x ||
                    (otherInteractAABB.position.x + otherInteractAABB.offSet.x + otherInteractAABB.secondBound.x) < otherCollider.position.x  + otherCollider.firstBound.x) return false;
            return true;
        }

        public void flip(boolean flip) {
            if (flip) setOffSet(owner.getFirstBound().x - getSecondBound().x, 0);
            else setOffSet(owner.getSecondBound().x, 0);
        }

        @Override
        public void debugRender(@NonNull GameRender gameRender) {
            super.debugRender(gameRender, 0);
        }
    }
}
