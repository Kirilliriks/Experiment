package org.anotherteam.game.object.type.entity;

import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.type.state.State;
import org.anotherteam.game.object.component.type.sprite.SpriteController;
import org.anotherteam.game.object.component.type.state.StateController;
import org.anotherteam.game.object.component.type.transform.Transform;
import org.jetbrains.annotations.NotNull;

public abstract class EntityObject extends GameObject {

    protected final Transform transform;
    protected final StateController stateController;
    protected final SpriteController spriteController;

    public EntityObject(int x, int y, @NotNull State defaultState) {
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
