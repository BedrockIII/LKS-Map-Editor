package WorldChunkManager;

import GUI.WallPoint;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.FloatTuples;

public class Wall 
{
	FloatTuple bottom = null;
	FloatTuple definition = null;
	FloatTuple middle = null;
	FloatTuple top = null;
	public Wall(WallPoint p)
	{
		double theta = p.getAngle();
		Vertex[] def = WallDefinitions.getSegmentCoords(p.getMaterial());
		bottom = getVertexCoords(p, theta, def[0]);
		definition = getVertexCoords(p, theta, def[1]);
		middle = getVertexCoords(p, theta, def[2]);
		top = getVertexCoords(p, theta, def[3]);
	}
	public FloatTuple getBottomVertex()
	{
		return bottom;
	}
	public FloatTuple getDefinitonVertex()
	{
		return definition;
	}
	public FloatTuple getMiddleVertex()
	{
		return middle;
	}
	public FloatTuple getTopVertex()
	{
		return top;
	}
	public int getHeight()
	{
		return (int)(top.getY() - bottom.getY());
	}
	private FloatTuple getVertexCoords(WallPoint p, double theta, Vertex v)
	{
		float x = (float) ((p.x + Math.cos(theta)*v.getX()-32)/100.0);
		float y = (float) (v.getZ()/100.0);
		float z = (float) ((-1*p.y + Math.sin(theta)*v.getX()+32)/-100.0);
		return FloatTuples.create(x, y, z);
	}
	public boolean isCoast() 
	{
		return bottom.getY()<0;
	}
}
