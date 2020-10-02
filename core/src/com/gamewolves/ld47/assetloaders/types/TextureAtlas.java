package com.gamewolves.ld47.assetloaders.types;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class TextureAtlas
{
	private Texture mapTexture;
	private HashMap<String, TextureRegion> regions;

	public TextureAtlas()
	{
		mapTexture = null;
		regions = new HashMap<>();
	}

	public TextureAtlas(Text packFile, Texture mapTexture)
	{
		this.mapTexture = mapTexture;
		regions = new HashMap<>();

		String[] lines = packFile.getString().split("\n");

		for (String line : lines)
		{
			String[] data = line.split(",");

			String name = data[0];
			int x = Integer.parseInt(data[1]);
			int y = Integer.parseInt(data[2]);
			int w = Integer.parseInt(data[3]);
			int h = Integer.parseInt(data[4]);

			TextureRegion region = new TextureRegion(mapTexture, x, y, w, h);

			regions.put(name, region);
		}
	}

	public TextureRegion get(String name)
	{
		return regions.get(name);
	}
}
