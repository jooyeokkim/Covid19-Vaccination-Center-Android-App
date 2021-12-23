package com.example.jooye.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private TextView textView;
    private GoogleMap googleMap;
    private boolean finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        textView = findViewById(R.id.textView2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Intent intent = new Intent();
                intent = getIntent();
                float x = intent.getExtras().getFloat("fx", 37.541f);
                float y = intent.getExtras().getFloat("fy", 126.986f);
                textView.setText(x+" "+y+" (네이버 제공 좌표와 구글맵 좌표가 미세하게 달라 마커의 위치가 다를 수 있습니다.");
                LatLng latLng = new LatLng(y, x);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(intent.getExtras().getString("address"));
                googleMap.addMarker(markerOptions).showInfoWindow();
            }
        });
    }
}
