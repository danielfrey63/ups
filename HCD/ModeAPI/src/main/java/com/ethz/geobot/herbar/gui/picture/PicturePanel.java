/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.image.PictureDetailPanel;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
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

    /**
     * Creates new picture panel showing the text with all themes in the model.
     *
     * @param herbarModel the main model
     */
    public PicturePanel( final HerbarModel herbarModel )
    {
        this( herbarModel, true, herbarModel.getPictureThemes() );
    }

    /**
     * Creates the picture panel with the given themes and allows to hide the text.
     *
     * @param herbarModel the main model
     * @param showText    whether to show the text
     * @param themes      the themes to show
     */
    public PicturePanel( final HerbarModel herbarModel, final boolean showText, final PictureTheme... themes )
    {
        model = new PictureModel( herbarModel );
        this.themes = themes;
        initGUI();
        setShowText( showText );
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
                LOG.info( "propertyChange(" + e.getPropertyName() + ")" );
                if ( e.getPropertyName().equalsIgnoreCase( PictureDetailPanel.IMAGE ) )
                {
                    imageChanged( e.getNewValue().toString() );
                }
                else
                {
                    zoomChanged( (Boolean) e.getNewValue() );
                }
            }
        } );
    }

    private PictureDetailPanel getDetail( final PictureTheme theme )
    {
        return pictureTab.getThemePanel( theme );
    }

    public void cacheTaxon( final Taxon taxon )
    {
        LOG.debug( "cacheTaxon(" + taxon + ")" );
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
                final String picture = pic.getPicture().getRelativURL();
                getDetail( theme ).cacheImage( picture, true );
            }
        }
    }

    /**
     * Changes the Taxon to be displayed in the PicturePanel
     *
     * @param taxon taxon to be displayed
     */
    public void setTaxon( final Taxon taxon )
    {
        LOG.info( "setTaxon( " + taxon + ")" );
        model.setTaxon( taxon );
        pictureTab.clearCachingList();
        pictureTab.clearAll();
        for ( final PictureTheme theme : themes )
        {
            fillTheme( theme );
        }
        setImage();
    }

    public void setShowText( final boolean showText )
    {
        this.showText = showText;
        textArea.setVisible( showText );
        // make sure thumbs do (not) show tooltips
        if ( model != null && model.getTaxon() != null )
        {
            setTaxon( model.getTaxon() );
        }
    }

    private int fillDetailPanel( final PictureDetailPanel detail, final PictureTheme theme )
    {
        int counter = 0;
        for ( Iterator j = model.getPictureCursor( theme ).getIterator(); j.hasNext(); )
        {
            final CommentedPicture pic = (CommentedPicture) j.next();
            final String picture = pic.getPicture().getRelativURL();
            detail.addImage( picture, ( showText ? pic.getComment() : "" ) );
            detail.cacheImage( picture, theme != model.getPictureTheme() );
            counter++;
        }
        return counter;
    }

    private void fillTheme( final PictureTheme theme )
    {
        final PictureDetailPanel detail = pictureTab.getThemePanel( theme );
        final int counter = fillDetailPanel( detail, theme );
        pictureTab.setEnabled( model.getIndex( theme ), counter != 0 );
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
        panel.setImage( ( isPicture ? pic.getPicture().getName() : null ) );
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
        pictureTab = new TabbedPictureDetailPanel( ImageLocator.pictLocator );
        pictureTab.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
        createTabs();
        pictureTab.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                LOG.info( "tab changed)" );
                tabChanged();
            }
        } );
        return pictureTab;
    }

    private void createTabs()
    {
        for ( final PictureTheme theme : themes )
        {
            final PictureDetailPanel detailPanel = pictureTab.addTab( theme, theme.getName() );
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
