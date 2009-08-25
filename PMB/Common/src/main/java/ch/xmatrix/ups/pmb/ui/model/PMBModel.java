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
package ch.xmatrix.ups.pmb.ui.model;

import ch.jfactory.binding.DefaultInfoModel;
import ch.jfactory.binding.InfoModel;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.pmb.domain.Entry;
import ch.xmatrix.ups.pmb.domain.FileEntry;
import com.jgoodies.binding.beans.Model;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.java.jveez.vfs.Picture;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:48 $
 */
public class PMBModel extends Model {

    private Settings settings = new Settings();

    private final Map<Entry, FileEntry> hierarchicalMapping = new HashMap<Entry, FileEntry>();
    private final List<Picture> selectedPictures = new ArrayList<Picture>();
    private final ArrayList<FileEntry> fileEntries = new ArrayList<FileEntry>();
    private final FileEntryThumbnailListModel thumbnailModel = new FileEntryThumbnailListModel();

    private final transient InfoModel infoModel = new DefaultInfoModel();

    public PMBModel() {
        TaxonModels.loadTaxonTrees();
        settings.loadSettings();
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public Map<Entry, FileEntry> getHierarchicalMapping() {
        return hierarchicalMapping;
    }

    public List<Picture> getSelectedPictures() {
        return selectedPictures;
    }

    public InfoModel getInfoModel() {
        return infoModel;
    }

    public ArrayList<FileEntry> getFileEntries() {
        return fileEntries;
    }

    public FileEntryThumbnailListModel getThumbnailModel() {
        return thumbnailModel;
    }

    //-- Utility methods

    /**
     * Returns the selected pictures as array of files.
     *
     * @return the array of files
     */
    public File[] getSelectedPicturesAsFile() {
        final List<Picture> pictures = getSelectedPictures();
        final File[] files = new File[pictures.size()];
        int i = 0;
        for (final Picture picture : pictures) {
            files[i++] = picture.getFile();
        }
        return files;
    }
}
