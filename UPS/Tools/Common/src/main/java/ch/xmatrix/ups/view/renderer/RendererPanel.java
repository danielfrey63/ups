/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.xmatrix.ups.view.renderer;

import ch.jfactory.color.ColorUtils;
import ch.jfactory.lang.LogicUtils;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Panel with four fields to be displayed in a tree or list. The first field contains an optional icon, the second an
 * optional prefix component (i.e. a checkbox), the third the object text and the fourth a textual extention to
 * display.<p/>
 *
 * To understand the detailed color changes in this panel, look at the decision tables for colors dependent of the three
 * states <code>enabled</code> (En), <code>ok</code> (Ok) and <code>selected</code> (Se). States in paranthesis are not
 * occuring states, mentioned only to complete the decision table.<p/>
 *
 * Ok-state dependent text forground color (OkDependent). The four colors are <code>OK_FULL</code> (full ok color),
 * <code>OK_FADE</code> (faded ok color), <code>NOK_FULL</code> (full nok color) and <code>NOK_FADE</code> (faded nok
 * color):
 *
 * <pre>Decision table
 * En Ok Se |  OkDependent
 * ---------+-----------------
 * 0  0  0  |  COLOR_NOK_FADE
 * 0  0 (1) | (COLOR_NOK_FADE)
 * 0  1  0  |  COLOR_OK_FADE
 * 0  1 (1) | (COLOR_OK_FADE)
 * 1  0  0  |  COLOR_NOK_FULL
 * 1  0  1  |  COLOR_NOK_FADE
 * 1  1  0  |  COLOR_OK_FULL
 * 1  1  1  |  COLOR_OK_FADE
 * </pre>
 *
 * Ok-independent text foreground color (IndependentText) are <code>COLOR_FOREGROUND_DISABLED</code> (default disbled
 * color), <code>COLOR_FOREGROUND_SELECTED</code> (selected foreground) and <code>COLOR_FOREGROUND_DESELECTED</code>
 * (deselected foreground):
 *
 * <pre>Decision tree
 * En Se |  IndependentText
 * ------+-----------------------------
 * 0  0  |  COLOR_FOREGROUND_DISABLED
 * 1  0  |  COLOR_FOREGROUND_DESELECTED
 * 1  1  |  COLOR_FOREGROUND_SELECTED
 * </pre>
 *
 * Background colors (BackgroundColor) are <code>COLOR_BACKGROUND_DESELECTED</code> (i.e. transparent) and
 * <code>COLOR_BACKGROUND_SELECTED</code> (dark background):
 *
 * <pre>Decision tree
 * Se |  BackgroundColor
 * ---+------------------------------
 * 0  |  COLOR_BACKGROUND_DESELECTED
 * 1  |  COLOR_BACKGROUND_SELECTED
 * </pre>
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2006/04/25 11:08:11 $
 */
public class RendererPanel extends JPanel
{

    /** Used to display ok-independent selected background. */
    private static final Color COLOR_BACKGROUND_SELECTED = UIManager.getColor("Tree.selectionBackground");

    /** Used to display ok-independent deselected background. */
    private static final Color COLOR_BACKGROUND_DESELECTED = UIManager.getColor("Tree.background");

    /** Array of background colors. */
    private static final Color[] COLOR_BACKGROUND = {COLOR_BACKGROUND_DESELECTED, COLOR_BACKGROUND_SELECTED};

    /** Used to display disabled text. */
    private static final Color COLOR_FOREGROUND_DISABLED = UIManager.getColor("Label.disabledForeground");

    /** Used to display ok-independent selected text. */
    private static final Color COLOR_FOREGROUND_SELECTED = UIManager.getColor("Tree.selectionForeground");

    /** Used to display ok-independent deselected text. */
    private static final Color COLOR_FOREGROUND_DESELECTED = UIManager.getColor("Tree.foreground");

    /** Array of foreground colors. */
    private static final Color[] COLOR_FOREGROUND = {
            COLOR_FOREGROUND_DISABLED, COLOR_FOREGROUND_DESELECTED, COLOR_FOREGROUND_SELECTED};

    /** Base color for ok state. */
    private static final Color COLOR_OK_FULL = new Color(0, 200, 0);

    /** Base color for nok state. */
    private static final Color COLOR_NOK_FULL = Color.red;

    /** Used to display selected text in ok state. */
    private static final Color COLOR_OK_FADE = ColorUtils.fade(COLOR_OK_FULL, 0.5);

    /** Used to display selected text in nok state. */
    private static final Color COLOR_NOK_FADE = ColorUtils.fade(COLOR_NOK_FULL, 0.5);

    /** Array of colors for decision table. */
    private static final Color[] COLOR_OKDEPENDENT = {
            COLOR_NOK_FADE, COLOR_NOK_FADE, COLOR_OK_FADE, COLOR_OK_FADE,
            COLOR_NOK_FULL, COLOR_NOK_FADE, COLOR_OK_FULL, COLOR_OK_FADE
    };

    /** Color of selected border. */
    private static final Color COLOR_BORDER_SELECTED = UIManager.getColor("Button.focus");

    /** Selected border. */
    public static final LineBorder BORDER_SELECTED = new LineBorder(COLOR_BORDER_SELECTED);

    /** Deselected border. */
    public static final EmptyBorder BORDER_DESELECTED = new EmptyBorder(1, 1, 1, 1);

    public static final Border[] BORDERS = new Border[]{BORDER_DESELECTED, BORDER_SELECTED};

    public static enum SelectionType
    {
        ALL, TEXTONLY
    }

    private final JPanel selectable;

    private final JLabel text = new JLabel();

    private final JLabel icon = new JLabel();

    private final JLabel extention = new JLabel();

