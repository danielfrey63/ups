/*
 * Herbar CD-ROM version 2
 *
 * LessonPanel.java
 *
 * Created on 26. April 2002
 * Created by dirk
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.component.split.NiceSplitPane;
import ch.jfactory.component.tab.NiceTabbedPane;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.DefaultResultModel;
import com.ethz.geobot.herbar.gui.DetailResultModel;
import com.ethz.geobot.herbar.gui.InterrogatorComplexityFactory;
import com.ethz.geobot.herbar.gui.PropertyInterrogator;
import com.ethz.geobot.herbar.gui.TransientDetailResultModel;
import com.ethz.geobot.herbar.gui.picture.PicturePanel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.HerbarGUIManager;
import com.ethz.geobot.herbar.modeapi.ModeActivationPanel;
import com.ethz.geobot.herbar.modeapi.SimpleTaxStateModel;
import com.ethz.geobot.herbar.model.EcoAttribute;
import com.ethz.geobot.herbar.model.EcoSubject;
import com.ethz.geobot.herbar.model.EcoText;
import com.ethz.geobot.herbar.model.EcoValue;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MedAttribute;
import com.ethz.geobot.herbar.model.MedSubject;
import com.ethz.geobot.herbar.model.MedText;
import com.ethz.geobot.herbar.model.MedValue;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorSubject;
import com.ethz.geobot.herbar.model.MorText;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;

/**
 * Will be added to the BaseFrame as an own mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class LessonPanel extends ModeActivationPanel implements PropertyChangeListener
{
    private final static Logger LOG;

    private final TaxStateModel taxStateModel;

    private final PropertyDisplayer lessonPanel;

    private final PropertyInterrogator askPanel;

    private final PicturePanel picturePanel;

    private final LessonBar lessonBar;

    private final JTabbedPane lessonSwitcher;

    /**
     * Creates new form LessonPanel. This panel will be instanciated by reflection.
     *
     * @param mode the mode of this panel
     */
    public LessonPanel( final LessonMode mode )
    {
        LOG.info( this + " initiate mode panel" );
        /*
         * The constructor is called by reflection, so any exceptions occurring
         * during construction would be visible as a simple InvocationException.
         * To keep the relevant excception, make sure to catch and rethrow
         * the exceptions in the constructor.
         */
        try
        {
            final HerbarContext context = mode.getHerbarContext();
            final HerbarGUIManager guiManager = context.getHerbarGUIManager();
            final JFrame parent = guiManager.getParentFrame();
            final HerbarModel herbarModel = context.getDataModel();

            taxStateModel = new TaxStateModel( herbarModel );

            lessonPanel = new PropertyDisplayer( herbarModel );
            askPanel = new PropertyInterrogator( context, initResultModel( herbarModel ) );
            askPanel.setTaxFocus( taxStateModel.getFocus() );

            lessonSwitcher = new NiceTabbedPane( JTabbedPane.TOP );
            lessonSwitcher.add( "Lehrgang", lessonPanel );
            lessonSwitcher.add( "Abfrage", askPanel );
            lessonSwitcher.addChangeListener( new ChangeListener()
            {
                public void stateChanged( final ChangeEvent e )
                {
                    final Component selectedComponent = lessonSwitcher.getSelectedComponent();
                    if ( askPanel == selectedComponent )
                    {
                        lessonBar.switchToAskMode();
                        picturePanel.setShowText( false );
                    }
                    else if ( lessonPanel == selectedComponent )
                    {
                        lessonBar.switchToLessonMode();
                        picturePanel.setShowText( true );
                    }
                }
            } );

            picturePanel = new PicturePanel( herbarModel );

            lessonBar = new LessonBar( parent, context, taxStateModel );

            final JSplitPane splitPane = new NiceSplitPane();
            splitPane.setResizeWeight( 0 );
            splitPane.add( picturePanel, JSplitPane.LEFT );
            splitPane.add( lessonSwitcher, JSplitPane.RIGHT );

            setLayout( new BorderLayout() );
            add( lessonBar, BorderLayout.NORTH );
            add( splitPane, BorderLayout.CENTER );

            taxStateModel.addPropertyChangeListener( SimpleTaxStateModel.FOCUS, this );
            askPanel.addChangeListener( new ChangeListener()
            {
                public void stateChanged( final ChangeEvent e )
                {
                    lessonPanel.synchronizeTabs( askPanel );
                }
            } );
            lessonPanel.addChangeListener( new ChangeListener()
            {
                public void stateChanged( final ChangeEvent e )
                {
                    askPanel.synchronizeTabs( lessonPanel );
                }
            } );

            splitPane.setDividerLocation( 400 );
        }
        catch ( RuntimeException e )
        {
            LOG.fatal( "LessonPanel(...)", e );
            throw e;
        }
        catch ( Error e )
        {
            LOG.error( "LessonPanel(...)", e );
            throw e;
        }
    }

    private DefaultResultModel initResultModel( final HerbarModel herbarModel )
    {
        final DefaultResultModel resultModel = new DefaultResultModel( herbarModel );
        DetailResultModel detailModel;

        detailModel = new TransientDetailResultModel( MorText.class, MorAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter( new Class[]{MorSubject.class, MorAttribute.class, MorValue.class, MorText.class},
                new int[][]{{1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Fernliegende", VirtualGraphTreeNodeFilter.getFilter( new Class[]{Taxon.class, MorText.class, MorValue.class, MorAttribute.class, MorSubject.class,
                MorAttribute.class, MorValue.class, MorText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 1, 1}, {1, 0, 0, 2},
                        {0, 0, 0, 2}, {1, 0, 0, 2}} ) ) );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Naheliegende", VirtualGraphTreeNodeFilter.getFilter( new Class[]{Taxon.class, MorText.class, MorValue.class, MorAttribute.class, MorSubject.class,
                MorAttribute.class, MorValue.class, MorText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 1, 2},
                        {0, 0, 0, 2}, {1, 0, 0, 2}} ) ) );
        resultModel.add( detailModel );

        detailModel = new TransientDetailResultModel( EcoText.class, EcoAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter( new Class[]{EcoSubject.class, EcoAttribute.class, EcoValue.class, EcoText.class},
                new int[][]{{1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        resultModel.add( detailModel );

        detailModel = new TransientDetailResultModel( MedText.class, MedAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter( new Class[]{MedSubject.class, MedAttribute.class, MedValue.class, MedText.class},
                new int[][]{{1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        resultModel.add( detailModel );
        return resultModel;
    }

    public void propertyChange( final PropertyChangeEvent e )
    {
        LOG.info( "set focus to " + e );
        final Taxon focus = (Taxon) e.getNewValue();
        lessonPanel.setTaxFocus( focus );
        askPanel.setTaxFocus( focus );
        picturePanel.setTaxon( focus );
        picturePanel.cacheTaxon( taxStateModel.getNext( focus ) );
        picturePanel.cacheTaxon( taxStateModel.getPrev( focus ) );
    }

    public void setModel( final HerbarModel newModel )
    {
        taxStateModel.setModel( newModel );
    }

    public void setScope( final Taxon taxon )
    {
        taxStateModel.setScope( taxon );
    }

    public void setLevel( final Level level )
    {
        taxStateModel.setLevel( level );
    }

    public void setOrdered( final boolean ordered )
    {
        taxStateModel.setSortedList( ordered );
    }

    public void setFocus( final Taxon focus )
    {
        taxStateModel.setFocus( focus );
    }

    public HerbarModel getModel()
    {
        return taxStateModel.getModel();
    }

    public Taxon getScope()
    {
        return taxStateModel.getScope();
    }

    public Level getLevel()
    {
        return taxStateModel.getLevel();
    }

    public boolean isOrdered()
    {
        return taxStateModel.isSortedList();
    }

    public Taxon getFocus()
    {
        return taxStateModel.getFocus();
    }

    public void activate()
    {
        super.activate();
        lessonBar.activate();
    }

    public void deactivate()
    {
        super.deactivate();
        lessonBar.deactivate();
    }

    static
    {
        LogUtils.init();
        try
        {
            Strings.addResourceBundle( LessonMode.class,
                    ResourceBundle.getBundle( LessonPanel.class.getName() ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        LOG = Logger.getLogger( LessonPanel.class );
    }
}
