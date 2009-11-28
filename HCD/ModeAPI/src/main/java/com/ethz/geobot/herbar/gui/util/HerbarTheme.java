/*
 * Herbar CD-ROM version 2
 *
 * HerbarTheme.java
 *
 * Created on ??. ??? 2002, ??:??
 * Created by ???
 */
package com.ethz.geobot.herbar.gui.util;

import ch.jfactory.application.view.border.BevelDirection;
import ch.jfactory.application.view.border.ThinBevelBorder;
import ch.jfactory.resource.Strings;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class HerbarTheme extends DefaultMetalTheme
{
    public static final String FONTFACE = Strings.getString( "APPLICATION.FONT.FACE" );

    public static final int FONTSIZE = Integer.parseInt( Strings.getString( "APPLICATION.FONT.SIZE" ) );

    public static final HerbarTheme THEME = new HerbarTheme( FONTFACE, FONTSIZE );

    static final int CONTROL_TEXT_FONT = 0;

    static final int SYSTEM_TEXT_FONT = 1;

    static final int USER_TEXT_FONT = 2;

    static final int MENU_TEXT_FONT = 3;

    static final int WINDOW_TITLE_FONT = 4;

    static final int SUB_TEXT_FONT = 5;

    private final FontDelegate fontDelegate;

    private HerbarTheme( final String name, final int size )
    {
        fontDelegate = new FontDelegate( name, size );

        MetalLookAndFeel.setCurrentTheme( this );
        UIManager.put( "TabbedPane.contentBorderInsets", new Insets( 1, 1, 1, 1 ) );
        UIManager.put( "TabbedPane.shadow", UIManager.get( "TabbedPane.background" ) );

        UIManager.put( "Tree.background", getBackground2() );

        UIManager.put( "ScrollBar.border", BorderFactory.createEmptyBorder() );
        UIManager.put( "ScrollBar.viewportBorder", new BorderUIResource( new LineBorder( Color.green, 3 ) ) );
        UIManager.put( "ScrollPane.border", new ThinBevelBorder( BevelDirection.LOWERED ) );

        UIManager.put( "SplitPane.dividerSize", new Integer( Strings.getString( "APPLICATION.DIVIDER.SIZE" ) ) );
//        UIManager.put("SplitPane.border", new BorderUIResource(new LineBorder(Color.orange, 3)));

        UIManager.put( "OptionPane.yesButtonText", Strings.getString( "BUTTON.YES.TEXT" ) );
        UIManager.put( "OptionPane.noButtonText", Strings.getString( "BUTTON.NO.TEXT" ) );
        UIManager.put( "OptionPane.cancelButtonText", Strings.getString( "BUTTON.CANCEL.TEXT" ) );

//        UIManager.put("MenuBar.background", UIManager.getColor("MenuItem.selectionBackground"));
//        UIManager.put("Menu.background", UIManager.getColor("MenuItem.selectionBackground"));

        final Font font = new Font( FONTFACE, Font.PLAIN, FONTSIZE );
        final Enumeration keys = UIManager.getDefaults().keys();
        while ( keys.hasMoreElements() )
        {
            final Object key = keys.nextElement();
            final Object value = UIManager.get( key );
            if ( value instanceof FontUIResource )
            {
                UIManager.put( key, font );
            }
        }
    }

    public String getName()
    {
        return super.getName();
    }

    public FontUIResource getControlTextFont()
    {
        return getFont( CONTROL_TEXT_FONT );
    }

    public FontUIResource getSystemTextFont()
    {
        return getFont( SYSTEM_TEXT_FONT );
    }

    public FontUIResource getUserTextFont()
    {
        return getFont( USER_TEXT_FONT );
    }

    public FontUIResource getMenuTextFont()
    {
        return getFont( MENU_TEXT_FONT );
    }

    public FontUIResource getWindowTitleFont()
    {
        return getFont( WINDOW_TITLE_FONT );
    }

    public FontUIResource getSubTextFont()
    {
        return getFont( SUB_TEXT_FONT );
    }

    private FontUIResource getFont( final int key )
    {
        return fontDelegate.getFont( key );
    }

    protected ColorUIResource getPrimary1()
    {
        // borderMenu, title, labelText, statusbartext, borderTabs
        // dunkel
        return new ColorUIResource( 30, 40, 40 );
    }

    protected ColorUIResource getPrimary2()
    {
        // desktop; aktive menus (rollovers), borderButton onclick
        // mittel
        return new ColorUIResource( 142, 172, 172 );
    }

    protected ColorUIResource getPrimary3()
    {
        // jframe menubar, default ordner in tree
        // mittel
        return new ColorUIResource( 205, 215, 207 );
    }

    protected ColorUIResource getSecondary1()
    {
        // thin outer bordersTabs, Panels and Menu
        // dunkel
//        return new ColorUIResource(176, 203, 191);
        return new ColorUIResource( 100, 116, 116 );
    }

    protected ColorUIResource getSecondary2()
    {
        // linie unter menubar, Background buttonsOnClick, Tabs passiv
        // dunkel
//        return new ColorUIResource(182,201,192);
        return new ColorUIResource( 176, 203, 191 );
    }

    protected ColorUIResource getSecondary3()
    {
        // defaultGround Buttons, Panels, active Tabs, inner thin Border tabs,
        // panels
        // mittel bis hell
        return new ColorUIResource( 224, 236, 231 );
    }

    public static Color getBackground2()
    {
        return new ColorUIResource( 232, 241, 237 ); //UIManager.getColor("Tree.background");
    }

    private static class FontDelegate
    {
        /**
         * Styles for the fonts.
         */
        private static final int[] fontStyles = {
                Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN
        };

        /**
         * Sizes for the fonts.
         */
        private static final int[] fontSizes = {
                2, 2, 2, 2, 2, 0
        };

        private static final int[] defaultMapping = {
                CONTROL_TEXT_FONT, SYSTEM_TEXT_FONT,
                USER_TEXT_FONT, CONTROL_TEXT_FONT,
                CONTROL_TEXT_FONT, SUB_TEXT_FONT
        };

        FontUIResource fonts[];

        private final int size;

        private final String name;

        // menu and window are mapped to controlFont
        public FontDelegate( final String name, final int size )
        {
            fonts = new FontUIResource[6];
            this.name = name;
            this.size = size;
        }

        /**
         * Returns the ideal font name for the font identified by key.
         */
        String getDefaultFontName( final int key )
        {
            return name;
        }

        /**
         * Returns the ideal font size for the font identified by key.
         */
        int getDefaultFontSize( final int key )
        {
            return fontSizes[key] + size;
        }

        /**
         * Returns the ideal font style for the font identified by key.
         */
        static int getDefaultFontStyle( final int key )
        {
            return fontStyles[key];
        }

        public FontUIResource getFont( int type )
        {
            type = defaultMapping[type];
            if ( fonts[type] == null )
            {
                Font f = getPrivilegedFont( type );

                if ( f == null )
                {
                    f = new Font( getDefaultFontName( type ),
                            getDefaultFontStyle( type ),
                            getDefaultFontSize( type ) );
                }
                fonts[type] = new FontUIResource( f );
            }
            return fonts[type];
        }

        static String getDefaultPropertyName( final int key )
        {
            return defaultNames[key];
        }

        private static final String[] defaultNames = {
                "swing.plaf.metal.controlFont",
                "swing.plaf.metal.systemFont",
                "swing.plaf.metal.userFont",
                "swing.plaf.metal.controlFont",
                "swing.plaf.metal.controlFont",
                "swing.plaf.metal.smallFont"
        };

        /**
         * This is the same as invoking <code>Font.getFont(key)</code>, with the exception that it is wrapped inside a
         * <code>doPrivileged</code> call.
         */
        protected Font getPrivilegedFont( final int key )
        {
            return (Font) java.security.AccessController.doPrivileged( new java.security.PrivilegedAction()
            {
                public Object run()
                {
                    return Font.getFont( getDefaultPropertyName( key ) );
                }
            } );
        }
    }

}

