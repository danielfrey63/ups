package com.ethz.geobot.herbar.gui.mode.wizard;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.Dialogs;
import ch.jfactory.component.tab.NiceTabbedPane;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.util.HerbarTheme;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Category;

/**
 * wizard pane for selecting the mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class WizardModePane extends WizardPane {

    private static final Category cat = Category.getInstance(WizardModePane.class);

    /**
     * Name of the pane.
     */
    public final static String NAME = "mode.select";

    private NiceTabbedPane modeGroupTab;
    private Map<String, JList> groupLists = new HashMap<String, JList>();
    private String modePropertyName;
    private String modeListPropertyName;
    private JLabel note;

    public WizardModePane(String modePropertyName, String modeListPropertyName) {
        super(NAME, new String[]{modePropertyName, modeListPropertyName});
        this.modePropertyName = modePropertyName;
        this.modeListPropertyName = modeListPropertyName;
    }

    public void activate() {
        ModeWizardModel modeWizardModel = (ModeWizardModel) getWizardModel();
        // when starting the very first time, the mode is null, and the wizard is called with no current mode.
        if (modeWizardModel.getCurrentMode() == null) {
            modeWizardModel.setNextEnabled(false);
            JDialog dialog = (JDialog) getTopLevelAncestor();
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            WindowListener[] listeners = dialog.getWindowListeners();
            for (WindowListener listener : listeners) {
                dialog.removeWindowListener(listener);
            }
            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    askForQuit();
                }
            });
        }
    }

    public boolean isCancelOk() {
        ModeWizardModel modeWizardModel = (ModeWizardModel) getWizardModel();
        boolean ret = true;
        if (modeWizardModel.getCurrentMode() == null) {
            ret = askForQuit();
        }
        return ret;
    }

    private boolean askForQuit() {
        boolean ret;
        String title = Strings.getString("WIZARD.MODE.CANCEL.TITLE");
        String text = Strings.getString("WIZARD.MODE.CANCEL.TEXT");
        ret = Dialogs.showQuestionMessageCancel(WizardModePane.this, title, text) != Dialogs.CANCEL;
        if (ret) {
            System.exit(1);
        }
        return ret;
    }

    protected JPanel createDisplayPanel(String prefix) {

        JPanel textPanel = createTextPanel(prefix);
        JPanel titlePanel = createDefaultTitlePanel(prefix);

        modeGroupTab = new NiceTabbedPane();
        modeGroupTab.setPreferredSize(new Dimension(350, 150));

        note = new JLabel(Strings.getString(prefix + ".NOTE.TEXT"));
        note.setForeground(Color.orange);
        note.setFont(note.getFont().deriveFont(Font.BOLD));
        JPanel notePanel = new JPanel(new BorderLayout());
        int gap = Constants.GAP_BETWEEN_GROUP;
        notePanel.setBorder(BorderFactory.createEmptyBorder(gap, 0, 0, 0));
        notePanel.add(note, BorderLayout.CENTER);
        Dimension noteDim = note.getPreferredSize();
        notePanel.setPreferredSize(new Dimension(noteDim.width, noteDim.height + gap));

        int y = 0;
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(textPanel, new GridBagConstraints(0, y++, 1, 1, 1, 0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(titlePanel, new GridBagConstraints(0, y++, 1, 1, 1, 0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(modeGroupTab, new GridBagConstraints(0, y++, 1, 1, 1, 1, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(notePanel, new GridBagConstraints(0, y++, 1, 1, 1, 0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        return panel;
    }

    private void createTabs(Mode[] modes) {
        modeGroupTab.removeAll();
        Arrays.sort(modes, new Comparator<Mode>() {
            public int compare(Mode m1, Mode m2) {
                final String g1 = (String) m1.getProperty(Mode.MODE_GROUP);
                final String g2 = (String) m2.getProperty(Mode.MODE_GROUP);
                final String prefix1 = prefix + ".GROUP." + g1.toUpperCase();
                final String prefix2 = prefix + ".GROUP." + g2.toUpperCase();
                if (g1.equals(g2)) {
                    final String n1 = (String) m1.getProperty(Mode.DESCRIPTION);
                    final String n2 = (String) m2.getProperty(Mode.DESCRIPTION);
                    return n1.compareTo(n2);
                }
                else {
                    final Integer i1 = new Integer(Strings.getString(prefix1 + ".INDEX"));
                    final Integer i2 = new Integer(Strings.getString(prefix2 + ".INDEX"));
                    return i1.compareTo(i2);
                }
            }
        });
        for (Mode mode : modes) {
            String group = (String) mode.getProperty(Mode.MODE_GROUP);
            JList list = groupLists.get(group);
            if (list == null) {
                list = createList(group);
            }
            DefaultListModel listModel = (DefaultListModel) list.getModel();
            listModel.addElement(mode);
        }
    }

    private JList createList(String group) {
        JList list = new JList(new DefaultListModel());
        list.setBackground(HerbarTheme.getBackground2());
        list.setCellRenderer(new ModeListCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                JList list = (JList) event.getSource();
                Mode mode = (Mode) list.getSelectedValue();
                setProperty(modePropertyName, mode);
                ModeWizardModel model = (ModeWizardModel) getWizardModel();
                Mode current = model.getCurrentMode();
                if (current == mode) {
                    list.getSelectionModel().clearSelection();
                }
                model.setFinishEnabled(current != mode && mode != null);
                // Todo: little bit of an antipattern. did not look for who is enabling it. should be disabled as
                // model is not yet consistent.
                model.setNextEnabled(false);
                note.setVisible((current == mode || mode == null));
            }
        });
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    if (getProperty(modePropertyName) != null) {
                        getWizardModel().finishWizard();
                    }
                }
            }
        });

        groupLists.put(group, list);

        JScrollPane scroll = new JScrollPane(list);
        scroll.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                JScrollPane scroll = (JScrollPane) e.getSource();
                JList list = (JList) scroll.getViewport().getView();
                list.getSelectionModel().clearSelection();
                getWizardModel().setFinishEnabled(false);
            }
        });
        String key = prefix + ".GROUP." + group.toUpperCase();
        String title = Strings.getString(key + ".TEXT");
        int index = Integer.parseInt(Strings.getString(key + ".INDEX"));
        modeGroupTab.add(scroll, title, index);
        return list;
    }

    /**
     * is invoked by the wizard to register change listener. This method is invoked by the init method.
     *
     * @param model refernce to the model
     */
    public void registerPropertyChangeListener(WizardModel model) {
        model.addPropertyChangeListener(modePropertyName, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                Object mode = event.getNewValue();
                if (mode != null) {
                    getWizardModel().setFinishEnabled(true);
                }
            }
        });
        model.addPropertyChangeListener(modeListPropertyName, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                Collection<Mode> modes = (Collection<Mode>) event.getNewValue();
                createTabs(modes.toArray(new Mode[0]));
            }
        });
    }

    class ModeListCellRenderer extends JLabel implements ListCellRenderer {

        public ModeListCellRenderer() {
            setOpaque(true);
        }

        public java.awt.Component getListCellRendererComponent(JList jList, Object value, int index,
                                                               boolean isSelected, boolean cellHasFocus) {
            try {
                Mode mode = (Mode) value;
                ModeWizardModel wizardModel = (ModeWizardModel) WizardModePane.this.getWizardModel();
                Mode current = wizardModel.getCurrentMode();
                setText((String) mode.getProperty(Mode.DESCRIPTION));
                setIcon((Icon) mode.getProperty(Mode.ICON));
                setDisabledIcon((Icon) mode.getProperty(Mode.DISABLED_ICON));
                setEnabled(current != mode);
            }
            catch (ClassCastException ex) {
                cat.error("illegal object in list:" + value);
            }
            if (isSelected) {
                setBackground(UIManager.getColor("List.selectionBackground"));
            }
            else {
                setBackground(jList.getBackground());
            }
            return this;
        }
    }
}
