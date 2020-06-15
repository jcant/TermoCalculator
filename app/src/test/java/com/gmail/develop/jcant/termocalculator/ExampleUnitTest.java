package com.gmail.develop.jcant.termocalculator;

import com.gmail.develop.jcant.termocalculator.tables.G50P.G50P0_500N;
import com.gmail.develop.jcant.termocalculator.tables.GradTable;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        GradTable gt = new G50P0_500N();
        assertEquals(24.91, gt.getTemperature(2.5));
    }
}