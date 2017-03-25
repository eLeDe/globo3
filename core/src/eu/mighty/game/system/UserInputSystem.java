package eu.mighty.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eu.mighty.game.Defaults;
import eu.mighty.game.component.BodyComponent;
import eu.mighty.game.component.PlayerComponent;

public class UserInputSystem extends IteratingSystem implements InputProcessor {

	private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);

	// control related variables
	Vector2 touchPos;
	boolean screenJustPressed;
	Vector3 currentPos, lastPos, velocity;
	float powerFactor;

	private OrthographicCamera camera;

	public UserInputSystem(OrthographicCamera orthographicCamera) {
		super(Family.all(BodyComponent.class, PlayerComponent.class).get());

		this.currentPos = new Vector3(0, 0, 0);
		this.lastPos = new Vector3(0, 0, 0);
		this.velocity = new Vector3(0, 0, 0);
		this.touchPos = new Vector2(0, 0);
		this.screenJustPressed = true;
		this.powerFactor = 800;
		this.camera = orthographicCamera;

		Gdx.input.setInputProcessor(this);
	}

	
	@Override
	public void processEntity(Entity entity, float deltaTime) {

		BodyComponent bodyC = bm.get(entity);

		Circle globoGeometry = new Circle(bodyC.body.getPosition(), Defaults.GREEN_GLOBO_RADIUS);

		Gdx.app.debug("UserInputSystem", "globo center:" + bodyC.body.getPosition());
		Gdx.app.debug("UserInputSystem", "mouse center:" + new Vector2(currentPos.x, 480 - currentPos.y));

		if (globoGeometry.contains(new Vector2(currentPos.x, 480 - currentPos.y))) {
			bodyC.body.applyLinearImpulse(new Vector2(velocity.x, -1 * velocity.y), touchPos,
					true);
			screenJustPressed = true;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenJustPressed = true;
		velocity.x = 0;
		velocity.y = 0;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenJustPressed = true;
		velocity.x = 0;
		velocity.y = 0;
		currentPos.set(0, 0, 0);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		touchPos.x = screenX;
		touchPos.y = Gdx.graphics.getHeight() - screenY;
		currentPos.set(touchPos.x, touchPos.y, 0);
		camera.unproject(currentPos);

		if (screenJustPressed == true) {
			velocity.x = 0;
			velocity.y = 0;
			lastPos.x = currentPos.x;
			lastPos.y = currentPos.y;
			screenJustPressed = false;
		} else {
			velocity.x = (currentPos.x - lastPos.x) * powerFactor;
			velocity.y = (currentPos.y - lastPos.y) * powerFactor;
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
