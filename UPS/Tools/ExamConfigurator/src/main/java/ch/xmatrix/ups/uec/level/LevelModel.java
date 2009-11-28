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
package ch.xmatrix.ups.uec.level;

/**
 * Model representing one level and one maximum.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class LevelModel
{
    private String level;

    private int maximum;

    public LevelModel( final String level, final int maximum )
    {
        this.level = level;
        this.maximum = maximum;
    }

    public LevelModel( final LevelModel original )
    {
        this( original.getLevel(), original.getMaximum() );
    }

    public String getLevel()
    {
        return level;
    }

    public int getMaximum()
    {
        return maximum;
    }

    public void setMaximum( final int maximum )
    {
        this.maximum = maximum;
    }

    public String toString()
    {
        return level + "=" + maximum;
    }
}
