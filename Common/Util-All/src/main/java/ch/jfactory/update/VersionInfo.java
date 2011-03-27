/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.update;

import ch.jfactory.resource.Version;

/**
 * This class contains version information.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2007/09/27 10:41:22 $
 */
public class VersionInfo
{
    /** Holds value of property name. */
    private String name;

    /** Holds value of property author. */
    private String author;

    /** Holds value of property description. */
    private String description;

    /** Holds value of property majorVersion. */
    private int majorVersion;

    /** Holds value of property minorVersion. */
    private int minorVersion;

    /** Holds the build number. */
    private int buildVersion;

    /** Root path for relative file name. */
    private transient String relativeName;

    /** Holds value of property location. */
    private transient String location;

    public VersionInfo()
    {
    }

    public VersionInfo( final String name, final String description, final String location, final int majorVersion, final int minorVersion,
                        final int buildVersion )
    {
        this.name = name;
        this.description = description;
        this.location = location;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.buildVersion = buildVersion;
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName( final String name )
    {
        this.name = name;
    }

    /**
     * Getter for property location.
     *
     * @return Value of property location.
     */
    public String getLocation()
    {
        return this.location;
    }

    /**
     * Setter for property location.
     *
     * @param location New value of property location.
     */
    public void setLocation( final String location )
    {
        this.location = location;
    }

    /**
     * Getter for property author.
     *
     * @return Value of property author.
     */
    public String getAuthor()
    {
        return this.author;
    }

    /**
     * Setter for property author.
     *
     * @param author New value of property author.
     */
    public void setAuthor( final String author )
    {
        this.author = author;
    }

    /**
     * Getter for property description.
     *
     * @return Value of property description.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Setter for property description.
     *
     * @param description New value of property description.
     */
    public void setDescription( final String description )
    {
        this.description = description;
    }

    /**
     * Getter for property majorVersion.
     *
     * @return Value of property majorVersion.
     */
    public int getMajorVersion()
    {
        return this.majorVersion;
    }

    /**
     * Setter for property majorVersion.
     *
     * @param majorVersion New value of property majorVersion.
     */
    public void setMajorVersion( final int majorVersion )
    {
        this.majorVersion = majorVersion;
    }

    /**
     * Getter for property minorVersion.
     *
     * @return Value of property minorVersion.
     */
    public int getMinorVersion()
    {
        return this.minorVersion;
    }

    /**
     * Setter for property minorVersion.
     *
     * @param minorVersion New value of property minorVersion.
     */
    public void setMinorVersion( final int minorVersion )
    {
        this.minorVersion = minorVersion;
    }

    public int getBuildVersion()
    {
        return buildVersion;
    }

    public void setBuildVersion( final int buildVersion )
    {
        this.buildVersion = buildVersion;
    }

    public String getRelativeName()
    {
        return relativeName;
    }

    public void setRelativeName( final String relativeName )
    {
        this.relativeName = relativeName;
    }

    public String getVersion()
    {
        final StringBuffer buffer = new StringBuffer( 10 );
        buffer.append( majorVersion );
        buffer.append( '.' );
        buffer.append( minorVersion );
        buffer.append( '.' );
        buffer.append( buildVersion );
        return buffer.toString();
    }

    public boolean isNewerAs( final VersionInfo info )
    {
        final Version thisVersion = new Version( majorVersion, minorVersion, buildVersion );
        final Version otherVersion = new Version( info.majorVersion, info.minorVersion, info.buildVersion );
        return thisVersion.compareTo( otherVersion ) > 0;
    }

    public String toString()
    {
        final StringBuffer buf = new StringBuffer( 100 );
        buf.append( "Location: " ).append( location );
        buf.append( ", Name: " ).append( name );
        buf.append( ", Description: " ).append( description );
        buf.append( ", MajorVersion: " ).append( majorVersion );
        buf.append( ", MinorVersion: " ).append( minorVersion );
        buf.append( ", MinorVersion: " ).append( minorVersion );
        return buf.toString();
    }
}
