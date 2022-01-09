package org.anotherteam.object.type.entity;


import lombok.NonNull;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.component.type.state.State;
import org.anotherteam.object.component.type.state.StateController;
import org.anotherteam.object.component.type.transform.Transform;

public abstract class EntityObject extends GameObject {

    protected final Transform transform;
    protected final StateController stateController;
    protected final SpriteController spriteController;

    public EntityObject(int x, int y, @NonNull State defaultState) {
        super(x, y);
        spriteController = new SpriteController();
        spriteController.setDrawPriority(1);
        addComponent(spriteController);
        stateController = new StateController();
        stateController.setDefaultState(defaultState);
        addComponent(stateController);
        transform = new Transform();
        addComponent(transform);
    }
}
