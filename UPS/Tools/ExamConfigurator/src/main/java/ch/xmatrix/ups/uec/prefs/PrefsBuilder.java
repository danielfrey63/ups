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
package ch.xmatrix.ups.uec.prefs;

import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import com.thoughtworks.xstream.XStream;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Builds a panel with a name field and some value setters.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2007/05/16 17:00:15 $
 */
public class PrefsBuilder extends AbstractDetailsBuilder
{

    public static final String COMPONENT_KNOWNCOUNT = "spinnerKnownTotalCount";

    public static final String COMPONENT_KNOWNWEIGHT = "spinnerKnownTotalWeight";

    public static final String COMPONENT_UNKNOWNCOUNT = "spinnerUnknownTotalCount";

    public static final String COMPONENT_UNKNOWNWEIGHT = "spinnerUnknownTotalWeight";

    public static final String COMPONENT_CYCLES = "spinnerCycles";

    private static final String RESOURCE_FORM = "/ch/xmatrix/ups/uec/prefs/PrefsPanel.jfd";

    private static final String RESOURCE_MODEL = "/data/prefs.xml";

    private JSpinner spinnerKnownTotalCount;

    private JSpinner spinnerKnownTotalWeight;

    private JSpinner spinnerUnknownTotalCount;

    private JSpinner spinnerUnknownTotalWeight;

    private JSpinner spinnerCycles;

    private XStream converter;

    public PrefsBuilder()
    {
        super(new PrefsFactory(), RESOURCE_MODEL, RESOURCE_FORM, 30);
    }

    //--- ActionCommandPanelBuilder overrides

    protected void initComponentListeners()
    {
        spinnerKnownTotalCount.addChangeListener(new ChangeListener()
        {
            public void stateChanged(final ChangeEvent e)
            {
                final PrefsModel model = (PrefsModel) getModels().getSelection();
                if (model != null)
                {
                    final Integer value = (Integer) spinnerKnownTotalCount.getValue();
                    model.setKnownTotalCount(value.intValue());
                    setDirty();
                }
            }
        });
        spinnerKnownTotalWeight.addChangeListener(new ChangeListener()
        {
            public void stateChanged(final ChangeEvent e)
            {
                final PrefsModel model = (PrefsModel) getModels().getSelection();
                if (model != null)
                {
                    final Integer value = (Integer) spinnerKnownTotalWeight.getValue();
                    model.setKnownTotalWeight(value.intValue());
                    setDirty();
                }
            }
        });
        spinnerUnknownTotalCount.addChangeListener(new ChangeListener()
        {
            public void stateChanged(final ChangeEvent e)
            {
                final PrefsModel model = (PrefsModel) getModels().getSelection();
                if (model != null)
                {
                    final Integer value = (Integer) spinnerUnknownTotalCount.getValue();
                    model.setUnknownTotalCount(value.intValue());
                    setDirty();
                }
            }
        });
        spinnerUnknownTotalWeight.addChangeListener(new ChangeListener()
        {
            public void stateChanged(final ChangeEvent e)
            {
                final PrefsModel model = (PrefsModel) getModels().getSelection();
                if (model != null)
                {
                    final Integer value = (Integer) spinnerUnknownTotalWeight.getValue();
                    model.setUnknownTotalWeight(value.intValue());
                    setDirty();
                }
            }
        });
        spinnerCycles.addChangeListener(new ChangeListener()
        {
            public void stateChanged(final ChangeEvent e)
            {
                final PrefsModel model = (PrefsModel) getModels().getSelection();
                if (model != null)
                {
                    final Integer value = (Integer) spinnerCycles.getValue();
                    model.setMaximumSeries(value.intValue());
                    setDirty();
                }
            }
        });
    }

    //--- DetailsBuilder implementations

    public void setEnabled(final boolean enabled)
    {
        spinnerKnownTotalCount.setEnabled(enabled);
        spinnerKnownTotalWeight.setEnabled(enabled);
        spinnerUnknownTotalCount.setEnabled(enabled);
        spinnerUnknownTotalWeight.setEnabled(enabled);
        spinnerCycles.setEnabled(enabled);
    }

    public void setModel(final TaxonBased taxonBased)
    {
        super.setModel(taxonBased);
        final PrefsModel model = (PrefsModel) taxonBased;
        if (model == null)
        {
            final Integer zero = new Integer(0);
            spinnerKnownTotalCount.setValue(zero);
            spinnerKnownTotalWeight.setValue(zero);
            spinnerUnknownTotalCount.setValue(zero);
            spinnerUnknownTotalWeight.setValue(zero);
            spinnerCycles.setValue(zero);
        }
        else
        {
            spinnerKnownTotalCount.setValue(new Integer(model.getKnownTotalCount()));
            spinnerKnownTotalWeight.setValue(new Integer(model.getKnownTotalWeight()));
            spinnerUnknownTotalCount.setValue(new Integer(model.getUnknownTotalCount()));
            spinnerUnknownTotalWeight.setValue(new Integer(model.getUnknownTotalWeight()));
            spinnerCycles.setValue(new Integer(model.getMaximumSeries()));
        }
    }

    //--- AbstractDetailsBuilder overrides

    protected boolean shouldMigrate(final String uid)
    {
        return true;
    }

    //--- AbstractDetailsBuilder implementations

    protected XStream getConverter()
    {
        if (converter == null)
        {
            converter = SimpleModelList.getConverter();
            converter.alias("prefsModels", SimpleModelList.class);
            converter.alias("prefsModel", PrefsModel.class);
        }
        return converter;
    }

    protected String getInfoString()
    {
        return "Einstellungs-Editor";
    }

    protected void initComponents()
    {
        spinnerKnownTotalCount = getCreator().getSpinner(COMPONENT_KNOWNCOUNT);
        spinnerKnownTotalCount.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        spinnerKnownTotalWeight = getCreator().getSpinner(COMPONENT_KNOWNWEIGHT);
        spinnerKnownTotalWeight.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        spinnerUnknownTotalCount = getCreator().getSpinner(COMPONENT_UNKNOWNCOUNT);
        spinnerUnknownTotalCount.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        spinnerUnknownTotalWeight = getCreator().getSpinner(COMPONENT_UNKNOWNWEIGHT);
        spinnerUnknownTotalWeight.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        spinnerCycles = getCreator().getSpinner(COMPONENT_CYCLES);
        spinnerCycles.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
    }

    protected String getModelId()
    {
        return MainModel.MODELID_PREFS;
    }
}
