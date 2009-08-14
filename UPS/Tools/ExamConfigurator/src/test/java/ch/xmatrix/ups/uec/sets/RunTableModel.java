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

import ch.jfactory.component.table.SortableTableModel;
import ch.jfactory.component.table.TableUtils;
import ch.jfactory.lang.ArrayUtils;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.Registration;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/23 22:19:16 $
 */
public class RunTableModel
{

    public static void main(final String[] args)
    {
        final JFrame f = new JFrame();
        final JTable t = new JTable();
        t.setDefaultRenderer(Calendar.class, new TableUtils.CalendarCellRenderer("HH:mm dd.MM.yyyy"));
        final TableModel tm = new TableModel(new Registration[0]);
        final SortableTableModel outer = new SortableTableModel(tm);
        t.setModel(outer);
        f.add(t, BorderLayout.CENTER);
        final JButton b = new JButton("Add");
        b.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                final Anmeldedaten anmeldedaten = new Anmeldedaten();
                anmeldedaten.setNachname("Frey");
                anmeldedaten.setVorname("Daniel");
                tm.add(new Registration(anmeldedaten, new PlantList()));
            }
        });
        f.add(b, BorderLayout.SOUTH);
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static class TableModel extends AbstractTableModel
    {

        private Registration[] registrations;

        private String[] columnNames = {"Nachname", "Vorname", "Leginummer", "Veranstaltung", "Von", "Bis", "Prüfungsliste"};

        public TableModel(final Registration[] registrations)
        {
            this.setRegistrations(registrations);
        }

        public String getColumnName(final int column)
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

        public Object getValueAt(final int rowIndex, final int columnIndex)
        {
            final Registration registration = getRegistrations()[rowIndex];
            final PlantList plantlist = registration.getPlantList();
            final IAnmeldedaten anmeldedaten = registration.getAnmeldedaten();
            switch (columnIndex)
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
                    return new Boolean(plantlist != null && !registration.isDefaultList());
                default:
                    return null;
            }
        }

        public Class getColumnClass(final int columnIndex)
        {
            switch (columnIndex)
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

        public void remove(final Registration registration)
        {
            setRegistrations((Registration[]) ArrayUtils.remove(registrations, registration, new Registration[0]));
            final int index = registrations.length;
            fireTableRowsDeleted(index, index);
        }

        public void add(final Registration registration)
        {
            setRegistrations((Registration[]) ArrayUtils.add(registrations, registration));
            final int index = registrations.length - 1;
            fireTableRowsInserted(index, index);
        }

        public Registration[] getRegistrations()
        {
            return registrations;
        }

        private void setRegistrations(final Registration[] registrations)
        {
            this.registrations = registrations;
            fireTableDataChanged();
        }
    }
}
