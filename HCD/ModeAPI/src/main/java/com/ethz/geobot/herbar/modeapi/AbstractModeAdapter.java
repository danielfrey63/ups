package com.ethz.geobot.herbar.modeapi;

import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import java.awt.Component;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a Mode Adapter to translate an existing Component based form into a mode class. When extending this
 * class, the user set the class of the Component form by calling the super([ComponentClass]). The constuctor of the
 * concrete adapter is also responsible to set the properties. There is a special behaviour how the Component will be
 * constructed. If the component have a default constructor, then it will initiate the object using it. If there is a
 * constructor with the Mode as an Argument, this construtor is invoked and the depending mode is given in the argument.
 * Take in mind, that the default constructor must be deleted when using the constructor with the Mode argument. Todo:
 * Remove refresh / GUI artifacts when changing mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class AbstractModeAdapter extends AbstractMode
{
    private static final Logger LOG = LoggerFactory.getLogger( AbstractModeAdapter.class.getName() );

    private final Class componentClass;

    private WeakReference<Component> viewComponent = new WeakReference<Component>( null );

    public AbstractModeAdapter( final Class<? extends Component> componentClass )
    {
        this.componentClass = componentClass;
    }

    public void wizardSettingsFinish()
    {
    }

    public void wizardSettingsInit()
    {
    }

    public boolean queryDeactivate()
    {
        final Component vc = viewComponent.get();
        if ( vc != null )
        {
            if ( ModeActivation.class.isAssignableFrom( vc.getClass() ) )
            {
                final ModeActivation act = (ModeActivation) vc;
                return act.queryDeactivate();
            }
        }
        return super.queryDeactivate();
    }

    public final Component getComponent()
    {
        Component vc = viewComponent.get();
        if ( vc == null )
        {
            vc = initiateComponent();
            viewComponent = new WeakReference<Component>( vc );
        }
        return vc;
    }

    public final void activate()
    {
        super.activate();

        LOG.info( "activating mode " + this );

        final Component vc = getComponent();

        getHerbarContext().getHerbarGUIManager().setViewComponent( vc );

        if ( ModeActivation.class.isAssignableFrom( vc.getClass() ) )
        {
            final ModeActivation act = (ModeActivation) vc;
            act.activate();
        }
    }

    public final void deactivate()
    {
        LOG.info( "de-activating mode " + this );

        final Component vc = viewComponent.get();
        if ( vc != null )
        {
            if ( ModeActivation.class.isAssignableFrom( vc.getClass() ) )
            {
                final ModeActivation act = (ModeActivation) vc;
                act.deactivate();
            }
        }
    }

    private Component initiateComponent()
    {
        Component component;

        LOG.info( "initializing component" );
        // first initiate component via default constructor
        try
        {
            component = (Component) componentClass.newInstance();
        }
        catch ( Exception ex )
        {
            try
            {
                final Class[] params = {this.getClass()};
                final Constructor cons = componentClass.getConstructor( params );
                final Object[] objects = {this};
                component = (Component) cons.newInstance( objects );
            }
            catch ( Exception ex2 )
            {
                LOG.error( "failed to instantiate panel class " + componentClass.getName(), ex );
                component = new JPanel();
                // return a default panel
            }
        }

        return component;
    }

    final public void destroy()
    {
        super.destroy();
    }

    final public void init( final HerbarContext context )
    {
        super.init( context );

        final WizardModel wm = getWizardModel();
        if ( wm != null )
        {
            context.getHerbarGUIManager().setWizardModel( wm );
        }
    }

    /**
     * Override this method to add a wizard to the mode.
     *
     * @return the WizardModel
     */
    public WizardModel getWizardModel()
    {
        return null;
    }
}
