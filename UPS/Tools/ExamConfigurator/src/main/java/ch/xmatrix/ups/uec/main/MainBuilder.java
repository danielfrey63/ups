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

import ch.jfactory.application.AbstractMainBuilder;
import ch.jfactory.application.view.builder.DockingWindowsUtils;
import ch.jfactory.binding.DefaultInfoModel;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.command.QuitCommand;
import ch.jfactory.component.I15nWelcomePanel;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.uec.constraints.ConstraintsBuilder;
import ch.xmatrix.ups.uec.exam.ExamsBuilder;
import ch.xmatrix.ups.uec.groups.GroupsBuilder;
import ch.xmatrix.ups.uec.level.LevelsBuilder;
import ch.xmatrix.ups.uec.main.commands.CloseCommand;
import ch.xmatrix.ups.uec.main.commands.Commands;
import ch.xmatrix.ups.uec.main.commands.ExportCommand;
import ch.xmatrix.ups.uec.main.commands.OpenCommand;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import ch.xmatrix.ups.uec.prefs.PrefsBuilder;
import ch.xmatrix.ups.uec.session.SessionBuilder;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import ch.xmatrix.ups.uec.specimens.SpecimensBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import net.infonode.docking.RootWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.16 $ $Date: 2008/01/23 22:19:08 $
 */
public class MainBuilder extends AbstractMainBuilder
{

    private static final Logger LOG = Logger.getLogger(MainBuilder.class);

    private static final String TOOL_PREFIX = "jfactory.ups.tool.";

    private final AbstractDetailsBuilder prefsBuilder = new PrefsBuilder();

    private final AbstractDetailsBuilder groupsBuilder = new GroupsBuilder();

    private final AbstractDetailsBuilder specimensBuilder = new SpecimensBuilder();

    private final AbstractDetailsBuilder levelsBuilder = new LevelsBuilder();

    private final AbstractDetailsBuilder constraintsBuilder = new ConstraintsBuilder();

    private final ExamsBuilder examsBuilder = new ExamsBuilder(prefsBuilder, groupsBuilder, specimensBuilder, levelsBuilder, constraintsBuilder);

    private final AbstractDetailsBuilder examInfoBuilder = new SessionBuilder();

    private final SetBuilder setBuilder = new SetBuilder(examsBuilder);

    private MainModel model;

    public MainBuilder(final MainModel model, final InfoModel infoModel)
    {
        super(model, infoModel,
                new String[]{Commands.COMMANDID_OPENUST, Commands.COMMANDID_OPENEXAM,
                        Commands.COMMANDID_QUIT, I15nWelcomePanel.SEPARATOR},
                new String[]{Commands.COMMANDID_OPENUST, Commands.COMMANDID_OPENEXAM});
        TaxonModels.setInfoModel(infoModel);
        TaxonModels.loadTaxonTrees();
        prefsBuilder.setInfoModel(infoModel);
        groupsBuilder.setInfoModel(infoModel);
        specimensBuilder.setInfoModel(infoModel);
        levelsBuilder.setInfoModel(infoModel);
        constraintsBuilder.setInfoModel(infoModel);
        examsBuilder.setInfoModel(infoModel);
        examInfoBuilder.setInfoModel(infoModel);
        setBuilder.setInfoModel(infoModel);
        setModel(model);
    }

    public void setModel(final MainModel model)
    {
        this.model = model;
    }

    protected void initCommands()
    {
        super.initCommands();
        initCommand(new OpenCommand(getCommandManager(), model, Commands.COMMANDID_OPENUST, MainModel.CARDS_UST), false);
        initCommand(new OpenCommand(getCommandManager(), model, Commands.COMMANDID_OPENEXAM, MainModel.CARDS_EXAM), false);
        initCommand(new CloseCommand(getCommandManager(), model), true);
        initCommand(new ExportCommand(getCommandManager(), model), true);
        initCommand(new QuitCommand(getCommandManager(), model), true);
    }

    protected void createNonWelcomPanels()
    {
        try
        {
            // The builders that should be inserted. If none are specified, alle builders will be used
            RootWindow rootWindow;
            ViewMap views;

            views = new ViewMap();
            views.addView(0, new View(Strings.getString("settings.title"), null, wrapPanel(prefsBuilder.getPanel())));
            views.addView(1, new View(Strings.getString("groups.title"), null, wrapPanel(groupsBuilder.getPanel())));
            views.addView(2, new View(Strings.getString("specimens.title"), null, wrapPanel(specimensBuilder.getPanel())));
            views.addView(3, new View(Strings.getString("levels.title"), null, wrapPanel(levelsBuilder.getPanel())));
            views.addView(4, new View(Strings.getString("constraints.title"), null, wrapPanel(constraintsBuilder.getPanel())));
            rootWindow = DockingUtil.createRootWindow(views, true);
            DockingWindowsUtils.setBasicRootWindowProps(rootWindow);
            DockingWindowsUtils.configureProperties(rootWindow, false, false, false, false, false, false);
            getCards().add(MainModel.CARDS_UST, rootWindow);

            views = new ViewMap();
            views.addView(0, new View(Strings.getString("examinfo.title"), null, wrapPanel(examInfoBuilder.getPanel())));
            views.addView(1, new View(Strings.getString("exams.title"), null, wrapPanel(examsBuilder.getPanel())));
            // Todo: Remove this hack
            System.setProperty("ch.jfactory.iconprefix", "/icons");
            views.addView(2, new View(Strings.getString("submit.title"), null, wrapPanel(setBuilder.getPanel())));
            System.clearProperty("ch.jfactory.iconprefix");
            rootWindow = DockingUtil.createRootWindow(views, true);
            DockingWindowsUtils.setBasicRootWindowProps(rootWindow);
            DockingWindowsUtils.configureProperties(rootWindow, false, false, false, false, false, false);
            getCards().add(MainModel.CARDS_EXAM, rootWindow);
        }
        catch (Exception e)
        {
            LOG.fatal("could not complete panel loading", e);
        }
    }

    protected JMenuBar getMenuBar()
    {
        return getCommandManager().getGroup(Commands.GROUPID_MENU).createMenuBar();
    }

    private static JComponent wrapPanel(final JComponent panel)
    {
        panel.setBorder(Borders.createEmptyBorder(Sizes.DLUX2, Sizes.DLUX4, Sizes.DLUX4, Sizes.DLUX4));
        return panel;
    }

    public static void main(final String[] args) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        UIManager.put("ToolBar.border", new EmptyBorder(0, 0, 0, 0));
        System.setProperty("jfactory.resource.path", "/icon");
        Strings.setResourceBundle(ResourceBundle.getBundle("ch.xmatrix.ups.uec.view.Strings"));
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JComponent panel = new MainBuilder(new MainModel(), new DefaultInfoModel()).examsBuilder.getPanel();
        panel.setBorder(Borders.createEmptyBorder(Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8));
        f.getContentPane().add(panel);
        f.setSize(650, 500);
        f.setVisible(true);
    }
}
