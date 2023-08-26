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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BAHAMenu extends JMenuBar {

	private static BAHAMenu obj;
	private JMenu verseMenu, songsMenu, announcementMenu, videoMenu, liveMenu, settingsMenu;
	private JMenu verseVersion, primaryLanguage, secondaryLanguage;
	private JMenuItem createSong, editSong, googleSync;
	public  JMenuItem kjv, nkjv, amp, niv;
	public static final Map<String, String> songsMap = new TreeMap<String, String>();
	UndoManager undoManager;
	// System.getProperty("user.dir") + "/src/main/resources/WorshipSongs.xlsx");
	Font font = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, 12);
	private String[] languages = new String[] { "English", "Hindi", "Kanada", "Malayalam" };

	private void init() {

		System.out.println("loading songs...");
		undoManager = new UndoManager();
		long startTime = System.currentTimeMillis();
		SwingWorker worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				File fl = new File(Constants.RESOURCES_PATH + "WorshipSongs.xlsx");
				try (FileInputStream inp = new FileInputStream(fl)) {
					XSSFSheet sheet = null;
					XSSFWorkbook workbook = new XSSFWorkbook(inp);
					sheet = workbook.getSheetAt(0);
					for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
						if (sheet.getRow(i) != null) {
							XSSFCell cell = sheet.getRow(i).getCell(0);
							XSSFCell cell1 = sheet.getRow(i).getCell(1);
							if (null == cell || null == cell1) {
								continue;
							}
							if (!cell.getStringCellValue().isEmpty() && !cell1.getStringCellValue().isEmpty()) {
								songsMap.put(cell.getStringCellValue().trim(),
										cell1.getStringCellValue().replaceFirst("\\s++$", ""));
							}
							if ((BAHAMenu.songsMap.keySet().size() % 500) == 0) {
								WorshipPannel.fcb.refresh(new ArrayList<String>(BAHAMenu.songsMap.keySet()));
							}
						}

					}
					WorshipPannel.fcb.refresh(new ArrayList<String>(BAHAMenu.songsMap.keySet()));

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				long endTime = System.currentTimeMillis();
				System.out.println("songs loaded...." + (endTime - startTime) + " ms");
				return null;
			}
		};
		worker.execute();

