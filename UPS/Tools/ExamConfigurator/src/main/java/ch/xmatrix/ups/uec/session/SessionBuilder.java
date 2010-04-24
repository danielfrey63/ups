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
package ch.xmatrix.ups.uec.session;

import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.SimpleNote;
import ch.jfactory.convert.Converter;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.model.SessionPersister;
import ch.xmatrix.ups.uec.constraints.commands.Commands;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import ch.xmatrix.ups.uec.session.commands.UploadCommand;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.layout.CellConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import mseries.Calendar.MDateChanger;
import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.Calendar.MFieldListener;
import mseries.ui.MDateEntryField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:20 $
 */
public class SessionBuilder extends AbstractDetailsBuilder
{
    private static final String COMPONENT_DESCRIPTION = "fieldDescription";

    private static final String COMPONENT_SESKZ = "fieldSeskz";

    private static final String COMPONENT_DUE = "fieldDue";

    private static final String COMPONENT_CONSTRAINTS = "comboConstraints";

    private static final Logger LOG = LoggerFactory.getLogger( SessionBuilder.class );

    private static final String RESOURCE_FORM = "ch/xmatrix/ups/uec/session/SessionPanel.jfd";

    private static final String RESOURCE_MODEL = "/data/sessions.xml";

    private InfoModel infoModel;

    private JComboBox comboConstraints;

    private JTextField fieldDescription;

    private JTextField fieldSeskz;

    private MDateEntryField fieldDue;

    private ComboBoxAdapter comboBoxAdapter;

    private boolean isAdjusting;

    private boolean enabledByConstraints;

    public SessionBuilder()
    {
        super( new SessionFactory(), RESOURCE_MODEL, RESOURCE_FORM, 50 );
    }

    //-- ActionCommandPanelBuilder overrides

