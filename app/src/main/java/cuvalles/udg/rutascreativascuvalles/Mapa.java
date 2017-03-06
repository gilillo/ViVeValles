package cuvalles.udg.rutascreativascuvalles;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.polok.routedrawer.RouteDrawer;
import com.github.polok.routedrawer.RouteRest;
import com.github.polok.routedrawer.model.Routes;
import com.github.polok.routedrawer.model.TravelMode;
import com.github.polok.routedrawer.parser.RouteJsonParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.kml.KmlLayer;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
//import com.google.maps.android.kml.KmlContainer;
//import com.google.maps.android.kml.KmlLayer;


/**
 * Created by GILBERTO on 10/04/2016.
 */
public class Mapa extends Fragment implements GoogleMap.OnInfoWindowClickListener {


    MapView mMapView;
   Marker marker ;
    private GoogleMap googleMap;
    private KmlLayer kmlLayer;
    public static final String KEY_CATEGORIAS = "KEY CATEGORIAS";
    String cadena = null, Titulo;

    public static Mapa Categorias(Bundle cat) {
        Mapa mapa_categorias = new Mapa();
        if (cat != null) {
            mapa_categorias.setArguments(cat);
        }

        return mapa_categorias;
    }

    public Mapa() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_maps, container, false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        Bundle bundle = getArguments();
        cadena = bundle.getString(KEY_CATEGORIAS);
        setUpMapIfNeeded();

        return v;
    }

    private void setUpMapIfNeeded() {

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        if (googleMap != null) {
            setUpMap();
            cargarKML();
        }
    }


    private void setUpMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(20.557762, -104.045004)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }else {

            googleMap.setMyLocationEnabled(true);
        }

        String url = "http://148.202.89.87/vias_verdes/ubicaciones.php?id="+cadena;
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                recorreArreglo(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("error volley:" + error.toString(), error.getMessage());
            }
        });
        queue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){


            }
                else {
                Toast.makeText(getActivity().getApplicationContext(),"permiso denegado",Toast.LENGTH_LONG).show();
            }
                return;
        }
    }

    private void recorreArreglo(JSONArray jsonArray){
        for(int i = 0; i<jsonArray.length();i++){

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                addMarcador(Double.parseDouble(jsonObject.getString("lat")),Double.parseDouble(jsonObject.getString("lon")),jsonObject.getString("nombre"),jsonObject.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void addMarcador( double lat, double lon,  String titulo,String id){

        marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(titulo).snippet(id));
        marker.showInfoWindow();

        googleMap.setOnInfoWindowClickListener( this);





    }

    private void cargarKML() {
        try {kmlLayer = new KmlLayer(googleMap, R.raw.doc, getActivity().getApplicationContext());
            kmlLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    


    @Override
    public void onInfoWindowClick(Marker marker) {
        Marcador FragmentMarcador = new Marcador();
        Bundle NombreMarker = new Bundle();
       Titulo= marker.getSnippet();
        NombreMarker.putString("titulo",Titulo);
        FragmentMarcador.setArguments(NombreMarker);
        FragmentTransaction CambioFragment = getFragmentManager().beginTransaction();
                CambioFragment.replace(R.id.content_frame, FragmentMarcador)
                .commit();



    }
}




