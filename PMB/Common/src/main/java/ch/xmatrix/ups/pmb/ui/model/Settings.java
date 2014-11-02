/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.pmb.ui.model;

import ch.xmatrix.ups.pmb.domain.SpeciesEntry;
import ch.xmatrix.ups.pmb.list.ListCellEditor;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.AbstractCellEditor;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:48 $
 */
public class Settings
{
    private String fileIncludePatterns;

    private String fileExcludePatterns;

    private String splitPattern;

    private String splitExcludePatterns;

    private String splitExclusivePatterns;

    private String splitRandomPatterns;

    private String defaultPattern;

    private String speciesExcludePatterns;

    private Boolean showSettingsOnStartup;

    private List<NamedValue> movePaths;

    private List<OrderedNamedValue> orderTokens;

    public static final String SEPARATOR = ";";

    private static final String PREFS_USER_NODE = "ups/pmb";

    private static final String PREFS_FILE_INCLUSIVE_PATTERNS = "fileIncludePatterns";

    private static final String PREFS_FILE_EXCLUSIVE_PATTERNS = "fileExcludePatterns";

    private static final String PREFS_SPLIT_CHARACTERS = "splitPattern";

    private static final String PREFS_SPLIT_RANDOM_PATTERNS = "splitRandomPatterns";

    private static final String PREFS_SPLIT_EXCLUSIVE_PATTERNS = "splitExclusivePatterns";

    private static final String PREFS_SPLIT_EXCLUDE_PATTERNS = "splitExcludePatterns";

    private static final String PREFS_DEFAULT_CHARACTER = "defaultPattern";

    private static final String PREFS_SPECIES_EXCLUDE_PATTERNS = "speciesExcludePatterns";

    private static final String PREFS_SHOW_SETTINGS_ON_STARTUP = "showSettingsOnStartup";

    private static final String PREFS_MOVE_PATHS = "movePaths";

    private static final String PREFS_ORDER_TOKENS = "orderTokens";

    public String getFileIncludePatterns()
    {
        return fileIncludePatterns;
    }

    public void setFileIncludePatterns( final String fileIncludePatterns )
    {
        this.fileIncludePatterns = fileIncludePatterns;
    }

    public String getFileExcludePatterns()
    {
        return fileExcludePatterns;
    }

    public void setFileExcludePatterns( final String fileExcludePatterns )
    {
        this.fileExcludePatterns = fileExcludePatterns;
    }

    public String getSplitCharacter()
    {
        return splitPattern;
    }

    public void setSplitPattern( final String splitPattern )
    {
        this.splitPattern = splitPattern;
    }

    public String getSplitExclusivePatterns()
    {
        return splitExclusivePatterns;
    }

    public void setSplitExclusivePatterns( final String splitExclusivePatterns )
    {
        this.splitExclusivePatterns = splitExclusivePatterns;
    }

    public String getSplitRandomPatterns()
    {
        return splitRandomPatterns;
    }

    public void setSplitRandomPatterns( final String splitRandomPatterns )
    {
        this.splitRandomPatterns = splitRandomPatterns;
    }

    public String getSplitExcludePatterns()
    {
        return splitExcludePatterns;
    }

    public void setSplitExcludePatterns( final String splitExcludePatterns )
    {
        this.splitExcludePatterns = splitExcludePatterns;
    }

    public String getDefaultPattern()
    {
        return defaultPattern;
    }

    public void setDefaultPattern( final String defaultPattern )
    {
        this.defaultPattern = defaultPattern;
    }

    public String getSpeciesExcludePatterns()
    {
        return speciesExcludePatterns;
    }

    public void setSpeciesExcludePatterns( final String speciesExcludePatterns )
    {
        this.speciesExcludePatterns = speciesExcludePatterns;
    }

    public Boolean getShowSettingsOnStartup()
    {
        return showSettingsOnStartup;
    }

