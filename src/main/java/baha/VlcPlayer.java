package main.java.baha;

public class VlcPlayer {
	private final JPanel displayPanel;
	private final JPanel progressBarPanel;
	Font font1 = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.BOLD, 14);
	private static VlcPlayer obj;
	private final JFileChooser  fileDialog;
	private JPanel imgPane = null;
	private JScrollPane scrollPane = null;
	private JToggleButton repeatButton;
	private JToggleButton repeatAllButton;
	private List<File> videoFiles = new LinkedList<File>();
	private int currVideoIndex = 0;
	String videoPath = null;
	private boolean action_stop = false;
	private final JProgressBar progressBar;
	public VlcPlayer() {
		if(BahaStater.properties.getProperty(Constants.VIDEOPROP)!=null && BahaStater.properties.getProperty(Constants.VIDEOPROP).trim().length()>0) {
			videoPath = BahaStater.properties.getProperty(Constants.VIDEOPROP);
			fileDialog = new JFileChooser(videoPath);
			fileDialog.setSelectedFile(new File(videoPath));
		} else {
			fileDialog = new JFileChooser();
		}
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		BahaStater.videoFrame.setSize(400, 400);
		BahaStater.videoFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		BahaStater.videoFrame.setVisible(false);
		BahaStater.videoFrame.setResizable(false);
		BahaStater.videoFrame.setUndecorated(true);
		BahaStater.videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final Canvas canvas = new Canvas();
		canvas.setBackground(Color.black);
		
		String VLC_PATH=BahaStater.properties.getProperty(Constants.VLCPATH);
		File f = new File(VLC_PATH + "/libvlc.dll");
		if(!f.exists()) {
			VLC_PATH = "C:\\Program Files\\VideoLAN\\VLC";
			f = new File(VLC_PATH + "/libvlc.dll");
		}
		if(!f.exists()) {
			VLC_PATH = "C:\\Program Files (x86)\\VideoLAN\\VLC";
			f = new File(VLC_PATH + "/libvlc.dll");
		} 
		if(!f.exists()) {
			displayPanel = new JPanel();
			progressBarPanel = null;
			JOptionPane.showMessageDialog(BahaStater.initFrame, "Please Install VLC media player!");
		}  else {
			
	    System.out.println("VLC PATH CORRECT: " + f.exists());
	    BahaStater.properties.setProperty(Constants.VLCPATH, VLC_PATH);
	    try (OutputStream output = new FileOutputStream(Constants.PROPERTIES_PATH)) {
			BahaStater.properties.store(output, null);
		} catch (IOException e2) {
			// TODO: handle exception
		}
//		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), BahaStater.properties.getProperty(Constants.VLCPATH));
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_PATH);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		final MediaPlayerFactory mpf = new MediaPlayerFactory();
		final EmbeddedMediaPlayer emp1 = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(BahaStater.videoFrame));
		BahaStater.videoFrame.add(canvas);
		emp1.setVideoSurface(mpf.newVideoSurface(canvas));
		emp1.setEnableMouseInputHandling(false);
		BahaStater.videoFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				BahaStater.videoFrame.setVisible(false);
				emp1.stop();
				GraphicsDevice device = BahaStater.initFrame.getGraphicsConfiguration().getDevice();
				for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
					if (!device.equals(gd)) {
						BahaStater.displayFrame.setSize(new Dimension(BahaStater.displayFrameX.intValue(), BahaStater.displayFrameY.intValue()));
						BahaStater.displayFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
						BahaStater.displayFrame.revalidate();
						BahaStater.displayFrame.repaint();
						break;
					}
				}
			}
		});
		displayPanel = new JPanel();
		displayPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		progressBarPanel = new JPanel();
		progressBarPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		imgPane =  new JPanel();
		imgPane.setLayout(new GridLayout(0,4,Double.valueOf(BahaStater.initFrame.getHeight()*.01).intValue(),Double.valueOf(BahaStater.initFrame.getHeight()*.01).intValue()));
		imgPane.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		imgPane.setPreferredSize(new Dimension(Double.valueOf(BahaStater.initFrame.getWidth()*.80).intValue(),Double.valueOf(BahaStater.initFrame.getHeight()*.75).intValue()));
		scrollPane = new JScrollPane( imgPane );
		scrollPane.setPreferredSize(new Dimension(Double.valueOf(BahaStater.initFrame.getWidth()*.81).intValue(),Double.valueOf(BahaStater.initFrame.getHeight()*.76).intValue()));
		scrollPane.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		JTextPane label1 = (JTextPane) BahaStater.label;
		BahaStater.displayFrame.add(label1);
		
		if(null != videoPath) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					randerFiles(new File(videoPath), emp1);
					imgPane.revalidate();
					imgPane.repaint();
				}
			});
		}
		
		
		JButton button1 = new JButton("Add Video File or Folder");
		fileDialog.setMultiSelectionEnabled(true);
		fileDialog.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							int returnVal = fileDialog.showOpenDialog(BahaStater.initFrame);
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = fileDialog.getSelectedFile();
								imgPane.removeAll();
								randerFiles(file, emp1);
								BahaStater.properties.setProperty(Constants.VIDEOPROP, file.getAbsolutePath());
								try (OutputStream output = new FileOutputStream(Constants.PROPERTIES_PATH)) {
									BahaStater.properties.store(output, null);
								} catch (IOException e2) {
									// TODO: handle exception
								}
								imgPane.revalidate();
								imgPane.repaint();
							}
						} catch (Exception ex) {
						}
					}
				});
			}
		});
		
		
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setVisible(true);
	    progressBar.setBorderPainted(true);
	    progressBar.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    progressBar.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
	    progressBar.setPreferredSize(new Dimension(imgPane.getPreferredSize().width,Double.valueOf(BahaStater.initFrame.getHeight()*.02).intValue()));
	    progressBar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
					int v = progressBar.getValue();

			       //Retrieves the mouse position relative to the component origin.
			       int mouseX = e.getX();

			       //Computes how far along the mouse is relative to the component width then multiply it by the progress bar's maximum value.
			       int progressBarVal = (int)Math.round(((double)mouseX / (double)progressBar.getWidth()) * progressBar.getMaximum());
			       emp1.setPosition(Double.valueOf(progressBarVal/100.00).floatValue());
			       progressBar.setValue(progressBarVal);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseDragged(e);
			}
	    	
		});
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				emp1.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

					@Override
					public void finished(MediaPlayer mediaPlayer) {
						
						progressBar.setValue(100);
						if (!repeatButton.isSelected() && !repeatAllButton.isSelected()) {
							BahaStater.videoFrame.setVisible(false);
							GraphicsDevice device = BahaStater.initFrame.getGraphicsConfiguration()
									.getDevice();
							for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment()
									.getScreenDevices()) {
								if (!device.equals(gd)) {
									BahaStater.displayFrame
											.setSize(new Dimension(BahaStater.displayFrameX.intValue(),
													BahaStater.displayFrameY.intValue()));
									BahaStater.displayFrame.setLocation(
											gd.getDefaultConfiguration().getBounds().getLocation());
									BahaStater.displayFrame.revalidate();
									BahaStater.displayFrame.repaint();
									break;
								}
							}
						} 
						
					}

					@Override
					public void error(MediaPlayer mediaPlayer) {
						BahaStater.videoFrame.setVisible(false);
						GraphicsDevice device = BahaStater.initFrame.getGraphicsConfiguration().getDevice();
						for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
							if (!device.equals(gd)) {
								BahaStater.displayFrame.setSize(new Dimension(BahaStater.displayFrameX.intValue(), BahaStater.displayFrameY.intValue()));
								BahaStater.displayFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
								BahaStater.displayFrame.revalidate();
								BahaStater.displayFrame.repaint();
								break;
							}
						}
						emp1.stop();
						emp1.release();
					}

					@Override
					public void stopped(MediaPlayer mediaPlayer) {
						if (repeatButton.isSelected()) {
//								emp1.play();
						} else if(repeatAllButton.isSelected() && !action_stop) {

							JLabel next;
							JLabel current = null;
							try {
								Color clr = Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND));
								for( int p = 0; p < imgPane.getComponents().length;p++) {
									if(imgPane.getComponent(p) instanceof JLabel 
											&& imgPane.getComponent(p).getBackground().getRed()==clr.getRed()
											&& imgPane.getComponent(p).getBackground().getGreen()==clr.getGreen()
											&& imgPane.getComponent(p).getBackground().getBlue()==clr.getBlue()) {
										current = (JLabel) imgPane.getComponent(p);
										break;
									}
								}
								Arrays.asList(imgPane.getComponents()).parallelStream().forEach(t -> {
			                		t.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
			            			t.setForeground(Color.WHITE);
			            			((JLabel)t).setOpaque(true);
			                	});
								for(int idx =0; idx<videoFiles.size();idx++) {
									if(videoFiles.get(idx).getName().equals(current.getText())) {
										currVideoIndex = idx;
										if (videoFiles.size() - 1 > currVideoIndex) {
											currVideoIndex++;
											emp1.prepareMedia(videoFiles.get(currVideoIndex).getAbsolutePath());
										} else {
											currVideoIndex = 0;
											emp1.prepareMedia(videoFiles.get(currVideoIndex).getAbsolutePath());
										}
									}
								}
								next = (JLabel) Arrays.asList(imgPane.getComponents()).parallelStream()
										.filter(f -> (((JLabel) f).getText()).equals(videoFiles.get(currVideoIndex).getName())).findFirst().get();
								next.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
								next.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_FONT_COLOR)));	
								imgPane.revalidate();
								imgPane.repaint();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							progressBar.setValue(0);
							emp1.play();
						} else {
							BahaStater.videoFrame.setVisible(false);
							GraphicsDevice device = BahaStater.initFrame.getGraphicsConfiguration().getDevice();
							for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
								if (!device.equals(gd)) {
									BahaStater.displayFrame.setSize(new Dimension(BahaStater.displayFrameX.intValue(), BahaStater.displayFrameY.intValue()));
									BahaStater.displayFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
									BahaStater.displayFrame.revalidate();
									BahaStater.displayFrame.repaint();
									break;
								}
							}
						}
						action_stop=false;
					}

					@Override
					public void playing(MediaPlayer mediaPlayer) {
						// TODO Auto-generated method stub
						super.playing(mediaPlayer);
						try {
							while(emp1.isPlaying()) {
								Thread.sleep(500);
								progressBar.setValue(Double.valueOf(emp1.getPosition()*100).intValue());
								progressBar.revalidate();
								progressBar.repaint();
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				});
			}
		});
		
		
		
		JButton playVideo = new JButton("Play");
		JToggleButton pauseVideo = new JToggleButton("Pause");
		playVideo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				action_stop = false;
				progressBar.setValue(0);
				BahaStater.videoFrame.setVisible(true);
				GraphicsDevice device = BahaStater.initFrame.getGraphicsConfiguration().getDevice();
				for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
					if (!device.equals(gd)) {
						BahaStater.videoFrame.setSize(new Dimension(BahaStater.displayFrameX.intValue(), BahaStater.displayFrameY.intValue()));
						BahaStater.videoFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
						break;
					}
				}
				emp1.play();
				pauseVideo.setSelected(false);
