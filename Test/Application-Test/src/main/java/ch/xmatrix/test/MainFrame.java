/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
package ch.xmatrix.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class MainFrame extends JFrame
{
    public MainFrame()
    {
        try
        {
            setTitle( "Test Application" );
            final JMenuBar menuBar = new JMenuBar();
            final JMenu sub = menuBar.add( new JMenu( "Test" ) );
            sub.add( new AbstractAction( "Test" )
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    System.out.println( "Hello Action" );
                }
            } );
            setJMenuBar( menuBar );

            final Container contentPane = this.getContentPane();
            contentPane.setLayout( new BorderLayout() );
            final JTree tree = new JTree();
            contentPane.add( new JScrollPane( tree ), BorderLayout.CENTER );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
