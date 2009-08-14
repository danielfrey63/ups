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

import ch.xmatrix.ups.domain.PersonData;
import ch.xmatrix.ups.domain.PlantList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;

/**
 * Action command ids.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:48:09 $
 */
public class Commands
{

    public static final String COMMANDID_OPENFROMINTERNET = "fromInternet";

    public static final String COMMANDID_REFRESH = "refresh";

    public static final String COMMANDID_LOADFILES = "fromFiles";

    public static final String COMMANDID_LOADDIRECTORY = "fromDirectory";

    public static final String COMMANDID_ADDPERSION = "newPerson";

    public static final String COMMANDID_MOVEUP = "moveUp";

    public static final String COMMANDID_MOVEDOWN = "moveDown";

    public static final String COMMANDID_REMOVE = "remove";

    public static final String COMMANDID_CALCULATE = "calculate";

    public static final String COMMANDID_RELOADSERVERDATA = "reloadServerData";

    public static final String GROUPID_TOOLBAR = "toolbar";

    public static final String FACENAME_TOOLBAR = "toolbar";

    public static XStream getConverter1()
    {
        final XStream converter;
        converter = new XStream(new DomDriver());
        converter.setMode(XStream.ID_REFERENCES);
        converter.alias("person", PersonData.class);
        converter.alias("root", PlantList.class);
        converter.alias("ch.xmatrix.ups.ust.main.commands.Commands-Encoded", Encoded.class);
        return converter;
    }

    public static XStream getConverter2()
    {
        final XStream converter;
        converter = new XStream(new DomDriver());
        converter.setMode(XStream.ID_REFERENCES);
        converter.alias("root", Encoded.class);
        return converter;
    }

    public static class Encoded
    {
        public List<String> list;

        public String uid;

        public String exam;
    }
}
