package main.java.baha;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class BahaStater {
	static JFrame initFrame;
	static JTextPane label;
	static JTextPane bglabel;
	static JFrame displayFrame;
	static JWindow webFrame;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static Double displayFrameX = screenSize.getWidth();
	static Double displayFrameY = screenSize.getHeight();
	static int maxfontSZ = 200;
	static int lwidth = displayFrameX.intValue();
	static int lheight = displayFrameY.intValue();
	static Timer t;
	static int x = 0;
	static boolean videoFlag = false;
	static Graphics dispGraphics;
	static JFrame videoFrame = new JFrame();
	static JFrame liveFrame = new JFrame();
	static Properties properties = new Properties();
	static JToggleButton showButton = new JToggleButton("SHOW SCREEN");

	private static void init() {
		displayFrame = new JFrame("Projector Frame");
		webFrame = new JWindow();
		initFrame = FrameDesigner.getFrame();
		initFrame.add(VerseView.getVerseView().getVerseViewPanel());
		initFrame.add(WorshipPannel.getWorship().getWorshipPanel());
		initFrame.setSize(900, 600);
		initFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		initFrame.setVisible(true);
		liveFrame.setVisible(false);
		Image icon = Toolkit.getDefaultToolkit().getImage(Constants.FAV_ICON_PATH);
		initFrame.setIconImage(icon);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			displayFrameX = Double.valueOf(gd.getDisplayMode().getWidth());
			displayFrameY = Double.valueOf(gd.getDisplayMode().getHeight());
			lwidth = Double.valueOf(displayFrameX * 0.85).intValue();
			lheight = Double.valueOf(displayFrameY * 0.90).intValue();
		}
		displayFrame.setResizable(false);
		displayFrame.setUndecorated(true);
//		webFrame.setResizable(false);
//		webFrame.setUndecorated(true);

		label = new JTextPane();
		label.setVisible(true);
		label.setOpaque(false);
		label.setContentType("text/html");
		label.setBackground(new Color(0, 0, 0, 0));

		label.setPreferredSize(new Dimension(lwidth, lheight));
		label.setDoubleBuffered(true);
		label.setMargin(new Insets(0,
				Double.valueOf(displayFrameX - (displayFrameX * .95)).intValue(),
				Double.valueOf(displayFrameY - (displayFrameY * .95)).intValue(),
				Double.valueOf(displayFrameX - (displayFrameX * .90)).intValue()));
		label.setEditable(false);
		
		bglabel = new JTextPane();
		bglabel.setVisible(true);
		bglabel.setOpaque(false);
		bglabel.setContentType("text/html");
		bglabel.setBackground(new Color(0, 0, 0, 0));

		bglabel.setPreferredSize(new Dimension(lwidth, lheight));
		bglabel.setDoubleBuffered(true);
		bglabel.setMargin(new Insets(0,
				Double.valueOf(displayFrameX - (displayFrameX * .95)).intValue(),
				Double.valueOf(displayFrameY - (displayFrameY * .95)).intValue(),
				Double.valueOf(displayFrameX - (displayFrameX * .90)).intValue()));
		bglabel.setEditable(false);
		bglabel.setVisible(false);
		displayFrame.add(new MovingText(), BorderLayout.NORTH);
		displayFrame.setBackground(new Color(0, 0, 0, 0));
		displayFrame.setPreferredSize(new Dimension(lwidth, lheight));
		webFrame.setBackground(new Color(0, 255, 0, 50));
		webFrame.setPreferredSize(new Dimension(displayFrameX.intValue(), Double.valueOf(displayFrameY*.25).intValue()));
		JToggleButton videoBtn = new JToggleButton("Web");
		JButton fontInc = new JButton("Font(+) " + maxfontSZ);
		JButton fontDec = new JButton("Font(-) " + maxfontSZ);
		JButton widthBtn1 = new JButton("Width(+) " + lwidth);
		JButton widthBtn2 = new JButton("Width(-) " + lwidth);
		JButton heightBtn1 = new JButton("Height(+) " + lheight);
		JButton heightBtn2 = new JButton("Height(-) " + lheight);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				label.setVisible(false);
				fontInc.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						maxfontSZ += 1;
						fontInc.setText("Font(+)" + maxfontSZ);
						fontDec.setText("Font(-)" + maxfontSZ);
					}
				});
				label.revalidate();
				label.setVisible(true);
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				label.setVisible(false);
				fontDec.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						maxfontSZ -= 1;
						fontInc.setText("Font(+)" + maxfontSZ);
						fontDec.setText("Font(-)" + maxfontSZ);
					}
				});
				label.revalidate();
				label.setVisible(true);
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				widthBtn1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						lwidth += 10;
						widthBtn1.setText("Width(+) " + lwidth);
						widthBtn2.setText("Width(-) " + lwidth);
					}
				});
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				widthBtn2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						lwidth -= 10;
						widthBtn1.setText("Width(+) " + lwidth);
						widthBtn2.setText("Width(-) " + lwidth);
					}
				});
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				heightBtn1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						lheight += 10;
						heightBtn1.setText("Height(+) " + lheight);
						heightBtn2.setText("Height(-) " + lheight);
					}
				});
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				heightBtn2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						lheight -= 10;
						heightBtn1.setText("Height(+) " + lheight);
						heightBtn2.setText("Height(-) " + lheight);
					}
				});
			}
		});

		GraphicsDevice device = initFrame.getGraphicsConfiguration().getDevice();
		Point p = null;
		for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			if (!device.equals(gd)) {
				p = gd.getDefaultConfiguration().getBounds().getLocation();
				break;
			}
		}
		makeNewFrame(p);
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		buttonsPanel.setLayout(new GridLayout(1, 0, 10, 0));
		buttonsPanel.setPreferredSize(new Dimension(displayFrameX.intValue(),fontInc.getPreferredSize().height+5));
