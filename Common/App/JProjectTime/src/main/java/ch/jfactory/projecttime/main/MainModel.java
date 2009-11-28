/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.main;

import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.projecttime.domain.impl.DefaultEntry;
import ch.jfactory.projecttime.entryeditor.EntryModel;
import ch.jfactory.projecttime.invoice.InvoiceModel;
import ch.jfactory.projecttime.project.ProjectModel;
import ch.jfactory.projecttime.stats.StatsModel;
import ch.jfactory.typemapper.TypeModel;
import com.jgoodies.binding.value.ValueHolder;
import java.util.HashMap;
import javax.swing.JFrame;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.3 $ $Date: 2006/11/16 13:25:17 $
 */
public class MainModel extends AbstractMainModel
{
    private static final MainModel DEFAULT = new MainModel( null );

    private final ValueHolder entry2InvoiceMap = new ValueHolder( new HashMap() );

    public static final String PROPERTYNAME_TYPEMODEL = "typeModel";

    private TypeModel typeModel = new TypeModel();

    public static final String PROPERTYNAME_PROJECTMODEL = "projectModel";

    private ProjectModel projectModel = new ProjectModel( new DefaultEntry( "JProjectTime", "Root" ), entry2InvoiceMap );

    public static final String PROPERTYNAME_ENTRYMODEL = "entryModel";

    private EntryModel entryModel = new EntryModel( getProjectModel().getCurrentBeanModel() );

    public static final String PROPERTYNAME_STATSMODEL = "statsModel";

    private StatsModel statsModel = new StatsModel( projectModel.getSelectionModel(), getProjectModel().getCurrentBeanModel() );

    public static final String PROPERTYNAME_INVOICEMODEL = "invoiceModel";

    private InvoiceModel invoiceModel = new InvoiceModel( entry2InvoiceMap );

    private final JFrame parent;

    public MainModel( final JFrame parent )
    {
        this.parent = parent;
    }

    public static MainModel getDefaultModel()
    {
        return DEFAULT;
    }

    public JFrame getParent()
    {
        return parent;
    }

    // Submodels

    public ProjectModel getProjectModel()
    {
        return projectModel;
    }

    public void setProjectModel( final ProjectModel projectModel )
    {
        final ProjectModel old = getProjectModel();
        this.projectModel = projectModel;
        firePropertyChange( PROPERTYNAME_PROJECTMODEL, old, projectModel );
    }

    public TypeModel getTypeModel()
    {
        return typeModel;
    }

    public void setTypeModel( final TypeModel typeModel )
    {
        final TypeModel old = getTypeModel();
        this.typeModel = typeModel;
        firePropertyChange( PROPERTYNAME_TYPEMODEL, old, typeModel );
    }

    public EntryModel getEntryModel()
    {
        return entryModel;
    }

    public void setEntryModel( final EntryModel entryModel )
    {
        final EntryModel old = getEntryModel();
        this.entryModel = entryModel;
        firePropertyChange( PROPERTYNAME_ENTRYMODEL, old, entryModel );
    }

    public StatsModel getStatsModel()
    {
        return statsModel;
    }

    public void setStatsModel( final StatsModel statsModel )
    {
        final StatsModel old = getStatsModel();
        this.statsModel = statsModel;
        firePropertyChange( PROPERTYNAME_STATSMODEL, old, statsModel );
    }

    public InvoiceModel getInvoiceModel()
    {
        return invoiceModel;
    }

    public void setInvoiceModel( final InvoiceModel invoiceModel )
    {
        final InvoiceModel old = getInvoiceModel();
        this.invoiceModel = invoiceModel;
        firePropertyChange( PROPERTYNAME_INVOICEMODEL, old, invoiceModel );
    }
}
