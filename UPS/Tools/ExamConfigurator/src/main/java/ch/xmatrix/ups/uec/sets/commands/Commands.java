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

import ch.jfactory.convert.Converter;
import ch.jfactory.xstream.XStreamConverter;
import ch.xmatrix.ups.domain.PersonData;
import ch.xmatrix.ups.domain.PlantList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action command ids.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:48:09 $
 */
public class Commands
{
    public static final String COMMAND_ID_OPEN_FROM_INTERNET = "fromInternet";

    public static final String COMMAND_ID_REFRESH = "refresh";

    public static final String COMMAND_ID_LOAD_FILES = "fromFiles";

    public static final String COMMAND_ID_LOAD_DIRECTORY = "fromDirectory";

    public static final String COMMAND_ID_ADD_PERSON = "newPerson";

    public static final String COMMAND_ID_MOVE_UP = "moveUp";

    public static final String COMMAND_ID_MOVE_DOWN = "moveDown";

    public static final String COMMAND_ID_REMOVE = "remove";

    public static final String COMMAND_ID_CALCULATE = "calculate";

    public static final String COMMAND_ID_RELOAD_SERVER_DATA = "reloadServerData";

    public static final String GROUP_ID_TOOLBAR = "toolbar";

    public static final String FACE_NAME_TOOLBAR = "toolbar";

    public static Converter<ArrayList<String>> getConverter1()
    {
        final Map<String, Class> aliases = new HashMap<String, Class>();
        aliases.put( "person", PersonData.class );
        aliases.put( "root", PlantList.class );
        aliases.put( "ch.xmatrix.ups.ust.main.commands.Commands-Encoded", Encoded.class );
        return new XStreamConverter<ArrayList<String>>( aliases );
    }

    public static Converter<Encoded> getConverter2()
    {
        final Map<String, Class> aliases = new HashMap<String, Class>();
        aliases.put( "person", PersonData.class );
        aliases.put( "root", Encoded.class );
        aliases.put( "ch.xmatrix.ups.ust.main.commands.Commands-Encoded", Encoded.class );
        return new XStreamConverter<Encoded>( aliases );
    }

    public static class Encoded
    {
        public List<String> list;

        public String uid;

        public String exam;
    }
}
