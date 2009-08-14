/*
 * Copyright 2002 by x-matrix Switzerland
 *
 * BevelDirection.java
 */
package ch.jfactory.application.view.border;

/** This class delivers constants for the direction of bevel borders. */
public class BevelDirection
{
    /** The bevel border is drawn like a lowerd bevel */
    public static final BevelDirection LOWERED = new BevelDirection();

    /** The bevel border is drawn like a raised bevel */
    public static final BevelDirection RAISED = new BevelDirection();

    /** Keep constructor privat for enumeration pattern */
    private BevelDirection()
    {
    }
}