//			}
//		});

	}

	private BAHAMenu() {
		init();
		verseMenu = new JMenu("Verse");
		verseMenu.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND)));
		songsMenu = new JMenu("Worship");
		announcementMenu = new JMenu("Announcements");
		videoMenu = new JMenu("Video Player");
		liveMenu = new JMenu("Live Stream");
		settingsMenu = new JMenu("Settings");
		kjv = new JMenuItem("King James Version (KJV)");
		nkjv = new JMenuItem("New King James Version (NKJV)");
		amp = new JMenuItem("Amplified Bible (AMP)");
		niv = new JMenuItem("New International Version (NIV)");
		kjv.setBackground(Color.LIGHT_GRAY);
		nkjv.setBackground(Color.WHITE);
		amp.setBackground(Color.WHITE);
		niv.setBackground(Color.WHITE);
		kjv.setSelected(true);
		nkjv.setSelected(false);
		amp.setSelected(false);
		niv.setSelected(false);
		verseVersion = new JMenu("Versions");
		verseMenu.add(verseVersion);
		verseVersion.add(kjv);
		verseVersion.add(nkjv);
		verseVersion.add(amp);		
		verseVersion.add(niv);
		primaryLanguage = new JMenu("Primary Language");
		secondaryLanguage = new JMenu("Secondary Language");
		for(String lang:languages) {
			primaryLanguage.add(new JMenuItem(lang));
			secondaryLanguage.add(new JMenuItem(lang));
		}
		
		verseMenu.add(primaryLanguage);
		verseMenu.add(secondaryLanguage);
		
		createSong = new JMenuItem("Create Song");
		editSong = new JMenuItem("Edit Song");
		googleSync = new JMenuItem("Google Sync");
		songsMenu.add(createSong);
		songsMenu.add(editSong);
		songsMenu.add(googleSync);
		
		
		this.add(songsMenu);
		this.add(verseMenu);
		this.add(announcementMenu);
		this.add(videoMenu);
		this.add(liveMenu);
		this.add(settingsMenu);
		verseMenu.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				FrameDesigner.getFrame().remove(WorshipPannel.getWorship().getWorshipPanel());
				FrameDesigner.getFrame().remove(Announcement.getAnnouncement().getAnnouncementPanel());
				FrameDesigner.getFrame().remove(VlcPlayer.getVlcPlayer().getVideoPanel());
				FrameDesigner.getFrame().remove(GoLiveCamera.getGoLiveCamera().getGoLivePanel());  
				FrameDesigner.getFrame().remove(Settings.getSettings().getSettingsPanel());
				FrameDesigner.getFrame().add(VerseView.getVerseView().getVerseViewPanel());
				FrameDesigner.getFrame().repaint();
				FrameDesigner.getFrame().setVisible(true);
				
				verseMenu.setSelected(true);
				verseMenu.setBackground(Color.LIGHT_GRAY);
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub

			}
		});

		songsMenu.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				FrameDesigner.getFrame().remove(VerseView.getVerseView().getVerseViewPanel());
				FrameDesigner.getFrame().remove(Announcement.getAnnouncement().getAnnouncementPanel());
				FrameDesigner.getFrame().remove(VlcPlayer.getVlcPlayer().getVideoPanel());
				FrameDesigner.getFrame().remove(GoLiveCamera.getGoLiveCamera().getGoLivePanel());  
				FrameDesigner.getFrame().remove(Settings.getSettings().getSettingsPanel());
				FrameDesigner.getFrame().add(WorshipPannel.getWorship().getWorshipPanel());
				FrameDesigner.getFrame().repaint();
				FrameDesigner.getFrame().setVisible(true);
				songsMenu.setSelected(true);
				songsMenu.setBackground(Color.LIGHT_GRAY);
			}

			@Override
			public void menuDeselected(MenuEvent e) {

			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub

			}
		});

		announcementMenu.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				FrameDesigner.getFrame().remove(VerseView.getVerseView().getVerseViewPanel());
				FrameDesigner.getFrame().remove(WorshipPannel.getWorship().getWorshipPanel());
				FrameDesigner.getFrame().remove(VlcPlayer.getVlcPlayer().getVideoPanel());
				FrameDesigner.getFrame().remove(GoLiveCamera.getGoLiveCamera().getGoLivePanel());  
				FrameDesigner.getFrame().remove(Settings.getSettings().getSettingsPanel());
				FrameDesigner.getFrame().add(Announcement.getAnnouncement().getAnnouncementPanel());
				FrameDesigner.getFrame().repaint();
				FrameDesigner.getFrame().setVisible(true);
			}

			@Override
			public void menuDeselected(MenuEvent e) {

			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		videoMenu.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				FrameDesigner.getFrame().remove(VerseView.getVerseView().getVerseViewPanel());
				FrameDesigner.getFrame().remove(WorshipPannel.getWorship().getWorshipPanel());
				FrameDesigner.getFrame().remove(Announcement.getAnnouncement().getAnnouncementPanel());
				FrameDesigner.getFrame().remove(GoLiveCamera.getGoLiveCamera().getGoLivePanel());  
				FrameDesigner.getFrame().remove(Settings.getSettings().getSettingsPanel());
				FrameDesigner.getFrame().add(VlcPlayer.getVlcPlayer().getVideoPanel());
				FrameDesigner.getFrame().repaint();
				FrameDesigner.getFrame().setVisible(true);
			}

			@Override
			public void menuDeselected(MenuEvent e) {

			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		settingsMenu.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				FrameDesigner.getFrame().remove(VerseView.getVerseView().getVerseViewPanel());
				FrameDesigner.getFrame().remove(WorshipPannel.getWorship().getWorshipPanel());
				FrameDesigner.getFrame().remove(Announcement.getAnnouncement().getAnnouncementPanel());
				FrameDesigner.getFrame().remove(GoLiveCamera.getGoLiveCamera().getGoLivePanel());  
				FrameDesigner.getFrame().remove(VlcPlayer.getVlcPlayer().getVideoPanel());
				FrameDesigner.getFrame().add(Settings.getSettings().getSettingsPanel());
				FrameDesigner.getFrame().repaint();
				FrameDesigner.getFrame().setVisible(true);
			}

			@Override
			public void menuDeselected(MenuEvent e) {

			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		liveMenu.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				FrameDesigner.getFrame().remove(VerseView.getVerseView().getVerseViewPanel());
				FrameDesigner.getFrame().remove(WorshipPannel.getWorship().getWorshipPanel());
				FrameDesigner.getFrame().remove(Announcement.getAnnouncement().getAnnouncementPanel());
				FrameDesigner.getFrame().remove(VlcPlayer.getVlcPlayer().getVideoPanel());
				FrameDesigner.getFrame().remove(Settings.getSettings().getSettingsPanel());
				FrameDesigner.getFrame().add(GoLiveCamera.getGoLiveCamera().getGoLivePanel()); 
				FrameDesigner.getFrame().repaint();
				FrameDesigner.getFrame().setVisible(true);
			}

			@Override
			public void menuDeselected(MenuEvent e) {

			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		createSong.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame createSongFrame = new JFrame();
				JButton createSongButton = new JButton();
				JPanel createSongPanel = new JPanel();
				JTextField songTitle = new JTextField(38);
				songTitle.setFont(font);
				createSongPanel.add(new JLabel("Song Title:"));
				createSongPanel.add(Box.createHorizontalStrut(20));
				songTitle.setPreferredSize(new Dimension(100, 20));
				createSongPanel.add(songTitle);
				createSongPanel.add(Box.createHorizontalStrut(15));
				JTextPane verse = new JTextPane();
				verse.setEditable(true);
				verse.setFont(font);
				verse.getDocument().addUndoableEditListener(undoManager);
				verse.registerKeyboardAction(new AbstractAction() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							undoManager.undo();
						} catch (CannotUndoException e1) {
						}
						
					}
				}, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
				verse.registerKeyboardAction(new AbstractAction() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							undoManager.redo();
						} catch (CannotUndoException e1) {
						}
						
					}
				}, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
				JScrollPane scrollPane = new JScrollPane(verse);
				scrollPane.requestFocus();
				verse.requestFocusInWindow();
				scrollPane.setPreferredSize(new Dimension(500, 580));
				createSongPanel.add(scrollPane);
				createSongPanel.add(Box.createVerticalStrut(15));
				createSongButton.setText("Save");
				createSongPanel.add(createSongButton);
				createSongPanel.setLayout(new BoxLayout(createSongPanel, BoxLayout.Y_AXIS));
