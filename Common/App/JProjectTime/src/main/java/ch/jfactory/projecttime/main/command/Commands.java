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
package ch.jfactory.projecttime.main.command;

import ch.jfactory.projecttime.domain.impl.DefaultEntry;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:10 $
 */
public class Commands
{
    public static final String MAIN_MENU_GROUP = "menu.main";

    public static final String FILE_IMPORT = "file.import";

    public static final String FILE_OPEN = "file.open";

    public static final String FILE_SAVEAS = "file.saveas";

    public static XStream getSerializer()
    {
        final XStream stream = new XStream( new DomDriver() );
        stream.setMode( XStream.ID_REFERENCES );
        stream.alias( "entry", DefaultEntry.class );
        stream.registerConverter( new ISO8601CalendarConverter() );
        return stream;
    }
}
