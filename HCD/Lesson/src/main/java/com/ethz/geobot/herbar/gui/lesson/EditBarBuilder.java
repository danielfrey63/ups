package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.component.ComponentFactory;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.EDIT;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.USE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Edit;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.List;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Model;

/**
 * Holds the edit functionality of the navigation/list-edit panel.
 */
public class EditBarBuilder implements Builder
{

    private final JToolBar bar;

    public EditBarBuilder( final HerbarContext context, final TaxStateModel taxStateModel )
    {
        bar = new JToolBar();
        final JToggleButton editButton = ComponentFactory.createToggleButton( EditBarBuilder.class, "BUTTON.EDIT", new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                taxStateModel.setEditMode( taxStateModel.getEditMode() == EDIT ? USE : EDIT );
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
                taxStateModel.setEditMode( EDIT );
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
        bar.add( editButton );
        bar.add( newList );
        bar.add( renameButton );
        bar.add( deleteButton );
        taxStateModel.addPropertyChangeListener( Model.name(), new PropertyChangeListener()
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
        taxStateModel.addPropertyChangeListener( Edit.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                editButton.setSelected( e.getNewValue() == EDIT );
            }
        } );
    }


    @Override
    public JComponent getPanel()
    {
        return bar;
    }
}
