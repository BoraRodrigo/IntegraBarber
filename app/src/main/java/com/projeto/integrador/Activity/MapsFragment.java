package com.projeto.integrador.Activity;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Model.Barbearia;
import com.projeto.integrador.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;
    private FirebaseAuth autenticacao;

    //Lista de Barbearia Cadastradas
    private List<Barbearia> listaDeBarbearia= new ArrayList<>();
    private DatabaseReference barberiaRef;

    SupportMapFragment mapFragment;


    private OnFragmentInteractionListener mListener;

    public MapsFragment() {

    }

    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        autenticacao= ConfiguracaoFirebase.getAutenticacao();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();

            barberiaRef=ConfiguracaoFirebase.getDatabaseReference().child("barbearia");

            recuperarBarbearia();
        }

        mapFragment.getMapAsync(this);


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);//Altera o tipo de mapa neste caso satelite


        //mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {//Adiciona marcador onde usuario Clicar, este evento é de clique curto pode adicionar um de clique long.
          //  @Override
            //public void onMapClick(LatLng latLng) {
              //  Double latitude= latLng.latitude;//recupera a latiude e a longitude onde o cara cliclou no caso podemos usar para salvar locais no firebase
                //Double longitude=latLng.longitude;
               // mMap.addMarker(new MarkerOptions().position(latLng).title("Local"));
            //}
        //});

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){ // Se a permissão foi autorizada
            // Localização autorizada
            mMap.setMyLocationEnabled(true); //Localização atual do dispositivo
            //map.setTrafficEnabled(true); //Informações de tráfego
        }

        LatLng unifacear = new LatLng(-25.538583, -49.362758);//-25.5385781,-49.3649467,17
        mMap.addMarker(new MarkerOptions().position(unifacear).title("UNIFACEAR"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(unifacear));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(),"Você clicou em "+marker.getTitle(), Toast.LENGTH_LONG).show();
                return false;
            }
        });


        //Recupera a lista de endereços da barbearia e adiona na lista e adicona em marcadores nomapa
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault() );
        try {
            for(int i=0; i<listaDeBarbearia.size(); i++){
                Barbearia barbearia = new Barbearia();
                barbearia=listaDeBarbearia.get(i);
                String stringEndereco=barbearia.getRua()+", "+barbearia.getNumero()+" - "+barbearia.getCidade()+" - PR";

                List<Address> listaEndereco = geocoder.getFromLocationName(stringEndereco,1);
                if( listaEndereco != null && listaEndereco.size() > 0 ){
                    Address endereco = listaEndereco.get(0);

                    Double lat = endereco.getLatitude();
                    Double lon = endereco.getLongitude();
                    
                    LatLng localUsuario = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions().position(localUsuario).title("Barbearia: "+barbearia.getNomebarbearia().toUpperCase() ));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario,16));

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //Lista Barbearias Cadastradas
    public void recuperarBarbearia(){
        barberiaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                        Barbearia barbearia = dados.getValue(Barbearia.class);
                        listaDeBarbearia.add(barbearia);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
