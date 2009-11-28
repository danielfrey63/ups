package ch.xmatrix.ups.pmb.exam;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import net.java.jveez.ui.thumbnails.ThumbnailListCellRenderer;

/**
 * Renders the exam thumbnail list.
 *
 * @author Daniel Frey 17.06.2008 17:31:20
 */
public class ExamThumbnailListCellRenderer extends ThumbnailListCellRenderer
{
    private final int thickness = 2;

    private final Color bg = new Color( 51, 51, 51 );

    private final Color selectedForeground = new Color( 255, 200, 0, 110 );

    private final Color deselectedForeground = new Color( 255, 255, 255, 110 );

    private final JPanel panel = new JPanel();

    private final JLabel number = new JLabel();

    private final Border selectedBorder = new CompoundBorder(
            new LineBorder( bg, 1 ),
            new LineBorder( Color.orange, thickness ) );

    private final Border unselectedBorder = new LineBorder( bg, 1 + thickness );

    public ExamThumbnailListCellRenderer()
    {
        setBackground( Color.gray );
        setAlignmentX( 0.5f );
        setAlignmentY( 0.5f );
        panel.setLayout( new OverlayLayout( panel ) );
        panel.setBackground( Color.gray );
        number.setFont( new Font( "Sans Serif", Font.BOLD, 65 ) );
        number.setOpaque( false );
        number.setForeground( deselectedForeground );
        number.setAlignmentX( 0.5f );
        number.setAlignmentY( 0.5f );
    }

    /**
     * {@inheritDoc}
     */
    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        final JLabel label = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
        label.setBorder( isSelected ? selectedBorder : unselectedBorder );
        number.setText( "" + ( index + 1 ) );
        number.setForeground( isSelected ? selectedForeground : deselectedForeground );
        panel.removeAll();
        panel.add( number );
        panel.add( label );

        return panel;
    }
}