    public void setShowSettingsOnStartup( final Boolean showSettingsOnStartup )
    {
        this.showSettingsOnStartup = showSettingsOnStartup;
    }

    public List<NamedValue> getMovePaths()
    {
        return movePaths;
    }

    public void setMovePaths( final List<NamedValue> movePaths )
    {
        this.movePaths = movePaths;
    }

    public List<OrderedNamedValue> getOrderTokens()
    {
        return orderTokens;
    }

    public void setOrderTokens( final List<OrderedNamedValue> orderTokens )
    {
        this.orderTokens = orderTokens;
    }

    //-- Utility methods

    public String getActivePicturePath()
    {
        for ( final NamedValue movePath : movePaths )
        {
            if ( movePath.getName().startsWith( "+" ) )
            {
                return movePath.getValue();
            }
        }
        return "";
    }

    public void setActivePicturePath( final String value )
    {
        boolean found = false;
        for ( final NamedValue movePath : movePaths )
        {
            if ( movePath.getName().startsWith( "+" ) )
            {
                movePath.setValue( value );
                found = true;
            }
        }
        if ( !found )
        {
            movePaths.add( new NamedValue( "+Default", value ) );
        }
    }

    /**
     * Returns whether the given token is a valid hierarchic token.
     *
     * @param token the token to check
     * @return whether valid
     */
    public boolean isValidHierarchicToken( final String token )
    {
        final String[] tokens = splitExcludePatterns.split( SEPARATOR );
        boolean passed = true;
        for ( final String current : tokens )
        {
            passed &= !token.matches( current );
        }
        return passed;
    }

    /**
     * Returns whether the given hiararcic token is a default for another token.
     *
     * @param name   file name
     * @param string the string to investigate
     * @return whether default or not
     */
    public boolean isDefault( final String name, final String string )
    {
        final String[] splitters = getSplitCharacter().split( "\\|" );
        boolean match = false;
        for ( final String splitter : splitters )
        {
            final String defaultToken = string + defaultPattern + splitter + ".*";
            match |= name.matches( "^" + defaultToken + "|" + ".*" + splitter + defaultToken );
        }
        return match;
    }

    /**
     * Used to filter file names. Checks whether the given string matches at least on {@link
     * #PREFS_FILE_INCLUSIVE_PATTERNS} and doesn't match any of the {@link #PREFS_FILE_EXCLUSIVE_PATTERNS}.
     *
     * @param string the string to check
     * @return whether the test passes
     */
    public boolean isAllowedFileName( final String string )
    {
        final String[] inclusivePattern = fileIncludePatterns.split( SEPARATOR );
        final String[] exclusivePattern = fileExcludePatterns.split( SEPARATOR );
        boolean matches = false;
        for ( final String s : inclusivePattern )
        {
            matches = string.matches( s );
            if ( matches )
            {
                break;
            }
        }
        for ( final String s : exclusivePattern )
        {
            matches &= !string.matches( s );
        }
        return matches;
    }

    /**
     * Returns the token without the default character
     *
     * @param token the string to match
     * @return null if it doesn't or the token without the default character
     */
    public String getCleanedUpHierarchicToken( final String token )
    {
        return token.replaceAll( defaultPattern, "" );
    }

    /**
     * See {@link #getCleanedSpeciesName(String)}
     *
     * @param speciesEntry the species as SpeciesEntry
     * @return the string without the exclude strings
     */
    public String getCleanedSpeciesName( final SpeciesEntry speciesEntry )
    {
        final String path = speciesEntry.getPath();
        return getCleanedSpeciesName( path.substring( path.lastIndexOf( "/" ) + 1 ) );
    }

