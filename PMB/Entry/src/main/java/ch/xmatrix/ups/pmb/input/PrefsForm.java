/*
 * Created by JFormDesigner on Tue Apr 10 10:28:07 CEST 2007
 */
package ch.xmatrix.ups.pmb.input;

import ch.jfactory.component.list.AlternateListCellRenderer;
import ch.xmatrix.ups.pmb.list.DefaultMutableListModel;
import ch.xmatrix.ups.pmb.list.JMutableList;
import ch.xmatrix.ups.pmb.ui.model.Settings;
import static ch.xmatrix.ups.pmb.ui.model.Settings.NamedValue;
import static ch.xmatrix.ups.pmb.ui.model.Settings.OrderedNamedValue;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/** @author Daniel Frey */
public class PrefsForm extends JDialog
{
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JMutableList movePathsList;

    private JMutableList orderList;

    private JTextField pathIncludePatterns;

    private JTextField pathExcludePatterns;

    private JTextField splitCharacters;

    private JTextField splitRandomPatterns;

    private JTextField splitExclusivePatterns;

    private JTextField splitExcludePatterns;

    private JTextField defaultCharacter;

    private JTextField speciesExcludePatterns;

    private JCheckBox showSettingsCheckBox;

    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private final Settings settings;

    private boolean ok;

    private DefaultMutableListModel orderListModel;

    private DefaultListModel movePathModel;

    public PrefsForm( final Frame owner, final Settings settings )
    {
        super( owner );
        this.settings = settings;
        initComponents();
        initMyComponents();
        initData();
        initListeners();
        getRootPane().setDefaultButton( okButton );
    }

    private void initMyComponents()
    {
        orderList.setCellRenderer( new AlternateListCellRenderer()
        {
            public Component getListCellRendererComponent( final JList list, final Object object, final int index,
                                                           final boolean isSelected, final boolean cellHasFocus )
            {
                final JLabel label = (JLabel) super.getListCellRendererComponent( list, object, index, isSelected, cellHasFocus );
                final NamedValue namedValue = (NamedValue) object;
                final String name = namedValue.getName();
                final String value = namedValue.getValue();
                final String text = name + "=" + value;
                label.setText( text );
                return label;
            }
        } );
        orderList.setListCellEditor( new Settings.NamedValueCellEditor() );
        movePathsList.setCellRenderer( new AlternateListCellRenderer()
        {
            public Component getListCellRendererComponent( final JList list, final Object object, final int index,
                                                           final boolean isSelected, final boolean cellHasFocus )
            {
                final JLabel label = (JLabel) super.getListCellRendererComponent( list, object, index, isSelected, cellHasFocus );
                final NamedValue namedValue = (NamedValue) object;
                final String name = namedValue.getName();
                final String value = namedValue.getValue();
                final String text;
                final Font font;
                final boolean isDefault = name.startsWith( "+" );
                if ( isDefault )
                {
                    text = name.substring( 1 ) + "=" + value;
                    font = label.getFont().deriveFont( Font.BOLD );
                }
                else
                {
                    text = name + "=" + value;
                    font = label.getFont().deriveFont( Font.PLAIN );
                }
                label.setText( text );
                label.setFont( font );
                return label;
            }
        } );
        movePathsList.setListCellEditor( new Settings.NamedValueCellEditor() );
    }

    private void initData()
    {
        pathIncludePatterns.setText( settings.getFileIncludePatterns() );
        pathExcludePatterns.setText( settings.getFileExcludePatterns() );
        splitCharacters.setText( settings.getSplitCharacter() );
        splitExclusivePatterns.setText( settings.getSplitExclusivePatterns() );
        splitRandomPatterns.setText( settings.getSplitRandomPatterns() );
        splitExcludePatterns.setText( settings.getSplitExcludePatterns() );
        defaultCharacter.setText( settings.getDefaultPattern() );
        speciesExcludePatterns.setText( settings.getSpeciesExcludePatterns() );
        showSettingsCheckBox.setSelected( settings.getShowSettingsOnStartup() );
        orderListModel = new DefaultMutableListModel();
        final List<OrderedNamedValue> orderTokens = settings.getOrderTokens();
        for ( final OrderedNamedValue orderToken : orderTokens )
        {
            orderListModel.addElement( orderToken );
        }
        orderList.setModel( orderListModel );

        movePathModel = new DefaultMutableListModel();
        final List<NamedValue> movePaths = settings.getMovePaths();
        for ( final NamedValue movePath : movePaths )
        {
            movePathModel.addElement( movePath );
        }
        movePathsList.setModel( movePathModel );
    }

