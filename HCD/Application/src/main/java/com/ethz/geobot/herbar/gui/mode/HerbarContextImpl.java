/*
 * Herbar CD-ROM version 2
 *
 * HerbarContextImpl.java
 *
 * Created on ??. ??? 2002
 * Created by ????
 */
package com.ethz.geobot.herbar.gui.mode;

import ch.jfactory.application.view.status.StatusBar;
import ch.jfactory.component.Dialogs;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.filter.FilterPersistentException;
import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.HerbarGUIManager;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.Wizard;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.model.HerbarModel;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HerbarContext implementation for MainFrame Herbar Application.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class HerbarContextImpl implements HerbarContext
{
    private static final Logger LOG = LoggerFactory.getLogger( HerbarContextImpl.class );

    private static final Properties props = new Properties();

    private static HerbarModel currentModel = null;

    private static final ModeWizard modeWizard = ModeWizard.getInstance();

    private static final Map<Mode, HerbarGUIManager> modeGUIManagers = new HashMap<Mode, HerbarGUIManager>();

    private final Mode mode;

    HerbarContextImpl( final Mode mode )
    {
        this.mode = mode;
    }

    public HerbarModel getDataModel()
    {
        return Application.getInstance().getModel();
    }

    public HerbarGUIManager getHerbarGUIManager()
    {
        HerbarGUIManager manager = modeGUIManagers.get( mode );
        if ( manager == null )
        {
            manager = new HerbarContextImpl.HerbarGUIManagerImpl();
            modeGUIManagers.put( mode, manager );
        }

        return manager;
    }

    public String getProperty( final String name )
    {
        return props.getProperty( name );
    }

    public Preferences getPreferencesNode()
    {
        return Preferences.userNodeForPackage( ModeManager.class ).node( (String) mode.getProperty( Mode.NAME ) );
    }

    public Set getPropertyNames()
    {
        return props.keySet();
    }

    public HerbarModel getModel( final String modelName )
    {
        HerbarModel model = null;
        try
        {
            model = Application.getInstance().getModel( modelName );
        }
        catch ( FilterPersistentException ex )
        {
            Dialogs.showErrorMessage( getHerbarGUIManager().getParentFrame().getRootPane(), "Fehler", "ERROR.FILTERLOAD" );
            model = getDataModel();
        }
        return model;
    }

    public Set getModelNames()
    {
        return Application.getInstance().getModelNames();
    }

    public Set getModels()
    {
        return Application.getInstance().getModels();
    }

    public Set getChangeableModelNames()
    {
        return Application.getInstance().getChangeableModelNames();
    }

    public Set getChangeableModels()
    {
        return Application.getInstance().getChangeableModels();
    }

    public void saveModel( final HerbarModel model )
    {
        try
        {
            Application.getInstance().saveModel( model );
        }
        catch ( FilterPersistentException ex )
        {
            final String msg = "failed to save filter : " + model.getName();
            LOG.error( msg, ex );
            Dialogs.showErrorMessage( getHerbarGUIManager().getParentFrame().getRootPane(), "Fehler", Strings.getString( "FILTER_SAVE_FAILED" ) );
            throw new IllegalStateException( msg );
        }
    }

    public void removeModel( final HerbarModel model )
    {
        try
        {
            Application.getInstance().removeModel( model );
        }
        catch ( FilterPersistentException ex )
        {
            final String msg = "failed to remove filter : " + model.getName();
            Dialogs.showErrorMessage( getHerbarGUIManager().getParentFrame().getRootPane(), "Fehler", Strings.getString( "FILTER_REMOVE_FAILED" ) );
            LOG.error( msg, ex );
            throw new IllegalStateException( msg );
        }
    }

    /**
     * Return current selected model.
     *
     * @return a reference to the model
     */
    public HerbarModel getCurrentModel()
    {
        if ( currentModel == null )
        {
            currentModel = getDataModel();
            if ( LOG.isInfoEnabled() )
            {
                LOG.info( "change current model to " + currentModel.getName() );
            }
        }
        return currentModel;
    }

    /**
     * Change the current model to the given one
     *
     * @param model reference to the model
     */
    public void setCurrentModel( final HerbarModel model )
    {
        if ( LOG.isInfoEnabled() )
        {
            LOG.info( "change current model to " + model.getName() );
        }
        currentModel = model;
    }

    class HerbarGUIManagerImpl implements HerbarGUIManager
    {
        public void setViewComponent( final Component component )
        {
            AppHerbar.getMainFrame().getModel().setViewComponent( component );
        }

        public JFrame getParentFrame()
        {
            return AppHerbar.getMainFrame();
        }

        public void setWizardModel( final WizardModel wm )
        {
            modeWizard.setWizardModel( mode, wm );
        }

        /**
         * invoke the mode wizard.
         */
        public void invokeWizard()
        {
            final WizardModel model = modeWizard.getWizardModel( mode );
            final Wizard dlg = new Wizard( model );
            /*boolean accepted =*/
            dlg.show( AppHerbar.getMainFrame() );
        }

        /**
         * shows an error message
         *
         * @param message the error message
         */
        public void showErrorMessage( final String message )
        {
            Dialogs.showErrorMessage( getParentFrame().getRootPane(), "Fehler", message );
        }

        public StatusBar getStatusBar()
        {
            return AppHerbar.getMainFrame().getStatusBar();
        }
    }
}
