/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.application;

import ch.jfactory.action.ActionBuilder;
import ch.jfactory.action.ActionRedirector;
import ch.jfactory.binding.CodedNote;
import ch.jfactory.binding.DefaultInfoModel;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.Note;
import ch.jfactory.component.splash.ImageSplash;
import ch.jfactory.resource.OperatingSystem;
import ch.jfactory.resource.ResourceHelper;
import ch.jfactory.resource.Strings;
import ch.jfactory.resource.Version;
import com.jgoodies.looks.Options;
import com.jgoodies.uif.AbstractFrame;
import com.jgoodies.uif.application.Application;
import com.jgoodies.uif.application.ApplicationConfiguration;
import com.jgoodies.uif.application.ApplicationDescription;
import com.jgoodies.uif.splash.Splash;
import com.jgoodies.uif.util.ResourceUtils;
import com.jgoodies.uifextras.convenience.DefaultApplicationStarter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.DimensionUIResource;
import org.apache.log4j.helpers.OptionConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class addresses three purposes:
 *
 * <ol>
 *
 * <li><b>Configuration:</b> It wraps configuration for JGoodies <code>DefaultApplicationStarter</code> application
 * properties into system properties.</li>
 *
 * <li><b>Version Check:</b> It handles java version checks.</li>
 *
 * <li><b>Model-View-Control:</b> It implements the row structure for a model-view-control concept.</li>
 *
 * </ol>
 *
 * <b>Configuration</b><p/>
 *
 * Configuration takes place as a properties file. The default name of the properties file is
 * <code>xmatrix.properties</code> and is tried to be loaded in one of the classpath root directories (more precise
 * <code>/xmatrix.properties</code> is loaded like a resource). A reference to the file may also be passed by the usual
 * mechanisms for configuring a system property with the key <code>app.configuration</code> or during extention of this
 * superclass by overwriding the constructor. For example:
 *
 * <pre>
 * public class Main extends MainRunner {
 *     public static void main(String[] args) {
 *         // Pass the absolute reference to a configuration file
 *         new Main("/myapp.properties");
 *     }
 *     public Main(String properties) {
 *         super(properties);
 *     }
 * }
 * </pre>
 *
 * Properties that should be located in this file which are used by JGoodies AbstractApplicationStarter:
 * <pre>
 * jfactory.main.about.name.long = Long application description
 * jfactory.main.about.name.short = Short application description
 * jfactory.main.product.name = Name of the product
 * jfactory.main.product.description = Long description of the product
 * jfactory.main.prefs.node.name = Path to the preferences node, i.e. mydomain/myapp
 * jfactory.main.version.major = Version number
 * jfactory.main.version.full = Version number
 * jfactory.main.copyright = ©
 * jfactory.main.vendor.name = Company name
 * jfactory.main.vendor.url = Company URL
 * jfactory.main.vendor.mail = Company e-mail
 * jfactory.main.path.tool = Path to the resources, i.e. resources/
 * jfactory.main.path.helpset = Path to the help resources
 * jfactory.main.path.tipindex = Path to the tipindex files
 * </pre>
 *
 * Mandatory properties used by JFactory:
 * <pre>
 * // Log4j configuration file.
 * jfactory.log4j.configuration = /log4j.configuration
 * </pre>
 *
 * <b>Version Check</b><p/>
 *
 * Version check (2) is done by the following optional properties:
 * <pre>
 * // Indicate the minimal version needed to run this application. Defaults to 1.0.
 * jfactory.version.minimal = 1.4.2
 *
 * // Indicate the error message title when a required java version is not used to run the
 * // application. Optional. Defaults to the text given here.
 * jfactory.version.error.title = Conflicting Java Versions
 *
 * // Indicate the text of the error message when a required java version is not used to run the
 * // application. Optional. Defaults to the text given here.
 * jfactory.version.error.text = You are using Java {1}. However, to run this application you need {0}.
 * </pre>
 *
 * <b>Model-View-Control</b><p/>
 *
 * To configure model and controler specify the following two properties:
 *
 * <pre>
 * jfactory.main.model = Main model class
 * jfactory.main.controler = Main controler class
 * jfactory.main.frame = Main frame class
 * </pre>
 *
 * The model class must provide a public field <code>DEFAULT</code> or an accessor <code>getDefaultModel()</code>. The
 * controler hast to implement {@link AbstractMainController} and the constructor has to accept as its only argument an
 * instance of the main model.
 *
 * @author Daniel Frey
 * @version $Revision: 1.6 $ $Date: 2007/09/27 10:41:22 $
 */
