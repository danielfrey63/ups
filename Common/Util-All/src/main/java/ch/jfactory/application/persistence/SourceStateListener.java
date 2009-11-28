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
package ch.jfactory.application.persistence;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public interface SourceStateListener
{
    /**
     * Notifies for a definite source state change.
     *
     * @param event the source state event
     */
    void sourceStateChanged( SourceStateEvent event );

    /**
     * Notifies for an optional source state change. Make sur the implementation fires a sourceStateChanged Event if
     * appropriate.
     *
     * @param event the source state event
     * @return whether it is ok to fire the source state changed event
     */
    void sourceStateMayChange( SourceStateEvent event ) throws SourceVetoedException;
}
