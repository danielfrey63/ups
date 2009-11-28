package ch.jfactory.lang;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class ReferencableBool
{
    private boolean bool;

    public ReferencableBool( final boolean bool )
    {
        this.bool = bool;
    }

    public boolean isTrue()
    {
        return bool;
    }

    public void setBool( final boolean bool )
    {
        this.bool = bool;
    }
}