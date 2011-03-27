/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.cache;

import java.awt.image.BufferedImage;

public interface ImageCache
{
    boolean isCached( String name );

    BufferedImage getImage( String name ) throws ImageCacheException;

    void setImage( String name, BufferedImage image ) throws ImageCacheException;

    void invalidateCache() throws ImageCacheException;
}
