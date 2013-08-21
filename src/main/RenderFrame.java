package main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class RenderFrame extends JFrame {
	private JMenuBar menuBar;
	private JPanel drawing;
	private BufferedImage image = null;

	public RenderFrame(){
		super("3D Renderer");
		init();
	}

	public void init(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //setting the default close operation of JFrame
		setSize(800,600);
		setLayout(new BorderLayout());
		//setResizable(false);
		
		drawing = new JPanel(){
			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				paint(g);
				
			}
			
			@Override
			public void paint(Graphics g){
				if(image!=null){
					g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this); 
				}
			}
		};
		add(drawing);
		pack();
		setVisible(true);
	}	
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(800,600);
	}
	
	public void setImage(BufferedImage i){
		this.image = i;
	}
	
	public JComponent getDrawing(){
		return this.drawing;
	}
	

}
