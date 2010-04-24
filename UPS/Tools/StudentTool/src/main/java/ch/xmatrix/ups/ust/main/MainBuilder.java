/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.ust.main;

import ch.jfactory.application.AbstractMainBuilder;
import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.application.view.builder.DockingWindowsUtils;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.command.CommonCommands;
import ch.jfactory.command.QuitCommand;
import ch.jfactory.command.RunBrowserCommand;
import ch.jfactory.component.I15nWelcomePanel;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.ust.edit.BalanceBuilder;
import ch.xmatrix.ups.ust.edit.ConstraintsSelectionBuilder;
import ch.xmatrix.ups.ust.edit.ExamInfoBuilder;
import ch.xmatrix.ups.ust.edit.SpeciesSelectionBuilder;
import ch.xmatrix.ups.ust.edit.TaxonCheckBuilder;
import ch.xmatrix.ups.ust.main.commands.AboutCommand;
import ch.xmatrix.ups.ust.main.commands.CloseCommand;
import ch.xmatrix.ups.ust.main.commands.Commands;
import ch.xmatrix.ups.ust.main.commands.ExportPlantlistAsTextTree;
import ch.xmatrix.ups.ust.main.commands.ExportTextCommand;
import ch.xmatrix.ups.ust.main.commands.NewDefaultCommand;
import ch.xmatrix.ups.ust.main.commands.NewFileCommand;
import ch.xmatrix.ups.ust.main.commands.OpenCommand;
import ch.xmatrix.ups.ust.main.commands.SaveAsCommand;
import ch.xmatrix.ups.ust.main.commands.SaveCommand;
import ch.xmatrix.ups.ust.main.commands.SubmitCommand;
import ch.xmatrix.ups.view.renderer.ConstraintsRendererUtils;
import com.jgoodies.uif.application.Application;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.ViewMap;
import org.pietschy.command.CommandManager;

/**
 * Builds the main panel.
 *
 * @author Daniel Frey
 * @version $Revision: 1.9 $ $Date: 2007/05/16 17:00:16 $
 */
public class MainBuilder extends AbstractMainBuilder
{
    private final MainModel model;

    private final ExamInfoBuilder examInfoBuilder;

    private final BalanceBuilder balanceBuilder;

    private final SpeciesSelectionBuilder speciesSelectionBuilder;

    private final ConstraintsSelectionBuilder constraintsSelectionBuilder;

    private final TaxonCheckBuilder taxonCheckBuilder;

    public MainBuilder( final MainModel model, final InfoModel infoModel )
    {
        super( model, infoModel, new String[]{
                I15nWelcomePanel.SEPARATOR, Commands.COMMANDID_NEW, Commands.COMMANDID_OPEN,
                Commands.COMMANDID_NEWDEFAULT, CommonCommands.COMMANDID_QUIT, CommonCommands.COMMANDID_ABOUT,
                I15nWelcomePanel.SEPARATOR, I15nWelcomePanel.TEXT, Commands.COMMANDID_UPSHOME, Commands.COMMANDID_USTHOME,
                I15nWelcomePanel.SEPARATOR, Commands.COMMANDID_USTHELP, Commands.COMMANDID_USTEXAM, Commands.COMMANDID_USTFAQ,
                I15nWelcomePanel.SEPARATOR
        }, new String[]{Commands.COMMANDID_NEW, Commands.COMMANDID_NEWDEFAULT, Commands.COMMANDID_OPEN} );
        this.model = model;
        examInfoBuilder = new ExamInfoBuilder();
        balanceBuilder = new BalanceBuilder();
        speciesSelectionBuilder = new SpeciesSelectionBuilder();
        constraintsSelectionBuilder = new ConstraintsSelectionBuilder();
        taxonCheckBuilder = new TaxonCheckBuilder();
    }

    protected void createNonWelcomPanels()
    {
        final ViewMap map = new ViewMap();
        map.addView( 1, new View( Strings.getString( "info.exam.title" ), null, examInfoBuilder.getPanel() ) );
        map.addView( 2, new View( Strings.getString( "info.balance.title" ), null, balanceBuilder.getPanel() ) );
        map.addView( 3, new View( Strings.getString( "navigation.selectables.title" ), null, speciesSelectionBuilder.getPanel() ) );
        map.addView( 4, new View( Strings.getString( "navigation.constraints.title" ), null, constraintsSelectionBuilder.getPanel() ) );

        final RootWindow rootWindow = DockingWindowsUtils.createParentChildDisplay( taxonCheckBuilder.getPanel(), map );
        rootWindow.setWindow( new SplitWindow( true, 0.8f, map.getView( 0 ), new SplitWindow( false, 0.2f,
                new TabWindow( new View[]{map.getView( 1 ), map.getView( 2 )} ),
                new TabWindow( new View[]{map.getView( 3 ), map.getView( 4 )} ) ) ) );
        getCards().add( MainModel.CARDS_EDIT, rootWindow );

        taxonCheckBuilder.addComponentsToRepaint( balanceBuilder.getComponentsToRepaint() );
        taxonCheckBuilder.addComponentToRepaint( constraintsSelectionBuilder.getRepaintComponent() );
    }

    protected JMenuBar getMenuBar()
    {
        return getCommandManager().getGroup( Commands.GROUPID_MENUBAR ).createMenuBar();
    }

    protected MainModel getMainModel()
    {
        return model;
    }

