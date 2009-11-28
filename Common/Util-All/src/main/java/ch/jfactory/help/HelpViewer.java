package ch.jfactory.help;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2007/09/27 10:41:22 $
 */
public class HelpViewer
{
    private static final String OPTION_SHORT_HELPSET = "h";

    private static final String OPTION_LONG_HELPSET = "helpset";

    public HelpViewer( final String helpset )
    {
        final JFrame f = new JFrame();
        f.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( final WindowEvent e )
            {
                System.exit( 0 );
            }
        } );

        final JButton button = new JButton( "Beenden" );
        button.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                System.exit( 0 );
            }
        } );
        f.getContentPane().setLayout( new FlowLayout() );
        f.getContentPane().add( button );
        f.pack();
        f.setVisible( true );
        init( f.getRootPane(), helpset );
    }

    public HelpViewer( final Component component, final String helpset )
    {
        init( component, helpset );
    }

    private void init( final Component component, final String helpset )
    {
        HelpSet hs = null;
        try
        {
            URL hsURL = HelpViewer.class.getResource( helpset );
            if ( hsURL == null && helpset.startsWith( "/" ) )
            {
                hsURL = HelpViewer.class.getResource( helpset.substring( 1 ) );
            }
            else if ( hsURL == null && !helpset.startsWith( File.separator ) )
            {
                hsURL = HelpViewer.class.getResource( "/" + helpset );
            }
            hs = new HelpSet( null, hsURL );
        }
        catch ( Exception e )
        {
            if ( e.getMessage() != null )
            {
                System.err.println( e.getMessage() );
            }
            System.err.println( "HelpSet " + helpset + " not found" );
            System.err.println( "Basedir is: " + System.getProperty( "user.dir" ) );
            return;
        }
        final HelpBroker helpBroker = hs.createHelpBroker();
        helpBroker.setLocale( Locale.GERMAN );
        helpBroker.setFont( UIManager.getFont( "Label.font" ) );

        final CSH.DisplayHelpFromSource displayer = new CSH.DisplayHelpFromSource( helpBroker );
        displayer.actionPerformed( new ActionEvent( component, 0, "SHOWHELP" ) );
    }

    public static void main( final String[] args )
    {
        final Options options = new Options();
        options.addOption( OPTION_SHORT_HELPSET, OPTION_LONG_HELPSET, true, "Helpset to open" );
        final Parser parser = new PosixParser();
        try
        {
            final CommandLine cl = parser.parse( options, args );
            final String helpset = cl.getOptionValue( OPTION_SHORT_HELPSET );
            new HelpViewer( helpset );
        }
        catch ( ParseException e )
        {
            System.err.println( "Error parsing commandline " + args );
        }
    }
}
