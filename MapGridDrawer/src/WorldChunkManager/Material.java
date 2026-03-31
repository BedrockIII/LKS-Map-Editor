package WorldChunkManager;

import java.awt.Color;

public class Material 
{
	String name = "Material_00";
	String texture = "cmn_02_m";
	boolean grassyWalls = false;
	int bottomHeight = -1; // where is the bottom of the wall
	int topHeight = 0; //where is the top of the wall
	Color displayColor = new Color(255,0,0);
	public Material(String name)
	{
		this.name = name;
	}
	public Material(Material sELECTEDMATERIAL) 
	{
		name = sELECTEDMATERIAL.getName();
		grassyWalls = sELECTEDMATERIAL.hasGrass();
		bottomHeight = sELECTEDMATERIAL.getBottomHeight();
		topHeight = sELECTEDMATERIAL.getTopHeight();
	}
	public boolean hasGrass() 
	{
		return grassyWalls;
	}
	public String getName() 
	{
		return name;
	}
	public int getBottomHeight() 
	{
		return bottomHeight;
	}
	public int getTopHeight() 
	{
		return topHeight;
	}
	public String toString()
	{
		
		return name;
	}
	public void setName(String name2) 
	{
		name = name2;
	}
	public void setTopHeight(int value) 
	{
		topHeight = value;
	}
	public void setGrass(boolean selected) 
	{
		grassyWalls = selected;
	}
	public void setBottomHeight(int value) 
	{
		bottomHeight = value;
	}
	public String getTexture() 
	{
		if (bottomHeight==-1) texture = "cmn_05_m";
		return texture;
	}
	public Color getColor() 
	{
		return displayColor;
	}
	public void setColor(Color newC) 
	{
		displayColor = newC;
	}
	public boolean hasShore() 
	{
		return bottomHeight==-1;
	}
}
