package com.ethz.geobot.herbar.gui.exam;

import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;

/*
 * Herbar CD-ROM version 2
 *
 * ExamList.java
 *
 * Created on Feb 12, 2003 9:07:21 PM
 * Created by Daniel
 */

public interface ExamList {

    public Taxon[] getExamList(int size);

    public void setHerbarModel(HerbarModel herbarModel);
}
