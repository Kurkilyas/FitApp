package com.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fitapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;


// Kullanıcının vereceği bilgilere göre özel arama eklenece

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public double lat , lng ;

    private GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mapFragment;

    Button findGymButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        findGymButton = findViewById(R.id.findGym);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

    }


    public void findGym(View view){

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + String.valueOf(currentLocation.getLatitude())+","+ currentLocation.getLongitude() + // Bağcılar'ın koordinatları
                "&radius=1000" + // 1 kilometrelik bir yarıçap
                "&type=gym" + // Park türündeki yerler
                "&key=" + getResources().getString(R.string.google_maps_key);

        Object[] dataFetch = new Object[2];
        dataFetch[0]=myMap;
        dataFetch[1]=url;
        System.out.println(Arrays.toString(dataFetch));

        FetchData fetchData = new FetchData();
        fetchData.execute(dataFetch);
    }



    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    updateMap();
                }
            }
        });
    }

    private void updateMap() {
        if (myMap != null && currentLocation != null) {
            LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            myMap.addMarker(new MarkerOptions().position(sydney).title("You Location"));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        getLastLocation(); // Harita hazır olduğunda konum bilgisini al
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
                System.out.print("Başarılı");
            } else {
                Toast.makeText(this, "Location permission is denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}