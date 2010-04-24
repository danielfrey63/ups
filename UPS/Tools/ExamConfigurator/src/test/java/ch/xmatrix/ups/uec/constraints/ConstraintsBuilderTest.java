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
package ch.xmatrix.ups.uec.constraints;

import ch.xmatrix.ups.uec.constraints.commands.Commands;
import ch.xmatrix.ups.uec.master.MasterDetailsBuilderTest;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JTree;
import org.uispec4j.Button;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.assertion.UISpecAssert;

/**
 * Test for constraints builder.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/04/25 11:07:45 $
 */
public class ConstraintsBuilderTest extends MasterDetailsBuilderTest
{
    public void test000Setup()
    {
        BUILDER = new ConstraintsBuilder();
        PANEL = new Panel( BUILDER.getPanel() );
        super.test000Setup();
    }

    public void test010InitialStates()
    {
        super.test010InitialStates();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
    }

    public void test011SelectFirst()
    {
        super.test011SelectFirst();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
        checkValues( false );
    }

    public void test012AddButton()
    {
        super.test012AddButton();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
    }

    public void test013SaveButton()
    {
        super.test013SaveButton();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
    }

    public void test014TaxtreeCombo()
    {
        super.test014TaxtreeCombo();
        checkButtons( true, false, false, false );
        checkComponents( true, false, false, true );
    }

    public void test015SaveButton()
    {
        super.test015SaveButton();
        checkButtons( true, false, false, false );
        checkComponents( true, false, false, true );
    }

    public void test016Rename()
    {
        super.test016Rename();
        checkButtons( true, false, false, false );
        checkComponents( true, false, false, true );
    }

    public void test017SaveButton()
    {
        test015SaveButton();
    }

    public void test018FixButton()
    {
        super.test018FixButton();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
    }

    public void test019SaveButton()
    {
        super.test019SaveButton();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
    }

    public void test020DeleteButton()
    {
        super.test020DeleteButton();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
    }

    public void test021SaveButton()
    {
        super.test021SaveButton();
        checkButtons( false, false, false, false );
        checkComponents( false, false, false, true );
    }

    public void test100Init()
    {
        super.test012AddButton();
        super.test014TaxtreeCombo();
        super.test015SaveButton();
    }

