package com.wegmueller.ups.webservice;

import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;
import java.text.SimpleDateFormat;

/** Created by: Thomas Wegmueller Date: 29.09.2005,  11:22:46 */
public class UPSWebServiceUtil {

    private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    private static SimpleDateFormat tf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static void dumpAnmeldungen(final IAnmeldedaten[] anm) {
        if (anm == null) {
            System.out.println("Keine Anmeldungen");
            return;
        }
        for (int k = 0; k < anm.length; k++) {
            final IAnmeldedaten anmeldung = anm[k];
            dumpAnmeldung(anmeldung);
        }
    }

    public static void dumpAnmeldung(final IAnmeldedaten anmeldung) {
        System.out.println("\t\t\t"
                + anmeldung.getVorname()
                + "\t"
                + anmeldung.getLkNummer()
                + "\t"
                + anmeldung.getLkEinheitTitel()
                + "\t"
                + anmeldung.getNachname()
                + "\t"
                + anmeldung.getStudentennummer()
                + "\t"
                + anmeldung.getStudiengang()
                + "\t"
                + anmeldung.getPruefungsraum()
                + "\t"
                + df.format(anmeldung.getPruefungsdatum().getTime())
                + "\t"
                + tf.format(anmeldung.getPruefungsdatumVon().getTime())
                + "\t"
                + tf.format(anmeldung.getPruefungsdatumBis().getTime())
        );
    }

    public static void dumpPruefungen(final IPruefung[] pruefungen) {
        for (int i = 0; i < pruefungen.length; i++) {
            final IPruefung p = pruefungen[i];
            System.out.println("\t"
                    + p.getTitle()
                    + "\t"
                    + p.getLKNummer());
        }
    }
}
