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
package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.resource.Strings;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Examination application.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:33 $
 */
public class Main
{
    /**
     * Pass in the following arguments as system properties:
     *
     * <ul>
     * <li>The directory of the top picture directory</li>
     * <li>The directory containing the XML files containing the list of taxa</li>
     * <li>The name of the XML file for this session</li>
     * <li>The time to run the examination</li>
     * </ul>
     *
     * @param args not used
     * @throws Exception
     */
    public static void main( final String[] args ) throws Exception
    {
        UIManager.setLookAndFeel( "com.jgoodies.looks.windows.WindowsLookAndFeel" );
        Strings.setResourceBundle( ResourceBundle.getBundle( "Strings" ) );

        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                final MainForm f = new MainForm( "Prüfung mit Bildern" );
                f.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
                f.setExtendedState( JFrame.MAXIMIZED_BOTH );
                f.setVisible( true );
            }
        } );
    }
}
