package com.example.t_sashar.persona;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Exchanger;

import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ScanCamera extends AppCompatActivity implements SurfaceHolder.Callback{
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.GONE);
        }
    };
    private boolean mVisible;
    boolean mPreviewRunning;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;
    Button cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_camera);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        // hide the action bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        mSurfaceView = (SurfaceView) findViewById(R.id.camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      //  cameraButton = (Button) findViewById(R.id.take_picture_button);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);

        if(mCamera != null) {
            mCamera.release();
        }
        mCamera = Camera.open();

        Log.v("Camera opened",mCamera.getParameters().getFlashMode());

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();

        // take a picture when the camera button is clicked
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cameraSource.start(mSurfaceHolder);
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
                //mCamera.takePicture(null,null, mPictureCallback);
            }
        });
    }

    private Thread cameraThread = null;
    private void openCamera(){
        if(cameraThread == null) cameraThread = new Thread() {
            @Override
            public void run() {
                openHelper();
            }
        };
        cameraThread.start();
        cameraThread.run();

    }

    /**
        Open the camera instance. Log any errors if seen.
     */
    private void openHelper(){
        try{
            mCamera = Camera.open();
            Log.v("CAMERA", "Camera opened??");
        }
        catch (RuntimeException e) {
            Log.e("CAMERA LOG", "failed to open camera");
        }
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }




    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w,
                               int h) {
        if (mPreviewRunning) {
              mCamera.stopPreview();
        }
        Camera.Parameters p = mCamera.getParameters();
        List<Camera.Size> previewSizes = p.getSupportedPreviewSizes();
        p.set("orientation", "portrait");

        p.setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE);

        Camera.Size prevSize = getOptimalPreviewSize(previewSizes, w, h);

        Camera.Size  previewSize = previewSizes.get(previewSizes.size()-1);
        // p.setPreviewSize(previewSize.width, previewSize.height);
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_270){
            mCamera.setDisplayOrientation(180);
        }
        p.setPreviewSize(prevSize.width, prevSize.height);
        mCamera.setParameters(p);
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.startPreview();
        mPreviewRunning = true;

    }

    /**
     * getOptimalPreviewSize returns the optimal size for the camera UI
     * @param sizes
     * @param w
     * @param h
     * @return optimalSize
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.2;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            //Log.d(TAG, "Checking size " + size.width + "w " + size.height
            //      + "h");
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the
        // requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @SuppressWarnings("ConstantConditions")
        public void onPictureTaken(byte[] data, Camera c) {


            try {
                File f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/"
                        + System.currentTimeMillis() + ".jpeg");

                Log.v("File", f.getAbsolutePath());

               // FileOutputStream outStream = new FileOutputStream(f);
               // outStream.write(data);
               // outStream.close();

                Log.v("LOC", Environment.getExternalStorageDirectory().getAbsolutePath());

                String file_string = f.toString();

            } catch (Exception e) {
                Log.v("Failure", e.toString());
            }
        }

    };



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // mCamera.stopPreview();
        mPreviewRunning = false;
        if(mCamera != null) {
            Log.v("Surface Destroyed", "INSIDE SURFACE DESTROYED");
            mCamera.release();
            // mCamera = null;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mCamera != null) {
            Log.v("back press", "Inside back pressed");
            mCamera.release();
            //  mCamera = null;
        }
    }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onStop(){
        if(mCamera != null) {
            Log.v("Stop", "INSIDE STOP");
            mCamera.release();
            //mCamera = null;
        }
        super.onStop();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.v("Restart", "Inside OnRestart");
        if (mCamera == null)
            Camera.open();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.v("Resume", "INside OnResume");
        if (mCamera == null)
            Camera.open();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mCamera != null) {
            Log.v("ONPAUSE", "Inside on Pause");
        }
    }
}
