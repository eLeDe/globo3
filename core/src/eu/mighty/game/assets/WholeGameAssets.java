package eu.mighty.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class WholeGameAssets extends AbstractAssets {

	static Texture allThingsSheet;

	public static final int DOING_NOTHING = 0;
	public static final int PLAYER_GREEN_GLOBO_IDLE = 1000;
	
	//Player animations
	TextureRegion greenGloboIdle1;

	@Override
	public TextureRegion getAsset(int asset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getAnimation(int anim) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

	@Override
	public TextureRegion getAnimation(int anim, float time) {
		
		TextureRegion texture;
		
		switch(anim)
		{
		case PLAYER_GREEN_GLOBO_IDLE:
			return new TextureRegion(this.greenGloboIdle1);
		}

		return null;
	}

	@Override
	public Rectangle getPolygonon(int assetType, float time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load() {
		//Load the sheet with the sprites
		
		allThingsSheet = loadTexture("playerImages/globo_green.png");
		allThingsSheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
		//Load level sheet
		
		
		//Load Textures here!
		
		//Green globo iddle
		this.greenGloboIdle1 = new TextureRegion(allThingsSheet, 0, 0, 40, 60);
	}
}
