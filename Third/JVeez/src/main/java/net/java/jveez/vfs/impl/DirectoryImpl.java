/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.vfs.impl;

import java.io.File;
import java.util.List;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import net.java.jveez.vfs.Directory;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

public class DirectoryImpl implements Directory, Comparable<Directory> {

    private static final Logger LOG = Logger.getLogger(DirectoryImpl.class);

    private static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();

    private Directory parent;

    private File directory;
    private String absolutePath;
    private String name;
    private Icon icon;
    private Boolean hidden;
    private List<Picture> pictures;

    public DirectoryImpl(Directory parent, File directory) {
        assert directory != null && directory.isDirectory();
        this.directory = directory;
        this.parent = parent;
    }

    public File getFile() {
        return directory;
    }

    public Directory getParent() {
        return parent;
    }

    public String getName() {
        if (name == null) {
            name = FILE_SYSTEM_VIEW.getSystemDisplayName(directory);
            if (name == null || name.length() == 0) {
                name = FILE_SYSTEM_VIEW.getSystemTypeDescription(directory);
            }
        }
        return name;
    }

    public String getAbsolutePath() {
        if (absolutePath == null) {
            absolutePath = directory.getAbsolutePath();
        }
        return absolutePath;
    }

    public String getDescription() {
        return null;
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = FILE_SYSTEM_VIEW.getSystemIcon(directory);
        }
        return icon;
    }

    public boolean isHidden() {
        if (hidden == null) {
            hidden = directory.isHidden();
        }
        return hidden.booleanValue();
    }

    public int compareTo(Directory directory) {
        if (directory == null) {
            return 1;
        }
        else {
            return getAbsolutePath().compareToIgnoreCase(directory.getAbsolutePath());
        }
    }

    public String toString() {
        return "DirectoryImpl[" +
                "directory=" + directory +
                "]";
    }
}
