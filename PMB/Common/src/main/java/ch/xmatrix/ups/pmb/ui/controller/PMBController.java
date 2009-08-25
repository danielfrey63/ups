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
package ch.xmatrix.ups.pmb.ui.controller;

import ch.jfactory.application.view.status.Message;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.SimpleNote;
import ch.jfactory.file.FileUtils;
import ch.xmatrix.ups.pmb.domain.Entry;
import ch.xmatrix.ups.pmb.domain.FileEntry;
import ch.xmatrix.ups.pmb.ui.model.PMBModel;
import ch.xmatrix.ups.pmb.ui.model.PictureStateModel;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.Math.min;
import static java.lang.Math.round;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import net.java.jveez.ui.viewer.ViewerPanel;
import net.java.jveez.ui.viewer.anim.AnimationState;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:48 $
 */
public class PMBController {

    private static final Logger LOG = Logger.getLogger(PMBController.class);

    private final PMBModel model;

    private final InfoModel infoModel;

    private final Component parent;

    public PMBController(final PMBModel model, final Component parent) {
        this.parent = parent;
        this.infoModel = model.getInfoModel();
        this.model = model;
    }

    /**
     * Finds whether the names of the files passed in are identical. If the collection of files is empty, true is
     * returned.
     *
     * @param silently whether to issue messages to the user.
     * @param files    the files to investigate.
     * @return whether identical. Also <code>true</code> if emtpy.
     */
    public boolean areNamesOfSelectedFilesEqual(final boolean silently, final File... files) {
        boolean result = true;
        final int size = files.length;
        if (size > 0) {
            final File firstFile = files[0];
            final String firstName = firstFile.getName();
            for (final File file : files) {
                final String currentName = file.getName();
                if (!currentName.equals(firstName)) {
                    if (!silently) {
                        showMessage(new StringBuilder().append("Mindestens zwei verschiedene Bildnamen ausgewählt: \"")
                                .append(firstName).append("\" und \"").append(currentName).append("\".").toString());
                    }
                    result = false;
                    break;
                }
            }
            if (result && !silently) {
                if (size == 1) {
                    final String folder = firstFile.getParentFile().getName();
                    final String species = model.getSettings().getCleanedSpeciesName(folder);
                    showMessage("1 Bild \"" + firstName + "\" von \"" + species + "\" ausgewählt");
                } else {
                    showMessage(size + " Bilder\"" + firstName + "\" ausgewählt");
                }
            }
        }
        return result;
    }

    /**
     * Renames the given pictures file entries to the new file name. If a new file already exists, the user is asked
     * whether to overwrite or skip or cancel the action.
     *
     * @param newFileName the new file name to give to all pictures
     * @param pictures    the pictures to rename the file entry for
     * @return a map of renamed files, the old file name and the new one. may be empty but not null.
     */
    public Map<String, String> renameSelected(final String newFileName, final File... pictures) {
        Map<String, String> result = new HashMap<String, String>(0);
        if (areNamesOfSelectedFilesEqual(false, pictures) && pictures.length > 0) {
            final Map<File, File> files = new HashMap<File, File>(pictures.length);
            for (final File oldFile : pictures) {
                final String filePath = oldFile.getAbsolutePath();
                final File file = new File(filePath);
                final String directoryPath = file.getParent();
                final File newFile = new File(directoryPath, newFileName.trim());
                files.put(oldFile, newFile);
            }
            result = move(files);
        }
        return result;
    }

    public Map<String, String> move(final String fromPath, final String toPath) {
        final List<Picture> pictures = model.getSelectedPictures();
        final Map<File, File> files = new HashMap<File, File>(pictures.size());
        for (final Picture picture : pictures) {
            final File oldFile = picture.getFile();
            final String path = oldFile.getAbsolutePath().replaceAll("\\\\", "/");
            final String newName;
            if (path.startsWith(fromPath)) {
                newName = toPath + path.substring(fromPath.length());
            } else {
                throw new IllegalArgumentException(oldFile + " not in " + fromPath);
            }
            files.put(oldFile, new File(newName));
        }
        return move(files);
    }

