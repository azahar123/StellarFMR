package com.huawei.stellarfmr.stellarfmr.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.utils.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, Camera.ShutterCallback
{

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;

    String output_file_name;

    ImageView button;
    ImageView done, next, delete;
    LinearLayout selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        File myDirectory = new File(Environment.getExternalStorageDirectory(), "StellarFMR");
        if(!myDirectory.exists()) {
            myDirectory.mkdirs();
        }


        button = (ImageView) findViewById(R.id.imgeButton);
        done = (ImageView) findViewById(R.id.cam_done);
        next = (ImageView) findViewById(R.id.cam_new);
        delete = (ImageView) findViewById(R.id.cam_delete);

        selector = (LinearLayout) findViewById(R.id.cam_sel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, jpegCallback);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.siteInfo = true;
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.VISIBLE);
                selector.setVisibility(View.GONE);
                if (((Config.imageInternetCount+1)+Config.siteInfoImages.size()) > Integer.parseInt(Config.userDetails.get(15))){

                    AlertDialog alertDialog = new AlertDialog.Builder(CameraActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("You can take maximum of "+ Config.userDetails.get(15)+" photos only.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    done.setVisibility(View.VISIBLE);
                                    button.setVisibility(View.GONE);
                                    Config.siteInfo = true;
                                    finish();
                                }
                            });
                    alertDialog.show();

                }else{
                    camera.startPreview();

                }
            }
        });

        try {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        File pictureFile = new File(output_file_name);
                        if (pictureFile.exists()) {
                            pictureFile.delete();
                        }
                        Config.siteInfoImages.remove(Config.siteInfoImages.size()-1);
                        camera.startPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        cameraView();


    }

    public void cameraView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        // mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                camera.stopPreview();
                String timeStamp = "stellar"+ Config.siteInfoImages.size();
                output_file_name = Environment.getExternalStorageDirectory() + File.separator+"StellarFMR"+File.separator + timeStamp + ".jpeg";
                button.setVisibility(View.GONE);
                selector.setVisibility(View.VISIBLE);

                File pictureFile = new File(output_file_name);
                if (pictureFile.exists()) {
                    pictureFile.delete();
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ExifInterface exif=new ExifInterface(pictureFile.toString());

                    //Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
                    if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                        realImage= rotate(realImage, 90);
                    } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                        realImage= rotate(realImage, 270);
                    } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                        realImage= rotate(realImage, 180);
                    } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")){
                        realImage= rotate(realImage, 90);
                    }

                    boolean bo = realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Config.siteInfoImages.add(output_file_name);

                    fos.close();


                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
                //refreshCamera();
            }
        };
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public void captureImage() throws IOException {
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            Camera.Parameters params = camera.getParameters();
            if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else {
                //Choose another supported mode
            }
            camera.setParameters(params);
            setCameraDisplayOrientation(CameraActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
        } catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

//        Camera.Parameters param;
//        param = camera.getParameters();
//        param.setPreviewSize(352, 288);
        //param.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//
//        if (mSupportedPreviewSizes != null) {
//            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
//        }
//
//        float ratio;
//        if(mPreviewSize.height >= mPreviewSize.width)
//            ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
//        else
//            ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;
//
//        // One of these methods should be used, second method squishes preview slightly
//        setMeasuredDimension(width, (int) (width * ratio));
////        setMeasuredDimension((int) (width * ratio), height);
//    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

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


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onShutter() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Config.siteInfo = true;
        finish();
    }
}
