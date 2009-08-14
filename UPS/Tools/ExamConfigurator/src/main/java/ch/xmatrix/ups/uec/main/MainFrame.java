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
package ch.xmatrix.ups.uec.main;

import ch.jfactory.binding.InfoModel;
import com.jgoodies.uif.AbstractFrame;
import com.jgoodies.uif.application.Application;
import javax.swing.JComponent;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/03/22 15:01:40 $
 */
public class MainFrame extends AbstractFrame
{

    private MainModel mainModel;

    private InfoModel infoModel;

    public MainFrame(final MainModel mainModel, final InfoModel infoModel)
    {
        super(Application.getDescription().getWindowTitle());
        this.mainModel = mainModel;
        this.infoModel = infoModel;
    }

    public JComponent buildContentPane()
    {
        return new MainBuilder(mainModel, infoModel).getPanel();
    }

    protected void configureCloseOperation()
    {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(Application.getApplicationCloseOnWindowClosingHandler());
    }

    public String getWindowID()
    {
        return "UPSExamConfiguratorMainFrame";
    }
}
