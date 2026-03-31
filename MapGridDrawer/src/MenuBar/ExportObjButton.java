package MenuBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import GUI.GridHandler;
import GUI.WallPoint;
import WorldChunkManager.GroundGroup;
import WorldChunkManager.Shore;
import WorldChunkManager.ShoreGroup;
import WorldChunkManager.Vertex;
import WorldChunkManager.Wall;
import WorldChunkManager.WallDefinitions;
import WorldChunkManager.WallGroup;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.FloatTuples;
import de.javagl.obj.Mtl;
import de.javagl.obj.MtlWriter;
import de.javagl.obj.Mtls;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjUtils;
import de.javagl.obj.ObjWriter;
import de.javagl.obj.Objs;

public class ExportObjButton extends JButton
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Obj obj = Objs.create();
	//ArrayList<String> verts = new ArrayList<String>();
	//ArrayList<String> normals = new ArrayList<String>();
	//ArrayList<String> uvMaps = new ArrayList<String>();
	//ArrayList<String> faces = new ArrayList<String>();
	ArrayList<WallPoint> points = null;
	ArrayList<WallPoint> usedPoints = new ArrayList<WallPoint>();
	public static int startOfShoreCoords = -1;
	private ShoreGroup shoreObjects = null;
	private WallGroup wallObjects = null;
	private GroundGroup GroundVerts = null;
	private ArrayList<ShoreGroup> shoreObjectGroups = new ArrayList<ShoreGroup>();
	private ArrayList<WallGroup> wallObjectGroups = new ArrayList<WallGroup>();
	private ArrayList<WallGroup> coastObjectGroups = new ArrayList<WallGroup>();
	private ArrayList<GroundGroup> groundVertexGroups = new ArrayList<GroundGroup>();
	@SuppressWarnings("resource")
	public ExportObjButton()
	{
		addActionListener(e -> 
		{
		    JFileChooser filePicker = new JFileChooser();
		    filePicker.setSelectedFile(new File("ExportedGrid.obj"));
		    int saveCode = filePicker.showSaveDialog(this);
		    if(saveCode == JFileChooser.APPROVE_OPTION)
		    {
		    	File file = filePicker.getSelectedFile();
		    	try 
		    	{
		    		toObj(GridHandler.points);
		    		OutputStream outputStream = new FileOutputStream(file);
		    		ObjUtils.triangulate(obj);
					ObjWriter.write(obj,  outputStream);
					//Map<String, Obj> objects = ObjSplitting.splitByGroups(obj);
					//Collection<Obj> objCol = objects.values();
					//Object[] objectArr =  objCol.toArray();
					//for(int i = 0; i < objectArr.length; i++)
					//{
						//ObjWriter.write((Obj)objectArr[i], outputStream);
					//}
				} catch (IOException e1) 
		    	{
					System.out.println("Failed to write obj file");
					e1.printStackTrace();
				}
		    	try 
		    	{
		    		String outputPath = file.getParent()+"/";
		    		OutputStream outputStream = new FileOutputStream(Paths.get(outputPath+file.getName().substring(0, file.getName().lastIndexOf('.'))+".mtl").toFile());
		    		MtlWriter.write(toMtl(), outputStream);
		    		
		    		try {
		    			//In jar
		    			InputStream test = ClassLoader.getSystemResourceAsStream("cmn_01.png");
		    			byte[] test2 = test.readAllBytes();
		    			System.out.println(test2.length);
		    			Files.write(Paths.get(outputPath+"cmn_01.png"), ClassLoader.getSystemResourceAsStream("cmn_01.png").readAllBytes());
						Files.write(Paths.get(outputPath+"cmn_02.png"), ClassLoader.getSystemResourceAsStream("cmn_02.png").readAllBytes());
						Files.write(Paths.get(outputPath+"cmn_05.png"), ClassLoader.getSystemResourceAsStream("cmn_05.png").readAllBytes());
		    		} catch (IOException e2) 
		    		{
		    			System.out.println("Failed to locate Texture");
		    		} catch (NullPointerException e2)
		    		{
		    			//In Eclipse
		    			Files.write(Paths.get(outputPath+"cmn_01.png"), Files.readAllBytes(new File("src/cmn_01.png").toPath()));
						Files.write(Paths.get(outputPath+"cmn_02.png"), Files.readAllBytes(new File("src/cmn_02.png").toPath()));
						Files.write(Paths.get(outputPath+"cmn_05.png"), Files.readAllBytes(new File("src/cmn_05.png").toPath()));
		    		}
		    		
					
				} catch (IOException e1) 
		    	{
					System.out.println("Failed to write file");
					e1.printStackTrace();
				}
		    	//;
		    }
		    
		});
	}
	private ArrayList<Mtl> toMtl() 
	{
		ArrayList<Mtl> materials = new ArrayList<Mtl>();
		Mtl wall = Mtls.create("cmn_02_m");
		wall.setNs(250f);
		wall.setKa(1f,1f,1f);
		wall.setKs(0f,0f,0f);
		wall.setKe(0f,0f,0f);
		wall.setNi(1.5f);
		wall.setD(1f);
		wall.setIllum(1);
		wall.setMapKd("C:/cmn_02.png");
		materials.add(wall);
		
		Mtl coast = Mtls.create("cmn_05_m");
		coast.setNs(250f);
		coast.setKa(1f,1f,1f);
		coast.setKs(0f,0f,0f);
		coast.setKe(0f,0f,0f);
		coast.setNi(1.5f);
		coast.setD(1f);
		coast.setIllum(1);
		coast.setMapKd("C:/cmn_05.png");
		materials.add(coast);
		
		Mtl ground = Mtls.create("cmn_01_m");
		ground.setNs(250f);
		ground.setKa(1f,1f,1f);
		ground.setKs(0f,0f,0f);
		ground.setKe(0f,0f,0f);
		ground.setNi(1.5f);
		ground.setD(1f);
		ground.setIllum(1);
		ground.setMapKd("C:/cmn_01.png");
		materials.add(ground);
		
		Mtl shore = Mtls.create("ripple_m__WAVESIDE");
		shore.setNs(250f);
		shore.setKa(1f,1f,1f);
		shore.setKs(0f,0f,0f);
		shore.setKe(0f,0f,0f);
		shore.setNi(1.5f);
		shore.setD(1f);
		shore.setIllum(1);
		shore.setMapKd("C:/waveside02.png");
		shore.setMapD("C:/waveside02.png");
		materials.add(shore);
		
		return materials;
	}
	private void addTextureCoords()
	{
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.250000f));
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.371094f));
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.433594f));
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.250000f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.371094f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.433594f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.250000f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.371094f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.433594f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.250000f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.371094f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.433594f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.250000f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.371094f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.433594f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.500000f));
		//Tall
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.871094f));
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.933594f));
		obj.addTexCoord(FloatTuples.create(0.000000f, 1.000000f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.871094f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.933594f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 1.000000f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.871094f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.933594f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 1.000000f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.871094f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.933594f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 1.000000f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.500000f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.871094f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.933594f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 1.000000f));
		startOfShoreCoords=obj.getNumTexCoords();
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.984f));
		obj.addTexCoord(FloatTuples.create(0.000000f, 0.344f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.984f));
		obj.addTexCoord(FloatTuples.create(0.250000f, 0.344f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.984f));
		obj.addTexCoord(FloatTuples.create(0.500000f, 0.344f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.984f));
		obj.addTexCoord(FloatTuples.create(0.750000f, 0.344f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.984f));
		obj.addTexCoord(FloatTuples.create(1.000000f, 0.344f));
		obj.addNormal(FloatTuples.create(0,1,0));
	}
	private void toObj(ArrayList<WallPoint> points1) 
	{
		obj = Objs.create();
		usedPoints = new ArrayList<WallPoint>();
		Set<WallPoint> setWithoutDuplicates = new HashSet<WallPoint>(points1);
		points = new ArrayList<WallPoint>(setWithoutDuplicates);
		addTextureCoords();
		
		groundVertexGroups = new ArrayList<GroundGroup>();
		int index = 0;
		for(int i = 0; i< points.size(); i++)
		{
			//System.out.println(Arrays.toString(points.toArray()));
			WallPoint p = points.get(i);
			ArrayList<String> group = new ArrayList<String>();
			group.add(p.getMaterial() + "_" + index);
			obj.setActiveGroupNames(group);
			if(p!=null&&usedPoints.indexOf(p)==-1)
			{
				addConnectedVertices(p, index);
				index++;
				i = -1;
			}
		}
		
		
		obj.setActiveMaterialGroupName("cmn_02_m");
		for(int i = 0; i<wallObjectGroups.size(); i++)
		{
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add("Wall_"+i);
			obj.setActiveGroupNames(groupNames);
			
			WallGroup wallObjects = wallObjectGroups.get(0);
			
			wallObjects.addVertices(obj);
			wallObjects.addFaces(obj);
		}
		obj.setActiveMaterialGroupName("cmn_05_m");
		for(int i = 0; i<coastObjectGroups.size(); i++)
		{
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add("Coast_"+i);
			obj.setActiveGroupNames(groupNames);
			
			WallGroup wallObjects = coastObjectGroups.get(0);
			
			wallObjects.addVertices(obj);
			wallObjects.addFaces(obj);
		}
		obj.setActiveMaterialGroupName("ripple_m__WAVESIDE");
		
		for(int i = 0; i<shoreObjectGroups.size(); i++)
		{
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add("Shore_"+i);
			obj.setActiveGroupNames(groupNames);
			
			ShoreGroup shoreObjects = shoreObjectGroups.get(0);
			
			shoreObjects.addVertices(obj);
			shoreObjects.addFaces(obj);
		}
		
		
		
		obj.setActiveMaterialGroupName("cmn_01_m");
		
		for(int i = 0; i<groundVertexGroups.size(); i++)
		{
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add("Ground_"+i);
			obj.setActiveGroupNames(groupNames);
			GroundGroup GroundVerts = groundVertexGroups.get(i);
			GroundVerts.addVertices(obj);
			GroundVerts.addFaces(obj);
			
		}
	}
	
	private void addConnectedVertices(WallPoint p, int objIndex) 
	{
		WallPoint point = p.getFurthestPoint();
		//obj.addNormal(FloatTuples.create(0,1,0));
		int index = 0;
		boolean isLoop = point==null;
		GroundVerts = new GroundGroup(isLoop);
		shoreObjects = new ShoreGroup(isLoop);
		wallObjects = new WallGroup(isLoop);
		
		if(isLoop)
		{
			point = p;
			while(point.nextPoint()!=p)
			{
				//Returns Ground Vertex for easier but less readable code
				OBJVertices(point, index);
				point = point.nextPoint();
				points.remove(point);
				usedPoints.add(point);
				index++;
			}
			//Adding in the last Point
			OBJVertices(point, index);
			point = point.nextPoint();
			points.remove(point);
			usedPoints.add(point);
			index++;
			
		}
		else
		{
			//Get the first point
			point = point.previousPoint().firstPoint(point);
			while(point!=null)
			{
				OBJVertices(point, index);
				points.remove(point);
				usedPoints.add(point);
				point = point.nextPoint();
				index--;
			}
		}
		groundVertexGroups.add(GroundVerts);
		shoreObjectGroups.add(shoreObjects);
		if(wallObjects.isCoast())
		{
			coastObjectGroups.add(wallObjects);
		}
		else
		{
			wallObjectGroups.add(wallObjects);
		}
	}
	private void OBJVertices(WallPoint p, int index)
	{
		Vertex[] def = WallDefinitions.getSegmentCoords(p.getMaterial());
		Vertex d = null;
		if(p!=null)
		{
			double theta = p.getAngle();
			for(int i =0; i <4; i++)
			{
				wallObjects.add(new Wall(p));
			}
			GroundVerts.add(getVertexCoords(p, theta, def[3]));
			System.out.println(GroundVerts.size());
			d = WallDefinitions.pointNormal(def);
			obj.addNormal(FloatTuples.create((float)d.getX(), (float)d.getY(), (float)d.getZ()));
			if(p.getMaterial().hasShore())
			{
				shoreObjects.add( new Shore(
						getVertexCoords(p, theta, new Vertex(0.261780,0,-1.98)),
						getVertexCoords(p, theta, new Vertex(-.95,0,-1.98))
						));
			}
		}
	}
	
	
	private FloatTuple getVertexCoords(WallPoint p, double theta, Vertex v)
	{
		float x = (float) ((p.x + Math.cos(theta)*v.getX()-32)/100.0);
		float y = (float) (v.getZ()/100.0);
		float z = (float) ((-1*p.y + Math.sin(theta)*v.getX()+32)/-100.0);
		return FloatTuples.create(x, y, z);
	}
}
