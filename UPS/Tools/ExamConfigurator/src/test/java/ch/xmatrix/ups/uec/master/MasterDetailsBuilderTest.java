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
package ch.xmatrix.ups.uec.master;

import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.uec.master.commands.Commands;
import ch.xmatrix.ups.controller.Loader;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import org.uispec4j.Button;
import org.uispec4j.ComboBox;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UIComponent;
import org.uispec4j.UISpec4J;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.finder.ComponentMatcher;
import junit.framework.TestCase;

/**
 * MasterDetails tests. To subclass this test, make sure to either override all tests, or none, to keep the execution
 * order correct.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/08/04 15:50:01 $
 */
public class MasterDetailsBuilderTest extends TestCase {

    public static final String COMBOVALUE_TAXTREE = "Taxa Herbar CD-ROM Version 2 ohne Moose und Flechten";
    public static final String MODELNAME_NEW = "Neu";
    public static final String MODELNAME_TEST = "Automatischer Test";

    protected static Panel PANEL;
    protected static DetailsBuilder BUILDER;

    static {
        System.setProperty(Loader.ENVIRONMENT_SIMULATESAVE, "true");
        UISpec4J.init();
        Strings.setResourceBundle(ResourceBundle.getBundle("ch.xmatrix.ups.uec.Strings"));
    }

    public void test000Setup() {
    }

    public void test010InitialStates() {
        checkButtons(true, false, false, false, false, false);
        checkComponents(true, false, false, false, true, true, true, true);
    }

    public void test011SelectFirst() {
        final JComboBox combo = (JComboBox) PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS).getAwtComponent();
        final Calendar date = Calendar.getInstance();
        combo.setSelectedIndex(0);
        checkModificationBefore(date);
        checkButtons(true, true, true, false, false, false);
        checkComponents(true, false, false, false, false, false, false, false);
    }

    public void test012AddButton() {
        final Calendar date1 = Calendar.getInstance();
        PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_ADD)).click();
        final Calendar date2 = Calendar.getInstance();
        UISpecAssert.assertTrue(PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS).selectionEquals(MODELNAME_NEW));
        UISpecAssert.assertFalse(PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS).selectionEquals(MODELNAME_NEW + "_"));
