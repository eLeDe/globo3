package eu.mighty.game.screen;

import java.util.ArrayList;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import eu.mighty.game.Globo;
import eu.mighty.game.Level;
import eu.mighty.game.system.PhysicsDebugSystem;
import eu.mighty.game.system.PhysicsSystem;
import eu.mighty.game.system.RenderingSystem;
import eu.mighty.game.system.UserInputSystem;

public class GameScreen extends DefaultScreen implements Screen {

	private PooledEngine entityEngine;
	private Globo game;
	private World world;

	// Available levels entities with initial parameters are stored here
	ArrayList<Level> levels;

	public int gameState;
	public static final int STATE_FADE_IN = 0;
	public static final int STATE_PLAYING = 1;
	public static final int STATE_FAIL = 2;
	public static final int STATE_WIN = 3;

	private int currentLevel;
	private int numberOfLevels;

	private boolean clearToStartNextLevel;

	public GameScreen(Globo game) {
		super(game);
		Gdx.app.log("GameScreen", "Initializing");
		this.game = game;
	}

	@Override
	public void show() {
		initPhysicsWorld();
		this.entityEngine = new PooledEngine();

		RenderingSystem renderingSystem = new RenderingSystem(game.batcher, this);
		this.entityEngine.addSystem(new UserInputSystem(renderingSystem.getCamera()));
		this.entityEngine.addSystem(new PhysicsSystem(this.world, this));
		this.entityEngine.addSystem(renderingSystem);
		this.entityEngine.addSystem(new PhysicsDebugSystem(this.world, renderingSystem.getCamera()));

		populateLevelsArray();

		this.currentLevel = 0;
		loadLevel(this.currentLevel);
	}

	public void setStateToFadeIn() {
		this.gameState = GameScreen.STATE_FADE_IN;
		this.entityEngine.getSystem(PhysicsSystem.class).setProcessing(false);
		this.entityEngine.getSystem(UserInputSystem.class).setProcessing(false);
		((RenderingSystem) (this.entityEngine.getSystem(RenderingSystem.class))).setRenderingStateFadeIn();
	}

	public void setStateToPlaying() {
		this.gameState = GameScreen.STATE_PLAYING;
		this.entityEngine.getSystem(PhysicsSystem.class).setProcessing(true);
		this.entityEngine.getSystem(UserInputSystem.class).setProcessing(true);
		((RenderingSystem) (this.entityEngine.getSystem(RenderingSystem.class))).setRenderingStatePlaying();
	}

	public void setStateToWin() {
		this.gameState = GameScreen.STATE_WIN;
		this.entityEngine.getSystem(PhysicsSystem.class).setProcessing(false);
		this.entityEngine.getSystem(UserInputSystem.class).setProcessing(false);
		((RenderingSystem) (this.entityEngine.getSystem(RenderingSystem.class))).setRenderingStateFadeOut();
	}

	public void setStateToFail() {
		this.gameState = GameScreen.STATE_FAIL;
		this.entityEngine.getSystem(PhysicsSystem.class).setProcessing(false);
		this.entityEngine.getSystem(UserInputSystem.class).setProcessing(false);
		((RenderingSystem) (this.entityEngine.getSystem(RenderingSystem.class))).setRenderingStateFadeOut();
	}

	public void raiseSignalToBeginNextLevelWhenEntityEngineFinishes() {
		this.clearToStartNextLevel = true;
	}

	private void initPhysicsWorld() {
		this.world = new World(new Vector2(0f, -9.8f), true);
	}

