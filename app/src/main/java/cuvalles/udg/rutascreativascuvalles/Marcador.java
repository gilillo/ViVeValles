package cuvalles.udg.rutascreativascuvalles;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.CircularArray;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Valles on 11/05/2016.
 */
public class Marcador extends Fragment implements View.OnClickListener {

TextView nombre,direccion,descripcion,telefono;
  CircularImageView Imagen;
    String NombreMarcador;
    String Nombre,Descripcion,Direccion,Telefono,Fotografia,Lat,Lon;
    Button BotonRuta;
    float LAT,LON;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.activity_marcador, container,
                false);

        nombre = (TextView)v.findViewById(R.id.ETNombre);
        direccion =(TextView)v.findViewById(R.id.ETDireccion);
        descripcion = (TextView)v.findViewById(R.id.ETDesc);
        telefono = (TextView)v.findViewById(R.id.ETtel);

        Imagen = (CircularImageView) v.findViewById(R.id.imagenMarker);
        BotonRuta = (Button)v.findViewById(R.id.btTrazaRuta);
        BotonRuta.setOnClickListener(this);

        Bundle Titulo = getArguments();
        NombreMarcador = Titulo.getString("titulo");

        enviarDatosMarcador();

        telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }else {


                    String NumTel = telefono.getText().toString();
                    Intent Llamar = new Intent(Intent.ACTION_CALL);
                    Llamar.setData(Uri.parse("tel:" + NumTel));
                    startActivity(Llamar);
                }
            }
        });


        return v;

    }


    public void enviarDatosMarcador (){

        String url = "http://148.202.89.87/vias_verdes/marcadores.php?nombre_marcador="+NombreMarcador+"";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Enviando datos", "Espere un momento");
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {



                progressDialog.dismiss();

                for(int i = 0; i<response.length();i++){

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Nombre = jsonObject.getString("nombre");
                        Descripcion= jsonObject.getString("descripcion");
                        Direccion= jsonObject.getString("domicilio");
                        Telefono= jsonObject.getString("telefono");
                        Fotografia= jsonObject.getString("fotografia");
                        Lat=jsonObject.getString("lat");
                        LAT=Float.parseFloat(Lat);
                        Lon = jsonObject.getString("lon");
                        LON = Float.parseFloat(Lon);
                        Fotografia=Fotografia.replace("\\","");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mostrarDatos();





            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               Toast.makeText(getActivity(), "Error de comunicación", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);


    }

    public void mostrarDatos(){
        nombre.setText(Nombre);
        descripcion.setText(Descripcion);
        direccion.setText(Direccion);
        telefono.setText(Telefono);
        Picasso.with(getActivity().getApplicationContext()).load(Fotografia).resize (190,190).into(Imagen);



    }


    @Override
    public void onClick(View v) {
        MapaMarcarRuta mapaRuta = new MapaMarcarRuta();

        Bundle bundle = new Bundle();
        bundle.putFloat("LAT",LAT);
        bundle.putFloat("LON",LON);
        mapaRuta.setArguments(bundle);
        FragmentTransaction CambioFragment = getFragmentManager().beginTransaction();
        CambioFragment.replace(R.id.content_frame,mapaRuta)
                .commit();



    }
}
