package com.ethz.geobot.herbar.gui.exam;

import com.ethz.geobot.herbar.modeapi.AbstractModeAdapter;
import com.ethz.geobot.herbar.modeapi.ModeRegistration;

/*
 * Herbar CD-ROM version 2
 *
 * ExamMode.java
 *
 * Created on Feb 12, 2003 5:51:07 PM
 * Created by Daniel
 */

/**
 * Tests the students.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:13 $
 */
public class ExamMode extends AbstractModeAdapter {

    static {
        ModeRegistration.register(new ExamMode());
    }

    public ExamMode() {
        super(ExamPanel.class);
    }
}
