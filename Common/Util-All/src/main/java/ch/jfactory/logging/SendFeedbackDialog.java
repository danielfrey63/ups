/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.logging;

import ch.jfactory.resource.Strings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/** @author Thomas Wegmueller */
public class SendFeedbackDialog extends JDialog
{
    private String dump = "---";

    private final String title = Strings.getString( "error.feedback.title" );

    private final String description = Strings.getString( "error.feedback.info" );

    private String errorMessage = Strings.getString( "error.feedback.message" );

    private String feedbackMail = Strings.getString( "error.feedback.email" );

    public SendFeedbackDialog()
    {
        super( (JFrame) null, Strings.getString( "error.feedback.window" ), true );
        this.setLocation( 100, 100 );
    }

    public void setDump( final String dump )
    {
        this.dump = dump;
    }

    public Container buildContentPane()
    {
        final JPanel panel = new JPanel( new BorderLayout( 5, 5 ) );

        panel.add( buildHeader(), BorderLayout.NORTH );
        panel.add( buildTabbedPane(), BorderLayout.CENTER );
        panel.add( buildButtonBar(), BorderLayout.SOUTH );
        return panel;
    }

    public JComponent createCopyToClipboard()
    {
        final JButton btn = new JButton( Strings.getString( "error.feedback.copy" ) );
        btn.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                copyToClipboard();
            }
        } );
        return btn;
    }

    public JComponent createCloseButton()
    {
        final JButton btn = new JButton( "Close" );
        btn.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                dispose();
            }
        } );
        getRootPane().setDefaultButton( btn );
        return btn;
    }

    public JComponent buildButtonBar()
    {
        final JPanel panel = new JPanel( new FlowLayout( FlowLayout.RIGHT, 5, 5 ) );
        panel.add( createCopyToClipboard() );
        panel.add( createCloseButton() );
        return panel;
    }

    public JComponent buildTabbedPane()
    {
        final JTabbedPane tab = new JTabbedPane();
        tab.setBorder( null );
        tab.add( Strings.getString( "error.feedback.helptab" ), createHelp() );
        tab.add( Strings.getString( "error.feedback.contenttab" ), createMultiLineLabel( dump ) );
        tab.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        return tab;
    }

    private JComponent createHelp()
    {
        final JComponent lab = createMultiLineLabel( errorMessage + feedbackMail );
        lab.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        return lab;
    }

    public void open()
    {
        setContentPane( buildContentPane() );
        this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        this.setSize( 500, 500 );
        this.requestFocus();
        this.setVisible( true );
    }

    public void copyToClipboard()
    {
        final StringSelection selection = new StringSelection( dump );
        getToolkit().getSystemClipboard().setContents( selection, selection );
    }

    public JComponent createMultiLineLabel( final String s )
    {
        final JTextArea area = new JTextArea( s );
        area.setLineWrap( true );
        area.setWrapStyleWord( true );
        area.setEditable( false );
        area.setBorder( new EmptyBorder( 12, 12, 12, 12 ) );
        area.setOpaque( false );
        final JScrollPane pane = new JScrollPane( area );
        pane.setOpaque( false );
        pane.getViewport().setOpaque( false );
        pane.setBorder( null );
        pane.setBorder( BorderFactory.createEmptyBorder( 0, 12, 0, 10 ) );
        return pane;
    }

    /**
     * Builds and answers the panel's center component.
     *
     * @return Description of the Return Value
     */
    protected JComponent buildHeaderComponent()
    {
        final JComponent header = buildHeaderPanel();
        header.setBorder( BorderFactory.createEmptyBorder( 0, 12, 0, 10 ) );
        header.setMinimumSize( new Dimension( 300, 70 ) );
        return header;
    }

    /**
     * Builds the panel.
     *
     * @return the header component
     */
    private JComponent buildHeader()
    {
        final JPanel pan = new JPanel( new BorderLayout( 5, 5 ) );
        pan.add( buildHeaderComponent(), BorderLayout.CENTER );
        pan.add( new JSeparator(), BorderLayout.SOUTH );
        pan.setBackground( Color.white );
        return pan;
    }

    /**
     * Builds the header panel - except the bottom component.
     *
     * @return Description of the Return Value
     */
    private JComponent buildHeaderPanel()
    {
        final JPanel panel = new JPanel( new GridBagLayout() );
        panel.setOpaque( false );

        final JLabel titleLabel = new JLabel( title );
        titleLabel.setFont( titleLabel.getFont().deriveFont( Font.BOLD ) );
        final JComponent descriptionArea = createMultiLineLabel( description );

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets( 4, 4, 0, 0 );
        panel.add( titleLabel, gbc );

        gbc.insets = new Insets( 2, 2, 0, 0 );
        panel.add( descriptionArea, gbc );

        gbc.weighty = 1.0;
        gbc.insets = new Insets( 0, 0, 3, 0 );
        panel.add( Box.createGlue(), gbc );
        //
        final JPanel result = new JPanel( new BorderLayout( 4, 0 ) );
        result.add( panel, BorderLayout.CENTER );

        final Dimension size = new Dimension( 300, 70 );
        result.setMinimumSize( size );
        result.setPreferredSize( size );
        result.setOpaque( false );

        return result;
    }

}
