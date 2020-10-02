package com.gamewolves.ld47.assetloaders.types;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Tilemap
{
	private Texture mapTexture = null;
	private int width = 0, height = 0;
	private int margin = 0;

	public Tilemap() {}

	public Tilemap(Text mapFile, Texture mapTexture)
	{
		this.mapTexture = mapTexture;

		String[] lines = mapFile.getString().split("\n");
		width = Integer.parseInt(lines[0]);
		height = Integer.parseInt(lines[1]);
		margin = Integer.parseInt(lines[2]);
	}

	public TextureRegion get(Vector2 pos)
	{
		return get((int)pos.x, (int)pos.y);
	}

	public TextureRegion get(int x, int y)
	{
		float offsetX = x * (width + margin * 2);
		float offsetY = y * (height + margin * 2);
		return new TextureRegion(mapTexture, (int)offsetX, (int)offsetY, width, height);
	}
}
