/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.component;

import abbot.tester.ComponentTester;
import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import junit.extensions.abbot.ComponentTestFixture;

/**
 * Abbot test to test the FixedFormatTextField.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class FixedFormatTextFieldTest extends ComponentTestFixture {

    /**
     * The abbot tester.
     */
    private ComponentTester tester;

    /**
     * Constructs a named test.
     *
     * @param base the base name for the test
     */
    public FixedFormatTextFieldTest(final String base) {
        super(base);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() {
        tester = new ComponentTester();
    }

    /**
     * Tests the creation of a field.
     */
    public void testDefaultPatterns() {
        doTestDefault("NN-NNN", "12-345");
        doTestDefault("NN.NN.NNNN", "12.34.5678");
        doTestDefault("NN.NN. NNN", "12.34. 567");
        doTestDefault("N-A.C", "1-d.r");

        doTestDefaultException("NN-NNN", "12-A45");
        doTestDefaultException("NN.NN.NNNN", "12.34.A678");
        doTestDefaultException("N-A.C", "A-d.r");
    }

    /**
     * Tests for the successful creation of a text field and checks the initial text.
     *
     * @param pattern the pattern to use
     * @param def     the default string
     */
    private void doTestDefault(final String pattern, final String def) {
        final FixedFormatTextField field = new FixedFormatTextField(pattern, def);
        showFrame(field);
        assertEquals("Text " + field.getText() + " doesn't match the expected " + def, field.getText(), def);
    }

    /**
     * Test for an exception during creation of a field. If an IllegalArgumentException is not thrown, the test fails.
     *
     * @param pattern the pattern to use
     * @param def     the default stirng
     */
    private void doTestDefaultException(final String pattern, final String def) {
        boolean exceptionOccured = false;

        try {
            new FixedFormatTextField(pattern, def);
        }
        catch (IllegalArgumentException e) {
            exceptionOccured = true;
        }

        assertTrue("Expected exception not thrown for pattern " + pattern + " and string " + def, exceptionOccured);
    }

    /**
     * Tests for simple inserts.
     */
    public void testSimpleTyping() {
        doSimpleInsert("NN.NNN", "11.111", 0, "99999", "99.999");
        doSimpleInsert("NN.NNN", "11.111", 4, "99", "11.199");
        doSimpleInsert("N x-.N", "1 x-.1", 0, "99", "9 x-.9");
    }

    /**
     * Tests simple inserts but with overlength chunks. A beep should be posted.
     */
    public void testOverlengthTyping() {
        doSimpleInsert("NN.NNN", "11.111", 0, "999999", "99.999");
        doSimpleInsert("NN. NN", "11. 11", 0, "99999", "99. 99");
        doSimpleInsert("NN.NNN", "11.111", 4, "999", "11.199");
    }

    /**
     * Test chopped inserts that produce a system beep.
     */
    public void testChoppedInserts() {
        // Point is not inserted
        doSimpleInsert("NN.NNN", "11.111", 1, "999.99", "19.999");
        doSimpleInsert("NN.NNN", "11.111", 1, "99.9", "19.991");
    }

    /**
     * Tests a simple insert without exceptions. The given insert string ist typed one character at a time.
     *
     * @param pattern the pattern to use
     * @param def     the default string
     * @param pos     the position to insert the text
     * @param insert  the text to insert
     * @param result  the expected result
     */
    private void doSimpleInsert(final String pattern, final String def, final int pos, final String insert, final String result) {
        final FixedFormatTextField field = new FixedFormatTextField(pattern, def);
        showFrame(field);
        tester.actionFocus(field);
        field.setSelectionStart(pos);
        field.setSelectionEnd(pos);
        tester.actionKeyString(insert);

        final String text = field.getText();
        assertEquals("The expected result " + result + " does not match " + text, text, result);
    }

    /**
     * Tests a simple paste of a string.
     */
    public void testPaste() {
        doPaste("NN-NNN", "11-111", 1, "99", "11-111");
        doPaste("NN-NNN", "11-111", 0, "99", "99-111");
        doPaste("NN-NNN", "11-111", 0, "99-99", "99-991");
    }

    /**
     * Tests a simple paste into the given location.
     *
     * @param pattern the pattern to use
     * @param def     the default string
     * @param pos     the position to insert the text
     * @param insert  the text to insert
     * @param result  the expected result
     */
    private void doPaste(final String pattern, final String def, final int pos, final String insert, final String result) {
        final JTextField source = new JTextField();
        final FixedFormatTextField field = new FixedFormatTextField(pattern, def);
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(source, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        showFrame(panel);

        source.setText(insert);
        source.selectAll();
        tester.actionKeyStroke(source, KeyEvent.VK_C, InputEvent.CTRL_MASK);

        field.setSelectionStart(pos);
        field.setSelectionEnd(pos);
        tester.actionKeyStroke(field, KeyEvent.VK_V, InputEvent.CTRL_MASK);

        final String text = field.getText();
        assertEquals("The expected result " + result + " does not match " + text, text, result);
    }
}
