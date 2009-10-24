/*
 * Herbar CD-ROM version 2
 *
 * AppHerbar.java
 *
 * Created on 2. April 2002
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.animation.AnimationQueue;
import ch.jfactory.animation.Paintable;
import ch.jfactory.animation.fading.FadingPaintable;
import ch.jfactory.animation.scrolltext.ScrollingTextPaintable;
import ch.jfactory.application.CopyProtection;
import ch.jfactory.application.SystemUtil;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.component.Dialogs;
import ch.jfactory.component.EditItem;
import ch.jfactory.component.SimpleDocumentListener;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.gui.about.Splash;
import com.ethz.geobot.herbar.gui.util.HerbarTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class AppHerbar {

    private static Category cat;

    private static final String DIR_GE = "/ge/";

    private static final String DIR_SC = "/sc/";

    private static final String EXT_GE = "ge";

    private static final String EXT_SC = "sc";

    private static final String homeDir = System.getProperty("user.home").replace('\\', '/') + "/.hcd2/";

    private static Splash splash;

    /** reference to the one and only mainframe */
    private static MainFrame mainFrame = null;

    private static final Check[] CHECKS_TO_PERFORM = new Check[]{new InitialCheck(), new PrefsCheck(), new UserCheck()};

    //Construct the application
    public AppHerbar() {
        securityCheck();
        switchDatabase();
        initSplash();
        decompressDatabase();
        Application.getInstance().getModel();
        System.setProperty(ImageLocator.PROPERTY_IMAGE_LOCATION, System.getProperty("xmatrix.picture.path"));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // set frame position
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                mainFrame.setSize((int) (screenSize.width / 1.2), (int) (screenSize.height / 1.2));
                WindowUtils.centerOnScreen(mainFrame);

                // load old user settings
                mainFrame.loadSettings();
                mainFrame.setVisible(true);
            }
        });
    }

    /** Blaböla */
    private void securityCheck() {
        boolean atLeastOneCheckPassed = false;
        for (int index = 0; !atLeastOneCheckPassed; index++) {
            Check check = CHECKS_TO_PERFORM[index];
            atLeastOneCheckPassed |= check.performCheck();
        }
        if (!atLeastOneCheckPassed) {
            throw new IllegalStateException("CD-ROM/images not found.");
        }
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    private void switchDatabase() {
        // Todo: Implement this as a case of specialized resource bundles
        String[] options = new String[]{"Deutsch", "Wissenschaftlich"};
        String message = Strings.getString("SWITCH.MESSAGE");
        String title = Strings.getString("SWITCH.TITLE");
        if (Dialogs.showOptionsQuestion(null, title, message, options, options[1]) != 1) {
            System.setProperty("xmatrix.input.db", System.getProperty("xmatrix.input.db") + EXT_GE);
            System.setProperty("herbar.filter.location", System.getProperty("herbar.filter.location") + DIR_GE);
            System.setProperty("herbar.exam.defaultlist", System.getProperty("herbar.exam.defaultlist.ge"));
        } else {
            System.setProperty("xmatrix.input.db", System.getProperty("xmatrix.input.db") + EXT_SC);
            System.setProperty("herbar.filter.location", System.getProperty("herbar.filter.location") + DIR_SC);
            System.setProperty("herbar.exam.defaultlist", System.getProperty("herbar.exam.defaultlist.sc"));
        }
        cat.info("setting database to (xmatrix.input.db): " + System.getProperty("xmatrix.input.db"));
        cat.info("setting filter directory to (herbar.filter.location): " + System.getProperty("herbar.filter.location"));
        cat.info("setting exam list to (herbar.exam.defaultlist): " + System.getProperty("herbar.exam.defaultlist"));
    }

    private void decompressDatabase() {
        final String destinationDir = homeDir + "data/";
        new File(destinationDir).mkdirs();
        final String[] files = new String[]{
                "sc.properties", "sc.backup", "sc.data", "sc.script", "ge.properties", "ge.backup", "ge.data", "ge.script"
        };
        try {
            for (final String file : files) {
                final String base = "/hcdsql" + file;
                final InputStream is = AppHerbar.class.getResourceAsStream(base);
                final OutputStream os = new FileOutputStream(destinationDir + base);
                IOUtils.copy(is, os);
                os.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());

        cat.debug("Starting main-Application");

        try {
            MetalLookAndFeel.setCurrentTheme(HerbarTheme.THEME);
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            new AppHerbar();
        }
        catch (IllegalStateException e) {
            cat.error("security check failed", e);
            showPicturesNotFound();
        }
        catch (Throwable e) {
            cat.fatal("fatal error occured in Application: " + e.getMessage(), e);
            SystemUtil.EXIT.exit(1);
        }
        finally {
            if (splash != null) {
                splash.finish();
            }
        }

    }

    private void initSplash() {
        final ImageIcon imageIcon = ImageLocator.getIcon("splash.jpg");
        final AnimationQueue scroller = getScroller();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                splash = new Splash(imageIcon);
            }
        });
    }

    private static void showPicturesNotFound() {
        final JComponent pane = (mainFrame == null ? null : mainFrame.getRootPane());
        Dialogs.showErrorMessage(pane, "Fehler", Strings.getString("ERROR.IMAGES_NOT_FOUND"));
        SystemUtil.EXIT.exit(1);
    }

    private static AnimationQueue getScroller() {
        final AnimationQueue scrollingComponent = new AnimationQueue();
        scrollingComponent.setBounds(100, 68, 200, 167);
        final Insets insets = new Insets(0, 10, 0, 10);
        scrollingComponent.setInsets(insets);

        final Color fadeColor = new Color(255, 255, 255, 150);
        final Paintable fader = new FadingPaintable(fadeColor);
        scrollingComponent.addPaintable(fader);

        final int printSpaceWidth = scrollingComponent.getSize().width - insets.left - insets.right;
        final InputStream textFileInputStream = AppHerbar.class.getResourceAsStream("/News.txt");
        final ScrollingTextPaintable scroller = new ScrollingTextPaintable(textFileInputStream, printSpaceWidth, true);
        scroller.setBackgroundColor(fadeColor);
        scroller.setScrollDelay(5);
        scroller.setParagraphDelay(12000);
        scrollingComponent.addPaintable(scroller);

        return scrollingComponent;
    }

    static {
        try {
            LogUtils.init();
            cat = Category.getInstance(AppHerbar.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static abstract class Check {

        private static final String file1 = "images/\u0007Lizenz.txt";

        private static final String file2 = "images/\u0008Lizenz.txt";

        public boolean performCheck() {
            final String dir = getDirectory();
            final boolean checkPassed = check(dir);
            if (checkPassed) {
                System.setProperty("xmatrix.cd.path", dir);
            }
            return checkPassed;
        }

        private boolean check(final String dir) {
            final File f2 = new File(dir, file2);
            final File f1 = new File(dir, file1);
            return CopyProtection.compareFiles(f1, f2);
        }

        abstract String getDirectory();
    }

    private static class InitialCheck extends Check {

        public String getDirectory() {
            return System.getProperty("xmatrix.cd.path", "D:");
        }
    }

    private static class PrefsCheck extends Check {

        public String getDirectory() {
            // ... the file setup.properties contains the path the the cdrom property (xmatrix.cd.path)
            final InputStream is = LogUtils.locateResourceAsStream("setup.properties");
            final Properties props = new Properties();
            if (is != null) {
                try {
                    props.load(is);
                }
                catch (Exception e) {
                    return "";
                }
            }
            return props.getProperty("xmatrix.cd.path");
        }
    }

    private static class UserCheck extends Check {

        public String getDirectory() {
            CDDriveDialog dialog = new CDDriveDialog(this);
            dialog.setSize(400, 250);
            WindowUtils.centerOnScreen(dialog);
            dialog.setVisible(true);
            // a cancel of the user will throw an IllegalStateException
            return dialog.getDirectory();
        }
    }

    private static class CDDriveDialog extends I15nComponentDialog {

        private EditItem editItem;

        private String directory;

        private Check checker;

        public CDDriveDialog(Check checker) {
            super(mainFrame, "DIALOG.SETUP");
            this.checker = checker;
        }

        protected JComponent createComponentPanel() {
            ActionListener action = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.showOpenDialog(null);
                    File file = chooser.getSelectedFile();
                    if (file != null) {
                        editItem.setUserObject(file);
                    }
                }
            };
            editItem = new EditItem("DIALOG.SETUP", action);
            editItem.setEditable(true);
            editItem.getTextField().getDocument().addDocumentListener(new SimpleDocumentListener() {
                // if the two files are found, enable ok button
                public void changedUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try {
                        directory = new File(doc.getText(0, doc.getLength())).toString();
                        enableApply(checker.check(directory));
                    }
                    catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(editItem, BorderLayout.NORTH);
            return panel;
        }

        protected void onApply() throws ComponentDialogException {
            try {
                // save settings for next time
                final FileWriter fw = new FileWriter(homeDir + "setup.properties");
                final String text = editItem.getTextField().getText();
                fw.write("xmatrix.cd.path=" + text);
                fw.close();
                // store in env for runtime
                System.getProperties().setProperty("xmatrix.cd.path", text);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void onCancel() {
            cat.error(new IllegalStateException("protection check not passed"));
            showPicturesNotFound();
        }

        public String getDirectory() {
            return directory;
        }
    }
}
