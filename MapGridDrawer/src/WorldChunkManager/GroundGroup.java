package WorldChunkManager;

import java.util.ArrayList;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.FloatTuples;
import de.javagl.obj.Obj;

public class GroundGroup 
{
	boolean isLooped = false;
	int VertexStartPos = -1;
	ArrayList<FloatTuple> Verts = new ArrayList<FloatTuple>();
	ArrayList<Integer> GroundVertexIndicies = new ArrayList<Integer>();
	public GroundGroup(boolean isLooped)
	{
		this.isLooped = isLooped;
	}
	public void add(FloatTuple def)
	{
		Verts.add(def);
	}
	public void addVertices(Obj obj) 
	{
		VertexStartPos = obj.getNumVertices();
		for(int j = 0; j<Verts.size(); j++)
		{
			obj.addVertex(Verts.get(j));
			GroundVertexIndicies.add(obj.getNumVertices());
		}
	}
	public void addFaces(Obj obj) 
	{
		int[] verticies = new int[GroundVertexIndicies.size()];
		int[] textureMappings = new int[GroundVertexIndicies.size()];
		int[] normals = new int[GroundVertexIndicies.size()];
		for(int j = 0; j<GroundVertexIndicies.size(); j++)
		{
			verticies[j]=GroundVertexIndicies.get(j)-1;
			textureMappings[j] = getTextureMappings(obj, GroundVertexIndicies.get(j))-1;
			normals[j] = 1;
		}
		if(groundFacesUp(obj, GroundVertexIndicies))
		{
			obj.addFace(verticies, textureMappings, normals);
		}
		
	}
	private int getTextureMappings(Obj obj, Integer integer) 
	{
		FloatTuple vert = obj.getVertex(integer.intValue()-1);
		float u = ((vert.getX()*100+32)/64)*4;
		float v = (((vert.getZ()*100-32)/64)*-1)*4-3;
		obj.addTexCoord(FloatTuples.create(u,v));
		return obj.getNumTexCoords();
	}
	private boolean groundFacesUp(Obj obj, ArrayList<Integer> groundVerts) 
	{
		if(groundVerts.size()<3)
		{
			return false;
		}
		//get Vectors
		FloatTuple A = obj.getVertex(groundVerts.get(1));//Middle
		FloatTuple B = obj.getVertex(groundVerts.get(0));//Start
		FloatTuple C = obj.getVertex(groundVerts.get(2));//End
		
		//Crossproduct but only calculating upness of vector
		float[] AB = new float[] {B.getX() - A.getX(), B.getY() - A.getY(), B.getZ() - A.getZ()};
		float[] AC = new float[] {C.getX() - A.getX(), C.getY() - A.getY(), C.getZ() - A.getZ()};
		float yMag = (AB[0]*AC[2]-AC[0]*AB[2]);
		return yMag>=0;
	}
	public int size()
	{
		return Verts.size();
	}
}
