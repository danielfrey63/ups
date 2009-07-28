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

import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.controller.Loader;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.CoursePersister;


/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2008/01/23 22:19:08 $
 */
public class MainModel extends AbstractMainModel {

    public static final String CARDS_UST = "ustTasks";
    public static final String CARDS_EXAM = "examTasks";
    public static final String MODELID_CONSTRAINTS = "constraints";
    public static final String MODELID_COURSEINFO = "courseInfo";
    public static final String MODELID_SESSION = "examInfo";
    public static final String MODELID_PREFS = "prefs";
    public static final String MODELID_EXAMS = "exams";
    public static final String MODELID_GROUPS = "groups";
    public static final String MODELID_LEVELS = "levels";
    public static final String MODELID_SPECIMENS = "specimens";
    public static final MainModel DEFAULT = new MainModel();

    // Submodels

    public MainModel() {
        // Load models that are not loaded by their corresponding editor
        final SimpleModelList model = Loader.loadModel("/data/courses.xml", "ups/uec", CoursePersister.getConverter());
        registerModels(MODELID_COURSEINFO, model);
    }

    public static TaxonBased findModel(final String uid) {
        for (int i = 0; i < MODELS.size(); i++) {
            final SimpleModelList list = MODELS.get(i);
            for (int j = 0; j < list.getSize(); j++) {
                final Object modelObject = list.getElementAt(j);
                if (modelObject instanceof TaxonBased) {
                    final TaxonBased model = (TaxonBased) modelObject;
                    if (model.getUid().equals(uid)) {
                        return model;
                    }
                }
            }
        }
        return null;
    }
}
