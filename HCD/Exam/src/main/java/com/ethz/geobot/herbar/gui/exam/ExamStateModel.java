/*
 * Herbar CD-ROM version 2
 *
 * ExamStateModel.java
 *
 * Created on Feb 12, 2003 6:47:43 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui.exam;

import ch.jfactory.collection.cursor.Cursor;
import ch.jfactory.collection.cursor.DefaultCursor;
import com.ethz.geobot.herbar.modeapi.state.TaxFocusListener;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Category;

/**
 * Traks Taxon objects and answers of student.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:14 $
 */
public class ExamStateModel implements Cursor {

    private static final Category cat = Category.getInstance(ExamStateModel.class);

    private List focusListeners = new ArrayList();
    private HerbarModel model;
    private DefaultCursor cursor;

    public ExamStateModel(HerbarModel model, int size) {
        this.model = model;
        init(size);
    }

    private void init(int size) {
        ExamList exam = null;
        try {
            Class examClass = Class.forName(System.getProperty("herbar.exam.defaultlist"));
            exam = (ExamList) examClass.newInstance();
        }
        catch (Exception e) {
            cat.error("Error occured", e);
        }
        exam.setHerbarModel(model);
        Taxon[] examList = exam.getExamList(size);
        cursor = new DefaultCursor(examList);
    }

    public Taxon getNextTaxon() {
        return (Taxon) cursor.next();
    }

    public Taxon getPreviousTaxon() {
        return (Taxon) cursor.previous();
    }

    public Object next() {
        Taxon oldTaxon = getCurrentTaxon();
        Taxon newTaxon = getNextTaxon();
        fireTaxFocusChangedEvent(oldTaxon, newTaxon);
        return newTaxon;
    }

    public Object previous() {
        Taxon oldTaxon = getCurrentTaxon();
        Taxon newTaxon = getPreviousTaxon();
        fireTaxFocusChangedEvent(oldTaxon, newTaxon);
        return newTaxon;
    }

    public boolean hasNext() {
        return cursor.hasNext();
    }

    public boolean hasPrevious() {
        return cursor.hasPrevious();
    }

    public int getCurrentIndex() {
        return cursor.getCurrentIndex();
    }

    public int getSize() {
        return cursor.getSize();
    }

    public Taxon getCurrentTaxon() {
        return (Taxon) cursor.getCurrent();
    }

    public Object getCurrent() {
        return getCurrentTaxon();
    }

    public void setCurrent(Object obj) {
        if (obj instanceof Taxon) {
            setCurrent((Taxon) obj);
        }
        else {
            cat.error("Not a Taxon: " + obj);
        }
    }

    public void setCurrent(Taxon taxon) {
        Taxon oldFocus = getCurrentTaxon();
        cursor.setCurrent(taxon);
        fireTaxFocusChangedEvent(oldFocus, taxon);
    }

    public boolean isEmpty() {
        return cursor.isEmpty();
    }

    public Iterator getIterator() {
        return cursor.getIterator();
    }

    public void addTaxFocusListener(TaxFocusListener l) {
        focusListeners.add(l);
    }

    public void removeTaxFocusListener(TaxFocusListener l) {
        focusListeners.remove(l);
    }

    private void fireTaxFocusChangedEvent(Taxon oldTaxon, Taxon newTaxon) {
        for (Iterator iterator = focusListeners.iterator(); iterator.hasNext();) {
            TaxFocusListener taxFocusListener = (TaxFocusListener) iterator.next();
            taxFocusListener.taxFocusChanged(oldTaxon, newTaxon);
        }
    }

    public void reset(int size) {
        init(size);
    }
}
