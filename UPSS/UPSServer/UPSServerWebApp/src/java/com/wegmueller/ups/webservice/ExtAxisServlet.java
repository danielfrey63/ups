package com.wegmueller.ups.webservice;

import org.apache.axis.transport.http.AxisServlet;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import javax.servlet.ServletException;

import com.wegmueller.ups.config.Configuration;

/**
 * Extension to the default axis servlet to setup logging
 */
public class ExtAxisServlet extends AxisServlet {

    public void init() throws ServletException {
        //BasicConfigurator.configure();

        //Logger.getRootLogger().setLevel(Level.DEBUG);
        //Configuration.configureForTomcat(getServletContext());
        super.init();

    }
}