    /**
     * Returns the species name without the parts defined as regular expressions in {@link
     * #setSpeciesExcludePatterns(String)}. I.e. if <cdoe>^x</code> would have been defined in {@link
     * #setSpeciesExcludePatterns(String)}, <code>xArtemisia vulgaris</code> would translate to <code>Artemisia
     * vulgaris</code>.
     *
     * @param speciesName the name of the species
     * @return the string without the exclude strings
     */
    public String getCleanedSpeciesName( final String speciesName )
    {
        final String[] exludePatterns = speciesExcludePatterns.split( SEPARATOR );
        for ( final String pattern : exludePatterns )
        {
            if ( Pattern.compile( pattern ).matcher( speciesName ).find() )
            {
                return speciesName.replaceFirst( pattern, "" );
            }
        }
        return speciesName;
    }

    /**
     * Returns an array of {@link #isValidHierarchicToken(String) valid},  {@link #getCleanedUpHierarchicToken(String)
     * cleaned up} tokens which build up the navigation hierarchy.
     *
     * @param name the name to extract the hierarchic tokens from
     * @return the array with the tokens
     */
    public List<String> getHierarchicTokens( final String name )
    {
        final String[] strings = name.split( getSplitCharacter() );
        final List<String> result = new ArrayList<String>();
        for ( final String string : strings )
        {
            if ( isValidHierarchicToken( string ) )
            {
                result.add( getCleanedUpHierarchicToken( string ) );
            }
        }
        return result;
    }

    public int getSortedPosition( final String token )
    {
        int i = 0;
        for ( final OrderedNamedValue orderToken : orderTokens )
        {
            final String pattern = orderToken.getNamedValue().getValue();
            if ( token.matches( pattern ) )
            {
                return orderToken.getOrder();
            }
            else
            {
                i++;
            }
        }
        return i;
    }

    public void loadSettings()
    {
        final Preferences prefs = Preferences.userRoot().node( PREFS_USER_NODE );
        setFileIncludePatterns( prefs.get( PREFS_FILE_INCLUSIVE_PATTERNS, "^.*\\.JPG$;^.*\\.jpg$" ) );
        setFileExcludePatterns( prefs.get( PREFS_FILE_EXCLUSIVE_PATTERNS, "^PICT.*" ) );
        setSplitPattern( prefs.get( PREFS_SPLIT_CHARACTERS, "\\.| \\(" ) );
        setSplitRandomPatterns( prefs.get( PREFS_SPLIT_RANDOM_PATTERNS, "^[0-9]+$" ) );
        setSplitExclusivePatterns( prefs.get( PREFS_SPLIT_EXCLUSIVE_PATTERNS, "^[a-zA-Z]$" ) );
        setSplitExcludePatterns( prefs.get( PREFS_SPLIT_EXCLUDE_PATTERNS, "^[0-9]+$;^jpg$;^JPG$;^$;^.*\\)$;^[a-zA-Z]$" ) );
        setDefaultPattern( prefs.get( PREFS_DEFAULT_CHARACTER, "\\+" ) );
        setSpeciesExcludePatterns( prefs.get( PREFS_SPECIES_EXCLUDE_PATTERNS, "^x;^xy;^z" ) );
        setShowSettingsOnStartup( prefs.getBoolean( PREFS_SHOW_SETTINGS_ON_STARTUP, Boolean.TRUE ) );

        final String[] movePathStrings = prefs.get( PREFS_MOVE_PATHS, "Löschen|C:/Trash" ).split( "\\|" );
        movePaths = new ArrayList<NamedValue>( movePathStrings.length / 2 );
        for ( int i = 0; i < movePathStrings.length; i++ )
        {
            final String name = movePathStrings[i++];
            final String path = movePathStrings[i];
            movePaths.add( new NamedValue( name, path ) );
        }

        final String[] orderTokenStrings = prefs.get( PREFS_ORDER_TOKENS, "Habitus|^Habitus$|1|" +
                "Oberseite|^Oberseite$|2|" +
                "Unterseite|^Unterseite$|3|" +
                "Längsschnitt|^Längsschnitt$|4|" +
                "Querschnitt|^Querschnitt$|5|" +
                "Herbar|^Herbar$|7|" +
                "Rest|^.+|6" ).split( "\\|" );
        orderTokens = new ArrayList<OrderedNamedValue>( orderTokenStrings.length / 3 );
        for ( int i = 0; i < orderTokenStrings.length; i++ )
        {
            final String name = orderTokenStrings[i++];
            final String pattern = orderTokenStrings[i++];
            orderTokens.add( new OrderedNamedValue(
                    new NamedValue( name, pattern ),
                    Integer.parseInt( orderTokenStrings[i] ) ) );
        }
    }

