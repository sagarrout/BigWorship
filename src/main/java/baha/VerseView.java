package main.java.baha;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VerseView {
	private final JPanel displayPanel;
	Font dFont = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, 16);
	Font font1 = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, 14);
	Font btnFont = new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, 13);
	private final Map<String, Integer> bookMap = new LinkedHashMap<String, Integer>();
	private final Map<String, String> bookMapHindi = new LinkedHashMap<String, String>();
	private final Map<String, Integer> chapterMap = new LinkedHashMap<String, Integer>();
	JPanel wordsPanel = new JPanel();
	protected File file;
	Map<String, Map<String, String>> primaryCache = new HashMap<String, Map<String, String>>();
	private static VerseView obj;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private String hindiFontColor = BahaStater.properties.getProperty(Constants.HINDI_FONT_COLOR);
	private String englishFontColor = BahaStater.properties.getProperty(Constants.ENGLISH_FONT_COLOR);
	private Map<String, String> mruCache;
	JPanel booksPane;
	JPanel panelMru = new JPanel();
	XSSFWorkbook workbook = null;
	XSSFSheet sheet = null;
	final JList<String> books;
	final JList<String> chapters;
	final JList<String> verses;

	public VerseView() {
		mruCache = new LinkedHashMap<String, String>(10, 0.75f, false) {
			protected boolean removeEldestEntry(Map.Entry eldest) {
				return size() > 50;
			}
		};
		if (null == hindiFontColor)
			hindiFontColor = "#FFFFFF";
		if (null == englishFontColor)
			englishFontColor = "#FFFFFF";// "#FFFF99
		this.displayPanel = new JPanel();
		displayPanel.setBackground(Color.WHITE);
		final DefaultListModel<String> bookList = new DefaultListModel<>();
		bookMap.put(Constants.GENESIS, 50);
		bookMapHindi.put(Constants.GENESIS, BahaStater.properties.getProperty(Constants.GENESIS));
		chapterMap.put(Constants.GENESIS + "-1", 31);
		chapterMap.put(Constants.GENESIS + "-2", 25);
		chapterMap.put(Constants.GENESIS + "-3", 24);
		chapterMap.put(Constants.GENESIS + "-4", 26);
		chapterMap.put(Constants.GENESIS + "-5", 32);
		chapterMap.put(Constants.GENESIS + "-6", 22);
		chapterMap.put(Constants.GENESIS + "-7", 24);
		chapterMap.put(Constants.GENESIS + "-8", 22);
		chapterMap.put(Constants.GENESIS + "-9", 29);
		chapterMap.put(Constants.GENESIS + "-10", 32);
		chapterMap.put(Constants.GENESIS + "-11", 32);
		chapterMap.put(Constants.GENESIS + "-12", 20);
		chapterMap.put(Constants.GENESIS + "-13", 18);
		chapterMap.put(Constants.GENESIS + "-14", 24);
		chapterMap.put(Constants.GENESIS + "-15", 21);
		chapterMap.put(Constants.GENESIS + "-16", 16);
		chapterMap.put(Constants.GENESIS + "-17", 27);
		chapterMap.put(Constants.GENESIS + "-18", 33);
		chapterMap.put(Constants.GENESIS + "-19", 38);
		chapterMap.put(Constants.GENESIS + "-20", 18);
		chapterMap.put(Constants.GENESIS + "-21", 34);
		chapterMap.put(Constants.GENESIS + "-22", 24);
		chapterMap.put(Constants.GENESIS + "-23", 20);
		chapterMap.put(Constants.GENESIS + "-24", 67);
		chapterMap.put(Constants.GENESIS + "-25", 34);
		chapterMap.put(Constants.GENESIS + "-26", 35);
		chapterMap.put(Constants.GENESIS + "-27", 46);
		chapterMap.put(Constants.GENESIS + "-28", 22);
		chapterMap.put(Constants.GENESIS + "-29", 35);
		chapterMap.put(Constants.GENESIS + "-30", 43);
		chapterMap.put(Constants.GENESIS + "-31", 55);
		chapterMap.put(Constants.GENESIS + "-32", 32);
		chapterMap.put(Constants.GENESIS + "-33", 20);
		chapterMap.put(Constants.GENESIS + "-34", 31);
		chapterMap.put(Constants.GENESIS + "-35", 29);
		chapterMap.put(Constants.GENESIS + "-36", 43);
		chapterMap.put(Constants.GENESIS + "-37", 36);
		chapterMap.put(Constants.GENESIS + "-38", 30);
		chapterMap.put(Constants.GENESIS + "-39", 23);
		chapterMap.put(Constants.GENESIS + "-40", 23);
		chapterMap.put(Constants.GENESIS + "-41", 57);
		chapterMap.put(Constants.GENESIS + "-42", 38);
		chapterMap.put(Constants.GENESIS + "-43", 34);
		chapterMap.put(Constants.GENESIS + "-44", 34);
		chapterMap.put(Constants.GENESIS + "-45", 28);
		chapterMap.put(Constants.GENESIS + "-46", 34);
		chapterMap.put(Constants.GENESIS + "-47", 31);
		chapterMap.put(Constants.GENESIS + "-48", 22);
		chapterMap.put(Constants.GENESIS + "-49", 33);
		chapterMap.put(Constants.GENESIS + "-50", 26);

		bookMap.put(Constants.EXODUS, 40);
		bookMapHindi.put(Constants.EXODUS, BahaStater.properties.getProperty(Constants.EXODUS));
		chapterMap.put(Constants.EXODUS + "-1", 22);
		chapterMap.put(Constants.EXODUS + "-2", 25);
		chapterMap.put(Constants.EXODUS + "-3", 22);
		chapterMap.put(Constants.EXODUS + "-4", 31);
		chapterMap.put(Constants.EXODUS + "-5", 23);
		chapterMap.put(Constants.EXODUS + "-6", 30);
		chapterMap.put(Constants.EXODUS + "-7", 25);
		chapterMap.put(Constants.EXODUS + "-8", 32);
		chapterMap.put(Constants.EXODUS + "-9", 35);
		chapterMap.put(Constants.EXODUS + "-10", 29);
		chapterMap.put(Constants.EXODUS + "-11", 10);
		chapterMap.put(Constants.EXODUS + "-12", 51);
		chapterMap.put(Constants.EXODUS + "-13", 22);
		chapterMap.put(Constants.EXODUS + "-14", 31);
		chapterMap.put(Constants.EXODUS + "-15", 27);
		chapterMap.put(Constants.EXODUS + "-16", 36);
		chapterMap.put(Constants.EXODUS + "-17", 16);
		chapterMap.put(Constants.EXODUS + "-18", 27);
		chapterMap.put(Constants.EXODUS + "-19", 25);
		chapterMap.put(Constants.EXODUS + "-20", 26);
		chapterMap.put(Constants.EXODUS + "-21", 36);
		chapterMap.put(Constants.EXODUS + "-22", 31);
		chapterMap.put(Constants.EXODUS + "-23", 33);
		chapterMap.put(Constants.EXODUS + "-24", 18);
		chapterMap.put(Constants.EXODUS + "-25", 40);
		chapterMap.put(Constants.EXODUS + "-26", 37);
		chapterMap.put(Constants.EXODUS + "-27", 21);
		chapterMap.put(Constants.EXODUS + "-28", 43);
		chapterMap.put(Constants.EXODUS + "-29", 46);
		chapterMap.put(Constants.EXODUS + "-30", 38);
		chapterMap.put(Constants.EXODUS + "-31", 18);
		chapterMap.put(Constants.EXODUS + "-32", 35);
		chapterMap.put(Constants.EXODUS + "-33", 23);
		chapterMap.put(Constants.EXODUS + "-34", 35);
		chapterMap.put(Constants.EXODUS + "-35", 35);
		chapterMap.put(Constants.EXODUS + "-36", 38);
		chapterMap.put(Constants.EXODUS + "-37", 29);
		chapterMap.put(Constants.EXODUS + "-38", 31);
		chapterMap.put(Constants.EXODUS + "-39", 43);
		chapterMap.put(Constants.EXODUS + "-40", 38);

		bookMap.put(Constants.LEVITICUS, 27);
		bookMapHindi.put(Constants.LEVITICUS, BahaStater.properties.getProperty(Constants.LEVITICUS));
		chapterMap.put(Constants.LEVITICUS + "-1", 17);
		chapterMap.put(Constants.LEVITICUS + "-2", 16);
		chapterMap.put(Constants.LEVITICUS + "-3", 17);
		chapterMap.put(Constants.LEVITICUS + "-4", 35);
		chapterMap.put(Constants.LEVITICUS + "-5", 19);
		chapterMap.put(Constants.LEVITICUS + "-6", 30);
		chapterMap.put(Constants.LEVITICUS + "-7", 38);
		chapterMap.put(Constants.LEVITICUS + "-8", 36);
		chapterMap.put(Constants.LEVITICUS + "-9", 24);
		chapterMap.put(Constants.LEVITICUS + "-10", 20);
		chapterMap.put(Constants.LEVITICUS + "-11", 47);
		chapterMap.put(Constants.LEVITICUS + "-12", 8);
		chapterMap.put(Constants.LEVITICUS + "-13", 59);
		chapterMap.put(Constants.LEVITICUS + "-14", 57);
		chapterMap.put(Constants.LEVITICUS + "-15", 33);
		chapterMap.put(Constants.LEVITICUS + "-16", 34);
		chapterMap.put(Constants.LEVITICUS + "-17", 16);
		chapterMap.put(Constants.LEVITICUS + "-18", 30);
		chapterMap.put(Constants.LEVITICUS + "-19", 37);
		chapterMap.put(Constants.LEVITICUS + "-20", 27);
		chapterMap.put(Constants.LEVITICUS + "-21", 24);
		chapterMap.put(Constants.LEVITICUS + "-22", 33);
		chapterMap.put(Constants.LEVITICUS + "-23", 44);
		chapterMap.put(Constants.LEVITICUS + "-24", 23);
		chapterMap.put(Constants.LEVITICUS + "-25", 55);
		chapterMap.put(Constants.LEVITICUS + "-26", 46);
		chapterMap.put(Constants.LEVITICUS + "-27", 34);

		bookMap.put(Constants.NUMBERS, 36);
		bookMapHindi.put(Constants.NUMBERS, BahaStater.properties.getProperty(Constants.NUMBERS));
		chapterMap.put(Constants.NUMBERS + "-1", 54);
		chapterMap.put(Constants.NUMBERS + "-2", 34);
		chapterMap.put(Constants.NUMBERS + "-3", 51);
		chapterMap.put(Constants.NUMBERS + "-4", 49);
		chapterMap.put(Constants.NUMBERS + "-5", 31);
		chapterMap.put(Constants.NUMBERS + "-6", 27);
		chapterMap.put(Constants.NUMBERS + "-7", 89);
		chapterMap.put(Constants.NUMBERS + "-8", 26);
		chapterMap.put(Constants.NUMBERS + "-9", 23);
		chapterMap.put(Constants.NUMBERS + "-10", 36);
		chapterMap.put(Constants.NUMBERS + "-11", 35);
		chapterMap.put(Constants.NUMBERS + "-12", 16);
		chapterMap.put(Constants.NUMBERS + "-13", 33);
		chapterMap.put(Constants.NUMBERS + "-14", 45);
		chapterMap.put(Constants.NUMBERS + "-15", 41);
		chapterMap.put(Constants.NUMBERS + "-16", 50);
		chapterMap.put(Constants.NUMBERS + "-17", 13);
		chapterMap.put(Constants.NUMBERS + "-18", 32);
		chapterMap.put(Constants.NUMBERS + "-19", 22);
		chapterMap.put(Constants.NUMBERS + "-20", 29);
		chapterMap.put(Constants.NUMBERS + "-21", 35);
		chapterMap.put(Constants.NUMBERS + "-22", 41);
		chapterMap.put(Constants.NUMBERS + "-23", 30);
		chapterMap.put(Constants.NUMBERS + "-24", 25);
		chapterMap.put(Constants.NUMBERS + "-25", 18);
		chapterMap.put(Constants.NUMBERS + "-26", 65);
		chapterMap.put(Constants.NUMBERS + "-27", 23);
		chapterMap.put(Constants.NUMBERS + "-28", 31);
		chapterMap.put(Constants.NUMBERS + "-29", 40);
		chapterMap.put(Constants.NUMBERS + "-30", 16);
		chapterMap.put(Constants.NUMBERS + "-31", 54);
		chapterMap.put(Constants.NUMBERS + "-32", 42);
		chapterMap.put(Constants.NUMBERS + "-33", 56);
		chapterMap.put(Constants.NUMBERS + "-34", 29);
		chapterMap.put(Constants.NUMBERS + "-35", 34);
		chapterMap.put(Constants.NUMBERS + "-36", 13);

		bookMap.put(Constants.DEUTERONOMY, 34);
		bookMapHindi.put(Constants.DEUTERONOMY, BahaStater.properties.getProperty(Constants.DEUTERONOMY));
		chapterMap.put(Constants.DEUTERONOMY + "-1", 46);
		chapterMap.put(Constants.DEUTERONOMY + "-2", 37);
		chapterMap.put(Constants.DEUTERONOMY + "-3", 29);
		chapterMap.put(Constants.DEUTERONOMY + "-4", 49);
		chapterMap.put(Constants.DEUTERONOMY + "-5", 33);
		chapterMap.put(Constants.DEUTERONOMY + "-6", 25);
		chapterMap.put(Constants.DEUTERONOMY + "-7", 26);
		chapterMap.put(Constants.DEUTERONOMY + "-8", 20);
		chapterMap.put(Constants.DEUTERONOMY + "-9", 29);
		chapterMap.put(Constants.DEUTERONOMY + "-10", 22);
		chapterMap.put(Constants.DEUTERONOMY + "-11", 32);
		chapterMap.put(Constants.DEUTERONOMY + "-12", 32);
		chapterMap.put(Constants.DEUTERONOMY + "-13", 18);
		chapterMap.put(Constants.DEUTERONOMY + "-14", 29);
		chapterMap.put(Constants.DEUTERONOMY + "-15", 23);
		chapterMap.put(Constants.DEUTERONOMY + "-16", 22);
		chapterMap.put(Constants.DEUTERONOMY + "-17", 20);
		chapterMap.put(Constants.DEUTERONOMY + "-18", 22);
		chapterMap.put(Constants.DEUTERONOMY + "-19", 21);
		chapterMap.put(Constants.DEUTERONOMY + "-20", 20);
		chapterMap.put(Constants.DEUTERONOMY + "-21", 23);
		chapterMap.put(Constants.DEUTERONOMY + "-22", 30);
		chapterMap.put(Constants.DEUTERONOMY + "-23", 25);
		chapterMap.put(Constants.DEUTERONOMY + "-24", 22);
		chapterMap.put(Constants.DEUTERONOMY + "-25", 19);
		chapterMap.put(Constants.DEUTERONOMY + "-26", 19);
		chapterMap.put(Constants.DEUTERONOMY + "-27", 26);
		chapterMap.put(Constants.DEUTERONOMY + "-28", 68);
		chapterMap.put(Constants.DEUTERONOMY + "-29", 29);
		chapterMap.put(Constants.DEUTERONOMY + "-30", 20);
		chapterMap.put(Constants.DEUTERONOMY + "-31", 30);
		chapterMap.put(Constants.DEUTERONOMY + "-32", 52);
		chapterMap.put(Constants.DEUTERONOMY + "-33", 29);
		chapterMap.put(Constants.DEUTERONOMY + "-34", 12);

		bookMap.put(Constants.JOSHUA, 24);
		bookMapHindi.put(Constants.JOSHUA, BahaStater.properties.getProperty(Constants.JOSHUA));
		chapterMap.put(Constants.JOSHUA + "-1", 18);
		chapterMap.put(Constants.JOSHUA + "-2", 24);
		chapterMap.put(Constants.JOSHUA + "-3", 17);
		chapterMap.put(Constants.JOSHUA + "-4", 24);
		chapterMap.put(Constants.JOSHUA + "-5", 15);
		chapterMap.put(Constants.JOSHUA + "-6", 27);
		chapterMap.put(Constants.JOSHUA + "-7", 26);
		chapterMap.put(Constants.JOSHUA + "-8", 35);
		chapterMap.put(Constants.JOSHUA + "-9", 27);
		chapterMap.put(Constants.JOSHUA + "-10", 43);
		chapterMap.put(Constants.JOSHUA + "-11", 23);
		chapterMap.put(Constants.JOSHUA + "-12", 24);
		chapterMap.put(Constants.JOSHUA + "-13", 33);
		chapterMap.put(Constants.JOSHUA + "-14", 15);
		chapterMap.put(Constants.JOSHUA + "-15", 63);
		chapterMap.put(Constants.JOSHUA + "-16", 10);
		chapterMap.put(Constants.JOSHUA + "-17", 18);
		chapterMap.put(Constants.JOSHUA + "-18", 28);
		chapterMap.put(Constants.JOSHUA + "-19", 51);
		chapterMap.put(Constants.JOSHUA + "-20", 9);
		chapterMap.put(Constants.JOSHUA + "-21", 45);
		chapterMap.put(Constants.JOSHUA + "-22", 34);
		chapterMap.put(Constants.JOSHUA + "-23", 16);
		chapterMap.put(Constants.JOSHUA + "-24", 33);

		bookMap.put(Constants.JUDGES, 21);
		bookMapHindi.put(Constants.JUDGES, BahaStater.properties.getProperty(Constants.JUDGES));
		chapterMap.put(Constants.JUDGES + "-1", 36);
		chapterMap.put(Constants.JUDGES + "-2", 23);
		chapterMap.put(Constants.JUDGES + "-3", 31);
		chapterMap.put(Constants.JUDGES + "-4", 24);
		chapterMap.put(Constants.JUDGES + "-5", 31);
		chapterMap.put(Constants.JUDGES + "-6", 40);
		chapterMap.put(Constants.JUDGES + "-7", 25);
		chapterMap.put(Constants.JUDGES + "-8", 35);
		chapterMap.put(Constants.JUDGES + "-9", 57);
		chapterMap.put(Constants.JUDGES + "-10", 18);
		chapterMap.put(Constants.JUDGES + "-11", 40);
		chapterMap.put(Constants.JUDGES + "-12", 15);
		chapterMap.put(Constants.JUDGES + "-13", 25);
		chapterMap.put(Constants.JUDGES + "-14", 20);
		chapterMap.put(Constants.JUDGES + "-15", 20);
		chapterMap.put(Constants.JUDGES + "-16", 31);
		chapterMap.put(Constants.JUDGES + "-17", 13);
		chapterMap.put(Constants.JUDGES + "-18", 31);
		chapterMap.put(Constants.JUDGES + "-19", 30);
		chapterMap.put(Constants.JUDGES + "-20", 48);
		chapterMap.put(Constants.JUDGES + "-21", 25);

		bookMap.put(Constants.RUTH, 4);
		bookMapHindi.put(Constants.RUTH, BahaStater.properties.getProperty(Constants.RUTH));
		chapterMap.put(Constants.RUTH + "-1", 22);
		chapterMap.put(Constants.RUTH + "-2", 23);
		chapterMap.put(Constants.RUTH + "-3", 18);
		chapterMap.put(Constants.RUTH + "-4", 22);

		bookMap.put(Constants.SAMUEL1_FILE, 31);
		bookMapHindi.put(Constants.SAMUEL1_FILE, BahaStater.properties.getProperty(Constants.SAMUEL1_PROP));
		chapterMap.put(Constants.SAMUEL1_FILE + "-1", 28);
		chapterMap.put(Constants.SAMUEL1_FILE + "-2", 36);
		chapterMap.put(Constants.SAMUEL1_FILE + "-3", 21);
		chapterMap.put(Constants.SAMUEL1_FILE + "-4", 22);
		chapterMap.put(Constants.SAMUEL1_FILE + "-5", 12);
		chapterMap.put(Constants.SAMUEL1_FILE + "-6", 21);
		chapterMap.put(Constants.SAMUEL1_FILE + "-7", 17);
		chapterMap.put(Constants.SAMUEL1_FILE + "-8", 22);
		chapterMap.put(Constants.SAMUEL1_FILE + "-9", 27);
		chapterMap.put(Constants.SAMUEL1_FILE + "-10", 27);
		chapterMap.put(Constants.SAMUEL1_FILE + "-11", 15);
		chapterMap.put(Constants.SAMUEL1_FILE + "-12", 25);
		chapterMap.put(Constants.SAMUEL1_FILE + "-13", 23);
		chapterMap.put(Constants.SAMUEL1_FILE + "-14", 52);
		chapterMap.put(Constants.SAMUEL1_FILE + "-15", 35);
		chapterMap.put(Constants.SAMUEL1_FILE + "-16", 23);
		chapterMap.put(Constants.SAMUEL1_FILE + "-17", 58);
		chapterMap.put(Constants.SAMUEL1_FILE + "-18", 30);
		chapterMap.put(Constants.SAMUEL1_FILE + "-19", 24);
		chapterMap.put(Constants.SAMUEL1_FILE + "-20", 42);
		chapterMap.put(Constants.SAMUEL1_FILE + "-21", 15);
		chapterMap.put(Constants.SAMUEL1_FILE + "-22", 23);
		chapterMap.put(Constants.SAMUEL1_FILE + "-23", 29);
		chapterMap.put(Constants.SAMUEL1_FILE + "-24", 22);
		chapterMap.put(Constants.SAMUEL1_FILE + "-25", 44);
		chapterMap.put(Constants.SAMUEL1_FILE + "-26", 25);
		chapterMap.put(Constants.SAMUEL1_FILE + "-27", 12);
		chapterMap.put(Constants.SAMUEL1_FILE + "-28", 25);
		chapterMap.put(Constants.SAMUEL1_FILE + "-29", 11);
		chapterMap.put(Constants.SAMUEL1_FILE + "-30", 31);
		chapterMap.put(Constants.SAMUEL1_FILE + "-31", 13);

		bookMap.put(Constants.SAMUEL2_FILE, 24);
		bookMapHindi.put(Constants.SAMUEL2_FILE, BahaStater.properties.getProperty(Constants.SAMUEL2_PROP));
		chapterMap.put(Constants.SAMUEL2_FILE + "-1", 27);
		chapterMap.put(Constants.SAMUEL2_FILE + "-2", 32);
		chapterMap.put(Constants.SAMUEL2_FILE + "-3", 39);
		chapterMap.put(Constants.SAMUEL2_FILE + "-4", 12);
		chapterMap.put(Constants.SAMUEL2_FILE + "-5", 25);
		chapterMap.put(Constants.SAMUEL2_FILE + "-6", 23);
		chapterMap.put(Constants.SAMUEL2_FILE + "-7", 29);
		chapterMap.put(Constants.SAMUEL2_FILE + "-8", 18);
		chapterMap.put(Constants.SAMUEL2_FILE + "-9", 13);
		chapterMap.put(Constants.SAMUEL2_FILE + "-10", 19);
		chapterMap.put(Constants.SAMUEL2_FILE + "-11", 27);
		chapterMap.put(Constants.SAMUEL2_FILE + "-12", 31);
		chapterMap.put(Constants.SAMUEL2_FILE + "-13", 39);
		chapterMap.put(Constants.SAMUEL2_FILE + "-14", 33);
		chapterMap.put(Constants.SAMUEL2_FILE + "-15", 37);
		chapterMap.put(Constants.SAMUEL2_FILE + "-16", 23);
		chapterMap.put(Constants.SAMUEL2_FILE + "-17", 29);
		chapterMap.put(Constants.SAMUEL2_FILE + "-18", 33);
		chapterMap.put(Constants.SAMUEL2_FILE + "-19", 43);
		chapterMap.put(Constants.SAMUEL2_FILE + "-20", 26);
		chapterMap.put(Constants.SAMUEL2_FILE + "-21", 22);
		chapterMap.put(Constants.SAMUEL2_FILE + "-22", 51);
		chapterMap.put(Constants.SAMUEL2_FILE + "-23", 39);
		chapterMap.put(Constants.SAMUEL2_FILE + "-24", 25);

		bookMap.put(Constants.KINGS1_FILE, 22);
		bookMapHindi.put(Constants.KINGS1_FILE, BahaStater.properties.getProperty(Constants.KINGS1_PROP));
		chapterMap.put(Constants.KINGS1_FILE + "-1", 53);
		chapterMap.put(Constants.KINGS1_FILE + "-2", 46);
		chapterMap.put(Constants.KINGS1_FILE + "-3", 28);
		chapterMap.put(Constants.KINGS1_FILE + "-4", 34);
		chapterMap.put(Constants.KINGS1_FILE + "-5", 18);
		chapterMap.put(Constants.KINGS1_FILE + "-6", 38);
		chapterMap.put(Constants.KINGS1_FILE + "-7", 51);
		chapterMap.put(Constants.KINGS1_FILE + "-8", 66);
		chapterMap.put(Constants.KINGS1_FILE + "-9", 28);
		chapterMap.put(Constants.KINGS1_FILE + "-10", 29);
		chapterMap.put(Constants.KINGS1_FILE + "-11", 43);
		chapterMap.put(Constants.KINGS1_FILE + "-12", 33);
		chapterMap.put(Constants.KINGS1_FILE + "-13", 34);
		chapterMap.put(Constants.KINGS1_FILE + "-14", 31);
		chapterMap.put(Constants.KINGS1_FILE + "-15", 34);
		chapterMap.put(Constants.KINGS1_FILE + "-16", 34);
		chapterMap.put(Constants.KINGS1_FILE + "-17", 24);
		chapterMap.put(Constants.KINGS1_FILE + "-18", 46);
		chapterMap.put(Constants.KINGS1_FILE + "-19", 21);
		chapterMap.put(Constants.KINGS1_FILE + "-20", 43);
		chapterMap.put(Constants.KINGS1_FILE + "-21", 29);
		chapterMap.put(Constants.KINGS1_FILE + "-22", 53);

		bookMap.put(Constants.KINGS2_FILE, 25);
		bookMapHindi.put(Constants.KINGS2_FILE, BahaStater.properties.getProperty(Constants.KINGS2_PROP));
		chapterMap.put(Constants.KINGS2_FILE + "-1", 18);
		chapterMap.put(Constants.KINGS2_FILE + "-2", 25);
		chapterMap.put(Constants.KINGS2_FILE + "-3", 27);
		chapterMap.put(Constants.KINGS2_FILE + "-4", 44);
		chapterMap.put(Constants.KINGS2_FILE + "-5", 27);
		chapterMap.put(Constants.KINGS2_FILE + "-6", 33);
		chapterMap.put(Constants.KINGS2_FILE + "-7", 20);
		chapterMap.put(Constants.KINGS2_FILE + "-8", 29);
		chapterMap.put(Constants.KINGS2_FILE + "-9", 37);
		chapterMap.put(Constants.KINGS2_FILE + "-10", 36);
		chapterMap.put(Constants.KINGS2_FILE + "-11", 21);
		chapterMap.put(Constants.KINGS2_FILE + "-12", 21);
		chapterMap.put(Constants.KINGS2_FILE + "-13", 25);
		chapterMap.put(Constants.KINGS2_FILE + "-14", 29);
		chapterMap.put(Constants.KINGS2_FILE + "-15", 38);
		chapterMap.put(Constants.KINGS2_FILE + "-16", 20);
		chapterMap.put(Constants.KINGS2_FILE + "-17", 41);
		chapterMap.put(Constants.KINGS2_FILE + "-18", 37);
		chapterMap.put(Constants.KINGS2_FILE + "-19", 37);
		chapterMap.put(Constants.KINGS2_FILE + "-20", 21);
		chapterMap.put(Constants.KINGS2_FILE + "-21", 26);
		chapterMap.put(Constants.KINGS2_FILE + "-22", 20);
		chapterMap.put(Constants.KINGS2_FILE + "-23", 37);
		chapterMap.put(Constants.KINGS2_FILE + "-24", 20);
		chapterMap.put(Constants.KINGS2_FILE + "-25", 30);

		bookMap.put(Constants.CHRONICLES_FILE, 29);
		bookMapHindi.put(Constants.CHRONICLES_FILE, BahaStater.properties.getProperty(Constants.CHRONICLES1_PROP));
		chapterMap.put(Constants.CHRONICLES_FILE + "-1", 54);
		chapterMap.put(Constants.CHRONICLES_FILE + "-2", 55);
		chapterMap.put(Constants.CHRONICLES_FILE + "-3", 24);
		chapterMap.put(Constants.CHRONICLES_FILE + "-4", 43);
		chapterMap.put(Constants.CHRONICLES_FILE + "-5", 26);
		chapterMap.put(Constants.CHRONICLES_FILE + "-6", 81);
		chapterMap.put(Constants.CHRONICLES_FILE + "-7", 40);
		chapterMap.put(Constants.CHRONICLES_FILE + "-8", 40);
		chapterMap.put(Constants.CHRONICLES_FILE + "-9", 44);
		chapterMap.put(Constants.CHRONICLES_FILE + "-10", 14);
		chapterMap.put(Constants.CHRONICLES_FILE + "-11", 47);
		chapterMap.put(Constants.CHRONICLES_FILE + "-12", 40);
		chapterMap.put(Constants.CHRONICLES_FILE + "-13", 14);
		chapterMap.put(Constants.CHRONICLES_FILE + "-14", 17);
		chapterMap.put(Constants.CHRONICLES_FILE + "-15", 29);
		chapterMap.put(Constants.CHRONICLES_FILE + "-16", 43);
		chapterMap.put(Constants.CHRONICLES_FILE + "-17", 27);
		chapterMap.put(Constants.CHRONICLES_FILE + "-18", 17);
		chapterMap.put(Constants.CHRONICLES_FILE + "-19", 19);
		chapterMap.put(Constants.CHRONICLES_FILE + "-20", 8);
		chapterMap.put(Constants.CHRONICLES_FILE + "-21", 30);
		chapterMap.put(Constants.CHRONICLES_FILE + "-22", 19);
		chapterMap.put(Constants.CHRONICLES_FILE + "-23", 32);
		chapterMap.put(Constants.CHRONICLES_FILE + "-24", 31);
		chapterMap.put(Constants.CHRONICLES_FILE + "-25", 31);
		chapterMap.put(Constants.CHRONICLES_FILE + "-26", 32);
		chapterMap.put(Constants.CHRONICLES_FILE + "-27", 34);
		chapterMap.put(Constants.CHRONICLES_FILE + "-28", 21);
		chapterMap.put(Constants.CHRONICLES_FILE + "-29", 30);

		bookMap.put(Constants.CHRONICLES2_FILE, 36);
		bookMapHindi.put(Constants.CHRONICLES2_FILE, BahaStater.properties.getProperty(Constants.CHRONICLES2_PROP));
		chapterMap.put(Constants.CHRONICLES2_FILE + "-1", 17);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-2", 18);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-3", 17);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-4", 22);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-5", 14);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-6", 42);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-7", 22);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-8", 18);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-9", 31);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-10", 19);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-11", 23);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-12", 16);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-13", 22);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-14", 15);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-15", 19);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-16", 14);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-17", 19);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-18", 34);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-19", 11);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-20", 37);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-21", 20);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-22", 12);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-23", 21);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-24", 27);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-25", 28);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-26", 23);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-27", 9);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-28", 27);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-29", 36);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-30", 27);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-31", 21);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-32", 33);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-33", 25);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-34", 33);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-35", 27);
		chapterMap.put(Constants.CHRONICLES2_FILE + "-36", 23);

		bookMap.put(Constants.EZRA, 10);
		bookMapHindi.put(Constants.EZRA, BahaStater.properties.getProperty(Constants.EZRA));
		chapterMap.put(Constants.EZRA + "-1", 11);
		chapterMap.put(Constants.EZRA + "-2", 70);
		chapterMap.put(Constants.EZRA + "-3", 13);
		chapterMap.put(Constants.EZRA + "-4", 24);
		chapterMap.put(Constants.EZRA + "-5", 17);
		chapterMap.put(Constants.EZRA + "-6", 22);
		chapterMap.put(Constants.EZRA + "-7", 28);
		chapterMap.put(Constants.EZRA + "-8", 36);
		chapterMap.put(Constants.EZRA + "-9", 15);
		chapterMap.put(Constants.EZRA + "-10", 44);

		bookMap.put(Constants.NEHEMIAH, 13);
		bookMapHindi.put(Constants.NEHEMIAH, BahaStater.properties.getProperty(Constants.NEHEMIAH));
		chapterMap.put(Constants.NEHEMIAH + "-1", 11);
		chapterMap.put(Constants.NEHEMIAH + "-2", 20);
		chapterMap.put(Constants.NEHEMIAH + "-3", 32);
		chapterMap.put(Constants.NEHEMIAH + "-4", 23);
		chapterMap.put(Constants.NEHEMIAH + "-5", 19);
		chapterMap.put(Constants.NEHEMIAH + "-6", 19);
		chapterMap.put(Constants.NEHEMIAH + "-7", 73);
		chapterMap.put(Constants.NEHEMIAH + "-8", 18);
		chapterMap.put(Constants.NEHEMIAH + "-9", 38);
		chapterMap.put(Constants.NEHEMIAH + "-10", 39);
		chapterMap.put(Constants.NEHEMIAH + "-11", 36);
		chapterMap.put(Constants.NEHEMIAH + "-12", 47);
		chapterMap.put(Constants.NEHEMIAH + "-13", 31);

		bookMap.put(Constants.ESTHER, 10);
		bookMapHindi.put(Constants.ESTHER, BahaStater.properties.getProperty(Constants.ESTHER));
		chapterMap.put(Constants.ESTHER + "-1", 22);
		chapterMap.put(Constants.ESTHER + "-2", 23);
		chapterMap.put(Constants.ESTHER + "-3", 15);
		chapterMap.put(Constants.ESTHER + "-4", 17);
		chapterMap.put(Constants.ESTHER + "-5", 14);
		chapterMap.put(Constants.ESTHER + "-6", 14);
		chapterMap.put(Constants.ESTHER + "-7", 10);
		chapterMap.put(Constants.ESTHER + "-8", 17);
		chapterMap.put(Constants.ESTHER + "-9", 32);
		chapterMap.put(Constants.ESTHER + "-10", 3);

		bookMap.put(Constants.JOB, 42);
		bookMapHindi.put(Constants.JOB, BahaStater.properties.getProperty(Constants.JOB));
		chapterMap.put(Constants.JOB + "-1", 22);
		chapterMap.put(Constants.JOB + "-2", 13);
		chapterMap.put(Constants.JOB + "-3", 26);
		chapterMap.put(Constants.JOB + "-4", 21);
		chapterMap.put(Constants.JOB + "-5", 27);
		chapterMap.put(Constants.JOB + "-6", 30);
		chapterMap.put(Constants.JOB + "-7", 21);
		chapterMap.put(Constants.JOB + "-8", 22);
		chapterMap.put(Constants.JOB + "-9", 35);
		chapterMap.put(Constants.JOB + "-10", 22);
		chapterMap.put(Constants.JOB + "-11", 20);
		chapterMap.put(Constants.JOB + "-12", 25);
		chapterMap.put(Constants.JOB + "-13", 28);
		chapterMap.put(Constants.JOB + "-14", 22);
		chapterMap.put(Constants.JOB + "-15", 35);
		chapterMap.put(Constants.JOB + "-16", 22);
		chapterMap.put(Constants.JOB + "-17", 16);
		chapterMap.put(Constants.JOB + "-18", 21);
		chapterMap.put(Constants.JOB + "-19", 29);
		chapterMap.put(Constants.JOB + "-20", 29);
		chapterMap.put(Constants.JOB + "-21", 34);
		chapterMap.put(Constants.JOB + "-22", 30);
		chapterMap.put(Constants.JOB + "-23", 17);
		chapterMap.put(Constants.JOB + "-24", 25);
		chapterMap.put(Constants.JOB + "-25", 6);
		chapterMap.put(Constants.JOB + "-26", 14);
		chapterMap.put(Constants.JOB + "-27", 23);
		chapterMap.put(Constants.JOB + "-28", 28);
		chapterMap.put(Constants.JOB + "-29", 25);
		chapterMap.put(Constants.JOB + "-30", 31);
		chapterMap.put(Constants.JOB + "-31", 40);
		chapterMap.put(Constants.JOB + "-32", 22);
		chapterMap.put(Constants.JOB + "-33", 33);
		chapterMap.put(Constants.JOB + "-34", 37);
		chapterMap.put(Constants.JOB + "-35", 16);
		chapterMap.put(Constants.JOB + "-36", 33);
		chapterMap.put(Constants.JOB + "-37", 24);
		chapterMap.put(Constants.JOB + "-38", 41);
		chapterMap.put(Constants.JOB + "-39", 30);
		chapterMap.put(Constants.JOB + "-40", 24);
		chapterMap.put(Constants.JOB + "-41", 34);
		chapterMap.put(Constants.JOB + "-42", 17);

		bookMap.put(Constants.PSALMS, 150);
		bookMapHindi.put(Constants.PSALMS, BahaStater.properties.getProperty(Constants.PSALMS));
		chapterMap.put(Constants.PSALMS + "-1", 6);
		chapterMap.put(Constants.PSALMS + "-2", 12);
		chapterMap.put(Constants.PSALMS + "-3", 8);
		chapterMap.put(Constants.PSALMS + "-4", 8);
		chapterMap.put(Constants.PSALMS + "-5", 12);
		chapterMap.put(Constants.PSALMS + "-6", 10);
		chapterMap.put(Constants.PSALMS + "-7", 17);
		chapterMap.put(Constants.PSALMS + "-8", 9);
		chapterMap.put(Constants.PSALMS + "-9", 20);
		chapterMap.put(Constants.PSALMS + "-10", 18);
		chapterMap.put(Constants.PSALMS + "-11", 7);
		chapterMap.put(Constants.PSALMS + "-12", 8);
		chapterMap.put(Constants.PSALMS + "-13", 6);
		chapterMap.put(Constants.PSALMS + "-14", 7);
		chapterMap.put(Constants.PSALMS + "-15", 5);
		chapterMap.put(Constants.PSALMS + "-16", 11);
		chapterMap.put(Constants.PSALMS + "-17", 15);
		chapterMap.put(Constants.PSALMS + "-18", 50);
		chapterMap.put(Constants.PSALMS + "-19", 14);
		chapterMap.put(Constants.PSALMS + "-20", 9);
		chapterMap.put(Constants.PSALMS + "-21", 13);
		chapterMap.put(Constants.PSALMS + "-22", 31);
		chapterMap.put(Constants.PSALMS + "-23", 6);
		chapterMap.put(Constants.PSALMS + "-24", 10);
		chapterMap.put(Constants.PSALMS + "-25", 22);
		chapterMap.put(Constants.PSALMS + "-26", 12);
		chapterMap.put(Constants.PSALMS + "-27", 14);
		chapterMap.put(Constants.PSALMS + "-28", 9);
		chapterMap.put(Constants.PSALMS + "-29", 11);
		chapterMap.put(Constants.PSALMS + "-30", 12);
		chapterMap.put(Constants.PSALMS + "-31", 24);
		chapterMap.put(Constants.PSALMS + "-32", 11);
		chapterMap.put(Constants.PSALMS + "-33", 22);
		chapterMap.put(Constants.PSALMS + "-34", 22);
		chapterMap.put(Constants.PSALMS + "-35", 28);
		chapterMap.put(Constants.PSALMS + "-36", 12);
		chapterMap.put(Constants.PSALMS + "-37", 40);
		chapterMap.put(Constants.PSALMS + "-38", 22);
		chapterMap.put(Constants.PSALMS + "-39", 13);
		chapterMap.put(Constants.PSALMS + "-40", 17);
		chapterMap.put(Constants.PSALMS + "-41", 13);
		chapterMap.put(Constants.PSALMS + "-42", 11);
		chapterMap.put(Constants.PSALMS + "-43", 5);
		chapterMap.put(Constants.PSALMS + "-44", 26);
		chapterMap.put(Constants.PSALMS + "-45", 17);
		chapterMap.put(Constants.PSALMS + "-46", 11);
		chapterMap.put(Constants.PSALMS + "-47", 9);
		chapterMap.put(Constants.PSALMS + "-48", 14);
		chapterMap.put(Constants.PSALMS + "-49", 20);
		chapterMap.put(Constants.PSALMS + "-50", 23);
		chapterMap.put(Constants.PSALMS + "-51", 19);
		chapterMap.put(Constants.PSALMS + "-52", 9);
		chapterMap.put(Constants.PSALMS + "-53", 6);
		chapterMap.put(Constants.PSALMS + "-54", 7);
		chapterMap.put(Constants.PSALMS + "-55", 23);
		chapterMap.put(Constants.PSALMS + "-56", 13);
		chapterMap.put(Constants.PSALMS + "-57", 11);
		chapterMap.put(Constants.PSALMS + "-58", 11);
		chapterMap.put(Constants.PSALMS + "-59", 17);
		chapterMap.put(Constants.PSALMS + "-60", 12);
		chapterMap.put(Constants.PSALMS + "-61", 8);
		chapterMap.put(Constants.PSALMS + "-62", 12);
		chapterMap.put(Constants.PSALMS + "-63", 11);
		chapterMap.put(Constants.PSALMS + "-64", 10);
		chapterMap.put(Constants.PSALMS + "-65", 13);
		chapterMap.put(Constants.PSALMS + "-66", 20);
		chapterMap.put(Constants.PSALMS + "-67", 7);
		chapterMap.put(Constants.PSALMS + "-68", 35);
		chapterMap.put(Constants.PSALMS + "-69", 36);
		chapterMap.put(Constants.PSALMS + "-70", 5);
		chapterMap.put(Constants.PSALMS + "-71", 24);
		chapterMap.put(Constants.PSALMS + "-72", 20);
		chapterMap.put(Constants.PSALMS + "-73", 28);
		chapterMap.put(Constants.PSALMS + "-74", 23);
		chapterMap.put(Constants.PSALMS + "-75", 10);
		chapterMap.put(Constants.PSALMS + "-76", 12);
		chapterMap.put(Constants.PSALMS + "-77", 20);
		chapterMap.put(Constants.PSALMS + "-78", 72);
		chapterMap.put(Constants.PSALMS + "-79", 13);
		chapterMap.put(Constants.PSALMS + "-80", 19);
		chapterMap.put(Constants.PSALMS + "-81", 16);
		chapterMap.put(Constants.PSALMS + "-82", 8);
		chapterMap.put(Constants.PSALMS + "-83", 18);
		chapterMap.put(Constants.PSALMS + "-84", 12);
		chapterMap.put(Constants.PSALMS + "-85", 13);
		chapterMap.put(Constants.PSALMS + "-86", 17);
		chapterMap.put(Constants.PSALMS + "-87", 7);
		chapterMap.put(Constants.PSALMS + "-88", 18);
		chapterMap.put(Constants.PSALMS + "-89", 52);
		chapterMap.put(Constants.PSALMS + "-90", 17);
		chapterMap.put(Constants.PSALMS + "-91", 16);
		chapterMap.put(Constants.PSALMS + "-92", 15);
		chapterMap.put(Constants.PSALMS + "-93", 5);
		chapterMap.put(Constants.PSALMS + "-94", 23);
		chapterMap.put(Constants.PSALMS + "-95", 11);
		chapterMap.put(Constants.PSALMS + "-96", 13);
		chapterMap.put(Constants.PSALMS + "-97", 12);
		chapterMap.put(Constants.PSALMS + "-98", 9);
		chapterMap.put(Constants.PSALMS + "-99", 9);
		chapterMap.put(Constants.PSALMS + "-100", 5);
		chapterMap.put(Constants.PSALMS + "-101", 8);
		chapterMap.put(Constants.PSALMS + "-102", 28);
		chapterMap.put(Constants.PSALMS + "-103", 22);
		chapterMap.put(Constants.PSALMS + "-104", 35);
		chapterMap.put(Constants.PSALMS + "-105", 45);
		chapterMap.put(Constants.PSALMS + "-106", 48);
		chapterMap.put(Constants.PSALMS + "-107", 43);
		chapterMap.put(Constants.PSALMS + "-108", 13);
		chapterMap.put(Constants.PSALMS + "-109", 31);
		chapterMap.put(Constants.PSALMS + "-110", 7);
		chapterMap.put(Constants.PSALMS + "-111", 10);
		chapterMap.put(Constants.PSALMS + "-112", 10);
		chapterMap.put(Constants.PSALMS + "-113", 9);
		chapterMap.put(Constants.PSALMS + "-114", 8);
		chapterMap.put(Constants.PSALMS + "-115", 18);
		chapterMap.put(Constants.PSALMS + "-116", 19);
		chapterMap.put(Constants.PSALMS + "-117", 2);
		chapterMap.put(Constants.PSALMS + "-118", 29);
		chapterMap.put(Constants.PSALMS + "-119", 176);
		chapterMap.put(Constants.PSALMS + "-120", 7);
		chapterMap.put(Constants.PSALMS + "-121", 8);
		chapterMap.put(Constants.PSALMS + "-122", 9);
		chapterMap.put(Constants.PSALMS + "-123", 4);
		chapterMap.put(Constants.PSALMS + "-124", 8);
		chapterMap.put(Constants.PSALMS + "-125", 5);
		chapterMap.put(Constants.PSALMS + "-126", 6);
		chapterMap.put(Constants.PSALMS + "-127", 5);
		chapterMap.put(Constants.PSALMS + "-128", 6);
		chapterMap.put(Constants.PSALMS + "-129", 8);
		chapterMap.put(Constants.PSALMS + "-130", 8);
		chapterMap.put(Constants.PSALMS + "-131", 3);
		chapterMap.put(Constants.PSALMS + "-132", 18);
		chapterMap.put(Constants.PSALMS + "-133", 3);
		chapterMap.put(Constants.PSALMS + "-134", 3);
		chapterMap.put(Constants.PSALMS + "-135", 21);
		chapterMap.put(Constants.PSALMS + "-136", 26);
		chapterMap.put(Constants.PSALMS + "-137", 9);
		chapterMap.put(Constants.PSALMS + "-138", 8);
		chapterMap.put(Constants.PSALMS + "-139", 24);
		chapterMap.put(Constants.PSALMS + "-140", 14);
		chapterMap.put(Constants.PSALMS + "-141", 10);
		chapterMap.put(Constants.PSALMS + "-142", 7);
		chapterMap.put(Constants.PSALMS + "-143", 12);
		chapterMap.put(Constants.PSALMS + "-144", 15);
		chapterMap.put(Constants.PSALMS + "-145", 21);
		chapterMap.put(Constants.PSALMS + "-146", 10);
		chapterMap.put(Constants.PSALMS + "-147", 20);
		chapterMap.put(Constants.PSALMS + "-148", 14);
		chapterMap.put(Constants.PSALMS + "-149", 9);
		chapterMap.put(Constants.PSALMS + "-150", 6);

		bookMap.put(Constants.PROVERBS, 31);
		bookMapHindi.put(Constants.PROVERBS, BahaStater.properties.getProperty(Constants.PROVERBS));
		chapterMap.put(Constants.PROVERBS + "-1", 33);
		chapterMap.put(Constants.PROVERBS + "-2", 22);
		chapterMap.put(Constants.PROVERBS + "-3", 35);
		chapterMap.put(Constants.PROVERBS + "-4", 27);
		chapterMap.put(Constants.PROVERBS + "-5", 23);
		chapterMap.put(Constants.PROVERBS + "-6", 35);
		chapterMap.put(Constants.PROVERBS + "-7", 27);
		chapterMap.put(Constants.PROVERBS + "-8", 36);
		chapterMap.put(Constants.PROVERBS + "-9", 18);
		chapterMap.put(Constants.PROVERBS + "-10", 32);
		chapterMap.put(Constants.PROVERBS + "-11", 31);
		chapterMap.put(Constants.PROVERBS + "-12", 28);
		chapterMap.put(Constants.PROVERBS + "-13", 25);
		chapterMap.put(Constants.PROVERBS + "-14", 35);
		chapterMap.put(Constants.PROVERBS + "-15", 33);
		chapterMap.put(Constants.PROVERBS + "-16", 33);
		chapterMap.put(Constants.PROVERBS + "-17", 28);
		chapterMap.put(Constants.PROVERBS + "-18", 24);
		chapterMap.put(Constants.PROVERBS + "-19", 29);
		chapterMap.put(Constants.PROVERBS + "-20", 30);
		chapterMap.put(Constants.PROVERBS + "-21", 31);
		chapterMap.put(Constants.PROVERBS + "-22", 29);
		chapterMap.put(Constants.PROVERBS + "-23", 35);
		chapterMap.put(Constants.PROVERBS + "-24", 34);
		chapterMap.put(Constants.PROVERBS + "-25", 28);
		chapterMap.put(Constants.PROVERBS + "-26", 28);
		chapterMap.put(Constants.PROVERBS + "-27", 27);
		chapterMap.put(Constants.PROVERBS + "-28", 28);
		chapterMap.put(Constants.PROVERBS + "-29", 27);
		chapterMap.put(Constants.PROVERBS + "-30", 33);
		chapterMap.put(Constants.PROVERBS + "-31", 31);

		bookMap.put(Constants.ECCLESIASTES, 12);
		bookMapHindi.put(Constants.ECCLESIASTES, BahaStater.properties.getProperty(Constants.ECCLESIASTES));
		chapterMap.put(Constants.ECCLESIASTES + "-1", 18);
		chapterMap.put(Constants.ECCLESIASTES + "-2", 26);
		chapterMap.put(Constants.ECCLESIASTES + "-3", 22);
		chapterMap.put(Constants.ECCLESIASTES + "-4", 16);
		chapterMap.put(Constants.ECCLESIASTES + "-5", 20);
		chapterMap.put(Constants.ECCLESIASTES + "-6", 12);
		chapterMap.put(Constants.ECCLESIASTES + "-7", 29);
		chapterMap.put(Constants.ECCLESIASTES + "-8", 17);
		chapterMap.put(Constants.ECCLESIASTES + "-9", 18);
		chapterMap.put(Constants.ECCLESIASTES + "-10", 20);
		chapterMap.put(Constants.ECCLESIASTES + "-11", 10);
		chapterMap.put(Constants.ECCLESIASTES + "-12", 14);

		bookMap.put(Constants.SONGOFSOLOMON_FILE, 8);
		bookMapHindi.put(Constants.SONGOFSOLOMON_FILE, BahaStater.properties.getProperty(Constants.SONGOFSOLOMON_PROP));
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-1", 17);
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-2", 17);
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-3", 11);
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-4", 16);
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-5", 16);
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-6", 13);
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-7", 13);
		chapterMap.put(Constants.SONGOFSOLOMON_FILE + "-8", 14);

		bookMap.put(Constants.ISAIAH, 66);
		bookMapHindi.put(Constants.ISAIAH, BahaStater.properties.getProperty(Constants.ISAIAH));
		chapterMap.put(Constants.ISAIAH + "-1", 31);
		chapterMap.put(Constants.ISAIAH + "-2", 22);
		chapterMap.put(Constants.ISAIAH + "-3", 26);
		chapterMap.put(Constants.ISAIAH + "-4", 6);
		chapterMap.put(Constants.ISAIAH + "-5", 30);
		chapterMap.put(Constants.ISAIAH + "-6", 13);
		chapterMap.put(Constants.ISAIAH + "-7", 25);
		chapterMap.put(Constants.ISAIAH + "-8", 22);
		chapterMap.put(Constants.ISAIAH + "-9", 21);
		chapterMap.put(Constants.ISAIAH + "-10", 34);
		chapterMap.put(Constants.ISAIAH + "-11", 16);
		chapterMap.put(Constants.ISAIAH + "-12", 6);
		chapterMap.put(Constants.ISAIAH + "-13", 22);
		chapterMap.put(Constants.ISAIAH + "-14", 32);
		chapterMap.put(Constants.ISAIAH + "-15", 9);
		chapterMap.put(Constants.ISAIAH + "-16", 14);
		chapterMap.put(Constants.ISAIAH + "-17", 14);
		chapterMap.put(Constants.ISAIAH + "-18", 7);
		chapterMap.put(Constants.ISAIAH + "-19", 25);
		chapterMap.put(Constants.ISAIAH + "-20", 6);
		chapterMap.put(Constants.ISAIAH + "-21", 17);
		chapterMap.put(Constants.ISAIAH + "-22", 25);
		chapterMap.put(Constants.ISAIAH + "-23", 18);
		chapterMap.put(Constants.ISAIAH + "-24", 23);
		chapterMap.put(Constants.ISAIAH + "-25", 12);
		chapterMap.put(Constants.ISAIAH + "-26", 21);
		chapterMap.put(Constants.ISAIAH + "-27", 13);
		chapterMap.put(Constants.ISAIAH + "-28", 29);
		chapterMap.put(Constants.ISAIAH + "-29", 24);
		chapterMap.put(Constants.ISAIAH + "-30", 33);
		chapterMap.put(Constants.ISAIAH + "-31", 9);
		chapterMap.put(Constants.ISAIAH + "-32", 20);
		chapterMap.put(Constants.ISAIAH + "-33", 24);
		chapterMap.put(Constants.ISAIAH + "-34", 17);
		chapterMap.put(Constants.ISAIAH + "-35", 10);
		chapterMap.put(Constants.ISAIAH + "-36", 22);
		chapterMap.put(Constants.ISAIAH + "-37", 38);
		chapterMap.put(Constants.ISAIAH + "-38", 22);
		chapterMap.put(Constants.ISAIAH + "-39", 8);
		chapterMap.put(Constants.ISAIAH + "-40", 31);
		chapterMap.put(Constants.ISAIAH + "-41", 29);
		chapterMap.put(Constants.ISAIAH + "-42", 25);
		chapterMap.put(Constants.ISAIAH + "-43", 28);
		chapterMap.put(Constants.ISAIAH + "-44", 28);
		chapterMap.put(Constants.ISAIAH + "-45", 25);
		chapterMap.put(Constants.ISAIAH + "-46", 13);
		chapterMap.put(Constants.ISAIAH + "-47", 15);
		chapterMap.put(Constants.ISAIAH + "-48", 22);
		chapterMap.put(Constants.ISAIAH + "-49", 26);
		chapterMap.put(Constants.ISAIAH + "-50", 11);
		chapterMap.put(Constants.ISAIAH + "-51", 23);
		chapterMap.put(Constants.ISAIAH + "-52", 15);
		chapterMap.put(Constants.ISAIAH + "-53", 12);
		chapterMap.put(Constants.ISAIAH + "-54", 17);
		chapterMap.put(Constants.ISAIAH + "-55", 13);
		chapterMap.put(Constants.ISAIAH + "-56", 12);
		chapterMap.put(Constants.ISAIAH + "-57", 21);
		chapterMap.put(Constants.ISAIAH + "-58", 14);
		chapterMap.put(Constants.ISAIAH + "-59", 21);
		chapterMap.put(Constants.ISAIAH + "-60", 22);
		chapterMap.put(Constants.ISAIAH + "-61", 11);
		chapterMap.put(Constants.ISAIAH + "-62", 12);
		chapterMap.put(Constants.ISAIAH + "-63", 19);
		chapterMap.put(Constants.ISAIAH + "-64", 12);
		chapterMap.put(Constants.ISAIAH + "-65", 25);
		chapterMap.put(Constants.ISAIAH + "-66", 24);

		bookMap.put(Constants.JEREMIAH, 52);
		bookMapHindi.put(Constants.JEREMIAH, BahaStater.properties.getProperty(Constants.JEREMIAH));
		chapterMap.put(Constants.JEREMIAH + "-1", 19);
		chapterMap.put(Constants.JEREMIAH + "-2", 37);
		chapterMap.put(Constants.JEREMIAH + "-3", 25);
		chapterMap.put(Constants.JEREMIAH + "-4", 31);
		chapterMap.put(Constants.JEREMIAH + "-5", 31);
		chapterMap.put(Constants.JEREMIAH + "-6", 30);
		chapterMap.put(Constants.JEREMIAH + "-7", 34);
		chapterMap.put(Constants.JEREMIAH + "-8", 22);
		chapterMap.put(Constants.JEREMIAH + "-9", 26);
		chapterMap.put(Constants.JEREMIAH + "-10", 25);
		chapterMap.put(Constants.JEREMIAH + "-11", 23);
		chapterMap.put(Constants.JEREMIAH + "-12", 17);
		chapterMap.put(Constants.JEREMIAH + "-13", 27);
		chapterMap.put(Constants.JEREMIAH + "-14", 22);
		chapterMap.put(Constants.JEREMIAH + "-15", 21);
		chapterMap.put(Constants.JEREMIAH + "-16", 21);
		chapterMap.put(Constants.JEREMIAH + "-17", 27);
		chapterMap.put(Constants.JEREMIAH + "-18", 23);
		chapterMap.put(Constants.JEREMIAH + "-19", 15);
		chapterMap.put(Constants.JEREMIAH + "-20", 18);
		chapterMap.put(Constants.JEREMIAH + "-21", 14);
		chapterMap.put(Constants.JEREMIAH + "-22", 30);
		chapterMap.put(Constants.JEREMIAH + "-23", 40);
		chapterMap.put(Constants.JEREMIAH + "-24", 10);
		chapterMap.put(Constants.JEREMIAH + "-25", 38);
		chapterMap.put(Constants.JEREMIAH + "-26", 24);
		chapterMap.put(Constants.JEREMIAH + "-27", 22);
		chapterMap.put(Constants.JEREMIAH + "-28", 17);
		chapterMap.put(Constants.JEREMIAH + "-29", 32);
		chapterMap.put(Constants.JEREMIAH + "-30", 24);
		chapterMap.put(Constants.JEREMIAH + "-31", 40);
		chapterMap.put(Constants.JEREMIAH + "-32", 44);
		chapterMap.put(Constants.JEREMIAH + "-33", 26);
		chapterMap.put(Constants.JEREMIAH + "-34", 22);
		chapterMap.put(Constants.JEREMIAH + "-35", 19);
		chapterMap.put(Constants.JEREMIAH + "-36", 32);
		chapterMap.put(Constants.JEREMIAH + "-37", 21);
		chapterMap.put(Constants.JEREMIAH + "-38", 28);
		chapterMap.put(Constants.JEREMIAH + "-39", 18);
		chapterMap.put(Constants.JEREMIAH + "-40", 16);
		chapterMap.put(Constants.JEREMIAH + "-41", 18);
		chapterMap.put(Constants.JEREMIAH + "-42", 22);
		chapterMap.put(Constants.JEREMIAH + "-43", 13);
		chapterMap.put(Constants.JEREMIAH + "-44", 30);
		chapterMap.put(Constants.JEREMIAH + "-45", 5);
		chapterMap.put(Constants.JEREMIAH + "-46", 28);
		chapterMap.put(Constants.JEREMIAH + "-47", 7);
		chapterMap.put(Constants.JEREMIAH + "-48", 47);
		chapterMap.put(Constants.JEREMIAH + "-49", 39);
		chapterMap.put(Constants.JEREMIAH + "-50", 46);
		chapterMap.put(Constants.JEREMIAH + "-51", 64);
		chapterMap.put(Constants.JEREMIAH + "-52", 34);

		bookMap.put(Constants.LAMENTATIONS, 5);
		bookMapHindi.put(Constants.LAMENTATIONS, BahaStater.properties.getProperty(Constants.LAMENTATIONS));
		chapterMap.put(Constants.LAMENTATIONS + "-1", 22);
		chapterMap.put(Constants.LAMENTATIONS + "-2", 22);
		chapterMap.put(Constants.LAMENTATIONS + "-3", 66);
		chapterMap.put(Constants.LAMENTATIONS + "-4", 22);
		chapterMap.put(Constants.LAMENTATIONS + "-5", 22);

		bookMap.put(Constants.EZEKIEL, 48);
		bookMapHindi.put(Constants.EZEKIEL, BahaStater.properties.getProperty(Constants.EZEKIEL));
		chapterMap.put(Constants.EZEKIEL + "-1", 28);
		chapterMap.put(Constants.EZEKIEL + "-2", 10);
		chapterMap.put(Constants.EZEKIEL + "-3", 27);
		chapterMap.put(Constants.EZEKIEL + "-4", 17);
		chapterMap.put(Constants.EZEKIEL + "-5", 17);
		chapterMap.put(Constants.EZEKIEL + "-6", 14);
		chapterMap.put(Constants.EZEKIEL + "-7", 27);
		chapterMap.put(Constants.EZEKIEL + "-8", 18);
		chapterMap.put(Constants.EZEKIEL + "-9", 11);
		chapterMap.put(Constants.EZEKIEL + "-10", 22);
		chapterMap.put(Constants.EZEKIEL + "-11", 25);
		chapterMap.put(Constants.EZEKIEL + "-12", 28);
		chapterMap.put(Constants.EZEKIEL + "-13", 23);
		chapterMap.put(Constants.EZEKIEL + "-14", 23);
		chapterMap.put(Constants.EZEKIEL + "-15", 8);
		chapterMap.put(Constants.EZEKIEL + "-16", 63);
		chapterMap.put(Constants.EZEKIEL + "-17", 24);
		chapterMap.put(Constants.EZEKIEL + "-18", 32);
		chapterMap.put(Constants.EZEKIEL + "-19", 14);
		chapterMap.put(Constants.EZEKIEL + "-20", 49);
		chapterMap.put(Constants.EZEKIEL + "-21", 32);
		chapterMap.put(Constants.EZEKIEL + "-22", 31);
		chapterMap.put(Constants.EZEKIEL + "-23", 49);
		chapterMap.put(Constants.EZEKIEL + "-24", 27);
		chapterMap.put(Constants.EZEKIEL + "-25", 17);
		chapterMap.put(Constants.EZEKIEL + "-26", 21);
		chapterMap.put(Constants.EZEKIEL + "-27", 36);
		chapterMap.put(Constants.EZEKIEL + "-28", 26);
		chapterMap.put(Constants.EZEKIEL + "-29", 21);
		chapterMap.put(Constants.EZEKIEL + "-30", 26);
		chapterMap.put(Constants.EZEKIEL + "-31", 18);
		chapterMap.put(Constants.EZEKIEL + "-32", 32);
		chapterMap.put(Constants.EZEKIEL + "-33", 33);
		chapterMap.put(Constants.EZEKIEL + "-34", 31);
		chapterMap.put(Constants.EZEKIEL + "-35", 15);
		chapterMap.put(Constants.EZEKIEL + "-36", 38);
		chapterMap.put(Constants.EZEKIEL + "-37", 28);
		chapterMap.put(Constants.EZEKIEL + "-38", 23);
		chapterMap.put(Constants.EZEKIEL + "-39", 29);
		chapterMap.put(Constants.EZEKIEL + "-40", 49);
		chapterMap.put(Constants.EZEKIEL + "-41", 26);
		chapterMap.put(Constants.EZEKIEL + "-42", 20);
		chapterMap.put(Constants.EZEKIEL + "-43", 27);
		chapterMap.put(Constants.EZEKIEL + "-44", 31);
		chapterMap.put(Constants.EZEKIEL + "-45", 25);
		chapterMap.put(Constants.EZEKIEL + "-46", 24);
		chapterMap.put(Constants.EZEKIEL + "-47", 23);
		chapterMap.put(Constants.EZEKIEL + "-48", 35);

		bookMap.put(Constants.DANIEL, 12);
		bookMapHindi.put(Constants.DANIEL, BahaStater.properties.getProperty(Constants.DANIEL));
		chapterMap.put(Constants.DANIEL + "-1", 21);
		chapterMap.put(Constants.DANIEL + "-2", 49);
		chapterMap.put(Constants.DANIEL + "-3", 30);
		chapterMap.put(Constants.DANIEL + "-4", 37);
		chapterMap.put(Constants.DANIEL + "-5", 31);
		chapterMap.put(Constants.DANIEL + "-6", 28);
		chapterMap.put(Constants.DANIEL + "-7", 28);
		chapterMap.put(Constants.DANIEL + "-8", 27);
		chapterMap.put(Constants.DANIEL + "-9", 27);
		chapterMap.put(Constants.DANIEL + "-10", 21);
		chapterMap.put(Constants.DANIEL + "-11", 45);
		chapterMap.put(Constants.DANIEL + "-12", 13);
