package main;

public class Polygon {
	private Vertex v1;
	private Vertex v2;
	private Vertex v3;
	private int r;
	private int g;
	private int b;

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

			this.r = Integer.parseInt(values[9]);

			this.g = Integer.parseInt(values[10]);

			this.b = Integer.parseInt(values[11]);

		}catch(NumberFormatException e){
			System.out.println(e);
		}
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

}
