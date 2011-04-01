/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            LOG.debug( "init herbar model by class name " + implClass );
            try
            {
                final Class<?> clazz = Class.forName( implClass );
                model = (HerbarModel) clazz.newInstance();
            }
            catch ( Exception ex )
            {
                final String message = "could not initiate model: " + implClass;
                LOG.error( message, ex );
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
            LOG.warn( "User want to save a model different than a FilterModel class:" + model.getClass() );
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
            LOG.warn( "User want to save a model different than a FilterModel class:" + model.getClass() );
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
    }

    static
    {
        System.setProperty( "sun.awt.exception.handler", ExceptionHandler.class.getName() );
        Strings.setResourceBundle( ResourceBundle.getBundle( "com.ethz.geobot.herbar.gui.Strings" ) );
        ROOT_NAME = Strings.getString( "FILTER_NAME.ALL_TAXA" );
        LOG = LoggerFactory.getLogger( Application.class );
        INSTANCE = new Application();
        ROOT_MODEL = INSTANCE.getModel();
        ROOT_MODEL.setName( ROOT_NAME );
    }
}
