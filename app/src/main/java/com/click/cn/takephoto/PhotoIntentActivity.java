package com.click.cn.takephoto;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.click.cn.R;
import com.click.cn.constants.Constans;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PhotoIntentActivity extends AppCompatActivity {

	private static final int ACTION_TAKE_PHOTO_B = 1;
	private static final int ACTION_TAKE_PHOTO_S = 2;
	private static final int ACTION_TAKE_VIDEO = 3;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageView mImageView;
	private Bitmap mImageBitmap;

	private static final String VIDEO_STORAGE_KEY = "viewvideo";
	private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
	private VideoView mVideoView;
	private Uri mVideoUri;

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

//	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;


	Button.OnClickListener mTakePicOnClickListener =
			new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
				}
			};

	Button.OnClickListener mTakePicSOnClickListener =
			new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
				}
			};

	Button.OnClickListener mTakeVidOnClickListener =
			new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					dispatchTakeVideoIntent();
				}
			};



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_takephoto);

		mImageView = (ImageView) findViewById(R.id.imageView1);
		mVideoView = (VideoView) findViewById(R.id.videoView1);
		mImageBitmap = null;
		mVideoUri = null;

		Button picBigBtn = (Button) findViewById(R.id.btnIntend);
		setBtnListenerOrDisable(
				picBigBtn,
				mTakePicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE
		);

		Button picSmallBtn = (Button) findViewById(R.id.btnIntendS);
		setBtnListenerOrDisable(
				picSmallBtn,
				mTakePicSOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE
		);

		Button videoBtn = (Button) findViewById(R.id.btnIntendV);
		setBtnListenerOrDisable(
				videoBtn,
				mTakeVidOnClickListener,
				MediaStore.ACTION_VIDEO_CAPTURE
		);


	}


	private void setBtnListenerOrDisable(
			Button btn,
			Button.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);
		} else {
			btn.setText("不能点击" + btn.getText());
			btn.setClickable(false);
		}
	}


	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
			case ACTION_TAKE_PHOTO_B:
				File f = null;

				try {
					f = createImageFile();
					mCurrentPhotoPath = f.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				} catch (IOException e) {
					e.printStackTrace();
					f = null;
					mCurrentPhotoPath = null;
				}
				break;
			case ACTION_TAKE_PHOTO_S:
				break;
			default:
				break;
		}

		startActivityForResult(takePictureIntent, actionCode);
	}

	private void dispatchTakeVideoIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case ACTION_TAKE_PHOTO_B: {
				if (resultCode == RESULT_OK) {
					handleBigCameraPhoto();
				}
				break;
			}

			case ACTION_TAKE_PHOTO_S: {
				if (resultCode == RESULT_OK) {
					handleSmallCameraPhoto(data);
				}
				break;
			}

			case ACTION_TAKE_VIDEO: {
				if (resultCode == RESULT_OK) {
					handleCameraVideo(data);
				}
				break;
			}
		}
	}



	private void handleSmallCameraPhoto(Intent intent) {
		//处理小图片方式
		Bundle extras = intent.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		mImageView.setImageBitmap(mImageBitmap);
		mVideoUri = null;
		mImageView.setVisibility(View.VISIBLE);
		mVideoView.setVisibility(View.INVISIBLE);
	}

	private void handleBigCameraPhoto() {
		Log.i("PhotoIntentActivity","------------------------"+mCurrentPhotoPath);
		if (mCurrentPhotoPath != null) {
			handleOperatePic();
			AddPicTogallery();
			mCurrentPhotoPath = null;
		}

	}





	private void handleOperatePic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();


		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
		mVideoUri = null;
		mImageView.setVisibility(View.VISIBLE);
		mVideoView.setVisibility(View.INVISIBLE);
	}

	private void AddPicTogallery() {  //添加拍照图片到相册
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}


	private void handleCameraVideo(Intent intent) {
		mVideoUri = intent.getData();
		mVideoView.setVideoURI(mVideoUri);
		mImageBitmap = null;
		mVideoView.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.INVISIBLE);
		Log.i("mVideoUri","---------------"+mVideoUri);
		if(mVideoUri!=null&&!mVideoView.isPlaying()){
			mVideoView.setVideoURI(mVideoUri);
//			mVideoView.setMediaController(new MediaController(PhotoIntentActivity.this));
			MediaController mediaController=new MediaController(PhotoIntentActivity.this){
				@Override
				public void hide() {
					super.show();
				}
			};

			mVideoView.setMediaController(mediaController );
			mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {

				}
			});
			mVideoView.start();
		}
	}




	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File fileDir = getFileDir();
		File imagef = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, fileDir);
		mCurrentPhotoPath = imagef.getAbsolutePath();
		return imagef;
	}




	
	private File getFileDir() {
		File dir=new File(Constans.PATH_DOWNLOAD_IMAGE_DIR);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}



	/** Called when the activity is first created. */



	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
		outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
				savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? 
						ImageView.VISIBLE : ImageView.INVISIBLE
		);
		mVideoView.setVideoURI(mVideoUri);
		mVideoView.setVisibility(
				savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ? 
						ImageView.VISIBLE : ImageView.INVISIBLE
		);
	}


	private   boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}




}