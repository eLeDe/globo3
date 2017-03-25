package eu.mighty.game.object;


abstract public class GameObjectInitialData {

	public static final int TYPE_GREEN_GLOBO = 0;
	public static final int TYPE_WALL = 1;
	public static final int TYPE_SPIKE = 2;
	public static final int TYPE_BACKGROUND = 3;
	public static final int TYPE_EXIT_MARK = 4;

	public int type;

	public GameObjectInitialData(int type) {
		this.type = type;
	}
}