    @Override
    protected void initComponentListeners()
    {
        fieldDescription.addCaretListener( new CaretListener()
        {
            public void caretUpdate( final CaretEvent e )
            {
                final String newText = fieldDescription.getText().trim();
                final SessionModel model = getExamInfoModel();
                if ( model != null )
                {
                    final String oldText = model.getDescription() == null ? "" : model.getDescription();
                    if ( newText != null && !newText.equals( oldText ) && !isAdjusting )
                    {
                        isAdjusting = true;
                        model.setDescription( newText );
                        setDirty();
                        isAdjusting = false;
                    }
                }
            }
        } );
        fieldSeskz.addCaretListener( new CaretListener()
        {
            public void caretUpdate( final CaretEvent e )
            {
                final String newText = fieldSeskz.getText().trim();
                final SessionModel model = getExamInfoModel();
                if ( model != null )
                {
                    final String oldText = model.getSeskz() == null ? "" : model.getSeskz();
                    if ( newText != null && !newText.equals( oldText ) && !isAdjusting )
                    {
                        isAdjusting = true;
                        model.setSeskz( newText );
                        setDirty();
                        isAdjusting = false;
                    }
                }
            }
        } );
        fieldDue.addMFieldListener( new MFieldListener()
        {
            public void fieldEntered( final FocusEvent event )
            {
            }

            public void fieldExited( final FocusEvent event )
            {
                try
                {
                    final Date newDate = fieldDue.getValue();
                    final Date oldDate = getExamInfoModel().getDue();
                    if ( ( oldDate == null || newDate != null && !newDate.equals( oldDate ) ) && !isAdjusting )
                    {
                        isAdjusting = true;
                        getExamInfoModel().setDue( newDate );
                        setDirty();
                        isAdjusting = false;
                    }
                }
                catch ( ParseException e )
                {
                    LOG.warn( "not a valid date: " + fieldDue.getText() );
                }
            }
        } );
        comboConstraints.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final Constraints newConstraints = (Constraints) comboConstraints.getSelectedItem();
                final String newConstraintsUid = newConstraints == null ? "" : newConstraints.getUid();
                if ( newConstraints != null )
                {
                    enabledByConstraints = true;
                    setEnabled( true );
                }
                final SessionModel model = getExamInfoModel();
                if ( model != null )
                {
                    final String oldConstraintsUid = model.getConstraintsUid();
                    if ( newConstraintsUid != null && !newConstraintsUid.equals( oldConstraintsUid ) && !isAdjusting )
                    {
                        isAdjusting = true;
                        model.setConstraintsUid( enabledByConstraints ? newConstraintsUid : null );
                        setDirty();
                        isAdjusting = false;
                    }
                }
            }
        } );
    }

    //--- DetailsBuilder implementation

    public void setEnabled( final boolean enabled )
    {
        final SessionModel model = getExamInfoModel();
        comboConstraints.setEnabled( model != null && !model.isFixed() );
        fieldDescription.setEnabled( enabled && enabledByConstraints );
        fieldSeskz.setEnabled( enabled && enabledByConstraints );
        fieldDue.setEnabled( enabled && enabledByConstraints );
    }

    //--- AbstractDetailsBuilder implementation

    public void setInfoModel( final InfoModel infoModel )
    {
        this.infoModel = infoModel;
    }

    protected void initComponents()
    {
        try
        {
            infoModel.setNote( new SimpleNote( "Lade " + getInfoString(), infoModel.getNote().getColor() ) );

            initCommand( new UploadCommand( getCommandManager(), getModels().getListModel() ), true );

            final JToolBar bar = getCommandManager().getGroup( Commands.GROUP_ID_TOOLBAR ).createToolBar();
            final JPanel separator = getCreator().getPanel( "panelSeparator" );
            separator.add( bar, new CellConstraints().xy( 3, 1 ) );

            fieldDescription = getCreator().getTextField( COMPONENT_DESCRIPTION );
            fieldSeskz = getCreator().getTextField( COMPONENT_SESKZ );
            fieldDue = (MDateEntryField) getCreator().getComponent( COMPONENT_DUE );
            final MDefaultPullDownConstraints pullDownConstraints = new MDefaultPullDownConstraints();
            pullDownConstraints.changerStyle = MDateChanger.BUTTON;
            fieldDue.setConstraints( pullDownConstraints );
            fieldDue.setEnabled( false );

            this.comboConstraints = getCreator().getComboBox( COMPONENT_CONSTRAINTS );
            final ListModel constraintsModels = MainModel.findModelById( MainModel.MODELID_CONSTRAINTS );
            comboBoxAdapter = new ComboBoxAdapter( constraintsModels, new ValueHolder() );
            comboConstraints.setModel( comboBoxAdapter );
            setTaxonTreeDisabled();
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "could not create exam info panel", e );
        }
    }

    protected Converter getConverter()
    {
        return SessionPersister.getConverter();
    }

    protected String getInfoString()
    {
        return "Session-Editor";
    }

    protected String getModelId()
    {
        return MainModel.MODELID_SESSION;
    }

    @Override
    public void setModel( final TaxonBased model )
    {
        if ( !isAdjusting )
        {
            isAdjusting = true;
            if ( model != null )
            {
                final SessionModel sessionModel = (SessionModel) model;
                fieldDescription.setText( sessionModel.getDescription() );
                fieldSeskz.setText( sessionModel.getSeskz() );
                fieldDue.setValue( sessionModel.getDue() );
                comboBoxAdapter.setSelectedItem( AbstractMainModel.findModel( sessionModel.getConstraintsUid() ) );
            }
            else
            {
                fieldDescription.setText( null );
                fieldSeskz.setText( null );
                fieldDue.setValue( null );
                comboBoxAdapter.setSelectedItem( null );
            }
            isAdjusting = false;
        }
    }

    //-- Utilities

    private SessionModel getExamInfoModel()
    {
        return (SessionModel) getModels().getSelection();
    }
}
