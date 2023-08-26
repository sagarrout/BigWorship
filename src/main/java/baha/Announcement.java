/**
 * Copyright 2021 Bethel AG Hindi Aaradhana
 * 
 * The source code is developed and owned by Bethel AG Hindi Aaradhana. 
 * User must not sell the software to third party under any circumstance.
 * User must not use the software for commercial purpose.
 */
package main.java.baha;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class Announcement {
	private final JPanel displayPanel;
	Font font1 = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.BOLD, 14);
	private static Announcement obj;
	private JFileChooser fileDialog = new JFileChooser();
	JPanel imgPane = null;
	JScrollPane scrollPane = null;
	private JProgressBar progressBar;

	public Announcement() {
		if (BahaStater.properties.getProperty(Constants.ANNOUNCEMENTPROP) != null
				&& BahaStater.properties.getProperty(Constants.ANNOUNCEMENTPROP).trim().length() > 0) {
			String announcementPath = BahaStater.properties.getProperty(Constants.ANNOUNCEMENTPROP);
			fileDialog = new JFileChooser(announcementPath);
			fileDialog.setSelectedFile(new File(announcementPath));
		} else {
			fileDialog = new JFileChooser();
		}
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		displayPanel = new JPanel();
		imgPane = new JPanel();
		imgPane.setLayout(new GridLayout(0, 7));
		imgPane.setPreferredSize(new Dimension(Double.valueOf(BahaStater.lwidth * .65).intValue(),
				Double.valueOf(BahaStater.lheight * .6).intValue()));
		scrollPane = new JScrollPane(imgPane);
		scrollPane.setPreferredSize(new Dimension(Double.valueOf(BahaStater.initFrame.getWidth() * .85).intValue(),
				Double.valueOf(BahaStater.initFrame.getHeight() * .70).intValue()));
//		progressBar.setPreferredSize(new Dimension(Double.valueOf(BahaStater.initFrame.getWidth() * .85).intValue(),
//				Double.valueOf(BahaStater.initFrame.getHeight() * .2).intValue()));
		final String lblContent = String.format("<html><body align='center' style='font-family:"
				+ BahaStater.properties.getProperty(Constants.FONT) + ";'><p>%s</p></body></html>", "");
		JTextPane label1 = (JTextPane) BahaStater.label;
		BahaStater.displayFrame.add(label1);
		JTextArea area = new JTextArea();
		area.setBackground(Color.WHITE);
		area.setForeground(Color.BLACK);
		area.setLineWrap(true);
		area.setPreferredSize(new Dimension(800, 100));
		Font font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, 15);
		area.setFont(font);
		displayPanel.add(area, BorderLayout.NORTH);// .setList
		JButton button = new JButton("Show Announcement");
		button.setMaximumSize(new Dimension(150, 25));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Component component : ((JRootPane) BahaStater.displayFrame.getComponent(0)).getComponents()) {
					if (component instanceof JLayeredPane) {
						JLayeredPane lpane = (JLayeredPane) component;
						for (Component component1 : ((JPanel) lpane.getComponent(0)).getComponents()) {
							if (component1 instanceof MovingText) {
								JLabel lbl = (JLabel) component1;
								if (!lbl.isShowing()) {
									button.setText("Hide Announcement");
									// lbl.setText(area.getText());
									lbl.setText(String.format("%s", area.getText()));
									// lbl.setOpaque(false);
									// lbl.setFont(font1);
									// lbl.setBorder(new EmptyBorder(10,20,10,20));
									lbl.setVisible(true);

								} else {
									button.setText("Show Announcement");
									lbl.setText("");
									lbl.setVisible(false);
								}

							}
						}
					}

				}
			}
		});
		displayPanel.add(button, BorderLayout.NORTH);

		JButton button1 = new JButton("Load Slides");
		button1.setMaximumSize(new Dimension(150, 25));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							int returnVal = fileDialog.showOpenDialog(BahaStater.initFrame);
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = fileDialog.getSelectedFile();
								buildAnnouncement(file);
								drawImages(imgPane, scrollPane, Constants.ANNOUNCEMENTS_IMG_PATH);
								BahaStater.properties.setProperty(Constants.ANNOUNCEMENTPROP, file.getAbsolutePath());
								try (OutputStream output = new FileOutputStream(Constants.PROPERTIES_PATH)) {
									BahaStater.properties.store(output, null);
								} catch (IOException e2) {
									e2.printStackTrace();
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			}
		});
		displayPanel.add(button1, BorderLayout.NORTH);

		setProgressBar(0);
		progressBar.setStringPainted(true);
		progressBar.setVisible(true);
		progressBar.setBorderPainted(true);
		progressBar.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
		progressBar.setPreferredSize(new Dimension(imgPane.getPreferredSize().width,
				Double.valueOf(BahaStater.initFrame.getHeight() * .02).intValue()));

