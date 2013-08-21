package main;


import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public class Renderer {
	private List<Polygon> polygons;
	private Vector3D lightSource;
	private float oldLat = 0;
	private float oldLon = 0;
	private float lat;
	private float lon;
	private float ambience;
	private Rectangle2D.Float bounds;
	private ZBufferCell[][] zBuffer;
	private BufferedImage image;
	private int startX;
	private int startY;
	private float scale;
	
	private RenderFrame frame;
	private final float pi =3.14f;
	

	public Renderer(String[] args){
		if(args.length != 4) throw new IllegalArgumentException("Incorrect Number of Arguments");

		initialise(args);		
		draw();
		addController();


	}

	private void addController() {
		Controller c = new Controller(this);
		frame.getDrawing().addMouseListener(c);
		frame.getDrawing().addMouseMotionListener(c);
		frame.getDrawing().addMouseWheelListener(c);
		frame.getDrawing().addKeyListener(c);
		
	}
	

	public synchronized void draw() {
		rotateVertices();		
		computeBoundingBox();		
		scaleAndTranslate();
		computeBoundingBox();
		markHidden();
		computePolygonShading();
		initZBuffer();
		
		for(Polygon p : polygons){
		//	System.out.println(p.isHidden());
			if(!p.isHidden())
				paintZBufferPoly(p);
		}

		convertToImage();
		frame.setImage(image);
		frame.repaint();
	}

	private void initialise(String[] args) {
		frame = new RenderFrame();
		
		
		String filename = args[0];
		parseArgs(args);

		polygons = new ArrayList<Polygon>();
		File shapesFile = new File(filename);
		try {
			Scanner scan = new Scanner(shapesFile);
			String lSource = scan.nextLine();
			lightSource = new Vector3D(lSource);
			while(scan.hasNextLine()){
				String poly = scan.nextLine();
				polygons.add(new Polygon(poly));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void scaleAndTranslate() {
		float dx = 0-bounds.x;
		float dy = 0-bounds.y;
		Transform translate = Transform.newTranslation(dx, dy, 0.0f);
		for(Polygon p : polygons){
			p.setVertex1(translate.multiply(p.getVertex1()));
			p.setVertex2(translate.multiply(p.getVertex2()));
			p.setVertex3(translate.multiply(p.getVertex3()));
			
		}
		
		computeBoundingBox();
		float sx = frame.getContentPane().getWidth()/bounds.width;
		float sy = frame.getContentPane().getHeight()/bounds.height;
		this.scale = Math.min(sx,sy);
		//System.out.println("scale: "+scale);
		Transform scaleTrans = Transform.newScale(scale, scale, scale);
		for(Polygon p : polygons){
			p.setVertex1(scaleTrans.multiply(p.getVertex1()));
			p.setVertex2(scaleTrans.multiply(p.getVertex2()));
			p.setVertex3(scaleTrans.multiply(p.getVertex3()));
			
		}
				
	}

	private void rotateVertices() {
		Transform yRot = Transform.newYRotation(oldLon-lon);
		Transform xRot = Transform.newXRotation(lat-oldLat);
		oldLat = lat;
		oldLon = lon;
		
		for(Polygon p : polygons){
			p.setVertex1(yRot.multiply(p.getVertex1()));
			p.setVertex2(yRot.multiply(p.getVertex2()));
			p.setVertex3(yRot.multiply(p.getVertex3()));

		}
		
		for(Polygon p : polygons){
			p.setVertex1(xRot.multiply(p.getVertex1()));
			p.setVertex2(xRot.multiply(p.getVertex2()));
			p.setVertex3(xRot.multiply(p.getVertex3()));

		}
		
		for(Polygon p : polygons){
			p.computeUnitNormal();
		}
		lightSource = yRot.multiply(lightSource);
		lightSource = xRot.multiply(lightSource);
		
	}

	//TODO working from here
	private void paintZBufferPoly(Polygon p) {
		EdgeListCell[] edgeLists = computeEdgeLists(p);
		/*for(EdgeListCell e : edgeLists){
			System.out.println(e);
		}*/
		for(int y = 0; y < edgeLists.length; y++){
			if(edgeLists[y].getLeftX() == Float.POSITIVE_INFINITY ||edgeLists[y].getRightX() == Float.POSITIVE_INFINITY)continue;
			int x = Math.round(edgeLists[y].getLeftX());
			float z = edgeLists[y].getLeftZ();
			float mz = (edgeLists[y].getRightZ() - edgeLists[y].getLeftZ()) / (edgeLists[y].getRightX() - edgeLists[y].getLeftX());

			int maxX = Math.round(edgeLists[y].getRightX());
			
			
			while(x < maxX){
				//System.out.println("max: "+maxX);
				
				if(z < zBuffer[x][y].getZ()){
					zBuffer[x][y].setZ(z);
					zBuffer[x][y].setColor(p.getShading());
				}
				x++; z+=mz;
				
			}
		}



	}

	private EdgeListCell[] computeEdgeLists(Polygon p) {
		float minYFloat = Math.min(p.getVertex1().y, p.getVertex2().y);
		minYFloat = Math.min(p.getVertex3().y, minYFloat);
		int minY = Math.round(minYFloat);

		float maxYFloat = Math.max(p.getVertex1().y, p.getVertex2().y);
		maxYFloat = Math.max(p.getVertex3().y, maxYFloat);
		int maxY = Math.round(maxYFloat);

		int height = maxY+1;
		

		EdgeListCell[] edgeLists = new EdgeListCell[height];
		for(int i = 0; i < edgeLists.length; i++){
			edgeLists[i] = new EdgeListCell();
		} 

		List<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(p.getVertex1(),p.getVertex2()));
		edges.add(new Edge(p.getVertex2(),p.getVertex3()));
		edges.add(new Edge(p.getVertex3(),p.getVertex1()));
		
		for(Edge e : edges){
			Vector3D v1 = e.getV1();
			Vector3D v2 = e.getV2();

			float mx = (v2.x - v1.x) / (v2.y - v1.y);
			float mz = (v2.z - v1.z) / (v2.y - v1.y);

			
			int i = Math.round(v1.y);
			int maxI = Math.round(v2.y);
			//System.out.println("v1.y: "+v1.y+" v2.y: "+v2.y+" length: "+edgeLists.length);
			

			float x = v1.x;
			float z = v1.z;

			while(i < maxI){
			//	System.out.println(i);
				edgeLists[i].setValues(x, z);
				i++; 
				x += mx; 
				z += mz; 
			}
		//	System.out.println();

			
			edgeLists[maxI].setValues(v2.x, v2.z);
		}
		

		return edgeLists;
	}

	private void initZBuffer() {
		//TODO change literals
		zBuffer = new ZBufferCell[frame.getContentPane().getWidth()+50][frame.getContentPane().getHeight()+50];
		for(int i=0; i<zBuffer.length; i++){
			for(int j = 0; j < zBuffer[i].length; j++){
				zBuffer[i][j] = new ZBufferCell();

			}
		}

	}

	private void convertToImage(){
		image  = new BufferedImage(zBuffer.length, zBuffer[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < zBuffer.length; x++) {
			for (int y = 0; y < zBuffer[0].length; y++) {
				image.setRGB(x, y, zBuffer[x][y].getColor().getRGB());
			}
		}
		
	}

	private void saveImage(String fname){
		try {ImageIO.write(image, "png", new File(fname));}
		catch(IOException e){System.out.println("Image saving failed: "+e);}
	}

	private void computePolygonShading() {
		for(Polygon p : polygons){
			p.computeShading(lightSource, ambience);
		}

	}

	public void parseArgs(String[] args){
		try{
			this.ambience = Float.parseFloat(args[1]);
			this.lat = (Float.parseFloat(args[2])) % (2*pi);
			this.lon = (Float.parseFloat(args[3])) % (2*pi);
			

		}catch(NumberFormatException e){
			throw new IllegalArgumentException(e);
		}
	}

	public void markHidden(){
		for(Polygon p : polygons){
			if(p.getUnitNormal().z>0.0f)
				p.setHidden(true);
			else
				p.setHidden(false);
		}
	}

	public void computeBoundingBox(){
		float maxX = Float.NEGATIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		float minX = Float.POSITIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		for(Polygon p : polygons){
			float v1X = p.getVertex1().x;
			float v1Y = p.getVertex1().y;
			if(v1X > maxX)
				maxX = v1X;
			if(v1X < minX)
				minX = v1X;
			if(v1Y > maxY)
				maxY = v1Y;
			if(v1Y < minY)
				minY = v1Y;

			float v2X = p.getVertex2().x;
			float v2Y = p.getVertex2().y;
			if(v2X > maxX)
				maxX = v2X;
			if(v2X < minX)
				minX = v2X;
			if(v2Y > maxY)
				maxY = v2Y;
			if(v2Y < minY)
				minY = v2Y;

			float v3X = p.getVertex3().x;
			float v3Y = p.getVertex3().y;
			if(v3X > maxX)
				maxX = v3X;
			if(v3X < minX)
				minX = v3X;
			if(v3Y > maxY)
				maxY = v3Y;
			if(v3Y < minY)
				minY = v3Y;
		}
		bounds = new Rectangle2D.Float(minX, minY, maxX-minX, maxY-minY);
	}
	
	public int getStartX(){
		return this.startX;
	}
	public void setStartX(int x){
		this.startX = x;
	}
	public int getStartY(){
		return this.startY;
	}
	public void setStartY(int y){
		this.startY = y;
	}

	public static void main(final String[] args){
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() {
			Renderer r=new Renderer(args);
			}});
			 
	}

	public void procLon(float diffX) {
		lon += diffX;
		lon = lon % (2*pi);		
		System.out.println("LON: "+lon);
	}
	public void procLat(float diffY) {
		lat += diffY;
		lat = lat % (2*pi);	
		System.out.println("LAT: "+lat);
	}
}
