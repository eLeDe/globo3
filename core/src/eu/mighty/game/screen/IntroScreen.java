package eu.mighty.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import eu.mighty.game.Defaults;
import eu.mighty.game.Globo;

public class IntroScreen extends DefaultScreen implements Screen {

	SpriteBatch batch;
	Texture img;
	Sprite spr;
	OrthographicCamera cam;
	StretchViewport sv;

	int waitFramesForHandle = Defaults.SCREEN_INITIAL_WAIT_TIME_SEC;

	public IntroScreen(Globo game) {
		super(game);
		Gdx.app.log("IntroScreen", "Initializing");

		this.cam = new OrthographicCamera();
		this.sv = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.cam);
		this.cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

		this.batch = new SpriteBatch();
		this.img = new Texture("screenImages/badlogic.jpg");
		this.spr = new Sprite(this.img);
		this.spr.setPosition(0, -Gdx.graphics.getHeight());
		this.spr.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.waitFramesForHandle -= 1;
		scrollIntroImage();
		if (this.waitFramesForHandle <= 0)
			handleInput();

		this.cam.update();
		batch.setProjectionMatrix(this.cam.combined);

		this.batch.begin();
		this.spr.draw(batch);
		this.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		this.sv.update(width, height);
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
			this.mightyGame.setScreen(new GameScreen(this.mightyGame));
		}
	}

	private void scrollIntroImage() {
		this.spr.setPosition(0, this.spr.getY() + 1);
		if (this.spr.getY() > 0) this.spr.setPosition(0,0);
	}

}
