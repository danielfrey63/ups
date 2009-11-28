package com.wegmueller.ups.storage;

import com.wegmueller.ups.lka.IAnmeldedaten;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import org.hibernate.HibernateException;

/**
 * Created by: Thomas Wegmueller Date: 03.10.2005,  22:21:58
 */
public class DumpUtils
{
    public static SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );

    public static SimpleDateFormat tf = new SimpleDateFormat( "dd.MM.yyyy HH:mm" );

    public static void dumpStringArray( final String[] stu ) throws StorageException
    {
        for ( final String s : stu )
        {
            System.out.println( s );
        }
    }

    public static void dumpTest( final IAnmeldedaten[] list, final String[] stu ) throws StorageException
    {
        try
        {
            final HashSet<String> set = new HashSet<String>();
            set.addAll( Arrays.asList( stu ) );
            for ( final IAnmeldedaten anm : list )
            {
                final boolean eingereicht = set.contains( anm.getStudentennummer() );
                set.remove( anm.getStudentennummer() );
                System.out.println( "\t"
                        + anm.getVorname()
                        + "\t"
                        + anm.getNachname()
                        + "\t"
                        + anm.getStudentennummer()
                        + "\t"
                        + anm.getEmail()
                        + "\t"
                        + anm.getStudiengang()
                        + "\t"
                        + anm.getPruefungsraum()
                        + "\t"
                        + df.format( anm.getPruefungsdatum().getTime() )
                        + "\t"
                        + tf.format( anm.getPruefungsdatumVon().getTime() )
                        + "\t"
                        + tf.format( anm.getPruefungsdatumBis().getTime() )
                        + "\t"
                        + ( eingereicht )
                );
            }
            /*
            for (Iterator it = set.iterator(); it.hasNext();) {
                dumpStudi((String) it.next());

            }
            */
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }

    }

    public static void dumpCalendars( final Calendar[] pruefungsZeiten )
    {
        for ( final Calendar pruefungsZeit : pruefungsZeiten )
        {
            System.out.println( df.format( pruefungsZeit.getTime() ) );
        }
    }
}
