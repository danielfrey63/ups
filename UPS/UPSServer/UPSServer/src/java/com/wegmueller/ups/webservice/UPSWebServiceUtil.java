package com.wegmueller.ups.webservice;

import com.wegmueller.ups.lka.IAnmeldedaten;

import java.text.SimpleDateFormat;

/**
 * Created by: Thomas Wegmueller
 * Date: 29.09.2005,  11:22:46
 */
public class UPSWebServiceUtil {
    private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat tf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static void dumpAnmeldungen(IAnmeldedaten[] anm) {
        if (anm==null) {
            System.out.println("Keine Anmeldungen");
            return;
        };
        for (int k = 0; k < anm.length; k++) {
            System.out.println("\t"
                               + anm[k].getVorname()
                               + "\t"
                               + anm[k].getLkNummer()
                               + "\t"
                               + anm[k].getLkEinheitTitel()
                               + "\t"
                               + anm[k].getNachname()
                               + "\t"
                               + anm[k].getStudentennummer()
                               + "\t"
                               + anm[k].getStudiengang()
                               + "\t"
                               + anm[k].getPruefungsraum()
                               + "\t"
                               + df.format(anm[k].getPruefungsdatum().getTime())
                               + "\t"
                               + tf.format(anm[k].getPruefungsdatumVon().getTime())
                               + "\t"
                               + tf.format(anm[k].getPruefungsdatumBis().getTime())
                               + "\t"
            );
        }
    }
}
