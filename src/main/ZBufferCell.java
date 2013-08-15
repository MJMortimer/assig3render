package main;

public class ZBufferCell {
		private float rShade;
		private float gShade;
		private float bShade;
		private float zDepth;
		
		public ZBufferCell(float r, float g, float b, float z){
			this.rShade = r;
			this.gShade = g;
			this.bShade = b;
			this.zDepth = z;
		}
		
		public ZBufferCell(){
			this.rShade = 0.0f;
			this.gShade = 0.0f;
			this.bShade = 0.0f;
			this.zDepth = Float.POSITIVE_INFINITY;
		}
}
