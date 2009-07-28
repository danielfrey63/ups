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
package ch.xmatrix.ups.uec.groups;

import ch.xmatrix.ups.uec.groups.commands.Commands;
import ch.xmatrix.ups.uec.master.MasterDetailsBuilder;
import ch.xmatrix.ups.uec.master.MasterDetailsBuilderTest;
import java.util.Calendar;
import javax.swing.JComboBox;
import org.uispec4j.ComboBox;
import org.uispec4j.Panel;
import org.uispec4j.assertion.UISpecAssert;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/04/25 11:07:45 $
 */
public class GroupsBuilderTest extends MasterDetailsBuilderTest {

    private static final Integer ZERO = new Integer(0);

    public void test000Setup() {
        BUILDER = new GroupsBuilder();
        PANEL = new Panel(BUILDER.getPanel());
        super.test000Setup();
    }

    public void test010InitialStates() {
        super.test010InitialStates();
        checkButtons(false, false);
        checkComponents(false, false, false, false, true);
    }

    public void test011SelectFirst() {
        super.test011SelectFirst();
        checkEmpties(false, true, true, true);
    }

    public void test012AddButton() {
        super.test012AddButton();
        checkButtons(false, false);
        checkComponents(false, false, false, false, true);
    }

    public void test013SaveButton() {
        super.test013SaveButton();
        checkButtons(false, false);
        checkComponents(false, false, false, false, true);
    }

    public void test014TaxtreeCombo() {
        super.test014TaxtreeCombo();
        checkButtons(true, false);
        checkComponents(true, false, false, false, true);
    }

    public void test015SaveButton() {
        super.test015SaveButton();
        checkButtons(true, false);
        checkComponents(true, false, false, false, true);
    }

    public void test016Rename() {
        super.test016Rename();
        checkButtons(true, false);
        checkComponents(true, false, false, false, true);
    }

    public void test017SaveButton() {
        test015SaveButton();
    }

    public void test018FixButton() {
        super.test018FixButton();
        checkButtons(false, false);
        checkComponents(false, false, false, false, true);
    }

    public void test019SaveButton() {
        super.test019SaveButton();
        checkButtons(false, false);
        checkComponents(false, false, false, false, true);
    }

    public void test020DeleteButton() {
        super.test020DeleteButton();
        checkButtons(false, false);
        checkComponents(false, false, false, false, true);
    }

    public void test021SaveButton() {
        super.test021SaveButton();
        checkButtons(false, false);
        checkComponents(false, false, false, false, true);
    }

    public void test100Init() {
        test012AddButton();
        test014TaxtreeCombo();
        test016Rename();
        test017SaveButton();
    }

    public void test110AddButton() {
        final Calendar date1 = Calendar.getInstance();
        getButton(Commands.COMMANDID_NEWGROUP).click();
        final Calendar date2 = Calendar.getInstance();
        checkModificationUpdate(date1, date2);
        assertEquals("list doesn't have 1 entry,", 1, PANEL.getListBox(GroupsBuilder.COMPONENT_LISTGROUPS).getSize());
        checkButtons(true, true, true, true, true, true);
        checkButtons(true, true);
        checkComponents(true, true, true, true, true);
        checkEmpties(false, false, false, false);
    }

    public void test111Save() {
        super.test015SaveButton();
    }

    public void test112SelectGroup() {
        final Calendar date = Calendar.getInstance();
        PANEL.getListBox(GroupsBuilder.COMPONENT_LISTGROUPS).selectIndex(0);
        checkModificationBefore(date);
        checkButtons(true, true, true, true, false, false);
        checkButtons(true, true);
        checkComponents(true, true, true, true, true);
        checkEmpties(false, false, false, false);
    }

    public void test113DeselectGroup() {
        final Calendar date = Calendar.getInstance();
        PANEL.getListBox(GroupsBuilder.COMPONENT_LISTGROUPS).clearSelection();
        checkModificationBefore(date);
        checkButtons(true, true, true, true, false, false);
        checkButtons(true, false);
        checkComponents(true, false, false, false, true);
        checkEmpties(false, true, true, true);
    }

