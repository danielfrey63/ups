package com.ethz.geobot.herbar.modeapi;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class ModeInfo {

    private String description;
    private String modeClass;
    private String modeGroup;
    private String modeName;
    private String icon;
    private String disabledIcon;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisabledIcon() {
        return disabledIcon;
    }

    public void setDisabledIcon(String disabledIcon) {
        this.disabledIcon = disabledIcon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getModeClass() {
        return modeClass;
    }

    public void setModeClass(String modeClass) {
        this.modeClass = modeClass;
    }

    public String getModeGroup() {
        return modeGroup;
    }

    public void setModeGroup(String modeGroup) {
        this.modeGroup = modeGroup;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }
}
