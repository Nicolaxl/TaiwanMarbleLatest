package com.leiguoliang.taiwanmarblelatest;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static String FINAL_PLAYER = "";
    String send;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int total_player = 0;
    MediaPlayer mpMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mpMain = MediaPlayer.create(getApplicationContext(), R.raw.onrun);
        mpMain.setLooping(true);
        mpMain.start();
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        Button tombol =(Button) findViewById(R.id.button);
        tombol.setOnClickListener(action);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpMain.stop();
        mpMain.release();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mpMain.start();
    }

    private View.OnClickListener action =new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            String selectedString;
            if(selectedId==-1){
                Toast.makeText(MainActivity.this,"Please select number of players! 請選擇玩家人數！", Toast.LENGTH_SHORT).show();
            }
            else{
                selectedString = (String) radioButton.getText();
                mpMain.stop();
                switch (selectedString) {
                    case "2 Player": {
                        total_player = 2;
                        send = String.valueOf(total_player);
                        Intent intent = new Intent(MainActivity.this, result.class);
                        intent.putExtra(FINAL_PLAYER, send);
                        startActivity(intent);
                        //Toast.makeText(MainActivity.this,radioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "3 Player": {
                        total_player = 3;
                        send = String.valueOf(total_player);
                        Intent intent = new Intent(MainActivity.this, result.class);
                        intent.putExtra(FINAL_PLAYER, send);
                        startActivity(intent);
                        //Toast.makeText(MainActivity.this,radioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "4 Player": {
                        total_player = 4;
                        send = String.valueOf(total_player);
                        Intent intent = new Intent(MainActivity.this, result.class);
                        intent.putExtra(FINAL_PLAYER, send);
                        startActivity(intent);
                        //Toast.makeText(MainActivity.this,radioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你要關閉這個遊戲嗎？")
                .setCancelable(false)
                .setPositiveButton("Yes 要", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        mpMain.stop();
                        mpMain.release();
                    }
                })
                .setNegativeButton("No 不要", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setTitle("Do you want to close this game?");
        alert.show();
    }

}