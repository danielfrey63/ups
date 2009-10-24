package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.resource.Strings;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.apache.log4j.Category;

/**
 * This class represents the UI for the wizard. It use the WizardModel to get information about the panes and data.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
class WizardDialogUI extends JDialog implements WizardStateListener {

    /**
     * logging category for class
     */
    private static final Category CAT = Category.getInstance(WizardDialogUI.class);
    private static final boolean DEBUG = CAT.isDebugEnabled();
    ;

    /**
     * the navigation panel
     */
    private JPanel navigationPanel = new JPanel();
    /**
     * back button
     */
    private JButton back;
    /**
     * next button
     */
    private JButton next;
    /**
     * finish button
     */
    private JButton finish;
    /**
     * cancel button
     */
    private JButton cancel;
    /**
     * the panel where the panes are shown
     */
    private JPanel wizardPanePanel = new JPanel();
    /**
     * model with information of the panes etc.
     */
    private WizardModel model = null;
    /**
     * current pane
     */
    private WizardPane currentPane = null;
    /**
     * this attribute defines if the changes are accepted
     */
    private boolean accepted = false;
    /**
     * Holds the minimum size for this dialog to show up propertly.
     */
    private Dimension minimumSize;

    private CardLayout cards;

    /**
     * Creates new WizardDialogUI
     *
     * @param parent parent of the wizard frame
     * @param modal  true = modal wizard dialog, false = modaless
     */
    public WizardDialogUI(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initGUI();
    }

    /**
     * Creates new WizardDialogUI
     *
     * @param parent parent of the wizard dialog
     * @param modal  true = modal wizard dialog, false = modaless
     */
    public WizardDialogUI(Dialog parent, String title, boolean modal) {
        super(parent, title, modal);
        initGUI();
    }

    /**
     * set the model of the wizard UI.
     *
     * @param model model of type WizardModel
     */
    public void setModel(WizardModel model) {
        if (model != this.model) {
            CAT.debug("set new model");
            if (this.model != null) {
                model.removeWizardStateListener(this);
            }
            this.model = model;
            model.addWizardStateListener(this);
            model.registerFinishAction(new FinishAction());
            Dimension minimumPanelSize = getBiggestPaneDimension();
            setPaneSize(minimumPanelSize);
            showPane(model.getPane(model.getStart()));
        }
    }

    /**
     * set the pane size, and calculate the size of the dialog.
     *
     * @param dim Dimension of the preferred pane size
     */
    private void setPaneSize(Dimension dim) {
        wizardPanePanel.setPreferredSize(dim);
        pack();
        minimumSize = getSize();
        if (DEBUG) {
            CAT.debug("wizard size = " + getSize());
        }
    }

    /**
     * Returns if the wizard is accepted.
     *
     * @return true finsished pressed, false cancel pressed
     */
    public boolean isAccepted() {
        return accepted;
    }

    private Dimension getBiggestPaneDimension() {
        WizardPane[] panes = model.getPanes();

        // set biggest size for pane area
        int width = 0;
        int height = 0;

        for (int i = 0; i < panes.length; i++) {
            Dimension paneDimension = panes[ i ].getPreferredSize();
            if (DEBUG) {
                CAT.debug("size of pane = " + paneDimension);
            }
            height = Math.max(height, paneDimension.height);
            width = Math.max(width, paneDimension.width);
        }

        if (DEBUG) {
            CAT.debug("larges pane size (" + width + "," + height + ")");
        }
        return new Dimension(width, height);
    }

    public void change(WizardStateChangeEvent event) {
        next.setEnabled(event.isNextEnabled() && event.hasNext());
        back.setEnabled(event.isPreviousEnabled() && event.hasPrevious());
        finish.setEnabled(event.isFinishEnabled());
        cancel.setEnabled(event.isCancelEnabled());

        // default button handling
        JRootPane rootPane = getRootPane();
        if (event.isNextEnabled() && event.hasNext()) {
            rootPane.setDefaultButton(next);
        }
        else if (event.isFinishEnabled()) {
            rootPane.setDefaultButton(finish);
        }
        else if (event.isPreviousEnabled() && event.hasPrevious()) {
            rootPane.setDefaultButton(back);
        }
        else {
            rootPane.setDefaultButton(cancel);
        }
    }

    /**
     * show the specified pane.
     *
     * @param pane to show
     */
    private void showPane(WizardPane pane) {
        CAT.info("showing pane " + pane);

        String name = pane.getName();
        wizardPanePanel.add(pane, name);
        cards.show(wizardPanePanel, name);

        if (currentPane != null) {
            CAT.debug("passivate old pane");
            currentPane.passivate();
            wizardPanePanel.remove(currentPane);
        }
        currentPane = pane;

        CAT.debug("activate current pane");
        wizardPanePanel.revalidate();
        currentPane.activate();
        wizardPanePanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initGUI() {
        // set glass pane
        getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // set layout
        back = ComponentFactory.createButton("WIZARD.NAVIGATION.BACK", new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (currentPane.isPreviousOk()) {
                    showPane(model.getPreviousPane());
                }
            }
        });
        cancel = ComponentFactory.createButton("WIZARD.NAVIGATION.CANCEL", new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (currentPane.isCancelOk()) {
                    closeDialog(event);
                }
            }
        });
        finish = ComponentFactory.createButton("WIZARD.NAVIGATION.FINISH", new FinishAction());
        next = ComponentFactory.createButton("WIZARD.NAVIGATION.NEXT", new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (currentPane.isNextOk()) {
                    showPane(model.getNextPane());
                }
            }
        });
        next.setHorizontalTextPosition(JButton.LEFT);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                closeDialog(evt);
            }
        });

