package MenuBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;

import GUI.GridHandler;
import GUI.WallPoint;
import WorldChunkManager.Vertex;
import WorldChunkManager.WallDefinitions;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.FloatTuples;
import de.javagl.obj.Mtl;
import de.javagl.obj.MtlWriter;
import de.javagl.obj.Mtls;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFaces;
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
	ArrayList<ArrayList<Integer>> GroundVertGroups = new ArrayList<ArrayList<Integer>>();
	int vCount = 1;
	int tCount = 0;
	int nCount = 0;
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
		
		return materials;
	}
	private void toObj(ArrayList<WallPoint> points1) 
	{
		obj = Objs.create();
		vCount = 1;
		tCount = 0;
		nCount = 0;
		usedPoints = new ArrayList<WallPoint>();
		Set<WallPoint> setWithoutDuplicates = new HashSet<WallPoint>(points1);
		points = new ArrayList<WallPoint>(setWithoutDuplicates);
		//Short
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
		
		obj.addNormal(FloatTuples.create(0,1,0));
		
		GroundVertGroups = new ArrayList<ArrayList<Integer>>();
		int index = 0;
		for(int i = 0; i< points.size(); i++)
		{
			System.out.println(Arrays.toString(points.toArray()));
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
		obj.setActiveMaterialGroupName("cmn_01_m");
		
		for(int i = 0; i<GroundVertGroups.size(); i++)
		{
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add("Ground_"+i);
			obj.setActiveGroupNames(groupNames);
			
			//create real int[] array for verts
			//createtexture array of dynamic points
			//create normal array of values 1
			ArrayList<Integer> GroundVerts = GroundVertGroups.get(i);
			int[] verticies = new int[GroundVerts.size()];
			int[] textureMappings = new int[GroundVerts.size()];
			int[] normals = new int[GroundVerts.size()];
			for(int j = 0; j<GroundVerts.size(); j++)
			{
				verticies[j]=GroundVerts.get(j)-1;
				textureMappings[j] = getTextureMappings(GroundVerts.get(j))-1;
				normals[j] = 1;
			}
			if(groundFacesUp(GroundVerts))
			{
				obj.addFace(verticies, textureMappings, normals);
			}
			
		}
	}
	private boolean groundFacesUp(ArrayList<Integer> groundVerts) 
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
	private int getTextureMappings(Integer integer) 
	{
		FloatTuple vert = obj.getVertex(integer.intValue()-1);
		float u = ((vert.getX()*100+32)/64)*4;
		float v = (((vert.getZ()*100-32)/64)*-1)*4-3;
		obj.addTexCoord(FloatTuples.create(u,v));
		return obj.getNumTexCoords();
	}
	private void addConnectedVertices(WallPoint p, int objIndex) 
	{
		ArrayList<Integer> GroundVerts = new ArrayList<Integer>();
		WallPoint point = p.getFurthestPoint();
		//obj.addNormal(FloatTuples.create(0,1,0));
		int index = 0;
		boolean isLoop = point==null;
		if(isLoop)
		{
			point = p;
			while(point.nextPoint()!=p)
			{
				//Returns Ground Vertex for easier but less readable code
				GroundVerts.add(OBJVertices(point, index));
				point = point.nextPoint();
				points.remove(point);
				usedPoints.add(point);
				index++;
			}
			//Adding in the last Point
			GroundVerts.add(OBJVertices(point, index));
			point = point.nextPoint();
			points.remove(point);
			usedPoints.add(point);
			index++;
			GroundVertGroups.add(GroundVerts);
		}
		else
		{
			point = point.previousPoint().firstPoint(point);
			while(point!=null)
			{
				GroundVerts.add(OBJVertices(point, index));
				points.remove(point);
				usedPoints.add(point);
				point = point.nextPoint();
				index--;
			}
			GroundVertGroups.add(GroundVerts);
		}
		//points.remove(p);
		//usedPoints.add(p);
		
		//String ret = "o " + p.getMaterial() + "_" + objIndex +"\n";
		//
		//System.out.println(ret);
		//verts = new ArrayList<String>();
		//normals = new ArrayList<String>();
		
		//faces = new ArrayList<String>();
		
		
		//OBJVertices(p, index);
		int iMod = 0;
		for(int i = 0; i<obj.getNumVertices()/4-1; i++)
		{
			if(obj.getVertex(i*4).getY()==-0.02)			
			{
				obj.setActiveMaterialGroupName("cmn_05_m");
			}
			else 
			{
				obj.setActiveMaterialGroupName("cmn_02_m");
			}
			int heightTextOffset =0;
			if(Math.abs(obj.getVertex(i*4).getY()
					-obj.getVertex(i*4+3).getY())>.019)			{
				heightTextOffset=20;
			}
			int j = i*4+vCount;
			obj.setActiveMaterialGroupName("cmn_02_m");
			addChunkFaces(j,j+4, heightTextOffset+i%4*4);
			iMod = i%4;
		}//"/" + (i%4*4+j+tCount) + "/"
		if(obj.getVertex(iMod*4).getY()==-0.02)			
		{
			obj.setActiveMaterialGroupName("cmn_05_m");
		}
		else 
		{
			obj.setActiveMaterialGroupName("cmn_02_m");
		}
		int heightTextOffset =0;
		if(Math.abs(obj.getVertex(iMod*4).getY()
				-obj.getVertex(iMod*4+3).getY())>.019)
		{
			heightTextOffset=20;
		}
		int maxFace = obj.getNumVertices()-3;
		iMod = (iMod+1)%4;
		if(isLoop) addChunkFaces(maxFace, vCount, heightTextOffset+(iMod)*4);
		
		
		//System.out.println(ret);
		vCount = obj.getNumVertices()+1;
		nCount += obj.getNumNormals();
	}
	private void addChunkFaces(int startVertexOffset, int endVertexOffset, int textureOffset)
	{
		startVertexOffset --;
		endVertexOffset --;
		textureOffset --; // Quick solution to an off by one mistake
		obj.addFace(ObjFaces.create(
				new int[] {startVertexOffset, endVertexOffset, startVertexOffset+1},
				new int[] {1+textureOffset, 5+textureOffset, 2+textureOffset},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {startVertexOffset+1, endVertexOffset, endVertexOffset+1},
				new int[] {2+textureOffset, 5+textureOffset, 6+textureOffset},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {startVertexOffset+1, endVertexOffset+1, startVertexOffset+2},
				new int[] {2+textureOffset, 6+textureOffset, 3+textureOffset},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {startVertexOffset+2, endVertexOffset+1, endVertexOffset+2},
				new int[] {3+textureOffset, 6+textureOffset, 7+textureOffset},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {startVertexOffset+2, endVertexOffset+2, startVertexOffset+3},
				new int[] {3+textureOffset, 7+textureOffset, 4+textureOffset},
				new int[] {0,0,0})
				);
		obj.addFace(ObjFaces.create(
				new int[] {startVertexOffset+3, endVertexOffset+2, endVertexOffset+3},
				new int[] {4+textureOffset, 7+textureOffset, 8+textureOffset},
				new int[] {0,0,0})
				);
	}
	
	private double getHeightVal(String string) 
	{
		//System.out.println(string);
		String temp = string.substring(string.indexOf(' ')+1, string.lastIndexOf(' '));
		temp = temp.substring(temp.indexOf(' '));
		//System.out.println(temp);
		return Double.parseDouble(temp);
	}
	private Integer OBJVertices(WallPoint p, int index)
	{
		//TODO change the return to void and add the ground vert outside this method
		//String ret = "";
		Vertex[] def = WallDefinitions.getSegmentCoords(p.getMaterial(), index%4);
		Vertex d = null;
		if(p!=null)
		{
			double theta = p.getAngle();
			for(int i =0; i <4; i++)
			{
				obj.addVertex(getVertexCoords(p, theta, def[i]));
			}
			d = WallDefinitions.pointNormal(def);
			obj.addNormal(FloatTuples.create((float)d.getX(), (float)d.getY(), (float)d.getZ()));
		}
		return obj.getNumVertices();
	}
	private FloatTuple getVertexCoords(WallPoint p, double theta, Vertex v)
	{
		float x = (float) ((p.x + Math.cos(theta)*v.getX()-32)/100.0);
		float y = (float) (v.getZ()/100.0);
		float z = (float) ((-1*p.y + Math.sin(theta)*v.getX()+32)/-100.0);
		return FloatTuples.create(x, y, z);
	}
}
