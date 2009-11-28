/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.uec.master;

import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.domain.TaxonBased;
import org.apache.log4j.Logger;

/**
 * This controller handles the creation of objects.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/06 10:16:20 $
 */
public abstract class MasterDetailsFactory
{
    private static final Logger LOG = Logger.getLogger( MasterDetailsFactory.class );

    private static final boolean INFO = LOG.isInfoEnabled();

    private static final boolean DEBUG = LOG.isDebugEnabled();

    private SimpleModelList models;

    public void setModels( final SimpleModelList models )
    {
        this.models = models;
    }

    /**
     * Adds and creates a new TaxonBased.
     *
     * @return new TaxonBased model
     */
    public TaxonBased create()
    {
        final TaxonBased model = createInstance();
        if ( INFO )
        {
            LOG.info( "new " + model.toDebugString() );
        }
        models.add( model );
        return model;
    }

    /**
     * Adds and returns a copied TaxonBased.
     *
     * @param orig the orignial to be casted
     * @return copy of TaxonBased
     */
    public TaxonBased copy( final TaxonBased orig )
    {
        if ( DEBUG )
        {
            LOG.debug( "copying " + orig.toDebugString() );
        }
        final TaxonBased copy = createCopy( orig );
        if ( INFO )
        {
            LOG.info( "copy " + copy.toDebugString() );
        }
        models.add( copy );
        return copy;
    }

    /**
     * Deletes the given TaxonBased.
     *
     * @param model
     */
    public void delete( final TaxonBased model )
    {
        assert model != null : "taxon based model shall not be null";
        if ( INFO )
        {
            LOG.info( "remove " + model.toDebugString() );
        }
        models.remove( model );
    }

    protected abstract TaxonBased createCopy( TaxonBased orig );

    protected abstract TaxonBased createInstance();
}