    private void initListeners()
    {
        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( final WindowEvent e )
            {
                close();
            }
        } );
    }

    private void actionOk()
    {
        ok = true;
        settings.setFileIncludePatterns( pathIncludePatterns.getText() );
        settings.setFileExcludePatterns( pathExcludePatterns.getText() );
        settings.setSplitPattern( splitCharacters.getText() );
        settings.setSplitRandomPatterns( splitRandomPatterns.getText() );
        settings.setSplitExclusivePatterns( splitExclusivePatterns.getText() );
        settings.setSplitExcludePatterns( splitExcludePatterns.getText() );
        settings.setDefaultPattern( defaultCharacter.getText() );
        settings.setSpeciesExcludePatterns( speciesExcludePatterns.getText() );
        settings.setShowSettingsOnStartup( showSettingsCheckBox.isSelected() );

        final int movePathsSize = movePathModel.getSize();
        final List<NamedValue> movePathList = new ArrayList<NamedValue>( movePathsSize );
        for ( int i = 0; i < movePathsSize; i++ )
        {
            final NamedValue item = (NamedValue) movePathModel.getElementAt( i );
            movePathList.add( item );
        }
        settings.setMovePaths( movePathList );

        final int orderListSize = orderListModel.getSize();
        final List<OrderedNamedValue> orderList = new ArrayList<OrderedNamedValue>( orderListSize );
        for ( int i = 0; i < orderListSize; i++ )
        {
            final NamedValue item = (NamedValue) orderListModel.getElementAt( i );
            final OrderedNamedValue orderItem = new OrderedNamedValue( item, i );
            orderList.add( orderItem );
        }
        settings.setOrderTokens( orderList );

        dispose();
    }

    private void actionCancel()
    {
        close();
    }

    private void close()
    {
        ok = false;
        dispose();
    }

    public boolean isOk()
    {
        return ok;
    }

    private void upButtonActionPerformed()
    {
        final int selectedIndex = orderList.getSelectedIndex();
        if ( selectedIndex > 0 )
        {
            final Object transfer = orderListModel.remove( selectedIndex );
            final int newIndex = selectedIndex - 1;
            orderListModel.add( newIndex, transfer );
            orderList.setSelectedIndex( newIndex );
        }
    }

    private void downButtonActionPerformed()
    {
        final int selectedIndex = orderList.getSelectedIndex();
        if ( selectedIndex < orderListModel.getSize() - 1 )
        {
            final Object transfer = orderListModel.remove( selectedIndex );
            final int newIndex = selectedIndex + 1;
            orderListModel.add( newIndex, transfer );
            orderList.setSelectedIndex( newIndex );
        }
    }

    private void deleteOrderItemActionPerformed()
    {
        deleteNamedValue( orderList, orderListModel );
    }

    private void addOrderItemActionPerformed()
    {
        addNamedValue( orderList, orderListModel );
    }

    private void addImagePathActionPerformed()
    {
        addNamedValue( movePathsList, movePathModel );
    }

    private void deleteImagePathActionPerformed()
    {
        deleteNamedValue( movePathsList, movePathModel );
    }

    private void addNamedValue( final JMutableList list, final DefaultListModel model )
    {
        final int selectedIndex = list.getSelectedIndex();
        final int newIndex;
        if ( selectedIndex > -1 && selectedIndex < model.getSize() )
        {
            newIndex = selectedIndex + 1;
        }
        else
        {
            newIndex = model.getSize();
        }
        model.add( newIndex, new NamedValue( "Neuer Name", "Neuer Wert" ) );
        list.setSelectedIndex( newIndex );
    }

    private void deleteNamedValue( final JMutableList list, final DefaultListModel model )
    {
        final int selectedIndex = list.getSelectedIndex();
        if ( selectedIndex > -1 && selectedIndex < model.getSize() )
        {
            model.remove( selectedIndex );
        }
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        final DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        final JPanel dialogPane = new JPanel();
        final JPanel contentPanel = new JPanel();
        final JComponent separator1 = compFactory.createSeparator( "Bilderpfade" );
        final JScrollPane scrollPane2 = new JScrollPane();
        movePathsList = new JMutableList();
        final JPanel panel2 = new JPanel();
        final JButton addImagePath = new JButton();
        final JButton deleteImagePath = new JButton();
        final JComponent separator6 = compFactory.createSeparator( "Sortierfolge" );
        final JScrollPane scrollPane1 = new JScrollPane();
        orderList = new JMutableList();
        final JPanel panel3 = new JPanel();
        final JButton addOrderItem = new JButton();
        final JButton deleteOrderItem = new JButton();
        final JButton upButton = new JButton();
        final JButton downButton = new JButton();
        final JComponent separator2 = compFactory.createSeparator( "Filter f\u00fcr Bildernamen" );
        final JLabel label3 = new JLabel();
        pathIncludePatterns = new JTextField();
        final JLabel label4 = new JLabel();
        pathExcludePatterns = new JTextField();
        final JComponent separator3 = compFactory.createSeparator( "Namenszerlegung f\u00fcr die Navigation" );
        final JLabel label11 = new JLabel();
        splitCharacters = new JTextField();
        final JLabel label8 = new JLabel();
        splitRandomPatterns = new JTextField();
        final JLabel label9 = new JLabel();
        splitExclusivePatterns = new JTextField();
        final JLabel label6 = new JLabel();
        splitExcludePatterns = new JTextField();
        final JLabel label10 = new JLabel();
        defaultCharacter = new JTextField();
        final JComponent separator4 = compFactory.createSeparator( "Artsortierung" );
        final JLabel label7 = new JLabel();
        speciesExcludePatterns = new JTextField();
        final JComponent separator5 = compFactory.createSeparator( "Start" );
        showSettingsCheckBox = new JCheckBox();
        final JPanel buttonBar = new JPanel();
        okButton = new JButton();
        final JButton cancelButton = new JButton();
        final CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle( "Einstellungen" );
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        setModal( true );
        final Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );

        //======== dialogPane ========
        {
            dialogPane.setBorder( Borders.DIALOG_BORDER );
            dialogPane.setLayout( new BorderLayout() );

            //======== contentPanel ========
            {
                contentPanel.setLayout( new FormLayout(
                        new ColumnSpec[]{
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec( ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW ),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
                        },
                        new RowSpec[]{
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec( RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW ),
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                new RowSpec( RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW ),
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.UNRELATED_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC
                        } ) );
                ( (FormLayout) contentPanel.getLayout() ).setRowGroups( new int[][]{{3, 7}} );
                contentPanel.add( separator1, cc.xywh( 1, 1, 5, 1 ) );

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView( movePathsList );
                }
                contentPanel.add( scrollPane2, cc.xywh( 1, 3, 3, 1 ) );

                //======== panel2 ========
                {
                    panel2.setLayout( new FormLayout(
                            ColumnSpec.decodeSpecs( "default:grow" ),
                            new RowSpec[]{
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC
                            } ) );

                    //---- addImagePath ----
                    addImagePath.setText( "+" );
                    addImagePath.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            addImagePathActionPerformed();
                        }
                    } );
                    panel2.add( addImagePath, cc.xy( 1, 1 ) );

                    //---- deleteImagePath ----
                    deleteImagePath.setText( "-" );
                    deleteImagePath.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            deleteImagePathActionPerformed();
                        }
                    } );
                    panel2.add( deleteImagePath, cc.xy( 1, 3 ) );
                }
                contentPanel.add( panel2, cc.xy( 5, 3 ) );
                contentPanel.add( separator6, cc.xywh( 1, 5, 5, 1 ) );

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView( orderList );
                }
                contentPanel.add( scrollPane1, cc.xywh( 1, 7, 3, 1 ) );

                //======== panel3 ========
                {
                    panel3.setLayout( new FormLayout(
                            ColumnSpec.decodeSpecs( "default" ),
                            new RowSpec[]{
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC
                            } ) );

                    //---- addOrderItem ----
                    addOrderItem.setText( "+" );
                    addOrderItem.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            addOrderItemActionPerformed();
                        }
                    } );
                    panel3.add( addOrderItem, cc.xy( 1, 1 ) );

                    //---- deleteOrderItem ----
                    deleteOrderItem.setText( "-" );
                    deleteOrderItem.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            deleteOrderItemActionPerformed();
                        }
                    } );
                    panel3.add( deleteOrderItem, cc.xy( 1, 3 ) );

                    //---- upButton ----
                    upButton.setText( "auf" );
                    upButton.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            upButtonActionPerformed();
                        }
                    } );
                    panel3.add( upButton, cc.xy( 1, 5 ) );

                    //---- downButton ----
                    downButton.setText( "ab" );
                    downButton.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            downButtonActionPerformed();
                        }
                    } );
                    panel3.add( downButton, cc.xy( 1, 7 ) );
                }
                contentPanel.add( panel3, cc.xy( 5, 7 ) );
                contentPanel.add( separator2, cc.xywh( 1, 9, 5, 1 ) );

                //---- label3 ----
                label3.setText( "Einschliesslich:" );
                label3.setLabelFor( pathIncludePatterns );
                label3.setDisplayedMnemonic( 'E' );
                label3.setDisplayedMnemonicIndex( 0 );
                contentPanel.add( label3, cc.xy( 1, 11 ) );

                //---- pathIncludePatterns ----
                pathIncludePatterns.setToolTipText( "Regul\u00e4re Ausdr\u00fccke f\u00fcr Dateinamen, die aufgenommen werden sollen, separiert mit Strichpunkt" );
                pathIncludePatterns.setColumns( 30 );
                contentPanel.add( pathIncludePatterns, cc.xywh( 3, 11, 3, 1 ) );

                //---- label4 ----
                label4.setText( "Ausschliesslich:" );
                label4.setLabelFor( pathExcludePatterns );
                label4.setDisplayedMnemonic( 'U' );
                contentPanel.add( label4, cc.xy( 1, 13 ) );

                //---- pathExcludePatterns ----
                pathExcludePatterns.setToolTipText( "Regul\u00e4re Ausdr\u00fccke f\u00fcr Dateinamen, die ausgeschlossen werden sollen, separiert mit Strichpunkt" );
                contentPanel.add( pathExcludePatterns, cc.xywh( 3, 13, 3, 1 ) );
                contentPanel.add( separator3, cc.xywh( 1, 15, 5, 1 ) );

                //---- label11 ----
                label11.setText( "Trennzeichen:" );
                label11.setDisplayedMnemonic( 'T' );
                label11.setLabelFor( splitCharacters );
                contentPanel.add( label11, cc.xy( 1, 17 ) );

                //---- splitCharacters ----
                splitCharacters.setToolTipText( "Namensbestandteile (Regul\u00e4re Ausdr\u00fccke durch Strichpunkt separiert), die nicht in die Hierarchie einfliessen sollen" );
                contentPanel.add( splitCharacters, cc.xywh( 3, 17, 3, 1 ) );

                //---- label8 ----
                label8.setText( "Zuf\u00e4llige Auswahl:" );
                label8.setLabelFor( splitRandomPatterns );
                label8.setDisplayedMnemonic( 'F' );
                contentPanel.add( label8, cc.xy( 1, 19 ) );

                //---- splitRandomPatterns ----
                splitRandomPatterns.setToolTipText( "Namensbestandteile (Regul\u00e4re Ausdr\u00fccke durch Strichpunkt separiert), die nicht in die Hierarchie einfliessen sollen" );
                contentPanel.add( splitRandomPatterns, cc.xywh( 3, 19, 3, 1 ) );

                //---- label9 ----
                label9.setText( "Exklusive Auswahl:" );
                label9.setLabelFor( splitExclusivePatterns );
                label9.setDisplayedMnemonic( 'X' );
                contentPanel.add( label9, cc.xy( 1, 21 ) );

                //---- splitExclusivePatterns ----
                splitExclusivePatterns.setToolTipText( "Namensbestandteile (Regul\u00e4re Ausdr\u00fccke durch Strichpunkt separiert), die nicht in die Hierarchie einfliessen sollen" );
                contentPanel.add( splitExclusivePatterns, cc.xywh( 3, 21, 3, 1 ) );

                //---- label6 ----
                label6.setText( "Ausschliesslich:" );
                label6.setLabelFor( splitExcludePatterns );
                label6.setDisplayedMnemonic( 'S' );
                contentPanel.add( label6, cc.xy( 1, 23 ) );

                //---- splitExcludePatterns ----
                splitExcludePatterns.setToolTipText( "Namensbestandteile (Regul\u00e4re Ausdr\u00fccke durch Strichpunkt separiert), die nicht in die Hierarchie einfliessen sollen" );
                contentPanel.add( splitExcludePatterns, cc.xywh( 3, 23, 3, 1 ) );

                //---- label10 ----
                label10.setText( "Default-Zeichen" );
                label10.setLabelFor( defaultCharacter );
                label10.setDisplayedMnemonic( 'D' );
                contentPanel.add( label10, cc.xy( 1, 25 ) );

                //---- defaultCharacter ----
                defaultCharacter.setToolTipText( "Namensbestandteile (Regul\u00e4re Ausdr\u00fccke durch Strichpunkt separiert), die nicht in die Hierarchie einfliessen sollen" );
                contentPanel.add( defaultCharacter, cc.xywh( 3, 25, 3, 1 ) );
                contentPanel.add( separator4, cc.xywh( 1, 27, 5, 1 ) );

                //---- label7 ----
                label7.setText( "Ausschliesslich:" );
                label7.setLabelFor( speciesExcludePatterns );
                label7.setDisplayedMnemonic( 'C' );
                contentPanel.add( label7, cc.xy( 1, 29 ) );

                //---- speciesExcludePatterns ----
                speciesExcludePatterns.setToolTipText( "Namensbestandteile (Regul\u00e4re Ausdr\u00fccke durch Strichpunkt separiert), die nicht in die Hierarchie einfliessen sollen" );
                contentPanel.add( speciesExcludePatterns, cc.xywh( 3, 29, 3, 1 ) );
                contentPanel.add( separator5, cc.xywh( 1, 31, 5, 1 ) );

                //---- showSettingsCheckBox ----
                showSettingsCheckBox.setText( "Zeige diese Einstellungen beim Start" );
                showSettingsCheckBox.setSelected( true );
                showSettingsCheckBox.setMnemonic( 'Z' );
                contentPanel.add( showSettingsCheckBox, cc.xywh( 1, 33, 5, 1, CellConstraints.LEFT, CellConstraints.DEFAULT ) );

                //======== buttonBar ========
                {
                    buttonBar.setBorder( null );
                    buttonBar.setLayout( new FormLayout(
                            new ColumnSpec[]{
                                    new ColumnSpec( ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW ),
                                    FormFactory.BUTTON_COLSPEC,
                                    FormFactory.RELATED_GAP_COLSPEC,
                                    FormFactory.BUTTON_COLSPEC
                            },
                            RowSpec.decodeSpecs( "pref" ) ) );
                    ( (FormLayout) buttonBar.getLayout() ).setColumnGroups( new int[][]{{2, 4}} );

                    //---- okButton ----
                    okButton.setText( "OK" );
                    okButton.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            actionOk();
                        }
                    } );
                    buttonBar.add( okButton, cc.xy( 2, 1 ) );

                    //---- cancelButton ----
                    cancelButton.setText( "Abbrechen" );
                    cancelButton.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            actionCancel();
                        }
                    } );
                    buttonBar.add( cancelButton, cc.xy( 4, 1 ) );
                }
                contentPanel.add( buttonBar, cc.xywh( 1, 35, 5, 1 ) );
            }
            dialogPane.add( contentPanel, BorderLayout.CENTER );
        }
        contentPane.add( dialogPane, BorderLayout.CENTER );
        pack();
        setLocationRelativeTo( getOwner() );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
