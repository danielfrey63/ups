package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.component.ComponentFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.EDIT;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.USE;

/**
 * Holds the edit functionality of the navigation/list-edit panel.
 */
public class EditBarBuilder implements Builder
{

    private final JToolBar bar;

    public EditBarBuilder( final TaxStateModel taxStateModel )
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
        bar.add( editButton );
    }


    @Override
    public JComponent getPanel()
    {
        return bar;
    }
}
