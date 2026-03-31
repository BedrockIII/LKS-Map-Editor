package WorldChunkManager;

import de.javagl.obj.FloatTuple;

public class Shore 
{
	FloatTuple interior = null;
	FloatTuple exterior = null;
	public Shore(FloatTuple i, FloatTuple e)
	{
		interior = i;
		exterior = e;
	}
	public FloatTuple getInteriorVertex()
	{
		return interior;
	}
	public FloatTuple getExteriorVertex()
	{
		return exterior;
	}
}
