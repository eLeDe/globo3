package eu.mighty.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eu.mighty.game.screen.IntroScreen;

public class Globo extends Game {
	// used by all screens
	public SpriteBatch batcher;

	@Override
	public void create () {
		this.batcher = new SpriteBatch();

		// setScreen(new BattleScreen(this));
		setScreen(new IntroScreen(this));
	}
	
	@Override
	public void render() {
		super.render();
	}
}
