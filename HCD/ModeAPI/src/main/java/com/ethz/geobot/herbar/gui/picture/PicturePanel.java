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

    private PictureDetailTab pictureTab;

    private PictureModel model;

    private JTextComponent textArea;

    private boolean showText = true;

    private PictureTheme[] shownThemes;

    /**
     * Creates new form PicturePanel.
     *
     * @param herbarModel the main model
     */
    public PicturePanel( final HerbarModel herbarModel )
    {
        this( herbarModel, true );
    }

    public PicturePanel( final HerbarModel herbarModel, final boolean showText )
    {
        this( herbarModel, herbarModel.getPictureThemes(), showText );
    }

    public PicturePanel( final HerbarModel herbarModel, final PictureTheme pictureTheme, final boolean showText )
    {
        this( herbarModel, new PictureTheme[]{pictureTheme}, showText );
    }

    public PicturePanel( final HerbarModel herbarModel, final PictureTheme[] themes, final boolean showText )
    {
        model = new PictureModel( herbarModel );
        shownThemes = themes;
        if ( !isThemeVisible( model.getPictureTheme() ) )
        {
            model.setPictureTheme( shownThemes[0] );
        }
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

    private boolean isThemeVisible( final PictureTheme theme )
    {
        for ( final PictureTheme shownTheme : shownThemes )
        {
            if ( shownTheme == theme )
            {
                return true;
            }
        }
        return false;
    }

    private void createTabs()
    {
        final PictureTheme[] themes = model.getPictureThemes();
        for ( int i = 0; i < themes.length; i++ )
        {
            final PictureDetailPanel detailPanel = pictureTab.addTab( themes[i], themes[i].getName() );
            pictureTab.setEnabledAt( i, isThemeVisible( themes[i] ) );
            if ( isThemeVisible( themes[i] ) )
            {
                createDetailPanelListener( detailPanel );
            }
        }
    }

    private void createDetailPanelListener( final PictureDetailPanel detailPanel )
    {
        detailPanel.addPropertyChangeListener( new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent e )
            {
                LOG.info( "propertyChange(" + e + ")" );
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
        for ( int i = 0; i < taxon.getPictureThemes().length; i++ )
        {
            final PictureTheme theme = taxon.getPictureThemes()[i];
            if ( !isThemeVisible( theme ) )
            {
                continue;
            }
            if ( model.getPictureTheme() == theme )
            {
                final CommentedPicture[] pics = taxon.getCommentedPictures( theme );
                for ( final CommentedPicture pic : pics )
                {
                    final String picture = pic.getPicture().getRelativURL();
                    getDetail( theme ).cacheImage( picture, true );
                }
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
        final PictureTheme[] themes = model.getPictureThemes();
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

    private String getComment( final CommentedPicture pic )
    {
        if ( pic == null )
        {
            return "";
        }
        if ( showText )
        {
            return pic.getComment();
        }
        else
        {
            return "";
        }
    }

    private int fillDetailPanel( final PictureDetailPanel detail, final PictureTheme theme )
    {
        int counter = 0;
        for ( Iterator j = model.getPictureCursor( theme ).getIterator(); j.hasNext(); )
        {
            final CommentedPicture pic = (CommentedPicture) j.next();
            final String picture = pic.getPicture().getRelativURL();
            detail.addImage( picture, ( showText ? getComment( pic ) : "" ) );
            detail.cacheImage( picture, theme != model.getPictureTheme() );
            counter++;
        }
        return counter;
    }

    private void fillTheme( final PictureTheme theme )
    {
        if ( !isThemeVisible( theme ) )
        {
            return;
        }
        final PictureDetailPanel detail = pictureTab.getThemePanel( theme );
        final int counter = fillDetailPanel( detail, theme );
        pictureTab.setEnabled( model.getIndex( theme ), counter != 0 );
    }

    private void setImage()
    {
        final PictureTheme theme = model.getPictureTheme();
        if ( !isThemeVisible( theme ) )
        {
            model.setPictureTheme( pictureTab.getTheme() );
        }
        final PictureDetailPanel panel = pictureTab.getThemePanel( model.getPictureTheme() );
        final CommentedPicture pic = model.getPicture();
        final boolean isPicture = pic != null;
        final boolean isShowing = showText && !( isPicture && "".equals( getComment( pic ) ) );
        final String comment = ( isShowing ? getComment( pic ) : "" );
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
        pictureTab = new PictureDetailTab( ImageLocator.pictLocator );
        pictureTab.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
        createTabs();
        pictureTab.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                LOG.info( "stateChanged(" + e + ")" );
                tabChanged();
            }
        } );
        return pictureTab;
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
