package eu.mighty.game.object;

public class SpikeInitialData extends GameObjectInitialData {

	public int x;
	public int y;

	public SpikeInitialData(int x, int y) {
		super(GameObjectInitialData.TYPE_SPIKE);
		this.x = x;
		this.y = y;
	}
}
