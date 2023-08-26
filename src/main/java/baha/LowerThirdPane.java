package main.java.baha;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextPane;

public class LowerThirdPane extends JTextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LowerThirdPane () {
		super();
	    setOpaque(false);
	    setBackground(new Color(0, 0, 0, 0));
	}
	@Override
	  protected void paintComponent(Graphics g) {
//	    g.setColor(Color.GREEN);
//	    g.fillRect(0, 0, getWidth(), getHeight());

//	     uncomment the following to draw an image
		Image image = Toolkit.getDefaultToolkit().getImage(BahaStater.properties.getProperty(Constants.LOWER_THIRD_IMAGE));
	     g.drawImage(image, 0, 0, this);

	    super.paintComponent(g);
	  }
}