//		
//		JButton button2 = new JButton("Load Google Slides");
//		button2.setMaximumSize(new Dimension(150, 25));
//		button2.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				SwingUtilities.invokeLater(new Runnable() {
//					public void run() {
//						try {
//							HttpUtil.loadGoogleSlides();
//								System.out.println();
////								File file = fileDialog.getSelectedFile();
////								new LoadAnnouncement(file);
////								drawImages(imgPane, scrollPane, Constants.ANNOUNCEMENTS_CLOUD_IMG_PATH);
////								BahaStater.properties.setProperty(Constants.ANNOUNCEMENTPROP, file.getAbsolutePath());
////								try (OutputStream output = new FileOutputStream(Constants.PROPERTIES_PATH)) {
////									BahaStater.properties.store(output, null);
////								} catch (IOException e2) {
////									// TODO: handle exception
////								}
//						} catch (Exception ex) {
//						}
//					}
//				});
//			}
//		});
//		displayPanel.add(button2,BorderLayout.NORTH);
		// drawImages(imgPane, scrollPane, Constants.ANNOUNCEMENTS_IMG_PATH);
		displayPanel.add(scrollPane);
		displayPanel.add(progressBar);
		displayPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				drawImages(imgPane, scrollPane, Constants.ANNOUNCEMENTS_IMG_PATH);
			}
		});
		displayPanel.revalidate();
		displayPanel.repaint();
	}

	public static Announcement getAnnouncement() {
		if (obj == null) {
			synchronized (Announcement.class) {
				if (obj == null) {
					obj = new Announcement();// instance will be created at request time
				}
			}
		}
		return obj;
	}

	public JPanel getAnnouncementPanel() {
		return displayPanel;
	}

	public void drawImages(JPanel imgPane, JScrollPane scrollPane, String theImagepath) {
		JList<Icon> list = new JList<Icon>();
		imgPane.removeAll();

		try (Stream<Path> paths = Files.walk(Paths.get(theImagepath))) {
			list.removeAll();
			// .sorted(Comparator.comparing(p-> p.toFile().))
			paths.filter(Files::isRegularFile)
					.sorted(Comparator.comparing(
							p -> Integer.valueOf(p.toFile().getName().substring(9, p.toFile().getName().indexOf(".")))))
					.forEach(f -> {
						try {
							int scaledWidth = (int) (BahaStater.lwidth * .85) / 8;
							int scaledHeight = (int) (BahaStater.lheight * .6) / 4;
							BufferedImage inputImage = ImageIO.read(f.toFile());
							BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight,
									inputImage.getType());

							BufferedImage outputImage1 = new BufferedImage(
									Double.valueOf(BahaStater.displayFrameX.intValue()).intValue(),
									Double.valueOf(BahaStater.displayFrameY.intValue()).intValue(),
									inputImage.getType());
							// Icon icon = new ImageIcon(f.toAbsolutePath().toString(),
							// f.getFileName().toString());
							Graphics2D g2d = outputImage.createGraphics();
							g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
							g2d.dispose();

							// Icon icon = new ImageIcon(f.toAbsolutePath().toString(),
							// f.getFileName().toString());
							Graphics2D g2d2 = outputImage1.createGraphics();
							g2d2.drawImage(inputImage, 0, 0, BahaStater.displayFrameX.intValue(),
									BahaStater.displayFrameY.intValue(), null);
							g2d2.dispose();

							JLabel lbl = new JLabel(new ImageIcon(outputImage));
							lbl.setBorder(BorderFactory.createBevelBorder(0));
							lbl.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent e) {
									Arrays.asList(imgPane.getComponents()).parallelStream()
											.forEach(l -> l.setBackground(null));
									lbl.setBackground(Color
											.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
									lbl.setOpaque(true);
									for (Component component : ((JRootPane) BahaStater.displayFrame.getComponent(0))
											.getComponents()) {
										try {
											JLayeredPane lpane = (JLayeredPane) component;
											for (Component component1 : ((JPanel) lpane.getComponent(0))
													.getComponents()) {
												if (component1 instanceof JTextPane) {
													JTextPane label = (JTextPane) component1;
													label.setText("");
													label.insertIcon(new ImageIcon(outputImage1));
													label.setBounds(0, 0, BahaStater.displayFrameX.intValue(),
															BahaStater.displayFrameY.intValue());
													// label.setMargin(new Insets(0, 0, 0, 0));
													label.setMargin(new Insets(0, 0, 0, 0));
													label.revalidate();
													label.repaint();
													label.setVisible(true);
													BahaStater.displayFrame.add(label);
													BahaStater.displayFrame.repaint();
													break;
												}
											}
										} catch (Exception ex) {
											continue;
										}
									}
								}
							});
							imgPane.add(lbl);
							imgPane.revalidate();
							imgPane.repaint();
						} catch (Exception exp) {
							exp.printStackTrace();
						}
						scrollPane.revalidate();
						scrollPane.repaint();
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void buildAnnouncement(File file) {
		setProgressBar(0);
		List<XSLFSlide> slides = new ArrayList<>();
		XMLSlideShow ppt = null;
		int ii = 0;
		int ij = 0;
		int percent = 0;
		FileInputStream fis = null;
		FileOutputStream out = null;

		try {
			fis = new FileInputStream(file);
			ppt = new XMLSlideShow(fis);
			slides = ppt.getSlides();
			if (CollectionUtils.isNotEmpty(slides)) {
				ij += slides.size();
			}
			File dir = new File(Constants.ANNOUNCEMENTS_IMG_PATH);
			if (dir.exists()) {
				List<File> fList = Arrays.asList(dir.listFiles());
				if (CollectionUtils.isNotEmpty(fList)) {
					ij += fList.size();
				}
				if (ij > 0) {
					for (File fl : fList) {
						fl.delete();
						ii++;
//						System.out.println("ii1==" + ii);
//						System.out.println("ij1==" + ij);
						percent = Double.valueOf((100.0 / ij) * ii).intValue();
//						System.out.println("p1>>>" + percent);
//						double max = progressBar.getWidth();
//						System.out.println("max==" + max);
//						System.out.println("current==" + (max / 100.0) * percent);
						setProgressBar(percent);
					}
				}
			} else
				dir.mkdir();

			BufferedImage img = null;
			Dimension pgsize = ppt.getPageSize();
			if (CollectionUtils.isNotEmpty(slides)) {
				for (int i = 0; i < slides.size(); i++) {
					if (!slides.get(i).isHidden()) {
						img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
						Graphics2D graphics = img.createGraphics();

						// clear area
						graphics.setPaint(Color.white);
						graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

						// draw the images
						slides.get(i).draw(graphics);

						out = new FileOutputStream(
								Constants.ANNOUNCEMENTS_IMG_PATH + File.separator + "ppt_image" + i + ".png");
						javax.imageio.ImageIO.write(img, "png", out);
						// ppt.write(out);
					}
					ii++;
//					System.out.println("ii2==" + ii);
//					System.out.println("ij2==" + ij);
					percent = Double.valueOf((100.0 / ij) * ii).intValue();
//					System.out.println("p2>>>" + percent);
//					double max = progressBar.getWidth();
//					System.out.println("max==" + max);
//					System.out.println("current==" + (max / 100.0) * percent);
					setProgressBar(percent);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void setProgressBar(int val) {
		try {
			progressBar.setValue(val);
			progressBar.update(progressBar.getGraphics());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
