/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.application;

import ch.jfactory.binding.CodedNote;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.resource.Strings;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/25 11:09:31 $
 */
public abstract class AbstractMainController {

    public AbstractMainController(final AbstractMainModel model, final InfoModel infoModel) {
        model.queue(new Runnable() {
            public void run() {
                infoModel.setNote(new CodedNote(Strings.getString("startup.data")));
            }
        });
        initModel(model);
    }

    protected abstract void initModel(AbstractMainModel model);
}