//		chapterMap.put(Constants.DANIEL+"-13", 64);
//		chapterMap.put(Constants.DANIEL+"-14", 42);

		bookMap.put(Constants.HOSEA, 14);
		bookMapHindi.put(Constants.HOSEA, BahaStater.properties.getProperty(Constants.HOSEA));
		chapterMap.put(Constants.HOSEA + "-1", 11);
		chapterMap.put(Constants.HOSEA + "-2", 23);
		chapterMap.put(Constants.HOSEA + "-3", 5);
		chapterMap.put(Constants.HOSEA + "-4", 19);
		chapterMap.put(Constants.HOSEA + "-5", 15);
		chapterMap.put(Constants.HOSEA + "-6", 11);
		chapterMap.put(Constants.HOSEA + "-7", 16);
		chapterMap.put(Constants.HOSEA + "-8", 14);
		chapterMap.put(Constants.HOSEA + "-9", 17);
		chapterMap.put(Constants.HOSEA + "-10", 15);
		chapterMap.put(Constants.HOSEA + "-11", 12);
		chapterMap.put(Constants.HOSEA + "-12", 14);
		chapterMap.put(Constants.HOSEA + "-13", 16);
		chapterMap.put(Constants.HOSEA + "-14", 9);

		bookMap.put("Joel", 3);
		bookMapHindi.put("Joel", BahaStater.properties.getProperty(Constants.JOEL));
		chapterMap.put(Constants.JOEL + "-1", 20);
		chapterMap.put(Constants.JOEL + "-2", 32);
		chapterMap.put(Constants.JOEL + "-3", 21);
