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
package ch.jfactory.projecttime.stats;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.binding.PresentationModel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class StatsBuilder extends ActionCommandPanelBuilder {

    private StatsModel model;
    private JTextField durationField;

    public StatsBuilder(final StatsModel model) {
        this.model = model;
        durationField = createDurationField();
    }

    protected JComponent createMainPanel() {
        final JPanel main = new JPanel(new BorderLayout());
        main.add(createFieldsPanel(), BorderLayout.CENTER);
        main.add(createToolbar(), BorderLayout.NORTH);
        return main;
    }

    protected void initCommands() {
        initCommand(new EntrySum(getCommandManager(), model));
    }

    protected void initModelListeners() {
        model.addPropertyChangeListener(StatsModel.PROPERTYNAME_SUM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                durationField.setText(model.getSum());
            }
        });
        model.getCurrentBeanModel().addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                getCommandManager().getCommand(Commands.COMMANDID_SUM).setEnabled(evt.getNewValue() != null);
            }
        });
    }

    private JComponent createToolbar() {
        return getCommandManager().getGroup(Commands.GROUPID_TOOLBAR).createToolBar("toolbar");
    }

    private JComponent createFieldsPanel() {
        final FormLayout layout = new FormLayout("12dlu, r:p, 4dlu, p:g(1.0), 12dlu", "12dlu, p, 8dlu, p, 12dlu:g(1.0)");
        final CellConstraints cc = new CellConstraints();
        final JPanel fields = new JPanel(layout);
        fields.add(new JLabel("Dauer:"), cc.xy(2, 2));
        fields.add(durationField, cc.xy(4, 2));
        return fields;
    }

    private static JTextField createDurationField() {
        final JTextField field = new JTextField();
        field.setEditable(false);
        return field;
    }
}
