package ch.jfactory.ant;

import org.apache.tools.ant.util.FileUtils;

/**
 * This class has some helper methods for the VersionTracker and BuildTracker classes.
 *
 * @author Ryan Grier <a href="http://www.ryangrier.com"> http://www.ryangrier.com</a>
 * @version 1.1
 */
public class TaskTools {

    /**
     * <p>This class takes the sourcePath file and The classname given to the ant task and figures out the filename of
     * the class.</p>
     * <p/>
     * Ex: <code>com.ryangrier.ant.test.VersionTest</code> becomes <code>com/ryangrier/ant/test/VersionTest.java</code>
     *
     * @param sourceDirectory Description of the Parameter
     * @param className       Description of the Parameter
     * @return The filePathFromClassName value
     */
    public static java.io.File getFilePathFromClassName(final java.io.File sourceDirectory, final String className) {
        final StringBuffer sb = new StringBuffer();
        sb.append(sourceDirectory.getPath());

        final java.util.StringTokenizer st = new java.util.StringTokenizer(className, ".");
        while (st.hasMoreTokens()) {
            sb.append(System.getProperty("file.separator", "/"));
            sb.append(st.nextToken());
        }
        sb.append(".java");
        return new java.io.File(sb.toString());
    }


    /**
     * Finds the value of the version/build number in the given class file contents.
     *
     * @param fileContents The file to look for.
     * @param variableName The variable name to look for.
     * @return The variable value.
     * @throws Exception           Description of the Exception
     * @throws java.lang.Exception An Exception has occurred.
     */
    public static String findVariableNameValueInClassFile(final String fileContents, final String variableName) throws Exception {
        final StringBuffer sb = new StringBuffer();

        final java.util.StringTokenizer st = new java.util.StringTokenizer(fileContents, System.getProperty("line.separator", "\n"));

        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            final int indexOfVariable = line.indexOf(variableName);
            final int indexOfEquals = line.indexOf("=");
            if ((indexOfVariable != -1) && (indexOfEquals != -1)) {
                line = line.substring(indexOfEquals + 1, line.length() - 1).trim();
                final java.util.StringTokenizer st2 = new java.util.StringTokenizer(line, "\"");
                while (st2.hasMoreTokens()) sb.append(st2.nextToken());

                break;
            }
        }
        return sb.toString();
    }


    /**
     * Finds the old version in the fileContents param and replaces with new version.
     *
     * @param fileContents The file contents to be searched and replaced.
     * @param oldString    The old string to find (to be replaced).
     * @param newString    Replace the old String with this.
     * @return String The file contents with the newString instead of the old.
     */
    public static String replaceOldVersion(String fileContents, final String oldString, final String newString) {

        final StringBuffer sb = new StringBuffer();

        int index = fileContents.indexOf(oldString);

        if (index != -1) {
            sb.append(fileContents.substring(0, index));
            sb.append(newString);
            sb.append(fileContents.substring(index + oldString.length()));
        }

        fileContents = sb.toString();
        index = fileContents.indexOf(oldString);
        if (index != -1) fileContents = replaceOldVersion(fileContents, oldString, newString);
        return fileContents;
    }


    /**
     * Reads a file into memory and returns it as a String.
     *
     * @param file The file to read.
     * @return The file as a String
     * @throws java.io.IOException           Something went wrong.
     * @throws java.io.FileNotFoundException Something went wrong.
     */
    public static String readFile(final java.io.File file) throws java.io.FileNotFoundException, java.io.IOException {
        final String returnString;
        final java.io.FileReader fileReader = new java.io.FileReader(file);
        returnString = FileUtils.readFully(fileReader);
        fileReader.close();

        return returnString;
    }


    /**
     * Writes a file out to disk.
     *
     * @param file     The file to write to.
     * @param contents The content to be written to the file.
     * @throws java.io.IOException           Something went wrong.
     * @throws java.io.FileNotFoundException Something went wrong.
     */
    public static synchronized void writeFile(final java.io.File file, final byte[] contents)
            throws java.io.IOException, java.io.FileNotFoundException {
        final java.io.FileOutputStream theFileOutputStream = new java.io.FileOutputStream(file);
        theFileOutputStream.write(contents);
        theFileOutputStream.close();
    }

    public static int incrementVersion(int ver, final boolean steps, final boolean odd) {
        final int oldVer = ver;
        ver++;
        if (steps) {
            if (odd) {
                ver += (ver + 1) % 2;
            }
            else {
                ver += ver % 2;
            }
        }
        System.out.println("Incrementing build " + oldVer + " to build " + ver);
        return ver;
    }

}
