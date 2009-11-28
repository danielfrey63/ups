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
import ch.xmatrix.ups.ust.main.MainModel;
import ch.xmatrix.ups.ust.main.UserModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Export all selected taxa to a simple flat text list.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/07/27 16:38:57 $
 */
public class ExportTextCommand extends ActionCommand
{
    private final MainModel model;

    private final FileFilter textFileFilter = new ExtentionFileFilter( "Textdatei (*.txt)", new String[]{".txt"}, true );

    public ExportTextCommand( final CommandManager commandManager, final MainModel model )
    {
        super( commandManager, Commands.COMMANDID_EXPORTTEXT );
        this.model = model;
    }

    protected void handleExecute()
    {
        new TextSaveChooser().open();
    }

    private class TextSaveChooser extends SaveChooser
    {
        public TextSaveChooser()
        {
            super( textFileFilter, "textchooser", "" );
        }

        public void save( final File file )
        {
            if ( file != null )
            {
                try
                {
                    final FileWriter fileWriter = new FileWriter( file );
                    final BufferedWriter out = new BufferedWriter( fileWriter );
                    final UserModel userModel = model.getUserModel();
                    final ArrayList<String> taxa = userModel.getTaxa();
                    for ( final String taxon : taxa )
                    {
                        out.write( taxon + System.getProperty( "line.separator" ) );
                    }
                    out.close();
                    fileWriter.close();
                }
                catch ( IOException x )
                {
                    x.printStackTrace();
                }
            }
        }
    }
}
