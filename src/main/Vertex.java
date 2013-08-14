package main;

public class Vertex {
	private float x;
	private float y;
	private float z;

	public Vertex(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setX(float x){
		this.x = x;
	}

	public float getX(){
		return this.x;
	}

	public void setY(float y){
		this.y = y;
	}

	public float getY(){
		return this.y;
	}

	public void setZ(float z){
		this.z = z;
	}

	public float getZ(){
		return this.z;
	}


}
