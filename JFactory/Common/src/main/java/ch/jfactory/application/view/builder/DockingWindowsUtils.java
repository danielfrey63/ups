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
package ch.jfactory.application.view.builder;

import ch.jfactory.lang.ArrayUtils;
import java.awt.Color;
import javax.swing.JComponent;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.properties.TabWindowProperties;
import net.infonode.docking.properties.WindowTabProperties;
import net.infonode.docking.properties.WindowTabStateProperties;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanel;

/**
 * Utility class to handle Docking Windows properties.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/08/29 13:10:43 $
 */
public abstract class DockingWindowsUtils {

    public static void setRootWindowProps(final RootWindow rootWindow) {

        setBasicRootWindowProps(rootWindow);

        final Color rootBackgroundColor = rootWindow.getRootWindowProperties().getTabWindowProperties().
                getTabbedPanelProperties().getContentPanelProperties().getComponentProperties().
                getBackgroundColor();
        rootWindow.getRootWindowProperties().getWindowAreaProperties().setBackgroundColor(rootBackgroundColor);
    }

    public static void setBasicRootWindowProps(final RootWindow rootWindow) {

        final RootWindowProperties props = rootWindow.getRootWindowProperties();
        props.getDockingWindowProperties().getTabProperties().getHighlightedButtonProperties().
                getCloseButtonProperties().setVisible(false);
        props.getTabWindowProperties().getTabbedPanelProperties().setTabSpacing(2);
        configureProperties(rootWindow, false, false, false, false, false, false);
    }

    protected static void setTabProps(final WindowTabStateProperties tabProps, final boolean closeButtonVisible,
                                      final boolean minimizeButtonVisible, final boolean restoreButtonVisible,
                                      final boolean dockButtonVisible, final boolean undockButtonVisible) {

        tabProps.getCloseButtonProperties().setVisible(closeButtonVisible);
        tabProps.getMinimizeButtonProperties().setVisible(minimizeButtonVisible);
        tabProps.getRestoreButtonProperties().setVisible(restoreButtonVisible);
//        tabProps.getDockButtonProperties().setVisible(dockButtonVisible);
//        tabProps.getUndockButtonProperties().setVisible(undockButtonVisible);
    }

    public static void configureProperties(final RootWindow rootWindow, final boolean closeButtonVisible,
                                           final boolean minimizeButtonVisible, final boolean restoreButtonVisible,
                                           final boolean maximizeButtonVisilbe, final boolean undockButtonVisible,
                                           final boolean dockButtonVisible) {

        final WindowTabProperties tabProps = rootWindow.getRootWindowProperties().getTabWindowProperties().getTabProperties();
        setTabProps(tabProps.getNormalButtonProperties(), closeButtonVisible, minimizeButtonVisible, restoreButtonVisible, dockButtonVisible, undockButtonVisible);
        setTabProps(tabProps.getHighlightedButtonProperties(), closeButtonVisible, minimizeButtonVisible, restoreButtonVisible, dockButtonVisible, undockButtonVisible);
        setTabProps(tabProps.getFocusedButtonProperties(), closeButtonVisible, minimizeButtonVisible, restoreButtonVisible, dockButtonVisible, undockButtonVisible);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getCloseButtonProperties().setVisible(closeButtonVisible);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getMaximizeButtonProperties().setVisible(maximizeButtonVisilbe);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getMinimizeButtonProperties().setVisible(minimizeButtonVisible);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getRestoreButtonProperties().setVisible(restoreButtonVisible);
//        rootWindow.getRootWindowProperties().getTabWindowProperties().getUndockButtonProperties().setVisible(undockButtonVisible);
//        rootWindow.getRootWindowProperties().getTabWindowProperties().getDockButtonProperties().setVisible(dockButtonVisible);
    }

    public static RootWindow createParentChildDisplay(final JComponent listPanel, final ViewMap views) {

        final TabbedPanel tabbedPanel = new TabbedPanel();
        tabbedPanel.addTab(new Tab(listPanel));

        final View listView = new View("", null, tabbedPanel);
        listView.getViewProperties().setAlwaysShowTitle(false);

        views.addView(0, listView);

        final RootWindow rootWindow = DockingUtil.createRootWindow(views, true);
        setBasicRootWindowProps(rootWindow);
        configureProperties(rootWindow, false, false, false, false, false, false);

        final RootWindowProperties rootWindowProperties = rootWindow.getRootWindowProperties();
        final TabWindowProperties properties = rootWindowProperties.getTabWindowProperties();
        tabbedPanel.getProperties().addSuperObject(properties.getTabbedPanelProperties());

        // Make sure to create the TabWindow AFTER the RootWindow, otherwise the content will be empty!
        final View[] windows = (View[]) ArrayUtils.remove(toArray(views), 0, new View[0]);
        final TabWindow rightTabs = new TabWindow(windows);
        rightTabs.setSelectedTab(0);

        rootWindow.setWindow(new SplitWindow(true, listView, rightTabs));
        rootWindowProperties.getWindowAreaProperties().setBackgroundColor(rootWindowProperties.getComponentProperties().getBackgroundColor());

        return rootWindow;
    }

    public static View[] toArray(final ViewMap views) {

        final int viewCount = views.getViewCount();
        final View[] array = new View[viewCount];
        for (int i = 0; i < viewCount; i++) {
            array[i] = views.getView(i);
        }
        return array;
    }
}
