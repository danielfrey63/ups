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
package ch.jfactory.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Formatter;
import javax.swing.JComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:23 $
 */
public class LogicUtils
{

    private static final Logger LOG = Logger.getLogger(LogicUtils.class);

    private static final boolean DEBUG = LOG.isDebugEnabled();

    public static final int NUMBER_OF_BITS_IN_A_BYTE = 8;

    /**
     * Returns the index of the first flag with value <code>false</code>. Useful to get the decision in a hierarchical
     * decision tree. I.e. you want to derive a list of distinct warnings when you write into a directory that will
     * contain some data for your application. The warnings may look like this:
     *
     * <ol>
     *
     * <li>No warning.</li>
     *
     * <li>If a file exists: <code>CAUTION: There is a file with this name that will be deleted!</code></li>
     *
     * <li>If an empty directory exists: <code>CAUTION: There is an empty directory at this location!</code></li>
     *
     * <li>If there is some unknown content in the directory: <code>CAUTION: There is a directory with content at this
     * location!</code></li>
     *
     * <li>If the directory contains some application data, but it is not valid in any sense: <code>CAUTION: The
     * directory contains an invalid version!</code></li>
     *
     * <li>If the directory contains some valid application data: <code>CAUTION: The directory contains a valid
     * version!</code></li>
     *
     * </ol>
     *
     * You could nest some if-else statements to pass to the right warning, or you simply use this method with an array
     * of flags for the following states:
     *
     * <ol>
     *
     * <li>The file exists, i.e. <code>new File(name).exists()</code>.</li>
     *
     * <li>The existing file is a directory, i.e. <code>new File(name).isDirectory()</code>.</li>
     *
     * <li>The directory contains files, i.e. <code>new File(name).list().length > 0</code>.</li>
     *
     * <li>The directory contains some application recognizable data</li>
     *
     * <li>The directory contains some valid application data</li>
     *
     * </ol>
     *
     * To derive the correct warning, just pass an array of the above decisions to this method and apply the resulting
     * index to the array of warnings. Note that for a number of warnings <code>N</code> you need <code>N-1</code>
     * boolean states. Hierarchical in this example means that subsequent decisions are dependent of previous ones.<p/>
     *
     * So if i.e. you find some data in a directory that is not recognizable as application data, pass an array of
     * <code>{true, true, true, false, false}</code> and you get index <code>3</code> out of it. The fourth warning
     * results then as <code>CAUTION: There is a directory with content at this location!</code>.<p/>
     *
     * The full decision table is given below. Flags in paranthesis are results of an illegal state, that means, you
     * cannot have a directory if the file with that directory name does not exist (lines 1-16) :
     *
     * <pre>
     * ex  di  fi  va  pr | warning
     * -------------------+--------------
     * 0  (0) (0) (0) (0) | warnNone
     * 0  (0) (0) (0) (1) | warnNone
     * 0  (0) (0) (1) (0) | warnNone
     * 0  (0) (0) (1) (1) | warnNone
     * 0  (0) (1) (0) (0) | warnNone
     * 0  (0) (1) (0) (1) | warnNone
     * 0  (0) (1) (1) (0) | warnNone
     * 0  (0) (1) (1) (1) | warnNone
     * 0  (1) (0) (0) (0) | warnNone
     * 0  (1) (0) (0) (1) | warnNone
     * 0  (1) (0) (1) (0) | warnNone
     * 0  (1) (0) (1) (1) | warnNone
     * 0  (1) (1) (0) (0) | warnNone
     * 0  (1) (1) (0) (1) | warnNone
     * 0  (1) (1) (1) (0) | warnNone
     * 0  (1) (1) (1) (1) | warnNone
     * 1   0  (0) (0) (0) | warnFile
     * 1   0  (0) (0) (1) | warnFile
     * 1   0  (0) (1) (0) | warnFile
     * 1   0  (0) (1) (1) | warnFile
     * 1   0  (1) (0) (0) | warnFile
     * 1   0  (1) (0) (1) | warnFile
     * 1   0  (1) (1) (0) | warnFile
     * 1   0  (1) (1) (1) | warnFile
     * 1   1   0  (0) (0) | warnDirectory
     * 1   1   0  (0) (1) | warnDirectory
     * 1   1   0  (1) (0) | warnDirectory
     * 1   1   0  (1) (1) | warnDirectory
     * 1   1   1   0  (0) | warnNotEmpty
     * 1   1   1   0  (1) | warnNotEmpty
     * 1   1   1   1   0  | warnValidApp
     * 1   1   1   1   1  | warnValidProj
     * </pre>
     *
     * You can compress the table into a tree if you leave the nonsense away:
     *
     * <pre>
     * ex  di  fi  va  pr | warning
     * -------------------+--------------
     * 0                  | warnNone
     * 1   0              | warnFile
     * 1   1   0          | warnDirectory
     * 1   1   1   0      | warnNotEmpty
     * 1   1   1   1   0  | warnValidApp
     * 1   1   1   1   1  | warnValidProj
     * </pre>
     *
     * If you have some non-hierarchical decisions consider using {@link #getFullIndex(boolean[])}.
     *
     * @param flags array of flags to scan.
     * @return the index of the first flag with value <code>false</code>
     */
    public static int getFirstFalse(final boolean[] flags)
    {
        for (int i = 0; i < flags.length; i++)
        {
            final boolean flag = flags[i];
            if (!flag)
            {
                return i;
            }
        }
        return flags.length;
    }

