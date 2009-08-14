package com.wegmueller.ups.webservice;

import javax.servlet.ServletException;
import org.apache.axis.transport.http.AxisServlet;

/** Extension to the default axis servlet to setup logging */
public class ExtAxisServlet extends AxisServlet
{

    public void init() throws ServletException
    {
        //BasicConfigurator.configure();

        //Logger.getRootLogger().setLevel(Level.DEBUG);
        //Configuration.configureForTomcat(getServletContext());
        super.init();

    }
}
