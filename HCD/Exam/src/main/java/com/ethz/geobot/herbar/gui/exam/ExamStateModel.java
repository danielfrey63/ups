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
import org.apache.log4j.Logger;

/**
 * Traks Taxon objects and answers of student.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:14 $
 */
public class ExamStateModel implements Cursor
{
    private static final Logger LOG = Logger.getLogger( ExamStateModel.class );

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
            final Class examClass = Class.forName( System.getProperty( "herbar.exam.defaultlist" ) );
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
