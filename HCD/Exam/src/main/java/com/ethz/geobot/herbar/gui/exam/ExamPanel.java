/*
 * Herbar CD-ROM version 2
 *
 * ExamPanel.java
 *
 * Created on Feb 12, 2003 6:44:41 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui.exam;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.view.border.BevelDirection;
import ch.jfactory.application.view.border.ThinBevelBorder;
import ch.jfactory.application.view.status.StatusBar;
import ch.jfactory.application.view.status.TimerPanel;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.Dialogs;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.DefaultResultModel;
import com.ethz.geobot.herbar.gui.InterrogatorComplexityFactory;
import com.ethz.geobot.herbar.gui.MemorizingDetailResultModel;
import com.ethz.geobot.herbar.gui.PropertyInterrogator;
import com.ethz.geobot.herbar.gui.PropertyInterrogatorPanel;
import com.ethz.geobot.herbar.gui.ResultModel;
import com.ethz.geobot.herbar.gui.TaxonNameInterrogator;
import com.ethz.geobot.herbar.gui.picture.PicturePanel;
import com.ethz.geobot.herbar.gui.util.IteratorControlPanel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.HerbarGUIManager;
import com.ethz.geobot.herbar.modeapi.ModeActivationPanel;
import com.ethz.geobot.herbar.modeapi.state.TaxFocusListener;
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
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <Comments here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:14 $
 */
public class ExamPanel extends ModeActivationPanel implements TaxFocusListener
{
    private static final Logger LOG;

    /** Identifier for card layout */
    private static final String INTRO = "INTRO";

    /** Identifier for card layout */
    private static final String EXAM = "EXAM";

    /** Identifier for card layout */
    private static final String RESULT = "RESULT";

    private static final int SIZE = 10;

    private final CardLayout cards;

    private final ExamStateModel examStateModel;

    private final HerbarGUIManager guiManager;

    private final HerbarModel herbarModel;

    private JButton restartButton;

    private JButton startButton;

    private JPanel resultPanel;

    private Level familyLevel;

    private Level speciesLevel;

    private PicturePanel picturePanel;

    private PropertyInterrogator asker;

    private ResultModel resultModel;

    private final StatusBar statusBar;

    private TaxonNameInterrogator familyInterrogator;

    private TaxonNameInterrogator speciesInterrogator;

    private TaxonNameInterrogator.MemorizingInterrogatorModel familyInterrogatorModel;

    private TaxonNameInterrogator.MemorizingInterrogatorModel speciesInterrogatorModel;

    private final TimerPanel timer;

    private boolean finished;

    private boolean started;

    private final int minutes;

    private InterrogatorComplexityFactory.Type filter1;

    private InterrogatorComplexityFactory.Type filter2;

    private InterrogatorComplexityFactory.Type filter3;

    private IteratorControlPanel navigator;

    public ExamPanel( final ExamMode mode )
    {
        try
        {
            // Init model
            final HerbarContext herbarContext = mode.getHerbarContext();
            guiManager = herbarContext.getHerbarGUIManager();
            herbarModel = herbarContext.getDataModel();
            statusBar = guiManager.getStatusBar();
            examStateModel = new ExamStateModel( herbarModel, SIZE );
            examStateModel.addTaxFocusListener( this );

            minutes = Integer.parseInt( Strings.getString( ExamMode.class, "EXAM.DURATION" ) );

            // Init layout
            cards = new CardLayout( 0, 0 );
            setLayout( cards );

            timer = createTimer();

            add( createIntroPanel(), INTRO );
            add( createExamPanel(), EXAM );
            add( createStatisticsPanel(), RESULT );

            statusBar.setText( "Prüfung bereit" );
        }
        catch ( RuntimeException e )
        {
            LOG.error( "RuntimeException in ExamPanel constructor", e );
            throw e;
        }
        catch ( Error e )
        {
            LOG.error( "Error in ExamPanel constructor", e );
            throw e;
        }
    }

    public void activate()
    {
        super.activate();
        statusBar.addStatusComponent( timer );
        startIntro();
        getRootPane().setDefaultButton( startButton );
    }

    public void deactivate()
    {
        super.deactivate();
        statusBar.removeStatusComponent( timer );
    }

    public boolean queryDeactivate()
    {
        return !started || finishExam();
    }

