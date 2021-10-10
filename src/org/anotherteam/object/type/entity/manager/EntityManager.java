package org.anotherteam.object.type.entity.manager;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.GameManager;
import org.anotherteam.object.type.entity.EntityObject;
import org.anotherteam.object.type.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public final class EntityManager extends GameManager {

    private final Set<EntityObject> entities;

    public static Player player;

    public EntityManager() {
        super();
        this.entities = new HashSet<>();
    }

    public void addEntity(@NonNull EntityObject entityObject){
        if (entityObject instanceof Player) player = (Player) entityObject;
        entities.add(entityObject);
    }

    public void removeEntity(@NonNull EntityObject entityObject){
        entities.remove(entityObject);
    }

    @Override
    public void update(float delta) { }

    @Override
    public void clear() {
        entities.clear();
    }
}