    public void storeSettings()
    {
        final Preferences prefs = Preferences.userRoot().node( PREFS_USER_NODE );
        prefs.put( PREFS_FILE_INCLUSIVE_PATTERNS, getFileIncludePatterns() );
        prefs.put( PREFS_FILE_EXCLUSIVE_PATTERNS, getFileExcludePatterns() );
        prefs.put( PREFS_SPLIT_CHARACTERS, getSplitCharacter() );
        prefs.put( PREFS_SPLIT_RANDOM_PATTERNS, getSplitRandomPatterns() );
        prefs.put( PREFS_SPLIT_EXCLUSIVE_PATTERNS, getSplitExclusivePatterns() );
        prefs.put( PREFS_SPLIT_EXCLUDE_PATTERNS, getSplitExcludePatterns() );
        prefs.put( PREFS_DEFAULT_CHARACTER, getDefaultPattern() );
        prefs.put( PREFS_SPECIES_EXCLUDE_PATTERNS, getSpeciesExcludePatterns() );
        prefs.putBoolean( PREFS_SHOW_SETTINGS_ON_STARTUP, getShowSettingsOnStartup() );

        final StringBuilder movePathStrings = new StringBuilder();
        for ( final NamedValue namedValue : movePaths )
        {
            if ( movePathStrings.length() > 0 )
            {
                movePathStrings.append( "|" );
            }
            movePathStrings.append( namedValue.name ).append( "|" ).append( namedValue.value );
        }
        prefs.put( PREFS_MOVE_PATHS, movePathStrings.toString() );

        final StringBuilder orderTokenStrings = new StringBuilder();
        for ( final OrderedNamedValue orderToken : orderTokens )
        {
            if ( orderTokenStrings.length() > 0 )
            {
                orderTokenStrings.append( "|" );
            }
            orderTokenStrings.append( orderToken.getNamedValue().name ).append( "|" ).append( orderToken.getNamedValue().value );
        }
        prefs.put( PREFS_ORDER_TOKENS, orderTokenStrings.toString() );
    }

    public static class NamedValue
    {
        private String name;

        private String value;

        public NamedValue( final String name, final String value )
        {
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public void setName( final String name )
        {
            this.name = name;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue( final String value )
        {
            this.value = value;
        }

        public String toString()
        {
            return name + "=" + value;
        }
    }

    public static class NamedValueCellEditor extends AbstractCellEditor implements ListCellEditor
    {
        private static final String SEPARATOR = "=";

        private final JTextField editor = new JTextField();

        public Object getCellEditorValue()
        {
            final String text = editor.getText();
            final String[] parts = text.split( SEPARATOR );
            return new NamedValue( parts[0], parts[1] );
        }

        public Component getListCellEditorComponent( final JList list, final Object value, final boolean isSelected, final int index )
        {
            final NamedValue namedValue = (NamedValue) value;
            editor.setText( namedValue.getName() + SEPARATOR + namedValue.getValue() );
            return editor;
        }
    }

    public static class OrderedNamedValue
    {
        private final NamedValue namedValue;
        private final int order;

        public OrderedNamedValue( final NamedValue namedValue, final int order )
        {
            this.namedValue = namedValue;
            this.order = order;
        }

        public NamedValue getNamedValue()
        {
            return namedValue;
        }

        public int getOrder()
        {
            return order;
        }
    }
}
