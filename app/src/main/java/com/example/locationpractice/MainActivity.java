package com.example.locationpractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 100;
    //Variable Declaration
    //For Views
    Button btnLoc;
    TextView tvLat, tvLon;
    //For Location
    LocationManager manager;
    LocationListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //region View variables are Instantiated
        btnLoc = findViewById(R.id.btn_Location);
        tvLat = findViewById(R.id.tv_lat);
        tvLon = findViewById(R.id.tv_lon);
        //endregion

        //region Location manager and listener set up
        //Location Set up
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("onLocationChanged", "Location Changed in the listener");
                String latText = getString(R.string.latLabel) + location.getLatitude();
                String lonText = getString(R.string.lonLabel) + location.getLongitude();

                tvLat.setText(latText);
                tvLon.setText(lonText);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.e("onProviderDisabled", "Location manager  permission denied");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    Intent settingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(settingsIntent);
                }
            }
        };
        //endregion


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET};
                Log.e("onCreate", "Request permission from users check permission");

                requestPermissions(permissions, LOCATION_REQUEST_CODE);
            }
        }

        configureButton();
    }

    private void configureButton() {
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("configureButton", "Button just Clicked");
                Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "Please consider enabling location.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configureButton();
            }
        }

    }


}
