/*
 * Herbar CD-ROM version 2
 *
 * DefaultExamList.java
 *
 * Created on Feb 12, 2003 9:07:13 PM
 * Created by Daniel
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

/**
 * Compiles a list of Taxon objects according to the Version 1.0 Herbar CD-ROM
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:13 $
 */
public class DefaultExamList implements ExamList
{
    private static final Logger LOG = LoggerFactory.getLogger( DefaultExamList.class );

    private HerbarModel herbarModel;

    private Taxon[] examTaxa;

    public void setHerbarModel( final HerbarModel herbarModel )
    {
        this.herbarModel = herbarModel;
    }

    public Taxon[] getExamList( final int size )
    {
        final String[] strNiet = new String[]{
                "Brassica oleracea", "Helianthus annuus", "Heracleum mantegazzianum", "Beta vulgaris", "Zea mays"
        };

        final String[][] strGroups = new String[][]{
                {"Pteridophyta", "Gymnospermae"}, // 30
                {"Ranunculaceae", "Rosaceae", "Papaveraceae"}, // 50
                {"Poaceae", "Cyperaceae"}, // 80
                {"Juncaceae", "Liliaceae", "Iridaceae", "Amaryllidaceae", "Orchidaceae", "Araceae", "Typhaceae",
                        "Lemnaceae"}, // 40
                {"Caryophyllaceae", "Fabaceae", "Geraniaceae", "Brassicaceae", "Saxifragaceae", "Crassulaceae",
                        "Apiaceae"}, // 50
                {"Primulaceae", "Gentianaceae", "Solanaceae", "Scrophulariaceae", "Lamiaceae", "Boraginaceae"},
                {"Rubiaceae", "Dipsacaceae", "Valerianaceae", "Campanulaceae", "Asteraceae"},
                {"Caprifoliaceae", "Ulmaceae", "Aceraceae", "Celastraceae", "Rhamnaceae", "Tiliaceae", "Aquifoliaceae",
                        "Cornaceae", "Oleaceae", "Fagales", "Juglandaceae", "Salicaceae"},
                {"Angiospermae"},
                {"Angiospermae"}
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
//        final javax.swing.JFrame f = new javax.swing.JFrame("Test Prüfungsliste");
//        final javax.swing.JList l = new javax.swing.JList();
//        javax.swing.JButton b = new javax.swing.JButton("Neu berechnen");
//        javax.swing.JButton p = new javax.swing.JButton("List");
//        java.awt.Container c = f.getContentPane();
//        l.setBackground(c.getBackground());
//        HerbarModel model = com.ethz.geobot.herbar.Application.getInstance().getModel();
//        final DefaultExamList defaultExamList = new DefaultExamList(model);
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
