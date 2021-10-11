package org.anotherteam.object.type.entity;


import lombok.NonNull;
import org.anotherteam.level.Level;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.collider.ColliderComponent;
import org.anotherteam.object.component.sprite.SpriteComponent;
import org.anotherteam.object.component.state.State;
import org.anotherteam.object.component.state.StateController;
import org.anotherteam.object.component.transform.TransformComponent;
import org.anotherteam.render.texture.Texture;
import org.joml.Vector2i;

public abstract class EntityObject extends GameObject {

    private final static int DEFAULT_SPEED = 25;

    protected final TransformComponent transform;
    protected final StateController stateController;
    protected final ColliderComponent collider;
    protected final SpriteComponent sprite;

    public EntityObject(@NonNull Vector2i position, @NonNull Level level, @NonNull Texture texture, @NonNull State startState) {
        super(position, level);
        sprite = new SpriteComponent( 1);
        sprite.setTexture(texture);
        addComponent(sprite);
        collider = new ColliderComponent(position);
        addComponent(collider);
        stateController = new StateController(startState);
        addComponent(stateController);
        transform = new TransformComponent(DEFAULT_SPEED);
        addComponent(transform);
    }
}
