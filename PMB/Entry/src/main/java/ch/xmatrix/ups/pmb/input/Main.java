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
package ch.xmatrix.ups.pmb.input;

import ch.jfactory.application.CheckThreadViolationRepaintManager;
import ch.jfactory.resource.OperatingSystem;
import javax.swing.JFrame;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:29 $
 */
public class Main
{
    public static void main( final String[] args ) throws Exception
    {
        RepaintManager.setCurrentManager( new CheckThreadViolationRepaintManager() );
        if ( OperatingSystem.IS_OS_WINDOWS )
        {
            UIManager.setLookAndFeel( "com.jgoodies.looks.windows.WindowsLookAndFeel" );
        }
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                final JFrame form = new MainForm( new MainModel() );
                form.setSize( 800, 600 );
                form.setLocationRelativeTo( null );
                form.setVisible( true );
            }
        } );
    }
}