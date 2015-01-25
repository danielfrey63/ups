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
        final JToggleButton editButton = ComponentFactory.createToggleButton( EditBarBuilder.class, "BUTTON.EDIT", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                taxStateModel.setEditMode( taxStateModel.getEditMode() == MODIFY ? USE : MODIFY );
            }
        } );
        final JButton newList = ComponentFactory.createButton( EditBarBuilder.class, "BUTTON.NEW", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final HerbarModel model = context.getDataModel();
                final FilterModel newModel = new FilterModel( model, "Neue Liste", false );
                newModel.addFilterTaxon( model.getRootTaxon() );
                taxStateModel.setModel( newModel );
                taxStateModel.setEditMode( MODIFY );
                context.saveModel( newModel );
            }
        } );
        final JButton renameButton = ComponentFactory.createButton( EditBarBuilder.class, "BUTTON.RENAME", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final String name = JOptionPane.showInputDialog( "Neuer Name:", taxStateModel.getModel().getName() );
                if ( name != null && !name.equals( taxStateModel.getModel().getName() ) )
                {
                    taxStateModel.setName( name );
                }
            }
        } );
        final JButton deleteButton = ComponentFactory.createButton( EditBarBuilder.class, "BUTTON.DELETE", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                context.removeModel( taxStateModel.getModel() );
                taxStateModel.setModel( context.getModel( System.getProperty( "herbar.model.default", "" ) ) );
            }
        } );
        final JButton copyButton = ComponentFactory.createButton( EditBarBuilder.class, "BUTTON.COPY", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final HerbarModel model = taxStateModel.getModel();
                if ( model instanceof FilterModel )
                {
                    final FilterModel currentModel = (FilterModel) model;
                    final FilterModel newModel = new FilterModel( context.getDataModel(), currentModel.getName() + " Kopie", false );
                    final FilterTaxon taxon = currentModel.getRootTaxon();
                    collectChildren( newModel, taxon );
                    taxStateModel.setModel( newModel );
                    taxStateModel.setEditMode( MODIFY );
                    context.saveModel( newModel );
                }
            }
        } );
        final JButton collapseButton = ComponentFactory.createButton( EditBarBuilder.class, "BUTTON.COLLAPSE", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                taxStateModel.setCollapse();
            }
        } );

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
            public void propertyChange( PropertyChangeEvent e )
            {
                final Object newModel = e.getNewValue();
                final boolean enable = newModel instanceof FilterModel && !((FilterModel) newModel).isFixed();
                editButton.setEnabled( enable );
                renameButton.setEnabled( enable );
                deleteButton.setEnabled( enable );
            }
        } );
        taxStateModel.addPropertyChangeListener( MODIFY.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                editButton.setSelected( e.getNewValue() == MODIFY );
            }
        } );
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
