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
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.trait.Ecology;
import com.ethz.geobot.herbar.model.trait.EcologyAttribute;
import com.ethz.geobot.herbar.model.trait.EcologySubject;
import com.ethz.geobot.herbar.model.trait.EcologyText;
import com.ethz.geobot.herbar.model.trait.EcologyValue;
import com.ethz.geobot.herbar.model.trait.Medicine;
import com.ethz.geobot.herbar.model.trait.MedicineAttribute;
import com.ethz.geobot.herbar.model.trait.MedicineSubject;
import com.ethz.geobot.herbar.model.trait.MedicineText;
import com.ethz.geobot.herbar.model.trait.MedicineValue;
import com.ethz.geobot.herbar.model.trait.Morphology;
import com.ethz.geobot.herbar.model.trait.MorphologyAttribute;
import com.ethz.geobot.herbar.model.trait.MorphologySubject;
import com.ethz.geobot.herbar.model.trait.MorphologyText;
import com.ethz.geobot.herbar.model.trait.MorphologyValue;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final PropertyDisplay lessonPanel;

    private final PropertyInterrogator askPanel;

    private final PicturePanel picturePanel;

    private final LessonBar lessonBar;

    private final JTabbedPane lessonSwitcher;

    /**
     * Creates new form LessonPanel. This panel will be instantiated by reflection.
     *
     * @param mode the mode of this panel
     */
    public LessonPanel( final LessonMode mode )
    {
        LOG.info( "initiate mode panel" );
        /*
         * The constructor is called by reflection, so any exceptions occurring
         * during construction would be visible as a simple InvocationException.
         * To keep the relevant exception, make sure to catch and re-throw
         * the exceptions in the constructor.
         */
        try
        {
            final HerbarContext context = mode.getHerbarContext();
            final HerbarGUIManager guiManager = context.getHerbarGUIManager();
            final JFrame parent = guiManager.getParentFrame();
            final HerbarModel herbarModel = context.getDataModel();

            taxStateModel = new TaxStateModel( herbarModel );

            lessonPanel = new PropertyDisplay( context, taxStateModel );
            askPanel = new PropertyInterrogator( initResultModel( herbarModel ) );
            askPanel.setTaxFocus( taxStateModel.getFocus() );

            lessonSwitcher = new JTabbedPane( JTabbedPane.TOP );
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

            splitPane.setDividerLocation( 740 );
        }
        catch ( RuntimeException e )
        {
            LOG.error( "LessonPanel(...)", e );
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

        detailModel = new TransientDetailResultModel( Morphology.class, "1", MorphologyAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter(
                new Class[]{Morphology.class, MorphologySubject.class, MorphologyAttribute.class, MorphologyValue.class, MorphologyText.class},
                new int[][]{{0, 0, 0, 2}, {1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Fernliegende", VirtualGraphTreeNodeFilter.getFilter(
                new Class[]{Taxon.class, MorphologyText.class, MorphologyValue.class, MorphologyAttribute.class, MorphologySubject.class, MorphologyAttribute.class, MorphologyValue.class, MorphologyText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 1, 1}, {1, 0, 0, 2}, {0, 0, 0, 2}, {1, 0, 0, 2}} ) ) );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Naheliegende", VirtualGraphTreeNodeFilter.getFilter(
                new Class[]{Taxon.class, MorphologyText.class, MorphologyValue.class, MorphologyAttribute.class, MorphologySubject.class, MorphologyAttribute.class, MorphologyValue.class, MorphologyText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 1, 2}, {0, 0, 0, 2}, {1, 0, 0, 2}} ) ) );
        resultModel.add( detailModel );

        detailModel = new TransientDetailResultModel( Ecology.class, "2", EcologyAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter(
                new Class[]{Ecology.class, EcologySubject.class, EcologyAttribute.class, EcologyValue.class, EcologyText.class},
                new int[][]{{0, 0, 0, 2}, {1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        resultModel.add( detailModel );

        detailModel = new TransientDetailResultModel( Medicine.class, "3", MedicineAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter(
                new Class[]{Medicine.class, MedicineSubject.class, MedicineAttribute.class, MedicineValue.class, MedicineText.class},
                new int[][]{{0, 0, 0, 2}, {1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        resultModel.add( detailModel );

        return resultModel;
    }

    public void propertyChange( final PropertyChangeEvent e )
    {
        LOG.info( "setting focus to \"" + e.getNewValue() + "\"" );
        final Taxon focus = (Taxon) e.getNewValue();
        lessonPanel.setTaxFocus( focus );
        askPanel.setTaxFocus( focus );
        picturePanel.setTaxon( focus, taxStateModel.getNext( focus ), taxStateModel.getPrev( focus ) );
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
        LOG = LoggerFactory.getLogger( LessonPanel.class );
    }
}
