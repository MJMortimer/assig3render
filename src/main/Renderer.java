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

public class Renderer {
	private List<Polygon> polygons;
	private Vector3D lightSource;
	private float lat;
	private float lon;
	private float ambience;
	private Rectangle2D.Float bounds;
	private ZBufferCell[][] zBuffer;
	private BufferedImage image;

	public Renderer(String[] args){
		if(args.length != 4) throw new IllegalArgumentException("Incorrect Number of Arguments");

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

		/**
		//TODO ROTATE THE POLYGONS AND LIGHT SOURCE
		 */
		computeBoundingBox();
		/**
		//TODO SCALE AND TRANSLATE TO FIT. Compute box again
		 */

		computePolygonShading();



		initZBuffer();
		convertToImage();
		saveImage("tester");

	}

	private void initZBuffer() {
		//TODO change literals
		zBuffer = new ZBufferCell[800][800];
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
			this.lat = Float.parseFloat(args[2]);
			this.lon = Float.parseFloat(args[3]);

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
			float v1X = p.getV1().getX();
			float v1Y = p.getV1().getY();
			if(v1X > maxX)
				maxX = v1X;
			if(v1X < minX)
				minX = v1X;
			if(v1Y > maxY)
				maxY = v1Y;
			if(v1Y < minY)
				minY = v1Y;

			float v2X = p.getV2().getX();
			float v2Y = p.getV2().getY();
			if(v2X > maxX)
				maxX = v2X;
			if(v2X < minX)
				minX = v2X;
			if(v2Y > maxY)
				maxY = v2Y;
			if(v2Y < minY)
				minY = v2Y;

			float v3X = p.getV3().getX();
			float v3Y = p.getV3().getY();
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

	public static void main(String[] args){
		Renderer r = new Renderer(args);
	}
}