//		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(fontInc);
		buttonsPanel.add(fontDec);
//		buttonsPanel.add(widthBtn1);
//		buttonsPanel.add(widthBtn2);
//		buttonsPanel.add(heightBtn1);
//		buttonsPanel.add(heightBtn2);
		buttonsPanel.add(videoBtn);
		buttonsPanel.add(showButton);
		buttonsPanel.add(new JLabel(""));
		initFrame.add(buttonsPanel, BorderLayout.PAGE_END);
		JLabel copyLabel = new JLabel(properties.getProperty(Constants.ESTABLISHMENT_NAME_PROP)+" \u00a9 2021        ",
				JLabel.RIGHT);
		copyLabel.setPreferredSize(new Dimension(400, 20));
		buttonsPanel.add(copyLabel, BorderLayout.EAST);
		copyLabel.setForeground(Color.WHITE);
		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color color = Color.decode(properties.getProperty(Constants.PROJECTOR_BACKGROUND));
				int transpNum = Constants.PROJECTOR_BACKGROUND_TRANSPARENT_DEFAULT;
				String transp = properties.getProperty(Constants.PROJECTOR_BACKGROUND_TRANSPARENT);
				if(null != transp)
					transpNum = Integer.valueOf(transp);
				if (showButton.getText().equals("SHOW SCREEN")) {
					lwidth = Double.valueOf(displayFrameX * 0.85).intValue();
					lheight = Double.valueOf(displayFrameY * 0.90).intValue();
					if (videoFlag) {
						for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
							displayFrameX = Double.valueOf(gd.getDisplayMode().getWidth());
							displayFrameY = Double.valueOf(gd.getDisplayMode().getHeight());
							//if (!device.equals(gd)) {
								
								screenSize = Toolkit.getDefaultToolkit().getScreenSize();
								webFrame.setSize(new Dimension(displayFrameX.intValue(), displayFrameY.intValue()));
								webFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
								webFrame.revalidate();
								webFrame.repaint();
								//break;
//							} else {
//								webFrame.revalidate();
//								webFrame.repaint();
//								// displayFrame.setLocation(device.getDefaultConfiguration().getBounds().getLocation());
//							}
						}
						webFrame.setPreferredSize(new Dimension(displayFrameX.intValue(), displayFrameY.intValue()));
						webFrame.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), transpNum));
						webFrame.revalidate();
						webFrame.repaint();
						showButton.setText("HIDE SCREEN");
						displayFrame.setVisible(false);
						bglabel.setVisible(true);
						webFrame.setVisible(true);
						
					} else {
						for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
							displayFrameX = Double.valueOf(gd.getDisplayMode().getWidth());
							displayFrameY = Double.valueOf(gd.getDisplayMode().getHeight());
							if (!device.equals(gd)) {
								screenSize = Toolkit.getDefaultToolkit().getScreenSize();
								displayFrame.setSize(new Dimension(displayFrameX.intValue(), displayFrameY.intValue()));
								displayFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
								displayFrame.revalidate();
								displayFrame.repaint();
//								break;
							} else {
								displayFrame.revalidate();
								displayFrame.repaint();
								// displayFrame.setLocation(device.getDefaultConfiguration().getBounds().getLocation());
							}
						}
						label.setPreferredSize(new Dimension(lwidth, lheight));
						displayFrame.setPreferredSize(new Dimension(lwidth, lheight));
						label.setBackground(new Color(0, 0, 0, 0));
						label.setForeground(Color.WHITE);
						
						displayFrame.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), transpNum));
