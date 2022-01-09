package org.anotherteam.object.component.type.player;

import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.fieldcontroller.FieldController;
import org.anotherteam.object.component.type.collider.Collider;
import org.anotherteam.object.component.type.state.StateController;
import org.anotherteam.object.component.type.state.type.PlayerState;
import org.anotherteam.object.component.type.transform.Transform;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public final class PlayerController extends Component {

    private Transform transform;
    private Collider collider;
    private StateController stateController;

    private Vector2i position;

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        position = ownerObject.getPosition();
    }

    @Override
    public List<FieldController> getFields() {
        final List<FieldController> list = new ArrayList<>();
        list.add(new FieldController("posX", position.x, (value) -> position.x = (int) value));
        list.add(new FieldController("posY", position.y, (value) -> position.y = (int) value));
        return list;
    }

    @Override
    public void setDependencies() {
        if (transform != null && collider != null && stateController != null) return;

        transform = getDependsComponent(Transform.class);
        collider = getDependsComponent(Collider.class);
        stateController = getDependsComponent(StateController.class);
    }

    @Override
    public void update(float dt) {
        GameScreen.gameCamera.setPosition(position.x, GameScreen.HEIGHT / 2.0f);

        float newMove = 0;

        if (Input.isKeyDown(Input.KEY_A)) {
            newMove -= transform.getSpeed() * dt;
        }
        if (Input.isKeyDown(Input.KEY_D)) {
            newMove += transform.getSpeed() * dt;
        }
        if (Input.isKeyDown(Input.KEY_E)) {
            collider.checkInteract();
        }

        if (newMove != 0) {
            transform.moveImpulse.add(newMove * 2, 0);
        } else {
            transform.moveImpulse.set(0, 0);
        }

        if (Input.isKeyDown(Input.KEY_SHIFT)) transform.moveImpulse.x *= 2;

        if (transform.isMoving()) stateController.setState(PlayerState.WALK);

        if (Input.isKeyPressed(Input.KEY_SPACE)) {
            Game.DebugMode = !Game.DebugMode;
        }
    }
}
