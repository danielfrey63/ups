package ch.jfactory.math;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class NumberPrinter
{

    public static String toBits(final long l)
    {
        final StringBuffer result = new StringBuffer();
        for (int order = memorySizeOfLong() - 1; order >= 0; order--)
        {
            final long value = (long) Math.pow(2, order);
            if ((l & value) == value)
            {
                result.append("1");
            }
            else
            {
                result.append("0");
            }
        }
        return result.toString();
    }

    public static String toBits(final int i)
    {
        final StringBuffer result = new StringBuffer();
        for (int order = 31; order >= 0; order--)
        {
            final int value = (int) Math.pow(2, order);
            if ((i & value) == value)
            {
                result.append("1");
            }
            else
            {
                result.append("0");
            }
        }
        return result.toString();
    }

    public static void testLong()
    {
        System.out.println(toBits((long) -1));
        long l = 0;
        for (int i = 0; i < 64; i++)
        {
            l++;
            l = (l << 1);
            System.out.println(toBits(l));
        }
    }

    public static int memorySizeOfLong()
    {
        long l = 1;
        int r = 0;
        while (l != 0)
        {
            l = (l << 1);
            r++;
        }
        return r;
    }
}
