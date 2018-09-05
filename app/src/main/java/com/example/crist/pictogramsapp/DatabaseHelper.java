package com.example.crist.pictogramsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa al controlador de la base de datos.
 *
 * @author Cristian Gonzalez Lopez
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DABATASE_VERSION = 1;
    /**
     * La constante DATABASE_NAME.
     */
    public static final String DATABASE_NAME = "pictos_db";
    private Context context;


    /**
     * Instancia un nuevo DatabaseHelper.
     *
     * @param context el contexto de la aplicación
     * @param name    el nombre de la base de datos
     * @param factory la factoria
     * @param version la version
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DABATASE_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(Pictogram.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Pictogram.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }


    /**
     * Inserta un pictograma en la base de datos.
     *
     * @param key   la key
     * @param name  el nombre
     * @param group el grupo
     * @param path  la ruta de la imagen
     */
    public void insertPictoDB (String key, String name, String group, String path){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(Pictogram.COLUMN_KEY, key);
        value.put(Pictogram.COLUMN_NAME, name);
        value.put(Pictogram.COLUMN_GROUP, group);
        value.put(Pictogram.COLUMN_PATH, path);

        db.insert(Pictogram.TABLE_NAME, null, value);

        db.close();

    }

    /**
     * Inserta una lista de pictogramas en la base de datos.
     *
     * @param list la lista de pictogramas
     */
    public void insertListReg(List<ContentValues> list){

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        for (ContentValues cv : list){

            db.insert(Pictogram.TABLE_NAME, null, cv);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

    }

    /**
     * Devuelve un pictograma de la base de datos buscando por la key del pictograma.
     *
     * @param key la key del pictograma
     * @return el pictograma
     */
    public Pictogram getPictoDB (String key){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+Pictogram.TABLE_NAME+" WHERE "+Pictogram.COLUMN_KEY+"='"+key+"' ORDER BY "+Pictogram.COLUMN_KEY + " ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){


            Pictogram picto = new Pictogram(
                    cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_KEY)),
                    cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_GROUP)),
                    cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_PATH)),null);

            cursor.close();

            return picto;
        }else{
            return null;
        }
    }

    /**
     * Devuelve todos los pictogramas de la base de datos.
     *
     * @return la lista de pictogramas
     */
    public List<Pictogram> getAllPictosBD (){

        List<Pictogram> pictoList = new ArrayList<>();

        String selectQuery = "SELECT * FROM "+Pictogram.TABLE_NAME+" ORDER BY "+Pictogram.COLUMN_KEY + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {

                Pictogram picto = new Pictogram(
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_KEY)),
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_GROUP)),
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_PATH)),
                        null);

                pictoList.add(picto);

            } while(cursor.moveToNext());
        }

        db.close();

        return pictoList;
    }


    /**
     * Devuelve una lista de pictogramas cuyo key empiece por el texto intrducido.
     *
     * @param pattern el texto introducido en el cuadro de texto por el usuario
     * @return la lista de pictogramas
     */
    public List<Pictogram> getSearchPictos (String pattern){

        List<Pictogram> pictoList = new ArrayList<>();
        pattern = pattern.replace(" ", "_");

        String selectQuery = "SELECT * FROM "+Pictogram.TABLE_NAME+" WHERE "+Pictogram.COLUMN_KEY+" GLOB '"+pattern.trim()+"*' ORDER BY "+Pictogram.COLUMN_KEY + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {

                Pictogram picto = new Pictogram(
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_KEY)),
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_GROUP)),
                        cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_PATH)),
                        null);

                pictoList.add(picto);

            } while(cursor.moveToNext());
        }

        db.close();

        return pictoList;
    }


    /**
     * Devuelve las rutas de las imagenes de los pictogramas que tienen el mismo nombre.
     *
     * @param key la key del pictograma
     * @return la lista de rutas de imagenes
     */
    public List<String> getImagesPathDB(String key){


        List<String> imageList = new ArrayList<>();


        String selectQuery = "SELECT * FROM "+Pictogram.TABLE_NAME+" WHERE TRIM("+Pictogram.COLUMN_KEY+") GLOB '"+key.trim()+"_[0-9]'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToFirst()){
            do {

                imageList.add(cursor.getString(cursor.getColumnIndex(Pictogram.COLUMN_PATH)));

            }while (cursor.moveToNext());
        }

        return imageList;

    }


    /**
     * Devuelve la cantidad de pictogramas que hay en la base de datos.
     *
     * @return la cantidad de pictogramas
     */
    public int getPictosCount() {
        String countQuery = "SELECT  * FROM " + Pictogram.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }






}
