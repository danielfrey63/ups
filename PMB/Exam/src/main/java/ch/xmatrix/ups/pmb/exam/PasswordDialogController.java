/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.application.presentation.WindowUtils;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

/**
 * Handles display of the password dialog.
 *
 * @author Daniel Frey 04.08.2008 10:48:43
 */
public class PasswordDialogController {

    /** This class logger. */
    private static final Logger LOG = Logger.getLogger(PasswordDialogController.class);

    /** Contains all registered users. */
    private final Properties registry = new Properties();

    /** The dialog to display. */
    private final PasswordDialog dialog;

    private boolean hasName;

    private boolean hasPassword;

    private boolean passed;

    public PasswordDialogController() {
        this.dialog = new PasswordDialog((Frame) null);
        initCredentials();
        initComponent();
        initListeners();
    }

    public PasswordDialogController(final Properties properties) {
        this.dialog = new PasswordDialog((Frame) null);
        registry.putAll(properties);
        initComponent();
        initListeners();
    }

    private void initCredentials () {
        InputStream credentials = null;
        try {
            credentials = PasswordDialogController.class.getResourceAsStream("/credentials.properties");
            if (credentials != null) {
                registry.load(credentials);
                LOG.info("using credentials");
            } else {
                LOG.info("not using credentials");
            }
        } catch (IOException e) {
            LOG.error("problems while loading credentials", e);
        } finally {
            IOUtils.closeQuietly(credentials);
        }
    }

    private void initComponent() {
        dialog.setLocationRelativeTo(null);
        dialog.pack();
    }

    private void initListeners() {
        final JButton okButton = dialog.getOkButton();
        final JButton cancelButton = dialog.getCancelButton();

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                try {
                    final String name = dialog.getFieldName().getText();
                    final Object registeredHash = registry.get(name);
                    if (registeredHash != null) {
                        final byte[] selection = new String(dialog.getFieldPassword().getPassword()).getBytes();
                        final MessageDigest md = MessageDigest.getInstance("SHA");
                        final String hash = new BASE64Encoder().encode(md.digest(selection));
                        if (hash.equals(registeredHash)) {
                            passed = true;
                            setVisible(false);
                        } else {
                            passed = false;
                        }
                    }
                } catch (NoSuchAlgorithmException e) {
                    LOG.error("cannot init hash function", e);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                passed = false;
                setVisible(false);
            }
        });
        dialog.getFieldName().addCaretListener(new CaretListener() {
            public void caretUpdate(final CaretEvent e) {
                hasName = !dialog.getFieldName().getText().trim().equals("");
                checkFlags();
            }
        });
        dialog.getFieldPassword().addCaretListener(new CaretListener() {
            public void caretUpdate(final CaretEvent e) {
                hasPassword = dialog.getFieldPassword().getPassword().length > 0;
                checkFlags();
            }
        });

    }

    private void checkFlags() {
        final boolean hasAll = hasName && hasPassword;
        dialog.getOkButton().setEnabled(hasAll);
        dialog.getRootPane().setDefaultButton(hasAll ? dialog.getOkButton() : dialog.getCancelButton());
    }

    private void setVisible(final boolean visible) {
        if (registry.size() != 0) {
            WindowUtils.centerOnScreen(dialog);
            dialog.getFieldName().requestFocus();
            dialog.getFieldName().setText("");
            dialog.getFieldPassword().setText("");
            dialog.setVisible(visible);
        }
    }

    public boolean check() {
        setVisible(true);
        return passed;
    }

    public boolean hasRegistry() {
        return registry.size() > 0;
    }
}
