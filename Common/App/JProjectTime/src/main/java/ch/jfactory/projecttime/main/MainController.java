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
package ch.jfactory.projecttime.main;

import javax.swing.JFrame;
import ch.jfactory.application.AbstractMainController;
import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.binding.InfoModel;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.3 $ $Date: 2006/11/16 13:25:17 $
 */
public class MainController extends AbstractMainController {

    private JFrame parent;
    private MainModel model;

    public MainController(MainModel model, InfoModel infoModel) {
        super(model, infoModel);
        this.model = model;
    }

    protected void initModel(AbstractMainModel model) {
    }
}
