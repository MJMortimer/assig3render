package main;

public class EdgeListCell {
	private float leftX;
	private float leftZ;
	private float rightX;
	private float rightZ;
	
	public EdgeListCell(float leftX, float leftZ, float rightX, float rightZ){
		this.leftX = leftX;
		this.leftZ = leftZ;
		this.rightX = rightX;
		this.rightZ = rightZ;
	}
	
	public EdgeListCell(){
		this.leftX = Float.POSITIVE_INFINITY;
		this.leftZ = Float.POSITIVE_INFINITY;
		this.rightX = Float.NEGATIVE_INFINITY;
		this.rightZ = Float.POSITIVE_INFINITY;
	}
	
	public void setValues(float x,float z){
		if(x < leftX ){
			leftX = x;
			leftZ = z;
		}
		if(x>rightX){
			rightX = x;
			rightZ = z;
		}
	}
	
	public float getLeftX(){
		return this.leftX;
	}
	public float getLeftZ(){
		return this.leftZ;
	}
	public float getRightX(){
		return this.rightX;
	}
	public float getRightZ(){
		return this.rightZ;
	}
	
	public String toString(){
		return String.format("LeftX: %f, LeftZ: %f, RightX: %f, RightZ: %f",leftX,leftZ,rightX,rightZ  ); 
	}
}
