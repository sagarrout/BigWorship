package main.java.baha;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.*;

public class InsertJTable{
JTable table;
JButton button;
public static void main(String[] args) {
new InsertJTable();
}
public InsertJTable(){
JFrame frame = new JFrame("Songs");
JPanel panel = new JPanel();
String data[][] = {{"Angelina","Mumbai"},{"Martina","Delhi"}};

String col[] = {"Title","Lyrics"};
DefaultTableModel model = new DefaultTableModel(data, col);
table = new JTable(model);
JScrollPane pane = new JScrollPane(table);
button=new JButton("Submit");
button.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent ae){

int count=table.getRowCount();

try{

Object obj1 = GetData(table, 0, 0);
Object obj2 = GetData(table, 0, 1);
Object obj3 = GetData(table, 1, 0);
Object obj4 = GetData(table, 1, 1);
String value1=obj1.toString();
String value2=obj2.toString();
String value3=obj3.toString();
String value4=obj4.toString();

String data =value1+" "+value2+"\n"+value3+" "+value4;
writeToFile(data, "data.doc");
}
catch(Exception e){}
}
});
panel.add(pane);
panel.add(button);
frame.add(panel);
frame.setSize(500,500);
frame.setUndecorated(true);
frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
frame.setVisible(true);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

public Object GetData(JTable table, int row_index, int col_index){
return table.getModel().getValueAt(row_index, col_index);
}
private static void writeToFile(String content, String path) {
try{
POIFSFileSystem fs = new POIFSFileSystem();
DirectoryEntry directory = fs.getRoot();
directory.createDocument("WordDocument", new ByteArrayInputStream(content.getBytes()));
FileOutputStream out = new FileOutputStream(path);

fs.writeFilesystem(out);
out.close();
}
catch(Exception ex){
System.out.println(ex.getMessage());
}
}
}