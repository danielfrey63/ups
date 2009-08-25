/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.utils;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 * Borrowed from the Spin project : http://spin.sourceforge.net
 */
public class CheckingRepaintManager extends RepaintManager {

    private static final Logger LOG = Logger.getLogger(CheckingRepaintManager.class);

    /**
     * Overriden to check EDT rule.
     */
    public synchronized void addInvalidComponent(JComponent component) {
        checkEDTRule(component);

        super.addInvalidComponent(component);
    }

    /**
     * Overriden to check EDT rule.
     */
    public synchronized void addDirtyRegion(JComponent component,
                                            int x, int y, int w, int h) {
        checkEDTRule(component);

        super.addDirtyRegion(component, x, y, w, h);
    }

    /**
     * Check EDT rule on access to the given component.
     *
     * @param component component to be repainted
     */
    protected void checkEDTRule(Component component) {
        if (violatesEDTRule(component)) {
            EDTRuleViolation violation = new EDTRuleViolation(component);

            StackTraceElement[] stackTrace = violation.getStackTrace();
            try {
                for (int e = stackTrace.length - 1; e >= 0; e--) {
                    if (isLiableToEDTRule(stackTrace[ e ])) {
                        StackTraceElement[] subStackTrace = new StackTraceElement[stackTrace.length - e];
                        System.arraycopy(stackTrace, e, subStackTrace, 0, subStackTrace.length);

                        violation.setStackTrace(subStackTrace);
                    }
                }
            }
            catch (Exception ex) {
                // keep stackTrace
            }

            indicate(violation);
        }
    }

    /**
     * Does acces to the given component violate the EDT rule.
     *
     * @param component accessed component
     * @return <code>true</code> if EDT rule is violated
     */
    protected boolean violatesEDTRule(Component component) {
        return !SwingUtilities.isEventDispatchThread() && component.isShowing();
    }

    /**
     * Is the given stackTraceElement liable to the EDT rule.
     *
     * @param element element
     * @return <code>true</code> if the className of the given element denotes a subclass of
     *         <code>java.awt.Component</code>
     */
    protected boolean isLiableToEDTRule(StackTraceElement element) throws Exception {
        return Component.class.isAssignableFrom(Class.forName(element.getClassName()));
    }

    /**
     * Indicate a violation of the EDT rule. This default implementation throws the given exception, subclasses may want
     * to log the exception instead.
     *
     * @param violation violation of EDT rule
     */
    protected void indicate(EDTRuleViolation violation) throws EDTRuleViolation {
        LOG.error("EDT rule violated", violation);
//    throw violation;
    }
}
