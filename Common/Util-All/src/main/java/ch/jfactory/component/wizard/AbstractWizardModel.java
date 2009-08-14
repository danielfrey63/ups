package ch.jfactory.component.wizard;

import com.jgoodies.binding.beans.Model;
import java.util.Properties;

/**
 * Base functionallity for WizardModel. Mainly the beans related parts.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2006/03/14 21:27:56 $
 */
abstract public class AbstractWizardModel extends Model implements WizardModel
{

    /** Configuration. */
    private Properties config;

    /** Used to identify the wizard model per instance. */
    private String name;

    /**
     * Create a wizard model for modes.
     *
     * @param config the configuration used in the mode
     * @param name   the name of the model
     */
    public AbstractWizardModel(final Properties config, final String name)
    {
        this.config = config;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Properties getConfig()
    {
        return config;
    }
}
