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

import ch.jfactory.model.DomDriver;
import ch.xmatrix.ups.controller.Loader;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:16 $
 */
public class PictureStatesModel {

    private static final String MODEL_RESOURCE = "/data/model.xml";
    private static final String MODEL_PATH = "ups/pmb";

    private transient XStream converter;

    private List<PictureStateModel> models = new ArrayList<PictureStateModel>();

    public void add(final PictureStateModel model) {
        models.add(model);
    }

    public PictureStateModel get(final String picture) {
        for (final PictureStateModel model : models) {
            if (picture != null && picture.equals(model.getPicture())) {
                return model;
            }
        }
        return null;
    }

    public void save() {
        Loader.saveModel(MODEL_RESOURCE, MODEL_PATH, getConverter(), models);
    }

    public void load() {
        models.clear();
        final List<PictureStateModel> otherModels = Loader.loadModel(MODEL_RESOURCE, MODEL_PATH, getConverter());
        if (otherModels != null) {
            models.addAll(otherModels);
        }
    }

    public XStream getConverter() {
        if (converter == null) {
            converter = new XStream(new DomDriver());
            converter.omitField(AbstractListModel.class, "listenerList");
            converter.alias("pictureStates", List.class);
            converter.alias("pictureState", PictureStateModel.class);
            converter.addImplicitCollection(PictureStatesModel.class, "models");
        }
        return converter;
    }
}
