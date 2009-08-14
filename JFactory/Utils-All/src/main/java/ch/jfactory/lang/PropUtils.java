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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Property Utilities.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/25 11:09:31 $
 */
public final class PropUtils {

    /**
     * Filters properties according to the given prefix. The prefix is removed from the resulting property keys. Keys
     * equaling to the prefix given are stored with the given default as key.
     *
     * @param properties the properties to filter
     * @param keyPrefix  the prefix to use for the keys
     * @param defaultKey
     * @return a new Properties object containing the found mappings or null if none could be found or input properties
     *         are null.
     */
    public static Properties filterProperties(final Properties properties, final String keyPrefix, final String defaultKey) {
        if (properties == null) {
            return null;
        }
        final Enumeration e = properties.keys();
        final Properties p = new Properties();
        while (e.hasMoreElements()) {
            final String key = e.nextElement().toString();
            if (key.equals(keyPrefix)) {
                p.put(defaultKey, properties.get(key));
            }
            else if (key.startsWith(keyPrefix)) {
                p.put(key.substring(keyPrefix.length() + 1), properties.get(key));
            }
        }
        return (p.isEmpty() ? null : p);
    }

    /**
     * Extracts all values of properties where the key starts with the given prefix.
     *
     * @param properties
     * @param prefix
     */
    public static String[] filterProperties(final Properties properties, final String prefix) {
        final Enumeration<?> enumeration = properties.propertyNames();
        final TreeMap<String, String> map = new TreeMap<String, String>();
        while (enumeration.hasMoreElements()) {
            final String key = (String) enumeration.nextElement();
            if (key.startsWith(prefix)) {
                map.put(key, properties.getProperty(key));
            }
        }
        final ArrayList<String> list = new ArrayList<String>(map.values());
        return (String[]) list.toArray(new String[0]);
    }

    public static void main(final String[] args) {
        final Properties p = new Properties();
        p.put("asdf.03", "03");
        p.put("asdf.12", "12");
        p.put("asdf.00", "00");
        p.put("asdf.11", "11");
        p.put("asdf.13", "13");
        p.put("asdf.02", "02");
        p.put("asdf.10", "10");
        p.put("asdf.01", "01");
        final String[] s = filterProperties(p, "asdf.");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
    }
}
