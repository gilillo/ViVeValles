package cuvalles.udg.rutascreativascuvalles;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import cuvalles.udg.rutascreativascuvalles.RecyclerView.Categorias;
import cuvalles.udg.rutascreativascuvalles.dummy.DummyContent;

public class MainActivity extends AppCompatActivity {

    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    TextView TVUsuario;
    String a ="";
public static final String KEY_CATEGORIAS = "KEY CATEGORIAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        a = i.getStringExtra("sesion");
        String Usuario = i.getStringExtra("usuario");

        if(!a.equals("valido") ){

            Intent J = new Intent(MainActivity.this,Login.class);
            startActivity(J);
        }


        appbar = (Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        appbar.setTitle(Usuario);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("VIVE-VALLES");

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        navView = (NavigationView) findViewById(R.id.navview);


        View headerView = navView.getHeaderView(0);
        TextView textView = (TextView)headerView.findViewById(R.id.TxUsuario);
        textView.setText("Bienvenido!! "+ Usuario);
        ImageView imagenHeaderView = (ImageView)headerView.findViewById(R.id.imagenUsuario);
        Glide.with(this).load("http://cache1.asset-cache.net/xc/496155811.jpg?v=2&c=IWSAsset&k=2&d=NAONeuuJhWx4zKV8hNEx7RLlvqi5oYgUELjThEjgbO5CpblwLyBIMv_y8xS1W6RY0")
                .into(imagenHeaderView);



        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                         Bundle categoria = new Bundle();
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.ruta:
                                categoria.putString(KEY_CATEGORIAS,"0");
                                fragment = Mapa.Categorias(categoria);


                                fragmentTransaction = true;
                                break;
                            case R.id.sitios:
                                categoria.putString(KEY_CATEGORIAS,"3");
                                fragment = Mapa.Categorias(categoria);
                                fragmentTransaction = true;
                                break;
                            case R.id.comidas:
                                categoria.putString(KEY_CATEGORIAS,"8");
                                fragment = Mapa.Categorias(categoria);
                                fragmentTransaction = true;
                                break;
                            case R.id.reparacion:
                                categoria.putString(KEY_CATEGORIAS,"9");
                                fragment = Mapa.Categorias(categoria);
                                fragmentTransaction = true;
                                break;
                            case R.id.wc:
                                categoria.putString(KEY_CATEGORIAS,"11");
                                fragment = Mapa.Categorias(categoria);
                                fragmentTransaction = true;
                                break;
                            case R.id.mas_cat:
                                fragment = new ListaRecyclerView();
                                fragmentTransaction = true;
                            break;
                            /*
                            case R.id.estadisticas:

                                break;
                            case R.id.ir_a_web:

                                break;*/


                        }

                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}

