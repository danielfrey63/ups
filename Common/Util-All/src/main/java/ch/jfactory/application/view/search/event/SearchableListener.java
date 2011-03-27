/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.search.event;

import java.util.EventListener;

// Referenced classes of package com.jidesoft.swing.event:
//            SearchableEvent

public interface SearchableListener
        extends EventListener
{
    public abstract void searchableEventFired( SearchableEvent searchableevent );
}