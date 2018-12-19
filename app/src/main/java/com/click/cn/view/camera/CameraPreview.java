package com.click.cn.view.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by pc on 2017/09/28.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    String TAG="CameraPreview";


    public CameraPreview(Context context , Camera camera) {
        super(context);
        mCamera=camera;
        mHolder=getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            startFaceDetection();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
       if(mHolder.getSurface()==null){
          return;
       }
        Log.d(TAG, "surfaceChanged: width=" + width+"--------height="+height);

//        try {
//            mCamera.stopPreview();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            Camera.Parameters parameters=  mCamera.getParameters();
           List<Camera.Size> data= parameters.getSupportedPictureSizes();
           for(int i=0;i<data.size();i++){
               Log.d(TAG, "Camera.Size: " + data.get(i).width+"-----"+data.get(i).height);
           }

            parameters.setPictureSize(width,height);
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            startFaceDetection();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        mCamera.stopPreview();
//        //释放Camera
//        mCamera.release();
//        mCamera = null;
    }

    //调用预览后，在调用startFaceDetection
    public  void startFaceDetection(){
        Camera.Parameters parameters= mCamera.getParameters();
        //是否支持人脸检测
        if(parameters.getMaxNumDetectedFaces()>0){
            mCamera.startFaceDetection();
        }

    }



}
