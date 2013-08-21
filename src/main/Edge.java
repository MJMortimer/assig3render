package main;

/**
 * An edge is an edge comprised of two vertices.
 * There is an invariant that the second vertex will always have the greater Y value.
 * @author matt
 *
 */
public class Edge {
	private Vector3D v1;
	private Vector3D v2;
	
	public Edge(Vector3D v1, Vector3D v2){
		if(v1.y<0 || v2.y<0){
			System.out.println(v1.y+" "+v2.y);
			throw new IllegalArgumentException();
		}
		if(v1.y < v2.y){
			this.v1 = v1;
			this.v2 = v2;
		}else{
			this.v1 = v2;
			this.v2 = v1;
		}
	}
	
	public Vector3D getV1(){
		return v1;
	}
	
	public Vector3D getV2(){
		return v2;
	}
}
