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
package ch.xmatrix.ups.uec.sets.commands;

import ch.jfactory.component.Dialogs;
import ch.jfactory.component.table.SortableTableModel;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.model.SpecimensModel;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.uec.exam.ExamModel;
import ch.xmatrix.ups.uec.groups.GroupsModel;
import ch.xmatrix.ups.uec.level.LevelsModel;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.prefs.PrefsModel;
import ch.xmatrix.ups.uec.sets.ExamsetsCalculator;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import javax.swing.JTextField;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Invokes the examset calculator.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:19:10 $
 */
public class CalculateExams extends ActionCommand
{
    private final SortableTableModel sortableTableModel;

    private final SetBuilder.SubmitTableModel submitTableModel;

    private final ComboBoxAdapter comboModel;

    private final JTextField seed;

    public CalculateExams( final CommandManager commandManager, final ComboBoxAdapter comboModel,
                           final JTextField seed, final SortableTableModel sortableTableModel,
                           final SetBuilder.SubmitTableModel submitTableModel )
    {
        super( commandManager, Commands.COMMANDID_CALCULATE );
        this.sortableTableModel = sortableTableModel;
        this.submitTableModel = submitTableModel;
        this.comboModel = comboModel;
        this.seed = seed;
    }

    protected void handleExecute()
    {
        final String text = seed.getText();
        final String lastSeed;
        if ( !"".equals( text ) )
        {
            final int result = Dialogs.showQuestionMessageCancel( null, "Randon Number Generator",
                    "Das Feld \"Random Seed\" enthält einen Wert. Sind Sie sicher, dass Sie damit den\n" +
                            "Zufallsgenerator auf einen reproduzierbaren Wert festlegen wollen? Wenn ja, klicken Sie\n" +
                            "auf \"Ja\". Wenn Sie den Zufallsgenerator auf einem neuen Zufallswert laufen lassen\n" +
                            "wollen, klicken Sie auf \"Nein\"." );
            if ( result == Dialogs.OK )
            {
                lastSeed = text;
            }
            else
            {
                lastSeed = null;
            }
        }
        else
        {
            lastSeed = null;
        }
        final ExamModel examModel = (ExamModel) comboModel.getSelectedItem();
        final TaxonTree taxa = TaxonModels.find( examModel.getTaxaUid() );
        final PrefsModel prefs = (PrefsModel) MainModel.findModel( examModel.getPrefsUid() );
        final GroupsModel groups = (GroupsModel) MainModel.findModel( examModel.getGroupsUid() );
        final SpecimensModel specimens = (SpecimensModel) MainModel.findModel( examModel.getSpecimensUid() );
        final LevelsModel levels = (LevelsModel) MainModel.findModel( examModel.getLevelsUid() );
        final Constraints constraints = (Constraints) MainModel.findModel( examModel.getConstraintsUid() );

        final Registration[] original = submitTableModel.getRegistrations();
        final Registration[] registrations = new Registration[original.length];
        final int[] indices = sortableTableModel.getIndexes();
        for ( int i = 0; i < indices.length; i++ )
        {
            final int index = indices[i];
            registrations[i] = original[index];
        }
        final ExamsetsCalculator calculator =
                new ExamsetsCalculator( taxa, prefs, groups, specimens, levels, constraints, registrations );
        calculator.setSeed( lastSeed == null ? 0 : Long.parseLong( lastSeed ) );
        calculator.execute();
        seed.setText( "" + calculator.getSeed() );
    }
}