//						displayFrame.setBackground(new Color(0, 0, 51, 180));
						label.revalidate();
						label.repaint();
						displayFrame.revalidate();
						displayFrame.repaint();
						showButton.setText("HIDE SCREEN");
						displayFrame.setVisible(true);
						webFrame.setVisible(false);
					}
					
				} else {
					showButton.setText("SHOW SCREEN");
					displayFrame.setBackground(new Color(0, 0, 0, 0));
					displayFrame.setVisible(false);
					webFrame.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), transpNum));
					webFrame.setVisible(false);
				}
			}
		});
		videoBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = Color.decode(properties.getProperty(Constants.PROJECTOR_BACKGROUND));
				String transp = properties.getProperty(Constants.PROJECTOR_BACKGROUND_TRANSPARENT);
				int transpNum = Constants.PROJECTOR_BACKGROUND_TRANSPARENT_DEFAULT;
				if (videoBtn.isSelected()) {
					videoFlag = true;
					bglabel.setPreferredSize(new Dimension(displayFrameX.intValue(), Double.valueOf(displayFrameY*.33).intValue()));
					webFrame.setPreferredSize(new Dimension(displayFrameX.intValue(), Double.valueOf(displayFrameY*.25).intValue()));
					webFrame.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), transpNum));
					webFrame.repaint();
				} else {
					videoFlag = false;
					label.setPreferredSize(new Dimension(lwidth, lheight));
					if(null != transp)
						transpNum = Integer.valueOf(transp);
					displayFrame.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), transpNum));
				}
			}
		});
		initFrame.setTitle(properties.getProperty(Constants.ESTABLISHMENT_NAME_PROP));
		initFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
