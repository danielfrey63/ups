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
import ch.jfactory.color.ColorUtils;
import java.awt.Color;

/**
 * Encode message for {@link InfoModel}.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/06 10:16:23 $
 */
public class CodedNote implements Note
{
    private String message = null;

    private Integer percentage = null;

    private Color color = null;

    private String subject = null;

    /**
     * Accepts a coded note like <code>Finished..., 10, #008000, false</code>. The first token is parsed to the message,
     * the second into the percentage, the third into the color.
     *
     * @param codedNote the encoded note
     */
    public CodedNote( final String codedNote )
    {
        final String[] tokens = codedNote.split( " *, *" );
        if ( tokens.length > 0 )
        {
            message = tokens[0];
        }
        if ( tokens.length > 1 )
        {
            percentage = Integer.parseInt( tokens[1] );
        }
        if ( tokens.length > 2 )
        {
            color = ColorUtils.parseColor( tokens[2] );
        }
        if ( tokens.length > 3 )
        {
            subject = tokens[3];
        }
    }

    public String getMessage()
    {
        return message;
    }

    public String getSubject()
    {
        return subject;
    }

    public Integer getPercentage()
    {
        return percentage;
    }

    public Color getColor()
    {
        return color;
    }

    public Message.Type getType()
    {
        return Message.Type.INFO;
    }

    public String toString()
    {
        return "Note[percent=" + percentage + ",color=" + color + ",subject=" + subject + ",message=" + message + "]";
    }
}
