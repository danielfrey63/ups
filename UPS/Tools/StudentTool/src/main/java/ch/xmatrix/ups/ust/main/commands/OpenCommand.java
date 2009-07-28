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

import ch.jfactory.component.Dialogs;
import ch.jfactory.file.ExtentionFileFilter;
import ch.jfactory.file.OpenChooser;
import ch.jfactory.math.RandomGUID;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.ust.main.MainModel;
import ch.xmatrix.ups.ust.main.UserModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import java.beans.PropertyVetoException;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2008/01/23 22:19:46 $
 */
public class OpenCommand extends ActionCommand {

    private static final Logger LOG = Logger.getLogger(OpenCommand.class);

    private MainModel model;
    private ExtentionFileFilter filter = new ExtentionFileFilter(Strings.getString("openchooser.filetype.description"),
            new String[]{Commands.OLD_FILE_EXTENTION, Commands.NEW_FILE_EXTENTION}, true);

    public OpenCommand(final CommandManager commandManager, final MainModel model) {
        super(commandManager, Commands.COMMANDID_OPEN);
        this.model = model;
    }

    protected void handleExecute() {
        try {
            model.setClosing();
            if (!model.modelValid()) return;
            new PlantlistOpenChooser().open();
        }
        catch (PropertyVetoException e) {
        }
    }

    private class PlantlistOpenChooser extends OpenChooser {

        public PlantlistOpenChooser() {
            super(OpenCommand.this.filter, "openchooser", OpenCommand.this.model.getLastOpenSaveDirectory());
        }

        protected void load(final File file) {
            LOG.info("loading file " + file);
            try {
                List<String> list = null;
                String uid = null;
                String examInfoUid = null;
                if (file.getName().endsWith(Commands.NEW_FILE_EXTENTION)) {
                    final XStream[] decoders = new XStream[]{
                            Commands.getConverterVersion1(), Commands.getConverterVersion2()};
                    boolean done = false;
                    for (int i = 0; i < decoders.length && !done; i++) {
                        Reader reader = null;
                        try {
                            final XStream decoder = decoders[i];
                            reader = new FileReader(file);
                            final Object decoded = decoder.fromXML(reader);
                            if (decoded instanceof Commands.Encoded) {
                                final Commands.Encoded encoded = (Commands.Encoded) decoded;
                                uid = encoded.uid;
                                list = (List<String>) encoded.list;
                                if (list == null) {
                                    reader.close();
                                    continue;
                                }
                                examInfoUid = encoded.exam;
                                done = true;
                            }
                            else if (decoded instanceof PlantList) {
                                final PlantList plantList = (PlantList) decoded;
                                list = plantList.getTaxa();
                                done = true;
                            }
                            else if (decoded instanceof ArrayList) {
                                list = (ArrayList<String>) decoded;
                                done = true;
                            }
                            else {
                                throw new IllegalStateException("Unknown format in file \"" + file + "\"");
                            }
                        }
                        catch (ConversionException e) {
                            if (i < decoders.length - 1) {
                                LOG.info("decoder failed, trying another");
                            }
                            else {
                                LOG.info("no decoder matches");
                            }
                        }
                        finally {
                            if (reader != null) reader.close();
                        }
                    }
                }
                else if (file.getName().endsWith(Commands.OLD_FILE_EXTENTION)) {
                    final InputStream stream = new FileInputStream(file);
                    final XMLDecoder decoder = new XMLDecoder(stream);
                    final Object decoded = decoder.readObject();
                    // Skip person data if there
                    if (!(decoded instanceof ArrayList)) {
                        list = (ArrayList<String>) decoder.readObject();
                    }
                    else {
                        list = (ArrayList<String>) decoded;
                    }
                    stream.close();
                }
                else {
                    model.setError("error.openwrongfileformat");
                    throw new IllegalStateException("only UST and XUST files may be opened.");
                }

                final boolean canceled = false;
                boolean toUpdate = false;
                if (examInfoUid == null || "".equals(examInfoUid) || MainModel.findModel(examInfoUid) == null) {
                    Dialogs.showInfoMessage(model.getMainFrame().getRootPane(), "Hinweis",
                            "Die Pflanzenliste ist von einer früheren Version des UPS Studenten Tools.\n" +
                                    "Sie wird im Folgenden in die neue Version konvertiert. Zuerst wird ein Dialog\n" +
                                    "gezeigt, mit dem Sie die Prüfungskonfiguration für Ihre Liste auswählen können,\n" +
                                    "dann wird ein Speichern-Dialog gezeigt, mit dem Sie die konvertierte Pflanzenliste\n" +
                                    "*unter einem anderen Name* speichern sollten. Verwenden Sie dann nur noch die\n" +
                                    "neue Version.");
                    final SessionModel sessionModel = Commands.runExamInfoChooser(model);
                    if (sessionModel != null) {
                        examInfoUid = sessionModel.getUid();
                        model.sessionModels.setSelection(sessionModel);
                        LOG.info("exam info uid assigned: " + examInfoUid);
                        toUpdate = true;

                    }
                    else {
                        LOG.info("user canceled selection of new exam info uid");
                    }
                }
                if (uid == null || "".equals(uid)) {
                    uid = new RandomGUID().toString();
                    LOG.info("new user info guid generated: " + uid);
                }
                if (!canceled) {

                    model.sessionModels.setSelection(MainModel.findModel(examInfoUid));
                    Commands.setNewUserModel(model);

                    final UserModel userModel = model.getUserModel();
                    userModel.setUid(uid);
                    userModel.setExamInfoUid(examInfoUid);
                    userModel.setTaxa(new ArrayList<String>(list));

                    model.setLastOpenSaveDirectory(file.getParentFile().getAbsolutePath());
                    model.setDirty(false);
                    model.setCurrentFile(file);
                    model.setOpening();
                }
                if (toUpdate) {
                    getCommandManager().getCommand(Commands.COMMANDID_SAVEAS).execute();
                }
            }
            catch (Exception e1) {
                LOG.error("file not compliant with xust format", e1);
            }
        }
    }
}
