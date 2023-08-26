package main.java.baha;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

public class Settings {
	private JPanel displayPanel;
	private static Settings obj;
	JTable table;
	private static String exportPath = Constants.RESOURCES_PATH;

	public Settings() {
		displayPanel = new JPanel();

		displayPanel.setLayout(new GridLayout(0, 6, 10, 10));

		final String mainBackColor = BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND);
//		if(null != mainBackColor)
		displayPanel.setBackground(Color.decode(mainBackColor));

		final String boxBackColor = BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND);

//		if(null != BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)) {
//			gridPan1.setBackground(Color.decode(boxBackColor));
//			gridPan2.setBackground(Color.decode(boxBackColor));
//		}

		JPanel gridPan1 = new JPanel();
		gridPan1.setLayout(new BoxLayout(gridPan1, BoxLayout.Y_AXIS));
		JLabel mainBackgroundLabel = new JLabel("Main Screen Background      ");
		gridPan1.add(mainBackgroundLabel);
		JButton mainBackgroundBtn = new JButton("Change");

		mainBackgroundBtn.setBackground(Color.decode(mainBackColor));
		mainBackgroundBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(mainBackgroundBtn, "Select a color",
						Color.decode(mainBackColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					mainBackgroundBtn.setBackground(color);
					setProperty(Constants.MAIN_BACKGROUND, temp);
				}
			}
		});
		gridPan1.add(mainBackgroundBtn);
		displayPanel.add(gridPan1);

		JPanel gridPan2 = new JPanel();
		gridPan2.setLayout(new BoxLayout(gridPan2, BoxLayout.Y_AXIS));
		JLabel boxBackgroundLabel = new JLabel("Box Background       ");
		gridPan2.add(boxBackgroundLabel);
		JButton boxBackgroundBtn = new JButton("Change");
		boxBackgroundBtn.setBackground(Color.decode(boxBackColor));
		boxBackgroundBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(boxBackgroundBtn, "Select a color", Color.decode(boxBackColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					boxBackgroundBtn.setBackground(color);
					setProperty(Constants.BLOCK_BACKGROUND, temp);
				}
			}
		});
		gridPan2.add(boxBackgroundBtn);
		displayPanel.add(gridPan2);

		final String selectedBackColor = BahaStater.properties.getProperty(Constants.SELECTED_BACKGROUND);
		JPanel gridPan3 = new JPanel();
		gridPan3.setLayout(new BoxLayout(gridPan3, BoxLayout.Y_AXIS));
		JLabel selectedBackgroundLabel = new JLabel("Selected Background       ");
		gridPan3.add(selectedBackgroundLabel);
		JButton selectedBackgroundBtn = new JButton("Change");
		selectedBackgroundBtn.setBackground(Color.decode(selectedBackColor));
		selectedBackgroundBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(selectedBackgroundBtn, "Select a color",
						Color.decode(selectedBackColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					selectedBackgroundBtn.setBackground(color);
					setProperty(Constants.SELECTED_BACKGROUND, temp);
				}
			}
		});
		gridPan3.add(selectedBackgroundBtn);
		displayPanel.add(gridPan3);

		final String normalFontColor = BahaStater.properties.getProperty(Constants.NORMAL_FONT_COLOR);
		JPanel gridPan4 = new JPanel();
		gridPan4.setLayout(new BoxLayout(gridPan4, BoxLayout.Y_AXIS));
		JLabel fontColorLabel = new JLabel("Font Colour       ");
		gridPan4.add(fontColorLabel);
		JButton fontColorBtn = new JButton("Change");
		fontColorBtn.setBackground(Color.decode(normalFontColor));
		fontColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(fontColorBtn, "Select a color", Color.decode(normalFontColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					fontColorBtn.setBackground(color);
					setProperty(Constants.NORMAL_FONT_COLOR, temp);
				}
			}
		});
		gridPan4.add(fontColorBtn);
		displayPanel.add(gridPan4);

		final String selectedFontColor = BahaStater.properties.getProperty(Constants.SELECTED_FONT_COLOR);
		JPanel gridPan5 = new JPanel();
		gridPan5.setLayout(new BoxLayout(gridPan5, BoxLayout.Y_AXIS));
		JLabel selectedFontColorLabel = new JLabel("Selected Font Colour       ");
		gridPan5.add(selectedFontColorLabel);
		JButton selectedFontColorBtn = new JButton("Change");
		selectedFontColorBtn.setBackground(Color.decode(selectedFontColor));
		selectedFontColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(selectedFontColorBtn, "Select a color",
						Color.decode(selectedFontColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					selectedFontColorBtn.setBackground(color);
					setProperty(Constants.SELECTED_FONT_COLOR, temp);
				}
			}
		});
		gridPan5.add(selectedFontColorBtn);
		displayPanel.add(gridPan5);
		
		final String projectorBgColor = BahaStater.properties.getProperty(Constants.PROJECTOR_BACKGROUND);
		JPanel gridPan6 = new JPanel();
		gridPan6.setLayout(new BoxLayout(gridPan6, BoxLayout.Y_AXIS));
		JLabel projectoBgColorLabel = new JLabel("Projector BG Colour       ");
		gridPan6.add(projectoBgColorLabel);
		JButton projectoBgColorBtn = new JButton("Change");
		Color tmpbg = Color.decode(projectorBgColor);
		projectoBgColorBtn.setBackground(new Color(tmpbg.getRed(), tmpbg.getGreen(), tmpbg.getBlue(), 180));
		projectoBgColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(projectoBgColorBtn, "Select a color",
						Color.decode(projectorBgColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					projectoBgColorBtn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
					setProperty(Constants.PROJECTOR_BACKGROUND, temp);
				}
			}
		});
		gridPan6.add(projectoBgColorBtn);
		displayPanel.add(gridPan6);
		
		

		final String hindiColor = BahaStater.properties.getProperty(Constants.HINDI_FONT_COLOR);
		JPanel gridPan7 = new JPanel();
		gridPan7.setLayout(new BoxLayout(gridPan7, BoxLayout.Y_AXIS));
		JLabel hindiColorLabel = new JLabel("Hindi Font Colour       ");
		gridPan7.add(hindiColorLabel);
		JButton hindiColorBtn = new JButton("Change");
		hindiColorBtn.setBackground(Color.decode(hindiColor));
		hindiColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(hindiColorBtn, "Select a color", Color.decode(hindiColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					hindiColorBtn.setBackground(color);
					setProperty(Constants.HINDI_FONT_COLOR, temp);
				}
			}
		});
		gridPan7.add(hindiColorBtn);
		displayPanel.add(gridPan7);

		final String englishColor = BahaStater.properties.getProperty(Constants.ENGLISH_FONT_COLOR);
		JPanel gridPan8 = new JPanel();
		gridPan8.setLayout(new BoxLayout(gridPan8, BoxLayout.Y_AXIS));
		JLabel englishColorLabel = new JLabel("English Font Colour       ");
		gridPan8.add(englishColorLabel);
		JButton englishColorBtn = new JButton("Change");
		englishColorBtn.setBackground(Color.decode(englishColor));
		englishColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(englishColorBtn, "Select a color", Color.decode(englishColor));
				if (null != color) {
					final String temp = "#" + Integer.toHexString(color.getRGB()).substring(2);
					englishColorBtn.setBackground(color);
					setProperty(Constants.ENGLISH_FONT_COLOR, temp);
				}
			}
		});
		gridPan8.add(englishColorBtn);
		displayPanel.add(gridPan8);

		final String worshipFontSz = BahaStater.properties.getProperty(Constants.WORSHIP_FONT_SIZE);
		JPanel gridPan9 = new JPanel();