public class MainRunner extends DefaultApplicationStarter
{
    private static Logger LOG;

    public static final String PROPERTYNAME_NAME_SHORT = "jfactory.main.about.name.short";

    public static final String PROPERTYNAME_PRODUCT_NAME = "jfactory.main.product.name";

    public static final String PROPERTYNAME_PRODUCT_FILENAME = "jfactory.main.product.filename";

    public static final String PROPERTYNAME_PRODUCT_DESCRIPTION = "jfactory.main.product.description";

    public static final String PROPERTYNAME_PREFERENCES_NODE_NAME = "jfactory.main.prefs.node.name";

    public static final String PROPERTYNAME_VERSION_SHORT = "jfactory.main.version.major";

    public static final String PROPERTYNAME_VERSION_FULL = "jfactory.main.version.full";

    public static final String PROPERTYNAME_COPYRIGHT = "jfactory.main.copyright";

    public static final String PROPERTYNAME_VENDOR_NAME = "jfactory.main.vendor.name";

    public static final String PROPERTYNAME_VENDOR_URL = "jfactory.main.vendor.url";

    public static final String PROPERTYNAME_VENDOR_MAIL = "jfactory.main.vendor.mail";

    public static final String PROPERTYNAME_RESOURCE_PATH = "jfactory.main.path.tool";

    public static final String PROPERTYNAME_HELP_SET_PATH = "jfactory.main.path.helpset";

    public static final String PROPERTYNAME_TIP_INDEX_PATH = "jfactory.main.path.tipindex";

    private static final String KEY_MAIN_MODEL = "jfactory.main.model";

    private static final String KEY_MAIN_CONTROLLER = "jfactory.main.controller";

    private static final String KEY_MAIN_FRAME = "jfactory.main.frame";

    private static final String KEY_MINIMAL_VERSION = "jfactory.version.minimal";

    private static final String DEFAULT_MINIMAL_VERSION = "1.0";

    private static final String KEY_VERSION_ERROR_TITLE = "jfactory.version.error.title";

    private static final String DEFAULT_VERSION_ERROR_TITLE = "Version Conflict";

    private static final String KEY_VERSION_ERROR_TEXT = "jfactory.version.error.text";

    private static final String DEFAULT_VERSION_ERROR_TEXT = "You are using Java version {1}. However, version {0} is needed.";

    private static final String KEY_APP_CONFIG = "app.configuration";

    private static final String DEFAULT_APP_CONFIG = "/xmatrix.properties";

    private static final String KEY_LOG_CONFIG = "log.configuration";

    private static final String DEFAULT_LOG_CONFIG = "/log4j.properties";

    private static final String KEY_SPLASH_IMAGE = "jfactory.splash.image";

    private static final String KEY_SPLASH_TEXTFONT = "jfactory.splash.text.font";

    private static final String KEY_SPLASH_TEXTFACE = "jfactory.splash.text.face";

    private static final String KEY_SPLASH_TEXTSIZE = "jfactory.splash.text.size";

    private static final String KEY_SPLASH_TEXTCOLOR = "jfactory.splash.text.color";

    private Object mainModel;

    private AbstractMainController mainController;

    private InfoModel infoModel;

    static
    {
        final String appConfig = System.getProperty( KEY_APP_CONFIG, DEFAULT_APP_CONFIG );
        try
        {
            initProperties( appConfig );
            final String logConfig = System.getProperty( KEY_LOG_CONFIG, DEFAULT_LOG_CONFIG );
            try
            {
                initLogging( logConfig );
            }
            catch ( Exception e )
            {
                System.err.println( "Make sure to supply system property \"" + KEY_LOG_CONFIG +
                        "\" or default log property file \"/log4j.property\"" );
            }
        }

        catch ( Exception e )
        {
            System.err.println( "Make sure to supply system property \"" + KEY_APP_CONFIG +
                    "\" or default application property file \"" + DEFAULT_APP_CONFIG + "\"" );
        }
    }

    public Object getMainModel()
    {
        return mainModel;
    }

    public AbstractMainController getMainController()
    {
        return mainController;
    }

    public InfoModel getInfoModel()
    {
        return infoModel;
    }

