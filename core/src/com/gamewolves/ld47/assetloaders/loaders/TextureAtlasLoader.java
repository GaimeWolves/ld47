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
import com.gamewolves.ld47.assetloaders.types.TextureAtlas;

public class TextureAtlasLoader extends AsynchronousAssetLoader<TextureAtlas, TextureAtlasLoader.TextureAtlasParameter>
{
	TextureAtlas textureAtlas;

	public TextureAtlasLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureAtlasParameter parameter)
	{
		this.textureAtlas = null;
		Text pack = new Text(file);
		Texture map = manager.get(file.pathWithoutExtension().concat(".png"), Texture.class);
		this.textureAtlas = new TextureAtlas(pack, map);
	}

	@Override
	public TextureAtlas loadSync(AssetManager manager, String fileName, FileHandle file, TextureAtlasParameter parameter)
	{
		TextureAtlas textureAtlas = this.textureAtlas;
		this.textureAtlas = null;

		return textureAtlas;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureAtlasParameter parameter)
	{
		Array<AssetDescriptor> deps = new Array<>();

		// Example: menu.png and menu.pac
		deps.add(new AssetDescriptor<>(file.pathWithoutExtension().concat(".png"), Texture.class));

		return deps;
	}

	public static class TextureAtlasParameter extends AssetLoaderParameters<TextureAtlas>
	{

	}
}
