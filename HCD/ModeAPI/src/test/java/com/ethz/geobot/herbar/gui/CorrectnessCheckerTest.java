package com.ethz.geobot.herbar.gui;

import com.ethz.geobot.herbar.gui.CorrectnessChecker.Correctness;
import static com.ethz.geobot.herbar.gui.CorrectnessChecker.IS_FALSE;
import static com.ethz.geobot.herbar.gui.CorrectnessChecker.IS_NEARLY_TRUE;
import static com.ethz.geobot.herbar.gui.CorrectnessChecker.IS_TRUE;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Daniel on 18.02.2016.
 */
public class CorrectnessCheckerTest
{
    private CorrectnessChecker checker;

    @Before
    public void setUp()
    {
        checker = new CorrectnessChecker();
    }

    @Test
    public void testGetCorrectnessTrueEqual() throws Exception
    {
        assertEquals( IS_TRUE, checker.getCorrectness( "Test", "Test", new String[0] ));
        assertEquals( IS_TRUE, checker.getCorrectness( "Test test", "Test test", new String[0] ));
    }

    @Test
    public void testGetCorrectnessFalseTansEns() throws Exception
    {
        assertEquals( IS_FALSE, checker.getCorrectness( "Testtans", "Testens", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testens", "Testtans", new String[0] ));
    }

    @Test
    public void testGetCorrectnessNearTansEns() throws Exception
    {
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testtans", "Test testens", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testens", "Test testtans", new String[0] ));
    }

    @Test
    public void testGetCorrectnessFalseIiI() throws Exception
    {
        // Testi <-> Testii will be close as of Levenstein similarity tolerance
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Testii", "Testi", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Testi", "Testii", new String[0] ));
    }

    @Test
    public void testGetCorrectnessNearIiI() throws Exception
    {
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testi", "Test testii", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testii", "Test testi", new String[0] ));
    }

    @Test
    public void testGetCorrectnessFalseEnRisRe() throws Exception
    {
        assertEquals( IS_FALSE, checker.getCorrectness( "Testen", "Testris", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testris", "Testen", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testris", "Testre", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testre", "Testris", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testre", "Testen", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testen", "Testre", new String[0] ));
    }

    @Test
    public void testGetCorrectnessNearEnRisRe() throws Exception
    {
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test tester", "Test testris", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testris", "Test tester", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testris", "Test testre", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testre", "Test testris", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testre", "Test tester", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test tester", "Test testre", new String[0] ));
    }

    @Test
    public void testGetCorrectnessFalseUmUsA() throws Exception
    {
        assertEquals( IS_FALSE, checker.getCorrectness( "Testum", "Testa", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testa", "Testum", new String[0] ));
        // Testus <-> Testum will be close as of Levenstein similarity tolerance
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Testus", "Testum", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Testum", "Testus", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testus", "Testa", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "Testa", "Testus", new String[0] ));
    }

    @Test
    public void testGetCorrectnessNearUmUsA() throws Exception
    {
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testum", "Test testa", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testa", "Test testum", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testus", "Test testum", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testum", "Test testus", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testus", "Test testa", new String[0] ));
        assertEquals( IS_NEARLY_TRUE, checker.getCorrectness( "Test testa", "Test testus", new String[0] ));
    }

    @Test
    public void testHuperziaSelago() throws Exception
    {
        assertEquals( IS_TRUE, checker.getCorrectness( "Huperzia selago", "Huperzium selago", new String[0] ));
    }

    @Test
    public void testGetCorrectnessFalse() throws Exception
    {
        assertEquals( IS_FALSE, checker.getCorrectness( "asdf", "Test", new String[0] ));
        assertEquals( IS_FALSE, checker.getCorrectness( "asdf", "Test test", new String[0] ));
    }
}