    /**
     * Moves a list of files. The map must contain existing files as keys and the destination files as values. If a
     * destination file exists, an opiton dialog is presented to the user where he can choose between overwriting (all),
     * skiping (all) or canceling the move process for that (all) file(s).
     *
     * @param files the files map with source (keys) and desintation (values) {@code File}s
     * @return a string map with the orignal files as keys and the moved files as values. Not successfully moved files
     *         are not included in the map.
     */
    public Map<String, String> move(final Map<File, File> files) {
        final Map<String, String> result = new HashMap<String, String>();
        int count = 0;
        boolean overwriteAll = false;
        boolean skipAll = false;
        boolean cancel = false;
        for (final File oldFile : files.keySet()) {
            final File newFile = files.get(oldFile);
            // After this if-block the destination file has to be non-existant.
            if (newFile.exists()) {
                if (skipAll) {
                    continue;
                } else if (cancel) {
                    break;
                }
                if (!overwriteAll) {
                    switch (overwriteFileDialog(newFile, parent)) {
                        case OVERWRITE:
                            break;
                        case SKIP:
                            model.getInfoModel().setNote(new SimpleNote("Umbenennen von \n\""
                                    + oldFile + "\" \nübersprungen."));
                            continue;
                        case OVERWRITE_ALL:
                            overwriteAll = true;
                            break;
                        case SKIP_ALL:
                            skipAll = true;
                            continue;
                        case CANCEL:
                            model.getInfoModel().setNote(new SimpleNote("Umbenennen abgebrochen"));
                            cancel = true;
                            continue;
                    }
                }
                if (!newFile.delete()) {
                    showError("Umbenennen", "Datei \n\"" + newFile + "\" \nkonnte nicht gelöscht werden");
                    break;
                }
            }
            // At this point the new file must not exist! Otherwise File.renameTo will not work or
            // FileUtils.copyFile will add the old file content to the new one!
            if (move(oldFile, newFile)) {
                count++;
                result.put(oldFile.getAbsolutePath(), newFile.getAbsolutePath());
            }
        }
        if (count > 1) {
            showMessage("Umbennenen der " + count + " Dateien erfolgreich");
        }
        return result;
    }

    /**
     * Moves one file from the source to the destination. If the file cannot be renamed easily, it is copied to the
     * destination. Then a file length comparision is done before the source file is deleted. All necessary parent
     * folders of the destination are created.
     *
     * TODO: Add exception instead of a false success flag and move to FileUtils.
     *
     * @param sourceFile      the source files fully qualified name (file path and file name), may not be null
     * @param destinationFile the destination files fully qualified name (file path and file name), may not be null
     * @return whether moving was successful and the source file could be deleted
     */
    public boolean move(final File sourceFile, final File destinationFile) {
        if (sourceFile == null) {
            throw new NullPointerException("source file may not be null");
        }
        if (destinationFile == null) {
            throw new NullPointerException("destination file may not be null");
        }

        final boolean success;

        final File newDir = destinationFile.getParentFile();
        newDir.mkdirs();
        if (newDir.exists()) {
            if (sourceFile.renameTo(destinationFile)) {
                final String sourcePath = sourceFile.getAbsolutePath().replaceAll("\\\\", "/");
                String destinationPath = destinationFile.getAbsolutePath().replaceAll("\\\\", "/");
                destinationPath = getDifference(sourcePath, destinationPath);
                showMessage("Datei Umbenennen oder Verschieben von \n\"" +
                        sourcePath + "\" \nnach \n\"" +
                        destinationPath + "\" erfolgreich (Direkt)");
                success = true;
            } else {
                // Copy the file
                FileUtils.copyFile(sourceFile, destinationFile);
                if (destinationFile.exists()) {
                    if (destinationFile.length() == sourceFile.length()) {
                        if (sourceFile.delete()) {
                            showMessage("Datei Umbenennen oder Verschieben von \n\"" +
                                    sourceFile.getName() + "\" \nnach \n\"" +
                                    destinationFile.getName() + "\" \n in \"" +
                                    sourceFile.getParent() + "\" erfolgreich (Kopie)");
                            success = true;
                        } else {
                            showError("Datei Umbenennen oder Verschieben", "Alte Datei \n\"" +
                                    sourceFile + "\" \nkonnte nicht gelöscht werden");
                            success = false;
                        }
                    } else {
                        showError("Datei Umbenennen oder Verschieben", "Dateien nicht gleich gross: \n\"" +
                                sourceFile + "\" \nund \n\"" + destinationFile + "\" \nAlte Datei nicht gelöscht");
                        success = false;
                    }
                } else {
                    showError("Datei Umbenennen oder Verschieben", "Neue Datei \n\"" +
                            destinationFile + "\" \nkonnte nicht angelegt werden");
                    success = false;
                }
            }
        } else {
            showError("Verschieben", "Verzeichnis \n\"" + newDir + "\" \nkann nicht erstellt werden");
            success = false;
        }

        return success;
    }

