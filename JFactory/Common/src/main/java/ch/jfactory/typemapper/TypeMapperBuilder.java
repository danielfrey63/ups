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
package ch.jfactory.typemapper;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.application.view.builder.DockingWindowsUtils;
import ch.jfactory.binding.ResourcePathToIconConverter;
import ch.jfactory.command.CommitPresentationModel;
import ch.jfactory.command.CommonCommands;
import ch.jfactory.command.ResetPresentationModel;
import ch.jfactory.image.BlankIcon;
import ch.jfactory.component.list.AlternateListCellRenderer;
import ch.jfactory.resource.Strings;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ListResourceBundle;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.infonode.docking.View;
import net.infonode.docking.util.ViewMap;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/06 10:16:23 $
 */
public class TypeMapperBuilder extends ActionCommandPanelBuilder {

    private TypeModel model;
    private final JList list = new JList();

    public static void main(final String[] args) {
        Strings.setResourceBundle(new ListResourceBundle() {
            protected Object[][] getContents() {
                return new String[][]{
                        {"BUTTON.OK.TEXT", "OK"},
                        {"BUTTON.CANCEL.TEXT", "Abbrechen"},
                        {"type.icons.SYMBOL", ""},
                        {"type.icons.TITLE", "Icons"},
                        {"type.icons.TEXT1", "Wählen Sie ein Icon aus"},
                        {"type.icons.TEXT2", "Hier sehen Sie eine Darstellung aller Icons aus dem Klassenpfad. Wählen Sie eines aus:"},
                };
            }
        });
        final JFrame frame = new JFrame();
        final JComponent panel = new TypeMapperBuilder(new TypeModel()).getPanel();
        panel.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), new EtchedBorder()), new EmptyBorder(8, 8, 8, 8)));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public TypeMapperBuilder(final TypeModel model) {
        this.model = model;
    }

    protected void initCommands() {
        // LIST
        initCommand(new AddType(getCommandManager(), model), true);
        initCommand(new DeleteType(getCommandManager(), model));
        // EDITOR
        initCommand(new CommitPresentationModel(getCommandManager(), model.getEditorModel()));
        initCommand(new ResetPresentationModel(getCommandManager(), model.getEditorModel()));
        initCommand(new IconsAction(getCommandManager(), model.getEditorModel()));
    }

    protected JComponent createMainPanel() {
        final ViewMap views = new ViewMap();
        views.addView(1, new View("Editor", null, createEditor()));
        return DockingWindowsUtils.createParentChildDisplay(createList(), views);
    }

    protected void initModelListeners() {
        // LIST
        model.getSelectionInList().addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION, new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                final Object selectedItem = evt.getNewValue();
                model.getEditorModel().setBean(selectedItem);
            }
        });
        model.getSelectionInList().addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION_EMPTY, new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                final boolean isEmpty = ((Boolean) evt.getNewValue()).booleanValue();
                getCommandManager().getCommand(Commands.COMMANDID_DELETE).setEnabled(!isEmpty);
                getCommandManager().getCommand(Commands.COMMANDID_EDITICONS).setEnabled(!isEmpty);
            }
        });
        model.getEditorModel().addPropertyChangeListener(PresentationModel.PROPERTYNAME_BUFFERING, new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                list.repaint();
            }
        });

        // EDITOR
        model.getEditorModel().addPropertyChangeListener(PresentationModel.PROPERTYNAME_BUFFERING, new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                final boolean isBuffering = ((Boolean) evt.getNewValue()).booleanValue();
                getCommandManager().getCommand(CommonCommands.COMMANDID_TRIGGERAPPLY).setEnabled(isBuffering);
                getCommandManager().getCommand(CommonCommands.COMMANDID_TRIGGERRESET).setEnabled(isBuffering);
            }
        });
    }

    // LIST

    private JComponent createList() {
        list.setModel(model.getSelectionInList());
        Bindings.bind(list, model.getSelectionInList());
        list.setCellRenderer(new AlternateListCellRenderer() {
            private final Icon BLANK_ICON = new BlankIcon(new Color(0, 0, 0, 0), 16);

            public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                final TypeMapping mapping = (TypeMapping) value;
                setIcon(getIcon(mapping));
                setText(mapping.getText());
                return this;
            }

            private Icon getIcon(final TypeMapping mapping) {
                Icon icon = null;
                try {
                    icon = new ImageIcon(TypeMapperBuilder.class.getResource(mapping.getIcon()));
                }
                catch (Exception e) {
                    icon = BLANK_ICON;
                }
                return icon;
            }
        });
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(getCommandManager().getGroup(Commands.GROUPID_LISTTOOLBAR).createToolBar(), BorderLayout.NORTH);
        return panel;
    }

    // EDITOR

    private JComponent createEditor() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(createEditorPanel(), BorderLayout.CENTER);
        panel.add(getCommandManager().getGroup(Commands.GROUPID_EDITTOOLBAR).createToolBar("toolbar"), BorderLayout.NORTH);
        return panel;
    }

    private JComponent createEditorPanel() {
        final FormLayout layout = new FormLayout("8dlu, r:p, 4dlu, p:g(1.0), 4dlu, p, 8dlu", "8dlu, p, 8dlu, p, 8dlu");
        final JPanel panel = new JPanel(layout);
        final CellConstraints cc = new CellConstraints();
        panel.add(new JLabel("Kategorie:"), cc.xy(2, 2));
        panel.add(createAndBindTextField(model.getEditorModel(), "text"), cc.xyw(4, 2, 3));
        panel.add(new JLabel("Icon:"), cc.xy(2, 4));
        panel.add(createAndBindLabel(model.getEditorModel(), "icon"), cc.xy(4, 4));
        panel.add(getCommandManager().getCommand("typeedit.icons").createButton(), cc.xy(6, 4));
        return panel;
    }

    private static JTextField createAndBindTextField(final PresentationModel model, final String property) {
        final JTextField field = new JTextField();
        Bindings.bind(field, model.getBufferedModel(property));
        return field;
    }

    private static JLabel createAndBindLabel(final PresentationModel model, final String property) {
        final JLabel label = new JLabel();
        final BufferedValueModel bufferedModel = model.getBufferedModel(property);
        final ResourcePathToIconConverter converter = new ResourcePathToIconConverter(bufferedModel);
        new PropertyConnector(converter, "value", label, "icon").updateProperty2();
        return label;
    }

}