//		gridPan9.setLayout(new BoxLayout(gridPan9, BoxLayout.Y_AXIS));
		JLabel worshipFontSzLabel = new JLabel("Worship Font Size       ");
		gridPan9.add(worshipFontSzLabel);
		JTextField worshipFontSzBtn = new JTextField(worshipFontSz);

		worshipFontSzBtn.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				setProperty(Constants.WORSHIP_FONT_SIZE, worshipFontSzBtn.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setProperty(Constants.WORSHIP_FONT_SIZE, worshipFontSzBtn.getText());

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setProperty(Constants.WORSHIP_FONT_SIZE, worshipFontSzBtn.getText());

			}
		});
		gridPan9.add(worshipFontSzBtn);
		displayPanel.add(gridPan9);

		JPanel gridPan10 = new JPanel();
		gridPan10.setLayout(new BoxLayout(gridPan10, BoxLayout.Y_AXIS));
		JLabel exportLabel = new JLabel(" Export Songs ");
		gridPan10.add(exportLabel);
		JButton exportSongsBtn = new JButton("Export to Microsoft Word");
		exportSongsBtn.setBackground(Color.LIGHT_GRAY);
		exportSongsBtn.setForeground(Color.BLACK);
		exportSongsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog(BahaStater.initFrame, "Songs", true);
				InsertJTable(dialog);
			}
		});
		gridPan10.add(exportSongsBtn);
		gridPan10.add(new JLabel("\n\n"));
		JButton exportSongsBtnpdf = new JButton("Export to pdf");
		exportSongsBtnpdf.setBackground(Color.LIGHT_GRAY);
		exportSongsBtnpdf.setForeground(Color.BLACK);
		exportSongsBtnpdf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog(BahaStater.initFrame, "Songs", true);
				InsertJTable(dialog);
			}
		});
		gridPan10.add(exportSongsBtnpdf);
		displayPanel.add(gridPan10);
		
		final String projectorBGTransparent = BahaStater.properties.getProperty(Constants.PROJECTOR_BACKGROUND_TRANSPARENT);
		JPanel gridPan11 = new JPanel();
