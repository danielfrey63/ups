package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.file.OpenChooser;
import ch.xmatrix.ups.model.ExamsetModel;
import ch.xmatrix.ups.model.ExamsetsModel;
import ch.xmatrix.ups.pmb.exam.filter.FilteredListModel;
import ch.xmatrix.ups.pmb.exam.filter.Validator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Handles the display of a students selection list. Queries for the password before displaying it.
 *
 * @author Daniel Frey 17.06.2008 17:22:41
 */
public class StudentsDialogController {

    /** This class logger. */
    private static final Logger LOG = Logger.getLogger(StudentsDialogController.class);

    /** The studentds dialog component. */
    private final StudentsDialog dialog;

    /** The exam model. */
    private final PMBExamModel model;

    private final PasswordDialogController passwordController;

    private final DateValidator validator;

    private String pictureDir;

    private FilteredListModel<ExamsetModel> filteredListModel;

    private boolean hasPictureDir;

    private boolean hasExamsetsFile;

    public StudentsDialogController(final StudentsDialog dialog, final PMBExamModel model, final PasswordDialogController passwordController) {
        this.dialog = dialog;
        this.model = model;
        validator = new DateValidator();
        filteredListModel = new FilteredListModel<ExamsetModel>(null, model.getStudentsListModel());
        pictureDir = model.getSettings().getActivePicturePath();
        this.passwordController = passwordController;
        initComponents();
        initListeners();
    }

    private void initComponents() {
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(null);
        dialog.getStudentsList().setModel(filteredListModel);
        dialog.getStudentsList().setCellRenderer(new StudentsListCellRenderer());
        dialog.getFieldPictureDirectory().setText(pictureDir);
        updatePictureField();
    }

    private void initListeners() {
        final JButton okButton = dialog.getOkButton();
        final JButton cancelButton = dialog.getCancelButton();
        final JButton openExamsets = dialog.getButtonOpenExamsets();
        final JButton openPictures = dialog.getButtonOpenPictureDirectory();
        final JCheckBox currentCheckBox = dialog.getCheckBoxCurrent();
        final JList list = dialog.getStudentsList();
        final JTextField pictureField = dialog.getFieldPictureDirectory();
        final JTextField examsetsField = dialog.getFieldExamsets();
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                setVisible(false);
                // Make sure to check for visibility as the password dialog may have been canceled
                if (!dialog.isVisible()) {
                    final Object selection = list.getSelectedValue();
                    if (selection != null) {
                        LOG.info("setting student selection to " + selection);
                        model.setDemo(false);
                        model.getSettings().setActivePicturePath(pictureDir);
                        model.setCurrentExamsetModel((ExamsetModel) selection);
                    }
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                setVisible(false);
            }
        });
        pictureField.addCaretListener(new CaretListener() {
            public void caretUpdate(final CaretEvent e) {
                updatePictureField();
            }
        });
        examsetsField.addCaretListener(new CaretListener() {
            public void caretUpdate(final CaretEvent e) {
                try {
                    hasExamsetsFile = false;
                    final File file = new File(examsetsField.getText());
                    if (file.exists() && file.isFile()) {
                        final FileReader reader = new FileReader(file);
                        final ExamsetsModel examsetsModel = StudentDataLoader.getExamsetModels(reader);
                        IOUtils.closeQuietly(reader);
                        model.getStudentsListModel().setExamsetModels(examsetsModel.getExamsetModels());
                        final Validator<ExamsetModel> filter = currentCheckBox.isSelected() ? validator : null;
                        filteredListModel = new FilteredListModel<ExamsetModel>(filter, model.getStudentsListModel());
                        dialog.getStudentsList().setModel(filteredListModel);
                        if (examsetsModel.getExamsetModels().size() > 0) {
                            list.setSelectedIndex(0);
                        }
                        hasExamsetsFile = true;
                        updateButton();
                    }
                } catch (FileNotFoundException e1) {
                    LOG.error("file not found", e1);
                } catch (TransformerException e11) {
                    LOG.error("could not transform", e11);
                } catch (IOException e12) {
                    LOG.error("io problem", e12);
                }
            }
        });
        openExamsets.addActionListener(new ChooseExamsetsActionListener(examsetsField.getDocument(), examsetsField.getText()));
        openPictures.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final ExamsetFileFilter filter = new ExamsetFileFilter();
                final OpenChooser chooser = new OpenChooser(filter, "pmb.open.picturedir", System.getProperty("user.dir"));
                chooser.setDirectory(new File(pictureField.getText()));
                chooser.getChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.getChooser().setFileFilter(new FileFilter() {

                    public boolean accept(final File f) {
                        return f.isDirectory();
                    }

                    public String getDescription() {
                        return "Directories";
                    }
                });
                chooser.setModal(true);
                chooser.open();
                final File[] files = chooser.getSelectedFiles();
                if (files.length == 1) {
                    pictureDir = files[0].getAbsolutePath();
                    pictureField.setText(pictureDir);
                }
            }
        });
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateButton();
                }
            }
        });
        currentCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JList list = dialog.getStudentsList();
                final Object selection = list.getSelectedValue();
                final boolean todayOnly = currentCheckBox.isSelected();
                if (todayOnly) {
                    filteredListModel.setFilter(validator);
                } else {
                    filteredListModel.setFilter(null);
                }
                list.setSelectedValue(selection, true);
            }
        });
    }

    private void updatePictureField() {
        final JTextField pictureField = dialog.getFieldPictureDirectory();
        final String directory = pictureField.getText();
        final File file = new File(directory);
        if (file.exists() && file.isDirectory()) {
            pictureDir = directory;
            hasPictureDir = true;
            pictureField.setForeground(Color.black);
        } else {
            hasPictureDir = false;
            pictureField.setForeground(Color.red);
        }
        updateButton();
    }

    private void updateButton() {
        final JList list = dialog.getStudentsList();
        final JButton okButton = dialog.getOkButton();
        final JButton cancelButton = dialog.getCancelButton();
        final boolean selected = list.getSelectedIndices().length > 0;
        final boolean enabled = selected && hasExamsetsFile && hasPictureDir;
        okButton.setEnabled(enabled);
        dialog.getRootPane().setDefaultButton(enabled ? okButton : cancelButton);
    }

    public void setVisible(final boolean visible) {
        if (passwordController.check()) {
            WindowUtils.centerOnScreen(dialog);
            dialog.setVisible(visible);
        }
    }

    private static class DateValidator implements Validator<ExamsetModel> {

        public boolean isValid(final ExamsetModel examsetModel) {
            final Calendar date = examsetModel.getRegistration().getAnmeldedaten().getPruefungsdatum();
            final Calendar today = Calendar.getInstance();
            boolean equals = true;
            equals &= date.get(Calendar.YEAR) == today.get(Calendar.YEAR);
            equals &= date.get(Calendar.MONTH) == today.get(Calendar.MONTH);
            equals &= date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
            return equals;
        }
    }
}
