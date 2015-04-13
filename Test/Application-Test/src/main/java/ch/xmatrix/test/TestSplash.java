package ch.xmatrix.test;

import java.awt.AWTException;
import java.awt.Dimension;
import static java.awt.Frame.ICONIFIED;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TestSplash extends JPanel
{

    private static TheFocusListener listener;
    private final BufferedImage image;

    public static void main( String[] args ) throws Exception
    {
        final JFrame frame = new JFrame();
        final JPanel panel = new TestSplash();
        frame.add( panel );
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.setUndecorated( true );
        frame.setSize( 400, 600 );
        frame.setLocationRelativeTo( null );
        listener = new TheFocusListener( frame );
        panel.addFocusListener( listener );
        frame.setVisible( true );
        panel.requestFocus();
    }

    public TestSplash() throws IOException
    {
        setLayout( null );
        image = ImageIO.read( getClass().getResourceAsStream( "splash.png" ) );
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        final Point location = getLocationOnScreen();
        g.drawImage( listener.getBackground(), -location.x, -location.y, null );
        g.drawImage( image, 0, 0, null );
    }
}

class TheFocusListener implements FocusListener
{
    private final JFrame frame;
    private final Robot robot;
    private BufferedImage background;

    TheFocusListener( final JFrame frame ) throws AWTException
    {
        this.frame = frame;
        robot = new Robot();
        captureScreen();
    }

    public BufferedImage getBackground()
    {
        return background;
    }

    private void captureScreen()
    {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        background = robot.createScreenCapture( new Rectangle( 0, 0, screenSize.width, screenSize.height ) );
    }

    @Override
    public void focusGained( FocusEvent e )
    {
        frame.setSize( 0, 0 );
        captureScreen();
        System.out.println( "focusGained" );
        frame.setSize( 600, 400 );
    }

    @Override
    public void focusLost( FocusEvent e )
    {
        System.out.println( "focusLost" );
        frame.setState( ICONIFIED );
    }
}