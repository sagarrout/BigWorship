package main.java.baha;

import javax.swing.JFrame;

public class FrameDesigner extends JFrame {
	private static FrameDesigner obj;
	private FrameDesigner() {
		
	}
	
	public static FrameDesigner getFrame() {
		 if (obj == null){  
		      synchronized(FrameDesigner.class){  
		        if (obj == null){  
		            obj = new FrameDesigner();//instance will be created at request time  
		        }  
		    }              
		    } 
		 obj.setJMenuBar(BAHAMenu.getMenu());
		 return obj;  
	}
}
