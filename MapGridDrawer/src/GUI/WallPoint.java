package GUI;

import java.awt.Point;

import WorldChunkManager.Material;

public class WallPoint extends Point
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Material material = null;
	Line L1 = null;
	Line L2 = null;
	boolean inverseAngles;
	public WallPoint(Point clickPos, Material material) 
	{
		super(clickPos.x, clickPos.y);
		this.material = material;
	}
	public boolean sameCoords(Point p) {
		return p.x==x&&p.y==y;
	}
	public Material getMaterial() 
	{
		return material;
	}
	public void setMaterial(Material m) 
	{
		material = m;
	}
	public void addReferenceLine(Line line) 
	{
		if(L1 == null)
		{
			System.out.println("First Line for Point");
			L1 = line;
		}
		else if(L2 == null)
		{
			System.out.println("Second Line for Point");
			L2 = line;
		}
		else
		{
			System.out.println("Too many lines at 1 point");
		}
	}
	public void addReferenceLine(Line line, int index) 
	{
		if(L1 == null&&index==0)
		{
			System.out.println("First Line for Point");
			L1 = line;
		}
		else if(L2 == null&&index==1)
		{
			System.out.println("Second Line for Point");
			L2 = line;
		}
		else
		{
			System.out.println("Too many lines at 1 point, or Line " + (index+1) +" was already set");
		}
	}
	private double getBearing(int[] vector, boolean inverseVector)
	{//get the angle counterclockwise between vector and x+
		//You know it's bad if the first release has comments
		
		
		
		int x = vector[0];
		int y = vector[1];
		if(inverseVector)
		{
			x *= -1;
			y *= -1;
		}
		//System.out.println("x: "+x);
		//System.out.println("y: "+y);
		
		double theta = 0;
		
		double magnitude = Math.sqrt(x*x+y*y);
		double cosTheta = x /magnitude;
		theta = Math.acos(cosTheta);
		//System.out.println("Dot Prod: "+theta);
		if(y<0)
		{
			theta = 2*Math.PI - theta;
		}
		return theta;
	}
	public double getAngle()
	{
		double line1Bearing = 0;
		double line2Bearing = 0;
		if(L1==null&&L2==null)
		{
			//System.out.println("aba");
			return -999.0;
			
		}
		else if(L1==null)
		{
			if(x == 0)
			{
				return Math.PI/2;
			}
			else if(y==0)
			{
				return 0;
			}
			if(x == 63)
			{
				return 3*Math.PI/2;
			}
			else if(y==63)
			{
				return Math.PI;
			}
			//If Starting Line
			line2Bearing = getBearing(L2.getL2Vector(),false);
			line1Bearing = getBearing(L2.getL2Vector(),true);
		}
		else if(L2==null)
		{
			if(x == 0)
			{
				return 3*Math.PI/2;
			}
			else if(y==0)
			{
				return Math.PI;
			}
			if(x == 63)
			{
				return Math.PI/2;
			}
			else if(y==63)
			{
				return 0;
			}
			//If Ending Line
			System.out.println("aba");
			line1Bearing = getBearing(L1.getL1Vector(),false);
			line2Bearing = getBearing(L1.getL1Vector(),true);
			//return -999;
		}
		else
		{
			line1Bearing = getBearing(L1.getL1Vector(),false);
			line2Bearing = getBearing(L2.getL2Vector(),false);
		}
		//otherwise get average
		
		//System.out.println("L1 theta: " + line1Bearing);
		//System.out.println("L2 theta: " + line2Bearing);
		double average = ((line2Bearing+line1Bearing)/2);
		if(line1Bearing<line2Bearing) average = (average+Math.PI);
		//System.out.println(average%(2*Math.PI));
		if(inverseAngles)average+=Math.PI;
		average %= Math.PI*2;
		return average;
	}
	public WallPoint nextPoint() 
	{
		if (L2 == null) return null;
		return L2.getEndPoint();
	}
	public WallPoint getFurthestPoint(WallPoint startingPoint) 
	{
		if (L2 == null) return this;
		if (this.equals(startingPoint)) return null;
		return L2.getEndPoint().getFurthestPoint(startingPoint, 100);
	}
	public WallPoint getFurthestPoint(WallPoint startingPoint, int buffer) 
	{
		if (L2 == null||buffer<0) return this;
		if (this.equals(startingPoint)) return null;
		return L2.getEndPoint().getFurthestPoint(startingPoint, buffer--);
	}
	public WallPoint getFurthestPoint() 
	{
		if (L2 == null) return this;
		return L2.getEndPoint().getFurthestPoint(this);
	}
	public WallPoint previousPoint() 
	{
		if (L1 == null) return null;
		return L1.getStartPoint();
	}
	public WallPoint firstPoint(WallPoint startingPoint) 
	{
		if (L1 == null) return this;
		if (this.equals(startingPoint)) return null;
		//if(startingPoint.getMaterial()!=material) return this;
		return L1.getStartPoint().firstPoint(startingPoint);
	}
	
}
