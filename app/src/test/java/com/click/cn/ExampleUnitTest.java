package com.click.cn;

import android.util.Log;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        dff.setTimeZone(TimeZone.getTimeZone("UTC"));
        String ee = dff.format(new Date());
        System.out.println("ExampleUnitTest"+ee);

        assertEquals(4, 2 + 2);
    }
}