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

import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.PersonData;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.ust.edit.SessionListRenderer;
import ch.xmatrix.ups.ust.main.MainModel;
import ch.xmatrix.ups.ust.main.UserModel;
import com.jgoodies.binding.list.SelectionInList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;

/**
 * Action command ids.
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2007/05/16 17:00:16 $
 */
public class Commands
{
    public static final String COMMANDID_NEW = "file.new";

    public static final String COMMANDID_NEWDEFAULT = "file.newdefault";

    public static final String COMMANDID_OPEN = "file.open";

    public static final String COMMANDID_SAVE = "file.save";

    public static final String COMMANDID_SAVEAS = "file.saveas";

    public static final String COMMANDID_CLOSE = "file.close";

    public static final String COMMANDID_EXPORTTEXT = "file.exporttext";

    public static final String COMMANDID_EXPORTTREE = "file.exporttree";

    public static final String COMMANDID_SUBMIT = "file.submit";

    public static final String COMMANDID_UPSHOME = "inet.upshome";

    public static final String COMMANDID_USTHOME = "inet.usthome";

    public static final String COMMANDID_USTEXAM = "inet.ustexam";

    public static final String COMMANDID_USTHELP = "inet.usthelp";

    public static final String COMMANDID_USTFAQ = "inet.ustfaq";

    public static final String COMMANDID_USTBUGS = "inet.ustbugs";

    public static final String COMMANDID_USTINFO = "inet.ustinfo";

    public static final String GROUPID_MENUBAR = "menubar";

    public static final String GROUPID_FILEMENU = "menu.file";

    public static final String GROUPID_HELPMENU = "menu.help";

    public static final String OLD_FILE_EXTENTION = ".ust";

    public static final String NEW_FILE_EXTENTION = ".xust";

    public static XStream getConverterVersion1()
    {
        final XStream converter;
        converter = new XStream( new DomDriver() );
        converter.setMode( XStream.ID_REFERENCES );
        converter.alias( "person", PersonData.class );
        converter.alias( "root", PlantList.class );
        converter.alias( "species", List.class );

        return converter;
    }

    public static XStream getConverterVersion2()
    {
        final XStream converter;
        converter = new XStream( new DomDriver() );
        converter.setMode( XStream.ID_REFERENCES );
        converter.registerConverter( new SqlTimestampConverter() );
        converter.registerConverter( new DateConverter( "yyyyMMddHHmmssSSS", new String[]{
                "yyyyMMddHHmmssSSS", "yyyyMMddHHmmss", "yyyyMMddHHmm"} ) );
        converter.alias( "person", PersonData.class );
        converter.alias( "root", Encoded.class );

        return converter;
    }

    public static XStream getConverter()
    {
        return getConverterVersion2();
    }

    /**
     * Returns whether the choice was canceled.
     *
     * @param model the main model
     * @return whether the choice was canceled
     * @throws PropertyVetoException if the user cancels quitting
     */
    public static SessionModel runExamInfoChooser( final MainModel model ) throws PropertyVetoException
    {
        model.setClosing();
        if ( !model.modelValid() )
        {
            return null;
        }
        final SelectionInList sessionModels = model.sessionModels;
        final int numberOfSessions = sessionModels.getSize();
        if ( numberOfSessions > 1 )
        {
            final JFrame top = model.getMainFrame();
            final SimpleModelList holder = (SimpleModelList) sessionModels.getListHolder().getValue();
            final ListDialog dialog = new ListDialog( top, "chooseexam", SimpleModelList.toArray( holder ) );
            dialog.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            dialog.setListCellRenderer( new SessionListRenderer() );
            dialog.setSize( new Dimension( 400, 700 ) );
            WindowUtils.centerOnComponent( dialog, top );
            dialog.setVisible( true );
            if ( !dialog.isAccepted() )
            {
                return null;
            }
            else
            {
                return (SessionModel) dialog.getSelectedData()[0];
            }
        }
        else
        {
            return (SessionModel) sessionModels.getElementAt( 0 );
        }
    }

    public static void setNewUserModel( final MainModel model )
    {
        final SessionModel sessionModel = (SessionModel) model.sessionModels.getSelection();
        final UserModel userModel = new UserModel();
        model.setUserModel( userModel );

        final Constraints constraints = (Constraints) AbstractMainModel.findModel( sessionModel.getConstraintsUid() );
        userModel.setExamInfoUid( sessionModel.getUid() );
        userModel.setConstraintsUid( sessionModel.getConstraintsUid() );
        userModel.setTaxaUid( constraints.getTaxaUid() );
        userModel.setTaxa( constraints.getObligateTaxa() );

        model.setCurrentFile( null );
        model.setCurrentCard( MainModel.CARDS_EDIT );
        model.setDirty( false );
    }

    public static class Encoded
    {
        public List list;

        public String uid;

        public String exam;
    }
}
