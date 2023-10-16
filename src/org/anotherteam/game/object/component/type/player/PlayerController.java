package org.anotherteam.game.object.component.type.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.anotherteam.game.Game;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.game.object.component.fieldcontroller.FieldController;
import org.anotherteam.game.object.component.type.collider.Collider;
import org.anotherteam.game.object.component.type.transform.Transform;
import org.anotherteam.input.Input;
import org.anotherteam.game.object.component.type.state.StateComponent;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public final class PlayerController extends Component {

    private Transform transform;
    private Collider collider;
    private StateComponent stateComponent;
    private Vector2i position;

    public PlayerController() {
        super();

        transform = null;
        collider = null;
        stateComponent = null;
        position = null;
        serializable = true;
    }

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
        if (transform != null && collider != null && stateComponent != null) return;

        transform = getDependsComponent(Transform.class);
        collider = getDependsComponent(Collider.class);
        stateComponent = getDependsComponent(StateComponent.class);
    }
    @Override
    public void update(float dt) {
        GameScreen.GAME_CAMERA.setPosition(position.x, GameScreen.HEIGHT / 2.0f);

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
            transform.getMoveImpulse().add(newMove * 2, 0);
        } else {
            transform.getMoveImpulse().set(0, 0);
        }

        if (Input.isKeyDown(Input.KEY_SHIFT)) transform.getMoveImpulse().x *= 2;

        if (transform.isMoving()) stateComponent.setState("walk");

        if (Input.isKeyPressed(Input.KEY_SPACE)) {
            Game.DEBUG_MODE = !Game.DEBUG_MODE;
        }
    }

    @Override
    public @NotNull JsonElement serialize(JsonObject result) {
        return result;
    }

    public static PlayerController deserialize(JsonObject object) {
        return new PlayerController();
    }
}