    public static int getFullIndex(final int[] flags)
    {
        double pos = Math.pow(2, flags.length - 1);
        int result = 0;
        for (final int flag : flags)
        {
            if (flag == 1)
            {
                result += pos;
            }
            pos /= 2;
        }
        return result;
    }

    public static int getFullIndex(final boolean[] flags)
    {
        double pos = Math.pow(2, flags.length - 1);
        int result = 0;
        for (final boolean flag : flags)
        {
            if (flag)
            {
                result += pos;
            }
            pos /= 2;
        }
        return result;
    }

    public static boolean[] toBoolean(final int[] flags)
    {
        if (flags == null)
        {
            return new boolean[0];
        }
        final boolean[] result = new boolean[flags.length];
        for (int i = 0; i < flags.length; i++)
        {
            final int flag = flags[i];
            result[i] = (flag == 1);
        }
        return result;
    }

    public static boolean[][] toBoolean(final int[][] flags)
    {
        if (flags == null)
        {
            return new boolean[0][];
        }
        final boolean[][] result = new boolean[flags.length][];
        for (int i = 0; i < flags.length; i++)
        {
            final int[] flag = flags[i];
            result[i] = toBoolean(flag);
        }
        return result;
    }

    /**
     * Does a logical AND on two arrays of booleans. The two arrays passes must have equal size.
     *
     * @param booleansA
     * @param booleansB
     * @return the logical AND of the two arrays of boolean
     * @throws IllegalArgumentException if the two arrays are of different size
     */
    public static boolean[] and(final boolean[] booleansA, final boolean[] booleansB)
    {
        if (booleansA.length != booleansB.length)
        {
            throw new IllegalArgumentException("array lengths must be equal");
        }
        final boolean[] result = new boolean[booleansA.length];
        for (int i = 0; i < booleansA.length; i++)
        {
            result[i] = booleansA[i] && booleansB[i];
        }
        return result;
    }

    /**
     * Does a logical OR on a set of booleans.
     *
     * @param booleans
     * @return one boolean with the logical OR of the set of booleans
     */
    public static boolean or(final boolean[] booleans)
    {
        boolean result = false;
        for (boolean aBoolean : booleans)
        {
            result |= aBoolean;
        }
        return result;
    }

