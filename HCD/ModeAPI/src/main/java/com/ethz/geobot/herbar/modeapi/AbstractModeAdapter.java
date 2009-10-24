package com.ethz.geobot.herbar.modeapi;

import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import java.awt.Component;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import javax.swing.JPanel;
import org.apache.log4j.Category;

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
public class AbstractModeAdapter extends AbstractMode {

    private static Category cat = Category.getInstance(AbstractModeAdapter.class.getName());

    private Class componentClass;
    private WeakReference viewComponent = new WeakReference(null);

    public AbstractModeAdapter(Class componentClass) {
        if (Component.class.isAssignableFrom(componentClass)) {
            this.componentClass = componentClass;
        }
        else {
            String errorText = "AbstractModeAdapter receive illegal class " + componentClass +
                    ", not extended from Component";
            cat.error(errorText);
            throw new IllegalArgumentException(errorText);
        }
    }

    public void wizardSettingsFinish() {
    }

    public void wizardSettingsInit() {
    }

    public boolean queryDeactivate() {
        Component vc = (Component) viewComponent.get();
        if (vc != null) {
            if (ModeActivation.class.isAssignableFrom(vc.getClass())) {
                ModeActivation act = (ModeActivation) vc;
                return act.queryDeactivate();
            }
        }
        return super.queryDeactivate();
    }

    public final Component getComponent() {
        Component vc = (Component) viewComponent.get();
        if (vc == null) {
            vc = initiateComponent();
            viewComponent = new WeakReference(vc);
        }
        return vc;
    }

    public final void activate() {
        super.activate();

        Component vc = getComponent();

        getHerbarContext().getHerbarGUIManager().setViewComponent(vc);

        if (ModeActivation.class.isAssignableFrom(vc.getClass())) {
            ModeActivation act = (ModeActivation) vc;
            act.activate();
        }
    }

    public final void deactivate() {
        Component vc = (Component) viewComponent.get();
        if (vc != null) {
            if (ModeActivation.class.isAssignableFrom(vc.getClass())) {
                ModeActivation act = (ModeActivation) vc;
                act.deactivate();
            }
        }
    }

    private Component initiateComponent() {
        Component component;

        cat.info("initiateComponent()");
        // first initiate component via default constructor
        try {
            component = (Component) componentClass.newInstance();
        }
        catch (Exception ex) {
            try {
                Class[] params = {this.getClass()};
                Constructor cons = componentClass.getConstructor(params);
                Object[] objects = {this};
                component = (Component) cons.newInstance(objects);
            }
            catch (Exception ex2) {
                cat.error("failed to instantiate panel class " + componentClass.getName(), ex);
                component = new JPanel();
                // return a default panel
            }
        }

        return component;
    }

    final public void destroy() {
        super.destroy();
    }

    final public void init(HerbarContext context) {
        super.init(context);

        WizardModel wm = getWizardModel();
        if (wm != null) {
            context.getHerbarGUIManager().setWizardModel(wm);
        }
    }

    /**
     * Override this method to add a wizard to the mode.
     *
     * @return the WizardModel
     */
    public WizardModel getWizardModel() {
        return null;
    }
}
