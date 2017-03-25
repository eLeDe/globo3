package eu.mighty.game;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import eu.mighty.game.component.TransformComponent;


public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<TransformComponent> transformM;

    public ZComparator(){
        transformM = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
		return (int) Math.signum(transformM.get(entityA).position.z - transformM.get(entityB).position.z);
    }
}
