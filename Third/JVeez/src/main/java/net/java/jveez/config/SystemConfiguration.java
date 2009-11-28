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

package net.java.jveez.config;

import org.garret.perst.Storage;

public class SystemConfiguration extends ConfigurationItem
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3544952164523717684L;

    private boolean firstRun;

    private boolean confirmBeforeQuit;

    public SystemConfiguration()
    {
    }

    public SystemConfiguration( final Storage storage )
    {
        super( storage );
    }

    public boolean isConfirmBeforeQuit()
    {
        return confirmBeforeQuit;
    }

    public void setConfirmBeforeQuit( final boolean confirmBeforeQuit )
    {
        this.confirmBeforeQuit = confirmBeforeQuit;
        modify();
    }

    public boolean isFirstRun()
    {
        return firstRun;
    }

    public void setFirstRun( final boolean firstRun )
    {
        this.firstRun = firstRun;
        modify();
    }

    public void resetToDefaults()
    {
        firstRun = true;
        confirmBeforeQuit = true;
        modify();
    }

    public String toString()
    {
        return "SystemConfiguration[" +
                "firstRun=" + firstRun +
                ", confirmBeforeQuit=" + confirmBeforeQuit +
                "]";
    }
}