//		gridPan9.setLayout(new BoxLayout(gridPan9, BoxLayout.Y_AXIS));
		JLabel projectorBGTransparentLabel = new JLabel("Projector Background Transperency      ");
		gridPan11.add(projectorBGTransparentLabel);
		JTextField projectorBGTransparentBtn = new JTextField(projectorBGTransparent);

		projectorBGTransparentBtn.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				setProperty(Constants.PROJECTOR_BACKGROUND_TRANSPARENT, projectorBGTransparentBtn.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setProperty(Constants.PROJECTOR_BACKGROUND_TRANSPARENT, projectorBGTransparentBtn.getText());

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setProperty(Constants.PROJECTOR_BACKGROUND_TRANSPARENT, projectorBGTransparentBtn.getText());

			}
		});
		gridPan11.add(projectorBGTransparentBtn);
		displayPanel.add(gridPan11);

	}

	public static Settings getSettings() {
		if (obj == null) {
			synchronized (Settings.class) {
				if (obj == null) {
					obj = new Settings();// instance will be created at request time
				}
			}
		}
		return obj;
	}

	public JPanel getSettingsPanel() {
		return displayPanel;
	}

	private void setProperty(String propName, String propValue) {
		BahaStater.properties.setProperty(propName, propValue);
		try (OutputStream output = new FileOutputStream(Constants.PROPERTIES_PATH)) {
			BahaStater.properties.store(output, null);
		} catch (IOException e2) {

		}
		BahaStater.loadProperties();
	}

	public void InsertJTable(JDialog dialog) {
		JPanel panel = new JPanel();
		// { { "Angelina", "Mumbai" }, { "Martina", "Delhi" } };
		Object[][] data = BAHAMenu.songsMap.entrySet().stream().map(e -> new Object[] { e.getKey(), e.getValue() })
				.toArray(Object[][]::new);
		String col[] = { "Title", "Lyrics" };
		DefaultTableModel model = new DefaultTableModel(data, col);
		table = new JTable(model);
		JScrollPane pane = new JScrollPane(table);
		JButton button = new JButton("Submit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
//					StringBuilder sb = new StringBuilder();
//					StringBuilder sb1 = new StringBuilder();
//					for(Map.Entry entry:BAHAMenu.songsMap.entrySet()) {
//						sb.append(String.valueOf(entry.getKey())+"\n---------------------------------------------------\n"+String.valueOf(entry.getValue())+"\n=End=\n");
//						sb1.append(String.valueOf(entry.getKey())+"\n---------------------------------------------------\n"+String.valueOf(entry.getValue())+"\n=End=\n");
//						System.out.println("----"+entry.getKey());
//					}
//					if(sb.length()>0)
					writeToFile();
				} catch (Exception e) {
				} finally {
					dialog.dispose();
				}
			}
		});
		panel.add(pane);
		panel.add(button);
		dialog.add(panel);
		dialog.setSize(new Dimension(Double.valueOf(BahaStater.initFrame.getWidth() * .40).intValue(),
				(Double.valueOf(BahaStater.initFrame.getHeight() * .60).intValue())));
		dialog.setVisible(true);
	}

	public Object GetData(JTable table, int row_index, int col_index) {
		return table.getModel().getValueAt(row_index, col_index);
	}

	private static void writeToFile() {
//		File file = new File(exportPath);
		XWPFDocument document = new XWPFDocument();
		try (FileOutputStream fileOut = new FileOutputStream(exportPath+"Worship_Songs.docx")) {
			int count = 0;
			XWPFParagraph paragraph1st = document.createParagraph();
			paragraph1st.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun run1st = paragraph1st.createRun();
			run1st.setBold(true);
			run1st.setCapitalized(true);
			run1st.setFontSize(16);
			run1st.setText("Worship Songs");
			run1st.addBreak();
			File image = new File(BahaStater.properties.getProperty(Constants.LOGO_PATH));
			try (FileInputStream imageData = new FileInputStream(image)) {
				// Step 5: Retrieving the image file name and image
				// type
				int imageType = XWPFDocument.PICTURE_TYPE_JPEG;
				String imageFileName = image.getName();

				// Step 6: Setting the width and height of the image
				// in pixels.
				int width = 200;
				int height = 200;

				// Step 7: Adding the picture using the addPicture()
				// method and writing into the document
				run1st.addPicture(imageData, imageType, imageFileName, Units.toEMU(width), Units.toEMU(height));
			} catch (Exception e) {

			}
			CTP ctP = paragraph1st.getCTP();
			CTSimpleField ctSimpleField = ctP.addNewFldSimple();
			ctSimpleField.setInstr("TOC \\o \"1-3\" \\h \\z \\u");
			int paraCol = 100;
			for (Map.Entry entry : BAHAMenu.songsMap.entrySet()) {
				int temp = ++count;

				XWPFParagraph paragraph = document.createParagraph();
				paragraph.setAlignment(ParagraphAlignment.CENTER);
				paragraph.setStyle("Heading 1");

				
				ctSimpleField.addNewR().addNewT().setStringValue((temp) + ". " + String.valueOf(entry.getKey()));
//				toc.setDirty(STOnOff.TRUE);

				XWPFRun run = paragraph.createRun();
				run.addBreak(BreakType.PAGE);
				run.setBold(true);
				run.setCapitalized(true);
				run.setFontSize(16);
//				run.setUnderline(UnderlinePatterns.THICK);
				run.setText((temp) + ". " + String.valueOf(entry.getKey()));
				run.addBreak();
				addCustomHeadingStyle(document, "Heading 1", 1);
				String val = String.valueOf(entry.getValue());
				val = val.replaceAll("\r\n", "\n");

				String[] paras = null;
				if (val.indexOf("==") > -1)
					paras = val.trim().split("\n==\n");
				else
					paras = val.trim().split("\n\n");
				XWPFTable table = document.createTable();
				table.setWidth("100%");
				table.removeBorders();
				table.getCTTbl().getTblPr().addNewTblCellSpacing()
						.setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth.DXA);
				table.getCTTbl().getTblPr().getTblCellSpacing().setW(java.math.BigInteger.valueOf(60));
				for (int i = 0; i < paras.length; i++) {
					if (paras[i].trim().length() > 0) {
//						System.out.println(paras[i]);
						String[] lines = paras[i].trim().split("\n");
						if (lines.length > 0) {
							StringBuffer sbH = new StringBuffer();
							StringBuffer sbE = new StringBuffer();
							for (int l = 0; l < lines.length; l++) {
								String line = lines[l];
								if (line.trim().length() > 0) {
									if ((line).trim().matches(".*[a-z].*")) {
										sbE.append(line.trim() + "=0=");
									} else {
										sbH.append(line.trim() + "=0=");
									}

								}
							}

							if (sbH.length()>0 && sbE.length()>0) {
								XWPFTableRow row = table.createRow();
								XWPFTableCell cell1 = row.getCell(0);
								XWPFParagraph p1 = cell1.addParagraph();
								XWPFRun run1 = p1.createRun();
								p1.setWordWrapped(true);
								Arrays.asList(sbH.toString().split("=0=")).stream().forEach(line -> {
									run1.setText(line);
									run1.addBreak();
								});

								cell1.setWidth("50%");
								cell1.setParagraph(p1);

								XWPFTableCell cell2 = row.addNewTableCell();
								XWPFParagraph p2 = cell2.addParagraph();
								XWPFRun run2 = p2.createRun();
								run2.setFontSize(12);
//								run2.setCapitalized(true);
								p2.setWordWrapped(true);
								Arrays.asList(sbE.toString().split("=0=")).stream().forEach(line -> {
									run2.setText(line);
									run2.addBreak();
								});
								cell2.setWidth("50%");

								cell2.setParagraph(p2);
								paraCol = 50;
							} else if (sbH.length()>0) {
								XWPFTableRow row = table.createRow();
								XWPFTableCell cell1 = row.getCell(0);
								XWPFParagraph p1 = cell1.addParagraph();
								XWPFRun run1 = p1.createRun();
								Arrays.asList(sbH.toString().split("=0=")).stream().forEach(line -> {
									run1.setText(line);
									run1.addBreak();
								});
								p1.setWordWrapped(true);
								if (paraCol != 50)
									cell1.setWidth("100%");
								cell1.setParagraph(p1);
							} else if (sbE.length()>0) {
								XWPFTableRow row = table.createRow();
								XWPFTableCell cell1 = row.getCell(0);
								XWPFParagraph p1 = cell1.addParagraph();
								XWPFRun run1 = p1.createRun();
//								run1.setCapitalized(true);
								run1.setFontSize(12);
								p1.setWordWrapped(true);
								Arrays.asList(sbE.toString().split("=0=")).stream().forEach(line -> {
									run1.setText(line);
									run1.addBreak();
								});
								if (paraCol != 50)
									cell1.setWidth("100%");
								cell1.setParagraph(p1);
							}
						}
					}
				}
				table.removeRow(0);
			}
			document.removeProtectionEnforcement();
			document.enforceUpdateFields();
			document.write(fileOut);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

		CTStyle ctStyle = CTStyle.Factory.newInstance();
		ctStyle.setStyleId(strStyleId);

		CTString styleName = CTString.Factory.newInstance();
		styleName.setVal(strStyleId);
		ctStyle.setName(styleName);

		CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
		indentNumber.setVal(BigInteger.valueOf(headingLevel));

		// lower number > style is more prominent in the formats bar
		ctStyle.setUiPriority(indentNumber);

		CTOnOff onoffnull = CTOnOff.Factory.newInstance();
		ctStyle.setUnhideWhenUsed(onoffnull);

		// style shows up in the formats bar
		ctStyle.setQFormat(onoffnull);

		// style defines a heading of the given level
		CTPPrGeneral ppr = CTPPrGeneral.Factory.newInstance();
		ppr.setOutlineLvl(indentNumber);
		ctStyle.setPPr(ppr);

		XWPFStyle style = new XWPFStyle(ctStyle);

		// is a null op if already defined
		XWPFStyles styles = docxDocument.createStyles();

		style.setType(STStyleType.PARAGRAPH);
		styles.addStyle(style);

	}
}
