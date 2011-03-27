/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.bean;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class adds some method to get the EventSetDescriptor, MethodDescriptor and PropertyDescriptor by name. It is a decorator for a BeanInfo object.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class ExtendedBeanInfo implements BeanInfo
{
    private final BeanInfo beanInfo;

    private Map eventSetDescriptors = Collections.EMPTY_MAP;

    private Map methodDescriptors = Collections.EMPTY_MAP;

    private Map propertyDescriptors = Collections.EMPTY_MAP;

    /**
     * Creates a new instance of ExtendedBeanInfo
     *
     * @param beanInfo the depending BeanInfo object
     */
    public ExtendedBeanInfo( final BeanInfo beanInfo )
    {
        this.beanInfo = beanInfo;

        // fill maps
        final EventSetDescriptor[] esd = beanInfo.getEventSetDescriptors();
        if ( esd.length > 0 )
        {
            eventSetDescriptors = new HashMap( esd.length );
            for ( final EventSetDescriptor anEsd : esd )
            {
                eventSetDescriptors.put( anEsd.getName(), anEsd );
            }
        }

        final MethodDescriptor[] md = beanInfo.getMethodDescriptors();
        if ( md.length > 0 )
        {
            methodDescriptors = new HashMap( md.length );
            for ( final MethodDescriptor aMd : md )
            {
                methodDescriptors.put( aMd.getName(), aMd );
            }
        }

        final PropertyDescriptor[] pd = beanInfo.getPropertyDescriptors();
        if ( pd.length > 0 )
        {
            propertyDescriptors = new HashMap( pd.length );
            for ( final PropertyDescriptor aPd : pd )
            {
                propertyDescriptors.put( aPd.getName(), aPd );
            }
        }
    }

    /** @see BeanInfo#getAdditionalBeanInfo() */
    public BeanInfo[] getAdditionalBeanInfo()
    {
        return beanInfo.getAdditionalBeanInfo();
    }

    /** @see BeanInfo#getBeanDescriptor() */
    public BeanDescriptor getBeanDescriptor()
    {
        return beanInfo.getBeanDescriptor();
    }

    /** @see BeanInfo#getDefaultEventIndex() */
    public int getDefaultEventIndex()
    {
        return beanInfo.getDefaultEventIndex();
    }

    /** @see BeanInfo#getDefaultPropertyIndex() */
    public int getDefaultPropertyIndex()
    {
        return beanInfo.getDefaultPropertyIndex();
    }

    /** @see BeanInfo#getIcon(int) */
    public Image getIcon( final int iconKind )
    {
        return beanInfo.getIcon( iconKind );
    }

    /** @see BeanInfo#getEventSetDescriptors() */
    public EventSetDescriptor[] getEventSetDescriptors()
    {
        return beanInfo.getEventSetDescriptors();
    }

    /**
     * Return the requested EventSetDescriptor. If the EventSetDescriptor is not found a NoSuchElementException is thrown.
     *
     * @param name name of the EventSetDescriptor
     * @return requested EventSetDescriptor
     * @throws NoSuchElementException if the EventSetDescriptor is not found
     */
    public EventSetDescriptor getEventSetDescriptor( final String name )
            throws NoSuchElementException
    {
        final EventSetDescriptor
                desc = (EventSetDescriptor) eventSetDescriptors.get( name );
        if ( desc == null )
        {
            throw new NoSuchElementException( "EventSetDescriptor with name " +
                    name + " not found." );
        }
        return desc;
    }

    /** @see BeanInfo#getMethodDescriptors() */
    public MethodDescriptor[] getMethodDescriptors()
    {
        return beanInfo.getMethodDescriptors();
    }

    /**
     * Return the requested MethodDescriptor. If the MethodDescriptor is not found a NoSuchElementException is thrown.
     *
     * @param name name of the MethodDescriptor
     * @return requested MethodDescriptor
     * @throws NoSuchElementException if the MethodDescriptor is not found
     */
    public MethodDescriptor getMethodDescriptor( final String name )
            throws NoSuchElementException
    {
        final MethodDescriptor
                desc = (MethodDescriptor) methodDescriptors.get( name );
        if ( desc == null )
        {
            throw new NoSuchElementException( "MethodDescriptor with name " +
                    name + " not found." );
        }
        return desc;
    }

    /** @see BeanInfo#getPropertyDescriptors() */
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return beanInfo.getPropertyDescriptors();
    }

    /**
     * Return the requested PropertyDescriptor. If the PropertyDescriptor is not found a NoSuchElementException is thrown.
     *
     * @param name name of the PropertyDescriptor
     * @return requested PropertyDescriptor
     * @throws NoSuchElementException if the PropertyDescriptor is not found
     */
    public PropertyDescriptor getPropertyDescriptor( final String name )
            throws NoSuchElementException
    {
        final PropertyDescriptor
                desc = (PropertyDescriptor) propertyDescriptors.get( name );
        if ( desc == null )
        {
            throw new NoSuchElementException( "PropertyDescriptor with name " +
                    name + " not found." );
        }
        return desc;
    }

    /**
     * check if the bean has the EventSetDescriptor with the given name.
     *
     * @param name name of the EventSetDescriptor
     * @return true = available, false = unavailable
     */
    public boolean hasEventSetDescriptor( final String name )
    {
        return eventSetDescriptors.containsKey( name );
    }

    /**
     * check if the bean has the EventSetDescriptor with the given names.
     *
     * @param names array of EventSetDescriptor names
     * @return true = available, false = unavailable
     */
    public boolean hasEventSetDescriptors( final String[] names )
    {
        boolean exists = true;
        for ( int i = 0; i < names.length && exists; i++ )
        {
            exists = hasEventSetDescriptor( names[i] );
        }
        return exists;
    }

    /**
     * check if the bean has the MethodDescriptor with the given name.
     *
     * @param name name of the MethodDescriptor
     * @return true = available, false = unavailable
     */
    public boolean hasMethodDescriptor( final String name )
    {
        return methodDescriptors.containsKey( name );
    }

    /**
     * check if the bean has the MethodDescriptor with the given names.
     *
     * @param names array of MethodDescriptor names
     * @return true = available, false = unavailable
     */
    public boolean hasMethodDescriptors( final String[] names )
    {
        boolean exists = true;
        for ( int i = 0; i < names.length && exists; i++ )
        {
            exists = hasMethodDescriptor( names[i] );
        }
        return exists;
    }

    /**
     * check if the bean has the PropertyDescriptor with the given name.
     *
     * @param name name of the PropertyDescriptor
     * @return true = available, false = unavailable
     */
    public boolean hasPropertyDescriptor( final String name )
    {
        return propertyDescriptors.containsKey( name );
    }

    /**
     * check if the bean has the PropertyDescriptor with the given names.
     *
     * @param names array of PropertyDescriptor names
     * @return true = available, false = unavailable
     */
    public boolean hasPropertyDescriptors( final String[] names )
    {
        boolean exists = true;
        for ( int i = 0; i < names.length && exists; i++ )
        {
            exists = hasPropertyDescriptor( names[i] );
        }
        return exists;
    }
}
