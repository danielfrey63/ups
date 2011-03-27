/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.animation;

import abbot.finder.ComponentNotFoundException;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.ClassMatcher;
import abbot.tester.ComponentTester;
import ch.jfactory.animation.fading.FadingPaintable;
import ch.jfactory.animation.scrolltext.ScrollingTextPaintable;
import ch.jfactory.component.splash.SimpleSplash;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import junit.extensions.abbot.ComponentTestFixture;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the splash screen in different compositions with animated text.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class AnimationTest extends ComponentTestFixture
{
    /** The splash image to display. */
    private static final String SPLASH = "/splash.jpg";

    /** Test series parameter for page modes. */
    private static final boolean[] pageModes = {false, true};

    /** Test series for paragraph delays. */
    private static final int[] paragraphDelays = {10, 1000};

    /**
     * Constructs a new test.
     *
     * @param name the animation test
     */
    public AnimationTest( final String name )
    {
        super( name );
    }

    /**
     * Return this test suite.
     *
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AnimationTest.class );
    }

    public void testNullIconSplash() throws Exception
    {
        final SimpleSplash splash = new SimpleSplash( null );
        splash.setSize( new Dimension( 300, 200 ) );
        assertNotNull( splash );

        final JLabel label = (JLabel) getFinder().find( new ClassMatcher( JLabel.class ) );
        assertEquals( "-= Null-ImageIcon defined =-", label.getText() );
        splash.stop();
        assertFalse( splash.isVisible() );
    }

    public void testInvalidIconSplash() throws ComponentNotFoundException, MultipleComponentsFoundException
    {
        final ImageIcon imageIcon = new ImageIcon( "DoesNotExist.jpg" );
        final SimpleSplash splash = new SimpleSplash( imageIcon );
        splash.start();
        assertTrue( splash.isVisible() );

        final SimpleSplash foundSplash = (SimpleSplash) getFinder().find( new ClassMatcher( SimpleSplash.class ) );
        assertSame( splash, foundSplash );

        final JLabel label = (JLabel) getFinder().find( new ClassMatcher( JLabel.class ) );
        assertEquals( "-= ImageIcon \"DoesNotExist.jpg\" not found =-", label.getText() );

        splash.stop();
        assertFalse( splash.isVisible() );
    }

    public void testSplashWithoutForeground() throws ComponentNotFoundException, MultipleComponentsFoundException
    {
        final URL url = AnimationTest.class.getResource( SPLASH );
        final ImageIcon imageIcon = new ImageIcon( url );

        final SimpleSplash splash = new SimpleSplash( imageIcon );
        splash.start();
        assertTrue( splash.isVisible() );

        final SimpleSplash foundSplash = (SimpleSplash) getFinder().find( new ClassMatcher( SimpleSplash.class ) );
        assertSame( splash, foundSplash );

        splash.stop();
        assertFalse( splash.isVisible() );
    }

    public void testFadingWithoutBackgroundImage() throws ComponentNotFoundException, MultipleComponentsFoundException
    {
        final JFrame frame = new JFrame( "AnimationQueue Test" );
        final Dimension size = new Dimension( 300, 200 );

        // Make sure that the frame is disposed and tests can go on when animation is finished
        final FadingPaintable paintable = new FadingPaintable( Color.ORANGE );
        final Thread sleeper = Thread.currentThread();
        paintable.addStopListener( new StopListener()
        {
            public void stopPerformed()
            {
                frame.dispose();
                sleeper.interrupt();
            }
        } );

        final AnimationQueue animation = new AnimationQueue();
        animation.setSize( size );
        animation.addPaintable( paintable );

        frame.getContentPane().setLayout( null );
        frame.getContentPane().add( animation );
        showWindow( frame, size );

        final JFrame foundSplash = (JFrame) getFinder().find( new ClassMatcher( JFrame.class ) );
        assertSame( frame, foundSplash );

        sleep( 10 );

        // before 10 seconds the animation should be terminated and the window invisible
        assertFalse( foundSplash.isVisible() );
    }

    public void testSimpleSplashWithFaderAndScroller()
    {
        // The background image
        final URL url = AnimationTest.class.getResource( SPLASH );
        final ImageIcon imageIcon = new ImageIcon( url );

        // The scrolling text
        final AnimationQueue animation = new AnimationQueue();
        animation.setBounds( 100, 68, 200, 167 );

        final Insets insets = new Insets( 0, 10, 0, 10 );
        animation.setInsets( insets );

        final Color fadeColor = new Color( 255, 255, 255, 150 );
        final int printSpaceWidth = animation.getSize().width - insets.left - insets.right;

        final String fileName = "/News.txt";
        final InputStream textFile = AnimationTest.class.getResourceAsStream( fileName );
        assertNotNull( "Text file " + fileName + " not found.", textFile );

        final ScrollingTextPaintable scroller = new ScrollingTextPaintable( textFile, printSpaceWidth, true );
        scroller.setBackgroundColor( fadeColor );
        scroller.setScrollDelay( 5 );
        animation.addPaintable( scroller );

        // The splash
        final SimpleSplash s = new SimpleSplash( imageIcon, animation );
        s.start();
        sleep( 1 );
        s.stop();
    }

    public void testSplashWithScroller() throws ComponentNotFoundException, MultipleComponentsFoundException
    {
        for ( final boolean b : pageModes )
        {
            for ( final int d : paragraphDelays )
            {
                doSplashWithForeground( b, d );
            }
        }
    }

    private void doSplashWithForeground( final boolean pageModus, final int paragraphDelay )
    {
        //throws ComponentNotFoundException, MultipleComponentsFoundException {
        // The background image
        final URL url = AnimationTest.class.getResource( SPLASH );
        final ImageIcon imageIcon = new ImageIcon( url );

        // The scrolling text
        final AnimationQueue animation = new AnimationQueue();
        animation.setBounds( 100, 68, 200, 167 );

        final Insets insets = new Insets( 0, 10, 0, 10 );
        animation.setInsets( insets );

        final String fileName = "/News.txt";
        final InputStream textFile = AnimationTest.class.getResourceAsStream( fileName );
        assertNotNull( "Textfile " + fileName + " not found.", textFile );

        final int printSpaceWidth = animation.getSize().width - insets.left - insets.right;
        final ScrollingTextPaintable scroller = new ScrollingTextPaintable( textFile, printSpaceWidth, true );
        final Color color = new Color( 255, 255, 255, 150 );
        scroller.setBackgroundColor( color );
        scroller.setScrollDelay( 5 );
        scroller.setPageModus( pageModus );
        scroller.setParagraphDelay( paragraphDelay );

        animation.addPaintable( scroller );

        // The splash
        final SimpleSplash splash = new SimpleSplash( imageIcon, animation );
        splash.start();
        assertTrue( splash.isVisible() );

        //final SimpleSplash foundSplash = (SimpleSplash) getFinder().find(new ClassMatcher(SimpleSplash.class));
        //assertSame(splash, foundSplash);
        //final AnimationQueue foundAnimation = (AnimationQueue) getFinder().find(new ClassMatcher(AnimationQueue.class));
        //assertSame(animation, foundAnimation);
        //final JLabel label = (JLabel) getFinder().find(new ClassMatcher(JLabel.class));
        //assertSame(imageIcon, label.getIcon());
        sleep( 1 );
        splash.stop();
        assertFalse( splash.isVisible() );
    }

    /**
     * Tests whether a click does deliver the next page in the scrolling text component. <p/> Todo: Disabled at the moment as of a class path problem.
     *
     * @throws ComponentNotFoundException
     * @throws MultipleComponentsFoundException
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public void doTestSplashScrollerMouseClick() throws ComponentNotFoundException, MultipleComponentsFoundException, IOException, URISyntaxException
    {
        final JDialog dialog = new JDialog();
        dialog.setSize( 300, 200 );

        final InputStream textFile = AnimationTest.class.getResourceAsStream( "/News.txt" );
        final ScrollingTextPaintable scroller = new ScrollingTextPaintable( textFile, dialog.getWidth(), true );
        scroller.setBackgroundColor( Color.orange );
        scroller.setPageModus( true );
        scroller.setScrollDelay( 3 );
        scroller.setParagraphDelay( 10000 );

        final AnimationQueue animation = new AnimationQueue();
        animation.addPaintable( scroller );
        dialog.getContentPane().add( animation );

        dialog.setVisible( true );
        assertTrue( dialog.isVisible() );

        //final JDialog foundDialog = (JDialog) getFinder().find(new ClassMatcher(JDialog.class));
        //assertSame(dialog, foundDialog);
        final ComponentTester tester = new ComponentTester();
        tester.delay( 1000 );
        tester.click( dialog, 100, 100 );
        tester.delay( 1000 );
        saveImage( dialog, "tmp.png", "png" );

        final File tempImageFile = new File( "tmp.png" );
        final URL correctImageURL = AnimationTest.class.getResource( "/afterClick.png" );
        System.out.println( correctImageURL );

        final URI uri = new URI( correctImageURL.toString() );
        System.out.println( uri );

        final File correctImageFile = new File( uri );
        assertTrue( areFilesEqual( tempImageFile, correctImageFile ) );

        dialog.dispose();
    }

    /**
     * To build tests which compare images before and afterwards.
     *
     * @param component the component to capture an image of
     * @param fileName  the file name to store it to
     * @param format
     * @throws IOException when something gets wrong during io
     */
    private void saveImage( final Component component, final String fileName, final String format ) throws IOException
    {
        final BufferedImage image = new ComponentTester().capture( component );
        ImageIO.write( image, format, new File( fileName ) );
    }

    private void sleep( final int sleepTimeInSeconds )
    {
        try
        {
            Thread.sleep( 1000 * sleepTimeInSeconds );
        }
        catch ( InterruptedException e )
        {
        }
    }

    /**
     * Compares two binary files.
     *
     * @param f1 first file to compare
     * @param f2 second file to compare
     * @return whether the files are identical in contents
     * @throws IOException if a problem arises during file read io
     */
    private boolean areFilesEqual( final File f1, final File f2 ) throws IOException
    {
        // compare file sizes
        if ( f1.length() != f2.length() )
        {
            return false;
        }

        // read and compare bytes pair-wise
        final InputStream i1 = new FileInputStream( f1 );
        final InputStream i2 = new FileInputStream( f2 );
        int b1;
        int b2;

        do
        {
            b1 = i1.read();
            b2 = i2.read();
        }
        while ( ( b1 == b2 ) && ( b1 != -1 ) );

        i1.close();
        i2.close();

        // true only if end of file is reached
        return b1 == -1;
    }
}
