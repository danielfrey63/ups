// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.06.2005 09:08:07
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb 

package ch.jfactory.application.view.search.event;

import java.util.EventListener;

// Referenced classes of package com.jidesoft.swing.event:
//            SearchableEvent

public interface SearchableListener
    extends EventListener {

    public abstract void searchableEventFired(SearchableEvent searchableevent);
}