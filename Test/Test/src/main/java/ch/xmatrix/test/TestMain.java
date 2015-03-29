package ch.xmatrix.test;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Created by Daniel on 27.03.2015.
 */
public class TestMain
{
    public static void main( String[] args )
    {
        final JLabel text1 = new JLabel( "Wenn etwas gelöscht werden soll, muss" );
        final JLabel text2 = new JLabel( "eBot anschliessen neu gestartet werden" );
        final JCheckBox resetSettings = new JCheckBox( "Alle Einstellungen löschen" );
        final JCheckBox resetPictures = new JCheckBox( "Alle gecachten Bilder löschen" );
        final int result = JOptionPane.showConfirmDialog( null, new JComponent[]{text1, text2, resetSettings, resetPictures}, "Zurücksetzen", JOptionPane.YES_NO_OPTION );
    }
}
