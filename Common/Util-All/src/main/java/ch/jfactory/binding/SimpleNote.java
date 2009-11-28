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
package ch.jfactory.binding;

import ch.jfactory.application.view.status.Message;
import java.awt.Color;

/**
 * Note object to store information for the InfoModel.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/06 10:16:23 $
 */
public class SimpleNote implements Note
{
    private final String message;

    private final String subject;

    private final Integer percentage;

    private final Color color;

    private final Message.Type type;

    public SimpleNote( final String message )
    {
        this( message, null, null, null, null );
    }

    public SimpleNote( final String message, final String subject )
    {
        this( message, subject, null, null, null );
    }

    public SimpleNote( final String message, final Message.Type type )
    {
        this( message, null, null, null, type );
    }

    public SimpleNote( final String message, final String subject, final Message.Type type )
    {
        this( message, subject, null, null, type );
    }

    public SimpleNote( final String message, final int percentage )
    {
        this( message, null, percentage, null, null );
    }

    public SimpleNote( final String message, final Color color )
    {
        this( message, null, null, color, null );
    }

    public SimpleNote( final String message, final Integer percentage, final Color color )
    {
        this( message, null, percentage, color, null );
    }

    /**
     * Constructs a new note with the complete set of arguments.
     *
     * @param message    the message
     * @param subject    the subject
     * @param percentage the percentage
     * @param color      the color
     * @param type       the {@link ch.jfactory.application.view.status.Message.Type}
     */
    private SimpleNote( final String message, final String subject, final Integer percentage, final Color color, final Message.Type type )
    {
        this.message = message;
        this.subject = subject;
        this.percentage = percentage;
        this.color = color;
        this.type = type;
    }

    /**
     * Copy constructor.
     *
     * @param note the note to copy
     */
    public SimpleNote( final Note note )
    {
        this( note.getMessage(), note.getSubject(), note.getPercentage(), note.getColor(), note.getType() );
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    public String getSubject()
    {
        return subject;
    }

    /**
     * {@inheritDoc}
     */
    public Integer getPercentage()
    {
        return percentage;
    }

    /**
     * {@inheritDoc}
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * {@inheritDoc}
     */
    public Message.Type getType()
    {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "Note[percent=" + percentage + ",color=" + ",subject=" + subject + color + ",message=" + message + "]";
    }
}
