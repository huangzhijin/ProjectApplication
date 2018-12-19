package com.click.cn.view.camera;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.click.cn.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.rotation;

public class CameraActivity extends AppCompatActivity {
    private Camera camera;
    private CameraPreview mpCameraPreview;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    ToneGenerator toneGenerator;
    boolean mCurrentOrientation=false;
    OrientationEventListener orientationEventListener;

    Button button;
    private boolean isRecording;
    String TAG="CameraPreview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_activity_camera);
        camera= getCameraInstance();
        mpCameraPreview=new CameraPreview(this,camera);
        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.frame);
        frameLayout.addView(mpCameraPreview);

         button=(Button)findViewById(R.id.button_capture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // takePicture()方法需要传入三个监听参数
                // 第一个监听器；当用户按下快门时激发该监听器
                // 第二个监听器；当相机获取原始照片时激发该监听器
                // 第三个监听器；当相机获取JPG照片时激发该监听器
                camera.takePicture(shutterCallback,null,callback);
            }
        });
//        button.setOnClickListener(clickListener);//录制视频

    }
    //判断是否存在摄像头
    private boolean hasCameraSupport() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    public int getNumberOfCameras() {
        return hasCameraSupport() ? 1 : 0;
    }
    //大于API9获取方法
    public int getNumberOfCameras2() {
        return Camera.getNumberOfCameras();
    }


    Camera.ShutterCallback shutterCallback=new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            if(toneGenerator==null){
                toneGenerator=new ToneGenerator(AudioManager.STREAM_MUSIC,ToneGenerator.MIN_VOLUME);
            }
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2);
        }
    };

    Camera.PictureCallback callback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Camera.Parameters parameters= camera.getParameters();
            if(parameters.getPictureFormat()== PixelFormat.JPEG){
                Log.i(TAG,"================parameters.getPictureFormat()=========="+parameters.getPictureFormat());
            }


            File file=createImageFile();
            try {
                byte[] newData=null;
                //判断是否需要旋转图片
                 if(mCurrentOrientation){
                    Bitmap oldBit= BitmapFactory.decodeByteArray(data,0,data.length);
                     Matrix matrix=new Matrix();
                     matrix.setRotate(90);
                     Bitmap newBitmap=  Bitmap.createBitmap(oldBit,0,0,oldBit.getWidth(),oldBit.getHeight(),matrix,true);
                     ByteArrayOutputStream outputStream1=new ByteArrayOutputStream();
                     newBitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream1);
                     newData=  outputStream1.toByteArray();
                     FileOutputStream outputStream=new FileOutputStream(file);
                     outputStream.write(newData);
                     outputStream.flush();
                     outputStream.close();
                 }else{
                     FileOutputStream outputStream=new FileOutputStream(file);
                     outputStream.write(data);
                     outputStream.flush();
                     outputStream.close();
                 }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //将图片交给Image程序处理
//            Uri uri = Uri.fromFile(file);
//            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            intent.setDataAndType(uri, "image/jpeg");
//            startActivity(intent);
            //重新浏览
            camera.stopPreview();
            camera.startPreview();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamrea();
    }



    public Camera getCameraInstance(){
        Camera c=null;
        try {
            c=Camera.open(1);//改变前置或后置摄像头
            Camera.Parameters parameters=c.getParameters();
            List<String> focuModes= parameters.getSupportedFocusModes();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            c.setParameters(parameters);
            setCameraDisplayOrientation(0,c);
//            c.setFaceDetectionListener(new MyFaceDetectionListener());//设置人脸检测监听器
//            c.setDisplayOrientation(90);
        }catch (Exception e){

        }
        return c;
    }

   private void releaseCamrea(){
       if(camera!=null){
           camera.release();
           camera=null;
       }
   }

    private File createImageFile(){
        // Create an image file name
        File imageF = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
            File albumF = Environment.getExternalStorageDirectory();
            imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageF;
    }

    public  void setCameraDisplayOrientation (  int cameraId , android.hardware.Camera camera ) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo ( cameraId , info );
        int rotation = this.getWindowManager().getDefaultDisplay ().getRotation ();
        int degrees = 0 ;
        Log.i(TAG,"================rotation=========="+rotation);

        switch ( rotation ) {
            case Surface.ROTATION_0 : degrees = 0 ;
                mCurrentOrientation=true;
                break ;
            case Surface.ROTATION_90 : degrees = 90 ;
                mCurrentOrientation=false;
                break ;
            case Surface.ROTATION_180 : degrees = 180 ;
                break ;
            case Surface.ROTATION_270 : degrees = 270 ;
                break ;
        }

        int result ;
        if ( info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ) {
            result = ( info.orientation + degrees ) % 360 ;
            result = ( 360 - result ) % 360 ;   // compensate the mirror
        } else {   // back-facing
            result = ( info.orientation - degrees + 360 ) % 360 ;
        }
        Log.i(TAG,"================result=========="+result);
        camera.setDisplayOrientation ( result );
    }

    private void startOrientationChangeListener(){  //设备方向监听器
        orientationEventListener=new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)
                        || ((rotation >= 135) && (rotation <= 225))) {// portrait
                    mCurrentOrientation = true;
                    Log.i(TAG, "竖屏");
                } else if (((rotation > 45) && (rotation < 135))
                        || ((rotation > 225) && (rotation < 315))) {// landscape
                    mCurrentOrientation = false;
                    Log.i(TAG, "横屏");
                }
            }
        };
    }

    MediaRecorder mediaRecorder;
    private boolean prepareVideoRecorder(){  //准备录制视屏
        camera=getCameraInstance();
        mediaRecorder=new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setOutputFile(getOutputMediaFile(2).toString());

        mediaRecorder.setPreviewDisplay(mpCameraPreview.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
//            releaseMediaRecorder();
            return false;
        }

        return true;
    }

    //录制视屏
    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isRecording){
                mediaRecorder.stop();
                releaseMediaRecorder();
                camera.lock();

                setCaptureButtonText("Capture");
                isRecording=false;
            }else{
                if(prepareVideoRecorder()){
                    mediaRecorder.start();
                    setCaptureButtonText("Stop");
                    isRecording=true;
                }else{
                   releaseMediaRecorder();
                }
            }
        }
    };

    private void releaseMediaRecorder(){
        if(mediaRecorder!=null){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder=null;
            camera.lock();
        }
    }

    private void setCaptureButtonText(String s){
        button.setText(s);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamrea();
    }

    private Uri getOutputMediaFileUri(int type){
        return  Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type){
        File mediaStorageDir =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
        Log.i(TAG,"------------------------"+mediaStorageDir.getPath());
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
          String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmms").format(new Date());
          File mediaFile;
           if(type==MEDIA_TYPE_IMAGE){
               mediaFile=new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
           }else if(type==MEDIA_TYPE_VIDEO){
               mediaFile=new File(mediaStorageDir.getPath()+File.separator+"VID"+timeStamp+".mp4");
           }else{
               return  null;
           }

        return mediaFile;
    }


}
