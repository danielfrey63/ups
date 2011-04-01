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
package com.ethz.geobot.herbar.game.maze;

import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.tree.GraphTreeNode;
import ch.jfactory.component.tree.TreeExpander;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.PictureConverter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.game.util.Question;
import com.ethz.geobot.herbar.gui.VirtualGraphTreeFactory;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.trait.MorphologyAttribute;
import com.ethz.geobot.herbar.model.trait.MorphologySubject;
import com.ethz.geobot.herbar.model.trait.MorphologyText;
import com.ethz.geobot.herbar.model.trait.MorphologyValue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * questionpanel modal.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class QuestionPanel extends JDialog implements Question
{
    private static final Logger LOG = LoggerFactory.getLogger( QuestionPanel.class );

    private final JLabel taxLabel;

    private final HerbarModel model;

    private final JLabel pict;

    private final PictureTheme[] theme;

    private final JButton okButton;

    private final JPanel okPanel;

    protected Taxon actualTaxon;

    protected JTree tree;

    protected CountScore countScore;

    protected QuestionModel questionModel;

    private final VirtualGraphTreeNodeFilter treeFilter;

    private final TreeExpander expander;

    /**
     * Constructor
     *
     * @param model     instance of the herbarmodel
     * @param countScor instance of CountScore
     */
    public QuestionPanel( final Frame frame, final HerbarModel model, final CountScore countScor )
    {
        super( frame );

        this.countScore = countScor;
        this.model = model;

        questionModel = new QuestionModel( countScore, this.model );

        final Border borderEmpty = BorderFactory.createEmptyBorder( 6, 6, 6, 6 );
        final Border borderLine = BorderFactory.createLineBorder( new Color( 170, 180, 180 ), 1 );
        final CompoundBorder cb = new CompoundBorder( borderEmpty, borderLine );

        // top
        final JTextPane frage = new JTextPane();
        final Font font = UIManager.getFont( "Label.font" );
        frage.setContentType( "text/html" );
        frage.setText( Strings.getString( MazePanel.class, "MAZE.FRAGE", font.getName() ) );
        frage.setBackground( UIManager.getColor( "Panel.background" ) );
        frage.setEditable( false );
        final SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setFontSize( attributes, font.getSize() );
        StyleConstants.setAlignment( attributes, StyleConstants.ALIGN_CENTER );
        final StyledDocument doc = frage.getStyledDocument();
        doc.setParagraphAttributes( 0, doc.getLength(), attributes, false );

        taxLabel = new JLabel( " " );
        taxLabel.setHorizontalAlignment( JLabel.CENTER );

        final JPanel frageAntwort = new JPanel( new BorderLayout() );
        frageAntwort.setBorder( cb );
        frageAntwort.add( frage, BorderLayout.CENTER );
        frageAntwort.add( taxLabel, BorderLayout.SOUTH );

        // mid
        pict = new JLabel( " " );
        pict.setHorizontalAlignment( JLabel.CENTER );
        pict.setPreferredSize( new Dimension( 370, 370 ) );
        pict.setBorder( cb );

        theme = this.model.getPictureThemes();

        // bottom
        treeFilter = VirtualGraphTreeNodeFilter.getFilter( new Class[]{Taxon.class, MorphologyText.class, MorphologyValue.class, MorphologyAttribute.class, MorphologySubject.class,
                MorphologyAttribute.class, MorphologyValue.class, MorphologyText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 1, 1},
                        {0, 0, 1, 2}, {0, 0, 0, 2}, {1, 0, 0, 2}} );
        tree = VirtualGraphTreeFactory.getVirtualTree( model.getRootTaxon().getAsGraphNode(), treeFilter );
        tree.setShowsRootHandles( true );
        expander = new TreeExpander( tree, 2 );

        final JScrollPane treePane = new JScrollPane( tree );
        treePane.setPreferredSize( new Dimension( 200, 200 ) );

        okButton = createOkButton();
        getRootPane().setDefaultButton( okButton );

        okPanel = new JPanel( new FlowLayout() );
        okPanel.add( okButton, null );
        okPanel.setBorder( borderEmpty );

        final JPanel lowerPanel = new JPanel();
        lowerPanel.setBorder( cb );
        lowerPanel.setLayout( new BorderLayout() );
        lowerPanel.add( treePane, BorderLayout.CENTER );
        lowerPanel.add( okPanel, BorderLayout.SOUTH );

        // build
        final Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );
        contentPane.add( frageAntwort, BorderLayout.NORTH );
        contentPane.add( new JScrollPane( pict ), BorderLayout.CENTER );
        contentPane.add( lowerPanel, BorderLayout.SOUTH );

        tree.requestFocus();
        setSize( 500, 800 );

        tree.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                final TreePath path = tree.getSelectionPath();
                if ( path != null )
                {
                    final TreeNode node = (TreeNode) path.getLastPathComponent();
                    okButton.setEnabled( node.isLeaf() );
                }
                else
                {
                    okButton.setEnabled( false );
                }
            }
        } );
    }

    private JButton createOkButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                // Double check whether all selected values are correct
                // and that there are selected values at all
                final TreePath selectionPath = tree.getSelectionPath();
                if ( selectionPath != null )
                {
                    final GraphTreeNode lastPathComponent = (GraphTreeNode) selectionPath.getLastPathComponent();
                    final GraphNode morText = lastPathComponent.getDependent();
                    final GraphNodeList correctTexts = actualTaxon.getAsGraphNode().getChildren( MorphologyText.class );
                    if ( correctTexts.contains( morText ) )
                    {
                        countScore.addRightScore( 1 );
                        questionModel.setRightAnswers( actualTaxon );
                    }
                    else
                    {
                        countScore.addWrongScore( 1 );
                        questionModel.setWrongAnswers( actualTaxon );
                        // collect attributes of the guess text
                        final GraphNodeList values = morText.getParents( MorphologyValue.class );
                        final GraphNodeList attributes = new GraphNodeList();
                        for ( int i = 0; i < values.size(); i++ )
                        {
                            attributes.addAll( values.get( i ).getParents( MorphologyAttribute.class ) );
                        }
                        // collect correct text for attributes of the text guess
                        final GraphNode attribute = attributes.get( 0 );
                        final GraphNodeList allValues = attribute.getChildren( MorphologyValue.class );
                        GraphNode correctText = null;
                        for ( int i = 0; correctText == null && i < allValues.size(); i++ )
                        {
                            final GraphNodeList allTexts = allValues.get( i ).getChildren( MorphologyText.class );
                            for ( int j = 0; correctText == null && j < allTexts.size(); j++ )
                            {
                                if ( correctTexts.contains( allTexts.get( j ) ) )
                                {
                                    correctText = allTexts.get( j );
                                }
                            }
                        }
                        String prompt = ( Strings.getString( MazePanel.class, "MAZE.OPTION.CORRECT" ) );
                        prompt += " " + correctText;
                        final String title = Strings.getString( MazePanel.class, "MAZE.OPTION.TITLE" );
                        final String[] button = new String[]{Strings.getString( "BUTTON.OK.TEXT" )};
                        final int m = JOptionPane.INFORMATION_MESSAGE;
                        JOptionPane.showOptionDialog( QuestionPanel.this, prompt, title, 0, m, null, button, button[0] );
                    }
                    tree.clearSelection();
                }
                dispose();
            }
        };
        final JButton button = ComponentFactory.createButton( MazePanel.class, "MAZE.OK", action );
        button.setFocusable( true );
        button.setEnabled( false );
        return button;
    }

    /** initializes the data for the questionpool */
    public void init()
    {
        questionModel.init();
        setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        final WindowListener[] listeners = this.getWindowListeners();
        for ( final WindowListener listener : listeners )
        {
            this.removeWindowListener( listener );
        }
    }

    /** @see Question#firstQuestion() */
    public void firstQuestion()
    {
    }

    /** @see Question#lastQuestion() */
    public void lastQuestion()
    {
    }

    /** @see Question#nextQuestion() */
    public void nextQuestion()
    {
        actualTaxon = questionModel.getTaxon();
        VirtualGraphTreeFactory.updateModel( tree, treeFilter, actualTaxon.getAsGraphNode() );
        ImageIcon plantPict = null;
        taxLabel.setText( actualTaxon.getName() );
        final List pictureNames = new ArrayList();
        for ( final PictureTheme aTheme : theme )
        {
            if ( aTheme.getName().equals( "Herbar" ) | aTheme.getName().equals( "Portrait" ) )
            {
                final CommentedPicture[] picts = actualTaxon.getCommentedPictures( aTheme );
                if ( picts != null )
                {
                    for ( final CommentedPicture pict1 : picts )
                    {
                        pictureNames.add( pict1.getPicture().getName() );
                    }
                }
            }
        }
        // Workaround as long as not all taxa have traits
        if ( ( (TreeNode) tree.getModel().getRoot() ).getChildCount() == 0 )
        {
            okButton.setEnabled( true );
        }
        if ( pictureNames.size() > 0 )
        {
            final int randPict = (int) ( Math.random() * pictureNames.size() );
            final String name = ( (String) pictureNames.get( randPict ) );
            plantPict = ImageLocator.getPicture( name );
            final Dimension plantPictDim = new Dimension( plantPict.getIconWidth(), plantPict.getIconHeight() );
            final Dimension shouldDim = new Dimension( 370, 370 );
            final Dimension newDim = PictureConverter.getFittingDimension( shouldDim, plantPictDim );
            final Image plantImage = plantPict.getImage().getScaledInstance( newDim.width, newDim.height, Image.SCALE_SMOOTH );
            tree.clearSelection();
            pict.removeAll();
            pict.setIcon( new ImageIcon( plantImage ) );
            pict.setText( "" );
            repaint();
        }
        // Workaround as long as not all taxa have pictures
        else
        {
            final String text = "Picture not available for " + actualTaxon;
            pict.setIcon( null );
            pict.setText( text );
            LOG.error( text );
        }
        getRootPane().setDefaultButton( okButton );
    }

}
