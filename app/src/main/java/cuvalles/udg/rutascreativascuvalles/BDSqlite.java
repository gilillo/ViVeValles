package cuvalles.udg.rutascreativascuvalles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GILBERTO on 12/04/2016.
 */
public class BDSqlite extends SQLiteOpenHelper{

    public BDSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "app_rutas_creativas.db", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table categorias(ID_categorias integer primary key autoincrement,nombre text not null,ID_lugares integer" +
                " foreign key ID_lugares reference lugares(ID_lugares)) ");
        db.execSQL("create table lugares (ID_lugares integer primary key autoincrement ,nombre text,descripcion text,direccion text,imagen text ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exits categorias");

        db.execSQL("create table categorias(ID_categorias integer primary key autoincrement,nombre text not null,ID_lugares integer" +
                " foreign key ID_lugares reference lugares(ID_lugares)) ");
        db.execSQL("create table lugares (ID_lugares integer primary key autoincrement ,nombre text,descripcion text,direccion text,imagen text ");



    }
}
