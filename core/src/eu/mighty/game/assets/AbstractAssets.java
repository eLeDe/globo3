package eu.mighty.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class AbstractAssets {

	
	public abstract TextureRegion getAsset(int asset);
	
	public abstract Animation getAnimation(int anim);
	
	public abstract TextureRegion getAnimation(int anim, float time);
	
	public abstract Rectangle getPolygonon(int assetType, float time);
	
	public abstract void load();
	
	
	//Common methods
	public static Texture loadTexture(String file)
	{
		return new Texture(Gdx.files.internal(file));
	}
	
}



