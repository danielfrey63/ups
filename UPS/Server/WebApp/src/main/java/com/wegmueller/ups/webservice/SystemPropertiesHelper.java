package com.wegmueller.ups.webservice;

import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class SystemPropertiesHelper implements javax.servlet.ServletContextListener
{

    public void contextInitialized( final ServletContextEvent event )
    {
        final ServletContext context = event.getServletContext();
        final Enumeration<String> parameters = context.getInitParameterNames();

        while ( parameters.hasMoreElements() )
        {
            final String parameter = parameters.nextElement();
            final String value = context.getInitParameter( parameter );
            System.setProperty( parameter, value );
        }
    }

    public void contextDestroyed( final ServletContextEvent event )
    {
    }
}
