package com.ethz.geobot.herbar.game.util;


/**
 * interface must be implemented from all question-panels, -windows.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:06 $
 */
public interface Question {

    public void firstQuestion();

    public void nextQuestion();

    public void lastQuestion();
}
