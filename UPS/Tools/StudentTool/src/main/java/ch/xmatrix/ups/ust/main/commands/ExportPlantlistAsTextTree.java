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
package ch.xmatrix.ups.ust.main.commands;

import ch.jfactory.file.ExtentionFileFilter;
import ch.jfactory.file.SaveChooser;
import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.ust.main.MainModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.lang.StringUtils;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Asks for a text file and exports all selected taxa to a text-like tree.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/08/29 13:33:56 $
 */
public class ExportPlantlistAsTextTree extends ActionCommand {

    private MainModel model;
    private FileFilter textFileFilter = new ExtentionFileFilter("Textdatei (*.txt)", new String[]{".txt"}, true);

    public ExportPlantlistAsTextTree(final CommandManager commandManager, final MainModel model) {
        super(commandManager, Commands.COMMANDID_EXPORTTREE);
        this.model = model;
    }

    protected void handleExecute() {
        new SaveChooser(textFileFilter, "textchooser", model.getLastExportDirectory()) {
            public void save(final File file) {
                if (file != null) {
                    try {
                        final FileWriter fileWriter = new FileWriter(file);
                        final BufferedWriter out = new BufferedWriter(fileWriter);
                        final ArrayList<String> taxa = model.getUserModel().getTaxa();
                        final TaxonTree tree = TaxonModels.find(model.getUserModel().getTaxaUid());
                        final int depth = tree.findTaxonByName(taxa.get(0)).getLevel().getRank() + 1;
                        final SimpleTaxon[] printedParents = new SimpleTaxon[depth];
                        for (int i = 0; i < taxa.size(); i++) {
                            final String name = taxa.get(i);
                            final SimpleTaxon taxon = tree.findTaxonByName(name);
                            printTaxon(taxon, out, printedParents);
                            out.flush();
                        }
                        out.close();
                        fileWriter.close();
                        model.setLastExportDirectory(file.getParentFile().getAbsolutePath());
                    }
                    catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        }.open();
    }

    private void printTaxon(final SimpleTaxon taxon, final Writer out, final SimpleTaxon[] printed) throws IOException {
        final SimpleTaxon parent = taxon.getParentTaxon();
        final SimpleLevel level = taxon.getLevel();
        final int indent = (level == null ? 0 : level.getRank());
        if (parent != null) {
            printTaxon(parent, out, printed);
        }
        if (taxon != printed[indent]) {
            out.write(StringUtils.repeat("  ", indent) + taxon.getName() + System.getProperty("line.separator"));
            printed[indent] = taxon;
            out.flush();
        }
    }
}
