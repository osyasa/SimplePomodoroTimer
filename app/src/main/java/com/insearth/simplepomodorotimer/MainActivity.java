package com.insearth.simplepomodorotimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    ImageView settingsView;
    Button setbutton;
    Button resetbutton;
    RecyclerView recyclerView;

    CountDownTimer countDownTimer;

    RecyclerViewAdapter recyclerViewAdapter;

    public static int finished = 0;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        settingsView = findViewById(R.id.settingsView);
        setbutton = findViewById(R.id.setbutton);
        resetbutton = findViewById(R.id.resetbutton);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        intent = new Intent(MainActivity.this, ForegroundService.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        countDownTimer.cancel();

        stopService(intent);
    }


    public void set(View view) {

        setbutton.setVisibility(View.INVISIBLE);
        resetbutton.setVisibility(View.VISIBLE);

            countDownTimer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long l) {
                int dk = (int) (l/1000) / 60;
                int sn = (int) (l/1000) % 60;

                String dkStr = Integer.toString(dk);
                String snStr = Integer.toString(sn);

                intent.putExtra("dkStr", dkStr);
                intent.putExtra("snStr", snStr);

                startService(intent);


                if(sn >= 10) {
                    textView.setText(dk + ":" + sn);
                } else {
                    textView.setText(dk + ":0" + sn);
                }
            }


            @Override
            public void onFinish() {

                countDownTimer.cancel();
                stopService(intent);

                finished++;
                recyclerViewAdapter.notifyDataSetChanged();

                setbutton.setVisibility(View.VISIBLE);
                resetbutton.setVisibility(View.INVISIBLE);
                setbutton.setClickable(false);
                setbutton.setText("REST");

                AlertDialog.Builder finishdialog = new AlertDialog.Builder(MainActivity.this);
                finishdialog.setCancelable(false);

                finishdialog.setTitle("TIME UP!");
                if(finished % 4 == 0) {
                    finishdialog.setMessage("Time is up. Now it is time to give 15 minutes break!");
                } else {
                    finishdialog.setMessage("Time is up. Now it is time to give 5 minutes break!");
                }
                finishdialog.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        textView.setText("5:00");

                        countDownTimer = new CountDownTimer(3000, 1000) {
                            @Override
                            public void onTick(long l) {

                                int dk = (int) (l/1000) / 60;
                                int sn = (int) (l/1000) % 60;

                                String dkStr = Integer.toString(dk);
                                String snStr = Integer.toString(sn);

                                intent.putExtra("dkStr", dkStr);
                                intent.putExtra("snStr", snStr);

                                startService(intent);

                                if(sn >= 10) {
                                    textView.setText(dk + ":" + sn);
                                } else {
                                    textView.setText(dk + ":0" + sn);
                                }

                            }

                            @Override
                            public void onFinish() {
                                countDownTimer.cancel();
                                stopService(intent);

                                textView.setText("25:00");
                                setbutton.setClickable(true);
                                setbutton.setText("SET");
                            }
                        }.start();

                    }
                }).setNegativeButton("I'm done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textView.setText("25:00");
                        setbutton.setClickable(true);
                        setbutton.setText("SET");
                        Toast.makeText(MainActivity.this, "You made " + finished + " pomodoro!", Toast.LENGTH_LONG).show();
                        finished = 0;
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        }.start();

    }

    public void reset(View view) {

        stopService(intent);

        countDownTimer.cancel();
        textView.setText("25:00");

        resetbutton.setVisibility(View.INVISIBLE);
        setbutton.setVisibility(View.VISIBLE);

    }


    public void settings(View view) {

        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);

    }





}