    protected MainRunner()
    {
        Toolkit.getDefaultToolkit().setDynamicLayout( true );
        try
        {
            final ApplicationDescription description = new ApplicationDescription(
                    System.getProperty( PROPERTYNAME_NAME_SHORT ),
                    System.getProperty( PROPERTYNAME_PRODUCT_NAME ),
                    System.getProperty( PROPERTYNAME_VERSION_SHORT ),
                    System.getProperty( PROPERTYNAME_VERSION_FULL ),
                    System.getProperty( PROPERTYNAME_PRODUCT_DESCRIPTION ),
                    System.getProperty( PROPERTYNAME_COPYRIGHT ),
                    System.getProperty( PROPERTYNAME_VENDOR_NAME ),
                    System.getProperty( PROPERTYNAME_VENDOR_URL ),
                    System.getProperty( PROPERTYNAME_VENDOR_MAIL ) );
            final ApplicationConfiguration configuration = new ApplicationConfiguration(
                    System.getProperty( PROPERTYNAME_PREFERENCES_NODE_NAME ),
                    System.getProperty( PROPERTYNAME_PRODUCT_FILENAME ),
                    System.getProperty( PROPERTYNAME_RESOURCE_PATH ),
                    System.getProperty( PROPERTYNAME_HELP_SET_PATH ),
                    System.getProperty( PROPERTYNAME_TIP_INDEX_PATH ) );

            boot( description, configuration );
        }
        catch ( Exception e )
        {
            LOG.error( "error during initialization of application", e );
        }
    }

    protected void launchApplication()
    {
        checkVersion();

        infoModel = new DefaultInfoModel();
        createSplashInfoModelGlue();

        getInfoModel().setNote( new CodedNote( Strings.getString( "startup.model" ) ) );
        mainModel = createMainModel();

        getInfoModel().setNote( new CodedNote( Strings.getString( "startup.controler" ) ) );
        mainController = createMainController();

        getInfoModel().setNote( new CodedNote( Strings.getString( "startup.actions" ) ) );
        initializeActions();

        getInfoModel().setNote( new CodedNote( Strings.getString( "startup.frame" ) ) );
        final AbstractFrame mainFrame = createMainFrame();
        Application.setDefaultParentFrame( mainFrame );

        checkSetup();

        mainFrame.build();
        mainFrame.open();
    }