    private void startIntro()
    {
        timer.resetTime();
        cards.show( this, INTRO );
        finished = false;
        getRootPane().setDefaultButton( startButton );
    }

    private void startExam()
    {
        speciesInterrogatorModel.reset();
        familyInterrogatorModel.reset();
        navigator.reset();
        examStateModel.reset( SIZE );
        finished = false;
        resultModel.reset();
        final Taxon focus = examStateModel.getCurrentTaxon();
        taxFocusChanged( null, focus );
        cards.show( this, EXAM );
        statusBar.addStatusComponent( navigator.getDisplay(), 1 );
        started = true;
        timer.resetTime();
        timer.start();
    }

    private boolean finishExam()
    {
        // Ask for finishing
        if ( finished || !started )
        {
            return finished;
        }
        final String question = Strings.getString( ExamMode.class, "EXAM_CLOSE.QUESTION" );
        final String title = Strings.getString( ExamMode.class, "EXAM_CLOSE.TITLE" );
        final int ret = Dialogs.showQuestionMessageOk( this, title, question );
        if ( ret == Dialogs.CANCEL )
        {
            return finished;
        }
        return stopExam();
    }

    private boolean stopExam()
    {
        // Calculate result
        resultPanel.removeAll();
        fillStatistics();

        // Stop and display result
        examStateModel.reset( SIZE );

        timer.stop();
        statusBar.setText( Strings.getString( ExamMode.class, "BUTTON.STOP.TEXT" ) );
        statusBar.removeStatusComponent( navigator.getDisplay() );
        cards.show( this, RESULT );

        finished = true;
        started = false;

        getRootPane().setDefaultButton( restartButton );

        return finished;
    }

    //==== Intro

