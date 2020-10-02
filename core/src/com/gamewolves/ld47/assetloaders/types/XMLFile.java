package com.gamewolves.ld47.assetloaders.types;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.gamewolves.ld47.Main;

public class XMLFile
{
	private XmlReader.Element root = null;

	public XMLFile()
	{

	}

	public XMLFile(byte[] data)
	{
		this.root = Main.get().xmlReader.parse(new String(data));
	}

	public XMLFile(String string)
	{
		this.root = Main.get().xmlReader.parse(string);
	}

	public XMLFile(FileHandle file)
	{
		this.root = Main.get().xmlReader.parse(file);
	}

	public XMLFile(XMLFile xmlFile)
	{
		this.root = xmlFile.getRoot();
	}

	public XmlReader.Element getRoot()
	{
		return root;
	}
}
