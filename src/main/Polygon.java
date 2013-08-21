package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
	private Vector3D v1;
	private Vector3D v2;
	private Vector3D v3;
	private int rReflect;
	private int gReflect;
	private int bReflect;
	private Color shade;
	private boolean hidden;
	private Vector3D vector1;
	private Vector3D vector2;
	private Vector3D vector3;
	private Vector3D unitNormal;

	public Polygon(String line){
		String[] values = line.split(" ");

		try{
			this.v1 = new Vector3D(Float.parseFloat(values[0]), 
					Float.parseFloat(values[1]), 
					Float.parseFloat(values[2]));

			this.v2 = new Vector3D(Float.parseFloat(values[3]), 
					Float.parseFloat(values[4]), 
					Float.parseFloat(values[5]));

			this.v3 = new Vector3D(Float.parseFloat(values[6]), 
					Float.parseFloat(values[7]), 
					Float.parseFloat(values[8]));

			this.rReflect = Integer.parseInt(values[9]);

			this.gReflect = Integer.parseInt(values[10]);

			this.bReflect = Integer.parseInt(values[11]);
			
			
			createVectors();
			computeUnitNormal();
		}catch(NumberFormatException e){
			System.out.println(e);
		}
	}

	public void createVectors() {
		this.vector1 = v2.minus(v1);

		this.vector2 = v3.minus(v2);

		this.vector3 = v1.minus(v3);

	}

	public Vector3D getVertex1(){
		return v1;
	}

	public Vector3D getVertex2(){
		return v2;
	}

	public Vector3D getVertex3(){
		return v3;
	}
	
	public void setVertex1(Vector3D v){
		this.v1 = v;
	}
	
	public void setVertex2(Vector3D v){
		this.v2 = v;
	}
	public void setVertex3(Vector3D v){
		this.v3 = v;
	}

	public void setHidden(boolean h){
		this.hidden = h;
	}

	public boolean isHidden(){
		return this.hidden;
	}

	public void computeUnitNormal(){
		this.createVectors();
		Vector3D normal = vector1.crossProduct(vector2);
		this.unitNormal = new Vector3D(normal.x/normal.mag ,normal.y/normal.mag, normal.z/normal.mag);
	}
	
	public Vector3D getUnitNormal(){
		return this.unitNormal;
	}

	public void computeShading(Vector3D lightSource, float ambience) {
		createVectors();
		computeUnitNormal();
		
		float intensity = 1.0f;
		
		float costh = unitNormal.dotProduct(lightSource.unitVector());
		//System.out.println(costh);
		int rShade = (int) Math.abs( ( ambience + intensity * costh) * rReflect);
		int gShade = (int) Math.abs( ( ambience + intensity * costh) * gReflect);
		int bShade = (int) Math.abs( ( ambience + intensity * costh) * bReflect);
		if(rShade>255)rShade = 255;
		if(gShade>255)gShade = 255;
		if(bShade>255)bShade = 255;
		//System.out.println(rShade);
		//System.out.println(gShade);
		//System.out.println(bShade);
		this.shade = new Color(rShade, gShade, bShade);
	}
	


	public Color getShading() {
		return shade;
	}
	
	public Vector3D getVector1(){
		return vector1;
	}
	public Vector3D getVector2(){
		return vector2;
	}
	public Vector3D getVector3(){
		return vector3;
	}

	public void setVector1(Vector3D v) {
		this.vector1 = v;
		
	}
	
	public void setVector2(Vector3D v) {
		this.vector2 = v;
		
	}
	public void setVector3(Vector3D v) {
		this.vector3 = v;
		
	}



}