    private JPanel createIntroPanel()
    {
        // start button
        startButton = new JButton( Strings.getString( ExamMode.class, "EXAM.START.TEXT" ) );
        startButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                startExam();
            }
        } );

        final JPanel startButtonPanel = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 15 ) );
        startButtonPanel.add( startButton );

        final JPanel introPanel = new JPanel( new GridLayout( 2, 1 ) );
        introPanel.setBorder( new EmptyBorder( 15, 15, 15, 15 ) );
        introPanel.add( createReadOnlyHTMLField( Strings.getString( ExamMode.class, "INTRO.TEXT" ) ) );
        introPanel.add( startButtonPanel, BorderLayout.CENTER );

        return introPanel;
    }

    private TimerPanel createTimer()
    {
        final TimerPanel timer = new TimerPanel( 60 * minutes, new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final String question = Strings.getString( ExamMode.class, "EXAM_STOP.QUESTION" );
                final String title = Strings.getString( ExamMode.class, "EXAM_STOP.TITLE" );
                final String[] buttons = new String[]{Strings.getString( "OK.TEXT" )};
                final JFrame frame = guiManager.getParentFrame();
                final int m = JOptionPane.QUESTION_MESSAGE;
                JOptionPane.showOptionDialog( frame, question, title, 0, m, null, buttons, buttons[0] );
                stopExam();
            }
        } );
        final Dimension prefSize = timer.getPreferredSize();
        timer.setPreferredSize( new Dimension( (int) ( prefSize.width * 1.5 ), prefSize.height ) );
        return timer;
    }

    private JPanel createReadOnlyHTMLField( final String text )
    {
        final JTextPane field = new JTextPane();
        field.setBackground( getBackground() );
        field.setEnabled( false );
        field.setContentType( "text/html" );
        field.setText( text );
        final SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setAlignment( attributes, StyleConstants.ALIGN_CENTER );
        final StyledDocument doc = field.getStyledDocument();
        doc.setParagraphAttributes( 0, doc.getLength(), attributes, false );

        final JPanel panel = new JPanel( new BorderLayout() );
        final JScrollPane scrollPane = new JScrollPane( field );
        scrollPane.setBorder( BorderFactory.createEmptyBorder() );
        panel.add( scrollPane, BorderLayout.CENTER );
        return panel;
    }

    //==== Exam

    private JPanel createExamPanel()
    {
        picturePanel = new PicturePanel( herbarModel, false, herbarModel.getPictureTheme( "Herbar" ) );

        initFilter();

        final ResultModel resultModel = createResultModel();
        asker = new PropertyInterrogator( resultModel );

        final JPanel toolBar = new JPanel( new BorderLayout() );
        toolBar.add( createTopToolbar(), BorderLayout.NORTH );
        toolBar.add( createInterrogatorToolbar(), BorderLayout.CENTER );

        // Lower main part
        final JSplitPane mainSplit = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        mainSplit.setTopComponent( picturePanel );
        mainSplit.setBottomComponent( asker );
        mainSplit.setResizeWeight( 1.0 );

        final JPanel examPanel = new JPanel( new BorderLayout() );
        examPanel.add( toolBar, BorderLayout.NORTH );
        examPanel.add( mainSplit, BorderLayout.CENTER );

        return examPanel;
    }

    private JPanel createInterrogatorToolbar()
    {
        speciesLevel = herbarModel.getLastLevel();
        speciesInterrogatorModel = new TaxonNameInterrogator.MemorizingInterrogatorModel();
        speciesInterrogator = new TaxonNameInterrogator( speciesLevel, speciesInterrogatorModel );
        speciesInterrogator.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, Constants.GAP_BETWEEN_REGIONS ) );

        familyLevel = herbarModel.getLevel( "Familie" );
        familyInterrogatorModel = new TaxonNameInterrogator.MemorizingInterrogatorModel();
        familyInterrogator = new TaxonNameInterrogator( familyLevel, familyInterrogatorModel );

        final JPanel secondToolBar = new JPanel( new GridLayout( 1, 2 ) );
        final int gap = Constants.GAP_BETWEEN_GROUP;
        secondToolBar.setBorder( new CompoundBorder( new ThinBevelBorder( BevelDirection.RAISED ),
                new EmptyBorder( gap, gap, gap, gap ) ) );
        secondToolBar.add( speciesInterrogator );
        secondToolBar.add( familyInterrogator );
        return secondToolBar;
    }

    private JToolBar createTopToolbar()
    {
        final StopExamAction stopAction = new StopExamAction();
        final JButton stopButton = new JButton();
        stopButton.setAction( stopAction );
        stopButton.setToolTipText( Strings.getString( ExamMode.class, "EXAM.BUTTON.STOPP.HINT" ) );
        stopButton.setFocusPainted( false );
        final String prefix = Strings.getString( ExamMode.class, "BUTTON.NAVIGATION.PREFIX" );
        navigator = new IteratorControlPanel( prefix );
        navigator.setCursor( examStateModel );
        final JToolBar topToolBar = new JToolBar();
        topToolBar.setFloatable( false );
        topToolBar.setRollover( true );
        topToolBar.add( stopButton );
        topToolBar.add( ComponentFactory.createSeparator() );
        topToolBar.add( navigator.getPrevButton() );
        topToolBar.add( navigator.getNextButton() );
        topToolBar.setBorder( new ThinBevelBorder( BevelDirection.RAISED ) );
        return topToolBar;
    }

    private ResultModel createResultModel()
    {
        resultModel = new DefaultResultModel( herbarModel );
        MemorizingDetailResultModel detailModel;

        detailModel = new MemorizingDetailResultModel( MorText.class, MorAttribute.class, herbarModel );
        detailModel.add( filter1 );
        detailModel.add( filter2 );
        detailModel.add( filter3 );
        resultModel.add( detailModel );

        detailModel = new MemorizingDetailResultModel( EcoText.class, EcoAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter( new Class[]{EcoSubject.class, EcoAttribute.class, EcoValue.class, EcoText.class},
                new int[][]{{1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        resultModel.add( detailModel );

        detailModel = new MemorizingDetailResultModel( MedText.class, MedAttribute.class, herbarModel );
        detailModel.add( InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter( new Class[]{MedSubject.class, MedAttribute.class, MedValue.class, MedText.class},
                new int[][]{{1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) ) );
        resultModel.add( detailModel );
        return resultModel;
    }

    private void initFilter()
    {
        filter1 = InterrogatorComplexityFactory.getFilter( herbarModel, "Alle", VirtualGraphTreeNodeFilter.getFilter( new Class[]{MorSubject.class, MorAttribute.class, MorValue.class, MorText.class},
                new int[][]{{1, 1, 0, 2}, {1, 1, 0, 2}, {0, 1, 0, 2}, {1, 1, 0, 2}} ) );
        filter2 = InterrogatorComplexityFactory.getFilter( herbarModel, "Fernliegende", VirtualGraphTreeNodeFilter.getFilter( new Class[]{Taxon.class, MorText.class, MorValue.class, MorAttribute.class, MorSubject.class,
                MorAttribute.class, MorValue.class, MorText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 1, 1}, {1, 0, 0, 2},
                        {0, 0, 0, 2}, {1, 0, 0, 2}} ) );
        filter3 = InterrogatorComplexityFactory.getFilter( herbarModel, "Naheliegende", VirtualGraphTreeNodeFilter.getFilter( new Class[]{Taxon.class, MorText.class, MorValue.class, MorAttribute.class, MorSubject.class,
                MorAttribute.class, MorValue.class, MorText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 1, 2},
                        {0, 0, 0, 2}, {1, 0, 0, 2}} ) );
    }

    //==== Statistics

    private JPanel createStatisticsPanel()
    {
        final JPanel statisticsPanel = new JPanel( new GridLayout( 3, 1 ) );
        statisticsPanel.add( createLegendPanel() );
        statisticsPanel.add( createResultPanel() );
        statisticsPanel.add( createRestartPanel() );

        return statisticsPanel;
    }

    /**
     * Initializes result panel and creates a intro to it
     *
     * @return panel containing intro at the top and the result in the center
     */
    private JPanel createLegendPanel()
    {
        return createReadOnlyHTMLField( Strings.getString( ExamMode.class, "STATS.LEGEND.TEXT" ) );
    }

    private JPanel createResultPanel()
    {
        // init result panel
        final CompoundBorder border = new CompoundBorder( new EtchedBorder( EtchedBorder.LOWERED ), new EmptyBorder( 6, 6, 6, 6 ) );
        resultPanel = new JPanel( new GridBagLayout() );
        resultPanel.setBorder( border );

        final JPanel resultFloater = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        resultFloater.add( new JScrollPane( resultPanel ) );
        return resultFloater;
    }

    private JPanel createRestartPanel()
    {
        final JPanel restartPanel = new JPanel( new FlowLayout( FlowLayout.CENTER, 5, 5 ) );
        restartButton = new JButton( Strings.getString( ExamMode.class, "BUTTON.RESTART.TEXT" ) );
        restartButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                startExam();
            }
        } );
        restartPanel.add( restartButton );
        return restartPanel;
    }

    private void fillStatistics()
    {
        int y = -1;

        placeLabel( Strings.getString( ExamMode.class, "STATS.COUNT.PART.TEXT", speciesLevel.toString() ), 0, ++y );
        placeStatisticsValue( getCorrectTaxonAbundance( speciesInterrogatorModel ), 1, y );
        placeStatisticsValue( getCorrectTaxonAccuracy( speciesInterrogatorModel ), 2, y );

        placeLabel( Strings.getString( ExamMode.class, "STATS.COUNT.PART.TEXT", familyLevel.toString() ), 0, ++y );
        placeStatisticsValue( getCorrectTaxonAbundance( familyInterrogatorModel ), 1, y );
        placeStatisticsValue( getCorrectTaxonAccuracy( familyInterrogatorModel ), 2, y );

        for ( int i = 0; i < asker.getTabCount(); i++ )
        {
            final PropertyInterrogatorPanel panel = (PropertyInterrogatorPanel) asker.getComponentAt( i );
            final MemorizingDetailResultModel detailResultModel = (MemorizingDetailResultModel) panel.getResultModel();
            final String name = asker.getTitleAt( i );

            placeLabel( Strings.getString( ExamMode.class, "STATS.COUNT.PART.TEXT", name ), 0, ++y );
            placeStatisticsValue( getCorrectAttributeAbundance( detailResultModel ), 1, y );
            placeStatisticsValue( getCorrectAttributeAccuracy( detailResultModel ), 2, y );
        }
    }

    private float getCorrectAttributeAbundance( final MemorizingDetailResultModel model )
    {
        float iComplete = 0;
        int iTaxa = 0;
        final Iterator iterator = examStateModel.getIterator();
        while ( iterator.hasNext() )
        {
            final Taxon taxon = (Taxon) iterator.next();
            model.setTaxFocus( taxon );
            if ( model.isComplete() )
            {
                iComplete++;
            }
            iTaxa++;
        }
        return iComplete / iTaxa;
    }

    private float getCorrectAttributeAccuracy( final MemorizingDetailResultModel model )
    {
        float iGuesses = 0;
        int iCorrect = 0;
        final Iterator iterator = examStateModel.getIterator();
        while ( iterator.hasNext() )
        {
            final Taxon taxon = (Taxon) iterator.next();
            model.setTaxFocus( taxon );
            final GraphNodeList guesses = model.getGuesses();
            final GraphNodeList answers = model.getAnswers();
            iGuesses += guesses.size();
            iCorrect += ArrayUtils.intersect( guesses.getAll(), answers.getAll(), new GraphNodeList[0] ).length;
        }
        return ( iGuesses == 0 ? 0 : iCorrect / iGuesses );
    }

    /**
     * Returns the amount of taxa, for which a correct answer has been found, divided by the taxa searched for.
     *
     * @param model The model to be investigated
     * @return float between 0.0 (0%) and 1.0 (100%)
     */
    private float getCorrectTaxonAbundance( final TaxonNameInterrogator.MemorizingInterrogatorModel model )
    {
        float iTotal = 0;
        final Set taxa = model.getTaxa();
        for ( final Object aTaxa : taxa )
        {
            final Taxon taxon = (Taxon) aTaxa;
            final boolean isCorrect = model.getComplete( taxon ).isTrue();
            if ( isCorrect )
            {
                iTotal++;
            }
        }
        return iTotal / SIZE;
    }

    /**
     * Returns the amount of guesses divided by the amount of successful guesses.
     *
     * @param model The model to be investigated
     * @return float between 0.0 (0%) and 1.0 (100%)
     */
    private float getCorrectTaxonAccuracy( final TaxonNameInterrogator.MemorizingInterrogatorModel model )
    {
        int iTotal = 0;
        float iSuccessful = 0;
        final Set taxa = model.getTaxa();
        for ( final Object aTaxa : taxa )
        {
            final Taxon taxon = (Taxon) aTaxa;
            final List answers = model.getAnswers( taxon );
            iTotal += answers.size();
            final boolean isCorrect = model.getComplete( taxon ).isTrue();
            if ( isCorrect )
            {
                iSuccessful++;
            }
        }
        return ( iTotal == 0 ? 0 : iSuccessful / iTotal );
    }

    private void placeLabel( final String text, final int x, final int y )
    {
        final JLabel label = new JLabel( text );
        label.setHorizontalAlignment( SwingConstants.RIGHT );
        addToResult( x, y, label );
    }

    private void placeStatisticsValue( final float lStatistic, final int x, final int y )
    {
        final NumberFormat format = NumberFormat.getPercentInstance();
        final JLabel label = new JLabel( format.format( lStatistic ) );
        label.setHorizontalAlignment( SwingConstants.RIGHT );
        final Border border = new CompoundBorder( new EtchedBorder( EtchedBorder.LOWERED ), new EmptyBorder( 3, 3, 3, 3 ) );
        label.setBorder( border );
        label.setPreferredSize( new Dimension( 80, label.getPreferredSize().height ) );
        addToResult( x, y, label );
    }

    private void addToResult( final int x, final int y, final JLabel label )
    {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        resultPanel.add( label, gbc );
    }

    public void taxFocusChanged( final Taxon oldFocus, final Taxon newFocus )
    {
        picturePanel.setTaxon( newFocus );
        speciesInterrogator.setTaxFocus( newFocus );
        familyInterrogator.setTaxFocus( newFocus );
        asker.setTaxFocus( newFocus );
    }

    static
    {
        LogUtils.init();
        try
        {
            Strings.addResourceBundle( ExamMode.class, ResourceBundle.getBundle( ExamPanel.class.getName() ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        LOG = LoggerFactory.getLogger( ExamPanel.class );
    }

    class StopExamAction extends AbstractAction
    {
        private final String STOPP_TEXT = Strings.getString( ExamMode.class, "EXAM.BUTTON.STOPP.TEXT" );

        private final String STOPP_ICON = Strings.getString( ExamMode.class, "EXAM.BUTTON.STOPP.ICON" );

        public StopExamAction()
        {
            putValue( AbstractAction.NAME, STOPP_TEXT );
            putValue( AbstractAction.SMALL_ICON, ImageLocator.getIcon( STOPP_ICON ) );
        }

        public void actionPerformed( final ActionEvent e )
        {
            finishExam();
        }
    }
}
