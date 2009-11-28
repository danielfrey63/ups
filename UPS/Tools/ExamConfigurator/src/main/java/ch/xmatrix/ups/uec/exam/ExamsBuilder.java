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
package ch.xmatrix.ups.uec.exam;

import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import com.jgoodies.binding.list.SelectionInList;
import com.thoughtworks.xstream.XStream;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * TODO: Disable configurations in comboboxes that are not fixed.
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2007/05/16 17:00:15 $
 */
public class ExamsBuilder extends AbstractDetailsBuilder
{
    public static final String COMPONENT_PREFS = "comboPrefs";

    public static final String COMPONENT_GROUPS = "comboGroups";

    public static final String COMPONENT_SPECIMENS = "comboSpecimens";

    public static final String COMPONENT_LEVELS = "comboLevels";

    public static final String COMPONENT_CONSTRAINTS = "comboConstraints";

    public static final String COMPONENT_EXAMS = "panelExam";

    private static final Color COLOR_DISABLED = UIManager.getColor( "Label.disabledForeground" );

    private static final Color COLOR_ENABLED = UIManager.getColor( "Label.foreground" );

    private static final Color COLOR_WRONG = Color.red;

    private static final String RESOURCE_FORM = "/ch/xmatrix/ups/uec/exam/Exams.jfd";

    private static final String RESOURCE_MODEL = "/data/exams.xml";

    private JComboBox[] combos;

    private ComboModel[] comboModels;

    private final AbstractDetailsBuilder[] builders;

    private ComboRenderer[] renderers;

    private ExamModel model;

    public ExamsBuilder( final AbstractDetailsBuilder... builders )
    {
        super( new ExamsFactory(), RESOURCE_MODEL, RESOURCE_FORM, 30 );
        this.builders = builders;
    }

    //--- DetailsBuilder implementation

    public void setEnabled( final boolean enabled )
    {
        for ( final JComboBox combo : combos )
        {
            combo.setEnabled( enabled );
        }
    }

    //--- AbstractDetailsBuilder implementation

    public void setModel( final TaxonBased taxonBased )
    {
        super.setModel( taxonBased );
        model = (ExamModel) taxonBased;
        setComboModels();
    }

    protected void initComponents()
    {
        combos = new JComboBox[]{
                getCreator().getComboBox( COMPONENT_PREFS ),
                getCreator().getComboBox( COMPONENT_GROUPS ),
                getCreator().getComboBox( COMPONENT_SPECIMENS ),
                getCreator().getComboBox( COMPONENT_LEVELS ),
                getCreator().getComboBox( COMPONENT_CONSTRAINTS )
        };
        comboModels = new ComboModel[]{
                new ComboModel( 0 ),
                new ComboModel( 1 ),
                new ComboModel( 2 ),
                new ComboModel( 3 ),
                new ComboModel( 4 )
        };
        renderers = new ComboRenderer[]{
                new ComboRenderer( 0 ),
                new ComboRenderer( 1 ),
                new ComboRenderer( 2 ),
                new ComboRenderer( 3 ),
                new ComboRenderer( 4 )
        };
        for ( int i = 0; i < combos.length; i++ )
        {
            final JComboBox combo = combos[i];
            final ComboModel model = comboModels[i];
            final ComboRenderer renderer = renderers[i];
            combo.setModel( model );
            combo.setRenderer( renderer );
        }
        getCreator().getPanel( COMPONENT_EXAMS ).addHierarchyListener( new HierarchyListener()
        {
            public void hierarchyChanged( final HierarchyEvent e )
            {
                if ( e.getComponent().isShowing() )
                {
                    setComboModels();
                }
            }
        } );
    }

    protected XStream getConverter()
    {
        final XStream x = SimpleModelList.getConverter();
        x.alias( "examModels", SimpleModelList.class );
        x.alias( "examModel", ExamModel.class );
        return x;
    }

    protected String getInfoString()
    {
        return "Prüfungs-Editor";
    }

    protected String getModelId()
    {
        return MainModel.MODELID_EXAMS;
    }

    //--- Utilities

