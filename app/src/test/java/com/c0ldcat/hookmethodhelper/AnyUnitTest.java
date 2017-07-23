package com.c0ldcat.hookmethodhelper;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AnyUnitTest {
    @Test
    public void anytest_isCorrect() throws Exception {
        String str = "onCreate(Bundle, A, B)";
        String out[] = str.replace(")", "").split("\\(");
        ArrayList<String> params = new ArrayList<>();
        if (out.length == 2) {
            for (String param : out[1].split(",")) {
                params.add(param.replace(" ", ""));
            }
        }
        assertEquals(new String[]{"Bundle", "A", "B"}, params.toArray());
    }
}