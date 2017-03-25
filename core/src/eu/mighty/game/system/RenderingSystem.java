package eu.mighty.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import eu.mighty.game.ZComparator;
import eu.mighty.game.component.PlayerComponent;
import eu.mighty.game.component.TextureComponent;
import eu.mighty.game.component.TransformComponent;
import eu.mighty.game.screen.GameScreen;


public class RenderingSystem extends IteratingSystem {

	public int renderingState;
	public static final int STATE_FADE_IN = 0;
	public static final int STATE_PLAYING = 1;
	public static final int STATE_FADE_OUT = 2;

	float fadeColor;
	private ShapeRenderer shapeRenderer;

    private SpriteBatch batch;
	private GameScreen gameScreen;
    private Array<Entity> renderQueue;
	private ZComparator comparator;
    private OrthographicCamera cam;

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;
	private ComponentMapper<PlayerComponent> playerM;

	public RenderingSystem(SpriteBatch batch, GameScreen gameScreen) {
		super(Family.all(TransformComponent.class, TextureComponent.class).get());

		this.textureM = ComponentMapper.getFor(TextureComponent.class);
		this.transformM = ComponentMapper.getFor(TransformComponent.class);
		this.playerM = ComponentMapper.getFor(PlayerComponent.class);

		this.renderQueue = new Array<Entity>();

        this.batch = batch;
		this.gameScreen = gameScreen;

		this.cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.cam.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);

		this.comparator = new ZComparator();

		this.renderingState = RenderingSystem.STATE_FADE_IN;

		this.shapeRenderer = new ShapeRenderer();

		// The system starts like if the game is going to start. The GameScreen
		// is the one telling the system if it has to fade in first.
		setRenderingStatePlaying();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(comparator);

        cam.update();

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();

        for (Entity entity : renderQueue) {
			TextureComponent tex = this.textureM.get(entity);
			TransformComponent t = this.transformM.get(entity);
			PlayerComponent p = this.playerM.get(entity);


            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width/2f;
            float originY = height/2f;

            // horrible hack to draw the globo texture in it's place
            if (p == null) {
				batch.draw(tex.region, t.position.x - originX, t.position.y - originY);
            } else {
				batch.draw(tex.region, t.position.x - originX, t.position.y - originY - 10);
            }
        }

		if (this.renderingState == RenderingSystem.STATE_FADE_IN) {
			shapeRenderer.setProjectionMatrix(this.cam.combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(fadeColor, fadeColor, fadeColor, fadeColor);
			shapeRenderer.rect(0, 0, 800, 480);
			shapeRenderer.end();

			fadeColor = fadeColor - 0.01f;
			if (fadeColor <= 0) {
				fadeColor = 0;
				this.gameScreen.setStateToPlaying();
			}
		}

		if (this.renderingState == RenderingSystem.STATE_FADE_OUT) {
			shapeRenderer.setProjectionMatrix(this.cam.combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(fadeColor, fadeColor, fadeColor, fadeColor);
			shapeRenderer.rect(0, 0, 800, 480);
			shapeRenderer.end();

			fadeColor = fadeColor + 0.01f;
			if (fadeColor >= 1) {
				this.gameScreen.raiseSignalToBeginNextLevelWhenEntityEngineFinishes();
			}
		}

		batch.end();
		renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

	public void setRenderingStateFadeIn() {
		this.renderingState = RenderingSystem.STATE_FADE_IN;
		this.fadeColor = 1f;
	}

	public void setRenderingStatePlaying() {
		this.renderingState = RenderingSystem.STATE_PLAYING;
	}

	public void setRenderingStateFadeOut() {
		this.renderingState = RenderingSystem.STATE_FADE_OUT;
		this.fadeColor = 0f;
	}
}
