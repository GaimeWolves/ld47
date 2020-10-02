package com.gamewolves.ld47.assetloaders.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.assetloaders.types.Text;
import com.gamewolves.ld47.assetloaders.types.Tilemap;

public class TilemapLoader extends AsynchronousAssetLoader<Tilemap, TilemapLoader.TilemapParameter>
{
	Tilemap tilemap;

	public TilemapLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, TilemapParameter parameter)
	{
		this.tilemap = null;
		Text map = new Text(file);
		Texture mapTex = manager.get(file.pathWithoutExtension().concat(".png"), Texture.class);
		this.tilemap = new Tilemap(map, mapTex);
	}

	@Override
	public Tilemap loadSync(AssetManager manager, String fileName, FileHandle file, TilemapParameter parameter)
	{
		Tilemap tilemap = this.tilemap;
		this.tilemap = null;

		return tilemap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TilemapParameter parameter)
	{
		Array<AssetDescriptor> deps = new Array<>();

		// Example: menu.png and menu.pac
		deps.add(new AssetDescriptor<>(file.pathWithoutExtension().concat(".png"), Texture.class));

		return deps;
	}

	public static class TilemapParameter extends AssetLoaderParameters<Tilemap>
	{

	}
}
