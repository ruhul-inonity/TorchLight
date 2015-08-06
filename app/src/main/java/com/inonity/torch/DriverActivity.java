package com.inonity.torch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class DriverActivity extends Activity {

    ImageButton btnTorchSwitch;

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private int firstTime = 1;
    Camera.Parameters params;
    //MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        btnTorchSwitch = (ImageButton) findViewById(R.id.imageButtonTorchSwitch);

        /*
        * device flash check
        * */
        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlash){
            AlertDialog alert = new AlertDialog.Builder(DriverActivity.this).create();
            alert.setTitle("Error!");
            alert.setMessage("Sorry! Your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alert.show();
            return;

        }


        //switch button click event to toggle flash on/off
        btnTorchSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( firstTime == 1){
                    //get camera access
                    getCamera();
                    firstTime = 0;
                    //display button image
                    toggleButtonImage();
                }
                 if(isFlashOn){
                    turnOffFlash();
                }else {
                    turnOnFlash();
                }

            }
        });

    }

    private void turnOnFlash() {
        if(!isFlashOn){
            if(camera == null || params == null){
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            //change switch image
            toggleButtonImage();

        }

    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null ) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }


    private void toggleButtonImage() {
        /*if (firstTimeScreen == 1 && isFlashOn){
            btnTorchSwitch.setImageResource(R.drawable.btn_switch_off);
            firstTimeScreen = 0;
        }*/
         if(isFlashOn){
            btnTorchSwitch.setImageResource(R.drawable.btn_switch_on);
        }else {
            btnTorchSwitch.setImageResource(R.drawable.btn_switch_off);
        }

    }


    //get the camera
    private void getCamera() {
        if (camera == null){
            try{
                camera = Camera.open();
                params = camera.getParameters();
                //isFlashOn = false;
                //camera.stopPreview();
                //params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                //camera.setParameters(params);


            }catch (RuntimeException e){
                Log.e("Failed to Open Camera",e.getMessage());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();
       // getCamera();
    }

    @Override
    protected void onStop() {

    super.onStop();
        if(camera != null){
            camera.release();
            camera = null;
        }
    }




}