	// Creates the levels array and fills it with each level information
	private void populateLevelsArray() {
		if (levels == null) {
			levels = new ArrayList<Level>();
		} else if (!levels.isEmpty()) {
			levels.clear();
		}
		this.numberOfLevels = 0;

		Level level_0 = new Level(this.entityEngine, this.world);
		level_0.addGreenGlobo(200, 360);
		level_0.addExitMark(15 * 40, 8 * 40);
		level_0.addWall(80, 160, 16 * 40, 40);
		level_0.addWall(80, 400, 16 * 40, 40);
		level_0.addWall(80, 200, 40, 200);
		level_0.addWall(17 * 40, 200, 40, 200);
		level_0.addBackground("level_0.png", 400, 240);
		levels.add(level_0);
		this.numberOfLevels += 1;
		
		Level level_1 = new Level(this.entityEngine, this.world);
		level_1.addGreenGlobo(100, 300);
		level_1.addExitMark(9 * 40, 160);
		// Bottom wall (ground)
		level_1.addWall(0, 0, 800, 40);
		// other walls
		level_1.addWall(0, 40, 40, 440); // left
		level_1.addWall(40, 440, 760, 40); // up
		level_1.addWall(760, 40, 40, 440); // right
		level_1.addWall(7 * 40, 400, 160, 40); // up centered small thing
		level_1.addWall(17 * 40, 400, 80, 40); // up right-side small thing
		level_1.addWall(7 * 40, 40, 40, 200);
		level_1.addWall(8 * 40, 200, 320, 40);
		level_1.addWall(15 * 40, 120, 40, 80);
		level_1.addBackground("level_1.png", 400, 240);
		levels.add(level_1);
		this.numberOfLevels += 1;
		
		Level level_2 = new Level(this.entityEngine, this.world);
		level_2.addGreenGlobo(200, 60);
		level_2.addExitMark(19 * 40, 2 * 40);
		// external borders
		level_2.addWall(0, -1, 800, 0);
		level_2.addWall(0, 480, 800, 481);
		level_2.addWall(0, 0, 1, 480);
		level_2.addWall(800, -1, 800, 480);
		// blocks inside the screen
		level_2.addWall(0, 11 * 40, 4 * 40, 40);
		level_2.addWall(0, 0, 40, 400);
		level_2.addWall(80, 9 * 40, 120, 40);
		level_2.addWall(3 * 40, 2 * 40, 200, 40);
		level_2.addWall(7 * 40, 3 * 40, 40, 120);
		level_2.addWall(10 * 40, 0, 40, 240);
		level_2.addWall(7 * 40, 9 * 40, 4 * 40, 40);
		level_2.addWall(13 * 40, 8 * 40, 2 * 40, 2 * 40);
		level_2.addWall(13 * 40, 2 * 40, 2 * 40, 3 * 40);
		level_2.addWall(12 * 40, 11 * 40, 4 * 40, 40);
		level_2.addWall(17 * 40, 0, 120, 40);
		level_2.addWall(17 * 40, 120, 120, 40);
		level_2.addWall(17 * 40, 160, 40, 240);
		// spikes
		level_2.addSpike(8 * 40 + 20, 9 * 40);
		level_2.addSpike(9 * 40 + 20, 9 * 40);
		level_2.addSpike(13 * 40, 8 * 40 + 20);
		level_2.addSpike(13 * 40 + 20, 8 * 40);
		level_2.addSpike(15 * 40, 8 * 40 + 20);
		level_2.addSpike(11 * 40, 20);
		level_2.addSpike(11 * 40 + 20, 0);
		level_2.addSpike(12 * 40 + 20, 0);
		level_2.addBackground("level_2.png", 400, 240);
		levels.add(level_2);
		this.numberOfLevels += 1;
	}

	// Cleans the world and the entity engine and loads them with the bodies and
	// entities of the chosen level
	private void loadLevel(int level) {
		Gdx.app.debug("GameScreen", "Load new level");

		Array<Body> bodies = new Array<Body>();
		this.world.getBodies(bodies);
		for (Body bod : bodies) {
			this.world.destroyBody(bod);
		}

		this.entityEngine.removeAllEntities();

		levels.get(level).loadLevel();
		this.clearToStartNextLevel = false;
		this.currentLevel = level;
		setStateToFadeIn();
	}

	@Override
	public void render(float delta) {
		this.entityEngine.update(delta);
		stateUpdate();
	}

	private void stateUpdate() {
		if (this.clearToStartNextLevel == true) {
			if (this.gameState == GameScreen.STATE_WIN) {
				loadLevel((this.currentLevel + 1) % this.numberOfLevels);
			} else if (this.gameState == GameScreen.STATE_FAIL) {
				loadLevel(this.currentLevel);
			}
			this.clearToStartNextLevel = false;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
