package WorldChunkManager;

import java.util.ArrayList;

import MenuBar.ExportObjButton;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFaces;

public class ShoreGroup 
{
	boolean isLooped = false;
	int VertexStartPos = -1;
	int startOfShoreCoords = -1;
	ArrayList<Shore> Shores = new ArrayList<Shore>();
	public ShoreGroup(boolean isLooped)
	{
		this.isLooped = isLooped;
		startOfShoreCoords = ExportObjButton.startOfShoreCoords;
	}
	public void add(Shore s)
	{
		Shores.add(s);
	}
	public void addVertices(Obj obj) 
	{
		VertexStartPos = obj.getNumVertices();
		for(int j = 0; j<Shores.size(); j++)
		{
			obj.addVertex(Shores.get(j).getInteriorVertex());
			obj.addVertex(Shores.get(j).getExteriorVertex());
		}
	}
	public void addFaces(Obj obj) 
	{
		for(int i = 0; i<Shores.size()-1; i++)
		{
			int LeftVertexIndex = VertexStartPos + i*2; 
			int RightVertexIndex = LeftVertexIndex + 2;
			int LeftTextureIndex = startOfShoreCoords + (i * 2) % 8;
			int RightTextureIndex = LeftTextureIndex + 2;
			addFaces(obj, LeftVertexIndex, RightVertexIndex, LeftTextureIndex, RightTextureIndex);
			if(isLooped&&i==Shores.size()-2)
			{
				addFaces(obj, RightVertexIndex, VertexStartPos, startOfShoreCoords + ((i + 1) * 2) % 8, startOfShoreCoords + ((i + 1) * 2) % 8 + 2);
			}
		}
		
	}
	private void addFaces(Obj obj, int LeftVertexIndex, int RightVertexIndex, int LeftTextureIndex, int RightTextureIndex)
	{
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex, LeftVertexIndex + 1, RightVertexIndex + 1},
				new int[] {LeftTextureIndex, LeftTextureIndex + 1, RightTextureIndex + 1},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {LeftVertexIndex, RightVertexIndex + 1, RightVertexIndex},
				new int[] {LeftTextureIndex, RightTextureIndex + 1, RightTextureIndex},
				new int[] {0,0,0})
				);
	}
}