//	            JOptionPane.showMessageDialog(
//	            		createVerseFrame, createVersePanel);
				createSongFrame.add(createSongPanel);
				createSongFrame.setSize(800, 600);
				createSongFrame.setVisible(true);
				createSongFrame.setResizable(false);
				createSongButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						// File file = new File(System.getProperty("user.dir") +
						// "/src/main/resources/WorshipSongs.xlsx");
						XSSFWorkbook workbook = null;
						XSSFSheet sheet = null;
						File fl = new File(Constants.RESOURCES_PATH +  "WorshipSongs.xlsx");
						try(FileInputStream inp= new FileInputStream(fl)) {
							//FileInputStream inp = new FileInputStream(fl);
							workbook = new XSSFWorkbook(inp);
							sheet = workbook.getSheetAt(0);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						if (workbook == null) {
							workbook = new XSSFWorkbook();
							sheet = workbook.createSheet("Songs");
						}
						Map<String,String> googleSyncMap = new HashedMap<>();
						if (!songTitle.getText().trim().isEmpty() && !verse.getText().trim().isEmpty()) {
							songsMap.put((String) songTitle.getText().trim(),
									(String) verse.getText().replaceFirst("\\s++$", ""));
							
							googleSyncMap.put((String) songTitle.getText().trim(),
									(String) verse.getText().replaceFirst("\\s++$", ""));
							for (int i = 0; i < sheet.getLastRowNum() + 5; i++) {
								XSSFRow row1 = sheet.getRow(i);
								if (null == row1) {
									row1 = sheet.createRow(i);
									XSSFCell cell = row1.createCell(0);
									cell.setCellValue((String) songTitle.getText());
									XSSFCell cell1 = row1.createCell(1);
									cell1.setCellValue(performSongFormatting((String) verse.getText()));
									XSSFCell cell3 = row1.createCell(3);
									cell3.setCellValue(new Date());
									break;
								}
							}
							FileOutputStream outputStream = null;
							try {
								outputStream = new FileOutputStream(fl);
								workbook.write(outputStream);
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							} finally {
								if (null != outputStream) {
									try {
										outputStream.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
						createSongFrame.dispose();
						if(MapUtils.isNotEmpty(googleSyncMap)) {
							try {
								HttpUtil.postContent(googleSyncMap,"create");
							} catch (IOException | InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			}
		});

		editSong.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame createSongFrame = new JFrame();
				JButton editSongButton = new JButton();
				JPanel editSongPanel = new JPanel();
				JTextField songTitle = new JTextField(38);
				songTitle.setFont(font);
				editSongPanel.add(new JLabel("Song Title:"));
				editSongPanel.add(Box.createHorizontalStrut(20));
				songTitle.setPreferredSize(new Dimension(100, 20));
				editSongPanel.add(songTitle);
				JTextPane verse = new JTextPane();
				verse.setFont(font);
				verse.getDocument().addUndoableEditListener(undoManager);
				verse.registerKeyboardAction(new AbstractAction() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							undoManager.undo();
						} catch (CannotUndoException e1) {
						}
						
					}
				}, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
				verse.registerKeyboardAction(new AbstractAction() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							undoManager.redo();
						} catch (CannotUndoException e1) {
						}
						
					}
				}, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
				JComboBox combo = new JComboBox();

				songTitle.addKeyListener(new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {
						if (!songTitle.getText().trim().isEmpty()) {
							JComboBox comboOld = (JComboBox) editSongPanel.getComponent(3);
							comboOld.removeAllItems();
							 songsMap.keySet().parallelStream().filter(s -> s.toLowerCase().trim()
										.indexOf(songTitle.getText().toLowerCase().trim())>-1)
							 .findFirst().ifPresent(comboOld::addItem);
							 comboOld.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void keyPressed(KeyEvent e) {
						if (!songTitle.getText().trim().isEmpty() && !verse.getText().trim().isEmpty()) {
							JComboBox comboOld = (JComboBox) editSongPanel.getComponent(3);
							comboOld.removeAllItems();
							// editSongPanel.remove(comboOld);
							 songsMap.keySet().parallelStream().filter(s -> s.toLowerCase().trim()
										.indexOf(songTitle.getText().toLowerCase().trim())>-1)
							 .findFirst().ifPresent(s -> {
								 comboOld.addItem(s);
								 comboOld.setSelectedItem(s);
							 });
							comboOld.setVisible(true);
						}

					}
				});

				combo.setVisible(false);
				editSongPanel.add(combo);
				combo.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						// TODO Auto-generated method stub
						if (null != combo.getSelectedItem()) {
							verse.setText(songsMap.get(combo.getSelectedItem()));
							// songTitle.setText(String.valueOf(selectedComboItem1));
						} else {
							verse.setText("");
						}
					}
				});

				editSongPanel.add(Box.createHorizontalStrut(15));
				verse.setEditable(true);
				JScrollPane scrollPane = new JScrollPane(verse);
				scrollPane.requestFocus();
				verse.requestFocusInWindow();
				scrollPane.setPreferredSize(new Dimension(500, 580));
				editSongPanel.add(scrollPane);
				editSongPanel.add(Box.createVerticalStrut(15));
				editSongButton.setText("Save");
				editSongPanel.add(editSongButton);
				editSongPanel.setLayout(new BoxLayout(editSongPanel, BoxLayout.Y_AXIS));
