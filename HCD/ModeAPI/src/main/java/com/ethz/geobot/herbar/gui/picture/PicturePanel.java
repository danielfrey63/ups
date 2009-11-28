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
import org.apache.log4j.Logger;

/**
 * This control is used to display the Pictures of the different Picture-Themes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class PicturePanel extends JPanel
{
    private final static Logger LOG = Logger.getLogger( PicturePanel.class );

    private PictureDetailTab pictureTab;

    private PictureDetailPanel pictureDetailPanel;

    private PictureModel model;

    private JTextComponent textArea;

    private boolean showText = true;

    private PictureTheme[] shownThemes;

    /**
     * Creates new form PicturePanel
     *
     * @param herbarModel Description of the Parameter
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
            final PictureDetailPanel detailPanel = pictureTab.addTheme( themes[i] );
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
                    zoomChanged( ( (Boolean) e.getNewValue() ).booleanValue() );
                }
            }
        } );
    }

    private PictureDetailPanel getDetail( final PictureTheme theme )
    {
        if ( pictureTab == null )
        {
            return pictureDetailPanel;
        }
        else
        {
            return pictureTab.getThemePanel( theme );
        }
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

        if ( pictureTab != null )
        {
            pictureTab.clearCachingList();
            pictureTab.clearAll();
        }
        else
        {
            pictureDetailPanel.clearCachingList();
            pictureDetailPanel.clear();

        }
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
        if ( pictureTab != null )
        {
            final PictureDetailPanel detail = pictureTab.getThemePanel( theme );
            final int counter = fillDetailPanel( detail, theme );
            pictureTab.setEnabled( model.getIndex( theme ), counter != 0 );
        }
        else
        {
            fillDetailPanel( pictureDetailPanel, theme );
        }
    }

    private void setImage()
    {
        PictureDetailPanel panel = pictureDetailPanel;
        if ( pictureTab != null )
        {
            final PictureTheme theme = model.getPictureTheme();
            if ( !isThemeVisible( theme ) )
            {
                model.setPictureTheme( pictureTab.getTheme() );
            }
            panel = pictureTab.getThemePanel( model.getPictureTheme() );
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initGUI()
    {
        setLayout( new BorderLayout() );
        if ( true )
        {
            add( buildSplitter(), BorderLayout.CENTER );
        }
        else
        {
            add( buildTab() );
        }
    }

    private JComponent buildSplitter()
    {
        int y = 0;
        final JPanel panel = new JPanel( new GridBagLayout() );
        panel.add( buildTab(), new GridBagConstraints( 0, y++, 1, 1, 1, 1, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        panel.add( buildTextArea(), new GridBagConstraints( 0, y++, 1, 1, 1, 0, GridBagConstraints.SOUTH,
                GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        return panel;
    }

    private JComponent buildTab()
    {
        if ( true )
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
        else
        {
            pictureDetailPanel = new PictureDetailPanel( ImageLocator.pictLocator );
            createDetailPanelListener( pictureDetailPanel );
            return pictureDetailPanel;
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

// $Log: PicturePanel.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.22  2005/06/17 06:39:58  daniel_frey
// New ActionButton icons and some corrections on documentation
//
// Revision 1.21  2004/08/31 22:10:16  daniel_frey
// Examlist loading working
//
// Revision 1.20  2004/04/25 13:56:42  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.19  2004/03/04 23:39:28  daniel_frey
// - Build with News on Splash
//
// Revision 1.18  2003/05/01 20:48:52  daniel_frey
// - Added switching of imgage resolution
//
// Revision 1.17  2003/04/13 21:51:15  daniel_frey
// - Separated status actions and display into toolbar and status bar
//
// Revision 1.16  2003/04/11 08:27:52  daniel_frey
// - Made sure that thumbnails do not show tooltip text when appropriate
//
// Revision 1.15  2003/04/02 14:49:03  daniel_frey
// - Revised wizards
//
// Revision 1.14  2003/03/14 09:51:00  thomas_wegmueller
// *** empty log message ***
//
// Revision 1.13  2003/03/13 15:05:12  thomas_wegmueller
// *** empty log message ***
//
// Revision 1.12  2003/03/03 12:27:02  thomas_wegmueller
// PicturePanel's update
//
// Revision 1.11  2003/02/13 11:08:43  thomas_wegmueller
// *** empty log message ***
//
// Revision 1.10  2003/01/22 14:44:31  daniel_frey
// - Removed unused code
//
// Revision 1.9  2002/11/05 11:21:58  daniel_frey
// - Level with tree from GraphNode
//
// Revision 1.8  2002/09/23 13:29:37  thomas_wegmueller
// Images update from Waegi
//
// Revision 1.7  2002/08/05 11:27:12  Dani
// - Moved to ch.xmatrix
//
// Revision 1.6  2002/08/02 00:42:20  Dani
// Optimized import statements
//
// Revision 1.5  2002/08/01 15:49:06  Dani
// Removed ambigous references claimed by jikes
//
// Revision 1.4  2002/05/30 16:01:57  Thomas
// ScrollerPanel updates
//
// Revision 1.3  2002/05/28 14:08:21  Thomas
// first shot of desired screendesign
//