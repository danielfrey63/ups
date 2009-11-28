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
package ch.xmatrix.ups.uec.sets.commands;

import ch.jfactory.component.Dialogs;
import ch.jfactory.file.OpenChooser;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import com.thoughtworks.xstream.XStream;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JTextField;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/23 22:19:10 $
 */
public abstract class LoadFilesystem extends ActionCommand
{
    protected static final String EXTENTION = ".xust";

    private static final Logger LOG = Logger.getLogger( FromDirectory.class );

    private static final boolean DEBUG = LOG.isDebugEnabled();

    private static final String PATTERN = "%B %I %L %F %X";

    private static final String PATTERN_FIRSTNAME = "%F";

    private static final String PATTERN_LASTNAME = "%L";

    private static final String PATTERN_ID = "%I";

    private static final String PATTERN_FROM = "%S";

    private static final String PATTERN_END = "%E";

    private static final String PATTERN_EXTENTION = "%X";

    private static final String PATTERN_SKIP = "%B";

    private static final String PATTERN_REGEX = PATTERN_FIRSTNAME + "|" + PATTERN_LASTNAME + "|" + PATTERN_END +
            "|" + PATTERN_EXTENTION + "|" + PATTERN_FROM + "|" + PATTERN_ID + "|" + PATTERN_SKIP;

    private final OpenChooser chooser;

    private final SetBuilder.SubmitTableModel model;

    private JTextField patternField;

    private String lastName;

    private ArrayList<Registration> buffer;

    private String lastDirectory = System.getProperty( "user.dir" );

    public LoadFilesystem( final CommandManager manager, final String commandId, final SetBuilder.SubmitTableModel model )
    {
        super( manager, commandId );
        this.model = model;
        chooser = getChooser();
        try
        {
            final String pack = getClass().getPackage().getName();
            final String path = pack.replace( '.', '/' );
            final FormCreator creator = new FormCreator( FormLoader.load( path + "/PatternLayoutPanel.jfd" ) );
            creator.createAll();
            patternField = creator.getTextField( "field" );
            patternField.setText( PATTERN );
            chooser.getChooser().setAccessory( creator.getPanel( "panel" ) );
            chooser.setDirectory( new File( lastDirectory ) );
        }
        catch ( Exception e )
        {
            LOG.error( "could not load chooser component", e );
        }
    }

    protected abstract OpenChooser getChooser();

    protected void writeBuffer()
    {
        if ( DEBUG )
        {
            LOG.debug( "there are " + buffer.size() + " registrations" );
        }
        for ( int i = 0; model != null && i < buffer.size(); i++ )
        {
            final Registration registration = buffer.get( i );
            model.add( registration );
        }
    }

    protected void handleExecute()
    {
        buffer = new ArrayList<Registration>();
        chooser.open();
    }

    protected void loadAnonymousFile( final File file )
    {
        lastDirectory = file.getParent();
        lastName = file.getName();
        final IAnmeldedaten anmeldedatum = loadPersonalData( lastName, patternField.getText() );
        final PlantList list = loadPlantList( file );
        buffer.add( new Registration( anmeldedatum, list ) );
    }

    private static Map<String, String> parsePattern( final String name, final String pattern )
    {
        if ( DEBUG )
        {
            LOG.debug( "parsing \"" + name + "\" with pattern \"" + pattern + "\"" );
        }
        final Map<String, String> result = new HashMap<String, String>();
        try
        {
            final String[] delimiters = pattern.split( PATTERN_REGEX );
            int nameCursor = 0;
            int patCursor = 0;
            final int length = delimiters.length;
            for ( int i = 0; i < length; i++ )
            {
                final String delim = delimiters[i];
                final int l = delim.length();
                final int nameStart = "".equals( delim ) ? 0 : name.indexOf( delim, nameCursor );
                final int nameEnd = i == length - 1 ? name.length() : name.indexOf( delimiters[i + 1], nameStart + l );
                final String value = name.substring( nameStart + l, nameEnd );
                final int patStart = "".equals( delim ) ? 0 : pattern.indexOf( delim, patCursor );
                final int patEnd = i == length - 1 ? pattern.length() : pattern.indexOf( delimiters[i + 1], patStart + l );
                final String key = pattern.substring( patStart + l, patEnd );
                if ( ( "".equals( delim ) || !name.endsWith( delim ) ) && !PATTERN_SKIP.equals( key ) )
                {
                    result.put( key, value );
                }
                nameCursor += value.length() + l;
                patCursor += key.length() + l;
            }
            if ( DEBUG )
            {
                final Set<String> keys = result.keySet();
                final StringBuffer b = new StringBuffer( "result is " );
                for ( final Iterator<String> iterator = keys.iterator(); iterator.hasNext(); b.append( ", " ) )
                {
                    final String s = iterator.next();
                    b.append( s ).append( "=" ).append( result.get( s ) );
                }
                LOG.debug( b );
            }
        }
        catch ( Exception e )
        {
            Dialogs.showErrorMessage( null, "Fehler", "" +
                    "Beim Verarbeiten der Datei \"" + name + "\" mit dem\n" +
                    "Muster \"" + pattern + "\" ist ein Fehler aufgetreten.\n" +
                    "Versuchen Sie es erneut mit einem anderen Muster." );
            throw new IllegalArgumentException( "could not parse \"" + name + "\" with pattern \"" + pattern + "\"", e );
        }
        return result;
    }

    private IAnmeldedaten loadPersonalData( final String name, final String pattern )
    {
        final Anmeldedaten anmeldedatum = new Anmeldedaten();
        final Map<String, String> map = parsePattern( name, pattern );
        anmeldedatum.setNachname( map.get( PATTERN_LASTNAME ) );
        anmeldedatum.setVorname( map.get( PATTERN_FIRSTNAME ) );
        anmeldedatum.setStudentennummer( map.get( PATTERN_ID ) );
        return anmeldedatum;
    }

    // Todo: Make this working with new file format from UST.
    private static PlantList loadPlantList( final File file )
    {
        PlantList list = null;
        try
        {
            final String species = IOUtils.toString( new FileReader( file ) );
            final XStream converter = Commands.getConverter2();
            final Object converted = converter.fromXML( species );
            if ( converted instanceof Commands.Encoded )
            {
                final Commands.Encoded encoded = (Commands.Encoded) converted;
                list = new PlantList();
                list.setTaxa( new ArrayList<String>( encoded.list ) );
            }
            else if ( converted instanceof ArrayList )
            {
                final ArrayList<String> taxa = (ArrayList<String>) converted;
                list = new PlantList();
                list.setTaxa( taxa );
            }
            else
            {
                Dialogs.showErrorMessage( null, "Fehler", "Unbekanntest (altes) Dateiformat" );
                LOG.error( "unknown file format" );
            }
        }
        catch ( Exception e )
        {
            Dialogs.showErrorMessage( null, "Fehler", "" +
                    "Beim Verarbeiten der Datei \"" + file.getName() + "\"\n" +
                    "ist ein Fehler aufgetreten.\n" +
                    "(" + e.getMessage() + ")" );
            throw new IllegalArgumentException( "could not deserialize list in \"" + file + "\"", e );
        }
        return list;
    }
}
