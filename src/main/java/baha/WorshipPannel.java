package main.java.baha;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WorshipPannel {
	private static WorshipPannel obj;
	private JSplitPane worshipPanel;
	static FilterComboBox fcb;

	private WorshipPannel() {
		Font font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.BOLD, 14);
		worshipPanel = new JSplitPane();
		JPanel scrollPane = new JPanel(new BorderLayout()); // split the window in top and bottom
		scrollPane.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		scrollPane.setFont(font);
		List<String> strList = new ArrayList<String>();
		strList.add(" ");
		strList.addAll(BAHAMenu.songsMap.keySet());
		fcb = new FilterComboBox(strList);
		//fcb.setSize(new Dimension(Double.valueOf(BahaStater.screenSize.width*0.25).intValue(), 25));
		worshipPanel.setDividerLocation(Double.valueOf(BahaStater.screenSize.width*0.28).intValue());
		worshipPanel.setEnabled(false);
		scrollPane.add(fcb, BorderLayout.NORTH);
		DefaultListModel listModel = new DefaultListModel();

		fcb.setFont(font);
		
		fcb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (fcb.getSelectedItem() != null && BAHAMenu.songsMap.containsKey(fcb.getSelectedItem()) 
						&& !listModel.contains(fcb.getSelectedItem())) {
					listModel.addElement(fcb.getSelectedItem());
				}
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
		JButton button = new JButton("Refresh");
		buttonPanel.add(button, BorderLayout.WEST);
		JButton button1 = new JButton("Remove");
		buttonPanel.add(button1, BorderLayout.EAST);
		scrollPane.add(buttonPanel, BorderLayout.SOUTH);
		int worshipFontSz = 18;
		if(null != BahaStater.properties.getProperty(Constants.WORSHIP_FONT_SIZE)) {
			worshipFontSz = Integer.valueOf(BahaStater.properties.getProperty(Constants.WORSHIP_FONT_SIZE));
		}
		final int fontSz = worshipFontSz;
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcb.refresh(new ArrayList<String>(BAHAMenu.songsMap.keySet()));
				fcb.repaint();
			}
		});
		
		JList<String> songsList = new JList<String>(listModel);
		songsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		songsList.setMinimumSize(new Dimension(30, 16));
		songsList.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		songsList.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.NORMAL_FONT_COLOR)));
		songsList.setFont(font);
		scrollPane.add(songsList, BorderLayout.CENTER);
		scrollPane.setSize(new Dimension(Double.valueOf(BahaStater.screenSize.getWidth()*0.25).intValue(), 0));
		scrollPane.revalidate();
		scrollPane.repaint();
		worshipPanel.setLeftComponent(scrollPane);

		JPanel panel = new JPanel(new BorderLayout());
		songsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

				JScrollPane paneScrollContents = (JScrollPane) panel.getComponent(0);
				JPanel paneContents = (JPanel) paneScrollContents.getViewport().getComponent(0);
				Color color = UIManager.getColor("Panel.background");
				paneContents.removeAll();
				paneScrollContents.setBackground(color);
				if (Objects.nonNull(songsList.getSelectedValue()) && BAHAMenu.songsMap.containsKey(songsList.getSelectedValue())) {
					
					if (null != songsList.getSelectedValue()) {
						String content = BAHAMenu.songsMap.get(songsList.getSelectedValue());
						content = content.replaceAll("==", "");
						content = content.replaceAll("\n\n", "\r\n\r\n");
						String[] songRows = content.split("\r\n\r\n");
						for (int i = 0; i < songRows.length; i++) {
							JTextArea area = new JTextArea("\r\n" + songRows[i] + "\r\n");
							area.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
							area.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.NORMAL_FONT_COLOR)));
							area.setBorder(BorderFactory.createCompoundBorder(
									   BorderFactory.createBevelBorder(0),
											      javax.swing.BorderFactory.createEmptyBorder(10, 25, 10, 10)
											   ));//new EmptyBorder(10, 20, 10, 10)
							area.setEditable(false);
							area.setLineWrap(true);
							area.setWrapStyleWord(false);
							Font font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, fontSz);
							area.setFont(font);
							paneContents.add(area);// .setListData(songRows);
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									area.addMouseListener(new MouseListener() {

										@Override
										public void mouseReleased(MouseEvent e) {

										}

										@Override
										public void mousePressed(MouseEvent e) {
											area.requestFocusInWindow();
											for (Component comp : paneContents.getComponents()) {
												comp.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
												comp.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.NORMAL_FONT_COLOR)));
											}
											area.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
											area.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_FONT_COLOR)));
											JTextPane label1 = (JTextPane) BahaStater.label;
											JTextPane label2 = (JTextPane) BahaStater.bglabel;
											label1.setVisible(false);
											SwingUtilities.invokeLater(new Runnable() {
												@Override
												public void run() {
													if(BahaStater.videoFlag) {
														StringBuilder hindi = new StringBuilder();
														StringBuilder eng = new StringBuilder();
														String[] arr = area.getText().replaceAll("==", "").split("\n \n");
														for(int i=0;i<arr.length;i++) {
															if(arr[i].trim().length()>0) {
																if((arr[i]).trim().matches(".*[a-z].*")) {
																	eng.append(arr[i].trim().replaceAll("\r", "").replaceAll("\n", "<br>"));
																} else {
																	hindi.append(arr[i].trim().replaceAll("\r", "").replaceAll("\n", "<br>"));
																}
															}
														}
														setContentTextWeb(label2,hindi+"\r\n"+eng);
													} else {
														setContentText(label1, area);
													}
												}
											});
											label1.setVisible(true);

										}

										@Override
										public void mouseExited(MouseEvent e) {

										}

										@Override
										public void mouseEntered(MouseEvent e) {

										}

										@Override
										public void mouseClicked(MouseEvent e) {

										}
									});
								}
							});

							area.addKeyListener(new KeyListener() {

								@Override
								public void keyTyped(KeyEvent e) {
									// TODO Auto-generated method stub

								}

								@Override
								public void keyReleased(KeyEvent e) {
									// TODO Auto-generated method stub

								}

								@Override
								public void keyPressed(KeyEvent e) {
									Color selectedColor = Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND));
									if (e.getKeyCode() == KeyEvent.VK_LEFT) {
										int i = 0;
										for (Component component : paneContents.getComponents()) {
											if (component.getBackground().getRed() == selectedColor.getRed() 
													&& component.getBackground().getGreen() == selectedColor.getGreen()
													&& component.getBackground().getBlue() == selectedColor.getBlue() && i > 0) {
												JTextArea target = (JTextArea) paneContents.getComponent(i - 1);
												JTextArea source = (JTextArea) paneContents.getComponent(i);
												target.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
												target.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_FONT_COLOR)));
												source.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
												source.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.NORMAL_FONT_COLOR)));
												JTextPane label1 = (JTextPane) BahaStater.label;
												JTextPane label2 = (JTextPane) BahaStater.bglabel;
												label1.setVisible(false);
												SwingUtilities.invokeLater(new Runnable() {
													@Override
													public void run() {
														if(BahaStater.videoFlag) {
															StringBuilder hindi = new StringBuilder();
															StringBuilder eng = new StringBuilder();
															String[] arr = target.getText().replaceAll("==", "").split("\n \n");
															for(int i=0;i<arr.length;i++) {
																if(arr[i].trim().length()>0) {
																	if((arr[i]).trim().matches(".*[a-z].*")) {
																		eng.append(arr[i].trim().replaceAll("\r", "").replaceAll("\n", "<br>"));
																	} else {
																		hindi.append(arr[i].trim().replaceAll("\r", "").replaceAll("\n", "<br>"));
																	}
																}
															}
															setContentTextWeb(label2,hindi+"\r\n"+eng);
														} else {
															setContentText(label1, target);
														}
														//setContentText(label1, target);
													}
												});
												label1.setVisible(true);
												if(BahaStater.videoFlag) {
													label1.setPreferredSize(
															new Dimension(BahaStater.lwidth, 200));
												} else {
													label1.setPreferredSize(
															new Dimension(BahaStater.lwidth, BahaStater.lheight));
												}
												target.scrollRectToVisible(target.getBounds());
												break;
											}
											i++;
										}
									} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
										int i = 0;
										for (Component component : paneContents.getComponents()) {
											if (component.getBackground().getRed() == selectedColor.getRed() 
													&& component.getBackground().getGreen() == selectedColor.getGreen()
													&& component.getBackground().getBlue() == selectedColor.getBlue()
													&& i < paneContents.getComponents().length - 1) {
												JTextArea target = (JTextArea) paneContents.getComponent(i + 1);
												JTextArea source = (JTextArea) paneContents.getComponent(i);
												target.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
												target.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_FONT_COLOR)));
												source.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
												source.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.NORMAL_FONT_COLOR)));

												JTextPane label1 = (JTextPane) BahaStater.label;
												JTextPane label2 = (JTextPane) BahaStater.bglabel;
												label1.setVisible(false);
												SwingUtilities.invokeLater(new Runnable() {
													@Override
													public void run() {
														if(BahaStater.videoFlag) {
															StringBuilder hindi = new StringBuilder();
															StringBuilder eng = new StringBuilder();
															String[] arr = target.getText().replaceAll("==", "").split("\n \n");
															for(int i=0;i<arr.length;i++) {
																if(arr[i].trim().length()>0) {
																	if((arr[i]).trim().matches(".*[a-z].*")) {
																		eng.append(arr[i].trim().replaceAll("\r", "").replaceAll("\n", "<br>"));
																	} else {
																		hindi.append(arr[i].trim().replaceAll("\r", "").replaceAll("\n", "<br>"));
																	}
																}
															}
															setContentTextWeb(label2,hindi+"\r\n"+eng);
														} else {
															setContentText(label1, target);
														}
														//setContentText(label1, target);
													}
												});
												label1.setVisible(true);
												target.scrollRectToVisible(target.getBounds());
												if(BahaStater.videoFlag) {
													label1.setPreferredSize(
															new Dimension(BahaStater.lwidth, Double.valueOf(BahaStater.displayFrameY*.15).intValue()));
												} else {
													label1.setPreferredSize(
															new Dimension(BahaStater.lwidth, BahaStater.lheight));
												}
												break;
											}
											i++;
										}
									} else if (e.getKeyCode() == KeyEvent.VK_UP) {
										
									} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
										
									}

								}
							});
						}

					}

				}
				paneContents.revalidate();
				paneContents.repaint();
			}
		});
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(songsList.getSelectedIndex()>-1) {
					//songsList.remove(songsList.getSelectedIndex());
					for(int i = 0; i<listModel.getSize(); i++)
					{
						if(listModel.get(i).equals(songsList.getSelectedValue())) {
							listModel.remove(i);
							songsList.revalidate();
							songsList.repaint();
							break;
						}
					}
				}
			}
		});

		JPanel contentpanel = new JPanel();
		contentpanel.setLayout(new GridLayout(3, 0, 10, 10));
		contentpanel.add(new JLabel());
		contentpanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		contentpanel.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.NORMAL_FONT_COLOR)));
		// contentpanel.setPreferredSize(new Dimension(500, 0));

		JScrollPane scrollContent = new JScrollPane(contentpanel);
		scrollContent.setPreferredSize(new Dimension(Double.valueOf(BahaStater.screenSize.width*0.70).intValue(), 0));
		// scrollContent.setVerticalScrollBar(new JScrollBar(0));
		panel.add(scrollContent, BorderLayout.EAST);
		panel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		panel.setPreferredSize(new Dimension(Double.valueOf(BahaStater.screenSize.width*0.70).intValue(), 0));

		worshipPanel.setRightComponent(panel);
		worshipPanel.revalidate();
		worshipPanel.repaint();
	}

	public static WorshipPannel getWorship() {
		if (obj == null) {
			synchronized (WorshipPannel.class) {
				if (obj == null) {
					obj = new WorshipPannel();// instance will be created at request time
				}
			}
		}
		return obj;
	}

	public JSplitPane getWorshipPanel() {
		return worshipPanel;
	}

	public void setWorshipPanel(JSplitPane worshipPanel) {
		this.worshipPanel = worshipPanel;
	}

	private void setContentText(JTextPane destination, JTextArea source) {
		int fontSizeHindi = BahaStater.calculateFontSize(source.getText(), (int) (BahaStater.displayFrameX.intValue()),
				(int) (BahaStater.displayFrameY.intValue()), 0, 10);
//		if (fontSizeHindi > BahaStater.maxfontSZ)
//			fontSizeHindi = BahaStater.maxfontSZ;
//		
//		if(fontSizeHindi > ((BahaStater.displayFrameY*.60)/8)) {
//			fontSizeHindi = Double.valueOf((BahaStater.displayFrameY*.60)/8).intValue();
//		}
		
		for (Component component : ((JRootPane) BahaStater.displayFrame.getComponent(0)).getComponents()) {
			if (component instanceof JLayeredPane) {
				JLayeredPane lpane = (JLayeredPane) component;
				for (Component component1 : ((JPanel) lpane.getComponent(0)).getComponents()) {
					if (component1 instanceof JScrollPane || component1 instanceof JTextPane) {
						((JPanel) lpane.getComponent(0)).remove(component1);
					}
				}
			}
		}
		BahaStater.displayFrame.revalidate();
		BahaStater.displayFrame.repaint();
		String myHtmlText = source.getText().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < myHtmlText.split("<br>").length; i++) {
//			builder.append("<span style='color:#FFFFFF;'>" + myHtmlText.split("<br>")[i] + "</span>");
//		}
		
		for (int i = 0; i < myHtmlText.split("<br>").length; i++) {
			if((myHtmlText.split("<br>")[i]).trim().matches(".*[a-z].*")) {
				builder.append("<span style='color:"+BahaStater.properties.getProperty(Constants.ENGLISH_FONT_COLOR)+";font-size:"+(fontSizeHindi*.88)+";'>"+myHtmlText.split("<br>")[i]+"</span><br>");
			} else {
				builder.append("<span style='color:"+BahaStater.properties.getProperty(Constants.HINDI_FONT_COLOR)+";'>"+myHtmlText.split("<br>")[i]+"</span><br>");
			}
		}
		
		
//		final String imagePath = new File(Constants.ANNOUNCEMENTS_IMG_PATH)+File.separator+(new File(Constants.ANNOUNCEMENTS_IMG_PATH).list()[0]);
		String imagePath = 	BahaStater.properties.getProperty(Constants.SONG_BACKGROUND_IMAGE_PATH);
		if(imagePath != null && imagePath != "" && new File(imagePath).exists()) {
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			scrollPane.setBorder(null);
			scrollPane.setViewport(new JViewport() {
				@Override
	            protected void paintComponent(Graphics g)
	            {
	                super.paintComponent(g);
	                Image image;
					try {
						image = ImageIO.read(new File(imagePath));
						 g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                //  add custom painting here. 
	                //  For a scaled image you can use:
	            }
			});
			//final String imagePath = new File(Constants.ANNOUNCEMENTS_IMG_PATH)+file.separator+(new File(Constants.ANNOUNCEMENTS_IMG_PATH).list()[0]);
			destination.setPreferredSize(new Dimension(BahaStater.lwidth, BahaStater.lheight));
			destination.setMargin(new Insets(0, 
					Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue(), 
					0, 
					Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue()));
			String lblContent = String
					.format("<html><body align='center' style='color:#FFFFFF;font-family:"+BahaStater.properties.getProperty(Constants.FONT)+";font-size:"
							+ fontSizeHindi + ";'>%s</body></html>", builder.toString());
			destination.setText(lblContent);
			destination.revalidate();
			destination.repaint();
			scrollPane.setViewportView(destination);
			BahaStater.displayFrame.add(scrollPane,BorderLayout.CENTER);
			BahaStater.displayFrame.revalidate();
			BahaStater.displayFrame.repaint();
		} else {
			destination.setPreferredSize(new Dimension(BahaStater.lwidth, BahaStater.lheight));
			destination.setMargin(new Insets(0, 
					Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue(), 
					0, 
					Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue()));
			String lblContent = String
					.format("<html><body align='center' style='color:#FFFFFF;font-family:"+BahaStater.properties.getProperty(Constants.FONT)+";font-size:"
							+ fontSizeHindi + ";'>%s</body></html>", builder.toString());
			destination.setText(lblContent);
			destination.revalidate();
			destination.repaint();
			BahaStater.displayFrame.add(destination,BorderLayout.CENTER);
			BahaStater.displayFrame.revalidate();
			BahaStater.displayFrame.repaint();
		}
	}
	
	
	private void setContentTextWeb(JTextPane destination, String source) {
		int fontSizeHindi = BahaStater.calculateFontSize(source, Double.valueOf(BahaStater.displayFrameX*.80).intValue(), Double.valueOf(BahaStater.displayFrameY*.25).intValue(), 0, 10);
		if (fontSizeHindi > BahaStater.maxfontSZ)
			fontSizeHindi = BahaStater.maxfontSZ;

		String myHtmlText = source.replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < myHtmlText.split("<br>").length; i++) {
			if((myHtmlText.split("<br>")[i]).trim().matches(".*[a-z].*")) {
				if(!builder.toString().contains("<nl>")) {
					builder.append("<span style='color:"+BahaStater.properties.getProperty(Constants.ENGLISH_FONT_COLOR)+";'><nl>"+myHtmlText.split("<br>")[i]+"</span>");
				}
				else {
					builder.append("<span style='color:"+BahaStater.properties.getProperty(Constants.ENGLISH_FONT_COLOR)+";'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+myHtmlText.split("<br>")[i]+"</span>");
				}
			} else {
				builder.append("<span style='color:"+BahaStater.properties.getProperty(Constants.HINDI_FONT_COLOR)+";font-size:"+(fontSizeHindi>8?fontSizeHindi-8:fontSizeHindi-3)+"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+myHtmlText.split("<br>")[i]+"</span>");
			}
//			builder.append("<span style='color:#FFFFFF;'>" + myHtmlText.split("<br>")[i] + "</span>");
		}
		destination.setBorder(new EmptyBorder(5,25,5,5));
		destination.setPreferredSize(new Dimension(Double.valueOf(BahaStater.displayFrameX.intValue()*.98).intValue(), Double.valueOf(BahaStater.displayFrameY*.26).intValue()));
//		destination.setMargin(new Insets(Double.valueOf(BahaStater.displayFrameY*.70).intValue(), 
//				Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue(), 
//				Double.valueOf(BahaStater.displayFrameY*.05).intValue(), 
//				Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue()));
		String lblContent = String
				.format("<html><body align='center' style='font-family:"+BahaStater.properties.getProperty(Constants.FONT)+";word-wrap:break-word;font-size:"
						+ (fontSizeHindi>8?fontSizeHindi-8:fontSizeHindi) + ";'>%s</body></html>", builder.toString().replaceAll("<nl>", "<br>"));
		destination.setText(lblContent);
		destination.revalidate();
		destination.repaint();
		BahaStater.webFrame.add(destination,BorderLayout.PAGE_END);
		BahaStater.webFrame.setAlwaysOnTop(true);
	}

}
