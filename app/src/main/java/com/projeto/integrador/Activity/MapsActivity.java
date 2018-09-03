package com.projeto.integrador.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projeto.integrador.Configuracoes.PermissoesMaps;
import com.projeto.integrador.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);//Altera o tipo de mapa neste caso satelite

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {//Adiciona marcador onde usuario Clicar, este evento é de clique curto pode adicionar um de clique long.
            @Override
            public void onMapClick(LatLng latLng) {
               Double latitude= latLng.latitude;//recupera a latiude e a longitude onde o cara cliclou no caso podemos usar para salvar locais no firebase
                Double longitude=latLng.longitude;
                mMap.addMarker(new MarkerOptions().position(latLng).title("Local"));
            }
        });


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){ // Se a permissão foi autorizada
            // Localização autorizada
            mMap.setMyLocationEnabled(true); //Localização atual do dispositivo
            //map.setTrafficEnabled(true); //Informações de tráfego
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));//Este numero é o zoom que carrega o mapa

        LatLng unifacear = new LatLng(-25.538583, -49.362758);//-25.5385781,-49.3649467,17
        mMap.addMarker(new MarkerOptions().position(unifacear).title("UNIFACEAR"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(unifacear));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),"Você clicou em "+marker.getTitle(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
}
