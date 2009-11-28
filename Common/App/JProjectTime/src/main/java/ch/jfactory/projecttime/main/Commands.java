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
package ch.jfactory.projecttime.main;

import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.impl.DefaultEntry;
import ch.jfactory.projecttime.domain.impl.Invoice;
import ch.jfactory.typemapper.TypeMapping;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.ISO8601DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import java.util.Map;

/**
 * Action command ids and serializer accessor.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class Commands
{
    public static final String COMMANDID_IMPORT = "file.import";

    public static final String COMMANDID_OPEN = "file.open";

    public static final String COMMANDID_SAVEAS = "file.saveas";

    public static final String COMMANDID_EDITTYPES = "edit.types";

    public static final String COMMANDID_ADDINVOICE = "invoice.add";

    public static final String COMMANDID_REMOVEINVOICE = "invoice.remove";

    public static final String GROUPID_MENU = "menu.main";

    public static final String GROUPID_INVOICE = "invoice";

    public static XStream getSerializer()
    {
        final XStream stream = new XStream( new DomDriver() );
        stream.setMode( XStream.ID_REFERENCES );
        stream.alias( "data", Data.class );
        stream.alias( "entry", DefaultEntry.class );
        stream.alias( "type", TypeMapping.class );
        stream.alias( "invoice", Invoice.class );
        stream.registerConverter( new ISO8601DateConverter() );
        return stream;
    }

    public static class Data
    {
        public IFEntry root;

        public List types;

        public List invoices;

        public Map entry2InvoiceMap;
    }
}