    /**
     * Returns the substring between two slashes that is different in the second path from the first pasth. So for
     * instance if you pass in two pathes <code>C:/directory1/subdirectory/file.txt</code> and
     * <code>C:/directory2/subdirectory/file.txt</code> then you get back <code>directory2</code>. Or if you pass in
     * <code>C:/directory/file1.txt</code> and <code>C:/directory/file2.txt</code> then you get back
     * <code>file2.txt</code>
     *
     * @param path1 the source path to compare, forward slash separated
     * @param path2 the destination path to compare, forward slash separated
     * @return the difference
     */
    public static String getDifference(final String path1, final String path2) {
        String prefix = "";
        while (true) {
            final int pos = path2.indexOf("/", prefix.length() + 1);
            if (pos > 0) {
                final String sourcePart = path1.substring(0, pos);
                final String destinationPart = path2.substring(0, pos);
                if (sourcePart.equals(destinationPart)) {
                    prefix = destinationPart;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        String postfix = "";
        while (true) {
            final int destinationStart = path2.lastIndexOf("/", path2.length() - postfix.length() - 1);
            if (destinationStart > -1) {
                final int destinationLength = path2.length();
                final int postfixLength = destinationLength - destinationStart;
                final int sourceLength = path1.length();
                final int sourceStart = sourceLength - postfixLength;
                final String sourcePart = path1.substring(Math.max(0, sourceStart));
                final String destinationPart = path2.substring(Math.max(0, destinationStart));
                if (sourcePart.equals(destinationPart)) {
                    postfix = destinationPart;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return path2.substring(0, path2.length() - postfix.length()).substring(prefix.length() + 1);
    }

    public void showMessage(final String message) {
        infoModel.setNote(new SimpleNote(message, "PMBController", Message.Type.INFO));
    }

    public void showError(final String title, final String message, final Exception e) {
        LOG.error(title + ": " + message, e);
        showError(message, title);
    }

    public void showError(final String title, final String message) {
        infoModel.setNote(new SimpleNote(message, title, Message.Type.WARN));
    }

    public String savePositionAndZoom(final ViewerPanel panel) {
        final PictureStateModel state = calculatePositionAndZoom(panel);
        final String newPath;
        if (state != null) {
            final Picture picture = panel.getCurrentPicture();
            final File oldFile = picture.getFile().getAbsoluteFile();
            final File newFile = getNewName(oldFile, state.toString());
            if (!oldFile.equals(newFile)) {
                renameSelected(newFile.getName(), picture.getFile());
                newPath = newFile.getAbsolutePath();
            } else {
                newPath = null;
            }
        } else {
            newPath = null;
        }
        return newPath;
    }

    public void save() {
        model.getSettings().storeSettings();
    }

    public void setCurrentPicture(final FileEntry fileEntry, final ViewerPanel panel) {
        setCurrentPicture(fileEntry == null ? null : fileEntry.getPicture(), panel);
    }

    public void setCurrentPicture(final Picture picture, final ViewerPanel panel) {
        panel.setCurrentPicture(picture);
    }

    public String getRelativePicturePath(final Picture picture) {
        if (picture != null) {
            final String path = picture.getFile().getAbsolutePath().replaceAll("\\\\", "/");
            return path.substring(model.getSettings().getActivePicturePath().length() + 1);
        } else {
            return null;
        }
    }

    public FileEntry findHabitus() {
        final Map<Entry, FileEntry> hierarchy = model.getHierarchicalMapping();
        final Collection<Entry> keys = hierarchy.keySet();
        FileEntry fileEntry = null;
        for (final Entry key : keys) {
            final String keyName = key.getPath();
            if (keyName.startsWith("Habitus")) {
                fileEntry = hierarchy.get(key);
                break;
            }
        }
        return fileEntry;
    }

    private static PictureStateModel calculatePositionAndZoom(final ViewerPanel panel) {
        final BufferedImage image = panel.getImage();
        final Picture picture = panel.getCurrentPicture();
        final PictureStateModel model;
        if (picture != null) {
            final File file = picture.getFile();
            model = new PictureStateModel(file.getAbsolutePath());
            if (image != null) {
                final AnimationState state = panel.getAnimationState();
                final double ix = state.x;
                final double iy = state.y;
                final double iz = state.sx;

                final int iw = (int) round(image.getWidth() * iz);
                final int ih = (int) round(image.getHeight() * iz);
                final Rectangle imageRect = new Rectangle((int) round(ix), (int) round(iy), iw, ih);
                final Rectangle component = panel.getImagePanel().getBounds();
                final double cw = component.getWidth();
                final double ch = component.getHeight();
                final Rectangle2D section = component.createIntersection(imageRect);

                if (ix >= 0 || -ix > iw) {
                    model.setX1(0d);
                } else {
                    model.setX1(-ix / iw);
                }
                if (ix + iw <= cw || ix > cw) {
                    model.setX2(1d);
                } else {
                    model.setX2((section.getWidth() - min(ix, 0)) / iw);
                }
                if (iy >= 0 || -iy > ih) {
                    model.setY1(0d);
                } else {
                    model.setY1(-iy / ih);
                }
                if (iy + ih <= ch || iy > ch) {
                    model.setY2(1d);
                } else {
                    model.setY2((section.getHeight() - min(iy, 0)) / ih);
                }
            }
        } else {
            model = null;
        }

        return model;
    }

    /** Enomeration to indicate the different options for a rename conflict dialog. */
    enum Options {
        /** Overwrite this file. */
        OVERWRITE("Überschreiben"),
        /** Skip this file. */
        SKIP("Überspringen"),
        /** Overwrite this file and all following files. */
        OVERWRITE_ALL("Alle Überschreiben"),
        /** Skip this file and all following files. */
        SKIP_ALL("Alle Überspringen"),
        /** Cancel the process. */
        CANCEL("Abbrechen");

        /** The description of the options. */
        private final String description;

        /**
         * Instantiates a new enumeration with the given description.
         *
         * @param description the description for this enumeration.
         */
        Options(final String description) {
            this.description = description;
        }

        /**
         * The description for this enumeration.
         *
         * @return the description for this enumeration
         */
        public String getDescription() {
            return description;
        }
    }

    private static Options overwriteFileDialog(final File newFile, final Component parent) {
        final Options[] options = {
                Options.OVERWRITE,
                Options.SKIP,
                Options.OVERWRITE_ALL,
                Options.SKIP_ALL,
                Options.CANCEL};

        final int value = JOptionPane.showOptionDialog(parent,
                "Die Datei \"" + newFile + "\" existiert breits! Was möchten Sie tun?",
                "Warnung", JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
                options, options[4]);

        final Options result;
        if (value == JOptionPane.CANCEL_OPTION) {
            result = Options.CANCEL;
        } else {
            result = options[value];
        }
        return result;
    }

    private static File getNewName(final File oldFile, final String tokenToInsert) {
        final String oldName = oldFile.getName();
        final String extention = getExtention(oldName);
        final String oldHierarchicalDescription = getHierarchicalDescriptionPart(oldName);
        return new File(oldFile.getParentFile(), oldHierarchicalDescription + " (" + tokenToInsert + ")" + extention);
    }

    private static String getHierarchicalDescriptionPart(final String name) {
        final int parenthesis = name.indexOf(" (");
        final String oldHierarchicalDescription;
        if (parenthesis > -1) {
            oldHierarchicalDescription = name.substring(0, parenthesis);
        } else {
            oldHierarchicalDescription = getBody(name);
        }
        return oldHierarchicalDescription;
    }

    private static String getExtention(final String name) {
        final int index = name.lastIndexOf(".");
        final String extention;
        if (index > -1) {
            extention = name.substring(index);
        } else {
            extention = "";
        }
        return extention;
    }

    private static String getBody(final String name) {
        final int index = name.lastIndexOf(".");
        final String body;
        if (index > -1) {
            body = name.substring(0, index);
        } else {
            body = name;
        }
        return body;
    }

    public static void collapseSisterNode(final TreePath oldSelection, final TreePath currentSelection, final JTree tree) {
        TreePath selection = oldSelection;
        while (selection != null && !selection.isDescendant(currentSelection) && selection.getPathCount() > 1) {
            tree.collapsePath(selection);
            selection = selection.getParentPath();
        }
    }

    /**
     * Expands subnodes. Expands all single subnodes recursively. Otherwise expands the first set of subnodes.
     *
     * @param path the path to expand
     * @param tree the tree the path is in
     */
    public static void expandIfNotEmpty(final TreePath path, final JTree tree) {
        tree.expandPath(path);
        final TreeModel model = tree.getModel();
        final Object parent = path.getLastPathComponent();
        if (model.getChildCount(parent) == 1) {
            final Object child = model.getChild(parent, 0);
            expandIfNotEmpty(path.pathByAddingChild(child), tree);
        }
    }
}
