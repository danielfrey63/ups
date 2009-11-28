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
package ch.jfactory.binding;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.BufferedValueModel;
import javax.swing.ImageIcon;

/**
 * Converts between a an absolute resource path that may be loaded with the class loader and an ImageIcon.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class ResourcePathToIconConverter extends AbstractConverter
{
    public ResourcePathToIconConverter( final BufferedValueModel bufferedModel )
    {
        super( bufferedModel );
    }

    public Object convertFromSubject( final Object subjectValue )
    {
        final String path = (String) subject.getValue();
        ImageIcon icon = null;
        if ( path != null )
        {
            icon = new ImageIcon( getClass().getResource( path ) );
        }
        return icon;
    }

    public void setValue( final Object newValue )
    {
        final ImageIcon icon = (ImageIcon) newValue;
        subject.setValue( icon.getDescription() );
    }
}
