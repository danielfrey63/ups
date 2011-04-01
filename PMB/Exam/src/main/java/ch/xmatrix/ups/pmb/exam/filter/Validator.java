/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */
package ch.xmatrix.ups.pmb.exam.filter;

/** @author Daniel Frey 06.08.2008 14:59:02 */
public interface Validator<T>
{
    boolean isValid( T t );
}