    private void setComboModels()
    {
        for ( int i = 0; i < comboModels.length; i++ )
        {
            final ComboModel comboModel = comboModels[i];
            final AbstractDetailsBuilder builder = builders[i];
            final TaxonBased detail = builder.find( model == null ? null : model.get( i ) );
            final SelectionInList detailModels = builder.getModels();
            comboModel.setData( model, detail, detailModels );
        }
    }

    private static boolean isValidTaxaUid( final ExamModel model, final TaxonBased toTest )
    {
        final String taxaUidOfExamModel = model == null ? null : model.getTaxaUid();
        final String taxaUidOfDetailsModel = toTest == null ? null : toTest.getTaxaUid();
        return taxaUidOfDetailsModel != null && taxaUidOfDetailsModel.equals( taxaUidOfExamModel );
    }

    private boolean isValidTaxaUid( final TaxonBased toTest )
    {
        final ExamModel examModel = (ExamModel) getModels().getSelection();
        return isValidTaxaUid( examModel, toTest );
    }

    private class ComboModel extends AbstractListModel implements ComboBoxModel
    {
        private ExamModel examModel;

        private TaxonBased selected;

        private SelectionInList models;

        private final int which;

        private final ListDataListener listDataListener = new ListDataListener()
        {
            public void intervalAdded( final ListDataEvent e )
            {
                fireIntervalAdded( this, e.getIndex0(), e.getIndex1() );
            }

            public void intervalRemoved( final ListDataEvent e )
            {
                fireIntervalRemoved( this, e.getIndex0(), e.getIndex1() );
            }

            public void contentsChanged( final ListDataEvent e )
            {
                fireContentsChanged( this, e.getIndex0(), e.getIndex1() );
            }
        };

        public ComboModel( final int which )
        {
            this.which = which;
        }

        public int getSize()
        {
            return models == null ? 0 : models.getSize();
        }

        public Object getElementAt( final int index )
        {
            return models.getElementAt( index );
        }

        public void setSelectedItem( final Object item )
        {
            final TaxonBased toTest = (TaxonBased) item;
            if ( isValidTaxaUid( examModel, toTest ) )
            {
                selected = toTest;
                examModel.setUid( which, selected.getUid() );
                fireContentsChanged( this, -1, -1 );
                setDirty();
            }
            setColor( toTest );
        }

        public Object getSelectedItem()
        {
            return selected;
        }

        public void setData( final ExamModel model, final TaxonBased selected, final SelectionInList models )
        {
            if ( this.models != null )
            {
                this.models.removeListDataListener( listDataListener );
            }
            examModel = model;
            this.selected = selected;
            setColor( selected );
            this.models = models;
            if ( this.models != null )
            {
                this.models.addListDataListener( listDataListener );
            }
            fireContentsChanged( this, -1, -1 );
        }

        private void setColor( final TaxonBased toTest )
        {
            final JComboBox combo = combos[which];
            if ( isValidTaxaUid( examModel, toTest ) )
            {
                combo.setForeground( COLOR_ENABLED );
            }
            else if ( !isValidTaxaUid( examModel, selected ) )
            {
                combo.setForeground( COLOR_WRONG );
            }
        }
    }

    private class ComboRenderer extends DefaultListCellRenderer
    {
        private final int which;

        private final AbstractDetailsBuilder builder;

        public ComboRenderer( final int which )
        {
            this.which = which;
            builder = builders[which];
        }

        public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
        {
            super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
            final TaxonBased model = (TaxonBased) value;
            if ( !isValidTaxaUid( model ) )
            {
                setBackground( list.getBackground() );
                final ExamModel examModel = (ExamModel) getModels().getSelection();
                final TaxonBased detailsModel = examModel == null ? null : builder.find( examModel.get( which ) );
                if ( examModel != null && value == detailsModel )
                {
                    setForeground( COLOR_WRONG );
                }
                else
                {
                    setForeground( COLOR_DISABLED );
                }
            }
            else
            {
                if ( isSelected )
                {
                    setBackground( list.getSelectionBackground() );
                    setForeground( list.getSelectionForeground() );
                }
                else
                {
                    setBackground( list.getBackground() );
                    setForeground( COLOR_ENABLED );
                }
            }
            return this;
        }
    }
}
