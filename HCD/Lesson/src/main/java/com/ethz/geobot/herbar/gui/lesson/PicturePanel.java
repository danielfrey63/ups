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
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.lang.ArrayUtils;
import com.ethz.geobot.herbar.gui.picture.PictureModel;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ethz.geobot.herbar.gui.lesson.PictureDetailPanel.IMAGE;

/**
 * This control is used to display the Pictures of the different Picture-Themes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class PicturePanel extends JPanel
{
    private final static Logger LOG = LoggerFactory.getLogger( PicturePanel.class );

    private TabbedPictureDetailPanel pictureTab;

    private PictureModel model;

    private JTextComponent textArea;

    private boolean showText = true;

    private PictureTheme[] themes;

    private PictureCache cache;

    /**
     * Creates new picture panel showing the text with all themes in the model.
     *
     * @param themes the themes in this panel to show
     * @param model
     * @param cache
     */
    public PicturePanel( final PictureTheme[] themes, final PictureModel model, final PictureCache cache )
    {
        this.model = model;
        this.cache = cache;
        this.themes = themes;

        initGUI();
        setShowText( true );
    }

    private void tabChanged()
    {
        model.setSelectedIndex( pictureTab.getSelectedIndex() );
        setImage();
    }

    private void imageChanged( final String name )
    {
        model.setPicture( name );
        setImage();
    }

    private void zoomChanged( final boolean b )
    {
        model.setZoomed( b );
        setImage();
    }

    private void createDetailPanelListener( final PictureDetailPanel detailPanel )
    {
        detailPanel.addPropertyChangeListener( new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent e )
            {
                if ( e.getPropertyName().equalsIgnoreCase( IMAGE ) )
                {
                    imageChanged( e.getNewValue() == null ? null : e.getNewValue().toString() );
                }
                else
                {
                    zoomChanged( (Boolean) e.getNewValue() );
                }
            }
        } );
    }

    private void cacheTaxon( final Taxon taxon )
    {
        LOG.debug( "caching taxon \"" + taxon + "\"" );
        if ( taxon == null )
        {
            return;
        }
        final PictureTheme[] modelThemes = taxon.getPictureThemes();
        final PictureTheme[] themes = ArrayUtils.intersect( modelThemes, this.themes, new PictureTheme[0] );
        for ( final PictureTheme theme : themes )
        {
            final CommentedPicture[] pics = taxon.getCommentedPictures( theme );
            for ( final CommentedPicture pic : pics )
            {
                final Picture inner = pic.getPicture();
                if ( inner != null )
                {
                    final String picture = inner.getRelativURL();
                    cache.cacheImage( picture, true, false );
                }
                else
                {
                    LOG.error( "picture for " + pic + " is null" );
                }
            }
        }
    }

    /**
     * Changes the Taxon to be displayed in the PicturePanel and optionally caches the taxa given.
     *
     * @param taxon   taxon to be displayed
     * @param toCache taxa that should be cached
     */
    public void setTaxon( final Taxon taxon, final Taxon... toCache )
    {
        LOG.info( "setting taxon to \"" + taxon + "\"" );
        model.setTaxon( taxon );
        pictureTab.clearAll();
        for ( final PictureTheme theme : themes )
        {
            final PictureDetailPanel detail = pictureTab.getThemePanel( theme );
            final int counter = fillDetailPanel( detail, theme );
            pictureTab.setEnabled( model.getIndex( theme ), counter != 0 );
        }
        for ( final Taxon taxonToCache : toCache )
        {
            if ( taxonToCache != null )
            {
                cacheTaxon( taxonToCache );
            }
        }
        setImage();
    }

    public void setShowText( final boolean showText )
    {
        this.showText = showText;
        textArea.setVisible( showText );
    }

    private int fillDetailPanel( final PictureDetailPanel detail, final PictureTheme theme )
    {
        int counter = 0;
        for ( Iterator j = model.getPictureCursor( theme ).getIterator(); j.hasNext(); )
        {
            final CommentedPicture picture = (CommentedPicture) j.next();
            final Picture inner = picture.getPicture();
            if ( inner != null )
            {
                final String name = inner.getRelativURL();
                detail.addImage( name, counter, ( showText ? picture.getComment() : "" ) );
                cache.cacheImage( name, theme != model.getPictureTheme(), false );
                counter++;
            }
            else
            {
                LOG.error( "picture for " + picture + " is null" );
            }
        }
        return counter;
    }

    private void setImage()
    {
        final PictureDetailPanel panel = pictureTab.getThemePanel( model.getPictureTheme() );
        final CommentedPicture pic = model.getPicture();
        final boolean isPicture = pic != null;
        final boolean isShowing = showText && !( isPicture && "".equals( pic.getComment() ) );
        final String comment = ( isShowing && pic != null ? pic.getComment() : "" );
        textArea.setVisible( isShowing );
        textArea.setText( comment );
        panel.setToolTipText( comment );
        panel.setImagePanel( ( isPicture ? ( pic.getPicture() == null ? null : pic.getPicture().getName() ) : null ) );
        panel.setZoomed( model.isZoomed() );
    }

    /** This method is called from within the constructor to initialize the form. */
    private void initGUI()
    {
        setLayout( new BorderLayout() );
        add( buildSplitter(), BorderLayout.CENTER );
    }

    private JComponent buildSplitter()
    {
        int y = 0;
        final JPanel panel = new JPanel( new GridBagLayout() );
        panel.add( buildTab(), new GridBagConstraints( 0, y++, 1, 1, 1, 1, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        panel.add( buildTextArea(), new GridBagConstraints( 0, y, 1, 1, 1, 0, GridBagConstraints.SOUTH,
                GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        return panel;
    }

    private JComponent buildTab()
    {
        pictureTab = new TabbedPictureDetailPanel( cache );
        pictureTab.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
        createTabs();
        pictureTab.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                LOG.info( "tab changed" );
                tabChanged();
            }
        } );
        return pictureTab;
    }

    private void createTabs()
    {
        for ( final PictureTheme theme : themes )
        {
            final PictureDetailPanel detailPanel = pictureTab.addTab( theme );
            createDetailPanelListener( detailPanel );
        }
    }

    private JComponent buildTextArea()
    {
        final int gap = Constants.GAP_WITHIN_TOGGLES;
        textArea = new JTextPane();
        textArea.setEditable( false );
        textArea.setFocusable( false );
        textArea.setOpaque( false );
        textArea.setBorder( new EmptyBorder( gap, gap, gap, gap ) );
        return textArea;
    }
}