//		chapterMap.put(Constants.JOEL+"-4", 21);

		bookMap.put(Constants.AMOS, 9);
		bookMapHindi.put(Constants.AMOS, BahaStater.properties.getProperty(Constants.AMOS));
		chapterMap.put(Constants.AMOS + "-1", 15);
		chapterMap.put(Constants.AMOS + "-2", 16);
		chapterMap.put(Constants.AMOS + "-3", 15);
		chapterMap.put(Constants.AMOS + "-4", 13);
		chapterMap.put(Constants.AMOS + "-5", 27);
		chapterMap.put(Constants.AMOS + "-6", 14);
		chapterMap.put(Constants.AMOS + "-7", 17);
		chapterMap.put(Constants.AMOS + "-8", 14);
		chapterMap.put(Constants.AMOS + "-9", 15);

		bookMap.put(Constants.OBADIAH, 1);
		bookMapHindi.put(Constants.OBADIAH, BahaStater.properties.getProperty(Constants.OBADIAH));
		chapterMap.put(Constants.OBADIAH + "-1", 21);

		bookMap.put(Constants.JONAH, 4);
		bookMapHindi.put(Constants.JONAH, BahaStater.properties.getProperty(Constants.JONAH));
		chapterMap.put(Constants.JONAH + "-1", 17);
		chapterMap.put(Constants.JONAH + "-2", 10);
		chapterMap.put(Constants.JONAH + "-3", 10);
		chapterMap.put(Constants.JONAH + "-4", 11);

		bookMap.put(Constants.MICAH, 7);
		bookMapHindi.put(Constants.MICAH, BahaStater.properties.getProperty(Constants.MICAH));
		chapterMap.put(Constants.MICAH + "-1", 16);
		chapterMap.put(Constants.MICAH + "-2", 13);
		chapterMap.put(Constants.MICAH + "-3", 12);
		chapterMap.put(Constants.MICAH + "-4", 13);
		chapterMap.put(Constants.MICAH + "-5", 15);
		chapterMap.put(Constants.MICAH + "-6", 16);
		chapterMap.put(Constants.MICAH + "-7", 20);

		bookMap.put(Constants.NAHUM, 3);
		bookMapHindi.put(Constants.NAHUM, BahaStater.properties.getProperty(Constants.NAHUM));
		chapterMap.put(Constants.NAHUM + "-1", 15);
		chapterMap.put(Constants.NAHUM + "-2", 13);
		chapterMap.put(Constants.NAHUM + "-3", 19);

		bookMap.put(Constants.HABAKKUK, 3);
		bookMapHindi.put(Constants.HABAKKUK, BahaStater.properties.getProperty(Constants.HABAKKUK));
		chapterMap.put(Constants.HABAKKUK + "-1", 17);
		chapterMap.put(Constants.HABAKKUK + "-2", 20);
		chapterMap.put(Constants.HABAKKUK + "-3", 19);

		bookMap.put(Constants.ZEPHANIAH, 3);
		bookMapHindi.put(Constants.ZEPHANIAH, BahaStater.properties.getProperty(Constants.ZEPHANIAH));
		chapterMap.put(Constants.ZEPHANIAH + "-1", 18);
		chapterMap.put(Constants.ZEPHANIAH + "-2", 15);
		chapterMap.put(Constants.ZEPHANIAH + "-3", 20);

		bookMap.put(Constants.HAGGAI, 2);
		bookMapHindi.put(Constants.HAGGAI, BahaStater.properties.getProperty(Constants.HAGGAI));
		chapterMap.put(Constants.HAGGAI + "-1", 15);
		chapterMap.put(Constants.HAGGAI + "-2", 23);

		bookMap.put(Constants.ZECHARIAH, 14);
		bookMapHindi.put(Constants.ZECHARIAH, BahaStater.properties.getProperty(Constants.ZECHARIAH));
		chapterMap.put(Constants.ZECHARIAH + "-1", 21);
		chapterMap.put(Constants.ZECHARIAH + "-2", 13);
		chapterMap.put(Constants.ZECHARIAH + "-3", 10);
		chapterMap.put(Constants.ZECHARIAH + "-4", 14);
		chapterMap.put(Constants.ZECHARIAH + "-5", 11);
		chapterMap.put(Constants.ZECHARIAH + "-6", 15);
		chapterMap.put(Constants.ZECHARIAH + "-7", 14);
		chapterMap.put(Constants.ZECHARIAH + "-8", 23);
		chapterMap.put(Constants.ZECHARIAH + "-9", 17);
		chapterMap.put(Constants.ZECHARIAH + "-10", 12);
		chapterMap.put(Constants.ZECHARIAH + "-11", 17);
		chapterMap.put(Constants.ZECHARIAH + "-12", 14);
		chapterMap.put(Constants.ZECHARIAH + "-13", 9);
		chapterMap.put(Constants.ZECHARIAH + "-14", 21);

		bookMap.put(Constants.MALACHI, 4);
		bookMapHindi.put(Constants.MALACHI, BahaStater.properties.getProperty(Constants.MALACHI));
		chapterMap.put(Constants.MALACHI + "-1", 14);
		chapterMap.put(Constants.MALACHI + "-2", 17);
		chapterMap.put(Constants.MALACHI + "-3", 18);
		chapterMap.put(Constants.MALACHI + "-4", 6);

		bookMap.put(Constants.MATTHEW, 28);
		bookMapHindi.put(Constants.MATTHEW, BahaStater.properties.getProperty(Constants.MATTHEW));
		chapterMap.put(Constants.MATTHEW + "-1", 25);
		chapterMap.put(Constants.MATTHEW + "-2", 23);
		chapterMap.put(Constants.MATTHEW + "-3", 17);
		chapterMap.put(Constants.MATTHEW + "-4", 25);
		chapterMap.put(Constants.MATTHEW + "-5", 48);
		chapterMap.put(Constants.MATTHEW + "-6", 34);
		chapterMap.put(Constants.MATTHEW + "-7", 29);
		chapterMap.put(Constants.MATTHEW + "-8", 34);
		chapterMap.put(Constants.MATTHEW + "-9", 38);
		chapterMap.put(Constants.MATTHEW + "-10", 42);
		chapterMap.put(Constants.MATTHEW + "-11", 30);
		chapterMap.put(Constants.MATTHEW + "-12", 50);
		chapterMap.put(Constants.MATTHEW + "-13", 58);
		chapterMap.put(Constants.MATTHEW + "-14", 36);
		chapterMap.put(Constants.MATTHEW + "-15", 39);
		chapterMap.put(Constants.MATTHEW + "-16", 28);
		chapterMap.put(Constants.MATTHEW + "-17", 27);
		chapterMap.put(Constants.MATTHEW + "-18", 35);
		chapterMap.put(Constants.MATTHEW + "-19", 30);
		chapterMap.put(Constants.MATTHEW + "-20", 34);
		chapterMap.put(Constants.MATTHEW + "-21", 46);
		chapterMap.put(Constants.MATTHEW + "-22", 46);
		chapterMap.put(Constants.MATTHEW + "-23", 39);
		chapterMap.put(Constants.MATTHEW + "-24", 51);
		chapterMap.put(Constants.MATTHEW + "-25", 46);
		chapterMap.put(Constants.MATTHEW + "-26", 75);
		chapterMap.put(Constants.MATTHEW + "-27", 66);
		chapterMap.put(Constants.MATTHEW + "-28", 20);

		bookMap.put(Constants.MARK, 16);
		bookMapHindi.put(Constants.MARK, BahaStater.properties.getProperty(Constants.MARK));
		chapterMap.put(Constants.MARK + "-1", 45);
		chapterMap.put(Constants.MARK + "-2", 28);
		chapterMap.put(Constants.MARK + "-3", 35);
		chapterMap.put(Constants.MARK + "-4", 41);
		chapterMap.put(Constants.MARK + "-5", 43);
		chapterMap.put(Constants.MARK + "-6", 56);
		chapterMap.put(Constants.MARK + "-7", 37);
		chapterMap.put(Constants.MARK + "-8", 38);
		chapterMap.put(Constants.MARK + "-9", 50);
		chapterMap.put(Constants.MARK + "-10", 52);
		chapterMap.put(Constants.MARK + "-11", 33);
		chapterMap.put(Constants.MARK + "-12", 44);
		chapterMap.put(Constants.MARK + "-13", 37);
		chapterMap.put(Constants.MARK + "-14", 72);
		chapterMap.put(Constants.MARK + "-15", 47);
		chapterMap.put(Constants.MARK + "-16", 20);

		bookMap.put(Constants.LUKE, 24);
		bookMapHindi.put(Constants.LUKE, BahaStater.properties.getProperty(Constants.LUKE));
		chapterMap.put(Constants.LUKE + "-1", 80);
		chapterMap.put(Constants.LUKE + "-2", 52);
		chapterMap.put(Constants.LUKE + "-3", 38);
		chapterMap.put(Constants.LUKE + "-4", 44);
		chapterMap.put(Constants.LUKE + "-5", 39);
		chapterMap.put(Constants.LUKE + "-6", 49);
		chapterMap.put(Constants.LUKE + "-7", 50);
		chapterMap.put(Constants.LUKE + "-8", 56);
		chapterMap.put(Constants.LUKE + "-9", 62);
		chapterMap.put(Constants.LUKE + "-10", 42);
		chapterMap.put(Constants.LUKE + "-11", 54);
		chapterMap.put(Constants.LUKE + "-12", 59);
		chapterMap.put(Constants.LUKE + "-13", 35);
		chapterMap.put(Constants.LUKE + "-14", 35);
		chapterMap.put(Constants.LUKE + "-15", 32);
		chapterMap.put(Constants.LUKE + "-16", 31);
		chapterMap.put(Constants.LUKE + "-17", 37);
		chapterMap.put(Constants.LUKE + "-18", 43);
		chapterMap.put(Constants.LUKE + "-19", 48);
		chapterMap.put(Constants.LUKE + "-20", 47);
		chapterMap.put(Constants.LUKE + "-21", 38);
		chapterMap.put(Constants.LUKE + "-22", 71);
		chapterMap.put(Constants.LUKE + "-23", 56);
		chapterMap.put(Constants.LUKE + "-24", 53);

		bookMap.put(Constants.JOHN, 21);
		bookMapHindi.put(Constants.JOHN, BahaStater.properties.getProperty(Constants.JOHN));
		chapterMap.put(Constants.JOHN + "-1", 51);
		chapterMap.put(Constants.JOHN + "-2", 25);
		chapterMap.put(Constants.JOHN + "-3", 36);
		chapterMap.put(Constants.JOHN + "-4", 54);
		chapterMap.put(Constants.JOHN + "-5", 47);
		chapterMap.put(Constants.JOHN + "-6", 71);
		chapterMap.put(Constants.JOHN + "-7", 53);
		chapterMap.put(Constants.JOHN + "-8", 59);
		chapterMap.put(Constants.JOHN + "-9", 41);
		chapterMap.put(Constants.JOHN + "-10", 42);
		chapterMap.put(Constants.JOHN + "-11", 57);
		chapterMap.put(Constants.JOHN + "-12", 50);
		chapterMap.put(Constants.JOHN + "-13", 38);
		chapterMap.put(Constants.JOHN + "-14", 31);
		chapterMap.put(Constants.JOHN + "-15", 27);
		chapterMap.put(Constants.JOHN + "-16", 33);
		chapterMap.put(Constants.JOHN + "-17", 26);
		chapterMap.put(Constants.JOHN + "-18", 40);
		chapterMap.put(Constants.JOHN + "-19", 42);
		chapterMap.put(Constants.JOHN + "-20", 31);
		chapterMap.put(Constants.JOHN + "-21", 25);

		bookMap.put(Constants.ACTS, 28);
		bookMapHindi.put(Constants.ACTS, BahaStater.properties.getProperty(Constants.ACTS));
		chapterMap.put(Constants.ACTS + "-1", 26);
		chapterMap.put(Constants.ACTS + "-2", 47);
		chapterMap.put(Constants.ACTS + "-3", 26);
		chapterMap.put(Constants.ACTS + "-4", 37);
		chapterMap.put(Constants.ACTS + "-5", 42);
		chapterMap.put(Constants.ACTS + "-6", 15);
		chapterMap.put(Constants.ACTS + "-7", 60);
		chapterMap.put(Constants.ACTS + "-8", 40);
		chapterMap.put(Constants.ACTS + "-9", 43);
		chapterMap.put(Constants.ACTS + "-10", 48);
		chapterMap.put(Constants.ACTS + "-11", 30);
		chapterMap.put(Constants.ACTS + "-12", 25);
		chapterMap.put(Constants.ACTS + "-13", 52);
		chapterMap.put(Constants.ACTS + "-14", 28);
		chapterMap.put(Constants.ACTS + "-15", 41);
		chapterMap.put(Constants.ACTS + "-16", 40);
		chapterMap.put(Constants.ACTS + "-17", 34);
		chapterMap.put(Constants.ACTS + "-18", 28);
		chapterMap.put(Constants.ACTS + "-19", 41);
		chapterMap.put(Constants.ACTS + "-20", 38);
		chapterMap.put(Constants.ACTS + "-21", 40);
		chapterMap.put(Constants.ACTS + "-22", 30);
		chapterMap.put(Constants.ACTS + "-23", 35);
		chapterMap.put(Constants.ACTS + "-24", 27);
		chapterMap.put(Constants.ACTS + "-25", 27);
		chapterMap.put(Constants.ACTS + "-26", 32);
		chapterMap.put(Constants.ACTS + "-27", 44);
		chapterMap.put(Constants.ACTS + "-28", 31);

		bookMap.put(Constants.ROMANS, 16);
		bookMapHindi.put(Constants.ROMANS, BahaStater.properties.getProperty(Constants.ROMANS));
		chapterMap.put(Constants.ROMANS + "-1", 32);
		chapterMap.put(Constants.ROMANS + "-2", 29);
		chapterMap.put(Constants.ROMANS + "-3", 31);
		chapterMap.put(Constants.ROMANS + "-4", 25);
		chapterMap.put(Constants.ROMANS + "-5", 21);
		chapterMap.put(Constants.ROMANS + "-6", 23);
		chapterMap.put(Constants.ROMANS + "-7", 25);
		chapterMap.put(Constants.ROMANS + "-8", 39);
		chapterMap.put(Constants.ROMANS + "-9", 33);
		chapterMap.put(Constants.ROMANS + "-10", 21);
		chapterMap.put(Constants.ROMANS + "-11", 36);
		chapterMap.put(Constants.ROMANS + "-12", 21);
		chapterMap.put(Constants.ROMANS + "-13", 14);
		chapterMap.put(Constants.ROMANS + "-14", 23);
		chapterMap.put(Constants.ROMANS + "-15", 33);
		chapterMap.put(Constants.ROMANS + "-16", 27);

		bookMap.put(Constants.CORINTHIANS1_FILE, 16);
		bookMapHindi.put(Constants.CORINTHIANS1_FILE, BahaStater.properties.getProperty(Constants.CORINTHIANS1_PROP));
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-1", 31);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-2", 16);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-3", 23);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-4", 21);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-5", 13);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-6", 20);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-7", 40);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-8", 13);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-9", 27);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-10", 33);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-11", 34);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-12", 31);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-13", 13);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-14", 40);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-15", 58);
		chapterMap.put(Constants.CORINTHIANS1_FILE + "-16", 24);

		bookMap.put(Constants.CORINTHIANS2_FILE, 13);
		bookMapHindi.put(Constants.CORINTHIANS2_FILE, BahaStater.properties.getProperty(Constants.CORINTHIANS2_PROP));
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-1", 24);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-2", 17);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-3", 18);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-4", 18);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-5", 21);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-6", 18);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-7", 16);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-8", 24);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-9", 15);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-10", 18);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-11", 33);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-12", 21);
		chapterMap.put(Constants.CORINTHIANS2_FILE + "-13", 14);

		bookMap.put(Constants.GALATIANS, 6);
		bookMapHindi.put(Constants.GALATIANS, BahaStater.properties.getProperty(Constants.GALATIANS));
		chapterMap.put(Constants.GALATIANS + "-1", 24);
		chapterMap.put(Constants.GALATIANS + "-2", 21);
		chapterMap.put(Constants.GALATIANS + "-3", 29);
		chapterMap.put(Constants.GALATIANS + "-4", 31);
		chapterMap.put(Constants.GALATIANS + "-5", 26);
		chapterMap.put(Constants.GALATIANS + "-6", 18);

		bookMap.put(Constants.EPHESIANS, 6);
		bookMapHindi.put(Constants.EPHESIANS, BahaStater.properties.getProperty(Constants.EPHESIANS));
		chapterMap.put(Constants.EPHESIANS + "-1", 23);
		chapterMap.put(Constants.EPHESIANS + "-2", 22);
		chapterMap.put(Constants.EPHESIANS + "-3", 21);
		chapterMap.put(Constants.EPHESIANS + "-4", 32);
		chapterMap.put(Constants.EPHESIANS + "-5", 33);
		chapterMap.put(Constants.EPHESIANS + "-6", 24);

		bookMap.put(Constants.PHILIPPIANS, 4);
		bookMapHindi.put(Constants.PHILIPPIANS, BahaStater.properties.getProperty(Constants.PHILIPPIANS));
		chapterMap.put(Constants.PHILIPPIANS + "-1", 30);
		chapterMap.put(Constants.PHILIPPIANS + "-2", 30);
		chapterMap.put(Constants.PHILIPPIANS + "-3", 21);
		chapterMap.put(Constants.PHILIPPIANS + "-4", 23);

		bookMap.put(Constants.COLOSSIANS, 4);
		bookMapHindi.put(Constants.COLOSSIANS, BahaStater.properties.getProperty(Constants.COLOSSIANS));
		chapterMap.put(Constants.COLOSSIANS + "-1", 29);
		chapterMap.put(Constants.COLOSSIANS + "-2", 23);
		chapterMap.put(Constants.COLOSSIANS + "-3", 25);
		chapterMap.put(Constants.COLOSSIANS + "-4", 18);

		bookMap.put(Constants.THESSALONIANS1_FILE, 5);
		bookMapHindi.put(Constants.THESSALONIANS1_FILE,
				BahaStater.properties.getProperty(Constants.THESSALONIANS1_PROP));
		chapterMap.put(Constants.THESSALONIANS1_FILE + "-1", 10);
		chapterMap.put(Constants.THESSALONIANS1_FILE + "-2", 20);
		chapterMap.put(Constants.THESSALONIANS1_FILE + "-3", 13);
		chapterMap.put(Constants.THESSALONIANS1_FILE + "-4", 18);
		chapterMap.put(Constants.THESSALONIANS1_FILE + "-5", 28);

		bookMap.put(Constants.THESSALONIANS2_FILE, 3);
		bookMapHindi.put(Constants.THESSALONIANS2_FILE,
				BahaStater.properties.getProperty(Constants.THESSALONIANS2_PROP));
		chapterMap.put(Constants.THESSALONIANS2_FILE + "-1", 12);
		chapterMap.put(Constants.THESSALONIANS2_FILE + "-2", 17);
		chapterMap.put(Constants.THESSALONIANS2_FILE + "-3", 18);

		bookMap.put(Constants.TIMOTHY1_FILE, 6);
		bookMapHindi.put(Constants.TIMOTHY1_FILE, BahaStater.properties.getProperty(Constants.TIMOTHY1_PROP));
		chapterMap.put(Constants.TIMOTHY1_FILE + "-1", 20);
		chapterMap.put(Constants.TIMOTHY1_FILE + "-2", 15);
		chapterMap.put(Constants.TIMOTHY1_FILE + "-3", 16);
		chapterMap.put(Constants.TIMOTHY1_FILE + "-4", 16);
		chapterMap.put(Constants.TIMOTHY1_FILE + "-5", 25);
		chapterMap.put(Constants.TIMOTHY1_FILE + "-6", 21);

		bookMap.put(Constants.TIMOTHY2_FILE, 4);
		bookMapHindi.put(Constants.TIMOTHY2_FILE, BahaStater.properties.getProperty(Constants.TIMOTHY2_PROP));
		chapterMap.put(Constants.TIMOTHY2_FILE + "-1", 18);
		chapterMap.put(Constants.TIMOTHY2_FILE + "-2", 26);
		chapterMap.put(Constants.TIMOTHY2_FILE + "-3", 17);
		chapterMap.put(Constants.TIMOTHY2_FILE + "-4", 22);

		bookMap.put(Constants.TITUS, 3);
		bookMapHindi.put(Constants.TITUS, BahaStater.properties.getProperty(Constants.TITUS));
		chapterMap.put(Constants.TITUS + "-1", 16);
		chapterMap.put(Constants.TITUS + "-2", 15);
		chapterMap.put(Constants.TITUS + "-3", 15);

		bookMap.put(Constants.PHILEMON, 1);
		bookMapHindi.put(Constants.PHILEMON, BahaStater.properties.getProperty(Constants.PHILEMON));
		chapterMap.put(Constants.PHILEMON + "-1", 25);

		bookMap.put(Constants.HEBREWS, 13);
		bookMapHindi.put(Constants.HEBREWS, BahaStater.properties.getProperty(Constants.HEBREWS));
		chapterMap.put(Constants.HEBREWS + "-1", 14);
		chapterMap.put(Constants.HEBREWS + "-2", 18);
		chapterMap.put(Constants.HEBREWS + "-3", 19);
		chapterMap.put(Constants.HEBREWS + "-4", 16);
		chapterMap.put(Constants.HEBREWS + "-5", 14);
		chapterMap.put(Constants.HEBREWS + "-6", 20);
		chapterMap.put(Constants.HEBREWS + "-7", 28);
		chapterMap.put(Constants.HEBREWS + "-8", 13);
		chapterMap.put(Constants.HEBREWS + "-9", 28);
		chapterMap.put(Constants.HEBREWS + "-10", 39);
		chapterMap.put(Constants.HEBREWS + "-11", 40);
		chapterMap.put(Constants.HEBREWS + "-12", 29);
		chapterMap.put(Constants.HEBREWS + "-13", 25);

		bookMap.put(Constants.JAMES, 5);
		bookMapHindi.put(Constants.JAMES, BahaStater.properties.getProperty(Constants.JAMES));
		chapterMap.put(Constants.JAMES + "-1", 27);
		chapterMap.put(Constants.JAMES + "-2", 26);
		chapterMap.put(Constants.JAMES + "-3", 18);
		chapterMap.put(Constants.JAMES + "-4", 17);
		chapterMap.put(Constants.JAMES + "-5", 20);

		bookMap.put(Constants.PETER1_FILE, 5);
		bookMapHindi.put(Constants.PETER1_FILE, BahaStater.properties.getProperty(Constants.PETER1_PROP));
		chapterMap.put(Constants.PETER1_FILE + "-1", 25);
		chapterMap.put(Constants.PETER1_FILE + "-2", 25);
		chapterMap.put(Constants.PETER1_FILE + "-3", 22);
		chapterMap.put(Constants.PETER1_FILE + "-4", 19);
		chapterMap.put(Constants.PETER1_FILE + "-5", 14);

		bookMap.put(Constants.PETER2_FILE, 3);
		bookMapHindi.put(Constants.PETER2_FILE, BahaStater.properties.getProperty(Constants.PETER2_PROP));
		chapterMap.put(Constants.PETER2_FILE + "-1", 21);
		chapterMap.put(Constants.PETER2_FILE + "-2", 22);
		chapterMap.put(Constants.PETER2_FILE + "-3", 18);

		bookMap.put(Constants.JOHN1_FILE, 5);
		bookMapHindi.put(Constants.JOHN1_FILE, BahaStater.properties.getProperty(Constants.JOHN1_PROP));
		chapterMap.put(Constants.JOHN1_FILE + "-1", 10);
		chapterMap.put(Constants.JOHN1_FILE + "-2", 29);
		chapterMap.put(Constants.JOHN1_FILE + "-3", 24);
		chapterMap.put(Constants.JOHN1_FILE + "-4", 21);
		chapterMap.put(Constants.JOHN1_FILE + "-5", 21);

		bookMap.put(Constants.JOHN2_FILE, 1);
		bookMapHindi.put(Constants.JOHN2_FILE, BahaStater.properties.getProperty(Constants.JOHN2_PROP));
		chapterMap.put(Constants.JOHN2_FILE + "-1", 13);

		bookMap.put(Constants.JOHN3_FILE, 1);
		bookMapHindi.put(Constants.JOHN3_FILE, BahaStater.properties.getProperty(Constants.JOHN3_PROP));
		chapterMap.put(Constants.JOHN3_FILE + "-1", 14);

		bookMap.put(Constants.JUDE, 1);
		bookMapHindi.put(Constants.JUDE, BahaStater.properties.getProperty(Constants.JUDE));
		chapterMap.put(Constants.JUDE + "-1", 25);

		bookMap.put(Constants.REVELATION, 22);
		bookMapHindi.put(Constants.REVELATION, BahaStater.properties.getProperty(Constants.REVELATION));
		chapterMap.put(Constants.REVELATION + "-1", 20);
		chapterMap.put(Constants.REVELATION + "-2", 29);
		chapterMap.put(Constants.REVELATION + "-3", 22);
		chapterMap.put(Constants.REVELATION + "-4", 11);
		chapterMap.put(Constants.REVELATION + "-5", 14);
		chapterMap.put(Constants.REVELATION + "-6", 17);
		chapterMap.put(Constants.REVELATION + "-7", 17);
		chapterMap.put(Constants.REVELATION + "-8", 13);
		chapterMap.put(Constants.REVELATION + "-9", 21);
		chapterMap.put(Constants.REVELATION + "-10", 11);
		chapterMap.put(Constants.REVELATION + "-11", 19);
		chapterMap.put(Constants.REVELATION + "-12", 17);
		chapterMap.put(Constants.REVELATION + "-13", 18);
		chapterMap.put(Constants.REVELATION + "-14", 20);
		chapterMap.put(Constants.REVELATION + "-15", 8);
		chapterMap.put(Constants.REVELATION + "-16", 21);
		chapterMap.put(Constants.REVELATION + "-17", 18);
		chapterMap.put(Constants.REVELATION + "-18", 24);
		chapterMap.put(Constants.REVELATION + "-19", 21);
		chapterMap.put(Constants.REVELATION + "-20", 15);
		chapterMap.put(Constants.REVELATION + "-21", 27);
		chapterMap.put(Constants.REVELATION + "-22", 21);

		booksPane = new JPanel();
		booksPane.setBorder(new TitledBorder("Books"));
		// booksPane.setLayout(new GridLayout(10,0));
		booksPane.setLayout(new GridLayout(0, 9, 5, 5));
		booksPane.setBackground(Color.WHITE);
		JScrollPane bookScrollPane = new JScrollPane(booksPane);
		booksPane.setPreferredSize(
				new Dimension(screenSize.width - 20, Double.valueOf(screenSize.height * .35).intValue()));
		books = new JList<>(bookList);
		books.setBorder(new EmptyBorder(10, 10, 10, 10));
		books.setName("book");
		DefaultListCellRenderer bookRenderer = (DefaultListCellRenderer) books.getCellRenderer();
		bookRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		// books.setSelectedIndex(0);
		String selectedBook = books.getSelectedValue();
		final DefaultListModel<String> chapterList = new DefaultListModel<>();
		if (selectedBook != null) {
			int noOfChapters = bookMap.get(selectedBook);
			for (int i = 0; i < noOfChapters; i++) {
				chapterList.addElement("" + (i + 1));
			}
		}

		chapters = new JList<>(chapterList);
		DefaultListCellRenderer chapterRenderer = (DefaultListCellRenderer) chapters.getCellRenderer();
		chapterRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		final DefaultListModel<String> verseList = new DefaultListModel<>();
		books.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					chapterList.clear();
					verseList.clear();
					if (null != books.getSelectedValue() && bookMap.containsKey(books.getSelectedValue().toString())) {
						int noOfChapters = bookMap.get(books.getSelectedValue().toString());
						for (int i = 0; i < noOfChapters; i++) {
							chapterList.addElement("" + (i + 1));
						}
						chapters.setFont(font1);
						// chapters.setSelectedIndex(0);
					}
				}
			}
		});
		chapters.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		chapters.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		chapters.setVisibleRowCount(-1);
		for (String element : bookMap.keySet()) {
			bookList.addElement(element);
			JToggleButton btn = new JToggleButton(element + "|" + bookMapHindi.get(element) + "");
			btn.setFont(btnFont);
			// btn.setBorder(null);
			btn.setBackground(new Color(185, 255, 51));
			btn.setMargin(new Insets(1, 5, 1, 5));
			btn.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mousePressed(MouseEvent e) {
					JToggleButton sourceBtn = (JToggleButton) e.getSource();
					books.setSelectedValue(sourceBtn.getText().split("\\|")[0].trim(), true);
					chapterList.clear();
					verseList.clear();
					if (bookMap.containsKey(books.getSelectedValue().toString())) {
						int noOfChapters = bookMap.get(books.getSelectedValue().toString());
						for (int i = 0; i < noOfChapters; i++) {
							chapterList.addElement("" + (i + 1));
						}
					}
					for (Component comp : booksPane.getComponents()) {
						((JToggleButton) comp).setSelected(false);
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			if (booksPane.getComponents().length > 38) {
				btn.setBackground(new Color(255, 215, 0));
				booksPane.add(btn);
			} else {
				booksPane.add(btn);
			}
		}
		displayPanel.add(bookScrollPane, BorderLayout.PAGE_START);
		JScrollPane chapterPanel = new JScrollPane(chapters); // split the window in top and bottom
		chapters.setFixedCellHeight(25);
		chapters.setFixedCellWidth(40);
		chapters.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		chapterPanel.setPreferredSize(
				new Dimension(screenSize.width / 2 - 10, Double.valueOf(screenSize.height * .15).intValue()));
		chapterPanel.getVerticalScrollBar().setPreferredSize(new Dimension(1, 1));
		chapterPanel.setBorder(new TitledBorder("Chapters"));
		chapterPanel.setBackground(Color.WHITE);
		displayPanel.add(chapterPanel, BorderLayout.CENTER);

		verses = new JList<String>(verseList);
		DefaultListCellRenderer verseRenderer = (DefaultListCellRenderer) verses.getCellRenderer();
		verseRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		String selectedChapter = chapters.getSelectedValue();
		if (selectedChapter != null) {
			int noOfVerses = chapterMap.get(selectedBook + "-" + selectedChapter);
			for (int i = 0; i < noOfVerses; i++) {
				verseList.addElement("" + (i + 1));
			}
			// verses.setSelectedIndex(0);
		}
		chapters.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				verseList.clear();
				if (chapters.getSelectedValue() != null) {
					if (chapterMap.containsKey(
							books.getSelectedValue().toString() + "-" + chapters.getSelectedValue().toString())) {
						int noOfChapters = chapterMap.get(
								books.getSelectedValue().toString() + "-" + chapters.getSelectedValue().toString());
						for (int i = 0; i < noOfChapters; i++) {
							verseList.addElement("" + (i + 1));
						}
						verses.setFont(font1);

						if (books.getSelectedValue() != null && !books.getSelectedValue().isEmpty()
								&& null != chapters.getSelectedValue() && !chapters.getSelectedValue().isEmpty()) {
							if (!primaryCache
									.containsKey(books.getSelectedValue() + "-" + chapters.getSelectedValue())) {

								final File fl = new File(
										Constants.RESOURCES_PATH + books.getSelectedValue().toString() + ".xlsx");
								try (FileInputStream inp = new FileInputStream(fl)) {
									workbook = new XSSFWorkbook(inp);
									sheet = workbook.getSheetAt(0);
									Map<String, String> secondaryCache = new LinkedHashMap<String, String>();
									for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
										if (sheet.getRow(i) != null) {
											XSSFCell cell = sheet.getRow(i).getCell(0);
											XSSFCell cell1 = sheet.getRow(i).getCell(1);
											XSSFCell cell2 = sheet.getRow(i).getCell(2);
											XSSFCell cell3 = sheet.getRow(i).getCell(3);
											XSSFCell cell4 = sheet.getRow(i).getCell(4);
											if (null == cell || null == cell1) {
												continue;
											}
											DataFormatter formatter = new DataFormatter();
											String cellValue = formatter.formatCellValue(cell);
											String cell1Value = formatter.formatCellValue(cell1);
											String cell2Value = formatter.formatCellValue(cell2);
											String cell3Value = "";
											if (null != cell3)
												cell3Value = formatter.formatCellValue(cell3);
											String cell4Value = "";
											if (null != cell4)
												cell4Value = formatter.formatCellValue(cell4);

											if (cellValue.equals(books.getSelectedValue())
													&& cell1Value.equals(chapters.getSelectedValue())) {
												if (!cellValue.isEmpty() && !cell1Value.isEmpty()) {
													secondaryCache.put(cellValue + "-" + cell1Value + "-" + cell2Value,
															cell3Value + "#" + cell4Value);// +
												}
											}
										}
									}
									if (primaryCache.containsKey(
											books.getSelectedValue() + "-" + chapters.getSelectedValue())) {
										primaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue())
												.putAll(secondaryCache);
									} else {
										primaryCache.put(books.getSelectedValue() + "-" + chapters.getSelectedValue(),
												secondaryCache);
									}
									// setContentText(books, chapters, verses);
									// displayPanel.scrollRectToVisible(displayPanel.getBounds());
								} catch (FileNotFoundException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
						// verses.setSelectedIndex(0);
					}
				}
			}
		});
		JScrollPane versePanel = new JScrollPane(verses);
		// verses.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		verses.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		verses.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		verses.setVisibleRowCount(-1);
		verses.setFixedCellHeight(25);
		verses.setFixedCellWidth(40);
		verses.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		versePanel.setPreferredSize(
				new Dimension(screenSize.width / 2 - 10, Double.valueOf(screenSize.height * .15).intValue()));
		versePanel.getVerticalScrollBar().setPreferredSize(new Dimension(1, 1));
		versePanel.setBackground(Color.WHITE);
		versePanel.setBorder(new TitledBorder("Verses"));
		displayPanel.add(versePanel, BorderLayout.CENTER);
		wordsPanel.setBackground(Color.WHITE);
		JPanel panel1 = new JPanel();
		JPanel colorPane = new JPanel();
		colorPane.setLayout(new GridLayout());
		// JPanel area1 = new JPanel();
		panel1.add(colorPane);
		GridLayout gl = new GridLayout(0, 4, 1, 1);
		panelMru.setLayout(gl);
		panelMru.setBorder(new TitledBorder("Recent"));
		// panel1.add(panelMru);
		panelMru.setBackground(Color.WHITE);
		JScrollPane scrollContent = new JScrollPane(panelMru);
		scrollContent.setPreferredSize(
				new Dimension((screenSize.width / 2) - 10, Double.valueOf(screenSize.height * .30).intValue()));
		panel1.add(scrollContent);
		panel1.setBackground(Color.WHITE);
		panel1.setPreferredSize(
				new Dimension((screenSize.width / 2) - 10, Double.valueOf(screenSize.height * .30).intValue()));
		// panel1.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