//        UISpecAssert.assertTrue(PANEL.getTextBox(MasterDetailsBuilder.COMPONENT_FIELD_NAME).textEquals(MODELNAME_NEW));
//        UISpecAssert.assertFalse(PANEL.getTextBox(MasterDetailsBuilder.COMPONENT_FIELD_NAME).textEquals(MODELNAME_NEW + "_"));
        checkButtons(true, true, true, false, true, true);
        checkComponents(true, true, false, true, false, false, false, true);
        checkModificationUpdate(date1, date2);
    }

    public void test013SaveButton() {
        final Calendar date = Calendar.getInstance();
        PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_SAVE)).click();
        checkButtons(true, true, true, false, false, false);
        checkComponents(true, true, false, true, false, false, false, true);
        checkModificationBefore(date);
    }

    public void test014TaxtreeCombo() {
        final Calendar date1 = Calendar.getInstance();
        PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_TAXTREES).select(COMBOVALUE_TAXTREE);
        final Calendar date2 = Calendar.getInstance();
        UISpecAssert.assertTrue(PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_TAXTREES).selectionEquals(COMBOVALUE_TAXTREE));
        checkButtons(true, true, true, true, true, true);
        checkComponents(true, true, false, true, false, false, false, false);
        checkModificationUpdate(date1, date2);
    }

    public void test015SaveButton() {
        final Calendar date = Calendar.getInstance();
        PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_SAVE)).click();
        checkButtons(true, true, true, true, false, false);
        checkComponents(true, true, false, true, false, false, false, false);
        checkModificationBefore(date);
    }

    public void test016Rename() {
        final ComboBox box = PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS);
        final JComboBox comboBox = (JComboBox) box.getAwtComponent();
        final JTextField field = (JTextField) comboBox.getEditor().getEditorComponent();
        final Calendar date1 = Calendar.getInstance();
        field.setText(MODELNAME_TEST);
        final Calendar date2 = Calendar.getInstance();
        UISpecAssert.assertTrue(box.selectionEquals(MODELNAME_TEST));
        checkButtons(true, true, true, true, true, true);
        checkComponents(true, true, false, true, false, false, false, false);
        checkModificationUpdate(date1, date2);
    }

    public void test017SaveButton() {
        final Calendar date = Calendar.getInstance();
        PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_SAVE)).click();
        checkButtons(true, true, true, true, false, false);
        checkComponents(true, true, false, true, false, false, false, false);
        checkModificationBefore(date);
    }

    public void test018FixButton() {
        final Calendar date1 = Calendar.getInstance();
        PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_FIX)).click();
        final Calendar date2 = Calendar.getInstance();
        checkButtons(true, true, true, false, true, true);
        checkComponents(true, false, false, false, false, false, false, false);
        checkModificationUpdate(date1, date2);
    }

    public void test019SaveButton() {
        final Calendar date = Calendar.getInstance();
        PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_SAVE)).click();
        checkButtons(true, true, true, false, false, false);
        checkComponents(true, false, false, false, false, false, false, false);
        checkModificationBefore(date);
    }

    public void test020DeleteButton() {
        final Button button = PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_DELETE));
        while (button.isEnabled().isTrue()) {
            button.click();
        }
        checkButtons(true, false, false, false, true, true);
        checkComponents(true, false, false, false, true, true, true, true);
    }

    public void test021SaveButton() {
        PANEL.getButton(new ActionCommandComponentMatcher(Commands.COMMANDID_SAVE)).click();
        checkButtons(true, false, false, false, false, false);
        checkComponents(true, false, false, false, true, true, true, true);
    }

    //--- Utilities

    protected static void checkModificationUpdate(final Calendar date1, final Calendar date2) {
        final TextBox fieldModified = PANEL.getTextBox(MasterDetailsBuilder.COMPONENT_FIELD_MODIFIED);
        final String textModified = fieldModified.getText();
        try {
            final DateFormat format = MasterDetailsBuilder.DATEFORMAT;
            final Date before = format.parse(format.format(date1.getTime()));
            final Date after = format.parse(format.format(date2.getTime()));
            final Date there = format.parse(textModified);
            final String message = "expected date between \"" + format.format(before) + "\" and \"" +
                    format.format(after) + "\", but got \"" + textModified + "\"";
            assertTrue(message, there.after(before) || there.equals(before));
            assertTrue(message, there.before(after) || there.equals(after));
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertFalse("date \"" + textModified + "\" not parsable", true);
        }
    }

    protected static void checkModificationBefore(final Calendar date) {
        final TextBox fieldModified = PANEL.getTextBox(MasterDetailsBuilder.COMPONENT_FIELD_MODIFIED);
        final String textModified = fieldModified.getText();
        try {
            final DateFormat format = MasterDetailsBuilder.DATEFORMAT;
            final Date after = format.parse(format.format(date.getTime()));
            final Date there = format.parse(textModified);
            final String message = "expected date after \"" + format.format(after) + "\", but got \"" + textModified + "\"";
            assertTrue(message, there.before(after) || there.equals(after));
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertFalse("date \"" + textModified + "\" not parsable", true);
        }
    }

    /**
     * Compares the date given to the date in the modification field. Assertion failes if they are different.
     *
     * @param date the date to compare to the actual modification date
     */
    protected static void checkModificationSame(final String date) {
        assertEquals(date, getModified());
    }

    protected static void checkButtons(final boolean addEnabled, final boolean copyEnabled, final boolean deleteEnabled,
                                       final boolean fixEnabled, final boolean loadEnabled, final boolean saveEnabled) {
        final String[] buttons = {Commands.COMMANDID_ADD, Commands.COMMANDID_COPY, Commands.COMMANDID_DELETE,
                Commands.COMMANDID_FIX, Commands.COMMANDID_LOAD, Commands.COMMANDID_SAVE};
        final boolean[] enableds = new boolean[]{addEnabled, copyEnabled, deleteEnabled, fixEnabled, loadEnabled, saveEnabled};
        for (int i = 0; i < buttons.length; i++) {
            final String name = buttons[i];
            final boolean enabled = enableds[i];
            checkButtonEnabled(enabled, name);
        }
    }

    protected static void checkComponents(final boolean comboModelsEnabled, final boolean fieldNameEnabled,
                                          final boolean fieldModifiedEnabled, final boolean comboTaxtreesEnabled,
                                          final boolean comboModelsEmpty, final boolean fieldNameEmpty,
                                          final boolean fieldModifiedEmpty, final boolean comboTaxtreesEmpty) {
        checkEnabled(comboModelsEnabled, PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS), "models combo");
        checkComboEditable(fieldNameEnabled, PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS), "models combo");
        checkEnabled(fieldModifiedEnabled, PANEL.getTextBox(MasterDetailsBuilder.COMPONENT_FIELD_MODIFIED), "midification field");
        checkEnabled(comboTaxtreesEnabled, PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_TAXTREES), "taxtrees combo");
        checkEmpty(comboModelsEmpty, PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS), "models combo");
        checkEditableComboEmpty(fieldNameEmpty, PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS), "combo model editor field");
        checkEmpty(fieldModifiedEmpty, PANEL.getTextBox(MasterDetailsBuilder.COMPONENT_FIELD_MODIFIED), "modified field");
        checkEmpty(comboTaxtreesEmpty, PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_TAXTREES), "taxatree combo");
    }

    protected static void checkEmpty(final boolean fieldEmpty, final TextBox field, final String name) {
        final String message = name + " should " + (fieldEmpty ? "" : "not ") + "be empty";
        UISpecAssert.assertEquals(message, fieldEmpty, field.textIsEmpty());
    }

    protected static void checkEmpty(final boolean comboEmpty, final ComboBox combo, final String name) {
        final String message = name + " should " + (comboEmpty ? "be emtpy" : "not be empty");
        UISpecAssert.assertEquals(message, comboEmpty, combo.selectionEquals(null));
    }

    protected static void checkEditableComboEmpty(final boolean fieldNameEmpty, final ComboBox combo, final String name) {
        final JComboBox comboBox = (JComboBox) combo.getAwtComponent();
        final JTextField field = (JTextField) comboBox.getEditor().getEditorComponent();
        final String message = name + " should " + (fieldNameEmpty ? "" : "not ") + "be empty.";
        if (comboBox.isEditable()) assertEquals(message, fieldNameEmpty, field.getText().equals(""));
    }

    protected static void checkComboEditable(final boolean comboEditable, final ComboBox combo, final String name) {
        final String message = name + " should " + (comboEditable ? "" : "not ") + "be editable";
        UISpecAssert.assertEquals(message, comboEditable, combo.isEditable());
    }

    protected static void checkEnabled(final boolean enabled, final UIComponent component, final String name) {
        final String message = name + " should be " + (enabled ? "enabled" : "disabled");
        UISpecAssert.assertEquals(message, enabled, component.isEnabled());
    }

    protected static void checkEnabled(final boolean enabled, final JComponent component, final String name) {
        final String message = name + " should be " + (enabled ? "enabled" : "disabled") + ",";
        assertEquals(message, enabled, component.isEnabled());
    }

    protected static void checkButtonEnabled(final boolean enabled, final String name) {
        final Button button = PANEL.getButton(new ActionCommandComponentMatcher(name));
        final String message = "button " + name + " is " + (enabled ? "disabled" : "enabled") +
                ", should be " + (enabled ? "enabled" : "disabled");
        UISpecAssert.assertEquals(message, enabled, button.isEnabled());
    }

    protected static JSpinner getSpinner(final String name) {
        final Component[] components = PANEL.getSwingComponents(JSpinner.class);
        for (int i = 0; i < components.length; i++) {
            final JSpinner component = (JSpinner) components[i];
            final String componentName = component.getName();
            if (componentName != null && componentName.equals(name)) {
                return component;
            }
        }
        return null;
    }

    protected static String getModified() {
        return PANEL.getTextBox(MasterDetailsBuilder.COMPONENT_FIELD_MODIFIED).getText();
    }

    protected static Button getButton(final String name) {
        return PANEL.getButton(new ActionCommandComponentMatcher(name));
    }

    protected static class ActionCommandComponentMatcher implements ComponentMatcher {

        private String name;

        public ActionCommandComponentMatcher(final String name) {
            this.name = name;
        }

        public boolean matches(final Component component) {
            if (!(component instanceof JButton)) {
                return false;
            }
            final JButton button = (JButton) component;
            return button.getActionCommand().equals(name);
        }
    }
}
