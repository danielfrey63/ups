package ch.xmatrix.test;

import java.util.logging.Logger;

/** @author Daniel Frey 30.04.2010 17:25:06 */
public class JNLPTest
{
    /** This class logger. */
    private static final Logger LOG = Logger.getLogger( JNLPTest.class.getName() );

    public static void main( String[] args )
    {
        System.out.println( System.getProperties() );
        LOG.info( "system properties are: " + System.getProperties() );
        System.setProperties( System.getProperties() );
        System.out.println( System.getProperties() );
        LOG.info( "system properties are: " + System.getProperties() );
    }
}
