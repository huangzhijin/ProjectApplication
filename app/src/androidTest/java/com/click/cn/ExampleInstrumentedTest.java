package com.click.cn;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context mContext = InstrumentationRegistry.getTargetContext();
        FileInputStream fis = null;
        Properties props = new Properties();
        props.setProperty("key","value");
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = mContext.getDir("config", Context.MODE_PRIVATE);

            File file=new File(dirConf,"config");
//            if(file.exists()){
//                file.delete();
//            }

            if(file.exists()){
                fis = new FileInputStream(file.getAbsoluteFile());
                props.load(fis);
            }



        } catch (Exception e) {
            Log.i("eee","------------------"+e.getMessage());
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
              Log.i("eee",e.getMessage());
            }
        }
//        assertEquals("com.click.cn", appContext.getPackageName());
    }
}
