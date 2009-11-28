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
package ch.xmatrix.ups.uec.sets;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.SimpleNote;
import ch.jfactory.command.TableMove;
import ch.jfactory.component.table.SortableTableModel;
import ch.jfactory.component.table.SortedTable;
import ch.jfactory.component.table.TableUtils;
import ch.jfactory.image.SimpleIconFactory;
import ch.jfactory.lang.ArrayUtils;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.uec.exam.ExamModel;
import ch.xmatrix.ups.uec.exam.ExamsBuilder;
import ch.xmatrix.ups.uec.sets.commands.AddPerson;
import ch.xmatrix.ups.uec.sets.commands.CalculateExams;
import ch.xmatrix.ups.uec.sets.commands.Commands;
import ch.xmatrix.ups.uec.sets.commands.FromDirectory;
import ch.xmatrix.ups.uec.sets.commands.FromFiles;
import ch.xmatrix.ups.uec.sets.commands.FromInternet;
import ch.xmatrix.ups.uec.sets.commands.ReloadExamDataOnServer;
import ch.xmatrix.ups.uec.sets.commands.RemoveFromTable;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.layout.CellConstraints;
import com.wegmueller.ups.lka.IAnmeldedaten;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.ActionCommandInterceptor;

/**
 * Displays a table of students and their status concerning submitted examlists.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:19:08 $
 */
public class SetBuilder extends ActionCommandPanelBuilder
{
    private static final String RESOURCE_FORM = "ch/xmatrix/ups/uec/sets/SetPanel.jfd";

    private static final SortableTableModel DUMMY_SORTABLETABLEMODEL = new SortableTableModel( new DefaultTableModel() );

    private final SubmitTableModel submitTableModel = new SubmitTableModel( new Registration[0] );

    private final SortableTableModel sortableTableModel = new SortableTableModel( submitTableModel );

    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();

    private final ExamsBuilder exams;

    private SortedTable table;

    private JComboBox combo;

    private ExamModel examModel;

    private ActionCommand fromInternet;

    private ActionCommand fromFiles;

    private ActionCommand fromDirectory;

    private ActionCommand moveUp;

    private ActionCommand moveDown;

    private ActionCommand remove;

    private ActionCommand calculate;

    private ActionCommand newPerson;

    private InfoModel infoModel;

    public SetBuilder( final ExamsBuilder exams )
    {
        this.exams = exams;
    }

    //--- ActionCommandPanelBuilder overrides

    protected void initCommands()
    {
        initCommand( new ReloadExamDataOnServer( getCommandManager() ), true );
        fromInternet = initCommand( new FromInternet( getCommandManager(), submitTableModel ) );
        fromFiles = initCommand( new FromFiles( getCommandManager(), submitTableModel ) );
        fromDirectory = initCommand( new FromDirectory( getCommandManager(), submitTableModel ) );
        newPerson = initCommand( new AddPerson( getCommandManager(), submitTableModel ) );
        moveUp = initCommand( new TableMove( getCommandManager(), Commands.COMMANDID_MOVEUP, sortableTableModel, selectionModel, -1 ) );
        moveDown = initCommand( new TableMove( getCommandManager(), Commands.COMMANDID_MOVEDOWN, sortableTableModel, selectionModel, 1 ) );
        remove = initCommand( new RemoveFromTable( getCommandManager(), submitTableModel, selectionModel, sortableTableModel ) );
    }

