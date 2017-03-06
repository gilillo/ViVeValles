package cuvalles.udg.rutascreativascuvalles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static android.support.design.widget.Snackbar.*;

/**
 * Created by Valles on 24/04/2016.
 */
public class Login extends Activity implements View.OnClickListener {
    EditText usuario, contra;
    ImageButton login, registro;
    String Usuario;
    private static final String TAG_SUCCESS = "respuesta";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.editTextUsuario);
        contra = (EditText) findViewById(R.id.editTextContra);

        login = (ImageButton) findViewById(R.id.btnLogin);
        registro = (ImageButton) findViewById(R.id.btnRegistro);

        login.setOnClickListener(this);
        registro.setOnClickListener(this);
        usuario.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:
                enviarLogin();
                break;
            case R.id.btnRegistro:
                Intent i = new Intent(Login.this, Registro.class);

                finish();
                startActivity(i);
                break;
            default:
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                break;

        }

    }




    private void  enviarLogin() {


        Usuario = usuario.getText().toString();
        String Contra = contra.getText().toString();
        List  parametros = new ArrayList();

        parametros.add(Usuario );
        parametros.add(Contra );

    String url = "http://148.202.89.87/vias_verdes/login.php?user="+Usuario+"&pwd="+Contra+"";

    RequestQueue queue = Volley.newRequestQueue(this);
    final ProgressDialog progressDialog = ProgressDialog.show(this, "Enviando datos", "Espere un momento");
    JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            String Respuesta =""  ;

            progressDialog.dismiss();


                try {

                    JSONObject obj = response.getJSONObject(0);
                    Respuesta = obj.getString("respuesta");

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            if (Respuesta.equals("valido")) {

                Intent i = new Intent(Login.this, MainActivity.class);
                i.putExtra("sesion","valido");
                i.putExtra("usuario",Usuario);
                finish();
                startActivity(i);
            }else if(Respuesta.equals("vacios")){

                Toast.makeText(Login.this, "DATOS VACIOS", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(Login.this, "DATOS ERRONEOS", Toast.LENGTH_LONG).show();

            }
            }




    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            progressDialog.dismiss();

            Toast.makeText(Login.this, "Error de comunicación", Toast.LENGTH_LONG).show();

        }
    });
    queue.add(request);


}


}