//		area1.setBorder(new EmptyBorder(10, 10, 10, 10));
//		area1.setEditable(false);
//		area1.setLineWrap(true);
//		area1.setWrapStyleWord(true);
//		area1.setAutoscrolls(true);
//		area1.setFont(dFont);
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		wordsPanel.add(panel1, BorderLayout.CENTER);
		JTextArea area2 = new JTextArea();
		area2.setPreferredSize(
				new Dimension(panel1.getPreferredSize().width - 10, panel1.getPreferredSize().height - 10));
		area2.setEditable(false);
		area2.setLineWrap(true);
		area2.setWrapStyleWord(true);
		area2.setAutoscrolls(true);
		area2.setFont(font1);
		panel2.add(area2);
		panel2.setPreferredSize(panel1.getPreferredSize());
//		panel2.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		wordsPanel.add(panel2, BorderLayout.CENTER);
		wordsPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		displayPanel.add(wordsPanel, BorderLayout.SOUTH);
		displayPanel.setBackground(Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
		verses.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					try {
						if (books.getSelectedValue() == null || chapters.getSelectedValue() == null
								|| verses.getSelectedValue() == null)
							return;
						String versionEng = BAHAMenu.getMenu().kjv.isSelected() ? "kjv" : "";
						if (null == versionEng || versionEng == "")
							versionEng = BAHAMenu.getMenu().nkjv.isSelected() ? "nkjv" : "";
						if (null == versionEng || versionEng == "")
							versionEng = BAHAMenu.getMenu().amp.isSelected() ? "amp" : "";
						if (null == versionEng || versionEng == "")
							versionEng = BAHAMenu.getMenu().niv.isSelected() ? "niv" : "";
						BibleResponseDTO bibleResponseDTO = HttpUtil.getOnlineVerseForTranslation(versionEng,
								books.getSelectedValue(), "" + (books.getSelectedIndex() + 1),
								"" + (chapters.getSelectedIndex() + 1), "" + (verses.getSelectedIndex() + 1));
						if (MapUtils.isNotEmpty(primaryCache)) {
							Map<String, String> secondaryCache = primaryCache
									.get(books.getSelectedValue() + "-" + chapters.getSelectedValue());
							if (MapUtils.isNotEmpty(secondaryCache)) {
								String hindi = secondaryCache.get(books.getSelectedValue() + "-"
										+ chapters.getSelectedValue() + "-" + verses.getSelectedValue())
										.split("\\#")[1];

								secondaryCache.put(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
										+ verses.getSelectedValue(), bibleResponseDTO.getText() + "#" + hindi);

							}
						}
					} catch (IOException e1) {
						BAHAMenu.getMenu().kjv.setBackground(Color.LIGHT_GRAY);
						BAHAMenu.getMenu().nkjv.setBackground(Color.WHITE);
						BAHAMenu.getMenu().amp.setBackground(Color.WHITE);
						BAHAMenu.getMenu().niv.setBackground(Color.WHITE);
						BAHAMenu.getMenu().kjv.setSelected(true);
						BAHAMenu.getMenu().nkjv.setSelected(false);
						BAHAMenu.getMenu().amp.setSelected(false);
						BAHAMenu.getMenu().niv.setSelected(false);
					} catch (InterruptedException e1) {
						BAHAMenu.getMenu().kjv.setBackground(Color.LIGHT_GRAY);
						BAHAMenu.getMenu().nkjv.setBackground(Color.WHITE);
						BAHAMenu.getMenu().amp.setBackground(Color.WHITE);
						BAHAMenu.getMenu().niv.setBackground(Color.WHITE);
						BAHAMenu.getMenu().kjv.setSelected(true);
						BAHAMenu.getMenu().nkjv.setSelected(false);
						BAHAMenu.getMenu().amp.setSelected(false);
						BAHAMenu.getMenu().niv.setSelected(false);
					}
					setContentText(books, chapters, verses);
				}

			}
		});

		chapters.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					JList list = (JList) e.getSource();
					int preferredHeight = list.getPreferredSize().height;
					int mouseHeight = e.getPoint().y;

					if (mouseHeight > preferredHeight)
						return;

					int row = list.locationToIndex(e.getPoint());
					Map<String, String> secondaryCache = primaryCache
							.get(books.getSelectedValue() + "-" + chapters.getSelectedValue());
					JDialog dialog = new JDialog(BahaStater.initFrame, "Verse", true);
					String content = "";
					for (String value : secondaryCache.values()) {
						content += value.split("#")[1] + "\r\n";
					}
					dialog.setMinimumSize(new Dimension(400, 300));
					JTextArea jta = new JTextArea(15, 55);
					jta.setFont(new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, 12));
					jta.setText(content);
					jta.setEditable(false);
					JScrollPane jsp = new JScrollPane(jta);
					JOptionPane.showMessageDialog(null, jsp,
							books.getSelectedValue() + " " + chapters.getSelectedValue(), 1);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});

		verses.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					JList list = (JList) e.getSource();
					int preferredHeight = list.getPreferredSize().height;
					int mouseHeight = e.getPoint().y;

					if (mouseHeight > preferredHeight)
						return;

					int row = list.locationToIndex(e.getPoint());
					Map<String, String> secondaryCache = primaryCache
							.get(books.getSelectedValue() + "-" + chapters.getSelectedValue());

					JDialog dialog = new JDialog(BahaStater.initFrame, "Verse", true);
					String content = "";
					for (String value : secondaryCache.values()) {
						content += value.split("#")[1] + "\r\n";
					}
					dialog.setMinimumSize(new Dimension(400, 300));
					JTextArea jta = new JTextArea(15, 55);
					jta.setFont(new Font(BahaStater.properties.getProperty(Constants.FONT), Font.PLAIN, 12));
					jta.setText(content);
					jta.setEditable(false);
					JScrollPane jsp = new JScrollPane(jta);
					JOptionPane.showMessageDialog(null, jsp,
							books.getSelectedValue() + " " + chapters.getSelectedValue(), 1);
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					setContentText(books, chapters, verses);
					BahaStater.copyToClipboard(selectedChapter);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});

		BahaStater.initFrame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (e.getComponent().getSize().width < screenSize.width * .8) {
					booksPane.setPreferredSize(
							new Dimension(Double.valueOf(e.getComponent().getSize().width * .98).intValue(),
									Double.valueOf(e.getComponent().getSize().height * .35).intValue()));
					booksPane.setLayout(new GridLayout(0,
							Double.valueOf(e.getComponent().getSize().width / (e.getComponent().getSize().width * .1))
									.intValue(),
							2, 2));
					chapterPanel.setPreferredSize(
							new Dimension(Double.valueOf(e.getComponent().getSize().width * .94).intValue(),
									Double.valueOf(e.getComponent().getSize().height * .20).intValue()));
					versePanel.setPreferredSize(
							new Dimension(Double.valueOf(e.getComponent().getSize().width * .94).intValue(),
									Double.valueOf(e.getComponent().getSize().height * .20).intValue()));
				} else {
					booksPane.setPreferredSize(
							new Dimension(Double.valueOf(e.getComponent().getSize().width * .98).intValue(),
									Double.valueOf(screenSize.height * .35).intValue()));
					chapterPanel.setPreferredSize(
							new Dimension(Double.valueOf(e.getComponent().getSize().width * .49).intValue(),
									Double.valueOf(e.getComponent().getSize().height * .20).intValue()));
					versePanel.setPreferredSize(
							new Dimension(Double.valueOf(e.getComponent().getSize().width * .49).intValue(),
									Double.valueOf(e.getComponent().getSize().height * .20).intValue()));
					booksPane.setLayout(new GridLayout(0, 9, 5, 5));
				}

