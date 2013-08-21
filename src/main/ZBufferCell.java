package main;

import java.awt.Color;

public class ZBufferCell {
		private Color shade;
		private float zDepth;
		
		public ZBufferCell(float r, float g, float b, float z){
			this.shade = new Color(r,g,b);
			this.zDepth = z;
		}
		
		public ZBufferCell(){
			this.shade = Color.WHITE;
			this.zDepth = Float.POSITIVE_INFINITY;
		}
		
		public Color getColor(){
			return shade;
		}
		
		public float getZ(){
			return this.zDepth;
		}

		public void setZ(float z) {
			this.zDepth = z;
			
		}

		public void setColor(Color shading) {
			this.shade = shading;			
		}
}
