package com.xyb.tape;

import com.xyb.tape.ui.MathUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void compare() throws Exception {
        int res= MathUtil.compareTwoNum(30f,190f);
       assertTrue(res<0);
    }
}