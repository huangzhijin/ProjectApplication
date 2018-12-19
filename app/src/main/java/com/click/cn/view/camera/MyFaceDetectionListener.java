package com.click.cn.view.camera;

import android.hardware.Camera;
import android.util.Log;

/**
 * Created by pc on 2017/09/29.
 */

public class MyFaceDetectionListener implements Camera.FaceDetectionListener {
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
       if(faces.length>0){
           Log.d("FaceDetection", "face detected: "+ faces.length +
                   " Face 1 Location X: " + faces[0].rect.centerX() +
                   "Y: " + faces[0].rect.centerY() );
       }
    }
}
