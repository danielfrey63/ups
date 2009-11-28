/*
 * Herbar CD-ROM version 2
 *
 * Application.java
 *
 * Created on 7. März 2002 17:42:20
 */

package com.ethz.geobot.herbar;

import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.filter.FilterFactory;
import com.ethz.geobot.herbar.filter.FilterPersistentException;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Application class, defines all application level attributes. Implements the <b>Singleton</b> pattern.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class Application
{
    private static final String ROOT_NAME;

    private static final Logger LOG;

    private static final Application INSTANCE;

    private static final HerbarModel ROOT_MODEL;

    private HerbarModel model;

    public static Application getInstance()
    {
        return INSTANCE;
    }

    /**
     * return the Herbar-data-model.
     *
     * @return the model
     */
    public HerbarModel getModel()
    {
        if ( model == null )
        {
            // access data
            final String implClass = System.getProperty( "herbar.model.impl" );
            LOG.debug( "init herbar model by classname " + implClass );
            try
            {
                final Class<?> clazz = Class.forName( implClass );
                model = (HerbarModel) clazz.newInstance();
                model.setReadOnly();
            }
            catch ( Exception ex )
            {
                final String message = "could not initiate model: " + implClass;
                LOG.fatal( message, ex );
                throw new ApplicationRuntimeException( message );
            }
        }
        return model;
    }

    public HerbarModel getModel( final String name ) throws FilterPersistentException
    {
        if ( ROOT_NAME.equals( name ) )
        {
            return getModel();
        }
        else
        {
            return FilterFactory.getInstance().getFilterModel( name );
        }
    }

    public void saveModel( final HerbarModel model ) throws FilterPersistentException
    {
        if ( model instanceof FilterModel )
        {
            FilterFactory.getInstance().saveFilterModel( (FilterModel) model );
        }
        else
        {
            LOG.warn( "User want to save a model different tham a FilterModel class:" + model.getClass() );
        }
    }

    public void removeModel( final HerbarModel model ) throws FilterPersistentException
    {
        if ( model instanceof FilterModel )
        {
            FilterFactory.getInstance().removeFilterModel( (FilterModel) model );
        }
        else
        {
            LOG.warn( "User want to save a model different tham a FilterModel class:" + model.getClass() );
        }
    }

    /**
     * This method returns a set containing all models which are available
     *
     * @return a set of model names (String)
     */
    public Set getModelNames()
    {
        final Set<String> names = new HashSet<String>();
        names.add( ROOT_MODEL.getName() );
        names.addAll( FilterFactory.getInstance().getFilterNames() );
        return names;
    }

    public Set<? extends HerbarModel> getModels()
    {
        final Set<HerbarModel> models = new HashSet<HerbarModel>();
        models.add( ROOT_MODEL );
        models.addAll( FilterFactory.getInstance().getFilters() );
        return models;
    }

    public Set<? extends String> getChangeableModelNames()
    {
        return FilterFactory.getInstance().getFilterNames();
    }

    public Set getChangeableModels()
    {
        return FilterFactory.getInstance().getFilters();
    }

    public static class ExceptionHandler
    {
        public void handle( final Throwable t )
        {
            LOG.fatal( "error dispatching event:", t );
        }
    }

    static
    {
        System.setProperty( "sun.awt.exception.handler", ExceptionHandler.class.getName() );
        Strings.setResourceBundle( ResourceBundle.getBundle( "com.ethz.geobot.herbar.gui.Strings" ) );
        ROOT_NAME = Strings.getString( "FILTERNAME.ALLTAXA" );
        LOG = Logger.getLogger( Application.class );
        INSTANCE = new Application();
        ROOT_MODEL = INSTANCE.getModel();
        ROOT_MODEL.setName( ROOT_NAME );
    }
}

// $Log: Application.java,v $
// Revision 1.1  2007/09/17 11:05:50  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.22  2004/08/31 22:10:15  daniel_frey
// Examlist loading working
//
// Revision 1.21  2004/04/25 13:56:41  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.20  2003/05/04 16:23:42  daniel_frey
// - More ready for fc1
//
// Revision 1.19  2003/05/03 13:25:54  daniel_frey
// - Fixed zoom of big pictures
//
// Revision 1.18  2003/05/03 12:32:10  daniel_frey
// - Added german exam list
//
// Revision 1.17  2003/05/02 14:41:11  daniel_frey
// - List dialog search field now searching also for parts of strings behind the beginning
//
// Revision 1.16  2003/04/30 12:52:41  daniel_frey
// - Introduced dialog questions that default to either ok and cancel
//
// Revision 1.15  2003/04/02 14:49:01  daniel_frey
// - Revised wizards
//
// Revision 1.14  2003/03/19 12:27:30  daniel_frey
// - Move some initialization stuff to static initializer
// - Unzipping db.jar now into data directory
//
// Revision 1.13  2003/02/25 22:25:57  dirk_hoffmann
// all Throwables of EventDispatchThread caught; fix edit bug in FilterDefinitionTable
//
// Revision 1.12  2003/02/25 18:01:43  daniel_frey
// - Made faster through read-only model
//
// Revision 1.11  2003/02/18 02:30:51  dirk_hoffmann
// correct wizard problems
//
// Revision 1.10  2003/01/23 10:54:27  daniel_frey
// - Optimized imports
//
// Revision 1.9  2002/09/20 13:50:53  dirk
// dummy update implementation
//
// Revision 1.8  2002/09/10 15:28:21  dirk
// export filter operations to HerbarContext
//
// Revision 1.7  2002/09/10 09:50:20  dirk
// init general bundle
//
// Revision 1.6  2002/08/21 16:26:45  dirk
// add filter
//
// Revision 1.5  2002/08/02 00:42:21  Dani
// Optimized import statements
//
// Revision 1.4  2002/08/01 15:46:15  Dani
// Exception with mor explaining message
//
