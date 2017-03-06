package cuvalles.udg.rutascreativascuvalles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valles on 24/08/2016.
 */
public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Categoria> ListaCategorias;
    Context context;
    private View.OnClickListener listener;


    public CategoriasAdapter (List<Categoria> ListaCategorias){
        this.ListaCategorias = ListaCategorias;
    }
    public String getIdCategoria(ArrayList<Categoria> categorias,int position){
        String id = categorias.get(position).getId();
        return id;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item,parent,false);
        context = parent.getContext();
        itemView.setOnClickListener(this);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Categoria categoria = ListaCategorias.get(position);
        holder.Contenido.setText(categoria.getId() + ": " + categoria.getNombre());
       Glide.with(context).load(categoria.getImagen()).into(holder.Imagen);
    }

public void setOnClickListener(View.OnClickListener listener){

    this.listener = listener;
}
    @Override
    public void onClick(View v) {
if (listener != null) {
    listener.onClick(v);


}

    }


    @Override
    public int getItemCount() {

        return ListaCategorias.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Contenido;
        public CircularImageView Imagen;

        public MyViewHolder(View itemView) {
            super(itemView);
            Contenido = (TextView) itemView.findViewById(R.id.id_item_lista);
            Imagen = (CircularImageView)itemView.findViewById(R.id.imagen_item);

        }
    }


}