//	     JWindow j=new JWindow();
//	     Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
//	     File f = new File(
//					System.getProperty("user.dir") + File.separator + "resources" + File.separator + "SplashScreen.JPG");
//	     BufferedImage inputImage;
//		try(InputStream is = new FileInputStream(f)) {
//			inputImage = ImageIO.read(is);
//			JLabel label = new JLabel(new ImageIcon(inputImage));
//			label.setSize(200, 300);
//			j.getContentPane().add(label);
//			j.setBounds(((int) d.getWidth() - 722) / 2, ((int) d.getHeight() - 401) / 2, 722, 401);
//			j.setVisible(true);
//		} catch (IOException e) {
//		}
//	     
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				try (InputStream isa = new FileInputStream(new File(System.getProperty("user.dir") + File.separator
//						+ "resources" + File.separator + "application.properties"))) {
//					if (isa != null) {
//						System.out.println("loading properties..");
//						properties.load(isa);
//					}
//				} catch (IOException e) {
//					// TODO: handle exception
//				}
//			}
//		});
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadProperties();
				init();
				// j.dispose();
			}
		});
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		
	}
	
	public static void loadProperties() {
		try (InputStream isa = new FileInputStream(new File(Constants.RESOURCES_PATH + "application.properties"))) {
			if (isa != null) {
				properties.load(isa);
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	private static void makeNewFrame(Point p) {
		displayFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		displayFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		webFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//		webFrame.setExtendedState(JFrame.MAXIMIZED_VERT);
		webFrame.setSize(new Dimension(Double.valueOf(displayFrameX).intValue(),Double.valueOf(displayFrameY*.20).intValue()));
//		webFrame.setLocationRelativeTo(displayFrame);
		if (null != p) {
			displayFrame.setLocation(p);
			webFrame.setLocation(p);
		}
//		webFrame.setLocation(0,Double.valueOf(displayFrameY*.70).intValue());

	}

	public static int calculateFontSize(String text, int maxWidth, int maxHeight, int rightPadding, int fontSize) {

		Font font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, fontSize);
		while (tryIfStringFits(text, new Dimension(maxWidth, maxHeight), font)) {
			font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, fontSize);
			fontSize += 1;
		}

		if(fontSize>maxfontSZ) {
			fontSize = maxfontSZ;
		}
		return fontSize;// -= 1;

	}
	
	public static int calculateFontSizeVerse(String text, int maxWidth, int maxHeight, int rightPadding, int fontSize) {

		Font font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, fontSize);
		while (tryIfStringFitsIn(text, new Dimension(maxWidth, maxHeight), font)) {
			font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, fontSize);
			fontSize += 1;
		}
		return fontSize;// -= 1;

	}

	private static boolean tryIfStringFits(String textToMeasure, Dimension areaToFit, Font font) {
		Hashtable hash = new Hashtable();
		AttributedString attributedString = new AttributedString(textToMeasure, hash);
		attributedString.addAttribute(TextAttribute.FONT, font);
		AttributedCharacterIterator attributedCharacterIterator = attributedString.getIterator();
		int start = attributedCharacterIterator.getBeginIndex();
		int end = attributedCharacterIterator.getEndIndex();

		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(attributedCharacterIterator,
				new FontRenderContext(null, false, false));
		float width = (float) (BahaStater.displayFrameX * .80);// *0.70
		lineBreakMeasurer.setPosition(start);
		boolean res = true;
		float lineHeight = 0;
		int noLines = 0;
		int lineBreakCount = 0;
		float lineHt = 0;
		int spaceCount = 0;
		lineBreakMeasurer.setPosition(start);
		boolean verseFg = false;
		boolean songFg = false;
		while (lineBreakMeasurer.getPosition() < end) {
			TextLayout textLayout = lineBreakMeasurer.nextLayout(width);
			if (textToMeasure.contains("\n")) {
				noLines = textToMeasure.split("\n").length;
				Pattern pattern = Pattern.compile("\n \r");
				Matcher matcher = pattern.matcher(textToMeasure);
				spaceCount = 0;
				while (matcher.find()) {
					spaceCount++;
				}
				songFg = true;
			} else {
				noLines++;
				verseFg = true;
			}
			lineBreakCount++;
			if (lineHt == 0)
				lineHt = textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();
			lineHeight += textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();

//			float maxFontHeight1 = textLayout.getAscent() + textLayout.getDescent();
//			maxFontHeight = maxFontHeight1+ (maxFontHeight1/2);
		}
		
		if(songFg)
			noLines = lineBreakCount+spaceCount+2;
		if(lineHeight>(BahaStater.displayFrameY * .80)) {
			lineHeight=Double.valueOf(BahaStater.displayFrameY * .80).floatValue();
		}

//		System.out.println("spaceCount....."+spaceCount);
//		System.out.println("noLines....."+noLines);
//		System.out.printlCn("lineBreakCount....."+lineBreakCount);
//		System.out.println("maxFontHeight....."+maxFontHeight);
//		System.out.println("lineHt....."+lineHt);
//		System.out.println("txt total Height....."+lineHeight);
//		System.out.println("total ht...."+((BahaStater.displayFrameY * .80)));
//		System.out.println("font....."+font.getSize());
//		if (verseFg) {
//			noLines = noLines + 1;
//		}
		if(songFg) {
			if(videoFlag && (int) lineHeight/lineBreakCount >= (int) (((areaToFit.getHeight())/(noLines))) // || lineHt >= 100
					) {
				return false;
			} else if ((int) lineHeight/lineBreakCount >= (int) (((BahaStater.displayFrameY * .80)/(noLines))) // || lineHt >= 100
					) {// || (lineHt*noLines)>=(lheight-(lineHt*2))
				return false;
			}
		} else if(verseFg) {
			if(videoFlag && (int) lineHeight/(lineBreakCount+1) >= (int) (((areaToFit.getHeight())/(noLines-1.5))) // || lineHt >= 100
					) {
				return false;
			} else if ((int) lineHeight/(lineBreakCount+1) >= (int) (((BahaStater.displayFrameY * .80)/(noLines))) // || lineHt >= 100
					) {// || (lineHt*noLines)>=(lheight-(lineHt*2))
				return false;
			}
		} else {
			if ((int) lineHeight >= (int) (((BahaStater.displayFrameY * .70))) // || lineHt >= 100
					) {// || (lineHt*noLines)>=(lheight-(lineHt*2))
				return false;
			}
		}
//		} else {
//			if ((maxFontHeight*noLines) >= (areaToFit.getHeight() * .85)) {
//				return false;
//			}
//		}
		return res;
	}
	
	private static boolean tryIfStringFitsIn(String textToMeasure, Dimension areaToFit, Font font) {
		Hashtable hash = new Hashtable();
		AttributedString attributedString = new AttributedString(textToMeasure, hash);
		attributedString.addAttribute(TextAttribute.FONT, font);
		AttributedCharacterIterator attributedCharacterIterator = attributedString.getIterator();
		int start = attributedCharacterIterator.getBeginIndex();
		int end = attributedCharacterIterator.getEndIndex();

		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(attributedCharacterIterator,
				new FontRenderContext(null, false, false));
		float width = (float) (BahaStater.displayFrameX * .80);// *0.70
		lineBreakMeasurer.setPosition(start);
		boolean res = true;
		float lineHeight = 0;
		int noLines = 0;
		int lineBreakCount = 0;
		float lineHt = 0;
		int spaceCount = 0;
		lineBreakMeasurer.setPosition(start);
		boolean verseFg = false;
		boolean songFg = false;
		while (lineBreakMeasurer.getPosition() < end) {
			TextLayout textLayout = lineBreakMeasurer.nextLayout(width);
			if (textToMeasure.contains("\n")) {
				noLines = textToMeasure.split("\n").length;
				Pattern pattern = Pattern.compile("\n \r");
				Matcher matcher = pattern.matcher(textToMeasure);
				spaceCount = 0;
				while (matcher.find()) {
					spaceCount++;
				}
				songFg = true;
			} else {
				noLines++;
				verseFg = true;
			}
			lineBreakCount++;
			if (lineHt == 0)
				lineHt = textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();
			lineHeight += textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();

		}
		
		if(songFg)
			noLines = lineBreakCount+spaceCount+2;

//		if (verseFg) {
//			noLines = noLines + 4;
//		}
		if(lineHeight>(BahaStater.displayFrameY * .80)) {
			lineHeight=Double.valueOf(BahaStater.displayFrameY * .80).floatValue();
		}
		
		if(songFg) {
			if(videoFlag && (int) lineHeight/lineBreakCount >= (int) (((areaToFit.getHeight())/(noLines))) // || lineHt >= 100
					) {
				return false;
			} else if ((int) lineHeight/lineBreakCount >= (int) (((BahaStater.displayFrameY * .80)/(noLines))) // || lineHt >= 100
					) {// || (lineHt*noLines)>=(lheight-(lineHt*2))
				return false;
			}
		} else if(verseFg) {
			if(videoFlag && (int) lineHeight/lineBreakCount >= (int) (((areaToFit.getHeight())/(noLines-1.5))) // || lineHt >= 100
					) {
				return false;
			} else if ((int) lineHeight/lineBreakCount >= (int) (((BahaStater.displayFrameY * .80)/(noLines))) // || lineHt >= 100
					) {// || (lineHt*noLines)>=(lheight-(lineHt*2))
				return false;
			}
		} else {
			if ((int) lineHeight >= (int) (((BahaStater.displayFrameY * .70))) // || lineHt >= 100
					) {// || (lineHt*noLines)>=(lheight-(lineHt*2))
				return false;
			}
		}
//		} else {
//			if ((maxFontHeight*noLines) >= (areaToFit.getHeight() * .85)) {
//				return false;
//			}
//		}
		return res;
	}
	
	static void copyToClipboard(String text) {
	    Toolkit.getDefaultToolkit().getSystemClipboard()
	        .setContents(new java.awt.datatransfer.StringSelection(text), null);
	}

}