    public void test114ChangeName() {
        test112SelectGroup();
        final Calendar date1 = Calendar.getInstance();
        PANEL.getTextBox(GroupsBuilder.COMPONENT_NAME).setText("Automtic Test");
        final Calendar date2 = Calendar.getInstance();
        checkModificationUpdate(date1, date2);
        checkButtons(true, true, true, true, true, true);
    }

    public void test115Save() {
        super.test015SaveButton();
    }

    public void test116ChangeMinimum() {
        test112SelectGroup();
        final Calendar date1 = Calendar.getInstance();
        getSpinner(GroupsBuilder.COMPONENT_MINIMUM).setValue(123);
        final Calendar date2 = Calendar.getInstance();
        checkModificationUpdate(date1, date2);
        checkButtons(true, true, true, true, true, true);
    }

    public void test117Save() {
        super.test015SaveButton();
    }

    public void test118ChangeMaximum() {
        test112SelectGroup();
        final Calendar date1 = Calendar.getInstance();
        getSpinner(GroupsBuilder.COMPONENT_MAXIMUM).setValue(123);
        final Calendar date2 = Calendar.getInstance();
        checkModificationUpdate(date1, date2);
        checkButtons(true, true, true, true, true, true);
    }

    public void test119Save() {
        super.test015SaveButton();
    }

    public void test120SelectEach() {
        final ComboBox combo = PANEL.getComboBox(MasterDetailsBuilder.COMPONENT_COMBO_MODELS);
        final JComboBox box = (JComboBox) combo.getAwtComponent();
        final Object[] objects = box.getSelectedObjects();
        for (int i = 0; i < objects.length; i++) {
            final Object object = objects[i];
            box.setSelectedItem(object);
            checkEmpties(false, false, false, false);
        }
    }

    public void test121DeleteGroup() {
        getButton(Commands.COMMANDID_DELETEGROUP).click();
    }

    public void test199DeleteAll() {
        super.test020DeleteButton();
    }

    //--- Utilities

    private static void checkButtons(final boolean addEnabled, final boolean removeEnabled) {
        checkButtonEnabled(addEnabled, Commands.COMMANDID_NEWGROUP);
        checkButtonEnabled(removeEnabled, Commands.COMMANDID_DELETEGROUP);
    }

    private static void checkComponents(final boolean listEnabled, final boolean nameEnabled, final boolean minimumEnabled,
                                 final boolean maximumEnabled, final boolean treeEnabled) {
        checkEnabled(listEnabled, PANEL.getListBox(GroupsBuilder.COMPONENT_LISTGROUPS), "groups list");
        checkEnabled(nameEnabled, PANEL.getTextBox(GroupsBuilder.COMPONENT_NAME), "name field");
        checkEnabled(minimumEnabled, getSpinner(GroupsBuilder.COMPONENT_MINIMUM), "minimum field");
        checkEnabled(maximumEnabled, getSpinner(GroupsBuilder.COMPONENT_MAXIMUM), "maximum field");
        checkEnabled(treeEnabled, PANEL.getTree(GroupsBuilder.COMPONENT_TREE), "taxa tree");
    }

    private static void checkEmpties(final boolean listEmpty, final boolean nameEmpty, final boolean minimumEmpty,
                              final boolean maximumEmpty) {
        final String m1 = "group list should " + (listEmpty ? "" : "not ") + "be empty";
        UISpecAssert.assertEquals(m1, listEmpty, PANEL.getListBox(GroupsBuilder.COMPONENT_LISTGROUPS).isEmpty());
        final String m2 = "name field should " + (nameEmpty ? "" : "not ") + "be empty";
        UISpecAssert.assertEquals(m2, nameEmpty, PANEL.getTextBox(GroupsBuilder.COMPONENT_NAME).textIsEmpty());
        final String m3 = "minimum field should " + (nameEmpty ? "" : "not ") + "be empty";
        if (minimumEmpty) assertEquals(m3, minimumEmpty, getSpinner(GroupsBuilder.COMPONENT_MINIMUM).getValue().equals(ZERO));
        final String m4 = "maximum field should " + (nameEmpty ? "" : "not ") + "be empty";
        if (maximumEmpty) assertEquals(m4, maximumEmpty, getSpinner(GroupsBuilder.COMPONENT_MAXIMUM).getValue().equals(ZERO));
    }
}
