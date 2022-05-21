package com.leiguoliang.taiwanmarblelatest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;


public class result extends AppCompatActivity {
    final static int player_init_money = 500;
    int icon_height = 133;
    int icon_width = 80;

    Marker p1_mark, p2_mark, p3_mark, p4_mark;

    MediaPlayer SGame;
    boolean atInit = false;
    static final LatLng CENTRALTW = new LatLng(23.69781, 120.960515);
    static final double[] pointX ={25.135389052877766, 25.039892147214466, 24.994926665365817,
            24.835541700441734, 24.568817202678524, 24.150687238149203, 24.018558941060665,
            23.72538947169954,  23.483252071214118, 23.007943047383666, 22.64992037875815,
            22.576392912465863, 22.859536037828413, 23.9509282845216, 24.73666665642769};

    static final double[] pointY ={121.74251014108933, 121.54609384840816, 121.32279953672855,
            120.95270991261124, 120.80591458558824, 120.72528052859283, 120.46063545363027,
            120.43906480284772,  120.45313389539437, 120.24418987930757, 120.25090763025693,
            120.60659002175329, 121.16318071503659, 121.55594191188305, 121.77566847039398};

    static final String[] cities_name = {"Keelung", "Taipei", "Taoyuan", "Hsinchu", "Miaoli", "Taichung",
    "Taichung", "Yunlin", "Chiayi", "Tainan", "Kaohsiung", "Pingtung", "Taitung", "Hualien", "Yilan"};

    PolylineOptions polylineOptions = new PolylineOptions();

    List<LatLng> loc_coor = new ArrayList<LatLng>();

    int now;

    ArrayList<Integer> player_seq;
    int[] player_money = new int[4];
    int[] player_position = new int[4];
    int[][] place_occ = new int[15][2];

    public static ArrayList<Integer> getRandomNonRepeatingIntegers(int size) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        Random random = new Random();
        while (numbers.size() < size) {
            int randomNumber = random.nextInt((size - 1) + 1) + 1;
            if (!numbers.contains(randomNumber)) {
                numbers.add(randomNumber);
            }
        }
        return numbers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView desc = (TextView)findViewById(R.id.desc);


        for (int i = 0 ; i < pointX.length; i++){
            loc_coor.add(new LatLng(pointX[i],pointY[i]));
        };

        Intent intent = getIntent();
        int numplayer = Integer.parseInt(intent.getStringExtra(MainActivity.FINAL_PLAYER));

        SGame = MediaPlayer.create(getApplicationContext(), R.raw.apcey);
        SGame.setLooping(true);
        SGame.start();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMapReady(@NonNull GoogleMap mMap) {
                Bitmap rp = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.player_red);
                Bitmap rpbit = Bitmap.createScaledBitmap(rp, icon_width, icon_height, false);
                BitmapDescriptor redp_icon = BitmapDescriptorFactory.fromBitmap(rpbit);

                Bitmap yp = BitmapFactory.decodeResource(getResources(),R.drawable.player_yellow);
                Bitmap ypbit = Bitmap.createScaledBitmap(yp, icon_width, icon_height, false);
                BitmapDescriptor yellowp_icon = BitmapDescriptorFactory.fromBitmap(ypbit);

                Bitmap gp = BitmapFactory.decodeResource(getResources(),R.drawable.player_green);
                Bitmap gpbit = Bitmap.createScaledBitmap(gp, icon_width, icon_height, false);
                BitmapDescriptor greenp_icon = BitmapDescriptorFactory.fromBitmap(gpbit);

                Bitmap bp = BitmapFactory.decodeResource(getResources(),R.drawable.player_blue);
                Bitmap bpbit = Bitmap.createScaledBitmap(bp, icon_width, icon_height, false);
                BitmapDescriptor bluep_icon = BitmapDescriptorFactory.fromBitmap(bpbit);


                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_coor.get(0)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                desc.setText("Starting " + numplayer + " Players game" +
                        "\n開始" + numplayer + "人遊戲" + "\n\n Initializing map 地圖初始中");


