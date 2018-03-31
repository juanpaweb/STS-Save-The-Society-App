package com.example.sony.sts;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import android.icu.util.Calendar;
import android.os.Bundle;

import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ImageButton img;
    ImageButton call;
    ImageButton location;
    ImageButton vicinity;
    Camera camera;
    Camera.Parameters parameters;
    boolean isflash = false;
    boolean ison = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageButton) findViewById(R.id.imageButton);
        call = (ImageButton) findViewById(R.id.call);
        location = (ImageButton) findViewById(R.id.location) ;
        vicinity = (ImageButton) findViewById(R.id.Vicinity) ;

// intent call
       call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             Intent i = new Intent(MainActivity.this,call.class);
              startActivity(i);
            }
        });

        call.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Emergency Dialler")
                        .setMessage("There are Four Emergency Contacts numbers. You can save the numbers by pressing on call button.")
                        .show();
                return true;
            }
        });

// intent location
       location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(MainActivity.this,location.class);
                Log.d("intent","intent created") ;
                startActivity(j);
            }
        });

        location.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Current Location")
                        .setMessage("Displays your current location. Requires Good Internet and Location Access.")
                        .show();
                return true;
            }
        });


// intent vicinity
        vicinity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, MapsActivity.class);
                Log.d("intent","intent created") ;
                startActivity(k);
            }
        });

        vicinity.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Vicinity Map")
                        .setMessage("Displays a map showing important location in your vicinity. Click on the name for " +
                                "required near by Places. Requires Good Internet and Location Access.")
                        .show();
                return true;
            }
        });



        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();
            parameters = camera.getParameters();
            isflash = true;
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (isflash) {
                    if (!ison) {
                        img.setImageResource(R.drawable.on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.startPreview();
                        ison = true;
                    } else {
                        img.setImageResource(R.drawable.off);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        ison = false;
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error...");
                    builder.setMessage("Flash light is not available on this device");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        final MediaPlayer SirenMP = MediaPlayer.create(this, R.raw.song);
        ImageButton siren = (ImageButton) this.findViewById(R.id.siren);

        siren.setOnClickListener(new View.OnClickListener() {
            long t=0;
            @Override
            public void  onClick(View v)
            {
                long c = SystemClock.elapsedRealtime();

                if(c-t < 1000) {

                    if (SirenMP.isPlaying())
                    {
                        SirenMP.pause();
                    } else
                    {
                        SirenMP.start();
                    }
                }
                else
                    Toast.makeText(MainActivity.this,"Double tap to activate/deactivate siren", Toast.LENGTH_SHORT).show();

                t = c;
            }
        });

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Flash Light")
                        .setMessage("It is a emergency Flash Light. Toggle the icon to start/stop the flash light.")
                        .show();
                return true;
            }
        });

        siren.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("SIREN")
                        .setMessage("Double tap on icon to activate siren. Once siren activated, siren will play an alarm siren for help. " +
                                "Double tap again to stop alarm siren. Please make sure your volume is full.")
                        .show();
                return true;
            }
        });
    }

    // menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        //return super.onOptionsItemSelected(item);
        switch(item.getItemId()) {

            case R.id.about:
                intent= new Intent(MainActivity.this, about.class);
                startActivity(intent);
                return true;
            case R.id.share:
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Do you really want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onStop()
        {
            super.onStop();
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    }
