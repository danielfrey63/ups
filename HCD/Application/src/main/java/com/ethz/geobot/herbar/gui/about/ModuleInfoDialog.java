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
import ch.jfactory.component.table.SortableTableModel;
import ch.jfactory.component.table.SortedTable;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.update.LocalVersionLocator;
import com.ethz.geobot.herbar.gui.mode.ModeManager;
import com.ethz.geobot.herbar.gui.picture.PictureCache;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumnModel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModuleInfoDialog extends I15nComponentDialog
{
    private static final Logger LOG = LoggerFactory.getLogger( ModuleInfoDialog.class );

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
        final SortableTableModel tm = new SortableTableModel( btm );
        tm.getSortState().setDecreasedDirective( 0 );
        final SortedTable moduleTable = new SortedTable( tm );
        moduleTable.setFocusable( false );

        final TableColumnModel model = moduleTable.getColumnModel();

        for ( int i = 0; i < columnWidths.length; i++ )
        {
            model.getColumn( i ).setPreferredWidth( columnWidths[i] );
        }

        final JPanel panel = new JPanel();
        panel.setLayout( new BorderLayout() );
        panel.add( new JScrollPane( moduleTable ), BorderLayout.CENTER );

        final JButton button = new JButton( "Erweitert..." );
        button.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final JLabel text = new JLabel( "Wenn etwas gelöscht werden soll, wird eBot sofort beendet" );
                final JCheckBox resetSettings = new JCheckBox( "Alle Einstellungen löschen" );
                final JCheckBox resetPictures = new JCheckBox( "Alle gecachten Bilder löschen" );
                final JCheckBox resetLists = new JCheckBox( "Alle Listen löschen" );
                final int result = JOptionPane.showConfirmDialog( null, new JComponent[]{text, resetSettings, resetPictures, resetLists}, "Zurücksetzen", JOptionPane.YES_NO_OPTION );
                if ( result == 0 )
                {
                    if ( resetSettings.isSelected() )
                    {
                        try
                        {
                            LOG.info( "deleting all settings for eBot" );
                            Preferences.userRoot().node( "ebot" ).removeNode();
                            LOG.info( "all settings deleted" );
                        }
                        catch ( BackingStoreException ex )
                        {
                            LOG.error( "error deleting all settings", ex );
                        }
                    }
                    if ( resetPictures.isSelected() )
                    {
                        try
                        {
                            LOG.info( "deleting all pictures for eBot" );
                            final PictureCache mainCache = ModeManager.getInstance().getMainCache();
                            mainCache.stop();
                            mainCache.resume();
                            final PictureCache backgroundCache = ModeManager.getInstance().getBackgroundCache();
                            backgroundCache.stop();
                            backgroundCache.resume();
                            Thread.currentThread().sleep( 1000 );
                            FileUtils.deleteDirectory( new File( ImageLocator.getPicturePath() ) );
                            LOG.info( "all pictures deleted" );
                        }
                        catch ( IOException ex )
                        {
                            LOG.error( "error deleting all images", ex );
                        }
                        catch ( InterruptedException ex )
                        {
                            LOG.error( "error sleeping", ex );
                        }
                    }
                    if ( resetLists.isSelected() )
                    {
                        try
                        {
                            LOG.info( "deleting all lists for eBot" );
                            FileUtils.deleteDirectory( new File( System.getProperty( "herbar.filter.location" ) ) );
                            LOG.info( "all lists deleted" );
                        }
                        catch ( IOException ex )
                        {
                            LOG.error( "error deleting all lists", ex );
                        }
                    }
                    if ( resetLists.isSelected() || resetPictures.isSelected() || resetSettings.isSelected() )
                    {
                        System.exit( 0 );
                    }
                }
            }
        } );
        final JPanel bottom = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        bottom.add( button );

        panel.add( bottom, BorderLayout.SOUTH );

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
