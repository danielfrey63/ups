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
package com.ethz.geobot.herbar;

import com.ethz.geobot.herbar.gui.AppHerbar;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uispec4j.Key;
import org.uispec4j.ListBox;
import org.uispec4j.TabGroup;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UIComponent;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

/**
 * Executes use cases and is meant to be used in conjunction with a code coverage tool.
 *
 * @author Daniel Frey 26.02.11 16:05
 */
public class TestCodeCoverageOnAppHerbar extends UISpecTestCase
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( TestCodeCoverageOnAppHerbar.class );

    private Window window;

    static
    {
        UISpec4J.init();
    }

    public void setUp()
    {
        // Startup with (1) selection of DB, (2) splash screen, and (3) main window
        WindowInterceptor
                .init( new Trigger()
                {
                    public void run() throws Exception
                    {
                        new AppHerbar( 1 );
                    }
                } )
                .process( new WindowHandler()
                {
                    @Override
                    public Trigger process( final Window window ) throws Exception
                    {
                        assertEquals( "Sprache", window.getTitle() );
                        return window.getButton( "Wissenschaftlich" ).triggerClick();
                    }
                } )
                .processTransientWindow()
                .process( new WindowHandler()
                {
                    public Trigger process( final Window window ) throws Exception
                    {
                        TestCodeCoverageOnAppHerbar.this.window = window;
                        return Trigger.DO_NOTHING;
                    }
                } )
                .run();
    }

    public void testAll()
    {
        selectMode( "Lernen", 0 );
        // Check title
        LOG.info( "--- Checking title" );
        assertTrue( window != null );
        assertTrue( window.getTitle().startsWith( "eBot" ) );
        LOG.info( "--- Checking for Cetraria islandica" );
        assertTrue( window.getUIComponents( new LabelChecker( "Cetraria islandica" ) ).length >= 1 );
        LOG.info( "--- Checking for Cladonia rangiferina" );
        window.getButton( "next" ).click();
        assertTrue( window.getUIComponents( new LabelChecker( "Cladonia rangiferina" ) ).length >= 1 );
        LOG.info( "--- Switching to ask sub mode" );
        final TabGroup tabs = window.getTabGroup( new NamedTabGroup( "Lehrgang", "Abfrage" ) );
        tabs.selectTab( "Abfrage" );
        tabs.selectedTabEquals( "Abfrage" );
        LOG.info( "--- Enter result" );
        final TextBox field = window.getTextBox( "Art:" );
        field.setText( "Cladonia rangiferina" );
        field.typeKey( Key.ENTER );
    }

    // -- Utilities

    private void selectMode( final String group, final int mode )
    {
        WindowInterceptor
                .init( window.getMenuBar().getMenu( "Einstellungen" ).getSubMenu( "Modus auswählen..." ).triggerClick() )
                .process( new SelectMode( group, mode ) )
                .run();
    }

    private static class ListInTabbedPaneComponentMatcher implements ComponentMatcher
    {
        public boolean matches( final Component component )
        {
            final Container superParent = component.getParent().getParent().getParent();
            if ( superParent instanceof JTabbedPane )
            {
                final JTabbedPane tabs = (JTabbedPane) superParent;
                if ( tabs.getSelectedComponent() == component.getParent().getParent() )
                {
                    return component instanceof JList;
                }
            }
            return false;
        }
    }

    private static class LabelChecker implements ComponentMatcher
    {
        private final String expectedLabel;

        public LabelChecker( final String expectedLabel )
        {
            this.expectedLabel = expectedLabel;
        }

        public boolean matches( final Component component )
        {
            return component != null
                    && component instanceof JLabel
                    && ( (JLabel) component ).getText() != null
                    && ( (JLabel) component ).getText().startsWith( expectedLabel );
        }
    }

    private static class NamedTabGroup implements ComponentMatcher
    {
        private final String[] tabNames;

        public NamedTabGroup( final String... tabNames )
        {
            this.tabNames = tabNames;
        }

        public boolean matches( final Component component )
        {
            if ( component != null && component instanceof JTabbedPane )
            {
                final JTabbedPane tabs = (JTabbedPane) component;
                if ( tabs.getTabCount() == tabNames.length )
                {
                    boolean allGood = true;
                    for ( int i = 0; i < tabs.getTabCount(); i++ )
                    {
                        allGood &= tabs.getTitleAt( i ).equals( tabNames[i] );
                    }
                    return allGood;
                }
            }
            return false;
        }
    }

    private static class SelectMode extends WindowHandler
    {
        private final String group;

        private final int mode;

        public SelectMode( final String group, final int mode )
        {
            this.group = group;
            this.mode = mode;
        }

        @Override
        public Trigger process( final Window window ) throws Exception
        {
            assertEquals( "Assistent zur Modus-Auswahl", window.getTitle() );
            final UIComponent[] tabs = window.getUIComponents( TabGroup.class );
            final TabGroup tab = (TabGroup) tabs[0];
            tab.selectTab( group );
            tab.selectedTabEquals( group );
            final UIComponent[] lists = window.getUIComponents( new ListInTabbedPaneComponentMatcher() );
            final ListBox list = (ListBox) lists[0];
            list.click( mode );
            if ( window.getButton( "OK" ).getAwtComponent().isEnabled() )
            {
                list.doubleClick( mode );
            }
            else
            {
                window.getButton( "Abbrechen" ).click();
            }
            return Trigger.DO_NOTHING;
        }
    }
}