//		    	  booksPane.setSize(e.getComponent().getSize());
				booksPane.revalidate();
				booksPane.repaint();
				bookScrollPane.setSize(e.getComponent().getSize());
				bookScrollPane.revalidate();
				bookScrollPane.repaint();
				chapterPanel.setSize(e.getComponent().getSize());
				chapterPanel.revalidate();
				chapterPanel.repaint();
				versePanel.setSize(e.getComponent().getSize());
				versePanel.revalidate();
				versePanel.repaint();
				BahaStater.initFrame.revalidate();
				BahaStater.initFrame.repaint();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}
		});
	}

	public static VerseView getVerseView() {
		if (obj == null) {
			synchronized (VerseView.class) {
				if (obj == null) {
					obj = new VerseView();// instance will be created at request time
				}
			}
		}
		return obj;
	}

	public JPanel getVerseViewPanel() {
		return displayPanel;
	}

	public void setContentText(JList books, JList chapters, JList verses) {
		// wordsPanel.removeAll();

		if (books.getSelectedValue() == null || chapters.getSelectedValue() == null
				|| verses.getSelectedValue() == null)
			return;

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

		JTextPane label = (JTextPane) BahaStater.label;
		Map<String, String> secondaryCache = primaryCache
				.get(books.getSelectedValue() + "-" + chapters.getSelectedValue());

		String versionEng = BAHAMenu.getMenu().kjv.isSelected() ? BAHAMenu.getMenu().kjv.getText() : "";
		if (null == versionEng || versionEng == "")
			versionEng = BAHAMenu.getMenu().nkjv.isSelected() ? BAHAMenu.getMenu().nkjv.getText() : "";
		if (null == versionEng || versionEng == "")
			versionEng = BAHAMenu.getMenu().amp.isSelected() ? BAHAMenu.getMenu().amp.getText() : "";
		if (null == versionEng || versionEng == "")
			versionEng = BAHAMenu.getMenu().niv.isSelected() ? BAHAMenu.getMenu().niv.getText() : "";
		final String version = versionEng;
		if (null != secondaryCache && secondaryCache.containsKey(
				books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-" + verses.getSelectedValue())) {
			String contentFontCalc = bookMapHindi.get(books.getSelectedValue()) + " " + chapters.getSelectedValue()
					+ ":" + verses.getSelectedValue()
					+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
							+ verses.getSelectedValue()).split("\\#")[1]
					+ books.getSelectedValue() + " " + chapters.getSelectedValue() + ":" + verses.getSelectedValue()
					+ " " + version + "" + secondaryCache.get(books.getSelectedValue() + "-"
							+ chapters.getSelectedValue() + "-" + verses.getSelectedValue()).split("\\#")[0];
			String contentFontCalcPrimary = bookMapHindi.get(books.getSelectedValue()) + " " + chapters.getSelectedValue()+ ":" + verses.getSelectedValue()
			+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
					+ verses.getSelectedValue()).split("\\#")[1];
	
			String contentFontCalcSecondary = books.getSelectedValue() + " " + chapters.getSelectedValue()+ ":" + verses.getSelectedValue()
			+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
					+ verses.getSelectedValue()).split("\\#")[0];
			new Thread(new Runnable() {
				@Override
				public void run() {
					String content = books.getSelectedValue() + " " + chapters.getSelectedValue() + ":"
							+ verses.getSelectedValue() + "  " + version + "\r\n"
							+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
									+ verses.getSelectedValue()).split("\\#")[1]
							+ "\r\n" + secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue()
									+ "-" + verses.getSelectedValue()).split("\\#")[0]
							+ "\r\n";

					JPanel panel1 = (JPanel) wordsPanel.getComponent(0);
					JPanel panel2 = (JPanel) wordsPanel.getComponent(1);

					JTextArea area2 = (JTextArea) panel2.getComponent(0);
					area2.setText(content);
				}
			}).start();
			int fontSizeBoth = BahaStater.calculateFontSizeVerse(contentFontCalc, BahaStater.lwidth, BahaStater.lheight, 0,
					10);
			int fontSizeHindi = BahaStater.calculateFontSizeVerse(contentFontCalcPrimary, BahaStater.lwidth, BahaStater.lheight, 0,
					10);
			int fontSizeEng = BahaStater.calculateFontSizeVerse(contentFontCalcSecondary, BahaStater.lwidth, BahaStater.lheight, 0,
					10);
			mruCache.putIfAbsent(
					books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-" + verses.getSelectedValue(),
					secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
							+ verses.getSelectedValue()));
			panelMru.removeAll();
			for (String mrukey : mruCache.keySet()) {
				JButton jb = new JButton(mrukey);
				jb.setBorder(BorderFactory.createBevelBorder(0, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
				jb.setForeground(Color.BLACK);

				jb.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						e.getComponent().setBackground(
								Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
					}

					@Override
					public void mousePressed(MouseEvent e) {
						books.setSelectedValue(jb.getText().split("\\-")[0], false);
						chapters.setSelectedValue(jb.getText().split("\\-")[1], false);
						verses.setSelectedValue(jb.getText().split("\\-")[2], false);
						Arrays.asList(booksPane.getComponents()).parallelStream()
								.filter(f -> ((JToggleButton) f).isSelected())
								.forEach(c -> ((JToggleButton) c).setSelected(false));
						((JToggleButton) booksPane.getComponents()[books.getSelectedIndex()]).setSelected(true);
						e.getComponent().setBackground(
								Color.decode(BahaStater.properties.getProperty(Constants.BLOCK_BACKGROUND)));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						e.getComponent().setBackground(
								Color.decode(BahaStater.properties.getProperty(Constants.MAIN_BACKGROUND)));
					}
				});
				panelMru.add(jb);
			}
			JToggleButton jb1 = new JToggleButton("Clear All");
			jb1.setBackground(Color.PINK);
			jb1.setForeground(Color.RED);
			jb1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(displayPanel, "You want to clear history?",
							"Confirm Clear", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						mruCache.clear();
						panelMru.removeAll();
						panelMru.add(jb1);
						panelMru.revalidate();
						panelMru.repaint();
					}
				}
			});
			hindiFontColor = BahaStater.properties.getProperty(Constants.HINDI_FONT_COLOR);
			englishFontColor = BahaStater.properties.getProperty(Constants.ENGLISH_FONT_COLOR);
			if (null == hindiFontColor)
				hindiFontColor = "#FFFFFF";
			if (null == englishFontColor)
				englishFontColor = "#FFFFFF";
			panelMru.add(jb1);
			panelMru.revalidate();
			panelMru.repaint();
			String contentHtm = "";
			if("3".equals(BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE))) {
				 contentHtm = "<span style='color:" + hindiFontColor + ";font-size:" + (fontSizeBoth*.80) + ";'>"
//				 		+ "<br>"
						+ bookMapHindi.get(books.getSelectedValue()) + " " + chapters.getSelectedValue() + ":" + verses.getSelectedValue()
						+ "</span>"
						+ "<br>"
						+ "<span style='color:" + hindiFontColor + ";font-size:" + (fontSizeBoth+5) + ";'>"
						+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
								+ verses.getSelectedValue()).split("\\#")[1]
						+ "<br>"
						+ "<span style='color:" + englishFontColor + ";font-size:" + (fontSizeBoth*.70) + ";'>"
