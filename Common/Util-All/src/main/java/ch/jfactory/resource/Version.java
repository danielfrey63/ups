/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.resource;

import java.util.StringTokenizer;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2005/11/17 11:54:59 $
 */
public class Version implements Comparable
{

    private transient int[] versions;

    private String version;

    public Version(final String versionString)
    {

        final String normalizedVersion = versionString.replaceAll(" ", "").replaceAll("build", ".");
        final StringTokenizer tokenizer = new StringTokenizer(normalizedVersion, "._");
        final int tokens = tokenizer.countTokens();
        if (tokens < 2)
        {
            throw new IllegalArgumentException("Not enough version information in string " + versionString);
        }
        versions = new int[tokens];
        for (int i = 0; tokenizer.hasMoreElements(); i++)
        {
            versions[i] = Integer.parseInt(tokenizer.nextToken());
        }
        setVersion();
    }

    public Version(final int main, final int major, final int minor, final int build)
    {

        versions = new int[4];
        versions[0] = main;
        versions[1] = major;
        versions[2] = minor;
        versions[3] = build;

        setVersion();
    }

    public Version(final int main, final int major, final int minor)
    {

        versions = new int[3];
        versions[0] = main;
        versions[1] = major;
        versions[2] = minor;

        setVersion();
    }

    public Version(final int main, final int major)
    {

        versions = new int[2];
        versions[0] = main;
        versions[1] = major;

        setVersion();
    }

    private void setVersion()
    {
        final StringBuffer result = new StringBuffer();
        result.append(versions[0]);
        result.append(".");
        result.append(versions[1]);
        if (versions.length > 2)
        {
            result.append(".");
            result.append(versions[2]);
        }
        if (versions.length > 3)
        {
            result.append(".");
            result.append(versions[3]);
        }
        if (versions.length > 4)
        {
            result.append(".");
            result.append(versions[4]);
        }
        version = result.toString();
    }

    public String toString()
    {
        return version;
    }

    public int compareTo(final Object o)
    {

        if (!(o instanceof Version))
        {
            throw new IllegalArgumentException(o + " is not of type " + Version.class.getName());
        }
        if (o == null)
        { // Todo: Doublecheck sense of this statement
            return 1;
        }
        final Version other = (Version) o;
        if (other.versions == null)
        { // Todo: Doublecheck sense of this statement
            return 1;
        }
        final int minLength = Math.min(versions.length, other.versions.length);
        final int maxLength = Math.max(versions.length, other.versions.length);
        int result = 0;
        int i;
        for (i = 0; i < minLength; i++)
        {
            result = new Integer(versions[i]).compareTo(other.versions[i]);
            if (result != 0)
            {
                break;
            }
        }
        if (result == 0)
        {
            if (versions.length > minLength)
            {
                for (; i < maxLength; i++)
                {
                    final int version = versions[i];
                    if (version > 0)
                    {
                        result = 1;
                        break;
                    }
                    else if (version < 0)
                    {
                        result = -1;
                        break;
                    }
                }
            }
            if (other.versions.length > minLength)
            {
                for (; i < maxLength; i++)
                {
                    final int version = other.versions[i];
                    if (version > 0)
                    {
                        result = -1;
                        break;
                    }
                    else if (version < 0)
                    {
                        result = 1;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
