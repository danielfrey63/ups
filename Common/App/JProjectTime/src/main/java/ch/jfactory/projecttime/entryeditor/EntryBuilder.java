/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.entryeditor;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.command.CommitPresentationModel;
import ch.jfactory.command.CommonCommands;
import ch.jfactory.command.ResetPresentationModel;
import ch.jfactory.projecttime.domain.api.IFEntry;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.formatter.EmptyDateFormatter;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class EntryBuilder extends ActionCommandPanelBuilder {

    private EntryModel model;
    private JTextField startField;
    private JTextField endField;
    private JTextField categoryField;
    private JTextField nameField;

    public EntryBuilder(final EntryModel model) {
        this.model = model;
        // Make sure to initialize any buffered value models first, before any other listeners are registered.
        final PresentationModel presentationModel = model.getModel();
        startField = createAndBindGregorianCalendarField(presentationModel, IFEntry.PROPERTYNAME_START);
        endField = createAndBindGregorianCalendarField(presentationModel, IFEntry.PROPERTYNAME_END);
        categoryField = BasicComponentFactory.createTextField(presentationModel.getBufferedModel(IFEntry.PROPERTYNAME_TYPE), false);
        nameField = BasicComponentFactory.createTextField(presentationModel.getBufferedModel(IFEntry.PROPERTYNAME_NAME), false);
    }

    protected void initCommands() {
        final PresentationModel presentationModel = model.getModel();
        initCommand(new CommitPresentationModel(getCommandManager(), presentationModel));
        initCommand(new ResetPresentationModel(getCommandManager(), presentationModel));
    }

    protected void initModelListeners() {
        // Make sure the apply and reset buttons are enabled/disabled when a bean is being edited.
        model.getModel().addPropertyChangeListener(PresentationModel.PROPERTYNAME_BUFFERING, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final boolean isBuffering = ((Boolean) evt.getNewValue()).booleanValue();
                getCommand(CommonCommands.COMMANDID_TRIGGERAPPLY).setEnabled(isBuffering);
                getCommand(CommonCommands.COMMANDID_TRIGGERRESET).setEnabled(isBuffering);
            }
        });
        // Make sure that the bean is set
        model.getModel().addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final IFEntry entry = (IFEntry) evt.getNewValue();
                model.getModel().setBean(entry);
                final boolean hasChildren = (entry != null && entry.getChildren() != null && entry.getChildren().length > 0);
                startField.setEnabled(!hasChildren);
                endField.setEnabled(!hasChildren);
            }
        });
    }

    protected JComponent createMainPanel() {
        final JPanel main = new JPanel(new BorderLayout());
        main.add(createFieldsPanel(), BorderLayout.CENTER);
        main.add(createToolbar(), BorderLayout.NORTH);
        return main;
    }

    private JComponent createToolbar() {
        return getCommandManager().getGroup(Commands.GROUPID_TOOLBAR).createToolBar("toolbar");
    }

    private JComponent createFieldsPanel() {
        final FormLayout layout = new FormLayout(
                "12dlu, r:p, 4dlu, p:g(1.0), 12dlu", "12dlu, p, 8dlu, p, 8dlu, p, 8dlu, p, 12dlu:g(1.0)");
        layout.setRowGroups(new int[][]{{2, 4, 6, 8}});
        final CellConstraints cc = new CellConstraints();
        final JPanel fields = new JPanel(layout);
        fields.add(new JLabel("Name:"), cc.xy(2, 2));
        fields.add(nameField, cc.xy(4, 2));
        fields.add(new JLabel("Kategorie:"), cc.xy(2, 4));
        fields.add(categoryField, cc.xy(4, 4));
        fields.add(new JLabel("Start:"), cc.xy(2, 6));
        fields.add(startField, cc.xy(4, 6));
        fields.add(new JLabel("Ende:"), cc.xy(2, 8));
        fields.add(endField, cc.xy(4, 8));
        return fields;
    }

    // UTILS

    private static JTextField createAndBindGregorianCalendarField(final PresentationModel model, final String property) {
        final DateFormat editFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        editFormat.setLenient(false);
        final DateFormat displayFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        final JFormattedTextField.AbstractFormatter defaultFormatter = new EmptyDateFormatter(editFormat);
        final JFormattedTextField.AbstractFormatter displayFormatter = new EmptyDateFormatter(displayFormat);
        final DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(defaultFormatter, displayFormatter);
        return BasicComponentFactory.createFormattedTextField(model.getBufferedModel(property), formatterFactory);
    }
}
