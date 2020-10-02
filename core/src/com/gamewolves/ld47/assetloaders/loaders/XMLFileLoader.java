package com.gamewolves.ld47.assetloaders.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.assetloaders.types.XMLFile;

public class XMLFileLoader extends AsynchronousAssetLoader<XMLFile, XMLFileLoader.XMLFileParameter>
{
	XMLFile xmlFile;

	public XMLFileLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, XMLFileParameter parameter)
	{
		this.xmlFile = null;
		this.xmlFile = new XMLFile(file);
	}

	@Override
	public XMLFile loadSync(AssetManager manager, String fileName, FileHandle file, XMLFileParameter parameter)
	{
		XMLFile xmlFile = this.xmlFile;
		this.xmlFile = null;

		return xmlFile;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, XMLFileParameter parameter)
	{
		return null;
	}

	public static class XMLFileParameter extends AssetLoaderParameters<XMLFile>
	{

	}
}
