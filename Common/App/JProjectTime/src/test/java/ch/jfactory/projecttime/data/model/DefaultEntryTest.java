/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.data.model;

import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.api.IFEntryHelper;
import ch.jfactory.projecttime.domain.impl.DefaultEntryBuildingRules;
import ch.jfactory.projecttime.domain.impl.DefaultEntryHelper;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test suite for the entry node persistence layer.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:56:29 $
 */
public class DefaultEntryTest extends TestCase
{
    private IFEntryHelper helper;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DefaultEntryTest( final String testName )
    {
        super( testName );
    }

    /**
     * Return this test suite.
     *
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DefaultEntryTest.class );
    }

    protected void setUp() throws Exception
    {
        final DefaultEntryBuildingRules rules = new DefaultEntryBuildingRules();
        rules.addRule( new DefaultEntryBuildingRules.Rule( null, "Customer" ) );
        rules.addRule( new DefaultEntryBuildingRules.Rule( "Customer", "Project" ) );
        rules.addRule( new DefaultEntryBuildingRules.Rule( "Project", "Feature" ) );
        rules.addRule( new DefaultEntryBuildingRules.Rule( "Feature", "TimeEntry" ) );
        helper = DefaultEntryHelper.getInstance( rules );
    }

    /**
     * Tests the EntryHelper.
     */
    public void testHelper()
    {
        final String name = "Customer1";
        final IFEntry customer = helper.createEntry( null, name, "Customer" );
        final IFEntry project = helper.createEntry( customer, "Project1", "Project" );
        final IFEntry feature = helper.createEntry( project, "WebStart Application", "Feature" );
        assertEquals( customer.getName(), name );
        assertEquals( project.getParent(), customer );

        final IFEntry timeEntry = helper.createEntry( feature, "Initial", "TimeEntry" );
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        timeEntry.setStart( startTime );
        timeEntry.setEnd( endTime );
        assertEquals( timeEntry.getStart(), startTime );
        assertEquals( timeEntry.getEnd(), endTime );

        final IFEntry timeEntry2 = helper.createEntry( feature, "Bugfix", "TimeEntry" );
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
        timeEntry2.setStart( startTime );
        timeEntry2.setEnd( endTime );
        assertEquals( timeEntry2.getStart(), startTime );
        assertEquals( timeEntry2.getEnd(), endTime );

        // Set parent with helper class using rules
        final IFEntry feature2 = helper.createEntry( project, "Desktop Application", "Feature" );
        helper.setParent( feature2, timeEntry2 );

        // Set false parent with helper class using rules
        boolean exception = false;
        try
        {
            helper.setParent( customer, timeEntry2 );
        }
        catch ( DefaultEntryBuildingRules.RuleViolationException e )
        {
            exception = true;
        }
        assertTrue( exception );

        // Set parent with object itself not using rules
        timeEntry2.setParent( customer );

        final Properties conf = new Properties();
        conf.put( "persistence.file", "out.xml" );
        helper.save( new IFEntry[]{customer}, conf );
    }

    /**
     * Tests the EntryHelpers load method.
     */
    public void testLoad()
    {
        final DefaultEntryBuildingRules rules = new DefaultEntryBuildingRules();
        final IFEntryHelper helper = DefaultEntryHelper.getInstance( rules );

        final Properties conf = new Properties();
        conf.put( "persistence.file", "out.xml" );
        final IFEntry[] entries = helper.load( conf );
    }

    /**
     * Tests violations of rules via the EntryHelper.
     */
    public void testRuleViolation()
    {
        final DefaultEntryBuildingRules rules = new DefaultEntryBuildingRules();
        rules.addRule( new DefaultEntryBuildingRules.Rule( null, "Single" ) );
        rules.addRule( new DefaultEntryBuildingRules.Rule( "Single", "Single" ) );
        final IFEntryHelper helper = DefaultEntryHelper.getInstance( rules );
        boolean exception = false;
        IFEntry entry;
        try
        {
            entry = helper.createEntry( null, "Should not build", "UnvalidType" );
        }
        catch ( DefaultEntryBuildingRules.RuleViolationException e )
        {
            exception = true;
        }
        assertTrue( exception );

        exception = false;
        try
        {
            entry = helper.createEntry( null, "Valid Single", "Single" );
            entry = helper.createEntry( entry, "Another Single", "UnvalidType" );
        }
        catch ( DefaultEntryBuildingRules.RuleViolationException e )
        {
            exception = true;
        }
        assertTrue( exception );
    }

    /**
     * Tests for the FileNotFoundException during load and save of the helper.
     */
    public void testExceptions()
    {
        final IFEntry customer = helper.createEntry( null, "Customer Name", "Customer" );
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final PrintStream oldErr = System.err;
        System.setErr( new PrintStream( stream ) );
        final Properties conf = new Properties();
        conf.put( "persistence.file", "asd://out.xml" );
        helper.save( new IFEntry[]{customer}, conf );
        assertTrue( stream.toString().indexOf( "FileNotFoundException" ) > -1 );
        stream.reset();
        helper.load( conf );
        assertTrue( stream.toString().indexOf( "FileNotFoundException" ) > -1 );
        System.setErr( oldErr );
    }
}
