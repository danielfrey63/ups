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

package ch.jfactory.filter;

/**
 * Partial implementation of {@link org.jdom.filter.Filter}.
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.1 $, $Date: 2005/11/17 11:54:58 $
 */
public abstract class AbstractFilter implements Filter {

    public Filter negate() {
        return new NegateFilter(this);
    }

    public Filter or(final Filter filter) {
        return new OrFilter(this, filter);
    }

    public Filter and(final Filter filter) {
        return new AndFilter(this, filter);
    }

}