    /**
     * Enables each component according to the corresponding mapping.
     *
     * @param bits                       the input states
     * @param components                 the components
     * @param componentMapps             the component mappings
     * @param numberOfHierarchicalStates the index where hierarchial mappings end and full matrix mapping starts
     */
    public static void setEnabledStates(final InfoBit[] bits, final Object[] components, final long[] componentMapps,
                                        final int numberOfHierarchicalStates)
    {
        final int status = Integer.parseInt(getIntString(bits), 2);
        final int hierarchicalIndex = findLowestZeroBit(status);

        StringBuilder debugStringBuffer = null;
        Formatter formatter = null;
        if (DEBUG)
        {
            debugStringBuffer = new StringBuilder();
            formatter = new Formatter(debugStringBuffer);
            final String stat = Integer.toBinaryString(status);
            formatter.format("%n    status                    %s%s", StringUtils.repeat("0", bits.length - stat.length()), stat);
            for (int i = bits.length - 1; i >= 0; i--)
            {
                formatter.format("%n    status                  %d %s", bits[i].isBit() ? 1 : 0, bits[i].getInfo());
            }
            formatter.format("%n    lowest hierarchical index %s", hierarchicalIndex);
            formatter.format("%n    hierarchical states count %s", numberOfHierarchicalStates);
            formatter.format("%n    hierarchical              %b", hierarchicalIndex < numberOfHierarchicalStates);
        }

        for (int i = 0; i < components.length; i++)
        {
            final Object component = components[i];
            final long componentMapping = componentMapps[i];
            boolean enabled = false;
            if (DEBUG)
            {
                final String name = component.getClass().getName();
                formatter.format("%n%n    component     %s", name.substring(name.lastIndexOf(".") + 1));
                formatter.format("%n    mapping       %s", Long.toBinaryString(componentMapping));
            }
            if (hierarchicalIndex < numberOfHierarchicalStates)
            {
                enabled = isBitSet(componentMapping, hierarchicalIndex);
                if (DEBUG)
                {
                    final int index = Long.toBinaryString(componentMapping).length() - hierarchicalIndex;
                    formatter.format("%n    " + (enabled ? "+ enabled " : "- disabled") + " %2d %" + index + "s", hierarchicalIndex, "^");
                }
            }
            else
            {
                final int tableIndex = status >> numberOfHierarchicalStates;
                final int shift = tableIndex + numberOfHierarchicalStates;
                enabled = isBitSet(componentMapping, shift);
                if (DEBUG)
                {
                    final int index = Long.toBinaryString(componentMapping).length() - tableIndex - numberOfHierarchicalStates;
                    formatter.format("%n    " + (enabled ? "+ enabled " : "- disabled") + " %2d %" + index + "s", shift, "^");
                }
            }
            if (component instanceof JComponent)
            {
                ((JComponent) component).setEnabled(enabled);
            }
            else if (component instanceof ActionCommand)
            {
                ((ActionCommand) component).setEnabled(enabled);
            }
            else
            {
                invokeSetEnabled(component, enabled);
            }
        }
        if (DEBUG)
        {
            LOG.debug(debugStringBuffer.toString());
        }
    }

    private static void invokeSetEnabled(final Object component, final boolean enabled)
    {
        try
        {
            final Class clazz = component.getClass();
            final Method method = clazz.getMethod("setEnabled", boolean.class);
            try
            {
                method.invoke(component, enabled);
            }
            catch (IllegalAccessException e)
            {
                throw new IllegalArgumentException("cannot access method " + method.getName(), e);
            }
            catch (InvocationTargetException e)
            {
                throw new IllegalArgumentException("error during invokation of " + method.getName(), e);
            }
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException("component " + component.getClass().getName()
                    + " is component without setEnabled(boolean) method.", e);
        }
    }

    public static String getIntString(final boolean[] flags)
    {
        final StringBuffer result = new StringBuffer();
        for (final boolean flag : flags)
        {
            result.append(flag ? "1" : "0");
        }
        return result.toString();
    }

    public static String getIntString(final InfoBit[] flags)
    {
        final StringBuffer result = new StringBuffer();
        for (InfoBit flag1 : flags)
        {
            final boolean flag = flag1.isBit();
            result.append(flag ? "1" : "0");
        }
        return result.toString();
    }

    public static int findLowestZeroBit(final int integer)
    {
        for (int i = 0; i < NUMBER_OF_BITS_IN_A_BYTE; i++)
        {
            final int mask = (1 << i);
            if ((integer & mask) == 0)
            {
                return i;
            }
        }
        return NUMBER_OF_BITS_IN_A_BYTE;
    }

    public static boolean isBitSet(final long b, final int i)
    {
        final long mask = (long) Math.pow(2, i);
        return (b & mask) == mask;
    }

    public static class InfoBit
    {

        private boolean bit;

        private String info;

        public InfoBit(final boolean bit, final String yes, final String no)
        {
            this.bit = bit;
            this.info = bit ? yes : no;
        }

        public boolean isBit()
        {
            return bit;
        }

        public String getInfo()
        {
            return info;
        }
    }
}
