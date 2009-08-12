package com.wegmueller.ups.storage;

import com.wegmueller.ups.storage.beans.PruefungsListe;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import com.wegmueller.ups.lka.IAnmeldedaten;
import org.hibernate.HibernateException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by: Thomas Wegmueller
 * Date: 03.10.2005,  22:21:58
 */
public class DumpUtils {
    public static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    public static SimpleDateFormat tf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static void dumpStringArray(final String[] stu) throws StorageException {
        for (int i = 0; i < stu.length; i++) {
            System.out.println(stu[i]);
        }
    }

    public static void dumpTest(final IAnmeldedaten[] list, final String[] stu) throws StorageException {
        try {
            final HashSet set = new HashSet();
            for (int i = 0; i < stu.length; i++) {
                set.add(stu[i]);
            }
            for (int k = 0; k < list.length; k++) {
                final IAnmeldedaten anm = list[k];
                final boolean eingereicht = set.contains(anm.getStudentennummer());
                set.remove(anm.getStudentennummer());
                System.out.println("\t"
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
                                   + df.format(anm.getPruefungsdatum().getTime())
                                   + "\t"
                                   + tf.format(anm.getPruefungsdatumVon().getTime())
                                   + "\t"
                                   + tf.format(anm.getPruefungsdatumBis().getTime())
                                   + "\t"
                                   + (eingereicht)
                );
            }
            /*
            for (Iterator it = set.iterator(); it.hasNext();) {
                dumpStudi((String) it.next());

            }
            */
        } catch (HibernateException e) {
            throw new StorageException(e);
        }

    }



    public static void dumpCalendars(final Calendar[] pruefungsZeiten) {
        for (int i=0;i<pruefungsZeiten.length;i++) {
            System.out.println(df.format(pruefungsZeiten[i].getTime()));
        }
    }

    /*
    public static void dumpStudi(List list) throws StorageException {
        try {
            if (list.size() == 0) {
                System.out.println("not found");
            } else {
                if (list.size() > 1) {
                    System.out.println("multiple");
                    return;
                }
                PruefungsListe pl = (PruefungsListe) list.get(0);
                Properties p = new Properties();
                p.load(new ByteArrayInputStream(pl.getProperties().getBytes()));
                //System.out.println("********"+snr);

                //p.list(System.out);
                System.out.print("" + p.getProperty("givenName"));
                System.out.print("\t" + p.getProperty("sn"));
                System.out.print("\t" + p.getProperty("carLicense"));
                System.out.print("\t" + p.getProperty("mail"));
                System.out.println("");

            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        } catch (IOException e) {
            throw new StorageException(e);
        }

    }
    */
}