//								+ "<br>"
								+ books.getSelectedValue()+ " " + chapters.getSelectedValue() + ":" + verses.getSelectedValue()
						+ "</span><span style='color:" + englishFontColor + ";font-size:" + (fontSizeBoth*.40) + ";'>&nbsp;&nbsp;&nbsp;&nbsp;  "+version+"</span>"
								+ "<br>"
								+ "<span style='color:" + englishFontColor + ";font-size:" + (fontSizeBoth-5)
						+ ";'>" + secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
								+ verses.getSelectedValue()).split("\\#")[0]
						+ "</span>";
			
			} else {
				if ("1".equals(BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE))
						|| "3".equals(BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE))
						|| null == BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE)
						|| "" == BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE)) {
					contentHtm += "<span style='color:" + hindiFontColor + ";font-size:" + (fontSizeHindi*.80) + ";'>"
//							+ "<br>"
							+ bookMapHindi.get(books.getSelectedValue()) + " " + chapters.getSelectedValue() + ":"
							+ verses.getSelectedValue() + "</span>" + "<br>" + "<span style='color:" + hindiFontColor
							+ ";font-size:" + (fontSizeHindi) + ";'>" + secondaryCache.get(books.getSelectedValue()
									+ "-" + chapters.getSelectedValue() + "-" + verses.getSelectedValue()).split("\\#")[1]
							+ "</span>";
				}
				if ("2".equals(BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE))
						|| "3".equals(BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE))
						|| null == BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE)
						|| "" == BahaStater.properties.getProperty(Constants.VERSE_DISPLAY_LANGUAGE)) {
					contentHtm += "<span style='color:" + englishFontColor + ";font-size:" + (fontSizeEng*.70)
							+ ";'>"
	//							+ "<br>"
							+ books.getSelectedValue() + " " + chapters.getSelectedValue() + ":" + verses.getSelectedValue()
							+ "</span>" + "<span style='color:" + englishFontColor + ";font-size:" + (fontSizeEng*.40)
							+ ";'>&nbsp;&nbsp;&nbsp;&nbsp;  " + version + "</span>" + "<br>" + "<span style='color:"
							+ englishFontColor + ";font-size:" + (fontSizeEng) + ";'>"
							+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
									+ verses.getSelectedValue()).split("\\#")[0]
							+ "</span>";
				}
			}
			BahaStater.copyToClipboard(bookMapHindi.get(books.getSelectedValue()) + " " + chapters.getSelectedValue()
					+ ":" + verses.getSelectedValue() + "\n"
					+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
							+ verses.getSelectedValue()).split("\\#")[1]
					+ "\n" + books.getSelectedValue() + " " + chapters.getSelectedValue() + ":"
					+ verses.getSelectedValue() + version + "\n" + secondaryCache.get(books.getSelectedValue() + "-"
							+ chapters.getSelectedValue() + "-" + verses.getSelectedValue()).split("\\#")[0]);
			if (BahaStater.videoFlag) {
				JTextPane label1 = (JTextPane) BahaStater.bglabel;
				fontSizeHindi = BahaStater.calculateFontSize(
						bookMapHindi.get(books.getSelectedValue()) + " " + chapters.getSelectedValue() + ":"
								+ verses.getSelectedValue()
								+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
										+ verses.getSelectedValue()).split("\\#")[1]
								+ books.getSelectedValue() + " " + chapters.getSelectedValue() + ":"
								+ verses.getSelectedValue() + version
								+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
										+ verses.getSelectedValue()).split("\\#")[0],
						Double.valueOf(BahaStater.displayFrameX * .80).intValue(),
						Double.valueOf(BahaStater.displayFrameY * .20).intValue(), 0, 10);
				contentHtm = "<span style='color:" + hindiFontColor + ";font-size:" + (fontSizeHindi - 5) + ";'>"
						+ bookMapHindi.get(books
								.getSelectedValue())
						+ " " + chapters.getSelectedValue() + ":" + verses.getSelectedValue()
						+ "</span><span style='color:" + englishFontColor + ";font-size:" + (fontSizeHindi / 2)
						+ ";'>  " + version + "</span>" + "<br><span style='color:" + hindiFontColor + ";font-size:"
						+ (fontSizeHindi - 5) + ";'>" + secondaryCache.get(books.getSelectedValue() + "-"
								+ chapters.getSelectedValue() + "-" + verses.getSelectedValue()).split("\\#")[1]
						+ "</span>"
