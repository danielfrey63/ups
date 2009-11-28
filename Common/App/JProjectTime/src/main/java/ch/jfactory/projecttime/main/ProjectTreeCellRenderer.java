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

import ch.jfactory.image.SimpleIconFactory;
import ch.jfactory.lang.DateUtils;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.impl.Invoice;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.2 $ $Date: 2006/11/16 13:25:17 $
 */
public class ProjectTreeCellRenderer extends DefaultTreeCellRenderer
{
    private final SimpleIconFactory factory = new SimpleIconFactory( "/16x16/fill" );

    private final Icon nullIcon = factory.createDropShadowIcon( "file_line0.png", "#FF0000" );

    private final Icon invoiceIcon = factory.createDropShadowIcon( "file_line2.png", "#00FF00" );

    private final ValueModel entry2InvoiceMap;

    public ProjectTreeCellRenderer( final ValueModel entry2InvoiceMap )
    {
        this.entry2InvoiceMap = entry2InvoiceMap;
    }

    private Icon getTimeEntryIcon( IFEntry entry )
    {
        boolean hasInvoice = false;
        final Icon result;
        while ( entry != null && !hasInvoice )
        {
            final Map map = (Map) entry2InvoiceMap.getValue();
            final Invoice invoice = (Invoice) map.get( entry );
            hasInvoice |= ( invoice != null );
            entry = entry.getParent();
        }
        if ( hasInvoice )
        {
            result = invoiceIcon;
        }
        else
        {
            result = nullIcon;
        }
        return result;
    }

    public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean sel, final boolean expanded,
                                                   final boolean leaf, final int row, final boolean hasFocus )
    {
        final JLabel label = (JLabel) super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );
        final IFEntry entry = (IFEntry) value;
        final StringBuffer buffer = new StringBuffer();
        final Calendar start = entry.getStart();
        final Calendar end = entry.getEnd();
        if ( start != null && end != null )
        {
            final SimpleDateFormat startFormat;
            if ( DateUtils.isSameDay( start, end ) )
            {
                startFormat = new SimpleDateFormat( "HH:mm" );
            }
            else if ( DateUtils.isSameMonth( start, end ) )
            {
                startFormat = new SimpleDateFormat( "HH:mm dd." );
            }
            else if ( DateUtils.isSameYear( start, end ) )
            {
                startFormat = new SimpleDateFormat( "HH:mm dd.MM." );
            }
            else
            {
                startFormat = new SimpleDateFormat( "HH:mm dd.MM.yyyy" );
            }
            buffer.append( startFormat.format( start ) );
            buffer.append( " - " );
            final SimpleDateFormat endFormat = new SimpleDateFormat( "HH:mm dd.MM.yyyy" );
            buffer.append( endFormat.format( end ) );
            buffer.append( " (" );
            buffer.append( DateUtils.dateDifference( start.getTime().getTime(), end.getTime().getTime(), "HH:mm" ) );
            buffer.append( ")" );
            label.setIcon( getTimeEntryIcon( entry ) );
        }
        else if ( !"".equals( entry.getName() ) )
        {
            buffer.append( entry.getName() );
        }
        label.setText( buffer.toString() );
        return label;
    }
}
