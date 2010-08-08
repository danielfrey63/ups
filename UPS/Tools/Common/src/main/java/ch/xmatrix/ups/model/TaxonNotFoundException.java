package ch.xmatrix.ups.model;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 06.08.2010 16:21:47
 */
public class TaxonNotFoundException extends RuntimeException
{
    private final String taxon;

    public TaxonNotFoundException( final String message, final String taxon )
    {
        super( message );
        this.taxon = taxon;
    }

    public String getTaxon()
    {
        return taxon;
    }
}
