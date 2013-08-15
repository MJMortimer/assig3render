package main;

import java.awt.Color;

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
			this.rShade = 1.0f;
			this.gShade = 1.0f;
			this.bShade = 1.0f;
			this.zDepth = Float.POSITIVE_INFINITY;
		}
		
		public Color getColor(){
			return new Color(rShade, gShade, bShade);
		}
}
