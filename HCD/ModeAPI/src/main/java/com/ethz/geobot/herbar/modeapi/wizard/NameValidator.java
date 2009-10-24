package com.ethz.geobot.herbar.modeapi.wizard;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public interface NameValidator {

    public String getValidName();

    public boolean isValidName(String newName);

    public String getInitialName();

    public void setInitialName(String initialName);
}
