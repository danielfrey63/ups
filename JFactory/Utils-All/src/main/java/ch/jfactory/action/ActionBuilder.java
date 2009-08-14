/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.jfactory.action;

import ch.jfactory.resource.ImageLocator;
import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.uif.action.ActionManager;
import com.jgoodies.uif.action.ToggleAction;
import com.jgoodies.uif.builder.ToolBarBuilder;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 * Factory to build toolbars and menubars.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public final class ActionBuilder {
    /**
     * The postfix to lookup the actions list.
     */
    private static final String POSTFIX_ACTIONS = ".actions";

    /**
     * The postfix to lookup the long description.
     */
    private static final String POSTFIX_DESCRIPTION_LONG = ".description.long";

    /**
     * The postfix to lookup the enabeld icon.
     */
    private static final String POSTFIX_ICON_ENABLED = ".icon.enabled";

    /**
     * The postfix to lookup the disabled icon.
     */
    private static final String POSTFIX_ICON_DISABLED = ".icon.disabled";

    /**
     * The postfix to lookup the short description which is used as a tooltip text.
     */
    private static final String POSTFIX_DESCRIPTION_SHORT = ".description.short";

    /**
     * The postfix to lookup the shortcut.
     */
    private static final String POSTFIX_SHORTCUT = ".short";

    /**
     * The postfix to lookup the label of the action.
     */
    private static final String POSTFIX_LABEL = ".label";

    /**
     * The character prepending the mnemonic character within the label.
     */
    private static final char SYMBOL_MNEMONIC = '&';

    /**
     * The string used to identify a separator.
     */
    private static final String SYMBOL_SEPARATOR = "-";

    /**
     * The string used to identify a simple gap.
     */
    private static final String SYMBOL_GAP = "_";

    /**
     * The map to store the actions.
     */
    private static final Map actions = new HashMap();

    /**
     * The properties associated with this instance. They are used to lookup the different action properties.
     */
    private static final Properties properties = new Properties();

    /**
     * Set properties used for subsequent action configuration lookup.
     *
     * @param resource the resource to lookup actions
     */
    public static void setProperties(final InputStream resource) {

        try {
            properties.load(resource);
        }
        catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Registers an action and associates it with the given string.
     *
     * @param name   the string to associate the action with
     * @param action the action to register
     */
    public static void registerAction(final String name, final Action action) {
        configureAction(action, name);
        actions.put(name, action);
    }

    /**
     * Returns the action identified by the given string.
     *
     * @param s the string to identify the action
     * @return the action found or null if none
     */
    public static Action getAction(final String s) {
        final Action action = (Action) actions.get(s);
        if (action == null) {
            throw new IllegalArgumentException("action not found: " + s);
        }
        return action;
    }

    public static Action[] getActions(final String base) {

        final String items = properties.getProperty(base + POSTFIX_ACTIONS);
        final StringTokenizer stringTokenizer = new StringTokenizer(items);
        final Action[] actions = new Action[stringTokenizer.countTokens()];

        for (int i = 0; stringTokenizer.hasMoreTokens(); i++) {
            final String token = stringTokenizer.nextToken();
            actions[i] = getAction(token);
        }

        return actions;
    }

    /**
     * Returns a tool bar.
     *
     * @param tools a string identifying the actions to put into the tool bar
     * @return the tool bar
     */
    public static JToolBar createToolBar(final String tools) {

        final ToolBarBuilder builder = new ToolBarBuilder();
        final String items = properties.getProperty(tools + POSTFIX_ACTIONS);

        for (StringTokenizer stringTokenizer = new StringTokenizer(items); stringTokenizer.hasMoreTokens();) {
            final String token = stringTokenizer.nextToken();

            if (token.endsWith("+")) {
                builder.addToggle(getToggleAction(token.substring(0, token.length() - 1)));
            }
            else if (token.equals(SYMBOL_SEPARATOR)) {
                builder.addSeparator();
            }
            else if (token.equals(SYMBOL_GAP)) {
                builder.addGap();
            }
            else {
                builder.add(getAction(token));
            }
        }

        final JToolBar toolBar = builder.getToolBar();
        toolBar.setOpaque(false);
        toolBar.setBackground(new Color(255, 255, 255, 0));
        return toolBar;
    }

    public static JMenuBar createMenuBar() {
        final String menues = properties.getProperty("menues");
        return createMenuBar(menues);
    }

    /**
     * Returns a menu bar.
     *
     * @param menus a string identifying the menus to be build into the menu bar
     * @return the menu bar
     */
    public static JMenuBar createMenuBar(final String menus) {
        // menus contains a space-separated list of menus to build into the menu bar
        final JMenuBar menuBar = new JMenuBar();
        configureComponent(menuBar);

        for (StringTokenizer stringTokenizer = new StringTokenizer(menus); stringTokenizer.hasMoreTokens();) {
            menuBar.add(createMenu(stringTokenizer.nextToken()));
        }

        return menuBar;
    }

    /**
     * Make the component looking good.
     *
     * @param component the component to alter the client properties for
     */
    private static void configureComponent(final JComponent component) {
        component.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        component.putClientProperty(PlasticLookAndFeel.BORDER_STYLE_KEY, BorderStyle.SEPARATOR);
    }

    /**
     * Constructs the menu. for a key build up from the given base concatenated with a {@link #POSTFIX_ACTIONS}.
     *
     * @param base the base to construct a key by appending {@link #POSTFIX_ACTIONS} and looking up the actions, a list
     *             of action identifiers separated by space.
     * @return the menu
     */
    private static JComponent createMenu(final String base) {
        final Mnemonics label = createMnemonic(base);
        final JMenu menu = new JMenu(label.label);
        menu.setMnemonic(label.mnemonic);

        final String menuItems = properties.getProperty(base + POSTFIX_ACTIONS);

        for (StringTokenizer tokenizer = new StringTokenizer(menuItems); tokenizer.hasMoreTokens();) {
            final String item = tokenizer.nextToken();

            if (item.equals(SYMBOL_SEPARATOR)) {
                menu.addSeparator();
            }
            else {
                menu.add(createMenuItem(item));
            }
        }

        return menu;
    }

    /**
     * Cunstructs a menu item for the given key.
     *
     * @param key the key to identify the action and wrap it into a menu item
     * @return the menu item
     */
    private static JMenuItem createMenuItem(final String key) {
        final Action action = getAction(key);
        final JMenuItem item = new JMenuItem(action);

        return item;
    }

    /**
     * Puts the different property values into the action by getting them from the property file.
     *
     * @param action the action to add the properties to
     * @param base   the base to concatenate with a postfix and lookup the property values in the property file. The
     *               method uses {@link #POSTFIX_DESCRIPTION_LONG} for the long description, {@link
     *               #POSTFIX_ICON_ENABLED} for the icon, {@link #POSTFIX_ICON_DISABLED} for a disabled icon. The rest
     *               is generated using helper methods.
     */
    private static void configureAction(final Action action, final String base) {
        final Mnemonics label = createMnemonic(base);
        action.putValue(Action.ACTION_COMMAND_KEY, base);
        action.putValue(Action.LONG_DESCRIPTION, properties.getProperty(base + POSTFIX_DESCRIPTION_LONG));
        action.putValue(Action.SHORT_DESCRIPTION, createShortDescription(base));
        action.putValue(Action.MNEMONIC_KEY, new Integer(label.mnemonic));
        action.putValue(Action.NAME, label.label);
        action.putValue(Action.SMALL_ICON, ImageLocator.getIcon(properties.getProperty(base + POSTFIX_ICON_ENABLED)));
        action.putValue(ActionManager.SMALL_GRAY_ICON, ImageLocator.getIcon(properties.getProperty(base + POSTFIX_ICON_DISABLED)));
        action.putValue(Action.ACCELERATOR_KEY, createKeyStroke(base));
    }

    /**
     * Creates a tooltip text by reading the short description from the properties and adding a symbolic name for the
     * shortcut in paranthesis.
     *
     * @param base the base to build the key for looking up the short description and the shortcut in the property file.
     *             The short descriptions key is concatenated from base and {@link #POSTFIX_DESCRIPTION_SHORT}, the
     *             short cut key from base and {@link #POSTFIX_SHORTCUT}.
     * @return the complete text
     */
    private static Object createShortDescription(final String base) {
        final String text = properties.getProperty(base + POSTFIX_DESCRIPTION_SHORT, "");
        final String shortcut = properties.getProperty(base + POSTFIX_SHORTCUT);
        String shortcutText = "";

        if (shortcut != null) {
            shortcutText = " (" + KeyBindings.getSymbolicModifierName(shortcut) + ")";
        }

        return text + shortcutText;
    }

    /**
     * Creates a key stroke by using the given base plus {@link #POSTFIX_SHORTCUT} as the lookup key for the
     * properties.
     *
     * @param base the base string
     * @return the key stroke build from the properties
     */
    private static KeyStroke createKeyStroke(final String base) {
        final String accelerator = properties.getProperty(base + POSTFIX_SHORTCUT);
        final KeyStroke keyStroke = KeyBindings.parseKeyStroke(accelerator);

        return keyStroke;
    }

    /**
     * Extracts a {@link ActionBuilder.Mnemonics} object consisting of a label and the mnemonic char from the
     * properties. The lookup key is composed by adding {@link #POSTFIX_LABEL} to the base.
     *
     * @param base the base to construct the lookup key
     * @return a Mnemonic object
     */
    private static Mnemonics createMnemonic(final String base) {
        final String label = properties.getProperty(base + POSTFIX_LABEL, base);
        final int offset = label.indexOf(SYMBOL_MNEMONIC);
        final Mnemonics ret = new Mnemonics();

        if (offset > -1) {
            ret.label = label.substring(0, offset).concat(label.substring(offset + 1));
            ret.mnemonic = ret.label.charAt(offset);
        }
        else {
            ret.label = label;
        }

        return ret;
    }

    /**
     * Returns a toggle action.
     *
     * @param key the key identifying the action
     * @return the toggle action or null if none
     */
    private static ToggleAction getToggleAction(final String key) {
        return (ToggleAction) actions.get(key);
    }

    public static Properties getProperties() {
        return properties;
    }

    /**
     * Helper data class to wrap a label and a mnemonic char.
     */
    private static final class Mnemonics {
        /**
         * The label, defaults to an empty string.
         */
        private String label = "";

        /**
         * The mnemonic, defaults to the zero character.
         */
        private int mnemonic = '\0';
    }
}