    protected JComponent createMainPanel()
    {
        try
        {
            infoModel.setNote( new SimpleNote( "Lade Prüfungssets-Editor", infoModel.getNote().getColor() ) );

            final FormCreator creator = new FormCreator( FormLoader.load( RESOURCE_FORM ) );
            creator.createAll();

            combo = creator.getComboBox( "combo" );
            final ComboBoxAdapter comboModel = new ComboBoxAdapter( (ListModel) exams.getModels(), new ValueHolder( null ) );
            combo.setModel( comboModel );
            combo.setRenderer( new ComboRenderer() );

            calculate = initCommand( new CalculateExams( getCommandManager(), comboModel, creator.getTextField( "field" ),
                    sortableTableModel, submitTableModel ) );

            table = (SortedTable) creator.getTable( "table" );
            final SimpleIconFactory factory = new SimpleIconFactory( "/icons" );
            final Icon trueIcon = factory.getIcon( "/com/famfamfam/silk/accept.png" );
            final Icon falseIcon = factory.getIcon( "/com/famfamfam/silk/cancel.png" );
            table.setDefaultRenderer( Boolean.class, new BooleanCellRenderer( trueIcon, falseIcon ) );
            table.setDefaultRenderer( Calendar.class, new TableUtils.CalendarCellRenderer( "HH:mm dd.MM.yyyy" ) );
            table.setModel( sortableTableModel );
            table.setSelectionModel( selectionModel );

            final JPanel toolbar = creator.getPanel( "panelToolbar" );
            final JToolBar bar = getCommandManager().getGroup( Commands.GROUPID_TOOLBAR ).createToolBar( Commands.FACENAME_TOOLBAR );
            toolbar.add( bar, new CellConstraints().xy( 3, 1 ) );

            return creator.getPanel( "panel" );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "could not create submit panel", e );
        }
    }

    protected void initComponentListeners()
    {
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                setStates( table );
            }
        } );
        table.addKeyStroke( SortedTable.DIRECTION_UP, KeyStroke.getKeyStroke( KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK ) );
        table.addKeyStroke( SortedTable.DIRECTION_DOWN, KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK ) );
        combo.addItemListener( new ItemListener()
        {
            public void itemStateChanged( final ItemEvent e )
            {
                if ( e.getStateChange() == ItemEvent.SELECTED )
                {
                    examModel = (ExamModel) e.getItem();
                    setStates( combo );
                }
            }
        } );
        final ActionCommandInterceptor interceptor = new ActionCommandInterceptor()
        {
            private Map<MultiKey, Integer> selections;

            public boolean beforeExecute( final ActionCommand command )
            {
                selections = TableUtils.getSelection( table );
                return true;
            }

            public void afterExecute( final ActionCommand command )
            {
                TableUtils.setSelection( selections, table );
                setStates( null );
            }
        };
        remove.addInterceptor( interceptor );
        fromDirectory.addInterceptor( interceptor );
        fromFiles.addInterceptor( interceptor );
        fromInternet.addInterceptor( interceptor );
        newPerson.addInterceptor( interceptor );
    }

    //--- Interface

    public void setInfoModel( final InfoModel infoModel )
    {
        this.infoModel = infoModel;
    }

    //--- Utilities

    private void setStates( final Object source )
    {
        final boolean enabled = examModel != null && examModel.isFixed();
        fromInternet.setEnabled( enabled );
        fromFiles.setEnabled( enabled );
        fromDirectory.setEnabled( enabled );
        newPerson.setEnabled( enabled );
        final int[] rows = table.getSelectedRows();
        final boolean selected = rows.length > 0;
        final boolean top = table.isAtTop();
        final boolean bottom = table.isAtBottom();
        moveDown.setEnabled( selected && !bottom );
        moveUp.setEnabled( selected && !top );
        remove.setEnabled( selected );
        calculate.setEnabled( enabled && sortableTableModel.getRowCount() > 0 );

        if ( source != table )
        {
            table.setEnabled( enabled );
            final Map<MultiKey, Integer> selection = TableUtils.getSelection( table );
            table.setModel( DUMMY_SORTABLETABLEMODEL );
            table.setModel( sortableTableModel );
            TableUtils.setSelection( selection, table );
        }
    }

    private static class BooleanCellRenderer extends DefaultTableCellRenderer
    {
        private final Icon trueIcon;

        private final Icon falseIcon;

        public BooleanCellRenderer( final Icon trueIcon, final Icon falseIcon )
        {
            this.trueIcon = trueIcon;
            this.falseIcon = falseIcon;
        }

        public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column )
        {
            final JLabel label = (JLabel) super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            final Boolean bool = (Boolean) value;
            label.setText( "" );
            if ( bool.booleanValue() )
            {
                label.setIcon( trueIcon );
            }
            else
            {
                label.setIcon( falseIcon );
            }
            return label;
        }
    }

    public static class SubmitTableModel extends AbstractTableModel
    {
        private Registration[] registrations;

        private final String[] columnNames = {"Nachname", "Vorname", "Leginummer", "Veranstaltung", "Von", "Bis", "Prüfungsliste"};

        public SubmitTableModel( final Registration[] registrations )
        {
            this.setRegistrations( registrations );
        }

        public String getColumnName( final int column )
        {
            return columnNames[column];
        }

        public int getColumnCount()
        {
            return 7;
        }

        public int getRowCount()
        {
            return getRegistrations().length;
        }

        public Object getValueAt( final int rowIndex, final int columnIndex )
        {
            final Registration registration = getRegistrations()[rowIndex];
            final PlantList plantlist = registration.getPlantList();
            final IAnmeldedaten anmeldedaten = registration.getAnmeldedaten();
            switch ( columnIndex )
            {
                case 0:
                    return anmeldedaten.getNachname();
                case 1:
                    return anmeldedaten.getVorname();
                case 2:
                    return anmeldedaten.getStudentennummer();
                case 3:
                    return anmeldedaten.getLkNummer();
                case 4:
                    return anmeldedaten.getPruefungsdatumVon();
                case 5:
                    return anmeldedaten.getPruefungsdatumBis();
                case 6:
                    return new Boolean( plantlist != null && !registration.isDefaultList() );
                default:
                    return null;
            }
        }

        public Class getColumnClass( final int columnIndex )
        {
            switch ( columnIndex )
            {
                case 0:
                case 1:
                case 2:
                case 3:
                    return String.class;
                case 4:
                case 5:
                    return Calendar.class;
                case 6:
                    return Boolean.class;
                default:
                    return null;
            }
        }

        public void remove( final Registration registration )
        {
            final int position = ArrayUtils.indexOf( registrations, registration );
            setRegistrations( (Registration[]) ArrayUtils.remove( registrations, registration, new Registration[0] ) );
            fireTableRowsDeleted( position, position );
        }

        public void add( final Registration registration )
        {
            setRegistrations( (Registration[]) ArrayUtils.add( registrations, registration ) );
            final int index = registrations.length - 1;
            fireTableRowsInserted( index, index );
        }

        public Registration[] getRegistrations()
        {
            return registrations;
        }

        private void setRegistrations( final Registration[] registrations )
        {
            this.registrations = registrations;
            fireTableDataChanged();
        }
    }

    public static class ComboRenderer extends DefaultListCellRenderer
    {
        private static final Color COLOR_DISABLED = UIManager.getColor( "Label.disabledForeground" );

        public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
        {
            super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
            final ExamModel model = (ExamModel) value;
            if ( model != null && model.isFixed() )
            {
                setForeground( isSelected ? list.getSelectionForeground() : list.getForeground() );
            }
            else
            {
                setBackground( list.getBackground() );
                setForeground( list.getForeground() );
                setText( model == null ? "" : getText() + " (nicht fixiert)" );
                setForeground( COLOR_DISABLED );
            }
            return this;
        }
    }
}