    protected void initCommands()
    {
        final CommandManager manager = getCommandManager();
        initCommand( new NewFileCommand( manager, model ), false );
        initCommand( new NewDefaultCommand( manager, model ), false );
        initCommand( new OpenCommand( manager, model ), false );
        initCommand( new SaveCommand( manager, model ), false );
        initCommand( new SaveAsCommand( manager, model ), true );
        initCommand( new CloseCommand( manager, model ), true );
        initCommand( new ExportPlantlistAsTextTree( manager, model ), true );
        initCommand( new ExportTextCommand( manager, model ), true );
        initCommand( new SubmitCommand( manager, model ) );
        initCommand( new QuitCommand( manager, model ), true );
        initCommand( new RunBrowserCommand( manager, Commands.COMMANDID_UPSHOME, "http://balti.ethz.ch/ups/site/ups" ), true );
        initCommand( new RunBrowserCommand( manager, Commands.COMMANDID_USTHOME, "http://balti.ethz.ch/ups/site/ust/" ), true );
        initCommand( new RunBrowserCommand( manager, Commands.COMMANDID_USTEXAM, "http://balti.ethz.ch/ups/site/ust/help/exam.html" ), true );
        initCommand( new RunBrowserCommand( manager, Commands.COMMANDID_USTHELP, "http://balti.ethz.ch/ups/site/ust/help/help.html" ), true );
        initCommand( new RunBrowserCommand( manager, Commands.COMMANDID_USTFAQ, "http://balti.ethz.ch/ups/site/ust/faq.html" ), true );
        initCommand( new RunBrowserCommand( manager, Commands.COMMANDID_USTBUGS, "http://www.xmatrix.ch/jira" ), true );
        initCommand( new AboutCommand( manager, ImageLocator.getIcon( "ust-logo.png" ), Application.getDescription().getFullVersion() ), true );
    }

    protected void initModelListeners()
    {
        super.initModelListeners();
        model.queue( new Runnable()
        {
            public void run()
            {
                // React upon an open state change. Show the open dialog.
                model.addPropertyChangeListener( AbstractMainModel.EVENTNAME_OPENING, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        final UserModel userModel = model.getUserModel();
                        examInfoBuilder.setModel( userModel );
                        balanceBuilder.setModel( userModel );
                        speciesSelectionBuilder.setModel( userModel );
                        constraintsSelectionBuilder.setModel( userModel );
                        taxonCheckBuilder.setModel( userModel );
                        model.setUserModel( userModel );
                        adjustForCompletion();
                    }
                } );

                speciesSelectionBuilder.addListSelectionListener( new ListSelectionListener()
                {
                    public void valueChanged( final ListSelectionEvent e )
                    {
                        final JList list = (JList) e.getSource();
                        taxonCheckBuilder.setSelection( (SimpleTaxon) list.getSelectedValue() );
                    }
                } );

                constraintsSelectionBuilder.addTreeSelectionListener( new TreeSelectionListener()
                {
                    public void valueChanged( final TreeSelectionEvent e )
                    {
                        final JTree tree = (JTree) e.getSource();
                        final TreePath path = tree.getSelectionPath();
                        if ( path != null )
                        {
                            final Object selection = path.getLastPathComponent();
                            final SessionModel session = (SessionModel) MainModel.findModel( model.getUserModel().getExamInfoUid() );
                            final Constraints constraints = (Constraints) AbstractMainModel.findModel( session.getConstraintsUid() );
                            final TaxonTree taxonTree = TaxonModels.find( constraints.getTaxaUid() );
                            final SimpleTaxon taxon;
                            if ( selection instanceof String )
                            {
                                taxon = taxonTree.getRootTaxon();
                            }
                            else if ( selection instanceof Constraint )
                            {
                                final Constraint constraint = (Constraint) selection;
                                final List<String> taxa = constraint.getTaxa();
                                if ( taxa != null && taxa.size() == 1 )
                                {
                                    final String taxonName = taxa.get( 0 );
                                    taxon = taxonTree.findTaxonByName( taxonName );
                                }
                                else
                                {
                                    taxon = null;
                                }
                            }
                            else if ( selection instanceof SimpleTaxon )
                            {
                                taxon = (SimpleTaxon) selection;
                            }
                            else
                            {
                                taxon = null;
                            }
                            if ( taxon != null )
                            {
                                taxonCheckBuilder.setSelection( taxon );
                            }
                        }
                    }
                } );

                // Make the submit menu item dis-/enabled when list is incomplete/complete.
                taxonCheckBuilder.addSpeciesSelectionListener( new TaxonCheckBuilder.SpeciesSelectionListener()
                {
                    public void speciesSelectionChanged()
                    {
                        model.setDirty( true );
                        adjustForCompletion();
                    }
                } );

                // The save command is only enabled if the file is not the default file and the model is dirty.
                model.addPropertyChangeListener( AbstractMainModel.PROPERTYNAME_DIRTY, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        final boolean dirty = ( (Boolean) evt.getNewValue() ).booleanValue();
                        final boolean isDefaultFile = model.isDefaultFile();
                        getCommandManager().getCommand( Commands.COMMANDID_SAVE ).setEnabled( dirty && !isDefaultFile );
                    }
                } );
            }
        } );
    }

    /**
     * Looks whether the constraints are complete and en-/disables the submit menu.
     */
    private void adjustForCompletion()
    {
        final UserModel userModel = model.getUserModel();
        final Constraints constraints = (Constraints) AbstractMainModel.findModel( userModel.getConstraintsUid() );
        final int complete = ConstraintsRendererUtils.getCompleteConstraints( constraints, userModel.getTaxa() );
        final int total = constraints.getConstraints().size();
        getCommandManager().getCommand( Commands.COMMANDID_SUBMIT ).setEnabled( complete >= total );
    }
}
