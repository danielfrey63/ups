/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Traks Taxon objects and answers of student.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:14 $
 */
public class ExamStateModel implements Cursor
{
    private static final Logger LOG = LoggerFactory.getLogger( ExamStateModel.class );

    private final List focusListeners = new ArrayList();

    private final HerbarModel model;

    private DefaultCursor cursor;

    public ExamStateModel( final HerbarModel model, final int size )
    {
        this.model = model;
        init( size );
    }

    private void init( final int size )
    {
        ExamList exam = null;
        try
        {
            final Class examClass = Class.forName( System.getProperty( "herbar.exam.default_list" ) );
            exam = (ExamList) examClass.newInstance();
        }
        catch ( Exception e )
        {
            LOG.error( "Error occured", e );
        }
        exam.setHerbarModel( model );
        final Taxon[] examList = exam.getExamList( size );
        cursor = new DefaultCursor( examList );
    }

    public Taxon getNextTaxon()
    {
        return (Taxon) cursor.next();
    }

    public Taxon getPreviousTaxon()
    {
        return (Taxon) cursor.previous();
    }

    public Object next()
    {
        final Taxon oldTaxon = getCurrentTaxon();
        final Taxon newTaxon = getNextTaxon();
        fireTaxFocusChangedEvent( oldTaxon, newTaxon );
        return newTaxon;
    }

    public Object previous()
    {
        final Taxon oldTaxon = getCurrentTaxon();
        final Taxon newTaxon = getPreviousTaxon();
        fireTaxFocusChangedEvent( oldTaxon, newTaxon );
        return newTaxon;
    }

    public boolean hasNext()
    {
        return cursor.hasNext();
    }

    public boolean hasPrevious()
    {
        return cursor.hasPrevious();
    }

    public int getCurrentIndex()
    {
        return cursor.getCurrentIndex();
    }

    public int getSize()
    {
        return cursor.getSize();
    }

    public Taxon getCurrentTaxon()
    {
        return (Taxon) cursor.getCurrent();
    }

    public Object getCurrent()
    {
        return getCurrentTaxon();
    }

    public void setCurrent( final Object obj )
    {
        if ( obj instanceof Taxon )
        {
            setCurrent( (Taxon) obj );
        }
        else
        {
            LOG.error( "Not a Taxon: " + obj );
        }
    }

    public void setCurrent( final Taxon taxon )
    {
        final Taxon oldFocus = getCurrentTaxon();
        cursor.setCurrent( taxon );
        fireTaxFocusChangedEvent( oldFocus, taxon );
    }

    public boolean isEmpty()
    {
        return cursor.isEmpty();
    }

    public Iterator getIterator()
    {
        return cursor.getIterator();
    }

    public void addTaxFocusListener( final TaxFocusListener l )
    {
        focusListeners.add( l );
    }

    public void removeTaxFocusListener( final TaxFocusListener l )
    {
        focusListeners.remove( l );
    }

    private void fireTaxFocusChangedEvent( final Taxon oldTaxon, final Taxon newTaxon )
    {
        for ( final Object focusListener : focusListeners )
        {
            final TaxFocusListener taxFocusListener = (TaxFocusListener) focusListener;
            taxFocusListener.taxFocusChanged( oldTaxon, newTaxon );
        }
    }

    public void reset( final int size )
    {
        init( size );
    }
}
