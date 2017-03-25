package eu.mighty.game.object;

public class WallInitialData extends GameObjectInitialData {

	public int x;
	public int y;
	public int width;
	public int heigh;

	public WallInitialData(int x, int y, int w, int h) {
		super(GameObjectInitialData.TYPE_WALL);
		this.x = x;
		this.y = y;
		this.width = w;
		this.heigh = h;
	}
}
