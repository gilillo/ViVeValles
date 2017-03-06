package cuvalles.udg.rutascreativascuvalles;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.polok.routedrawer.RouteDrawer;
import com.github.polok.routedrawer.RouteRest;
import com.github.polok.routedrawer.model.Routes;
import com.github.polok.routedrawer.model.TravelMode;
import com.github.polok.routedrawer.parser.RouteJsonParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Valles on 29/11/2016.
 */
    public class MapaMarcarRuta extends Fragment {

        private GoogleMap mMap;
        MapView mMapView;
        float Lat, Lon;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.activity_maps, container, false);

            mMapView = (MapView) v.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();
            LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            }
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 0,  locationListener);
            mMap=mMapView.getMap();
            return v;
        }






        public void marcarRuta(Location location, GoogleMap googleMap) {

            Bundle bundle = getArguments();
            Lat= bundle.getFloat("LAT");
            Lon = bundle.getFloat("LON");

            final RouteDrawer routeDrawer = new RouteDrawer.RouteDrawerBuilder(googleMap)
                    .withColor(Color.BLUE)
                    .withWidth(8)
                    .withAlpha(0.5f)
                    .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .build();

            RouteRest routeRest = new RouteRest();

            routeRest.getJsonDirections(new LatLng(location.getLatitude(),location.getLongitude()), new LatLng(Lat, Lon), TravelMode.WALKING)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<String, Routes>() {
                        @Override
                        public Routes call(String s) {
                            return new RouteJsonParser<Routes>().parse(s, Routes.class);
                        }
                    })
                    .subscribe(new Action1<Routes>() {
                        @Override
                        public void call(Routes r) {
                            routeDrawer.drawPath(r);
                        }
                    });

        }


        LocationListener locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {

                location.getLatitude();
                location.getLongitude();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(12).build();
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                marcarRuta(location,mMap);

            }




            public void onStatusChanged(String provider, int status, Bundle extras) {

            }


            public void onProviderEnabled(String provider) {

            }


            public void onProviderDisabled(String provider) {

            }
        };




    }

