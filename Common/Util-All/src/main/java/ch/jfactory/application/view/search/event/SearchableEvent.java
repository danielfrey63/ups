// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.06.2005 09:08:05
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb

package ch.jfactory.application.view.search.event;

import ch.jfactory.application.view.search.Searchable;
import java.awt.AWTEvent;

public class SearchableEvent extends AWTEvent
{
    private String _searchingText;

    private String _oldSearchingText;

    private String _matchingText;

    private Object _matchingObject;

    public static final int SEARCHABLE_FIRST = 2999;

    public static final int SEARCHABLE_LAST = 3005;

    public static final int SEARCHABLE_START = 2999;

    public static final int SEARCHABLE_END = 3000;

    public static final int SEARCHABLE_MATCH = 3002;

    public static final int SEARCHABLE_NOMATCH = 3003;

    public static final int SEARCHABLE_CHANGE = 3004;

    public SearchableEvent( final Searchable searchable, final int i )
    {
        super( searchable, i );
    }

    public SearchableEvent( final Object obj, final int i, final String s )
    {
        super( obj, i );
        _searchingText = s;
    }

    public SearchableEvent( final Object obj, final int i, final String s, final String s1 )
    {
        super( obj, i );
        _searchingText = s;
        _oldSearchingText = s1;
    }

    public SearchableEvent( final Object obj, final int i, final String s, final Object obj1, final String s1 )
    {
        super( obj, i );
        _searchingText = s;
        _matchingObject = obj1;
        _matchingText = s1;
    }

    public String paramString()
    {
        final String s;
        switch ( id )
        {
            case 2999:
                s = "SEARCHABLE_START: searchingText = \"" + _searchingText + "\"";
                break;

            case 3000:
                s = "SEARCHABLE_END";
                break;

            case 3002:
                s = "SEARCHABLE_MATCH: searchingText = \"" + _searchingText + "\" matchingText = \"" + _matchingText + "\"";
                break;

            case 3003:
                s = "SEARCHABLE_NOMATCH: searchingText = \"" + _searchingText + "\"";
                break;

            case 3004:
                s = "SEARCHABLE_CHANGE: searchingText = \"" + _searchingText + "\" oldSearchingText = \"" + _oldSearchingText + "\"";
                break;

            case 3001:
            default:
                s = "SEARCHABLE_UNKNOWN";
                break;
        }
        return s;
    }

    public Searchable getSearchable()
    {
        return ( source instanceof Searchable ) ? (Searchable) source : null;
    }

    public String getSearchingText()
    {
        return _searchingText;
    }

    public String getOldSearchingText()
    {
        return _oldSearchingText;
    }

    public String getMatchingText()
    {
        return _matchingText;
    }

    public Object getMatchingObject()
    {
        return _matchingObject;
    }
}