/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.uec.level;

import ch.jfactory.color.ColorUtils;
import ch.jfactory.component.Dialogs;
import ch.jfactory.model.SimpleModelList;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.thoughtworks.xstream.XStream;
import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.Logger;

/**
 * Builds a panel with a name field and a table.
 *
 * @author Daniel Frey
 * @version $Revision: 1.6 $ $Date: 2008/01/06 10:16:20 $
 */
public class LevelsBuilder extends AbstractDetailsBuilder {

    private static final Logger LOG = Logger.getLogger(LevelsBuilder.class);
    private static final boolean INFO = LOG.isInfoEnabled();
    private static final String RESOURCE_FORM = "/ch/xmatrix/ups/uec/level/LevelsPanel.jfd";
    private static final String RESOURCE_MODEL = "/data/levels.xml";

    private LevelTableModel tableModel;
    private JTable table;
    private XStream converter;
    private LevelNameTableCellRenderer levelRenderer;
    private LevelValueCellRenderer valueRenderer;

    public LevelsBuilder() {
        super(new LevelsFactory(), RESOURCE_MODEL, RESOURCE_FORM, 30);
    }

    //--- DetailsBuilder implementations

    public void setModel(final TaxonBased taxonBased) {
        super.setModel(taxonBased);
        if (taxonBased != null) {
            final TaxonTree tree = TaxonModels.find(taxonBased.getTaxaUid());
            tableModel.setModel(tree == null ? null : tree.getRootLevel());
        }
        else {
            tableModel.setModel(null);
        }
    }

    public void setEnabled(final boolean enabled) {
        table.setEnabled(enabled);
        table.getSelectionModel().clearSelection();
        levelRenderer.setEnabled(enabled);
        valueRenderer.setEnabled(enabled);
    }

    protected void initComponents() {
        try {
            table = getCreator().getTable("tableMaxima");
            tableModel = new LevelTableModel(getModels());
            table.setModel(tableModel);
            final Color color = UIManager.getColor("Table.selectionBackground");
            levelRenderer = new LevelNameTableCellRenderer(color);
            valueRenderer = new LevelValueCellRenderer(color);
            table.setDefaultRenderer(String.class, levelRenderer);
            table.setDefaultRenderer(Integer.class, valueRenderer);
        }
        catch (Exception e) {
            final String message = "failed to init components";
            LOG.error(message, e);
            throw new IllegalStateException(message);
        }
    }

    //--- ActionCommandPanelBuilder implementation

    protected void initComponentListeners() {
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(final TableModelEvent e) {
                setDirty();
            }
        });
    }

    //---- AbstractDetailsBuilder implementations

    public ArrayList findMigrationErrors(final String uid) {
        final LevelsModel model = (LevelsModel) getModels().getSelection();
        final ArrayList<LevelModel> levels = model.getLevelModels();
        final TaxonTree tree = TaxonModels.find(uid);
        final ArrayList errors = (ArrayList) levels.clone();
        for (int i = 0; i < levels.size(); i++) {
            final LevelModel m = levels.get(i);
            SimpleLevel level = tree.getRootLevel();
            while (level != null) {
                if (m.getLevel().equals(level.getName())) {
                    errors.remove(m);
                }
                level = level.getChildLevel();
            }
        }
        return errors;
    }

    public void removeMigrationErrors(final ArrayList errors) {
        final LevelsModel model = (LevelsModel) getModels().getSelection();
        final ArrayList<LevelModel> levels = model.getLevelModels();
        for (int i = 0; i < errors.size(); i++) {
            final LevelModel levelModel = (LevelModel) errors.get(i);
            levels.remove(levelModel);
            if (INFO) LOG.info("");
        }
    }

    protected void commitSuccessful() {
        Dialogs.showInfoMessage(table, "Migration", "Die Migration war ohne problemantische Fälle erfolgreich.");
    }

    protected XStream getConverter() {
        if (converter == null) {
            converter = SimpleModelList.getConverter();
            converter.alias("levelsModels", SimpleModelList.class);
            converter.alias("levelsModel", LevelsModel.class);
            converter.alias("levelModel", LevelModel.class);
            converter.addImplicitCollection(LevelsModel.class, "models");
        }
        return converter;
    }

    protected String getInfoString() {
        return "Ebenen-Editor";
    }

    protected String getModelId() {
        return MainModel.MODELID_LEVELS;
    }

    //--- Utilities

    public static void main(final String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        UIManager.put("ToolBar.border", new EmptyBorder(0, 0, 0, 0));
        Strings.setResourceBundle(ResourceBundle.getBundle("ch.xmatrix.ups.uec.Strings"));
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JComponent panel = new LevelsBuilder().getPanel();
        panel.setBorder(Borders.createEmptyBorder(Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8));
        f.getContentPane().add(panel);
        f.setSize(450, 300);
        f.setVisible(true);
    }

    private static class LevelNameTableCellRenderer extends DefaultTableCellRenderer {

        private static final Color COLOR_FOREGROUND_DISABLED = UIManager.getColor("Label.disabledForeground");
        private static final Color COLOR_FOREGROUND_ENABLED = UIManager.getColor("Label.foreground");

        private final Color selected;
        private final Color even;
        private final Color odd;
        private final Color evenDisabled;
        private final Color oddDisabled;

        private boolean enabled;


        public LevelNameTableCellRenderer(final Color color) {
            selected = ColorUtils.darken(color, 0.5);
            even = ColorUtils.fade(color, 0.5);
            odd = ColorUtils.fade(color, 0.7);
            evenDisabled = ColorUtils.fade(color, 0.7);
            oddDisabled = ColorUtils.fade(color, 0.9);
        }

        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            final String text = value.toString();
            setText(text);
            final String iconName = "/icon/icon" + text + (isSelected ? "Selected" : "") + ".png";
            final URL iconUrl = LevelsBuilder.class.getResource(iconName);
            if (iconUrl != null) {
                final ImageIcon icon = new ImageIcon(iconUrl);
                setIcon(icon);
            }
            else {
                LOG.warn("icon for " + iconName + " not found");
                setIcon(null);
            }
            if (isSelected && enabled) {
                setBackground(selected);
                setForeground(Color.white);
            }
            else if (!enabled) {
                setBackground(row % 2 == 0 ? evenDisabled : oddDisabled);
                setForeground(COLOR_FOREGROUND_DISABLED);
            }
            else {
                setBackground(row % 2 == 0 ? even : odd);
                setForeground(COLOR_FOREGROUND_ENABLED);
            }
            return this;
        }

        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }
    }

    private static class LevelValueCellRenderer extends DefaultTableCellRenderer {

        private static final Color COLOR_FOREGROUND_DISABLED = UIManager.getColor("Label.disabledForeground");
        private static final Color COLOR_FOREGROUND_ENABLED = UIManager.getColor("Label.foreground");

        private final Color even;
        private final Color odd;
        private final Color evenDisabled;
        private final Color oddDisabled;

        private boolean enabled;

        public LevelValueCellRenderer(final Color color) {
            even = ColorUtils.fade(color, 0.7);
            odd = ColorUtils.fade(color, 0.9);
            evenDisabled = ColorUtils.fade(color, 0.9);
            oddDisabled = ColorUtils.fade(color, 1.0);
        }

        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            final String text = value.toString();
            setText(("0".equals(text) ? "" : text));
            setForeground(enabled ? COLOR_FOREGROUND_ENABLED : COLOR_FOREGROUND_DISABLED);
            setBackground(enabled ? (row % 2 == 0 ? even : odd) : (row % 2 == 0 ? evenDisabled : oddDisabled));
            return this;
        }

        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }
    }
}
