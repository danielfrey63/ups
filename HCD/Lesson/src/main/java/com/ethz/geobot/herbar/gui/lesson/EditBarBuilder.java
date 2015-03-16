package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.component.ComponentFactory;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.MODIFY;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.USE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.LIST;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import com.ethz.geobot.herbar.model.filter.FilterTaxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * Holds the edit functionality of the navigation/list-edit panel.
 */
public class EditBarBuilder implements Builder
{

    private final JToolBar bar;

    public EditBarBuilder( final HerbarContext context, final TaxStateModel taxStateModel )
    {
        final JToggleButton editButton = ComponentFactory.createToggleButton( LessonMode.class, "BUTTON.EDIT", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                taxStateModel.setEditMode( taxStateModel.getEditMode() == MODIFY ? USE : MODIFY );
            }
        } );
        final JButton newList = ComponentFactory.createButton( LessonMode.class, "BUTTON.NEW", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final HerbarModel model = context.getDataModel();
                final FilterModel newModel = new FilterModel( model, "Neue Liste", false, 99 );
                newModel.addFilterTaxon( model.getRootTaxon() );
                taxStateModel.setNewModel( newModel );
                context.saveModel( newModel );
                if ( !editButton.isSelected() )
                {
                    editButton.doClick();
                }
            }
        } );
        final JButton renameButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.RENAME", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final String name = JOptionPane.showInputDialog( "Neuer Name:", taxStateModel.getModel().getName() );
                if ( name != null && !name.equals( taxStateModel.getModel().getName() ) )
                {
                    context.removeModel( taxStateModel.getModel() );
                    taxStateModel.setName( name );
                }
                context.saveModel( taxStateModel.getModel() );
            }
        } );
        final JButton deleteButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.DELETE", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                context.removeModel( taxStateModel.getModel() );
                if ( editButton.isSelected() )
                {
                    editButton.doClick();
                }
                taxStateModel.setModel( context.getDefaultModel() );
            }
        } );
        final JButton copyButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.COPY", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final HerbarModel model = taxStateModel.getModel();
                if ( model instanceof FilterModel )
                {
                    final FilterModel currentModel = (FilterModel) model;
                    final FilterModel newModel = new FilterModel( context.getDataModel(), currentModel.getName() + " Kopie", false, 99 );
                    final FilterTaxon taxon = currentModel.getRootTaxon();
                    collectChildren( newModel, taxon );
                    taxStateModel.setModel( newModel );
                    context.saveModel( newModel );
                    if ( !editButton.isSelected() )
                    {
                        editButton.doClick();
                    }
                }
            }
        } );
        final JButton collapseButton = ComponentFactory.createButton( LessonMode.class, "BUTTON.COLLAPSE", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                taxStateModel.setCollapse();
            }
        } );

        setEnabledStates( taxStateModel.getModel(), editButton, renameButton, deleteButton );

        bar = new JToolBar();
        bar.add( editButton );
        bar.add( newList );
        bar.add( copyButton );
        bar.add( renameButton );
        bar.add( deleteButton );
        bar.add( collapseButton );
        taxStateModel.addPropertyChangeListener( LIST.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( final PropertyChangeEvent e )
            {
                final Object model = e.getNewValue();
                setEnabledStates( model, editButton, renameButton, deleteButton );
            }
        } );
        taxStateModel.addPropertyChangeListener( MODIFY.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( final PropertyChangeEvent e )
            {
                editButton.setSelected( e.getNewValue() == MODIFY );
            }
        } );
    }

    private void setEnabledStates( final Object model, final JToggleButton editButton, final JButton renameButton, final JButton deleteButton )
    {
        final boolean enable = model instanceof FilterModel && !((FilterModel) model).isFixed();
        editButton.setEnabled( enable );
        renameButton.setEnabled( enable );
        deleteButton.setEnabled( enable );
    }

    private void collectChildren( final FilterModel newModel, final FilterTaxon taxon )
    {
        newModel.addFilterTaxon( taxon.getDependentTaxon() );
        for ( final FilterTaxon child : taxon.getChildTaxa() )
        {
            collectChildren( newModel, child );
        }
    }

    @Override
    public JComponent getPanel()
    {
        return bar;
    }
}
