package ch.xmatrix.ups.pmb.exam;

import ch.xmatrix.ups.model.ExamsetModel;
import com.wegmueller.ups.lka.IAnmeldedaten;
import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Displays the student information in a list.
 *
 * @author Daniel Frey 17.06.2008 17:29:27
 */
class StudentsListCellRenderer extends DefaultListCellRenderer
{
    private final Color foregroundColor = Color.orange;

    private final Color backgroundColor = new Color( Color.orange.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 100 );

    private final DateFormat formatter = new SimpleDateFormat( "d.M.yyyy H:mm" );

    /**
     * {@inheritDoc}
     */
    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
        final ExamsetModel set = (ExamsetModel) value;
        final IAnmeldedaten anmeldedaten = set.getRegistration().getAnmeldedaten();
        final String id = anmeldedaten.getStudentennummer();
        final Calendar von = anmeldedaten.getPruefungsdatumVon();
        final String date = von == null ? "---" : formatter.format( von.getTime() );
        setText( anmeldedaten.getNachname() + ", " + anmeldedaten.getVorname() + " (" + ( id == null ? "---" : id ) + ", " + date + ")" );
        setForeground( isSelected ? foregroundColor : list.getForeground() );
        setBackground( isSelected ? backgroundColor : list.getBackground() );
        setOpaque( false );
        return this;
    }
}
