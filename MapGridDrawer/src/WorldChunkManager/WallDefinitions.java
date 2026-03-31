package WorldChunkManager;

@SuppressWarnings("unused")
public class WallDefinitions 
{//return depth and height values
	public final int GroundWallModel1 = 0;
	public final int GroundWallModel2 = 1;
	public final int ShoreWallModel1 = 2;
	public final int ShoreWallModel2 = 3;
	public final int WaterWallModel = 4;
	private static int height;
	public static Vertex[] getSegmentCoords(Material mat)
	{
		int topHeight = mat.getTopHeight();
		int bottomHeight = mat.getBottomHeight();
		boolean includeGrass = mat.hasGrass();
		Vertex[] ret = null;
		height = topHeight-bottomHeight;//if 1, use the shorter UV cords
		
		ret = new Vertex[] {new Vertex(0.261780,0,2*bottomHeight),new Vertex(0,0,2*topHeight-.7),new Vertex(0.261780,0,2*topHeight-.1),new Vertex(0.902954,0,2*topHeight)};
		
		//if(bottomHeight == -1)
		//{
			//ret = new Vertex[] {new Vertex(0.261780,0,2*bottomHeight),new Vertex(0,0,2*topHeight-.7),
					//new Vertex(0.261780,0,2*topHeight-.1),new Vertex(0.902954,0,2*topHeight), 
					//new Vertex(0.261780,0,2*bottomHeight+.02),new Vertex(-.95,0,2*bottomHeight+.02)};
		//}
		
		//Useless
		int index = 0;
		if(height==1)
		{ //Bottom, Outermost and definition, End of grass, Top
			ret[0].setUV(.25, .25*index%4);
			ret[1].setUV(.371, .25*index%4);
			ret[2].setUV(.434, .25*index%4);
			ret[3].setUV(.5, .25*index%4);
		}
		else 
		{
			ret[0].setUV(.5, .25*index%4);
			ret[1].setUV(.871, .25*index%4);
			ret[2].setUV(.934, .25*index%4);
			ret[3].setUV(1, .25*index%4);
		}
		
		
		
		
		
		
		//if (bottomHeight == -1)
		//{
			//ret = addShore(ret);
		//}
		if (includeGrass) ret = addGrass(ret);
		return ret;
	}
	private static Vertex[] addGrass(Vertex[] wallData) 
	{
		// TODO Auto-generated method stub
		return wallData;
	}
	private static Vertex[] addShore(Vertex[] wallData) 
	{
		// TODO Auto-generated method stub
		return wallData;
	}
	public static Vertex pointNormal(Vertex[] wallData)
	{
		return new Vertex(0,1,0);
	}
}
