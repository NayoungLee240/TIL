package com.young.ws201020;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ThirdFragment extends Fragment {


    MapView mapView;

    public ThirdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = null;
        viewGroup = (ViewGroup) inflater.inflate(
                R.layout.fragment_third, container, false
        );
        mapView = (MapView) viewGroup.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                LatLng latLng = new LatLng(37.4556574, 126.4367135);
                googleMap.addMarker(
                        new MarkerOptions().position(latLng).title("공항").snippet("xxx")
                );
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        });


//        myLocation = new MyLocation();
//        locationManager = (LocationManager) m.getSystemService(Context.LOCATION_SERVICE);
//
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                1,//대기시간
//                0,//데이터를 받을 최소 움직인거리
//                myLocation
//        );

        return viewGroup;
    }

//    class MyLocation implements LocationListener {
//
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            double lat = location.getLatitude();
//            double lon = location.getLongitude();
//            TextView textView = m.findViewById(R.id.textView);
//            textView.setText(lat+" "+lon);
//            LatLng latLng = new LatLng(lat, lon);
//            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//        }
//    }


}