    private void createSplashInfoModelGlue()
    {
        getInfoModel().addPropertyChangeListener( InfoModel.PROPERTYNAME_NOTE, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final Note note = (Note) evt.getNewValue();
                Splash.setNote( note.getMessage() );
                final Integer percentage = note.getPercentage();
                if ( percentage != null )
                {
                    Splash.setProgress( percentage );
                }
            }
        } );
    }

    protected void configureSplash()
    {
        final URL resource = getResourceUrl( KEY_SPLASH_IMAGE );
        final Image image = new ImageIcon( resource ).getImage();
        Splash.setProvider( new ImageSplash( image, true ) );
        final ImageSplash splash = (ImageSplash) Splash.getProvider();
        splash.setNoteEnabled( true );
        splash.setFont( new Font( getResourceString( KEY_SPLASH_TEXTFONT ),
                (Integer) ResourceHelper.getConstant( Font.class, getResourceString( KEY_SPLASH_TEXTFACE ), int.class ),
                Integer.parseInt( getResourceString( KEY_SPLASH_TEXTSIZE ) ) ) );
        splash.setTextColor( ResourceHelper.decode( getResourceString( KEY_SPLASH_TEXTCOLOR ) ) );
        splash.setForeground( new Color( 64, 64, 64 ) );
        splash.setBackground( Color.white );
        splash.setVersionHeight( 49 );
        splash.setVersion( "Version " + Application.getDescription().getFullVersion() );
    }

    private URL getResourceUrl( final String key )
    {
        final String string = getResourceString( key );
        URL resource = MainRunner.class.getResource( string );
        if ( string == null )
        {
            resource = MainRunner.class.getResource( string );
            if ( resource == null )
            {
                resource = ResourceUtils.getURL( key );
                if ( resource == null )
                {
                    System.out.println( "" );
                }
            }
        }
        return resource;
    }

    private String getResourceString( final String key )
    {
        String string = System.getProperty( key );
        if ( string == null )
        {
            string = ResourceUtils.getString( key );
        }
        return string;
    }

    protected void configureUI()
    {
        Options.setUseSystemFonts( true );
//        Options.setGlobalFontSizeHints(FontSizeHints.MIXED2);
        Options.setDefaultIconSize( new Dimension( 18, 18 ) );
        Options.setPopupDropShadowEnabled( true );

        super.configureUI();
        final String laf = System.getProperty( "ch.jfactory.laf" );
        if ( laf != null )
        {
            try
            {
                UIManager.setLookAndFeel( laf );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }

        UIManager.put( Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE );
        UIManager.put( "ToolBar.separatorSize", new DimensionUIResource( 4, 10 ) );
        UIManager.put( "ToolBar.border", BorderFactory.createEmptyBorder() );
        UIManager.put( "ToolBar.nonrolloverBorder", BorderFactory.createLineBorder( Color.lightGray, 1 ) );
        UIManager.put( "ToolBar.rolloverBorder", BorderFactory.createLineBorder( Color.lightGray, 3 ) );
        UIManager.put( "Tree.font", UIManager.getFont( "Label.font" ) );
    }

    protected void initializeActions()
    {
        // Load action properties
        final String resource = "/" + Application.getConfiguration().getResourcePath() + "Action.properties";
        ActionBuilder.setProperties( ActionBuilder.class.getResourceAsStream( resource ) );
        final Properties props = ActionBuilder.getProperties();
        try
        {
            // Init actions
            final Enumeration propKeys = props.keys();
            final String keySuffix = ".controller";
            while ( propKeys.hasMoreElements() )
            {
                final String prop = (String) propKeys.nextElement();
                if ( prop.endsWith( keySuffix ) )
                {
                    final String keyPartOne = prop.substring( 0, prop.indexOf( keySuffix ) );
                    final String value = props.getProperty( prop );
                    ActionBuilder.registerAction( keyPartOne, new ActionRedirector( getMainController(), value ) );
                }
            }
        }
        catch ( Exception e )
        {
            LOG.error( "error during initialization of actions", e );
        }
    }

    /**
     * Make sure the application does not quit.
     *
     * @return always false
     */
    protected boolean requestForWindowClose()
    {
        return false;
    }

    /**
     * Loads the properties from the hardcoded properties file. Makes sure that these properties are overwritten from
     * the properties loaded by the system or command line.
     */
    private static void initProperties( final String properties ) throws IOException
    {
        final Properties configProperties = new Properties();
        configProperties.load( MainRunner.class.getResourceAsStream( properties ) );
        for ( final Object o : configProperties.keySet() )
        {
            final String key = (String) o;
            System.setProperty( key, System.getProperty( key, (String) configProperties.get( key ) ) );
        }
        System.out.println( "properties loaded from the configuration file " + properties + " are: " + listProperties( configProperties ) );
        System.out.println( "system properties set are: " + listProperties( System.getProperties() ) );
    }

    /**
     * Concatenats properties in the form <code>{{key1=value1}, {key2=value2}}.
     *
     * @param properties the properties to concatenate
     * @return the concatenated string
     */
    private static String listProperties( final Properties properties )
    {
        final StringBuilder buffer = new StringBuilder( "{" );
        boolean first = true;
        for ( final Object key : properties.keySet() )
        {
            final Object value = properties.get( key );
            if ( !first )
            {
                buffer.append( ", " );
            }
            buffer.append( "{" ).append( key ).append( "=" );
            buffer.append( value.toString().replace( "\r\n", "\\n\\r" ).replace( "\r", "\\n" ).replace( "\n", "\\r" ) );
            buffer.append( "}" );
            first = false;
        }
        return buffer.toString();
    }

    private static void initLogging( final String properties ) throws IOException
    {
        final Properties props = System.getProperties();
        props.load( MainRunner.class.getResourceAsStream( properties ) );
        final Enumeration keys = props.keys();
        while ( keys.hasMoreElements() )
        {
            final String key = (String) keys.nextElement();
            if ( key.endsWith( ".file" ) )
            {
                final String value = props.getProperty( key );
                final String fileName = OptionConverter.substVars( value, props );
                new File( fileName ).getParentFile().mkdirs();
            }
        }

        LOG = LoggerFactory.getLogger( MainRunner.class );
    }

    private Object createMainModel()
    {
        try
        {
            final String modelProperty = System.getProperty( KEY_MAIN_MODEL );
            if ( modelProperty == null )
            {
                throw new NullPointerException( "The main model has not be defined. Please specify a \"" +
                        KEY_MAIN_MODEL + "\" system property." );
            }
            final Class modelClass = Class.forName( modelProperty );
            try
            {
                final Field field = modelClass.getField( "DEFAULT" );
                return field.get( null );
            }
            catch ( NoSuchFieldException e )
            {
                try
                {
                    final Method defaultModel = modelClass.getMethod( "getDefaultModel" );
                    return defaultModel.invoke( null );
                }
                catch ( Exception e1 )
                {
                    e1.printStackTrace();
                }
            }
            catch ( Exception e )
            {
                LOG.error( "error during initialization of model", e );
            }
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
        }
        return null;
    }

    private AbstractMainController createMainController()
    {
        return (AbstractMainController) createObject( KEY_MAIN_CONTROLLER, "main controller" );
    }

    public AbstractFrame createMainFrame()
    {
        return (AbstractFrame) createObject( KEY_MAIN_FRAME, "main frame" );
    }

    private Object createObject( final String propertyKey, final String name )
    {
        final String propertyString = System.getProperty( propertyKey );
        if ( propertyString == null )
        {
            throw new NullPointerException( "property for " + name + " not set. Please specify a system property \"" +
                    propertyKey + "\"" );
        }
        final Class clazz;
        try
        {
            clazz = Class.forName( propertyString );
        }
        catch ( ClassNotFoundException e )
        {
            LOG.error( e.getMessage(), e );
            throw new RuntimeException( e.getMessage(), e );
        }
        Constructor constructor;
        try
        {
            constructor = clazz.getConstructor( getMainModel().getClass(), InfoModel.class );
            return constructor.newInstance( getMainModel(), getInfoModel() );
        }
        catch ( NoSuchMethodException e )
        {
            try
            {
                constructor = clazz.getConstructor( getMainModel().getClass() );
                return constructor.newInstance( getMainModel() );
            }
            catch ( NoSuchMethodException e1 )
            {
                LOG.error( e.getMessage(), e );
                throw new RuntimeException( e.getMessage(), e );
            }
            catch ( IllegalAccessException e1 )
            {
                LOG.error( e.getMessage(), e );
                throw new RuntimeException( e.getMessage(), e );
            }
            catch ( InvocationTargetException e1 )
            {
                LOG.error( e.getMessage(), e );
                throw new RuntimeException( e.getMessage(), e );
            }
            catch ( InstantiationException e1 )
            {
                LOG.error( e.getMessage(), e );
                throw new RuntimeException( e.getMessage(), e );
            }
        }
        catch ( IllegalAccessException e )
        {
            LOG.error( e.getMessage(), e );
            throw new RuntimeException( e.getMessage(), e );
        }
        catch ( InvocationTargetException e )
        {
            LOG.error( e.getMessage(), e );
            throw new RuntimeException( e.getMessage(), e );
        }
        catch ( InstantiationException e )
        {
            LOG.error( e.getMessage(), e );
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    private static void checkVersion()
    {
        final String min = System.getProperty( KEY_MINIMAL_VERSION, DEFAULT_MINIMAL_VERSION );
        final MessageFormat format = new MessageFormat( System.getProperty( KEY_VERSION_ERROR_TEXT, DEFAULT_VERSION_ERROR_TEXT ) );
        final String running = new Version( OperatingSystem.JAVA_VERSION_TRIMMED ).toString();
        final String message = format.format( new String[]{min, running} );
        if ( !OperatingSystem.isMinimalVersionRunning( min ) )
        {
            final String title = System.getProperty( KEY_VERSION_ERROR_TITLE, DEFAULT_VERSION_ERROR_TITLE );
            JOptionPane.showMessageDialog( null, message, title, JOptionPane.ERROR_MESSAGE );
            System.exit( 1 );
        }
    }

    public static void main( final String[] args )
    {
        setVersion( MainRunner.class );
        new MainRunner();
    }

    public static void setVersion( final Class<? extends MainRunner> clazz )
    {
        try
        {
            final String classContainer = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
            final URL manifestUrl = new URL( "jar:" + classContainer + "!/META-INF/MANIFEST.MF" );
            final Manifest manifest = new Manifest( manifestUrl.openStream() );
            final String version = manifest.getMainAttributes().getValue( Attributes.Name.IMPLEMENTATION_VERSION );
            System.setProperty( PROPERTYNAME_VERSION_FULL, version );
        }
        catch ( IOException e )
        {
            System.setProperty( PROPERTYNAME_VERSION_FULL, "Test Version" );
        }
    }
}
