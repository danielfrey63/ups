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
package net.java.jveez.vfs;

import java.util.List;

public class VfsEvent
{
    public static enum EventType
    {
        VFS_OPENED,
        VFS_CLOSED,
        SYNCHRONIZATION_STARTED,
        SYNCHRONIZATION_DONE,
        SORTER_CREATED,
        SORTER_DELETED,
        SORTER_UPDATED,
        PAGE_CREATED,
        PAGE_DELETED,
        PAGE_UPDATED,
    }

    private final EventType eventType;

    private Directory directory;

    private List<? extends Directory> pages;

    public VfsEvent( final EventType eventType )
    {
        this.eventType = eventType;
    }

    public VfsEvent( final EventType eventType, final Directory directoryArgument )
    {
        this.eventType = eventType;
        this.directory = directoryArgument;
    }

    public VfsEvent( final EventType eventType, final List<? extends Directory> pageListArgument )
    {
        this.eventType = eventType;
        this.pages = pageListArgument;
    }

    public EventType getEventType()
    {
        return eventType;
    }

    public List<? extends Directory> getPages()
    {
        return pages;
    }

    public Directory getPage()
    {
        return directory;
    }

    public String toString()
    {
        return "VfsEvent[" +
                "eventType=" + eventType +
                ", directory=" + directory +
                ", pages=" + pages +
                "]";
    }

}