//		            JOptionPane.showMessageDialog(
//		            		createVerseFrame, createVersePanel);
				createSongFrame.add(editSongPanel);
				createSongFrame.setSize(800, 600);
				createSongFrame.setVisible(true);
				createSongFrame.setResizable(false);
				editSongButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Map<String,String> googleSyncMap = new HashedMap<>();
						if (null != combo.getSelectedItem()) {
							// TODO Auto-generated method stub
							XSSFWorkbook workbook = null;
							XSSFSheet sheet = null;
							File fl = new File(Constants.RESOURCES_PATH +  "WorshipSongs.xlsx");
							try(FileInputStream inp = new FileInputStream(fl)) {
								workbook = new XSSFWorkbook(inp);
								sheet = workbook.getSheetAt(0);
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (workbook == null) {
								workbook = new XSSFWorkbook();
								sheet = workbook.createSheet("Songs");
							}
							String content = null;
							XSSFRow row = null;
							XSSFCell cell = null;
							content = performSongFormatting((String) verse.getText());
							songsMap.put(String.valueOf(combo.getSelectedItem()).trim(),
									content);
							googleSyncMap.put(String.valueOf(combo.getSelectedItem()).trim(),
									content);
							for (int i = 0; i < sheet.getLastRowNum() + 5; i++) {
								row = sheet.getRow(i);
								cell = row.getCell(0);
								if (null != cell 
										&& cell.getStringCellValue().trim().startsWith(String.valueOf(combo.getSelectedItem()).trim())
										&& cell.getStringCellValue().trim()
										.equalsIgnoreCase(String.valueOf(combo.getSelectedItem()).trim())) {
									cell.setCellValue(String.valueOf(combo.getSelectedItem()).trim());
									XSSFCell cell1 = row.getCell(1);
									cell1.setCellValue(content);
									XSSFCell cell4 = row.createCell(4);
									cell4.setCellValue(new Date());
									break;
								}
							}
							try(FileOutputStream outputStream = new FileOutputStream(fl)) {
								workbook.write(outputStream);
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							} 
							
						}
						createSongFrame.dispose();
						if(MapUtils.isNotEmpty(googleSyncMap)) {
							try {
								HttpUtil.postContent(googleSyncMap,"update");
							} catch (IOException | InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			}
		});
		
		
		
		googleSync.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						try {
							if(HttpUtil.checkInternetConnection()) {
								HttpUtil.syncGoogle();
							} else {
								JOptionPane.showMessageDialog(BahaStater.initFrame, "Internet Connection is down!");
							}
						} catch (IOException | InterruptedException e) {
							
						}
					}
				});
			}
		});
		
		kjv.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				kjv.setBackground(Color.LIGHT_GRAY);
				nkjv.setBackground(Color.WHITE);
				amp.setBackground(Color.WHITE);
				niv.setBackground(Color.WHITE);
				kjv.setSelected(true);
				nkjv.setSelected(false);
				amp.setSelected(false);
				niv.setSelected(false);
				// TODO Auto-generated method stub
				try {
					if (VerseView.getVerseView().books.getSelectedValue() == null || VerseView.getVerseView().chapters.getSelectedValue() == null
							|| VerseView.getVerseView().verses.getSelectedValue() == null)
						return;
					BibleResponseDTO bibleResponseDTO = HttpUtil.getOnlineVerseForTranslation("kjv",VerseView.getVerseView().books.getSelectedValue(),
							""+(VerseView.getVerseView().books.getSelectedIndex()+1),""+(VerseView.getVerseView().chapters.getSelectedIndex()+1),
							""+(VerseView.getVerseView().verses.getSelectedIndex()+1));
					Map<String, Map<String, String>> primaryCache = VerseView.getVerseView().primaryCache;
					if(MapUtils.isNotEmpty(primaryCache)) {
						Map<String, String> secondaryCache = primaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue());
						if(MapUtils.isNotEmpty(secondaryCache) && Objects.nonNull(bibleResponseDTO)) {
							String hindi = secondaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue()).split("\\#")[1];
							
							secondaryCache.put(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue(), bibleResponseDTO.getText()+"#"+hindi);
							
							System.out.println(secondaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue()));
							VerseView.getVerseView().setContentText(VerseView.getVerseView().books, 
									VerseView.getVerseView().chapters, 
									VerseView.getVerseView().verses);
							
						}
					}
				} catch (IOException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				} catch (InterruptedException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				}
			}
		});

		nkjv.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					kjv.setBackground(Color.WHITE);
					nkjv.setBackground(Color.LIGHT_GRAY);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(false);
					nkjv.setSelected(true);
					amp.setSelected(false);
					niv.setSelected(false);
					if (VerseView.getVerseView().books.getSelectedValue() == null || VerseView.getVerseView().chapters.getSelectedValue() == null
							|| VerseView.getVerseView().verses.getSelectedValue() == null)
						return;
					BibleResponseDTO bibleResponseDTO = HttpUtil.getOnlineVerseForTranslation("nkjv",VerseView.getVerseView().books.getSelectedValue(),
							""+(VerseView.getVerseView().books.getSelectedIndex()+1),""+(VerseView.getVerseView().chapters.getSelectedIndex()+1),
							""+(VerseView.getVerseView().verses.getSelectedIndex()+1));
					Map<String, Map<String, String>> primaryCache = VerseView.getVerseView().primaryCache;
					if(MapUtils.isNotEmpty(primaryCache)) {
						Map<String, String> secondaryCache = primaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue());
						if(MapUtils.isNotEmpty(secondaryCache) && Objects.nonNull(bibleResponseDTO)) {
							String hindi = secondaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue()).split("\\#")[1];
							
							secondaryCache.put(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue(), bibleResponseDTO.getText()+"#"+hindi);
							VerseView.getVerseView().setContentText(VerseView.getVerseView().books, 
									VerseView.getVerseView().chapters, 
									VerseView.getVerseView().verses);
							
						}
					}
				} catch (IOException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				} catch (InterruptedException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				}
			}
		});
		
		amp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					kjv.setBackground(Color.WHITE);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.LIGHT_GRAY);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(false);
					nkjv.setSelected(false);
					amp.setSelected(true);
					niv.setSelected(false);
					if (VerseView.getVerseView().books.getSelectedValue() == null || VerseView.getVerseView().chapters.getSelectedValue() == null
							|| VerseView.getVerseView().verses.getSelectedValue() == null)
						return;
					BibleResponseDTO bibleResponseDTO = HttpUtil.getOnlineVerseForTranslation("amp",VerseView.getVerseView().books.getSelectedValue(),
							""+(VerseView.getVerseView().books.getSelectedIndex()+1),""+(VerseView.getVerseView().chapters.getSelectedIndex()+1),
							""+(VerseView.getVerseView().verses.getSelectedIndex()+1));
					Map<String, Map<String, String>> primaryCache = VerseView.getVerseView().primaryCache;
					if(MapUtils.isNotEmpty(primaryCache)) {
						Map<String, String> secondaryCache = primaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue());
						if(MapUtils.isNotEmpty(secondaryCache) && Objects.nonNull(bibleResponseDTO)) {
							String hindi = secondaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue()).split("\\#")[1];
							
							secondaryCache.put(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue(), bibleResponseDTO.getText()+"#"+hindi);
							VerseView.getVerseView().setContentText(VerseView.getVerseView().books, 
									VerseView.getVerseView().chapters, 
									VerseView.getVerseView().verses);
							
						}
					}
				} catch (IOException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				} catch (InterruptedException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				}
			}
		});
		
		niv.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					kjv.setBackground(Color.WHITE);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.LIGHT_GRAY);
					kjv.setSelected(false);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(true);
					if (VerseView.getVerseView().books.getSelectedValue() == null || VerseView.getVerseView().chapters.getSelectedValue() == null
							|| VerseView.getVerseView().verses.getSelectedValue() == null)
						return;
					BibleResponseDTO bibleResponseDTO = HttpUtil.getOnlineVerseForTranslation("niv",VerseView.getVerseView().books.getSelectedValue(),
							""+(VerseView.getVerseView().books.getSelectedIndex()+1),""+(VerseView.getVerseView().chapters.getSelectedIndex()+1),
							""+(VerseView.getVerseView().verses.getSelectedIndex()+1));
					Map<String, Map<String, String>> primaryCache = VerseView.getVerseView().primaryCache;
					if(MapUtils.isNotEmpty(primaryCache)) {
						Map<String, String> secondaryCache = primaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue());
						if(MapUtils.isNotEmpty(secondaryCache) && Objects.nonNull(bibleResponseDTO)) {
							String hindi = secondaryCache.get(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue()).split("\\#")[1];
							
							secondaryCache.put(VerseView.getVerseView().books.getSelectedValue() + "-" + VerseView.getVerseView().chapters.getSelectedValue() + "-"
									+ VerseView.getVerseView().verses.getSelectedValue(), bibleResponseDTO.getText()+"#"+hindi);
							VerseView.getVerseView().setContentText(VerseView.getVerseView().books, 
									VerseView.getVerseView().chapters, 
									VerseView.getVerseView().verses);
							
						}
					}
				} catch (IOException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				} catch (InterruptedException e1) {
					kjv.setBackground(Color.LIGHT_GRAY);
					nkjv.setBackground(Color.WHITE);
					amp.setBackground(Color.WHITE);
					niv.setBackground(Color.WHITE);
					kjv.setSelected(true);
					nkjv.setSelected(false);
					amp.setSelected(false);
					niv.setSelected(false);
				}
			}
		});
	}

	public static BAHAMenu getMenu() {
		if (obj == null) {
			synchronized (BAHAMenu.class) {
				if (obj == null) {
					obj = new BAHAMenu();// instance will be created at request time
				}
			}
		}
		return obj;
	}

	public static String performSongFormatting(String song) {
		String formattedSong = "";
		if (null != song && song.length() > 0) {
			song = song.replaceAll("\r\n", "\n");
			song = song.replaceAll("\n\n\n", "\n\n");
			String[] paragraphs = song.split("\n\n");
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < paragraphs.length; i++) {
				if (null != paragraphs[i] && paragraphs[i].trim().length() == 0) {
					paragraphs[i] = "";
					song = song.replaceAll("\n\n\n", "\n\n");
					continue;
				}
				String[] lines = paragraphs[i].split("\n");
				StringBuilder subBuilder = new StringBuilder();
				if (null != lines && lines.length > 0) {
					int firstLineLen = lines[0].trim().length();
					int len = firstLineLen;
					String firstLine = lines[0].trim();
					for (int l = 1; l < lines.length; l++) {
						if (lines[l].trim().length() > len) {
							len = lines[l].length();
						}
						subBuilder.append(lines[l] + "\n");
					}
					for (int k = 0; k < ((len - firstLineLen) + Math.round(len / 2)); k++) {
						firstLine = firstLine + " ";
					}
					subBuilder.insert(0, firstLine + "\n");
					builder.append(subBuilder);
				}
				builder.append("\n\n");
			}
			formattedSong = builder.toString().replaceAll("\n\n\n", "\n\n");
			formattedSong = formattedSong.replaceAll("\n", "\r\n");
		}
		return formattedSong;
	}

}
