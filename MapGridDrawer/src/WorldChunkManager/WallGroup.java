package WorldChunkManager;

import java.util.ArrayList;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjFaces;

public class WallGroup 
{
	boolean isLooped = false;
	int VertexStartPos = -1;
	int startOfWallCoords = -1;
	ArrayList<Wall> Walls = new ArrayList<Wall>();
	public WallGroup(boolean isLooped)
	{
		this.isLooped = isLooped;
		startOfWallCoords = 0;
	}
	public void add(Wall w)
	{
		Walls.add(w);
	}
	public void addVertices(Obj obj) 
	{
		VertexStartPos = obj.getNumVertices();
		for(int j = 0; j<Walls.size(); j++)
		{
			obj.addVertex(Walls.get(j).getBottomVertex());
			obj.addVertex(Walls.get(j).getDefinitonVertex());
			obj.addVertex(Walls.get(j).getMiddleVertex());
			obj.addVertex(Walls.get(j).getTopVertex());
		}
	}
	public void addFaces(Obj obj) 
	{
		for(int i = 0; i<Walls.size()-1; i++)
		{
			int LeftVertexIndex = VertexStartPos + i*4; 
			int RightVertexIndex = LeftVertexIndex + 4;
			int LeftTextureIndex = startOfWallCoords + (i * 4) % 16;
			int RightTextureIndex = LeftTextureIndex + 4;
			if(Walls.get(i).getHeight()>2)
			{
				LeftTextureIndex += 16;
				RightTextureIndex += 16;
			}
			addFaces(obj, LeftVertexIndex, RightVertexIndex, LeftTextureIndex, RightTextureIndex);
			if(isLooped&&i==Walls.size()-2)
			{
				addFaces(obj, RightVertexIndex, VertexStartPos, startOfWallCoords + ((i + 1) * 4) % 16, startOfWallCoords + ((i + 1) * 4) % 16 + 4);
			}
		}
		
	}
	private void addFaces(Obj obj, int LeftVertexIndex, int RightVertexIndex, int LeftTextureIndex, int RightTextureIndex)
	{
		LeftTextureIndex--;
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex, RightVertexIndex, LeftVertexIndex+1},
				new int[] {1+LeftTextureIndex, 5+LeftTextureIndex, 2+LeftTextureIndex},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex+1, RightVertexIndex, RightVertexIndex+1},
				new int[] {2+LeftTextureIndex, 5+LeftTextureIndex, 6+LeftTextureIndex},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex+1, RightVertexIndex+1, LeftVertexIndex+2},
				new int[] {2+LeftTextureIndex, 6+LeftTextureIndex, 3+LeftTextureIndex},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex+2, RightVertexIndex+1, RightVertexIndex+2},
				new int[] {3+LeftTextureIndex, 6+LeftTextureIndex, 7+LeftTextureIndex},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex+2, RightVertexIndex+2, LeftVertexIndex+3},
				new int[] {3+LeftTextureIndex, 7+LeftTextureIndex, 4+LeftTextureIndex},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex+3, RightVertexIndex+2, RightVertexIndex+3},
				new int[] {4+LeftTextureIndex, 7+LeftTextureIndex, 8+LeftTextureIndex},
				new int[] {0,0,0})
				);
	}
	public boolean isCoast()
	{
		if(Walls.size()<1)
		{
			return false;
		}
		return Walls.get(0).isCoast();
	}
}
