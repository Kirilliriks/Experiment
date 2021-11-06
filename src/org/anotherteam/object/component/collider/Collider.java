package org.anotherteam.object.component.collider;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.debug.DebugRender;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.type.level.InteractiveObject;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class Collider extends AABB {

    private final InteractAABB interactAABB;

    private boolean solid;
    private boolean interactive;

    public Collider() {
        super();
        solid = false;
        interactAABB = new InteractAABB(this);
        serializable = true;
    }

    @Override
    public void initBy(Component component) {
        super.initBy(component);
        val collider = ((Collider)component);
        solid = collider.solid;
        interactive = collider.interactive;
        val intAABB = collider.getInteractAABB();
        interactAABB.setBounds(intAABB.firstBound.x, intAABB.firstBound.y, intAABB.secondBound.x, intAABB.secondBound.y);
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
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
        return isCollide(aabb, moveVector.x, moveVector.y);
    }

    public boolean isCollide(@NonNull Collider aabb, float x, float y){
        if (!aabb.isSolid()) return false;
        val collisionX =  objectPosition.x + secondBound.x + x >= aabb.getPosition().x  + aabb.getFirstBound().x &&
                objectPosition.x + firstBound.x + x <= aabb.getPosition().x  + aabb.getSecondBound().x;

        val collisionY =  objectPosition.y + secondBound.y + y >= aabb.getPosition().y  + aabb.getFirstBound().y &&
                objectPosition.y + firstBound.y + y <= aabb.getPosition().y  + aabb.getSecondBound().y;
        return collisionX && collisionY;
    }

    public boolean isOnMouse(float x, float y) {
        if (x < 0 || y < 0) return false;

        return objectPosition.x + firstBound.x <= x && x <= objectPosition.x + secondBound.x &&
               objectPosition.y + firstBound.y <= y && y <= objectPosition.y + secondBound.y;
    }

    public boolean isCanInteract(@NonNull Collider collider){
        if (!interactive) return false;
        return interactAABB.isInteract(collider);
    }

    public void checkInteract() {
        for (val object : ownerObject.getRoom().getGameObjects()) {
            if (!(object instanceof InteractiveObject)) continue;
            val collider = object.getCollider();
            if (!isCanInteract(collider)) continue;
            ((InteractiveObject)ownerObject).interactBy(object);
        }
    }

    public boolean checkCollide(@NonNull Vector2f moveVector) {
        for (val object : ownerObject.getRoom().getGameObjects()) {
            val collider = object.getCollider();
            if (!isCollide(collider, moveVector)) continue;
            return true;
        }
        return false;
    }

    public void setFlipX(boolean flip) {
        interactAABB.flip(flip);
    }

    @Override
    public void debugRender(@NonNull DebugRender debugRender) {
        interactAABB.debugRender(debugRender);
        if (isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY()))
            super.debugRender(debugRender, Color.GREEN);
        else super.debugRender(debugRender, Color.RED);
    }

    private static class InteractAABB extends AABB {

        private final Collider ownerCollider;

        public InteractAABB(@NonNull Collider ownerCollider){
            super();
            this.ownerCollider = ownerCollider;
            setBounds(ownerCollider.firstBound.x, ownerCollider.firstBound.y, ownerCollider.secondBound.x, ownerCollider.secondBound.y);
            serializable = false;
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
        public void debugRender(@NonNull DebugRender debugRender) {
            super.debugRender(debugRender, Color.BLUE);
        }
    }
}
