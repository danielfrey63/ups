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
package ch.jfactory.action;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class ActionRedirector extends AbstractAction
{

    private Method method;

    private Object controller;

    public ActionRedirector(final Object controller, final String methodName)
    {
        try
        {
            this.controller = controller;
            this.method = controller.getClass().getMethod(methodName);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    public void actionPerformed(final ActionEvent e)
    {
        try
        {
            method.invoke(controller);
        }
        catch (IllegalAccessException e1)
        {
            System.err.println(method);
            e1.printStackTrace();
        }
        catch (InvocationTargetException e1)
        {
            System.err.println(method);
            e1.printStackTrace();
        }
    }
}