    public void test120NewConstraintButton()
    {
        final Calendar date1 = new GregorianCalendar();
        getButton( Commands.COMMAND_ID_NEW_CONSTRAINT ).click();
        final Calendar date2 = new GregorianCalendar();
        checkModificationUpdate( date1, date2 );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, false );
        checkComponents( true, false, false, true );
    }

    public void test121NewConstraintSelected()
    {
        final ListBox constraints = PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_CONSTRAINTS );
        UISpecAssert.assertFalse( "constraint is not selected", constraints.selectionIsEmpty() );
    }

    public void test122DeselectConstraint()
    {
        final String date = getModified();
        final ListBox constraints = PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_CONSTRAINTS );
        constraints.clearSelection();
        checkModificationSame( date );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, false, false, false );
        checkComponents( true, false, false, true );
        UISpecAssert.assertTrue( "constraints list should have no selection", constraints.selectionIsEmpty() );
    }

    public void test123SelectConstraint()
    {
        final String date = getModified();
        final ListBox constraints = PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_CONSTRAINTS );
        constraints.selectIndex( 0 );
        checkModificationSame( date );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, false );
        checkComponents( true, false, false, true );
        UISpecAssert.assertFalse( "constraints list should have selection", constraints.selectionIsEmpty() );
    }

    public void test124Save()
    {
        super.test015SaveButton();
        checkButtons( true, true, false, false );
        checkComponents( true, false, false, true );
    }

    public void test125SelectNode()
    {
        final String date = getModified();
        PANEL.getTree( ConstraintsBuilder.COMPONENT_TREE_TAXA ).getJTree().setSelectionRow( 1 );
        checkModificationSame( date );
        checkButtons( true, true, true, true, false, false );
        checkButtons( true, true, true, false );
        checkComponents( true, false, false, true );
    }

    public void test126AddTaxon()
    {
        final Calendar date1 = new GregorianCalendar();
        getButton( Commands.COMMAND_ID_ADD_TAXA ).click();
        final Calendar date2 = new GregorianCalendar();
        checkModificationUpdate( date1, date2 );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, false );
        checkComponents( true, false, true, true );
        assertEquals( "taxa list should have 1 entrie,", 1, PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_TAXA ).getSize() );
    }

    public void test129Save()
    {
        super.test015SaveButton();
        checkButtons( true, true, false, false );
        checkComponents( true, false, true, true );
    }

    public void test130RemoveConstraintButton()
    {
        final Calendar date1 = new GregorianCalendar();
        getButton( Commands.COMMAND_ID_DELETE_CONSTRAINT ).click();
        final Calendar date2 = new GregorianCalendar();
        checkModificationUpdate( date1, date2 );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, false, false, false );
        checkComponents( true, false, false, true );
    }

    public void test131Save()
    {
        super.test015SaveButton();
        checkButtons( true, false, false, false );
        checkComponents( true, false, false, true );
    }

    public void test132AddSameTaxon()
    {
        test120NewConstraintButton();
        test124Save();
        test125SelectNode();
        test126AddTaxon();
        PANEL.getTree( ConstraintsBuilder.COMPONENT_TREE_TAXA ).getJTree().setSelectionRow( 1 );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, false );
        checkComponents( true, false, true, true );
    }

    public void test133AddAnotherTaxon()
    {
        PANEL.getTree( ConstraintsBuilder.COMPONENT_TREE_TAXA ).getJTree().setSelectionRow( 2 );
        final Calendar date1 = new GregorianCalendar();
        getButton( Commands.COMMAND_ID_ADD_TAXA ).click();
        final Calendar date2 = new GregorianCalendar();
        assertEquals( "taxa list should have 2 entries,", 2, PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_TAXA ).getSize() );
        checkModificationUpdate( date1, date2 );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, false );
        checkComponents( true, true, true, true );
    }

    public void test134Save()
    {
        super.test015SaveButton();
        checkButtons( true, true, false, false );
        checkComponents( true, true, true, true );
    }

    public void test134SelectTaxon()
    {
        final Calendar date = Calendar.getInstance();
        PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_TAXA ).selectIndex( 0 );
        checkModificationBefore( date );
        checkComponents( true, true, false, true, false, false, false, false );
        checkButtons( true, true, false, true );
        checkComponents( true, true, true, true );
    }

    public void test135RemoveTaxon()
    {
        final Calendar date1 = new GregorianCalendar();
        getButton( Commands.COMMAND_ID_REMOVE_TAXA ).click();
        final Calendar date2 = new GregorianCalendar();
        assertEquals( "taxa list should have 1 entries,", 1, PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_TAXA ).getSize() );
        checkModificationUpdate( date1, date2 );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, true );
        checkComponents( true, false, true, true );
    }

    public void test136RemoveTaxon()
    {
        final Calendar date1 = new GregorianCalendar();
        getButton( Commands.COMMAND_ID_REMOVE_TAXA ).click();
        final Calendar date2 = new GregorianCalendar();
        assertEquals( "taxa list should have 0 entries,", 0, PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_TAXA ).getSize() );
        checkModificationUpdate( date1, date2 );
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, false );
        checkComponents( true, false, false, true );
    }

    public void test137Save()
    {
        super.test015SaveButton();
        checkButtons( true, true, false, false );
        checkComponents( true, false, false, true );
    }

    public void test138AddSpecies()
    {
        final JTree tree = PANEL.getTree( ConstraintsBuilder.COMPONENT_TREE_TAXA ).getJTree();
        for ( int i = 0; i < 4; i++ )
        {
            tree.expandRow( i );
        }
        tree.setSelectionRow( 4 );
        getButton( Commands.COMMAND_ID_ADD_TAXA ).click();
        checkButtons( true, true, true, true, true, true );
        checkButtons( true, true, false, false );
        checkComponents( true, false, false, true );
        assertEquals( "taxa list should have 1 entrie,", 1, PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_TAXA ).getSize() );
    }

    public void test199RemoveAll()
    {
        super.test020DeleteButton();
    }

    //--- Utilities

    private static void checkComponents( final boolean fieldDescriptionEnabled, final boolean fieldNameEnabled,
                                         final boolean spinnerCountEnabled, final boolean treeTaxaEnabled )
    {
        checkEnabled( fieldDescriptionEnabled, PANEL.getTextBox( ConstraintsBuilder.COMPONENT_FIELD_DESCRIPTION ), "description field" );
        checkEnabled( fieldNameEnabled, PANEL.getTextBox( ConstraintsBuilder.COMPONENT_FIELD_NAME ), "name field" );
        checkEnabled( spinnerCountEnabled, getSpinner( ConstraintsBuilder.COMPONENT_SPINNER_COUNT ), "counter" );
        checkEnabled( treeTaxaEnabled, PANEL.getTree( ConstraintsBuilder.COMPONENT_TREE_TAXA ), "taxa tree" );
    }

    private static void checkButtons( final boolean newEnabled, final boolean deleteEnabled,
                                      final boolean addEnabled, final boolean removeEnabled )
    {
        final String[] buttons = {Commands.COMMAND_ID_NEW_CONSTRAINT, Commands.COMMAND_ID_DELETE_CONSTRAINT,
                Commands.COMMAND_ID_ADD_TAXA, Commands.COMMAND_ID_REMOVE_TAXA};
        final boolean[] enableds = new boolean[]{newEnabled, deleteEnabled, addEnabled, removeEnabled};
        for ( int i = 0; i < buttons.length; i++ )
        {
            final String name = buttons[i];
            final boolean enabled = enableds[i];
            final Button button = getButton( name );
            final String message = "button " + name + " is " + ( enabled ? "disabled" : "enabled" ) +
                    ", should be " + ( enabled ? "enabled" : "disabled" );
            UISpecAssert.assertEquals( message, enabled, button.isEnabled() );
        }
    }

    private static void checkValues( final boolean constraintsListEmpty )
    {
        UISpecAssert.assertEquals( constraintsListEmpty, PANEL.getListBox( ConstraintsBuilder.COMPONENT_LIST_CONSTRAINTS ).isEmpty() );
    }
}
