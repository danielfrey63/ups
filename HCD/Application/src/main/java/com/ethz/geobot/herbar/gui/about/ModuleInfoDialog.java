package com.ethz.geobot.herbar.gui.about;

import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.component.table.BeanTableModel;
import ch.jfactory.component.table.SortedTable;
import ch.jfactory.update.LocalVersionLocator;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModuleInfoDialog extends I15nComponentDialog
{
    private static final int[] columnWidths = new int[]{50, 100, 20};

    public ModuleInfoDialog( final Frame owner, final String prefix ) throws HeadlessException
    {
        super( owner, prefix );
    }

    public ModuleInfoDialog( final Dialog owner, final String prefix ) throws HeadlessException
    {
        super( owner, prefix );
    }

    protected JComponent createComponentPanel()
    {
        // display version info of modules in table list
        final List<?> versions = LocalVersionLocator.locateVersions();
        final BeanTableModel btm = new BeanTableModel( versions,
                new String[]{"name", "description", "version"},
                new String[]{"Name", "Beschreibung", "Version"},
                true );
        final SortedTable moduleTable = new SortedTable( btm );
        moduleTable.setFocusable( false );

        final TableColumnModel model = moduleTable.getColumnModel();

        for ( int i = 0; i < 3; i++ )
        {
            model.getColumn( i ).setPreferredWidth( columnWidths[i] );
        }

        final JPanel panel = new JPanel();
        panel.setLayout( new BorderLayout() );
        panel.add( new JScrollPane( moduleTable ), BorderLayout.CENTER );

        return panel;
    }

    protected void onApply() throws ComponentDialogException
    {
    }

    protected void onCancel()
    {
    }

    protected boolean isCancelShowing()
    {
        return false;
    }

    public static class TableColumnAjuster
    {
        private final TableColumnModel model;

        private final JTable table;

        private final float[] relativeWidths;

        public TableColumnAjuster( final JTable table, final float[] relativeWidths )
        {
            this.table = table;
            this.relativeWidths = relativeWidths;
            model = table.getColumnModel();
            final int columnCount = model.getColumnCount();
            if ( columnCount != relativeWidths.length )
            {
                throw new IllegalArgumentException( "Relative widths given and columns must be same count" );
            }
            float total = 0;
            for ( final float relativeWidth : relativeWidths )
            {
                total += relativeWidth;
            }
            if ( total != 1.0 )
            {
                throw new IllegalArgumentException( "Relative widths must sum up to 1.0" );
            }

            table.addHierarchyListener( new HierarchyListener()
            {
                public void hierarchyChanged( final HierarchyEvent e )
                {
                    final Container top = table.getTopLevelAncestor();
                    if ( top instanceof JDialog || top instanceof JFrame )
                    {
                        if ( top.isVisible() && table.isVisible() )
                        {
                            ajustColumns();
                        }
                    }
                }
            } );
        }

        public void ajustColumns()
        {
            final int mode = table.getAutoResizeMode();
            table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
            final int totalColumnWidth = model.getTotalColumnWidth();
            final int columnCount = model.getColumnCount();
            for ( int i = 0; i < columnCount; i++ )
            {
                model.getColumn( i ).setWidth( (int) ( totalColumnWidth * relativeWidths[i] ) );
            }
            table.setAutoResizeMode( mode );
        }
    }
}
