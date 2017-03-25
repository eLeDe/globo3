package eu.mighty.game.object;

public class ExitMarkInitialData extends GameObjectInitialData {

	public int centerX;
	public int centerY;

	public ExitMarkInitialData(int centerX, int centerY) {
		super(GameObjectInitialData.TYPE_EXIT_MARK);
		this.centerX = centerX;
		this.centerY = centerY;
	}
}