//				progressBar.setValue(emp1.getLength());
			}
		});
		
		
		
		pauseVideo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				action_stop = false;
				BahaStater.videoFrame.setVisible(true);
				emp1.pause();
			}
		});
		
		JButton stopVideo = new JButton("Stop");
		stopVideo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				action_stop = true;
				progressBar.setValue(0);
				repeatButton.setSelected(false);
				repeatAllButton.setSelected(false);
				emp1.stop();
				BahaStater.videoFrame.setVisible(false);
				GraphicsDevice device = BahaStater.initFrame.getGraphicsConfiguration().getDevice();
				for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
					if (!device.equals(gd)) {
						BahaStater.displayFrame.setSize(new Dimension(BahaStater.displayFrameX.intValue(), BahaStater.displayFrameY.intValue()));
						BahaStater.displayFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
						BahaStater.displayFrame.revalidate();
						BahaStater.displayFrame.repaint();
						break;
					}
				}
				
			}
		});
		
		JToggleButton muteVideo = new JToggleButton("Mute");
		if(emp1.isMute()) {
			muteVideo.setSelected(true);
		}
		muteVideo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(muteVideo.isSelected()) {
					emp1.mute(true);
				} else {
					emp1.mute(false);
				}
			}
		});
		
		repeatButton = new JToggleButton("Repeat");
		repeatButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(repeatButton.isSelected()) {
					repeatAllButton.setSelected(false);
					emp1.setRepeat(true);
				} else
					emp1.setRepeat(false);
				
			}
		});
		
		
		repeatAllButton = new JToggleButton("Repeat All");
		repeatAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(repeatAllButton.isSelected()) {
					repeatButton.setSelected(false);
					emp1.setRepeat(false);
				} 
				
			}
		});
		
		Desktop desktop = Desktop.getDesktop();
		
		JButton openFolder = new JButton("Browse");
		openFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(null != videoPath) {
					try {
						desktop.open(new File(videoPath));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0, 1, Double.valueOf(BahaStater.initFrame.getHeight()*.02).intValue(),Double.valueOf(BahaStater.initFrame.getHeight()*.02).intValue()));
		buttonPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		buttonPanel.add(button1);
		buttonPanel.add(playVideo);
		buttonPanel.add(openFolder);
		buttonPanel.add(pauseVideo);
		buttonPanel.add(muteVideo);
		buttonPanel.add(stopVideo);
		buttonPanel.add(repeatButton);
		buttonPanel.add(repeatAllButton);
		buttonPanel.add(openFolder);
		buttonPanel.setPreferredSize(new Dimension(Double.valueOf(BahaStater.initFrame.getWidth()*.10).intValue(),Double.valueOf(BahaStater.initFrame.getHeight()*.80).intValue()));
		progressBarPanel.setPreferredSize(new Dimension(Double.valueOf(BahaStater.initFrame.getWidth()*.80).intValue(),Double.valueOf(BahaStater.initFrame.getHeight()*.80).intValue()));
