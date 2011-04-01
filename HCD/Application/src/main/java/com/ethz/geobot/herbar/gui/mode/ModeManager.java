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
package com.ethz.geobot.herbar.gui.mode;

import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.ModeInfo;
import com.ethz.geobot.herbar.modeapi.ModeRegistration;
import com.ethz.geobot.herbar.modeapi.ModeRegistrationSupport;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage Mainframe modes.
 *
 * @author $Author: daniel_frey $ $date$
 * @version $Revision: 1.1 $
 */
public class ModeManager implements ModeRegistrationSupport
{
    /** Category object for logging. */
    private static final Logger LOG = LoggerFactory.getLogger( ModeManager.class );

    private static ModeManager instance = null;

    /** Store mode settings from the manifest file. */
    private final Map<String, ModeInfo> modeInfos = new HashMap<String, ModeInfo>();

    /** HashMap containing all registered modes. */
    private final Map<Object, Mode> registeredModes = new HashMap<Object, Mode>();

    protected ModeManager()
    {
        ModeRegistration.setRegistrationSupport( this );
    }

    /**
     * static factory method to get a reference to a ModeManager instance (singleton)
     *
     * @return reference to the ModeManager
     */
    public static ModeManager getInstance()
    {
        if ( instance == null )
        {
            instance = new ModeManager();
            instance.registerAllModes();
        }

        return instance;
    }

    /**
     * Return a reference to a mode identified by a name.
     *
     * @param name name of the mode
     * @return reference to the mode
     * @throws ModeNotFoundException if the mode cannot be found
     */
    public Mode getMode( final String name ) throws ModeNotFoundException
    {
        final Mode mode = registeredModes.get( name );
        if ( mode == null )
        {
            final String errorMessage = "Mode with name " + name + " not found!";
            LOG.info( errorMessage );
            throw new ModeNotFoundException( errorMessage );
        }
        return mode;
    }

    /**
     * Return a set of all registered modes.
     *
     * @return reference to a set containing all modes
     */
    public Set getModeNames()
    {
        return registeredModes.keySet();
    }

    /**
     * Return a collection of all registered modes.
     *
     * @return reference to a set containing all modes
     */
    public Collection<? extends Mode> getModes()
    {
        return registeredModes.values();
    }

    public void register( final Mode mode )
    {
        // load settings from manifest
        final ModeInfo info = modeInfos.get( mode.getClass().getName() );
        if ( info != null )
        {
            final String description = info.getDescription();
            if ( description != null )
            {
                mode.setProperty( Mode.DESCRIPTION, description );
            }
            final String name = info.getModeName();
            if ( name != null )
            {
                mode.setProperty( Mode.NAME, name );
            }
            final String group = info.getModeGroup();
            if ( group != null )
            {
                mode.setProperty( Mode.MODE_GROUP, group );
            }
            final String iconName = info.getIcon();
            if ( iconName != null )
            {
                mode.setProperty( Mode.ICON, ImageLocator.getIcon( iconName ) );
            }
            final String disabledIconName = info.getDisabledIcon();
            if ( disabledIconName != null )
            {
                mode.setProperty( Mode.DISABLED_ICON, ImageLocator.getIcon( disabledIconName ) );
            }
        }

        registeredModes.put( mode.getProperty( Mode.NAME ), mode );
        mode.init( new HerbarContextImpl( mode ) );
        LOG.info( "successfully register mode; classname = " + mode.getClass() );
    }

    public void unregister( final Mode mode )
    {
        registeredModes.remove( mode.getProperty( Mode.NAME ) );
    }

    /** register all jar files in a directory specified by the "herbar.modedir" environment variable. */
    private void registerAllModes()
    {
        try
        {
            final Enumeration<URL> modes = ModeManager.class.getClassLoader().getResources( "META-INF/ModeInfo.xml" );
            if ( modes.hasMoreElements() )
            {
                // at least one mode jar has been found in the classpath (i.e. WebStart)
                while ( modes.hasMoreElements() )
                {
                    final URL url = modes.nextElement();
                    loadClass( url.openStream() );
                }
            }
            else
            {
                // try on file system
                final String moddir = System.getProperty( "herbar.modedir" );
                if ( LOG.isInfoEnabled() )
                {
                    LOG.info( "loading modes from directory " + moddir );
                }
                final File file = new File( moddir );
                final File[] files = file.listFiles( new FilenameFilter()
                {
                    public boolean accept( final File dir, final String name )
                    {
                        return name.toLowerCase().endsWith( ".jar" );
                    }
                } );
                final int nummodes = files.length;
                LOG.info( "try to register " + nummodes + " modes from directory " + moddir );
                for ( int i = 0; i < nummodes; i++ )
                {
                    registerJarFile( files[i] );
                }
            }
        }
        catch ( Exception ex )
        {
            LOG.error( "Mode initialization failed", ex );
        }
    }

    /**
     * register a specified jar file.
     *
     * @param file the reference to the File
     */
    private void registerJarFile( final File file )
    {
        String clazzName = "";
        final String resource = "META-INF/ModeInfo.xml";
        try
        {
            final JarFile jarFile = new JarFile( file );
            final ZipEntry entry = jarFile.getEntry( resource );
            if ( entry != null )
            {
                final InputStream inputStream = jarFile.getInputStream( entry );
                clazzName = loadClass( inputStream );
            }
        }
        catch ( IOException e )
        {
            final String msg = "Error during decoding of XML mode information (" + resource + ") in " + file;
            LOG.error( msg, e );
            throw new IllegalArgumentException( msg );
        }
        catch ( ClassNotFoundException cnfex )
        {
            LOG.error( "Mode class not found: " + clazzName, cnfex );
        }
        catch ( LinkageError error )
        {
            LOG.error( "Initialization of mode class failed: " + clazzName, error );
        }
    }

    private String loadClass( final InputStream inputStream ) throws ClassNotFoundException
    {
        final XMLDecoder decoder = new XMLDecoder( inputStream );
        final ModeInfo info = (ModeInfo) decoder.readObject();
        final String clazzName = info.getModeClass();
        if ( clazzName != null && !"".equals( clazzName ) )
        {
            modeInfos.put( clazzName, info );
            Class.forName( clazzName );
        }
        return clazzName;
    }
}