                Handler myHandler = new Handler();
                for (int in=0; in <=pointX.length;in++) {
                    final int[] finalIn = {in};
                    myHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (finalIn[0] == pointX.length){
                                finalIn[0] = 0;
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_coor.get(finalIn[0])));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            polylineOptions.add(loc_coor.get(finalIn[0]));
                            mMap.addPolyline(polylineOptions);
                        }
                    }, 400L * finalIn[0]);
                }

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final CameraPosition posisikamera = new CameraPosition.Builder().target(CENTRALTW).zoom(7).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(posisikamera), 1000, new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                desc.setText("There are 15 cities \n 總共有15個城市 \n\n Buy them all to win this game!\n全部購買即可贏得遊戲！");
                            }

                            @Override
                            public void onCancel() {
                                desc.setText("animateCamera Error！");
                            }
                        });
                    }
                }, 6500);

                final Boolean[] fdone = {false};
                final int[] exempt = new int[1];

                final Handler outerhandler = new Handler(Looper.getMainLooper());
                outerhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        player_seq = getRandomNonRepeatingIntegers(numplayer);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_coor.get(0)));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                        if (numplayer == 2){
                            desc.setText("Player sequence 玩家序列: \n" + player_seq.get(0) +" " +
                                    player_seq.get(1));

                            p1_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 1"));
                            p1_mark.setIcon(redp_icon);
                            p2_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 2"));
                            p2_mark.setIcon(yellowp_icon);
                        }
                        else if(numplayer == 3){
                            desc.setText("Player sequence 玩家序列: \n" + player_seq.get(0) +" " +
                                    player_seq.get(1) +" "+ player_seq.get(2));

                            p1_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 1"));
                            p1_mark.setIcon(redp_icon);
                            p2_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 2"));
                            p2_mark.setIcon(yellowp_icon);
                            p3_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude - 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 3"));
                            p3_mark.setIcon(greenp_icon);
                        }
                        else if (numplayer == 4){
                            desc.setText("Player sequence 玩家序列: \n" + player_seq.get(0) +" " +
                                    player_seq.get(1) +" "+ player_seq.get(2) +" "+ player_seq.get(3));

                            p1_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 1"));
                            p1_mark.setIcon(redp_icon);
                            p2_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 2"));
                            p2_mark.setIcon(yellowp_icon);
                            p3_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude - 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 3"));
                            p3_mark.setIcon(greenp_icon);
                            p4_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude - 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 4"));
                            p4_mark.setIcon(bluep_icon);
                        }
                    }
                }, 10000);

                final Handler moneyinit = new Handler(Looper.getMainLooper());
                Handler loadmoney= new Handler();
                moneyinit.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (numplayer == 2){
                            for (int in=0; in <= player_init_money;in++) {
                                int finalIn = in;
                                loadmoney.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        desc.setText("Initial player money:\n" + "初始玩家錢:\n"
                                        + "Player 1: $" + finalIn + "\nPlayer 2: $" + finalIn);
                                    }
                                }, 5L * in);
                            }
                            player_money[0] = player_init_money;
                            player_money[1] = player_init_money;
                        }
                        else if(numplayer == 3){
                            for (int in=0; in <= player_init_money;in++) {
                                int finalIn = in;
                                loadmoney.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        desc.setText("Initial player money:\n" + "初始玩家錢:\n"
                                                + "Player 1: $" + finalIn + "\nPlayer 2: $" + finalIn
                                                + "\nPlayer 3: $" + finalIn);
                                    }
                                }, 5L * in);
                            }
                            player_money[0] = player_init_money;
                            player_money[1] = player_init_money;
                            player_money[2] = player_init_money;
                        }
                        else if (numplayer == 4){
                            for (int in=0; in <= player_init_money;in++) {
                                int finalIn = in;
                                loadmoney.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        desc.setText("Initial player money:\n" + "初始玩家錢:\n"
                                                + "Player 1: $" + finalIn + "\nPlayer 2: $" + finalIn
                                                + "\nPlayer 3: $" + finalIn + "\nPlayer 4: $" + finalIn);
                                    }
                                }, 5L * in);
                            }
                            player_money[0] = player_init_money;
                            player_money[1] = player_init_money;
                            player_money[2] = player_init_money;
                            player_money[3] = player_init_money;


                        }
                    }
                }, 13000);
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