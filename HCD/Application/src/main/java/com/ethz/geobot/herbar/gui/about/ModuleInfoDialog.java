/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
package com.ethz.geobot.herbar.gui.about;

import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.component.table.BeanTableModel;
import ch.jfactory.component.table.SortState;
import ch.jfactory.component.table.SortedTable;
import ch.jfactory.update.LocalVersionLocator;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumnModel;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModuleInfoDialog extends I15nComponentDialog
{
    private static final int[] columnWidths = new int[]{150, 20};

    public ModuleInfoDialog( final Frame owner, final String prefix ) throws HeadlessException
    {
        super( owner, prefix );
    }

    protected JComponent createComponentPanel()
    {
        // display version info of modules in table list
        final List<?> versions = LocalVersionLocator.locateVersions();
        final BeanTableModel btm = new BeanTableModel( versions,
                new String[]{"name", "version"},
                new String[]{"Name", "Version"},
                true );
        final SortedTable moduleTable = new SortedTable( btm );
        moduleTable.setFocusable( false );
        final SortState sortState = new SortState();
        sortState.setDirective( new SortState.Directive( SortState.SORT_DESCENDING, 0 ) );
        moduleTable.getSortableModel().setSortState( sortState );

        final TableColumnModel model = moduleTable.getColumnModel();

        for ( int i = 0; i < columnWidths.length; i++ )
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
}
