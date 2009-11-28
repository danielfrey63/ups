/*
 * Herbar CD-ROM version 2
 *
 * InconsistentModelException.java
 *
 * Created on 31.5.2002
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.gui.tax;

/**
 * Exception that is thrown when the MorphologyListModel has a invalid context. An invalid context is found when a taxon
 * is set, which is not part of the given siblings.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class InconsistentModelException extends RuntimeException
{
    InconsistentModelException()
    {
    }

    InconsistentModelException( final String message )
    {
        super( message );
    }
}
