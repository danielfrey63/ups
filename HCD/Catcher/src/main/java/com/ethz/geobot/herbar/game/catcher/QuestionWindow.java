/*
 * Herbar CD-ROM version 2
 *
 * QuestionWindow.java
 *
 * Created on 9. Mai 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.Dialogs;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.PictureConverter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.Question;
import com.ethz.geobot.herbar.gui.CorrectnessChecker;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Window selects and shows the question of this game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class QuestionWindow extends JDialog implements Question
{
    private static final Logger LOG = LoggerFactory.getLogger( Catcher.class );

    private ImageIcon plantPict;

    private final JLabel pict = new JLabel();

    private JButton go;

    private final JTextField answer = new JTextField();

    private Taxon tax;

    private Taxon ancestorTax;

    private PictureTheme[] theme;

    private final JLabel frage = new JLabel();

    private Frame parentFrame;

    private HerbarModel model;

    private CorrectnessChecker.Correctness correctness;

    private final CorrectnessChecker correctnessChecker = new CorrectnessChecker();

    /**
     * Method QuestionWindow. Constructor
     *
     * @param model instance of the herbarmodel
     */
    public QuestionWindow( final Frame frame, final HerbarModel model )
    {
        super( frame );
        try
        {
            this.model = model;
            parentFrame = frame;
            init();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * prepares the optionpane to show the correct answer
     *
     * @param unique correct taxon
     * @param text   The new correctOptionPane value
     */
    private void setCorrectOptionPane( final String text, final Taxon unique )
    {
        Dialogs.showInfoMessage( this.getRootPane(), "Falsche Antwort", text + " " + unique.getName() );
    }

    /**
     * @see Question#firstQuestion()
     */
    public void firstQuestion()
    {
    }

    /**
     * @see Question#lastQuestion()
     */
    public void lastQuestion()
    {
    }

    /**
     * demand a questionObject of the datapool to generate the next question.
     */
    public CorrectnessChecker.Correctness showQuestion( final QuestionDataUnit questionDataUnit )
    {
        tax = questionDataUnit.getTaxon();
        ancestorTax = questionDataUnit.getParentTaxon();
        // randomly select a pict of all picts of the selected taxon
        final Vector pictureNames = new Vector();
        pictureNames.removeAllElements();
        for ( final PictureTheme aTheme : theme )
        {
            if ( aTheme.getName().equals( "Herbar" ) | aTheme.getName().equals( "Portrait" ) )
            {
                final CommentedPicture[] picts = tax.getCommentedPictures( aTheme );
                if ( picts != null )
                {
                    for ( final CommentedPicture pict1 : picts )
                    {
                        pictureNames.add( pict1.getPicture().getName() );
                    }
                }
            }
        }
        final int randPict = (int) ( Math.random() * pictureNames.size() );
        if ( pictureNames.size() < 1 )
        {
            answer.setText( "kein Bild vorhanden" );
            //return showQuestion(questionDataUnit);
        }
        else
        {
            final String name = ( (String) pictureNames.elementAt( randPict ) );
            plantPict = ImageLocator.getPicture( name );
            final Dimension plantPictDim = new Dimension( plantPict.getIconWidth(), plantPict.getIconHeight() );
            final Dimension shouldDim = new Dimension( 370, 370 );
            final Dimension newDim = PictureConverter.getFittingDimension( shouldDim, plantPictDim );
            final Image plantImage = plantPict.getImage().getScaledInstance( newDim.width, newDim.height, Image.SCALE_SMOOTH );
            answer.setEditable( true );
            answer.setText( "" );
            pict.setIcon( new ImageIcon( plantImage ) );
            this.setSize( 500, 550 );
            this.repaint();
            getRootPane().setDefaultButton( go );
            frage.setText( Strings.getString( Catcher.class, "CATCHER.FRAGE", ancestorTax.getLevel().getName() ) );
            LOG.debug( "Taxon: " + tax.getName() + ", AncestorTaxon: " + ancestorTax.getName() );
        }
        setVisible( true );
        return correctness;
    }

    public void nextQuestion()
    {
    }

    protected void processWindowEvent( final WindowEvent e )
    {
        if ( e.getID() == WindowEvent.WINDOW_CLOSING )
        {
        }
        super.processWindowEvent( e );
    }

    /**
     * initialisation of jwindow and components, containing the answer validation
     */
    private void init()
    {
        setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        final WindowListener[] listeners = this.getWindowListeners();
        for ( final WindowListener listener : listeners )
        {
            this.removeWindowListener( listener );
        }

        theme = model.getPictureThemes();
        final Border borderEmpty = BorderFactory.createEmptyBorder( 6, 6, 6, 6 );
        final Border borderLine = BorderFactory.createLineBorder( new Color( 170, 180, 180 ), 1 );
        final CompoundBorder cb = new CompoundBorder( borderEmpty, borderLine );
        go = createButtonGo();
        setModal( true );
        setSize( 500, 550 );
        WindowUtils.centerOnComponent( this, parentFrame );

        answer.getKeymap().removeKeyStrokeBinding( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) );
        final JPanel frageAntwort = new JPanel( new BorderLayout() );
        final JPanel answerFlow = new JPanel( new FlowLayout() );
        answerFlow.add( answer, null );

        answer.setPreferredSize( new Dimension( 200, 20 ) );

        frage.setFont( UIManager.getFont( "Menu.font" ) );
        answer.setHorizontalAlignment( JLabel.CENTER );
        frage.setHorizontalAlignment( JLabel.CENTER );
        pict.setHorizontalAlignment( JLabel.CENTER );

        frageAntwort.add( frage, BorderLayout.NORTH );
        frageAntwort.add( answerFlow, BorderLayout.CENTER );

        final JPanel goFlow = new JPanel( new FlowLayout() );
        goFlow.add( go, null );
        frageAntwort.setBorder( cb );
        pict.setBorder( cb );
        goFlow.setBorder( cb );

        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( frageAntwort, BorderLayout.NORTH );
        getContentPane().add( pict, BorderLayout.CENTER );
        getContentPane().add( goFlow, BorderLayout.SOUTH );

        answer.setFocusable( true );
    }

    private JButton createButtonGo()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                correctness = correctnessChecker.getCorrectness( ancestorTax, answer.getText() );
                if ( correctness == CorrectnessChecker.IS_NEARLY_TRUE )
                {
                    setCorrectOptionPane( Strings.getString( Catcher.class, "CATCHER.CORRECT.DISTANCE", "" +
                            correctness ), ancestorTax );
                }
                else if ( correctness == CorrectnessChecker.IS_FALSE )
                {
                    setCorrectOptionPane( Strings.getString( Catcher.class, "CATCHER.CORRECT.RIGHT" ), ancestorTax );
                }
                setVisible( false );
            }
        };
        final JButton button = ComponentFactory.createButton( Catcher.class, "CATCHER.GO", action );
        button.setFocusable( true );
        return button;
    }
}
