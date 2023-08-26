package main.java.baha;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FilterComboBox extends JComboBox {
	 /* Entries to the combobox list.
     */
    private List<String> entries;

    public List<String> getEntries()
    {
        return entries;
    }

    public FilterComboBox(List<String> entries)
    {
        super(entries.toArray());
        this.entries = entries  ;
        this.setEditable(true);
        setSize(new Dimension(350, 25));
        final JTextField textfield =
            (JTextField) this.getEditor().getEditorComponent();

        /**
         * Listen for key presses.
         */
        textfield.addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent ke)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        /**
                         * On key press filter the list.
                         * If there is "text" entered in text field of this combo use that "text" for filtering.
                         */
                        comboFilter(textfield.getText());
                    }
                });
            }
        });

    }

    /**
     * Build a list of entries that match the given filter.
     */
    public void comboFilter(String enteredText)
    {
        List<String> entriesFiltered = new ArrayList<String>();
        Set<String> entriesFilteredSet = new HashSet<String>();
        if(!getEntries().isEmpty()) {
        	entriesFilteredSet.add("");
        	entriesFilteredSet = getEntries().stream().filter(e -> e.toLowerCase().contains(enteredText.toLowerCase())).collect(Collectors.toSet());
        	if(entriesFilteredSet.isEmpty()) {
        		entriesFilteredSet.addAll(BAHAMenu.songsMap.entrySet().stream().filter(e -> e.getValue().toLowerCase().contains(enteredText.toLowerCase())).map(e->e.getKey()).collect(Collectors.toSet()));
        	}
        }
        if (entriesFilteredSet.size() > 0)
        {
        	entriesFiltered = entriesFilteredSet.stream().sorted((o1,o2) -> o1.compareToIgnoreCase(o2)).collect(Collectors.toList());
            this.setModel(new DefaultComboBoxModel(entriesFiltered.toArray()));
            this.setSelectedItem(enteredText);
            this.showPopup();
        }
        else
        {
            this.hidePopup();
        }
    }
    
    public void refresh(List<String> entries)
    {
    	List<String> strList = new ArrayList<String>();
    	strList.add("");
    	strList.addAll(entries);
    	strList = strList.stream().sorted((o1,o2) -> o1.compareToIgnoreCase(o2)).collect(Collectors.toList());
    	this.setModel(new DefaultComboBoxModel(strList.toArray()));
        this.entries = entries ;
        this.setSize(new Dimension(350, 25));

    }
}
