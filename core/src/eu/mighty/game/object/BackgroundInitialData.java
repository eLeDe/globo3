package eu.mighty.game.object;

public class BackgroundInitialData extends GameObjectInitialData {

	public String pathToImageFile;
	public float backgroundCenterX;
	public float backgroundCenterY;

	public BackgroundInitialData(String pathToImageFile, float x, float y) {
		super(GameObjectInitialData.TYPE_BACKGROUND);
		this.pathToImageFile = pathToImageFile;
		this.backgroundCenterX = x;
		this.backgroundCenterY = y;
	}
}