//		buttonPanel.setPreferredSize(new Dimension(Double.valueOf(BahaStater.screenSize.getWidth()*.10).intValue(),Double.valueOf(BahaStater.screenSize.getHeight()*.80).intValue()));
		displayPanel.add(buttonPanel,BorderLayout.WEST);
		progressBarPanel.add(scrollPane,BorderLayout.NORTH);
//		displayPanel.add(progressBar, BorderLayout.SOUTH);
		progressBarPanel.add(progressBar, BorderLayout.SOUTH);
		displayPanel.add(progressBarPanel,BorderLayout.EAST);
		displayPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		displayPanel.revalidate();
		displayPanel.repaint();
		}
	}
	
	public static VlcPlayer getVlcPlayer() {
		if (obj == null) {
			synchronized (VlcPlayer.class) {
				if (obj == null) {
					obj = new VlcPlayer();// instance will be created at request time
				}
			}
		}
		return obj;
	}

	public JPanel getVideoPanel() {
		return displayPanel;
	}
	
	private void randerFiles(File file, EmbeddedMediaPlayer emp1) {
		if(!videoFiles.isEmpty())
			videoFiles.clear();
		if(file.isDirectory()) {
			for(File fl:file.listFiles()) {
				if(!fl.isHidden() 
						&& fl.isFile()) {
					Image icon = Toolkit.getDefaultToolkit().getImage(Constants.VIDEOICON_PATH);
					JLabel lblFile = new JLabel();
					lblFile.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
					lblFile.setForeground(Color.WHITE);
					lblFile.setHorizontalAlignment(SwingUtilities.CENTER);
					lblFile.setVerticalAlignment(SwingUtilities.CENTER);
					lblFile.setOpaque(true);
					lblFile.setText(fl.getName());
					lblFile.setBorder(BorderFactory.createCompoundBorder(
							   BorderFactory.createBevelBorder(0),
							      javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
							   ));
					videoFiles.add(fl);
					lblFile.setIcon(new ImageIcon(icon));
					lblFile.setName(fl.getName());
					imgPane.add(lblFile);
					imgPane.revalidate();
					imgPane.repaint();
					lblFile.addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                	Arrays.asList(imgPane.getComponents()).parallelStream().forEach(l -> {
		                		l.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
		            			l.setForeground(Color.WHITE);
		            			((JLabel)l).setOpaque(true);
		                	});
		                	currVideoIndex=videoFiles.indexOf(fl);
		                	lblFile.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
		                	lblFile.setForeground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_FONT_COLOR)));
		                	lblFile.setOpaque(true);
		                	emp1.prepareMedia(fl.getAbsolutePath());
		                }
		            });
				}
			}
			
		} else {
			Image icon = Toolkit.getDefaultToolkit().getImage(Constants.VIDEOICON_PATH);
			JLabel lblFile = new JLabel();
			lblFile.setText(file.getName());
			videoFiles.add(file);
			lblFile.setBorder(BorderFactory.createCompoundBorder(
			   BorderFactory.createBevelBorder(0),
					      javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
					   ));
			lblFile.setIcon(new ImageIcon(icon));
			lblFile.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
			lblFile.setForeground(Color.WHITE);
			lblFile.setHorizontalAlignment(SwingUtilities.CENTER);
			lblFile.setVerticalAlignment(SwingUtilities.CENTER);
			lblFile.setOpaque(true);
			lblFile.setName(file.getName());
			imgPane.add(lblFile);
			imgPane.revalidate();
			imgPane.repaint();
			lblFile.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	Arrays.asList(imgPane.getComponents()).parallelStream().forEach(l -> {
                		l.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
            			l.setForeground(Color.WHITE);
            			((JLabel)l).setOpaque(true);
                	});
                	currVideoIndex=videoFiles.indexOf(file);
                	lblFile.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
                	lblFile.setOpaque(true);
                	emp1.prepareMedia(file.getAbsolutePath());
                }
            });
		}
		for(int f = videoFiles.size();f<12;f++) {
			JLabel lblFile = new JLabel();
			lblFile.setText("- No Content -");
			lblFile.setBorder(BorderFactory.createCompoundBorder(
					   BorderFactory.createBevelBorder(0),
							      javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
							   ));
			lblFile.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
			lblFile.setHorizontalAlignment(SwingUtilities.CENTER);
			lblFile.setVerticalAlignment(SwingUtilities.CENTER);
			lblFile.setForeground(Color.WHITE);
			lblFile.setOpaque(true);
			imgPane.add(lblFile);
		}
	}
	

}
