/*
 * Herbar CD-ROM version 2
 *
 * ModeActivationPanel.java
 *
 * Created on Feb 12, 2003 8:51:40 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.modeapi;

import javax.swing.JPanel;

/**
 * Abstract JPanel that implements dummies for the ModeActivation interface.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public abstract class ModeActivationPanel extends JPanel implements ModeActivation {

    /**
     * @see ModeActivation#activate
     */
    public void activate() {
    }

    /**
     * @see ModeActivation#deactivate
     */
    public void deactivate() {
    }

    /**
     * @see ModeActivation#queryDeactivate
     */
    public boolean queryDeactivate() {
        return true;
    }
}
