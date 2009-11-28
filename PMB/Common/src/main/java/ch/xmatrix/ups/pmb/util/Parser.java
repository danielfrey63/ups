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
package ch.xmatrix.ups.pmb.util;

import ch.xmatrix.ups.pmb.ui.model.Settings;
import java.io.File;
import java.io.FilenameFilter;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:37 $
 */
public abstract class Parser
{
    protected Settings settings = new Settings();

    public Parser( final Settings settings )
    {
        this.settings = settings;
    }

    protected final FilenameFilter filter = new FilenameFilter()
    {
        public boolean accept( final File dir, final String name )
        {
            final File file = new File( dir, name );
            return file.isDirectory() || settings.isAllowedFileName( name );
        }
    };

    public void setSettings( final Settings settings )
    {
        this.settings = settings;
    }
}