//								+ "<br>"
						+ "<span style='color:" + englishFontColor + ";font-size:" + (fontSizeHindi - 8) + ";'>"
						+ books.getSelectedValue() + " " + chapters
								.getSelectedValue()
						+ ":" + verses.getSelectedValue() + "</span><span style='color:" + englishFontColor
						+ ";font-size:" + (fontSizeHindi / 2) + ";'>&nbsp;&nbsp;&nbsp;&nbsp; " + version + "</span>"
						+ "<br><span style='color:" + englishFontColor + ";font-size:" + (fontSizeHindi - 5) + ";'>"
						+ secondaryCache.get(books.getSelectedValue() + "-" + chapters.getSelectedValue() + "-"
								+ verses.getSelectedValue()).split("\\#")[0]
						+ "</span>";
				final String lblContent = String.format("<html><body align='center' style='font-family:"
						+ new Font("Arial Unicode MS", Font.PLAIN, fontSizeHindi)
						+ ";word-wrap:break-word;'><p style=\"background-image: url('"
						+ BahaStater.properties.getProperty(Constants.LOWER_THIRD_IMAGE) + "');\">%s</p></body></html>", // <div
						contentHtm.replaceAll("\r\n", "<br>"));
				label1.setPreferredSize(new Dimension(BahaStater.displayFrameX.intValue(),
						Double.valueOf(BahaStater.displayFrameY * .15).intValue()));
