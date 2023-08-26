/**
 * Copyright 2021 Bethel AG Hindi Aaradhana
 * 
 * The source code is developed and owned by Bethel AG Hindi Aaradhana. 
 * User must not sell the software to third party under any circumstance.
 * User must not use the software for commercial purpose.
 */
package main.java.baha;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
public class MovingText extends JLabel implements ActionListener {
	public MovingText()
	{
	this.setFont(new Font(BahaStater.properties.getProperty(Constants.FONT), Font.ITALIC, 50));
	this.setHorizontalAlignment(JLabel.CENTER);
	this.setVerticalAlignment(JLabel.CENTER);
	this.setVisible(false);
	//this.setOpaque(false);
	this.setBorder(new EmptyBorder(10,10,10,20));
	this.setBackground(new Color(255, 255, 255, 138));
	this.setForeground(new Color(204, 255, 255));
	this.setPreferredSize(new Dimension(Double.valueOf(BahaStater.lwidth*0.90).intValue(),100));
	this.setName("AnnouncementTicker");
	Timer t = new Timer(250, this);
	t.start();
	}

	public void actionPerformed(ActionEvent e)
	{
		String oldText = this.getText();
		if(null != oldText && !"".equals(oldText)) {
			String newText= oldText.substring(1)+oldText.substring(0,1);
			this.setText(newText);
		}
	
	}

}
