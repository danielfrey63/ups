/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.table;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Table Model to display a list of beans. BeanTableModel could only handle a list of the same bean types.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.4 $ $Date: 2007/09/27 10:41:47 $
 */
public class BeanTableModel extends DefaultTableModel
{
    private List list;

    private PropertyDescriptor[] columnDescriptors;

    private boolean readOnly = false;

    /**
     * Create a new table model.
     *
     * @param list             a list of beans
     * @param columnProperties an array of property names to display in the table
     * @param readOnly         model should be hanlded read only
     * @param columnNames      the names of the columns
     */
    public BeanTableModel( final List list, final String[] columnProperties, final String[] columnNames, final boolean readOnly )
    {
        this.readOnly = readOnly;
        final Object[][] infos = new Object[list.size()][columnProperties.length];
        if ( list.size() > 0 )
        {

            if ( columnNames.length != columnProperties.length )
            {
                throw new IllegalArgumentException( "Properties and names array must be of same length." );
            }
            this.list = list;

            // hole descriptor für bean
            final BeanInfo beanInfo;
            try
            {
                beanInfo = Introspector.getBeanInfo( list.get( 0 ).getClass() );
            }
            catch ( IntrospectionException ex )
            {
                throw new IllegalArgumentException( "List contains faulty beans" );
            }

            // map alle columns
            columnDescriptors = new PropertyDescriptor[columnProperties.length];
            final PropertyDescriptor[] descs = beanInfo.getPropertyDescriptors();
            for ( int i = 0; i < columnProperties.length; i++ )
            {
                final String name = columnProperties[i];
                for ( final PropertyDescriptor desc : descs )
                {
                    if ( desc.getName().equals( name ) )
                    {
                        columnDescriptors[i] = desc;
                        break;
                    }
                }
            }

            int row = 0;
            for ( Iterator iterator = list.iterator(); iterator.hasNext(); row++ )
            {
                final Object o = iterator.next();
                for ( int col = 0; col < columnDescriptors.length; col++ )
                {
                    final PropertyDescriptor desc = columnDescriptors[col];
                    try
                    {
                        infos[row][col] = desc.getReadMethod().invoke( o );
                    }
                    catch ( Exception ex )
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
        super.setDataVector( infos, columnNames );
    }

    public BeanTableModel( final List list, final String[] columnProperties )
    {
        this( list, columnProperties, columnProperties, false );
    }

    public void setValueAt( final Object value, final int row, final int column )
    {
        final Object o = list.get( row );

        final PropertyDescriptor desc = columnDescriptors[column];
        try
        {
            desc.getWriteMethod().invoke( o, value );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        super.setValueAt( value, row, column );
    }

    public boolean isCellEditable( final int row, final int column )
    {
        if ( readOnly )
        {
            return false;
        }
        else
        {
            final PropertyDescriptor desc = columnDescriptors[column];
            return ( desc.getWriteMethod() != null );
        }
    }
}
