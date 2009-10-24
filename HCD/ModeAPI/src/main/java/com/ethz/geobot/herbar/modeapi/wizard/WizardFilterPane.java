package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.Dialogs;
import ch.jfactory.component.EditItem;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.wizard.filter.FilterWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterBasePane;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterDefinitionPane;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterNamePane;
import com.ethz.geobot.herbar.modeapi.wizard.filter.WizardFilterPreviewPane;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Category;

/**
 * WizardPane to display Filter selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardFilterPane extends WizardPane {

    private static final Category cat = Category.getInstance(WizardFilterPane.class);
    private static final boolean DEBUG = cat.isDebugEnabled();

    /**
     * name of the pane
     */
    public static final String NAME = "lesson.filter";

    private static final String INTRO_NAME = "filter.intro";
    private JList list;
    private EditItem current;
    private String modelPropertyName;
    private MiniListModel listModel;

    public WizardFilterPane(String modelPropertyName) {
        super(NAME, new String[]{modelPropertyName});
        this.modelPropertyName = modelPropertyName;
    }

    protected JPanel createDisplayPanel(String prefix) {
        JPanel text = createTextPanel(prefix);

        JLabel title = new JLabel(Strings.getString(prefix + ".TITLE.TEXT"));
        title.setFont(title.getFont().deriveFont(Font.BOLD));

        // the buttons need list to be ready
        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton add = createAddButton();
        JButton minus = createDeleteButton();
        JButton edit = createEditButton();
        JButton copy = createActivateButton();

        current = new EditItem(prefix + ".BUTTON");
        copy.setHorizontalTextPosition(JButton.LEFT);
        copy.setFocusPainted(false);

        JPanel panel = new JPanel(new GridBagLayout());
        int smallGap = Constants.GAP_WITHIN_TOGGLES;
        int bigGap = Constants.GAP_BETWEEN_GROUP;

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(text, gbc);

        gbc.gridy += 1;
        panel.add(current, gbc);

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(bigGap, 0, bigGap, 0);
        panel.add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, smallGap, smallGap);
        panel.add(add, gbc);

        gbc.gridx = 1;
        panel.add(minus, gbc);

        gbc.gridx = 2;
        panel.add(edit, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(copy, gbc);

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(list), gbc);

        return panel;
    }

    private JButton createActivateButton() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!isVisible()) {
                    return;
                }
                HerbarModel model = (HerbarModel) list.getSelectedValue();
                setModel(model);
                activate();
            }
        };
        final JButton button = createListEditButton(prefix + ".COPY.BUTTON", action);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Object selectedValue = list.getSelectedValue();
                button.setEnabled(selectedValue != null && selectedValue != current.getUserObject());
            }
        });
        button.setEnabled(false);
        return button;
    }

    private JButton createEditButton() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!isVisible()) {
                    return;
                }
                invokeEditFilterWizrad();
            }
        };
        final JButton button = createListEditButton(prefix + ".EDIT.BUTTON", action);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Set mutableModels = getWizardModel().getHerbarContext().getChangeableModels();
                button.setEnabled(ArrayUtils.contains(mutableModels.toArray(), list.getSelectedValue()));
            }
        });
        button.setEnabled(false);
        return button;
    }

    private JButton createDeleteButton() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!isVisible()) {
                    return;
                }
                String title = Strings.getString("WIZARD.LESSON.FILTER.DELETE.TITLE");
                String text = Strings.getString("WIZARD.LESSON.FILTER.DELETE.TEXT");
                if (Dialogs.CANCEL != Dialogs.showQuestionMessageOk(WizardFilterPane.this, title, text)) {
                    HerbarModel model = (HerbarModel) list.getSelectedValue();
                    listModel.deleteElement(model);
                    HerbarContext herbarContext = getWizardModel().getHerbarContext();
                    if (herbarContext.getCurrentModel() == model) {
                        String defaultModel = Strings.getString("FILTERNAME.ALLTAXA");
                        setModel(herbarContext.getModel(defaultModel));
                    }
                }
            }
        };
        final JButton button = createListEditButton(prefix + ".DELETE.BUTTON", action);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Set mutableModels = getWizardModel().getHerbarContext().getChangeableModels();
                button.setEnabled(ArrayUtils.contains(mutableModels.toArray(), list.getSelectedValue()));
            }
        });
        button.setEnabled(false);
        return button;
    }

    private JButton createAddButton() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!isVisible()) {
                    return;
                }
                invokeAddFilterWizard();
            }
        };
        JButton button = createListEditButton(prefix + ".ADD.BUTTON", action);
        return button;
    }

    private void invokeAddFilterWizard() {
        WizardPane[] panes = new WizardPane[]{
                new WizardIntroPane(INTRO_NAME),
                new WizardFilterNamePane(FilterWizardModel.FILTER_NAME),
                new WizardFilterBasePane(FilterWizardModel.FILTER_BASE),
                new WizardFilterDefinitionPane(FilterWizardModel.FILTER_MODEL),
                new WizardFilterPreviewPane(FilterWizardModel.FILTER_MODEL)
        };
        HerbarContext herbarContext = getWizardModel().getHerbarContext();
        NameValidator validator = new FilterModelNameValidater(herbarContext);
        String filterName = validator.getInitialName();
        HerbarModel base = herbarContext.getCurrentModel();
        FilterModel model = new FilterModel(base, filterName);
        String title = Strings.getString("WIZARD.FILTER.TITLE");
        FilterWizardModel wizardModel = new FilterWizardModel(herbarContext, panes, model, validator, title);
        Wizard dlg = new Wizard(wizardModel);
        boolean accepted = dlg.show((JDialog) getTopLevelAncestor(), 700, 388);

        FilterModel filterModel = wizardModel.getFilterModel();
        if (accepted && filterModel != null) {
            if (DEBUG) {
                cat.debug("store filter model: " + filterModel.getName());
            }
            listModel.addElement(filterModel);
        }
    }

    private void invokeEditFilterWizrad() {
        WizardPane[] panes = new WizardPane[]{
                new WizardIntroPane(INTRO_NAME),
                new WizardFilterNamePane(FilterWizardModel.FILTER_NAME),
                new WizardFilterBasePane(FilterWizardModel.FILTER_BASE),
                new WizardFilterDefinitionPane(FilterWizardModel.FILTER_MODEL),
                new WizardFilterPreviewPane(FilterWizardModel.FILTER_MODEL)
        };
        HerbarContext herbarContext = getWizardModel().getHerbarContext();
        NameValidator validator = new FilterModelNameValidater(herbarContext);
        FilterModel model = (FilterModel) list.getSelectedValue();
        validator.setInitialName(model.getName());
        String title = Strings.getString("WIZARD.FILTER.TITLE");
        FilterWizardModel wizardModel = new FilterWizardModel(herbarContext, panes, model, validator, title);
        Wizard dlg = new Wizard(wizardModel);
        boolean accepted = dlg.show((JDialog) getTopLevelAncestor(), 700, 388);

        if (accepted) {
            getWizardModel().getHerbarContext().saveModel(model);
        }
    }

    public void registerPropertyChangeListener(WizardModel model) {
        model.addPropertyChangeListener(modelPropertyName, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                HerbarModel model = (HerbarModel) event.getNewValue();
                current.setUserObject(model);
            }
        });
    }

    public void activate() {
        listModel = new MiniListModel(getWizardModel());
        list.setModel(listModel);
        current.setUserObject(((LessonWizardModel) getWizardModel()).getModel());
    }

    private void setModel(HerbarModel fmodel) {
        setProperty(modelPropertyName, fmodel);
    }

    public void initDefaultValues() {
    }

    public void fillModelSelection() {
    }

    public static class MiniListModel extends AbstractListModel {

        private WizardModel wizardModel;

        public MiniListModel(WizardModel wizardModel) {
            this.wizardModel = wizardModel;
        }

        public int getSize() {
            Set listData = wizardModel.getHerbarContext().getModels();
            return listData.size();
        }

        public Object getElementAt(int i) {
            Set models = wizardModel.getHerbarContext().getModels();
            HerbarModel[] listData = (HerbarModel[]) models.toArray(new HerbarModel[0]);
            Arrays.sort(listData, new ToStringComparator());
            return listData[ i ];
        }

        public void deleteElement(HerbarModel model) {
            Set models = wizardModel.getHerbarContext().getModels();
            HerbarModel[] listData = (HerbarModel[]) models.toArray(new HerbarModel[0]);
            Arrays.sort(listData, new ToStringComparator());
            List list = new ArrayList(Arrays.asList(listData));

            wizardModel.getHerbarContext().removeModel(model);
            int index = list.indexOf(model);
            fireIntervalRemoved(this, index, index);
        }

        public void addElement(HerbarModel model) {
            wizardModel.getHerbarContext().saveModel(model);

            Set models = wizardModel.getHerbarContext().getModels();
            HerbarModel[] listData = (HerbarModel[]) models.toArray(new HerbarModel[0]);
            Arrays.sort(listData, new ToStringComparator());
            List list = new ArrayList(Arrays.asList(listData));
            int index = list.indexOf(model);
            fireIntervalAdded(this, index, index);
        }
    }

    public static class FilterModelNameValidater implements NameValidator {

        private HerbarContext context;
        private String initialName;

        public FilterModelNameValidater(HerbarContext context) {
            this.context = context;
            this.initialName = getValidName();
        }

        public String getValidName() {
            String prefix = Strings.getString("WIZARD.FILTER.NAME.DEFAULT.PREFIX");
            prefix = (prefix.equals("") ? "New" : prefix);
            String pattern = Strings.getString("WIZARD.FILTER.NAME.DEFAULT.POSTFIX");
            pattern = (pattern.equals("") ? "000" : pattern);
            int postfix = 1;
            String newName;
            NumberFormat format = new DecimalFormat(pattern);
            while (!isValidName(newName = prefix + format.format(postfix))) {
                postfix++;
            }
            return newName;
        }

        public boolean isValidName(String newName) {
            boolean found = false;
            Set modelNames = context.getModelNames();
            for (Iterator iterator = modelNames.iterator(); iterator.hasNext();) {
                String name = (String) iterator.next();
                if (!name.equals(initialName) && name.equals(newName)) {
                    found = true;
                    break;
                }
            }
            return !found && !newName.equals("");
        }

        public String getInitialName() {
            return initialName;
        }

        public void setInitialName(String initialName) {
            this.initialName = initialName;
        }
    }
}
