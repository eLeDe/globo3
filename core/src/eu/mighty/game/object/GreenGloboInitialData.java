package eu.mighty.game.object;

public class GreenGloboInitialData extends GameObjectInitialData {

	public int x;
	public int y;

	public GreenGloboInitialData(int x, int y) {
		super(GameObjectInitialData.TYPE_GREEN_GLOBO);
		this.x = x;
		this.y = y;
	}
}
