package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Renderer {
	private List<Polygon> polygons;
	private Vector3D lightSource;
	private float lat;
	private float lon;
	private float ambience;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public static void main(String[] args){
		Renderer r = new Renderer(args);
	}
}
