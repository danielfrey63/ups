package ch.jfactory.application.view.dialog;

import ch.jfactory.component.AbstractPlainDocument;
import ch.jfactory.component.list.DefaultJList;
import ch.jfactory.application.view.search.SearchableUtils;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import org.apache.log4j.Category;

/**
 * Simple dialog that displays a text and a list.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.11 $ $Date: 2007/09/27 10:41:35 $
 */
public class ListDialog extends I15nComponentDialog implements ListSelectionListener, DocumentListener {

    private static final Category cat = Category.getInstance(ListDialog.class);

    private Object[] selectedData;
    private Object[] allData;
    private DefaultJList list;
    private JTextField search;
    private JPanel listPanel;

    /**
     * Constructs a new list dialog based as a child of the dialog given. A title is displayed, for which the string is
     * taken from a string resource {@link ch.jfactory.resource.Strings} with a key like <code>PREFIX.TITLE</code>,
     * where <code>PREFIX</code> is the prefix argument given. A text multiline label is displayed above the list where
     * the text is retrieved by the key <code>PREFIX.TEXT</code>.
     *
     * @param parent   the parent dialog to center this dialog on
     * @param prefix   the key prefix to use
     * @param listData the data to put into the list
     */
    public ListDialog(final Dialog parent, final String prefix, final Object[] listData) {
        super(parent, prefix);
        init(listData);
    }

    /**
     * Constructs a new list dialog based as a child of the frame given. A title is displayed, for which the string is
     * taken from a string resource {@link ch.jfactory.resource.Strings} with a key like <code>PREFIX.TITLE</code>,
     * where <code>PREFIX</code> is the prefix argument given. A text multiline label is displayed above the list where
     * the text is retrieved by the key <code>PREFIX.TEXT</code>.
     *
     * @param parent   the parent dialog to center this dialog on
     * @param prefix   the key prefix to use
     * @param listData the data to put into the list
     */
    public ListDialog(final Frame parent, final String prefix, final Object[] listData) {
        super(parent, prefix);
        init(listData);
    }

    private void init(final Object[] listData) {
        allData = new ArrayList(Arrays.asList(listData)).toArray();
        list.setListData(allData);
        list.requestFocus();
    }

    /**
     * Returns the data selected. The type of the object array is <code>Object[]</code>.
     *
     * @return the data selected uppon close of the dialog, if the apply butten was pressed, otherwise an empty array.
     */
    public Object[] getSelectedData() {
        return selectedData;
    }

    /**
     * Returns the data selected. The type of the object array is the type given.
     *
     * @param type the type of the array to which the data should be copied
     * @return the data selected uppon close of the dialog, if the apply butten was pressed, otherwise an empty array.
     */
    public Object[] getSelectedData(final Object[] type) {
        return new ArrayList(Arrays.asList(selectedData)).toArray(type);
    }

    /**
     * Sets the renderer for the list displayed.
     *
     * @param renderer the ListCellRenderer
     */
    public void setListCellRenderer(final ListCellRenderer renderer) {
        list.setCellRenderer(renderer);
    }

    /**
     * Set which selection mode you want.
     *
     * @param selectionMode selection mode from {@link javax.swing.ListSelectionModel} might be {@link
     *                      javax.swing.ListSelectionModel#SINGLE_SELECTION} or {@link javax.swing.ListSelectionModel#SINGLE_INTERVAL_SELECTION}
     *                      or {@link javax.swing.ListSelectionModel#MULTIPLE_INTERVAL_SELECTION}
     */
    public void setSelectionMode(final int selectionMode) {
        list.setSelectionMode(selectionMode);
    }

    /**
     * Sets the preselected data.
     *
     * @param selected the objects to select in the list
     */
    public void setSelectedData(final Object[] selected) {
        list.setSelectedValues(selected);
    }

    // I15nComponentDialog
    protected JComponent createComponentPanel() {

        list = new DefaultJList();
        list.addListSelectionListener(this);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    apply();
                }
            }
        });
        list.setVisibleRowCount(3);

        SearchableUtils.installSearchable(list);

        return new JScrollPane(list);
    }

    protected void onApply() throws ComponentDialogException {
        selectedData = list.getSelectedValues();
    }

    protected void onCancel() {
        selectedData = new Object[0];
    }

    // ListSelectionListener
    public void valueChanged(final ListSelectionEvent e) {
        final JList list = (JList) e.getSource();
        final Object[] selection = list.getSelectedValues();
        enableApply(selection != null && selection.length > 0);
    }

    // DocumentListener
    public void insertUpdate(final DocumentEvent e) {
        changedUpdate(e);
    }

    public void removeUpdate(final DocumentEvent e) {
        changedUpdate(e);
    }

    public void changedUpdate(final DocumentEvent e) {
        final Document d = e.getDocument();
        String str = null;
        try {
            str = d.getText(0, d.getLength()).toUpperCase();
        }
        catch (Exception x) {
            cat.error("Error getting text", x);
        }
        final List<Object> currentObjects = new ArrayList<Object>();
        for (int i = 0; i < allData.length; i++) {
            final Object o = allData[i];
            if (o.toString().toUpperCase().indexOf(str) > -1) {
                currentObjects.add(o);
            }
        }
        list.setListData(currentObjects.toArray());
    }

    public static class InListDocument extends AbstractPlainDocument {

        private Object[] listData;

        public InListDocument(final Object[] listData) {
            this.listData = listData;
        }

        protected boolean validate(String newValue) {
            newValue = newValue.toUpperCase();
            int index = 0;
            boolean found = false;
            while (index < listData.length && !found) {
                final String listEntry = listData[index++].toString().toUpperCase();
                found = listEntry.indexOf(newValue) > -1;
            }
            return found;
        }
    }

    public static void main(final String[] args) {
        final java.util.ResourceBundle bundle = new java.util.ListResourceBundle() {
            protected Object[][] getContents() {
                return new String[][]{
                        {"test.TITLE", "Öffne UST Datei"},
                        {"test.TEXT1", "Öffne UST Datei"},
                        {"test.TEXT2", "Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt. Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt. Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt."},
                        {"test.SYMBOL", "file.png"},
                        {"BUTTON.OK.TEXT", "OK"},
                        {"BUTTON.CANCEL.TEXT", "Abbrechen"}
                };
            }
        };
        ch.jfactory.resource.Strings.setResourceBundle(bundle);
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add("Eintrag " + i);
        }
        final Object[] objects = list.toArray();
        final ListDialog dialog = new ListDialog((javax.swing.JFrame) null, "test", objects);
        dialog.setSize(400, 700);
        ch.jfactory.application.presentation.WindowUtils.centerOnScreen(dialog);
        dialog.setVisible(true);
    }
}
