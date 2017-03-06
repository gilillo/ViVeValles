package cuvalles.udg.rutascreativascuvalles;

/**
 * Created by Valles on 24/08/2016.
            */
    public class Categoria {
        private String Id,Nombre,Imagen;

    public Categoria (){

    }

    public Categoria (String Id,String Nombre,String Imagen){
        this.Id = Id;
        this.Nombre = Nombre;
        this.Imagen = Imagen;
    }
    public String getId (){
        return Id;
    }

    public void setId (String Id){
        this.Id = Id;

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        this.Imagen = imagen;
    }
}