//        addComponentListener(new ComponentAdapter() {
//            public void componentResized(ComponentEvent e) {
//                if (minimumSize != null) {
//                    Dimension size = getSize();
//                    size.width = Math.max(size.width, minimumSize.width);
//                    size.height = Math.max(size.height, minimumSize.height);
//                    setSize(size);
//                }
//            }
//        });
//
        navigationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        navigationPanel.add(back);
        navigationPanel.add(next);
        navigationPanel.add(finish);
        navigationPanel.add(cancel);
        JButton[] buttons = new JButton[]{back, next, finish, cancel};
        WindowUtils.spaceComponents(buttons);
        WindowUtils.equalizeButtons(buttons);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(Constants.GAP_BETWEEN_GROUP, 0, 0, 0));

        cards = new CardLayout(0, 0);
        wizardPanePanel.setLayout(cards);

        getRootPane().setDefaultButton(finish);

        // set placeholder for next button
        JPanel main = new JPanel(new BorderLayout());
        main.setLayout(new BorderLayout());
        main.add(wizardPanePanel, BorderLayout.CENTER);
        main.add(navigationPanel, BorderLayout.SOUTH);
        main.setBorder(BorderFactory.createEmptyBorder(Constants.GAP_BORDER_LEFT_TOP, Constants.GAP_BORDER_LEFT_TOP,
                Constants.GAP_BORDER_RIGHT_BOTTOM, Constants.GAP_BORDER_RIGHT_BOTTOM));
        Container contentPane = getContentPane();
        contentPane.add(main, BorderLayout.CENTER);
    }

    private void closeDialog(ActionEvent evt) {
        closeDialog();
    }

    private void closeDialog(WindowEvent evt) {
        closeDialog();
    }

    /**
     * Passivates the current pane and closes the dialog.
     */
    private void closeDialog() {
        currentPane.passivate();
        setVisible(false);
        dispose();
    }

    class FinishAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            accepted = true;
            closeDialog(e);
        }

        public FinishAction() {
            super(Strings.getString("WIZARD.NAVIGATION.FINISH.TEXT"));
        }
    }
}
