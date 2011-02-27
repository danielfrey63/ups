package com.ethz.geobot.herbar;

import ch.jfactory.typemapper.Commands;
import com.ethz.geobot.herbar.gui.AppHerbar;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTabbedPane;
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
    static
    {
        UISpec4J.init();
    }

    private Window window;

    protected void setUp() throws Exception
    {
        super.setUp();
        // Startup with (1) selection of DB, (2) splash screen, and (3) main window
        WindowInterceptor
                .init( new Trigger()
                {
                    public void run() throws Exception
                    {
                        new AppHerbar();
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
        // Make sure lesson mode is active
        selectMode( "Lernen", 0 );
    }

    public void testMainWindow()
    {
        assertTrue( window != null );
        assertTrue( window.getTitle().startsWith( "Herbar CD-ROM" ) );
    }

    public void testLesson()
    {
        assertTrue( window.getUIComponents( new LabelChecker( "Cetraria islandica" ) ).length >= 1 );
    }

    public void testNext()
    {
        window.getButton( "next" ).click();
        assertTrue( window.getUIComponents( new LabelChecker( "Cladonia rangiferina" ) ).length >= 1 );
    }

    public void testSwitchToQuestions()
    {
        final TabGroup tabs = window.getTabGroup( new NamedTabGroup( "Lehrgang", "Abfrage" ) );
        tabs.selectTab( "Abfrage" );
        tabs.selectedTabEquals( "Abfrage" );
    }

    public void testEnterSpecies()
    {
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
