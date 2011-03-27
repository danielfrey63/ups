/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.convert;

import java.io.Reader;

/**
 * Interface hiding the implementation of a specific serializer.
 *
 * @author Daniel Frey 01.01.2010 14:35:29
 */
public interface Converter<T>
{
    T from( Reader reader );

    String from( T object );
}
