package com.example.matheus_silva_rodrigues_calculo_percurso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import java.util.Observer;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity{
    private int check;
    private Button permicao;
    private Button ativar;
    private Button desativar;
    private Button iniciar;
    private Button finalizar;
    private EditText textDistancia;
    private Chronometer simpleChronometer;


    private ImageButton btnEncontrar;
    private double latitude;
    private double longitude;
    private double latitude2;
    private double longitude2;
    private TextInputEditText encontar;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int REQUEST_PERMISSION_CODE_GPS = 1001;
    private boolean iniciado = false;
    private Chronometer chronometer;
    private String s;
    private float tempo;


    public interface Observer{

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE_GPS) {
            if (grantResults.length > 0 &&
                    grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
                    check = 1;

                }
            } else {
                Toast.makeText(
                        this,
                        getString(R.string.Permissão),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permicao = findViewById(R.id.buttonpermissionGPS);
        ativar = findViewById(R.id.buttonenableGPS);
        desativar = findViewById(R.id.buttonDisableGPS);
        iniciar = findViewById(R.id.button_startRote);
        finalizar = findViewById(R.id.buttonFinishRote);
        textDistancia = findViewById(R.id.editTextdistancia);
        simpleChronometer = (Chronometer)findViewById(R.id.simpleChronometer);
        encontar = findViewById(R.id.Find);
        btnEncontrar = findViewById(R.id.button7);


        locationManager = (LocationManager)
                getSystemService(MainActivity.this.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(s == null) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    s = getString(
                            R.string.lat_long,
                            latitude,
                            longitude);

                }else{
                    latitude2 = location.getLatitude();
                    longitude2 = location.getLongitude();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        permicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                try {

                    if (ActivityCompat.checkSelfPermission(
                            MainActivity.this,
                                ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED){
                                Toast.makeText(MainActivity.this,R.string.permissaoconcedida,Toast.LENGTH_SHORT);


                    } else {
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{
                                        ACCESS_FINE_LOCATION
                                },
                                REQUEST_PERMISSION_CODE_GPS);


                    }
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"erro",Toast.LENGTH_SHORT);


                }

            }


        });

        ativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!GPSEnabled && checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }else {
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.gps2),
                        Toast.LENGTH_SHORT).show();
            }

            }
        });

        desativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (GPSEnabled && checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 1);

                }else {
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.gps1),
                        Toast.LENGTH_SHORT).show();
            }
            }
        });






        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (GPSEnabled && checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                //LOCALIZAÇÃO
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            2000,
                            2,
                            locationListener);
                    iniciado = true;
                    tempo = 0;
                    encontar.setText("");
                    latitude2 = 0;
                    longitude2 = 0;

                //CHRONOMETER
                    simpleChronometer.setBase(SystemClock.elapsedRealtime());
                    simpleChronometer.start();

                    simpleChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {
                            simpleChronometer.setBase(SystemClock.elapsedRealtime());
                            simpleChronometer.setText((int) SystemClock.elapsedRealtime());
                        }
                    });








                                }else {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.gps1),
                            Toast.LENGTH_SHORT).show();

                }
            }
            });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iniciado){
                    float[] results = new float[1];
                    Location.distanceBetween(latitude, longitude, latitude2, longitude2, results);
                    float distance = results[0];
                    DecimalFormat df = new DecimalFormat("#,###.00");
                    df.format(distance);
                    s = getString(
                            R.string.results,
                            distance);
                    textDistancia.setText(s);
                    latitude = 0;
                    longitude = 0;
                    latitude2 = 0;
                    longitude2= 0;
                    s = null;
                    simpleChronometer.stop();





                }else{
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.Iniciado),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnEncontrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.
                        parse(getString
                                (R.string.uri_mapa,
                                        latitude2,
                                        longitude2,
                                        encontar.getText()));
                Intent intent
                        = new Intent (Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });



        }
    }




