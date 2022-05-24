package com.leiguoliang.taiwanmarblelatest;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class result extends AppCompatActivity {
    final static int player_init_money = 500;
    int icon_height = 133;
    int icon_width = 80;

    Marker p1_mark, p2_mark, p3_mark, p4_mark;

    List<Marker> p1_build = new ArrayList<>();
    List<Marker> p2_build = new ArrayList<>();
    List<Marker> p3_build = new ArrayList<>();
    List<Marker> p4_build = new ArrayList<>();

    MediaPlayer SGame;

    Button ok, roll, cancel;

    int dicenum = 0;

    int numplayer = 0;

    boolean wintrue = false;

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
    "Changhua", "Yunlin", "Chiayi", "Tainan", "Kaohsiung", "Pingtung", "Taitung", "Hualien", "Yilan"};

    static final String[] cities_name_ch = {"基隆", "臺北", "桃園", "新竹", "苗慄", "臺中",
            "彰化", "雲林", "嘉義", "臺南", "高雄", "屏東", "臺東", "花蓮", "宜蘭"};

    PolylineOptions polylineOptions = new PolylineOptions();

    List<LatLng> loc_coor = new ArrayList<>();

    int player_now = 0;

    ArrayList<Integer> player_seq;
    int[] player_money = new int[4];
    int[] player_position = new int[4];
    int[][] place_occ = new int[15][2];
    int[] rank = {1,2,3,4};

    public static ArrayList<Integer> getRandomNonRepeatingIntegers(int size) {
        ArrayList<Integer> numbers = new ArrayList<>();
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

        for (int i = 0; i < 15;i++){
            place_occ[i][0] = -1;
        }

        setContentView(R.layout.activity_result);

        ok = (Button)findViewById(R.id.button_buy);
        roll = (Button)findViewById(R.id.button_roll);
        cancel = (Button)findViewById(R.id.button_cancel);

        ok.setVisibility(View.INVISIBLE);
        roll.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);


        TextView desc = (TextView)findViewById(R.id.desc);


        for (int i = 0 ; i < pointX.length; i++){
            loc_coor.add(new LatLng(pointX[i],pointY[i]));
        }

        Intent intent = getIntent();

        numplayer = Integer.parseInt(intent.getStringExtra(MainActivity.FINAL_PLAYER));

        SGame = MediaPlayer.create(getApplicationContext(), R.raw.apcey);
        SGame.setLooping(true);
        SGame.start();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);



        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            public void roll(){
                roll.setVisibility(View.INVISIBLE);
                Handler rollhandler = new Handler();
                for (int r = 1; r <= 150; r++){
                    rollhandler.postDelayed(() -> {
                        dicenum = new Random().nextInt(6) + 1;
                        desc.setText(String.valueOf(dicenum));
                    }, 10L * r);
                }
            }


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

                Bitmap rb1p = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.build1_red);
                Bitmap rp1bbit = Bitmap.createScaledBitmap(rb1p, icon_width, icon_height, false);
                BitmapDescriptor redp_b1 = BitmapDescriptorFactory.fromBitmap(rp1bbit);

                Bitmap yb1p = BitmapFactory.decodeResource(getResources(),R.drawable.build1_yellow);
                Bitmap yp1bbit = Bitmap.createScaledBitmap(yb1p, icon_width, icon_height, false);
                BitmapDescriptor yellowp_b1 = BitmapDescriptorFactory.fromBitmap(yp1bbit);

                Bitmap gb1p = BitmapFactory.decodeResource(getResources(),R.drawable.build1_green);
                Bitmap gp1bbit = Bitmap.createScaledBitmap(gb1p, icon_width, icon_height, false);
                BitmapDescriptor greenp_b1 = BitmapDescriptorFactory.fromBitmap(gp1bbit);

                Bitmap bb1p = BitmapFactory.decodeResource(getResources(),R.drawable.build1_blue);
                Bitmap bpb1bit = Bitmap.createScaledBitmap(bb1p, icon_width, icon_height, false);
                BitmapDescriptor bluep_b1 = BitmapDescriptorFactory.fromBitmap(bpb1bit);

                Bitmap rb2p = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.build2_red);
                Bitmap rp2bbit = Bitmap.createScaledBitmap(rb2p, icon_width, icon_height, false);
                BitmapDescriptor redp_b2 = BitmapDescriptorFactory.fromBitmap(rp2bbit);

                Bitmap yb2p = BitmapFactory.decodeResource(getResources(),R.drawable.build2_yellow);
                Bitmap yp2bbit = Bitmap.createScaledBitmap(yb2p, icon_width, icon_height, false);
                BitmapDescriptor yellowp_b2 = BitmapDescriptorFactory.fromBitmap(yp2bbit);

                Bitmap gb2p = BitmapFactory.decodeResource(getResources(),R.drawable.build2_green);
                Bitmap gp2bbit = Bitmap.createScaledBitmap(gb2p, icon_width, icon_height, false);
                BitmapDescriptor greenp_b2 = BitmapDescriptorFactory.fromBitmap(gp2bbit);

                Bitmap bb2p = BitmapFactory.decodeResource(getResources(),R.drawable.build2_blue);
                Bitmap bpb2bit = Bitmap.createScaledBitmap(bb2p, icon_width, icon_height, false);
                BitmapDescriptor bluep_b2 = BitmapDescriptorFactory.fromBitmap(bpb2bit);


                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_coor.get(0)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                desc.setText("Starting " + numplayer + " Players game" +
                        "\n開始" + numplayer + "人遊戲" + "\n\n Initializing map 地圖初始中");


                Handler myHandler = new Handler();
                for (int in=0; in <=pointX.length;in++) {
                    final int[] finalIn = {in};
                    myHandler.postDelayed(() -> {
                        if (finalIn[0] == pointX.length){
                            finalIn[0] = 0;
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_coor.get(finalIn[0])));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        polylineOptions.add(loc_coor.get(finalIn[0]));
                        mMap.addPolyline(polylineOptions);
                    }, 400L * finalIn[0]);
                }

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
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
                }, 6500);

                final Handler outerhandler = new Handler(Looper.getMainLooper());
                outerhandler.postDelayed(() -> {
                    player_seq = getRandomNonRepeatingIntegers(numplayer);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_coor.get(0)));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    if (numplayer == 2){
                        desc.setText("Player sequence 玩家序列: \n" + player_seq.get(0) +" " +
                                player_seq.get(1));

                        p1_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 1"));
                        assert p1_mark != null;
                        p1_mark.setIcon(redp_icon);
                        p2_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 2"));
                        assert p2_mark != null;
                        p2_mark.setIcon(yellowp_icon);
                    }
                    else if(numplayer == 3){
                        desc.setText("Player sequence 玩家序列: \n" + player_seq.get(0) +" " +
                                player_seq.get(1) +" "+ player_seq.get(2));

                        p1_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 1"));
                        assert p1_mark != null;
                        p1_mark.setIcon(redp_icon);
                        p2_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 2"));
                        assert p2_mark != null;
                        p2_mark.setIcon(yellowp_icon);
                        p3_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude - 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 3"));
                        assert p3_mark != null;
                        p3_mark.setIcon(greenp_icon);
                    }
                    else if (numplayer == 4){
                        desc.setText("Player sequence 玩家序列: \n" + player_seq.get(0) +" " +
                                player_seq.get(1) +" "+ player_seq.get(2) +" "+ player_seq.get(3));

                        p1_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 1"));
                        assert p1_mark != null;
                        p1_mark.setIcon(redp_icon);
                        p2_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude + 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 2"));
                        assert p2_mark != null;
                        p2_mark.setIcon(yellowp_icon);
                        p3_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude - 0.01, loc_coor.get(0).longitude - 0.01)).title("Player 3"));
                        assert p3_mark != null;
                        p3_mark.setIcon(greenp_icon);
                        p4_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(0).latitude - 0.01, loc_coor.get(0).longitude + 0.01)).title("Player 4"));
                        assert p4_mark != null;
                        p4_mark.setIcon(bluep_icon);
                    }
                }, 10000);

                final Handler moneyinit = new Handler(Looper.getMainLooper());
                Handler loadmoney= new Handler();
                moneyinit.postDelayed(() -> {
                    for(int pos = 0; pos < numplayer; pos++){
                        player_position[pos] = 0;
                    }
                    if (numplayer == 2){
                        for (int in=0; in <= player_init_money;in++) {
                            int finalIn = in;
                            loadmoney.postDelayed(() -> desc.setText("Initial player money:\n" + "初始玩家錢:\n"
                            + "Player 1: $" + finalIn + "\nPlayer 2: $" + finalIn), (long) in);
                        }
                        player_money[0] = player_init_money;
                        player_money[1] = player_init_money;
                    }
                    else if(numplayer == 3){
                        for (int in=0; in <= player_init_money;in++) {
                            int finalIn = in;
                            loadmoney.postDelayed(() -> desc.setText("Initial player money:\n" + "初始玩家錢:\n"
                                    + "Player 1: $" + finalIn + "\nPlayer 2: $" + finalIn
                                    + "\nPlayer 3: $" + finalIn), (long) in);
                        }
                        player_money[0] = player_init_money;
                        player_money[1] = player_init_money;
                        player_money[2] = player_init_money;
                    }
                    else if (numplayer == 4){
                        for (int in=0; in <= player_init_money;in++) {
                            int finalIn = in;
                            loadmoney.postDelayed(() -> desc.setText("Initial player money:\n" + "初始玩家錢:\n"
                                    + "Player 1: $" + finalIn + "\nPlayer 2: $" + finalIn
                                    + "\nPlayer 3: $" + finalIn + "\nPlayer 4: $" + finalIn), (long) in);
                        }
                        player_money[0] = player_init_money;
                        player_money[1] = player_init_money;
                        player_money[2] = player_init_money;
                        player_money[3] = player_init_money;
                    }
                }, 13000);




                    final Handler OnGameRun = new Handler(Looper.getMainLooper());
                    Handler playertest = new Handler();

                    OnGameRun.postDelayed(() -> {

                           desc.setText("It's player " + player_seq.get(player_now) +" turn!"
                           );
                           roll.setVisibility(View.VISIBLE);



                        roll.setOnClickListener(view -> {
                            if(player_now >= numplayer){
                                player_now = 0;
                            }
                            // player_now - 1

                            roll();

                            final Handler waitdice = new Handler(Looper.getMainLooper());
                            waitdice.postDelayed(() -> {



                      for (int in = player_position[player_seq.get(player_now-1)-1] ; in <= player_position[player_seq.get(player_now-1)-1] + dicenum; in++) {
                        int finalIn = (in) % 15;
                         playertest.postDelayed(() -> {
                             mMap.moveCamera(CameraUpdateFactory.newLatLng(loc_coor.get(finalIn)));
                             mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                             if(player_seq.get(player_now-1) == 1){
                                 p1_mark.remove();
                                 p1_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(finalIn).latitude + 0.01, loc_coor.get(finalIn).longitude - 0.01)).title("Player 1"));
                                 assert p1_mark != null;
                                 p1_mark.setIcon(redp_icon);
                             }
                             else if(player_seq.get(player_now-1) == 2){
                                 p2_mark.remove();
                                 p2_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(finalIn).latitude + 0.01, loc_coor.get(finalIn).longitude + 0.01)).title("Player 2"));
                                 assert p2_mark != null;
                                 p2_mark.setIcon(yellowp_icon);
                             }
                             else if(player_seq.get(player_now-1) == 3){
                                 p3_mark.remove();
                                 p3_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(finalIn).latitude - 0.01, loc_coor.get(finalIn).longitude - 0.01)).title("Player 3"));
                                 assert p3_mark != null;
                                 p3_mark.setIcon(greenp_icon);
                             }
                             else if(player_seq.get(player_now-1) == 4){
                                 p4_mark.remove();
                                 p4_mark = mMap.addMarker(new MarkerOptions().position(new LatLng(loc_coor.get(finalIn).latitude - 0.01, loc_coor.get(finalIn).longitude + 0.01)).title("Player 4"));
                                 assert p4_mark != null;
                                 p4_mark.setIcon(bluep_icon);
                             }
                         }, 1000L * in);

                       }
                                Toast.makeText(getApplicationContext(), "Player " + player_seq.get(player_now - 1) + " On the way to " + cities_name[(player_position[player_seq.get(player_now-1)-1] + dicenum)%15]
                                        + "\n玩家" + player_seq.get(player_now - 1) + "在路上往" + cities_name_ch[(player_position[player_seq.get(player_now-1)-1] + dicenum)%15], Toast.LENGTH_LONG).show();
                                player_position[player_seq.get(player_now-1)-1] = player_position[player_seq.get(player_now-1)-1] + dicenum;
                            }, 2000);

                            final Handler autobuy = new Handler(Looper.getMainLooper());
                            autobuy.postDelayed(() -> {

                                if(place_occ[player_position[player_seq.get(player_now-1)-1]%15][0] == -1){
                                    if(player_seq.get(player_now-1) == 1) {
                                        if(player_money[player_seq.get(player_now-1)-1] < 100){
                                            desc.setText("Player " + player_seq.get(player_now-1) + " Balance Insufficient, can't buy " + cities_name[player_position[player_seq.get(player_now-1)-1]%15]+
                                                    "!\n玩家" + player_seq.get(player_now-1) + "錢不足, 買不到" + cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] + "!");
                                        }
                                        else{
                                            p1_build.add(mMap.addMarker(new MarkerOptions().position(loc_coor.get(player_position[player_seq.get(player_now - 1) - 1]%15)).title("Player 1 Building")));
                                            p1_build.get(p1_build.size() - 1).setIcon(redp_b1);

                                            desc.setText("Player " + player_seq.get(player_now-1) + " purchased " +
                                                    cities_name[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "! \n玩家"+ player_seq.get(player_now-1) + "買了" +
                                                    cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "!\nMoney 錢: -100 = $" + (player_money[player_seq.get(player_now-1)-1] - 100));
                                            player_money[player_seq.get(player_now-1)-1] -= 100;

                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][0] = 1;
                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][1] = player_seq.get(player_now-1);
                                        }
                                    }
                                    else if(player_seq.get(player_now-1) == 2){
                                        if(player_money[player_seq.get(player_now-1)-1] < 100){
                                            desc.setText("Player " + player_seq.get(player_now-1) + " Balance Insufficient, can't buy " + cities_name[player_position[player_seq.get(player_now-1)-1]%15]+
                                                    "!\n玩家" + player_seq.get(player_now-1) + "錢不足, 買不到" + cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] + "!");
                                        }
                                        else{
                                            p2_build.add(mMap.addMarker(new MarkerOptions().position(loc_coor.get(player_position[player_seq.get(player_now - 1) - 1]%15)).title("Player 2 Building")));
                                            p2_build.get(p2_build.size()-1).setIcon(yellowp_b1);

                                            desc.setText("Player " + player_seq.get(player_now-1) + " purchased " +
                                                    cities_name[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "! \n玩家"+ player_seq.get(player_now-1) + "買了" +
                                                    cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "!\nMoney 錢: -100 = $" + (player_money[player_seq.get(player_now-1)-1] - 100));
                                            player_money[player_seq.get(player_now-1)-1] -= 100;

                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][0] = 1;
                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][1] = player_seq.get(player_now-1);
                                        }

                                    }
                                    else if(player_seq.get(player_now-1) == 3){
                                        if(player_money[player_seq.get(player_now-1)-1] < 100){
                                            desc.setText("Player " + player_seq.get(player_now-1) + " Balance Insufficient, can't buy " + cities_name[player_position[player_seq.get(player_now-1)-1]%15]+
                                                    "!\n玩家" + player_seq.get(player_now-1) + "錢不足, 買不到" + cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] + "!");
                                        }
                                        else{
                                            p3_build.add(mMap.addMarker(new MarkerOptions().position(loc_coor.get(player_position[player_seq.get(player_now - 1) - 1]%15)).title("Player 2 Building")));
                                            p3_build.get(p3_build.size()-1).setIcon(greenp_b1);

                                            desc.setText("Player " + player_seq.get(player_now-1) + " purchased " +
                                                    cities_name[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "! \n玩家"+ player_seq.get(player_now-1) + "買了" +
                                                    cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "!\nMoney 錢: -100 = $" + (player_money[player_seq.get(player_now-1)-1] - 100));
                                            player_money[player_seq.get(player_now-1)-1] -= 100;

                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][0] = 1;
                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][1] = player_seq.get(player_now-1);
                                        }
                                    }
                                    else if(player_seq.get(player_now-1) == 4){
                                        if(player_money[player_seq.get(player_now-1)-1] < 100){
                                            desc.setText("Player " + player_seq.get(player_now-1) + " Balance Insufficient, can't buy " + cities_name[player_position[player_seq.get(player_now-1)-1]%15]+
                                                    "!\n玩家" + player_seq.get(player_now-1) + "錢不足, 買不到" + cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] + "!");
                                        }
                                        else{
                                            p4_build.add(mMap.addMarker(new MarkerOptions().position(loc_coor.get(player_position[player_seq.get(player_now - 1) - 1]%15)).title("Player 4 Building")));
                                            p4_build.get(p4_build.size()-1).setIcon(bluep_b1);

                                            desc.setText("Player " + player_seq.get(player_now-1) + " purchased " +
                                                    cities_name[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "! \n玩家"+ player_seq.get(player_now-1) + "買了" +
                                                    cities_name_ch[player_position[player_seq.get(player_now-1)-1]%15] +
                                                    "!\nMoney 錢: -100 = $" + (player_money[player_seq.get(player_now-1)-1] - 100));
                                            player_money[player_seq.get(player_now-1)-1] -= 100;

                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][0] = 1;
                                            place_occ[player_position[player_seq.get(player_now-1)-1]%15][1] = player_seq.get(player_now-1);
                                        }
                                    }
                                }
                                else if(place_occ[player_position[player_seq.get(player_now-1)-1]%15][0] == 1){
                                    if(player_seq.get(player_now-1) == place_occ[player_position[player_seq.get(player_now-1)-1]%15][1]) {
                                        //desc.setText("Player 玩家 " + player_seq.get(player_now-1) +"\n visit own place 參觀自己的地方! \n +$100");
                                        //player_money[player_seq.get(player_now-1)-1] += 100;
                                    }
                                    else{
                                        desc.setText("Player 玩家 " + player_seq.get(player_now-1) + " Trespass侵入 \n Player 玩家" + place_occ[player_position[player_seq.get(player_now-1)-1]%15][1]
                                        + " Place 地方!\nPlayer 玩家" + player_seq.get(player_now-1) +":　-$100 \n Player 玩家"
                                        + place_occ[player_position[player_seq.get(player_now-1)-1]%15][1] + ": +$100");
                                        player_money[player_seq.get(player_now-1)-1] -= 100;
                                        player_money[place_occ[player_position[player_seq.get(player_now-1)-1]%15][1]-1] += 100;

                                        if(player_money[player_seq.get(player_now-1)-1] < 0){

                                            desc.setText("Player 玩家" +player_seq.get(player_now-1) + " Bankrupt 破產!\n Game ended 遊戲結束! \n Player 玩家"
                                                    + rank[0] + " wins 贏!");
                                            wintrue = true;

                                            int n = numplayer;
                                            int temp, temp2;
                                            for(int i=0; i < n; i++) {
                                                for (int j = 1; j < (n - i); j++) {
                                                    if (player_money[j - 1] > player_money[j]) {
                                                        temp = player_money[j - 1];
                                                        temp2 = rank[j-1];
                                                        player_money[j - 1] = player_money[j];
                                                        rank[j-1] = rank[j];
                                                        player_money[j] = temp;
                                                        rank[j] = temp2;
                                                    }

                                                }
                                            }
                                            SGame.stop();
                                        }
                                    }
                                }
                            }, 5000);


                            final Handler en_button = new Handler(Looper.getMainLooper());
                            en_button.postDelayed(() -> {
                                if(!wintrue){
                                    int copy = player_now-1;
                                    copy++;
                                    if(copy > numplayer-1){
                                        copy = 0;
                                    }
                                    desc.setText("It's player " + player_seq.get(copy) +" turn!");
                                    roll.setVisibility(View.VISIBLE);
                                }
                            }, 10000);
                            player_now++;
                        });

                    }, 20000);




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
                .setPositiveButton("Yes 要", (dialog, id) -> {
                    Intent i = new Intent(result.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                })
                .setNegativeButton("No 不要", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();

        alert.setTitle("Do you want back to main menu?");
        alert.show();

    }
}