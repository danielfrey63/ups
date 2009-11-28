/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.uec;

import ch.jfactory.application.MainRunner;

/**
 * Local main runner subclass to put entrypoint into this module.
 *
 * @author Daniel Frey
 * @version $Revision: 1.13 $ $Date: 2008/01/23 22:19:08 $
 */
public class Main extends MainRunner
{
    public static void main( final String[] args )
    {
//        System.setProperty("ch.jfactory.laf", "net.sourceforge.napkinlaf.NapkinLookAndFeel");
        MainRunner.main( args );
    }
}