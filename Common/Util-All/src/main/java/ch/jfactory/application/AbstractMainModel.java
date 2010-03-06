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
package ch.jfactory.application;

import ch.jfactory.command.ClosingModel;
import ch.jfactory.jgoodies.model.DirtyCapableModel;
import ch.jfactory.model.IdAware;
import ch.jfactory.model.SimpleModelList;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This model is provided under the assumption, that a view builds its content based on cards. It provides some common
 * tasks for main models:
 *
 * <ul>
 *
 * <li>The welcome card property</li>
 *
 * <li>A property to handle and notify changes of the cards</li>
 *
 * <li>A property to handle and notify the completion of loading the model</li>
 *
 * <li>A property to handle and notify the opening and closing of a file in work</li>
 *
 * <li>A property to handle and notify errors</li>
 *
 * </ul>
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class AbstractMainModel extends DirtyCapableModel implements ClosingModel
{
    /**
     * This class logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger( AbstractMainModel.class );

    public static final String PROPERTYNAME_CURRENTCARD = "currentCard";

    public static final String CARDS_WELCOME = "welcomeCard";

    private String currentCard = CARDS_WELCOME;

    /**
     * Property for a new file that has been opened.
     */
    public static final String PROPERTYNAME_CURRENTFILE = "currentFile";

    /**
     * I use two default files to trigger events even when the user create another "new file", to distiguish these
     * events. That's also the reason to hide these properties. Instead a method {@link #isDefaultFile()} is provided.
     */
    private static final File DEFAULT_NEWFILE1 = new File( "<unbenannt>" );

    private static final File DEFAULT_NEWFILE2 = new File( "<unbenannt>" );

    private File currentFile = DEFAULT_NEWFILE1;

    /**
     * Property that is fired when the main model has been loaded.
     */
    public static final String PROPERTYNAME_MODELLOADED = "modelLoaded";

    private boolean modelLoaded = false;

    /**
     * Property for the state when a file is being opened. Used to invoke actions in the view.
     */
    public static final String EVENTNAME_OPENING = "opening";

    /**
     * Property for closing the edit card.
     */
    public static final String EVENTNAME_CLOSING = "closing";

    public static final String PROPERTYNAME_ERROR = "error";

    private String error;

    private String lastOpenSaveDirectory = System.getProperty( "user.dir" );

    private String lastExportDirectory = System.getProperty( "user.dir" );

    private final WorkQueue queue = new WorkQueue( 1 );

    protected static final List<SimpleModelList> MODELS = new ArrayList<SimpleModelList>();

    private static final Map<String, SimpleModelList> MAP = new HashMap<String, SimpleModelList>();

    //--- Events

    public void setClosing() throws PropertyVetoException
    {
        if ( currentCard != CARDS_WELCOME )
        {
            fireVetoableChange( EVENTNAME_CLOSING, false, true );
            firePropertyChange( EVENTNAME_CLOSING, false, true );
        }
    }

    /**
     * Indicates that something will be opened (i.e. a new file).
     */
    public void setOpening()
    {
        firePropertyChange( EVENTNAME_OPENING, false, true );
    }

    //--- Properties

    public File getCurrentFile()
    {
        return currentFile == null ? DEFAULT_NEWFILE1 : currentFile;
    }

    public void setCurrentFile( final File newValue )
    {
        final File oldValue = getCurrentFile();
        currentFile = newValue == null ?
                ( currentFile == DEFAULT_NEWFILE2 || currentFile == null ? DEFAULT_NEWFILE1 :
                        ( currentFile == DEFAULT_NEWFILE1 ? DEFAULT_NEWFILE2 : newValue ) ) : newValue;
        firePropertyChange( PROPERTYNAME_CURRENTFILE, oldValue, currentFile, true );
    }

    public boolean isDefaultFile()
    {
        final File file = getCurrentFile();
        return file == DEFAULT_NEWFILE1 || file == DEFAULT_NEWFILE2;
    }

    public boolean isModelLoaded()
    {
        return modelLoaded;
    }

    public void setModelLoaded( final boolean newLoaded )
    {
        final boolean oldLoaded = modelLoaded;
        modelLoaded = newLoaded;
        firePropertyChange( PROPERTYNAME_MODELLOADED, oldLoaded, newLoaded );
    }

    public String getCurrentCard()
    {
        return currentCard;
    }

    public void setCurrentCard( final String newCard )
    {
        final String oldCard = getCurrentCard();
        currentCard = newCard;
        firePropertyChange( PROPERTYNAME_CURRENTCARD, oldCard, currentCard );
    }

    public String getError()
    {
        return error;
    }

    public void setError( final String error )
    {
        final String old = getError();
        this.error = error;
        firePropertyChange( PROPERTYNAME_ERROR, old, error );
    }

    public String getLastOpenSaveDirectory()
    {
        return lastOpenSaveDirectory;
    }

    public void setLastOpenSaveDirectory( final String lastOpenSaveDirectory )
    {
        this.lastOpenSaveDirectory = lastOpenSaveDirectory;
    }

    public String getLastExportDirectory()
    {
        return lastExportDirectory;
    }

    public void setLastExportDirectory( final String lastExportDirectory )
    {
        this.lastExportDirectory = lastExportDirectory;
    }

    public void queue( final Runnable runnable )
    {
        queue.execute( runnable );
    }

    public static void registerModels( final String typeId, final SimpleModelList models )
    {
        MODELS.add( models );
        MAP.put( typeId, models );
    }

    public static List<SimpleModelList> getModels()
    {
        return MODELS;
    }

    /**
     * Searches all types of models for the given guid.
     *
     * @param guid the global unique identifier to search for
     * @return the models IdAware interface or null if none can be found
     */
    public static IdAware findModel( final String guid )
    {
        for ( final SimpleModelList list : MODELS )
        {
            for ( int j = 0; j < list.getSize(); j++ )
            {
                final IdAware model = (IdAware) list.getElementAt( j );
                if ( model.getUid().equals( guid ) )
                {
                    return model;
                }
            }
        }
        LOG.warn( "model for " + guid + " not found" );
        return null;
    }

    /**
     * Returns the model with the given type id.
     *
     * @param typeId the id to search the model types for
     * @return the model found or null if none can be found
     */
    public static SimpleModelList findModelById( final String typeId )
    {
        return MAP.get( typeId );
    }
}