// $Log: HerbarTheme.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.15  2004/08/31 22:10:16  daniel_frey
// Examlist loading working
//
// Revision 1.14  2004/04/25 13:56:42  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.13  2003/05/25 21:38:46  daniel_frey
// - Optimized imports
// - Replaced static access by proper class access instead of object access
//
// Revision 1.12  2003/04/13 21:51:15  daniel_frey
// - Separated status actions and display into toolbar and status bar
//
// Revision 1.11  2003/04/02 14:49:03  daniel_frey
// - Revised wizards
//
// Revision 1.10  2003/03/16 23:11:47  daniel_frey
// - New approach to GUI
//
// Revision 1.9  2003/03/16 08:53:01  daniel_frey
// - Exam mode with first shot of results
// - UI rolled partly back
//
// Revision 1.8  2003/03/04 17:21:14  daniel_frey
// - Introduced common model for ask and exam mode results
//
// Revision 1.7  2003/03/03 12:39:10  thomas_wegmueller
// *** empty log message ***
//
// Revision 1.6  2003/03/03 12:27:25  thomas_wegmueller
// Using HerbarTheme with standard font Tahoma
//
// Revision 1.5  2003/01/23 10:54:27  daniel_frey
// - Optimized imports
//
// Revision 1.4  2002/05/28 14:01:54  Thomas
// Added Fontface to be static variable, Theme to be singleton
//
// Revision 1.3  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//