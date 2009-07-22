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
package data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 * Simple main information class. Displays two tabs: (a) one containing all the attributes from the MANIFEST.MF in the
 * same classpath location that this Main class and if none is found the name of the missing MANIFEST.MF, and (b) one
 * containing all the system properties.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/08/04 15:50:02 $
 */
public class Main {

    public static void main(final String[] args) throws Exception {
        final String className = "/data/Main.class";
        final String path = Main.class.getResource(className).toString();
        // JAR files have a "!" and classpath files a "/"
        final String manifestPath = path.substring(0, path.indexOf(className)) + "/META-INF/MANIFEST.MF";
        final JTabbedPane tabs = new JTabbedPane();
        final JComponent manifestComponent;
        if (isValid(manifestPath)) {
            manifestComponent = new JTable(new DefaultTableModel(appendManifestAttributes(manifestPath), new Object[]{"Property", "Value"}));
        } else {
            manifestComponent = new JTextArea("could not find manifest: \n" + manifestPath);
        }
        tabs.add("Manifest", new JScrollPane(manifestComponent));
        tabs.add("System", new JScrollPane(new JTable(new DefaultTableModel(appendSystemProperties(), new Object[]{"Attribute", "Value"}))));
        JOptionPane.showMessageDialog(null, tabs, "Versionsinformation", JOptionPane.INFORMATION_MESSAGE);
    }

    private static Object[][] appendManifestAttributes(final String manifestPath) throws IOException {
        final InputStream stream = new URL(manifestPath).openStream();
        final Manifest manifest = new Manifest(stream);
        final Attributes attributes = manifest.getMainAttributes();
        final Object[][] result = new Object[attributes.size()][];
        int counter = 0;
        for (final Object key : attributes.keySet()) {
            result[counter++] = new Object[]{key.toString(), attributes.get(key)};
        }
        stream.close();
        Arrays.sort(result, new ObjectMatrixComparator());
        return result;
    }

    private static Object[][] appendSystemProperties() {
        final Properties properties = System.getProperties();
        final Object[][] result = new Object[properties.size()][];
        int counter = 0;
        for (final Object property : properties.keySet()) {
            result[counter++] = new Object[]{property, properties.get(property)};
        }
        Arrays.sort(result, new ObjectMatrixComparator());
        return result;
    }

    private static boolean isValid(final String file) {
        boolean result;
        try {
            final URL url = new URL(file);
            final InputStream stream = url.openStream();
            stream.close();
            result = true;
        }
        catch (IOException e) {
            result = false;
        }
        return result;
    }

    private static class ObjectMatrixComparator implements Comparator<Object[]> {
        public int compare(final Object[] o1, final Object[] o2) {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            } else {
                final String s1 = (String) o1[0];
                final String s2 = (String) o2[0];
                return s1.compareTo(s2);
            }
        }
    }
}
