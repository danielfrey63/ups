package ch.xmatrix.test.osgi.provider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 26.12.10 03:58
 */
public class OSGiVersionActivator implements BundleActivator
{
    public void start( final BundleContext context ) throws Exception
    {
        context.registerService( OSGiVersionInterface.class.getName(), new OSGiVersion(), null );
        System.out.println( "service registered" );
    }

    public void stop( final BundleContext context ) throws Exception
    {
    }
}
