package cuvalles.udg.rutascreativascuvalles;

        import android.app.ActionBar;
        import  android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.Toast;
        import android.widget.Toolbar;

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

/**
 * Created by Valles on 24/04/2016.
 */
public class Registro extends AppCompatActivity implements View.OnClickListener{

    EditText nombre,apellido,alias,pass,email,residencia,edad,estatura,peso;
    String Nombre,Apellido,Alias,Pass,Email,Cp,Edad,Estatura,Peso;
    ImageButton registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actyivity_registro);

        nombre = (EditText)findViewById(R.id.editTextNombre);
        apellido =(EditText)findViewById(R.id.editTextApellido);
        alias = (EditText)findViewById(R.id.editTextAlias);
        pass = (EditText)findViewById(R.id.editTextPass);
        email = (EditText)findViewById(R.id.editTextEmail);
        residencia = (EditText)findViewById(R.id.editTextResidencia);
        edad = (EditText)findViewById(R.id.editTextEdad);
        estatura = (EditText)findViewById(R.id.editTextEsta);
        peso = (EditText)findViewById(R.id.editTextPeso);

        registrar = (ImageButton) findViewById(R.id.btnRegistrar);
        registrar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRegistrar:
                validarCampos();
                if(validarCampos()){



                    enviarRegistro();}
                else {
                    Toast.makeText(this,"ERROR campos vacios",Toast.LENGTH_LONG).show();

                }
                break;
            default:
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                break;

        }
    }

    private void enviarRegistro(){

        Nombre = nombre.getText().toString();
        Apellido = apellido.getText().toString();
        Alias = alias.getText().toString();
        Pass = pass.getText().toString();
        Email = email.getText().toString();
        Cp = residencia.getText().toString();
        Edad = edad.getText().toString();
        Estatura = estatura.getText().toString();
        Peso=peso.getText().toString();

       // 148.202.89.87

        String url = "http://148.202.89.87/vias_verdes/registro.php?nombre="+Nombre+"&apellido="+Apellido+"&alias="+Alias+"&pass="+Pass
                +"&email="+Email+"&cp="+Cp+"&edad="+Edad+"&estatura="+Estatura+"&peso="+Peso;
        url = url.replaceAll(" ","%20");
        


        RequestQueue queue = Volley.newRequestQueue(this);
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Registrando Usuario", "Espere un momento");
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                String Respuesta;
                Toast mensaje = Toast.makeText(Registro.this,response.toString(),Toast.LENGTH_LONG);

                mensaje.show();

                progressDialog.dismiss();
                JSONObject obj = null;
                try {
                    obj = response.getJSONObject(0);
                    Respuesta = obj.getString("respuesta");
                    if(Respuesta.equals("REGISTRADO")){
                        Intent i = new Intent(Registro.this, Login.class);
                        finish();
                        startActivity(i);
                    }else if (Respuesta.equals("ERROR")){
                        Toast.makeText(Registro.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR",Respuesta);

                    }
                    else if (Respuesta.equals("vacio")){
                        Toast.makeText(Registro.this, "Existen campos vacios", Toast.LENGTH_SHORT).show();

                    }
                    else if (Respuesta.equals("alias_usado")){
                        Toast.makeText(Registro.this, "Ese alias ya esta en uso", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(Registro.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);



    }

    public boolean validarCampos(){

        if(nombre.length()==0){
            return false;
        }
        if(apellido.length()==0){
            return false;
        }
        if(alias.length()==0){
            return false;
        }
        if(pass.length()==0){
            return false;
        }
        if(email.length()==0){
            return false;
        }
        if(residencia.length()==0){
            return false;
        }
        if(estatura.length()==0){
            return false;
        }
        if(edad.length()==0){
            return false;
        }
        if(peso.length()==0){
            return false;
        }
        return true;
    }
}
