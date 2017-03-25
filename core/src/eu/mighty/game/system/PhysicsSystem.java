package eu.mighty.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import eu.mighty.game.component.BodyComponent;
import eu.mighty.game.component.ExitMarkComponent;
import eu.mighty.game.component.SpikeComponent;
import eu.mighty.game.component.TransformComponent;
import eu.mighty.game.screen.GameScreen;

public class PhysicsSystem extends IteratingSystem implements ContactListener {

    private static final float MAX_STEP_TIME = 1/45f;
    private static float accumulator = 0f;

    private World world;
	private GameScreen gameScreen;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

	public PhysicsSystem(World world, GameScreen gameScreen) {
        super(Family.all(BodyComponent.class, TransformComponent.class).get());

        this.world = world;
		this.gameScreen = gameScreen;

		this.world.setContactListener(this);

		this.bodiesQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if(accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            //Entity Queue
            for (Entity entity : bodiesQueue) {
                TransformComponent tfm = tm.get(entity);
                BodyComponent bodyComp = bm.get(entity);
                Vector2 position = bodyComp.body.getPosition();
                tfm.position.x = position.x;
                tfm.position.y = position.y;
				// TODO: fix rotation for green globo (should not rotate (?))
				// tfm.rotation = bodyComp.body.getAngle() *
				// MathUtils.radiansToDegrees;
            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }

	@Override
	public void beginContact(Contact contact) {

		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

		if (entityHasExitMarkComponent(entityA) | entityHasExitMarkComponent(entityB)) {
			this.gameScreen.setStateToWin();
		}

		if (entityHasSpikeComponent(entityA) | entityHasSpikeComponent(entityB)) {
			this.gameScreen.setStateToFail();
		}
	}

	private boolean entityHasExitMarkComponent(Entity e) {
		if (e.getComponent(ExitMarkComponent.class) == null) {
			return false;
		}
		return true;
	}

	private boolean entityHasSpikeComponent(Entity e) {
		if (e.getComponent(SpikeComponent.class) == null) {
			return false;
		}
		return true;
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
