package com.leiguoliang.taiwanmarblelatest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class result extends AppCompatActivity {
    MediaPlayer SGame;
    Button start, stop, up, down, left, right;
    static final LatLng THISCLASS = new LatLng(24.178326564014018, 120.6482920747276);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.FINAL_PLAYER);
        Toast.makeText(result.this, message, Toast.LENGTH_SHORT).show();

        start = (Button)findViewById(R.id.btn_start);
        stop = (Button)findViewById(R.id.btn_stop);
        up = (Button)findViewById(R.id.btn_up);
        down = (Button)findViewById(R.id.btn_down);
        left = (Button)findViewById(R.id.btn_left);
        right = (Button)findViewById(R.id.btn_right);

        SGame = MediaPlayer.create(getApplicationContext(), R.raw.apcey);
        SGame.setLooping(true);
        SGame.start();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap mMap) {

                LatLng TSH = new LatLng(24.163554905925793, 120.64733915466618);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(TSH));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

                final CameraPosition posisikamera = new CameraPosition.Builder().target(THISCLASS).zoom(17).build();
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(posisikamera), 2000, new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                Log.d("Map", "Animation finished");
                            }

                            @Override
                            public void onCancel() {
                                Log.d("Map", "Animation Interrupted");
                            }
                        });
                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.stopAnimation();
                    }
                });

                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.moveCamera(CameraUpdateFactory.scrollBy(0,-10));
                    }
                });

                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.moveCamera(CameraUpdateFactory.scrollBy(0,10));
                    }
                });

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.moveCamera(CameraUpdateFactory.scrollBy(-10,0));
                    }
                });

                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.moveCamera(CameraUpdateFactory.scrollBy(10,0));
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SGame.stop();
        SGame.release();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SGame.start();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你想回到主菜單嗎？")
                .setCancelable(false)
                .setPositiveButton("Yes 要", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(result.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No 不要", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setTitle("Do you want back to main menu?");
        alert.show();

    }
}