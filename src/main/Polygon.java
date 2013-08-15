package main;

public class Polygon {
	private Vertex v1;
	private Vertex v2;
	private Vertex v3;
	private int rReflect;
	private int gReflect;
	private int bReflect;
	private float rShade;
	private float gShade;
	private float bShade;
	private boolean hidden;
	private Vector3D vector1;
	private Vector3D vector2;
	private Vector3D vector3;
	private Vector3D unitNormal;

	public Polygon(String line){
		String[] values = line.split(" ");

		try{
			this.v1 = new Vertex(Float.parseFloat(values[0]), 
					Float.parseFloat(values[1]), 
					Float.parseFloat(values[2]));

			this.v2 = new Vertex(Float.parseFloat(values[3]), 
					Float.parseFloat(values[4]), 
					Float.parseFloat(values[5]));

			this.v3 = new Vertex(Float.parseFloat(values[6]), 
					Float.parseFloat(values[7]), 
					Float.parseFloat(values[8]));

			this.rReflect = Integer.parseInt(values[9]);

			this.gReflect = Integer.parseInt(values[10]);

			this.bReflect = Integer.parseInt(values[11]);

			//createVectors();
			//computeNormal();
		}catch(NumberFormatException e){
			System.out.println(e);
		}
	}

	private void createVectors() {
		this.vector1 = new Vector3D(v2.getX() - v1.getX(),
									v2.getY() - v1.getY(),
									v2.getZ() - v1.getZ());

		this.vector2 = new Vector3D(v3.getX() - v2.getX(),
									v3.getY() - v2.getY(),
									v3.getZ() - v2.getZ());

		this.vector3 = new Vector3D(v1.getX() - v3.getX(),
									v1.getY() - v3.getY(),
									v1.getZ() - v3.getZ());

	}

	public Vertex getV1(){
		return v1;
	}

	public Vertex getV2(){
		return v2;
	}

	public Vertex getV3(){
		return v3;
	}

	public void setHidden(boolean h){
		this.hidden = h;
	}

	public boolean isHidden(){
		return this.hidden;
	}

	public void computeUnitNormal(){
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
		
		float costh = unitNormal.dotProduct(lightSource);
		this.rShade = ( ( (ambience + intensity) * costh) * rReflect);
		this.gShade = ( ( (ambience + intensity) * costh) * gReflect);
		this.bShade = ( ( (ambience + intensity) * costh) * bReflect);
	}



}
