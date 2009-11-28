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
package ch.xmatrix.ups.uec.prefs;

import ch.xmatrix.ups.uec.master.MasterDetailsBuilderTest;
import java.util.Calendar;
import org.uispec4j.Panel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/21 11:02:52 $
 */
public class PrefsBuilderTest extends MasterDetailsBuilderTest
{
    public void test000Setup()
    {
        BUILDER = new PrefsBuilder();
        PANEL = new Panel( BUILDER.getPanel() );
        super.test000Setup();
    }

    public void test010InitialStates()
    {
        super.test010InitialStates();
    }

    public void test011SelectFirst()
    {
        super.test011SelectFirst();
        checkEnabled( false, false, false, false, false );
    }

    public void test012AddButton()
    {
        super.test012AddButton();
        checkEnabled( false, false, false, false, false );
    }

    public void test013SaveButton()
    {
        super.test013SaveButton();
    }

    public void test014TaxtreeCombo()
    {
        super.test014TaxtreeCombo();
        checkEnabled( true, true, true, true, true );
    }

    public void test015SaveButton()
    {
        super.test015SaveButton();
    }

    public void test016Rename()
    {
        super.test016Rename();
    }

    public void test017SaveButton()
    {
        super.test017SaveButton();
    }

    public void test018FixButton()
    {
        super.test018FixButton();
    }

    public void test019SaveButton()
    {
        super.test019SaveButton();
    }

    public void test020DeleteButton()
    {
        super.test020DeleteButton();
    }

    public void test021SaveButton()
    {
        super.test021SaveButton();
    }

    public void test100Init()
    {
        test012AddButton();
        test014TaxtreeCombo();
        test015SaveButton();
    }

    public void test101KnownTotalCount()
    {
        checkSpinner( PrefsBuilder.COMPONENT_KNOWNCOUNT );
    }

    public void test102KnownTotalCount()
    {
        checkSpinner( PrefsBuilder.COMPONENT_KNOWNWEIGHT );
    }

    public void test103KnownTotalCount()
    {
        checkSpinner( PrefsBuilder.COMPONENT_UNKNOWNCOUNT );
    }

    public void test104KnownTotalCount()
    {
        checkSpinner( PrefsBuilder.COMPONENT_UNKNOWNWEIGHT );
    }

    //--- Utilities

    private void checkSpinner( final String name )
    {
        final Calendar date1 = Calendar.getInstance();
        getSpinner( name ).setValue( 123 );
        final Calendar date2 = Calendar.getInstance();
        checkModificationUpdate( date1, date2 );
    }

    private void checkEnabled( final boolean knownCountEnabled, final boolean knownWeightEnabled,
                               final boolean unknownCountEnabled, final boolean unknowWeightEnabled,
                               final boolean cyclesEnabled )
    {
        assertEquals( knownCountEnabled, getSpinner( PrefsBuilder.COMPONENT_KNOWNCOUNT ).isEnabled() );
        assertEquals( knownWeightEnabled, getSpinner( PrefsBuilder.COMPONENT_KNOWNWEIGHT ).isEnabled() );
        assertEquals( unknownCountEnabled, getSpinner( PrefsBuilder.COMPONENT_UNKNOWNCOUNT ).isEnabled() );
        assertEquals( unknowWeightEnabled, getSpinner( PrefsBuilder.COMPONENT_UNKNOWNWEIGHT ).isEnabled() );
        assertEquals( cyclesEnabled, getSpinner( PrefsBuilder.COMPONENT_CYCLES ).isEnabled() );
    }
}
