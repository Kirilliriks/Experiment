package org.anotherteam.game.object.component.type.state;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.game.object.component.type.sprite.SpriteComponent;
import org.anotherteam.game.object.component.type.state.registry.StateSet;
import org.anotherteam.game.object.component.type.state.registry.StateSetRegistry;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public final class StateComponent extends Component {

    private SpriteComponent sprite;
    private StateSet states;
    private State state;

    @Override
    public void init() {
        defaultState();
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
    }

    @Override
    public void setDependencies() {
        if (sprite != null) return;

        sprite = getDependsComponent(SpriteComponent.class);
    }

    public void setStates(String states) {
        this.states = StateSetRegistry.get(states);
    }

    public void setStates(StateSet states) {
        this.states = states;
    }

    public void setState(String state) {
        setState(states.get(state));
    }

    public void setState(@NotNull State state) {
        this.state = state;

        if (state.getAnimation() == null) {
            sprite.stopAnimation();
            return;
        }
        sprite.setAnimation(state.getAnimation());
    }

    public void defaultState() {
        setState(states.getDefaultState());
    }

    @Override
    public JsonElement serialize(JsonObject result) {
        result.add("stateSetName", new JsonPrimitive(states.getName()));
        return result;
    }

    public static StateComponent deserialize(JsonObject object) {
        final String states = object.get("stateSetName").getAsString();

        final var stateComponent = new StateComponent();
        stateComponent.setStates(StateSetRegistry.get(states));
        return stateComponent;
    }
}