//				label1.setMargin(new Insets(Double.valueOf(BahaStater.displayFrameY*.70).intValue(), 
//						Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue(), 
//						Double.valueOf(BahaStater.displayFrameY*.05).intValue(), 
//						Double.valueOf(BahaStater.displayFrameX-(BahaStater.displayFrameX*.95)).intValue()));
				label1.setText(lblContent);
				label1.revalidate();
				label1.repaint();
				label1.setVisible(true);
				// label.setIgnoreRepaint(true);
				BahaStater.webFrame.add(label1, BorderLayout.PAGE_END);
				BahaStater.webFrame.setAlwaysOnTop(true);
			} else {
				final String lblContent = String.format(
						"<html><body align='center' style='font-family:"
								+ BahaStater.properties.getProperty(Constants.FONT) + ";'><p>%s</p></body></html>", // <div
						contentHtm.replaceAll("\r\n", "<br>"));
				// final String imagePath = new
				// File(Constants.ANNOUNCEMENTS_IMG_PATH)+File.separator+(new
				// File(Constants.ANNOUNCEMENTS_IMG_PATH).list()[2]);
				String imagePath = BahaStater.properties.getProperty(Constants.VERSE_BACKGROUND_IMAGE_PATH);
				if (imagePath != null && imagePath != "" && new File(imagePath).exists()) {
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
					scrollPane.setViewport(new JViewport() {
						@Override
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);
							Image image;
							try {
								image = ImageIO.read(new File(imagePath));
								g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// add custom painting here.
							// For a scaled image you can use:
						}
					});
					// final String imagePath = new
					// File(Constants.ANNOUNCEMENTS_IMG_PATH)+file.separator+(new
					// File(Constants.ANNOUNCEMENTS_IMG_PATH).list()[0]);
					label.setPreferredSize(new Dimension(BahaStater.lwidth, BahaStater.lheight));
					label.setText(lblContent);
					label.setMargin(
							new Insets(0, Double.valueOf(BahaStater.lwidth - (BahaStater.lwidth * .95)).intValue(),
									Double.valueOf(BahaStater.lheight - (BahaStater.lheight * .98)).intValue(),
									Double.valueOf(BahaStater.lwidth - (BahaStater.lwidth * .95)).intValue()));
					label.revalidate();
					label.repaint();
					label.setVisible(true);
					scrollPane.setViewportView(label);
					scrollPane.setBorder(null);
					scrollPane.setVisible(true);
					scrollPane.revalidate();
					scrollPane.repaint();
					BahaStater.displayFrame.add(scrollPane, BorderLayout.CENTER);
					BahaStater.displayFrame.revalidate();
					BahaStater.displayFrame.repaint();
				} else {
					label.setPreferredSize(new Dimension(BahaStater.lwidth, BahaStater.lheight));
					label.setText(lblContent);
					label.setMargin(
							new Insets(0, Double.valueOf(BahaStater.lwidth - (BahaStater.lwidth * .95)).intValue(),
									Double.valueOf(BahaStater.lheight - (BahaStater.lheight * .98)).intValue(),
									Double.valueOf(BahaStater.lwidth - (BahaStater.lwidth * .95)).intValue()));
					label.revalidate();
					label.repaint();
					label.setVisible(true);
					BahaStater.displayFrame.add(label, BorderLayout.CENTER);
					BahaStater.displayFrame.revalidate();
					BahaStater.displayFrame.repaint();
				}

				// label.setIgnoreRepaint(true);

				BahaStater.webFrame.setVisible(false);
				BahaStater.webFrame.setAlwaysOnTop(false);
			}

		}
	}

}
