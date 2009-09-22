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
package ch.jfactory.projecttime.entryeditor.command;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.BufferedValueModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:10 $
 */
public class GregorianCalenderConverter extends AbstractConverter {

    private SimpleDateFormat format;

    public GregorianCalenderConverter(final BufferedValueModel model) {
        super(model);
        format = new SimpleDateFormat();
    }

    public Object convertFromSubject(Object subjectValue) {
        if (subjectValue != null) {
            return format.format(((Calendar) subjectValue).getTime());
        }
        return null;
    }

    public void setValue(Object newValue) {
        try {
            final GregorianCalendar calendar = new GregorianCalendar();
            final Date date = format.parse((String) newValue);
            calendar.setTime(date);
            subject.setValue(calendar);
        }
        catch (ParseException e) {
        }
    }
}
