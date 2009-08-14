package ch.jfactory.application;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class SystemUtil
{

    public static QuitHandler EXIT = new QuitHandler()
    {
        public void exit(final int i)
        {
            System.exit(i);
        }
    };

    public interface QuitHandler
    {
        public void exit(int i);
    }
}
