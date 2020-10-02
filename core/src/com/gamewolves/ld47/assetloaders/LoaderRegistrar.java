package com.gamewolves.ld47.assetloaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.gamewolves.ld47.assetloaders.loaders.TextLoader;
import com.gamewolves.ld47.assetloaders.loaders.TextureAtlasLoader;
import com.gamewolves.ld47.assetloaders.loaders.TilemapLoader;
import com.gamewolves.ld47.assetloaders.loaders.XMLFileLoader;
import com.gamewolves.ld47.assetloaders.types.Text;
import com.gamewolves.ld47.assetloaders.types.TextureAtlas;
import com.gamewolves.ld47.assetloaders.types.Tilemap;
import com.gamewolves.ld47.assetloaders.types.XMLFile;

public class LoaderRegistrar
{
	public static void registerLoaders(AssetManager assetManager)
	{
		assetManager.setLoader(
				Text.class,
				new TextLoader(
						new InternalFileHandleResolver()
				)
		);

		assetManager.setLoader(
				XMLFile.class,
				new XMLFileLoader(
						new InternalFileHandleResolver()
				)
		);

		assetManager.setLoader(
				TextureAtlas.class,
				new TextureAtlasLoader(
						new InternalFileHandleResolver()
				)
		);

		assetManager.setLoader(
				Tilemap.class,
				new TilemapLoader(
						new InternalFileHandleResolver()
				)
		);
	}
}
