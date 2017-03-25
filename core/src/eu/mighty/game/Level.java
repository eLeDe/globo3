package eu.mighty.game;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import eu.mighty.game.component.BodyComponent;
import eu.mighty.game.component.ExitMarkComponent;
import eu.mighty.game.component.PlayerComponent;
import eu.mighty.game.component.SpikeComponent;
import eu.mighty.game.component.TextureComponent;
import eu.mighty.game.component.TransformComponent;
import eu.mighty.game.object.BackgroundInitialData;
import eu.mighty.game.object.ExitMarkInitialData;
import eu.mighty.game.object.GameObjectInitialData;
import eu.mighty.game.object.GreenGloboInitialData;
import eu.mighty.game.object.SpikeInitialData;
import eu.mighty.game.object.WallInitialData;

// The level class maintains information regarding the initial state of a level
// the class also provides with a helper to load a level into the entities and
// physics engines.
public class Level {

	private World world;
	private PooledEngine entityEngine;

	private ArrayList<GameObjectInitialData> objects;

	private TextureRegion greenGloboTextureRegion;


	public Level(PooledEngine entityEngine, World world) {
		this.entityEngine = entityEngine;
		this.world = world;

		this.objects = new ArrayList<GameObjectInitialData>();

		Texture tex = new Texture(Gdx.files.internal(Defaults.playerTextureFile));
		this.greenGloboTextureRegion = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
	}

	public void addGreenGlobo(int x, int y) {
		GreenGloboInitialData g = new GreenGloboInitialData(x, y);
		this.objects.add(g);
	}

	public void addExitMark(int x, int y) {
		ExitMarkInitialData e = new ExitMarkInitialData(x, y);
		this.objects.add(e);
	}

	public void addWall(int x, int y, int w, int h) {
		WallInitialData ww = new WallInitialData(x, y, w, h);
		this.objects.add(ww);
	}

	public void addBackground(String fileName, float backgroundCenterX, float backgroundCenterY) {
		BackgroundInitialData b = new BackgroundInitialData(fileName, backgroundCenterX, backgroundCenterY);
		this.objects.add(b);
	}

	public void addSpike(int x, int y) {
		SpikeInitialData e = new SpikeInitialData(x, y);
		this.objects.add(e);
	}

	public void loadLevel() {

		for (GameObjectInitialData object : this.objects) {
			if (object.type == GameObjectInitialData.TYPE_GREEN_GLOBO) {
				GreenGloboInitialData gg = (GreenGloboInitialData) object;

				Entity e = this.entityEngine.createEntity();

				e.add(new PlayerComponent());

				TextureComponent tc = new TextureComponent();
				tc.region = this.greenGloboTextureRegion;
				e.add(tc);

				BodyComponent bc = new BodyComponent();
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.DynamicBody;
				bodyDef.position.set(gg.x, gg.y); // position of the center!
				Body body = world.createBody(bodyDef);

				// Create a circle shape and set its radius
				CircleShape circle = new CircleShape();
				circle.setRadius(Defaults.GREEN_GLOBO_RADIUS);

				// Create a fixture definition to apply our shape to
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape = circle;
				// TODO Move these fixed parameters to Defaults
				fixtureDef.density = 0.3f;
				fixtureDef.friction = 0.6f;
				fixtureDef.restitution = 0.6f; // Make it bounce a little bit

				// Create our fixture and attach it to the body
				body.createFixture(fixtureDef);

				body.setUserData(e);

				circle.dispose();

				bc.body = body;

				e.add(bc);

				TransformComponent tfc = new TransformComponent();
				tfc.position.set(gg.x, gg.y, Defaults.Z_GREEN_GLOBO);
				tfc.rotation = 0f;
				tfc.scale.set(1f, 1f);
				e.add(tfc);

				this.entityEngine.addEntity(e);

				continue;
			}
			if (object.type == GameObjectInitialData.TYPE_WALL) {
				WallInitialData w = (WallInitialData) object;

				Entity e = this.entityEngine.createEntity();

				BodyComponent bc = new BodyComponent();
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.StaticBody;
				// position of the center!
				bodyDef.position.set(w.x + w.width / 2, w.y + w.heigh / 2);
				Body body = world.createBody(bodyDef);

				PolygonShape box = new PolygonShape();
				box.setAsBox(w.width / 2, w.heigh / 2);

				// Create a fixture definition to apply our shape to
				FixtureDef fixtureDef = new FixtureDef();
				// TODO Move these fixed parameters to Defaults
				fixtureDef.shape = box;
				fixtureDef.density = 1f;
				fixtureDef.friction = 0.5f;

				// Create our fixture and attach it to the body
				body.createFixture(fixtureDef);

				body.setUserData(e);

				box.dispose();

				bc.body = body;

				e.add(bc);

				this.entityEngine.addEntity(e);

				continue;
			}
			if (object.type == GameObjectInitialData.TYPE_BACKGROUND) {
				BackgroundInitialData b = (BackgroundInitialData) object;

				Entity e = this.entityEngine.createEntity();

				TextureComponent tc = new TextureComponent();
				Texture tex = new Texture(Gdx.files.internal(Defaults.BACKGROUND_BASE_DIRECTORY + b.pathToImageFile));
				tc.region = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());

				e.add(tc);

				TransformComponent tfc = new TransformComponent();
				tfc.position.set(b.backgroundCenterX, b.backgroundCenterY, Defaults.Z_BACKGROUND);
				tfc.rotation = 0f;
				tfc.scale.set(1f, 1f);
				e.add(tfc);

				this.entityEngine.addEntity(e);

				continue;
			}
			if (object.type == GameObjectInitialData.TYPE_EXIT_MARK) {
				ExitMarkInitialData em = (ExitMarkInitialData) object;

				Entity e = this.entityEngine.createEntity();

				e.add(new ExitMarkComponent());

				BodyComponent bc = new BodyComponent();
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.StaticBody;
				bodyDef.position.set(em.centerX, em.centerY);
				Body body = world.createBody(bodyDef);

				// Create a circle shape and set its radius
				CircleShape circle = new CircleShape();
				circle.setRadius(15);

				// Create a fixture definition to apply our shape to
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape = circle;

				// Create our fixture and attach it to the body
				body.createFixture(fixtureDef);

				body.setUserData(e);

				circle.dispose();

				bc.body = body;

				e.add(bc);

				this.entityEngine.addEntity(e);

				continue;
			}
			if (object.type == GameObjectInitialData.TYPE_SPIKE) {
				SpikeInitialData s = (SpikeInitialData) object;

				Entity e = this.entityEngine.createEntity();

				e.add(new SpikeComponent());

				BodyComponent bc = new BodyComponent();
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.StaticBody;
				bodyDef.position.set(s.x, s.y); // position of the center!
				Body body = world.createBody(bodyDef);

				// Create a circle shape and set its radius
				CircleShape circle = new CircleShape();
				circle.setRadius(Defaults.SPIKE_RADIUS);

				// Create a fixture definition to apply our shape to
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape = circle;

				// Create our fixture and attach it to the body
				body.createFixture(fixtureDef);

				body.setUserData(e);

				circle.dispose();

				bc.body = body;

				e.add(bc);

				this.entityEngine.addEntity(e);

				continue;
			}
		}
	}
}
