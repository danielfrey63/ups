/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.ant;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Manifest;

/**
 * <p>This ant task is able to increment any number in a text getFile(). It is assumed that the text getFile() is structured like a Manifest getFile(), having entries like the folloing:</p> <p/>
 * <pre>
 * Manifest-Version: 1.0
 * <p/>
 * Name: UpdateableJar
 * JarName: modeapi.jar
 * Description: Modus Schnittstelle
 * MajorVersion: 1
 * MinorVersion: 0
 * BuildVersion: 73
 * </pre>
 * <p/> <p>An ant task may look like the following:</p>
 * <pre>
 * &lt;manifest getFile()="Manifest.txt" getVariable()="UpdateableJar.BuildVersion" steps="true" odd="true"/&gt;
 * </pre>
 * <p/> <p>The attributes <code>getFile()</code> and <code>getVariable()</code> are mandatory, <code>steps</code> and <code>odd</code> are facultative.</p> <p/> <p>The <code>getFile()</code> attribute holds the getFile() on the getFile() system to update and search for the entry to update. The <code>getVariable()</code> is the name of the entry prefix and optionally the section name (value of a name entry) as a prefix separated by a dot.</p> <p/> <p>The <code>steps</code> flag set to true (default is false) enable a mode where numbers are snapped to the next odd or even number. Use <code>odd</code> (defaults to false) to specify whether odd or even numbers should be next. Without <code>steps</code> set to true, the <code>odd</code> attribute has no effect.</p>
 *
 * @author $Author: daniel_frey $ <a href="http://www.xmatrix.ch"> http://www.xmatrix.ch</a>
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class ManifestUpdate extends Incrementor
{
    /**
     * Replaces the old variable name value with the new one. In this case, it is replacing a version/build string.
     *
     * @throws BuildException Something went wrong.
     */
    protected void doExecute() throws BuildException
    {
        Manifest manifest = null;
        try
        {
            manifest = new Manifest( new FileReader( getFile() ) );
        }
        catch ( FileNotFoundException x )
        {
            throw new BuildException( "Manifest file not found. " + x.getMessage() );
        }
        catch ( Exception x )
        {
            throw new BuildException( x.getMessage() );
        }

        final Manifest.Section section;
        String sectionString = "Main";
        if ( getVariable().indexOf( "." ) > -1 )
        {
            final int index = getVariable().lastIndexOf( "." );
            sectionString = getVariable().substring( 0, index );
            setVariable( getVariable().substring( index + 1 ) );
            section = manifest.getSection( sectionString );
        }
        else
        {
            section = manifest.getMainSection();
        }
        if ( null == section )
        {
            throw new BuildException( "The " + sectionString + " section does not exist." );
        }

        final Manifest.Attribute attribute = section.getAttribute( getVariable() );
        if ( null == attribute )
        {
            throw new BuildException( "The " + getVariable() + " attribute does not exist." );
        }

        final String version = attribute.getValue();
        int ver = 0;
        try
        {
            ver = Integer.parseInt( version );
        }
        catch ( NumberFormatException x )
        {
            throw new BuildException( "Must be a number: " + version );
        }

        if ( isDebug() )
        {
            System.out.println( "Found version number: " + ver );
        }

        ver = incrementVersion( ver );

        attribute.setValue( "" + ver );

        try
        {
            TaskTools.writeFile( getFile(), manifest.toString().getBytes() );
        }
        catch ( IOException x )
        {
            throw new BuildException( "Error during write" );
        }
    }

    protected String usage()
    {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( "usage: use this ant task like the following:\n" );
        buffer.append( "<manifest file=\"mainfest.txt\" variable=\"Section.Build\"/>\n" );
        buffer.append( "optionally you may define the two attributes \"steps\" and \"odd\"\n" );
        return buffer.toString();
    }
}

