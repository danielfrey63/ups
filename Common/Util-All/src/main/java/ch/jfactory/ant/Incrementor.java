package ch.jfactory.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public abstract class Incrementor extends Task
{
    /** The manifest file file. */
    private java.io.File file = null;

    /** The variable to replace. */
    private String variable = null;

    private boolean debug = false;

    /** If set to true, ncrements to the next odd number. Default is false. */
    private boolean odd = false;

    /** Enables the incrementation into odd or even numbers. */
    private boolean steps = false;

    /**
     * Sets the file attribute of the ManifestFile object
     *
     * @param file The new file value
     */
    public void setFile(final java.io.File file)
    {
        this.file = file;
    }

    public java.io.File getFile()
    {
        return file;
    }

    /**
     * Sets the variable attribute of the ManifestFile object. The vairable parameter supports sections. To identify a
     * section of the manifest file, use the dot separator. I.e given the following Manifest file:
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
     * The variable <code>UpdateableJar.BuildVersion</code> is the line <code>BuildVersion</code> in section
     * <code>UpdateableJar</code> that will be updated.
     *
     * @param variable The new variable value
     */
    public void setVariable(final String variable)
    {
        this.variable = variable;
    }

    public String getVariable()
    {
        return variable;
    }

    public void setDebug(final boolean debug)
    {
        this.debug = debug;
    }

    public boolean isDebug()
    {
        return debug;
    }

    public void setOdd(final boolean odd)
    {
        this.steps = true;
        this.odd = odd;
    }

    public int incrementVersion(final int oldVersion)
    {
        return TaskTools.incrementVersion(oldVersion, steps, odd);
    }

    public void execute() throws BuildException
    {
        if (null == getFile())
        {
            throw new BuildException("The file attribute must be provided.\n" + usage());
        }
        else if (!getFile().exists())
        {
            throw new BuildException("The file provided could not be found: " + getFile() + "\n" + usage());
        }

        if (null == getVariable())
        {
            throw new BuildException("The variable attribute must be provided.\n" + usage());
        }

        doExecute();

    }

    protected abstract void doExecute() throws BuildException;

    protected abstract String usage();
}
