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

import ch.jfactory.update.VersionInfo;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;

/**
 * <p>This ant task is able to increment any number in a {@link VersionInfo VersionInfo} getFile(). It is assumed that the getFile() is an xml getFile() like the one produced by the java.bean.XMLEncoder of Sun for a {@link VersionInfo VersionInfo} instance.</p> <p/> <p>Use the task as follows:</p> <p/>
 * <pre>
 * &lt;versioninfo getFile()="VersionInfo.xml" property="BuildVersion" steps="true" odd="false/&gt;
 * </pre>
 * <p/> <p>The attributes <code>getFile()</code> and <code>property</code> are mandatory, <code>steps</code> and <code>odd</code> are facultative.</p> <p/> <p>The <code>getFile()</code> attribute holds the getFile() on the getFile() system to update and search for the entry to update. The <code>property</code> is the name of the VersionInfo property to be updated. See the documentation of {@link VersionInfo VersionInfo} for a list of valid properties.</p> <p/> <p>The <code>steps</code> flag set to true (default is false) enable a mode where numbers are snapped to the next odd or even number. Use <code>odd</code> (defaults to false) to specify whether odd or even numbers should be next. Without <code>steps</code> set to true, the <code>odd</code> attribute has no effect.</p>
 *
 * @author $Author: daniel_frey $ <a href="http://www.xmatrix.ch"> http://www.xmatrix.ch</a>
 * @version $Revision: 1.3 $ $Date: 2006/04/15 23:03:21 $
 */
public class VersionInfoIncrementor extends Incrementor
{
    /**
     * Replaces the old variable name value with the new one. In this case, it is replacing a version/build string.
     *
     * @throws BuildException Something went wrong.
     */
    protected void doExecute() throws BuildException
    {
        final File file = getFile();

        VersionInfo info = null;
        try
        {
            final XMLDecoder decoder = new XMLDecoder( new FileInputStream( file ) );
            info = (VersionInfo) decoder.readObject();
            if ( info == null )
            {
                throw new NullPointerException( "VersionInfo object in file " + file + " not deserialized successfully" );
            }
            decoder.close();
        }
        catch ( FileNotFoundException x )
        {
            throw new BuildException( "Manifest file not found. " + x.getMessage() );
        }
        final String variable = getVariable();
        final Class viClass = VersionInfo.class;
        if ( viClass == null )
        {
            throw new BuildException( "Class " + viClass + " not found." );
        }
        if ( new VersionInfo() == null )
        {
            throw new BuildException( "Class " + viClass + " not instanciable." );
        }

        String methodName = "get" + variable;
        int ver = 0;
        try
        {
            final Method method = viClass.getMethod( methodName );
            if ( method == null )
            {
                throw new BuildException( "Method " + methodName + " not found in VersionInfo class." );
            }
            System.out.println( method );
            ver = (Integer) method.invoke( info );
        }
        catch ( NoSuchMethodException e )
        {
            throw new BuildException( "Method with name " + methodName + " not found. " + e.getMessage() );
        }
        catch ( SecurityException e )
        {
            throw new BuildException( "Method with name " + methodName + " not accessible. " + e.getMessage() );
        }
        catch ( IllegalAccessException e )
        {
            throw new BuildException( "Method with name " + methodName + " not accessible. " + e.getMessage() );
        }
        catch ( InvocationTargetException e )
        {
            throw new BuildException( "Method with name " + methodName + " not invokable. " + e.getMessage() );
        }

        ver = incrementVersion( ver );

        methodName = "set" + variable;
        try
        {
            final Method method = viClass.getMethod( methodName, int.class );
            if ( method == null )
            {
                throw new BuildException( "Method " + methodName + " not found in VersionInfo class." );
            }
            method.invoke( info, ver );
        }
        catch ( NoSuchMethodException e )
        {
            throw new BuildException( "Method with name " + methodName + " not found. " + e.getMessage() );
        }
        catch ( SecurityException e )
        {
            throw new BuildException( "Method with name " + methodName + " not accessible. " + e.getMessage() );
        }
        catch ( IllegalAccessException e )
        {
            throw new BuildException( "Method with name " + methodName + " not accessible. " + e.getMessage() );
        }
        catch ( InvocationTargetException e )
        {
            throw new BuildException( "Method with name " + methodName + " not invokable. " + e.getMessage() );
        }

        try
        {
            final XMLEncoder encoder = new XMLEncoder( new FileOutputStream( file ) );
            encoder.writeObject( info );
            encoder.close();
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
        buffer.append( "<versioninfo file=\"mainfest.txt\" property=\"Section.Build\"/>\n" );
        buffer.append( "optionally you may define the two attributes \"steps\" and \"odd\"\n" );
        return buffer.toString();
    }
}

