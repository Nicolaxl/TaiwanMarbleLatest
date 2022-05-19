package com.leiguoliang.taiwanmarblelatest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class result extends AppCompatActivity {
    MediaPlayer SGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.FINAL_PLAYER);
        Toast.makeText(result.this, message, Toast.LENGTH_SHORT).show();


        SGame = MediaPlayer.create(getApplicationContext(), R.raw.apcey);
        SGame.setLooping(true);
        SGame.start();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap mMap) {

                LatLng sydney = new LatLng(25.105497, 121.597366);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Taiwan"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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