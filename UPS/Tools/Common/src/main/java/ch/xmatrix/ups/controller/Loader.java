/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.controller;

import ch.jfactory.model.IdAware;
import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.model.SessionModel;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.log4j.Logger;

/**
 * Loads models from the filesystem (user home) or classspath, saves to the filesystem (user home).
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/06 10:16:20 $
 */
public class Loader
{
    /** Set this system property to <code>true</code> if the model should not be saved really, but printed to the log. */
    public static final String ENVIRONMENT_SIMULATESAVE = "ch.jfactory.simulatesave";

    private static final Logger LOG = Logger.getLogger(Loader.class);

    /**
     * Loads the model from the user home by adding a dot, the relative path and the resource to it. If it doesn't exist
     * the resource is loaded from the classpath by prepending the relative path and making it absolut. So i.e. if you
     * give <code>resource=/data/model.xml</code> and <code>path=myapps/myapp</code> the search strategy looks like
     * that:
     *
     * <ol>
     *
     * <li><code>&lt;path to user home&gt;/.myapps/myapp/data/model.xml</code></li>
     *
     * <li><code>/myapps/myapp/data/model.xml</code></li>
     *
     * </ol>
     *
     * @param resource  the model resource (file or classpath resource)
     * @param path      the realtive path to the resource
     * @param converter the converter to transform the content to an object
     * @return a SimpleModelList object containing the model
     */
    public static <T> T loadModel(final String resource, final String path, final XStream converter)
    {
        final File file = getSettingsFile(resource, path);
        return Loader.<T>loadModel(file, resource, converter);
    }

    /**
     * Saves the model to the a file using the given converter. The file is formed by using the user home, adding a dot,
     * the relative path and the resource to it.
     *
     * @param resource  the resource name
     * @param path      relative path to the resource
     * @param converter the resource converter
     * @param model     the model to save
     */
    public static <T> void saveModel(final String resource, final String path, final XStream converter, final T model)
    {
        final File file = getSettingsFile(resource, path);
        saveModel(converter, model, file);
    }

    private static <T> T loadModel(final File file, final String modelResource, final XStream converter)
    {
        final T list;
        String feedback = null;
        try
        {
            final InputStream in;
            if (file.exists())
            {
                feedback = "file " + file.getAbsolutePath();
                in = new FileInputStream(file);
            }
            else
            {
                feedback = "resource " + modelResource;
                in = Loader.class.getResourceAsStream(modelResource);
            }
            final Reader reader = new InputStreamReader(in);
            final Object object = converter.fromXML(reader);
            if (object instanceof SimpleModelList)
            {
                final SimpleModelList sml = (SimpleModelList) object;
                for (final Object o : sml)
                {
                    final IdAware ia = (IdAware) o;
                    System.out.println("model " + modelResource + ": " + ia.getUid());
                    if (ia instanceof SessionModel)
                    {
                        System.out.println("    constraints: " + ((SessionModel)ia).getConstraintsUid());
                    }
                }
            }
            list = (T) object;
        }
        catch (Exception e)
        {
            final String message = "problems loading model from " + feedback;
            LOG.error(message, e);
            throw new IllegalStateException(message, e);
        }
        return list;
    }

    private static <T> void saveModel(final XStream converter, final T model, final File file)
    {
        final String content = converter.toXML(model);
        if (!Boolean.parseBoolean(System.getProperty(ENVIRONMENT_SIMULATESAVE, "false")))
        {
            try
            {
                file.getParentFile().mkdirs();
                final FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
            }
            catch (IOException e)
            {
                LOG.error("save to file " + file + " failed", e);
            }
        }
        else
        {
            LOG.warn("save is in simulation mode");
        }
        LOG.info("saved to " + file);
    }

    /**
     * Returns the file used as setting for the given resource in the user home by adding the given relativ path and the
     * resource to it.
     *
     * @param resource the settings resource
     * @param path     the relative path to the settings resource
     * @return the file
     */
    private static File getSettingsFile(final String resource, final String path)
    {
        final String userHome = System.getProperty("user.home");
        final File result = new File(userHome, "." + path);
        return new File(result, resource);
    }
}
