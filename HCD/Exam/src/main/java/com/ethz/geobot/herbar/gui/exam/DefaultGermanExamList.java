/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.gui.exam;

import ch.jfactory.math.RandomUtils;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGermanExamList implements ExamList
{
    private static final Logger LOG = LoggerFactory.getLogger( DefaultGermanExamList.class );

    private HerbarModel herbarModel;

    private Taxon[] examTaxa;

    public void setHerbarModel( final HerbarModel herbarModel )
    {
        this.herbarModel = herbarModel;
    }

    public Taxon[] getExamList( final int size )
    {
        final String[] strNiet = new String[]{
                "Gem�se-Kohl", "Einj�hrige Sonnenblume", "Riesen-B�renklau", "Runkelr�be", "Mais"
        };

        final String[][] strGroups = new String[][]{
                {"Farnpflanzen", "Nacktsamer"}, // 30
                {"Hahnenfussgew�chse", "Rosengew�chse", "Mohngew�chse"}, // 50
                {"S�ssgr�ser", "Sauergr�ser"}, // 80
                {"Binsengew�chse", "Liliengew�chse", "Schwertliliengew�chse", "Amaryllisgew�chse", "Orchideen",
                        "Aronstabgew�chse", "Rohrkolbengew�chse", "Wasserlinsengew�chse"}, // 40
                {"Nelkengew�chse", "Schmetterlingsbl�tler", "Storchschnabelgew�chse", "Kreuzbl�tler", "Steinbrechgew�chse",
                        "Dickblattgew�chse", "Doldengew�chse"}, // 50
                {"Primelgew�chse", "Enziangew�chse", "Nachtschattengew�chse", "Nachtschattengew�chse", "Lippenbl�tler",
                        "Rauhhaargew�chse"},
                {"R�tegew�chse", "Kardengew�chse", "Baldriangew�chse", "Glockenblumengew�chse", "Korbbl�tler"},
                {"Geissblattgew�chse", "Ulmengew�chse", "Ahorngew�chse", "Spindelstrauchgew�chse", "Kreuzdorngew�chse",
                        "Lindengew�chse", "Stechpalmengew�chse", "Hornstrauchgew�chse", "�lbaumgew�chse", "Buchenartige",
                        "Walnussgew�chse", "Weidengew�chse"},
                {"Bedecktsamer"},
                {"Bedecktsamer"}
        };

        if ( size != strGroups.length )
        {
            LOG.error( "Number of Taxon groups (" + strGroups.length + ") doesn't correspond to the number expected (" +
                    size + ")" );
        }

        final int length = strNiet.length;
        final List donts = new ArrayList();
        for ( int i = 0; i < length; i++ )
        {
            final Taxon tax = getTaxon( strNiet[i] );
            donts.add( tax );
        }

        // build final Taxon list
        examTaxa = new Taxon[size];

        // get a valid Taxon objects for each string given and add all Taxon objects on the last level for that Taxon
        // object an array
        final Level lastLevel = herbarModel.getLastLevel();
        final List[] vs = new ArrayList[size];
        // just to initzialize random generator
        RandomUtils.randomize( examTaxa );
        for ( int g = 0; g < size; g++ )
        {
            vs[g] = new ArrayList();
            for ( int s = 0; s < strGroups[g].length; s++ )
            {
                final String str = strGroups[g][s];
                final Taxon tax = getTaxon( str );
                final List taxa = Arrays.asList( tax.getAllChildTaxa( lastLevel ) );
                vs[g].addAll( taxa );
            }
            vs[g].removeAll( donts );
            vs[g].removeAll( Arrays.asList( examTaxa ) );
            // get randomly a taxon out of it
            final Taxon[] taxa = (Taxon[]) vs[g].toArray( new Taxon[0] );
            RandomUtils.randomizeNext( taxa );
            examTaxa[g] = taxa[0];
        }

        // randomize list
        RandomUtils.randomize( examTaxa );
        return examTaxa;
    }

    private Taxon getTaxon( final String str )
    {
        final Taxon tax = herbarModel.getTaxon( str );
        if ( tax == null )
        {
            LOG.error( "Taxon " + str + " not found" );
        }
        return tax;
    }

//    public static void main(String[] args) {
//        ch.jfactory.logging.LogUtils.init();
//        final javax.swing.JFrame f = new javax.swing.JFrame("Test Pr�fungsliste");
//        final javax.swing.JList l = new javax.swing.JList();
//        javax.swing.JButton b = new javax.swing.JButton("Neu berechnen");
//        javax.swing.JButton p = new javax.swing.JButton("List");
//        java.awt.Container c = f.getContentPane();
//        l.setBackground(c.getBackground());
//        HerbarModel model = com.ethz.geobot.herbar.Application.getInstance().getModel();
//        final DefaultGermanExamList defaultExamList = new DefaultGermanExamList(model);
//
//        c.setLayout(new java.awt.BorderLayout());
//        b.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent e) {
//                l.setModel(new javax.swing.AbstractListModel() {
//                    final Object[] listData = defaultExamList.getExamList(10);
//                    public int getSize() {
//                        return listData.length;
//                    }
//                    public Object getElementAt(int i) {
//                        return listData[i];
//                    }
//                });
//                f.pack();
//            }
//        });
//        p.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent e) {
//                StringBuffer buffer = new StringBuffer();
//                for (int i = 0; i < 1000; i++) {
//                    Object[] listData = defaultExamList.getExamList(10);
//                    for (int j = 0; j < listData.length; j++) {
//                        Object o = listData[j];
//                        buffer.append(o + (j+1==listData.length?"":", "));
//                    }
//                    buffer.append(System.getProperty("line.separator"));
//                }
//                System.out.println(buffer);
//            }
//        });
//
//        f.addWindowListener(new java.awt.event.WindowAdapter() {
//            public void windowClosing(java.awt.event.WindowEvent e) {
//                System.exit(0);
//            }
//        });
//
//        javax.swing.JPanel bPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 15));
//        javax.swing.JPanel lPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 15));
//        bPanel.add(b);
//        bPanel.add(p);
//        lPanel.add(l);
//        c.add(bPanel, java.awt.BorderLayout.WEST);
//        c.add(lPanel, java.awt.BorderLayout.CENTER);
//        f.setSize(400, 500);
//        f.setVisible(true);
//    }
}
