package org.anotherteam.object.type.entity;


import lombok.NonNull;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.object.component.state.State;
import org.anotherteam.object.component.state.StateController;
import org.anotherteam.object.component.transform.Transform;
import org.anotherteam.render.sprite.Sprite;
import org.joml.Vector2i;

public abstract class EntityObject extends GameObject {

    private final static int DEFAULT_SPEED = 25;

    protected final Transform transform;
    protected final StateController stateController;
    protected final Collider collider;
    protected final SpriteController spriteController;

    public EntityObject(@NonNull Vector2i position, @NonNull Room room, @NonNull Sprite sprite, @NonNull State startState) {
        super(position, room);
        spriteController = new SpriteController( 1);
        spriteController.setSprite(sprite);
        addComponent(spriteController);
        collider = new Collider(position);
        addComponent(collider);
        stateController = new StateController(startState);
        addComponent(stateController);
        transform = new Transform(DEFAULT_SPEED);
        addComponent(transform);
    }
}
