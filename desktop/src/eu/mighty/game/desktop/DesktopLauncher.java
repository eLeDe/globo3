package eu.mighty.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import eu.mighty.game.Globo;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Globo v2 with components";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new Globo(), config);
	}
}