    private final JPanel textPanel = new JPanel();

    private final CellConstraints cc = new CellConstraints();

    private JComponent prefix = null;

    private boolean ok = true;

    private boolean selected = false;

    private FormLayout layout;

    /** Whether the prefix component (i.e. a checkbox) is enabled or not. */
    private boolean prefixEnabled;

    public RendererPanel()
    {
        this(SelectionType.TEXTONLY);
    }

    public RendererPanel(final SelectionType selectionType)
    {
        if (selectionType == SelectionType.ALL)
        {
            selectable = this;
            textPanel.setOpaque(false);
        }
        else
        {
            selectable = textPanel;
            textPanel.setOpaque(true);
        }

        layout = new FormLayout("l:p, 1dlu, l:p, 0dlu, l:p", "c:p");
        setLayout(layout);

        final Font font = UIManager.getFont("Label.font");
        setFont(font);
        icon.setFont(font);
        text.setFont(font);
        extention.setFont(font);

        final boolean opaque = false;
        icon.setOpaque(opaque);
        text.setOpaque(opaque);
        extention.setOpaque(opaque);

        setOpaque(true);
        setBackground(COLOR_BACKGROUND_DESELECTED);

        textPanel.setLayout(new FormLayout("l:p, 1dlu, l:p", "c:p"));
        textPanel.add(text, cc.xy(1, 1));
        add(textPanel, cc.xy(5, 1));
        add(icon, cc.xy(1, 1));
        addExtention();
        setBorder(BORDER_DESELECTED);
    }

    /**
     * Sets the prefix icon.
     *
     * @param newIcon the icon to set
     */
    public void setIcon(final Icon newIcon)
    {
        icon.setIcon(newIcon);
    }

    /**
     * Sets the object text.
     *
     * @param newText the text to set
     */
    public void setText(final String newText)
    {
        text.setText(newText);
    }

    /**
     * Set this to the text you want to display at the end of the panel. If set to null, the place is freed and nothing
     * is displayed
     *
     * @param extention the text to set, or null
     */
    public void setExtentionText(final String extention)
    {
        if (extention != null)
        {
            this.extention.setText(extention);
            addExtention();
        }
        else
        {
            removeExtention();
        }
    }

    /**
     * Sets the component between prefix icon and object text.
     *
     * @param prefixComponent the component to set
     */
    public void setPrefixComponent(final JComponent prefixComponent)
    {
        prefix = prefixComponent;
        prefix.setOpaque(false);
    }

    /**
     * Removes or adds the prefix component according to the argument given. Make sure to set the prefix component
     * before calling this method.
     *
     * @param showPrefix whether to show it
     */
    public void setShowPrefixComponent(final boolean showPrefix)
    {
        assert prefix != null : "set the prefix component before calling setShowPrefixComponent";
        if (showPrefix)
        {
            add(prefix, cc.xy(3, 1));
            layout.setColumnSpec(4, FormFactory.RELATED_GAP_COLSPEC);
        }
        else
        {
            remove(prefix);
            layout.setColumnSpec(4, FormFactory.createGapColumnSpec(Sizes.dluX(0)));
        }
    }

    /**
     * Enables or disables the whole panel and all its components.
     *
     * @param enabled whether to enable
     */
    public void setEnabled(final boolean enabled)
    {
        super.setEnabled(enabled);
        if (prefix != null)
        {
            prefix.setEnabled(enabled);
        }
        // Extention must always be enabled, so that the foreground color can be set correctly.
        extention.setEnabled(true);
        text.setEnabled(enabled);
    }

    /**
     * Enables or disables the prefix component.
     *
     * @param enabled whether to enable the prefix component
     */
    public void setPrefixEnabled(final boolean enabled)
    {
        prefixEnabled = enabled;
    }

    /**
     * Sets the ok state of the panel. This state affects the extention color.
     *
     * @param newOk whether ok
     */
    public void setOk(final boolean newOk)
    {
        ok = newOk;
    }

    /**
     * Returns the ok state.
     *
     * @return the ok state
     */
    public boolean isOk()
    {
        return ok;
    }

    /**
     * Sets the selected state of the panel. This state affects the whole panel.
     *
     * @param newSelected whether selected
     */
    public void setSelected(final boolean newSelected)
    {
        selected = newSelected;
    }

    /**
     * Returns the selected state.
     *
     * @return the selected state
     */
    public boolean isSelected()
    {
        return selected;
    }

    /**
     * Call this method to update the subcomponents states to reflect the actual state. The prefix component is enabled
     * if prefix-enabled is set to true AND the panel itself is enabled.
     *
     * @see #setPrefixEnabled(boolean)
     * @see #setEnabled(boolean)
     */
    public void update()
    {
        selectable.setBackground(COLOR_BACKGROUND[LogicUtils.getFirstFalse(new boolean[]{isSelected() && isEnabled()})]);
        selectable.setBorder(BORDERS[LogicUtils.getFirstFalse(new boolean[]{isSelected()})]);
        text.setForeground(COLOR_FOREGROUND[LogicUtils.getFirstFalse(new boolean[]{isEnabled(), isSelected()})]);
        extention.setForeground(COLOR_OKDEPENDENT[LogicUtils.getFullIndex(new boolean[]{isEnabled(), isOk(), isSelected()})]);
        if (prefix != null)
        {
            prefix.setEnabled(isEnabled() && prefixEnabled);
        }
        revalidate();
    }

    //--- Utilities

    /** Adds the extention to the panel. */
    private void addExtention()
    {
        textPanel.add(extention, cc.xy(3, 1));
    }

    /** Removes the extention from the panel. */
    private void removeExtention()
    {
        textPanel.remove(extention);
    }
}
