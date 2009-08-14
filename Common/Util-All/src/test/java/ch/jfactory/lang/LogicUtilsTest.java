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
package ch.jfactory.lang;

import junit.framework.TestCase;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/03/22 15:05:10 $
 */
public class LogicUtilsTest extends TestCase
{

    public void testGetFullIndex()
    {
        final boolean[][] cases = {
                {false, false, false},
                {false, false, true},
                {false, true, false},
                {false, true, true},
                {true, false, false},
                {true, false, true},
                {true, true, false},
                {true, true, true}
        };
        final int[] result = {0, 1, 2, 3, 4, 5, 6, 7};
        for (int i = 0; i < cases.length; i++)
        {
            final boolean[] caze = cases[i];
            assertEquals(result[i], LogicUtils.getFullIndex(caze));
        }
    }
}
