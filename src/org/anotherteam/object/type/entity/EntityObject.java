package org.anotherteam.object.type.entity;


import lombok.NonNull;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.object.component.state.State;
import org.anotherteam.object.component.state.StateController;
import org.anotherteam.object.component.transform.Transform;
import org.anotherteam.render.sprite.SpriteAtlas;

public abstract class EntityObject extends GameObject {

    private final static int DEFAULT_SPEED = 25;

    protected final Transform transform;
    protected final StateController stateController;
    protected final SpriteController spriteController;

    public EntityObject(int x, int y, @NonNull SpriteAtlas spriteAtlas, @NonNull State defaultState) {
        super(x, y);
        spriteController = new SpriteController(1);
        spriteController.setSpriteAtlas(spriteAtlas);
        addComponent(spriteController);
        stateController = new StateController(defaultState);
        addComponent(stateController);
        transform = new Transform(DEFAULT_SPEED);
        addComponent(transform);

        stateController.setDefaultState();
    }
}
