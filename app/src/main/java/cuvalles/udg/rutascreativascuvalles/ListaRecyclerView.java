package cuvalles.udg.rutascreativascuvalles;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Valles on 23/08/2016.
 */
public class ListaRecyclerView extends Fragment {
    private final  ArrayList<Categoria> ListaCategorias = new ArrayList<>();
    private RecyclerView recyclerView;
    private CategoriasAdapter mAdapter;
    public static final String KEY_CATEGORIAS = "KEY CATEGORIAS";



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list,container,false);

        recyclerView =(RecyclerView) v.findViewById(R.id.list);
        mAdapter = new CategoriasAdapter(ListaCategorias);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        descargaDatos();

        mAdapter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildLayoutPosition(v);
                String id =mAdapter.getIdCategoria(ListaCategorias,position);
                Mapa mapa = new Mapa();
                Bundle Id = new Bundle();
                Id.putString(KEY_CATEGORIAS,id);
                mapa.setArguments(Id);
                FragmentTransaction CambioFragment = getFragmentManager().beginTransaction();
                CambioFragment.replace(R.id.content_frame,mapa)
                        .commit();
            }
        });




        return v;
    }




    private void descargaDatos() {

        String url = "http://148.202.89.87/vias_verdes/categorias.php";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Enviando datos", "Espere un momento");
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressDialog.dismiss();
                for(int i = 0; i<response.length();i++){

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                       Categoria categoria = new Categoria(jsonObject.getString("idcategoria"),
                                jsonObject.getString("nombre_cat"),jsonObject.getString("imagen"));
                        ListaCategorias.add(categoria);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                mAdapter.notifyDataSetChanged();


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

}
