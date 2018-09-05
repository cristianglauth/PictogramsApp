package com.example.crist.pictogramsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Esta clase representa el modelo del pictograma
 *
 * @author Cristian Gonzalez Lopez
 */
public class Pictogram implements Comparable<Pictogram> , Parcelable, Serializable{


    /**
     * La constante TABLE_NAME.
     */
    public static final String TABLE_NAME = "pictos";
    /**
     * La constante COLUMN_ID.
     */
    public static final String COLUMN_ID = "id";
    /**
     * La constante COLUMN_KEY.
     */
    public static final String COLUMN_KEY = "keyname";
    /**
     * La constante COLUMN_NAME.
     */
    public static final String COLUMN_NAME = "name";
    /**
     * La constante COLUMN_GROUP.
     */
    public static final String COLUMN_GROUP = "grupo";
    /**
     * La constante COLUMN_PATH.
     */
    public static final String COLUMN_PATH = "path";


    private int id;
    private String key;
    private String name;
    private String group;
    private String path;
    private List<String> listImages;
    private Iterator<String> iter;


    /**
     * La constante CREATE_TABLE.
     */
// Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_KEY + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_GROUP + " TEXT,"
                    + COLUMN_PATH + " TEXT"
                    + ")";


    /**
     * Instacia un nuevo objeto de la clase
     */
    public Pictogram(){

    }

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param key        la key
     * @param name       el nombre
     * @param group      el grupo
     * @param path       la ruta de la imagen
     * @param listImages la lista de las rutas del resto de imagenes
     */
    public Pictogram(String key, String name, String group, String path, List<String> listImages){
        this.key = key;
        this.name = name;
        this.group = group;
        this.path = path;
        this.listImages = listImages;
    }

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param in Parcel
     */
    public Pictogram(Parcel in){
        id = in.readInt();
        key = in.readString();
        name = in.readString();
        group = in.readString();
        path = in.readString();
        in.readStringList(getListImages());

        if (getListImages() != null)
            iter = getListImages().iterator();

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets key.
     *
     * @return la key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key la key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets nombre.
     *
     * @return el name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets nombre.
     *
     * @param name el name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets grupo.
     *
     * @return el grupo
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets grupo.
     *
     * @param group el grupo
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Gets ruta de la imagen.
     *
     * @return la ruta de la imagen
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets ruta de la imagen.
     *
     * @param path la ruta de la imagen
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets la lista de las rutas del resto de imagenes.
     *
     * @return la lista de las rutas del resto de imagenes
     */
    public List<String> getListImages() {
        return listImages;
    }

    /**
     * Sets la lista de las rutas del resto de imagenes.
     *
     * @param listImages la lista de las rutas del resto de imagenes
     */
    public void setListImages(List<String> listImages) {
        this.listImages = listImages;
        this.iter = listImages.iterator();
    }

    /**
     * Sets iterator de las imagenes.
     */
    public void setIter() {
        this.iter = getListImages().iterator();
    }

    /**
     * Sets iterator de las imagenes.
     *
     * @param iter el iterator de la imagenes
     */
    public void setIter(Iterator<String> iter) {
        this.iter = iter;
    }

    /**
     * Gets iterator de la imagenes.
     *
     * @return el iterator
     */
    public Iterator<String> getIter() {
        return iter;
    }

    @Override
    public int compareTo(@NonNull Pictogram other) {
        return name.toLowerCase().compareTo(other.name.toLowerCase());
    }

    /**
     * Cambiar la imagen utilizando el iterator.
     */
    public void changeImage(){

        if (iter.hasNext()){
            this.path = iter.next();
        }else{
            this.iter = listImages.iterator();
            this.path = iter.next();
        }



    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(key);
        parcel.writeString(group);
        parcel.writeString(path);
        parcel.writeStringList(listImages);

    }


    /**
     * La constante CREATOR.
     */
    public static final Parcelable.Creator<Pictogram> CREATOR = new Parcelable.Creator<Pictogram>() {

        public Pictogram createFromParcel(Parcel in) {
            return new Pictogram(in);
        }

        public Pictogram[] newArray(int size) {
            return new Pictogram[size];
        }
    };


    /**
     * Get vista del pictograma.
     *
     * @param context el contexto de la aplicación
     * @param tipo    el tipo de pictograma
     * @return la vista
     */
    public View getPictoImagen(Context context, String tipo){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.grid_item, null);


        String folder = context.getDir("DirName", Context.MODE_PRIVATE).toString()+"/pictograms/";
        TextView text = view.findViewById(R.id.grid__item_text);
        ImageView img = view.findViewById(R.id.grid__item_img);

        Bitmap bitmap = BitmapFactory.decodeFile(folder+path);
        img.setImageBitmap(bitmap);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);

        text.setText(name.toUpperCase());

        GradientDrawable drawable = (GradientDrawable)view.getBackground();
        switch (group){
            case "descriptivos":
                drawable.setStroke(10, Color.BLUE);
                break;
            case "personas":
                drawable.setStroke(10, Color.YELLOW);
                break;
            case "nombres":
                drawable.setStroke(10, Color.rgb(255,140, 0));
                break;
            case "miscelaneos":
                drawable.setStroke(10, Color.WHITE);
                break;
            case "sociales":
                drawable.setStroke(10, Color.rgb(229,26,197));
                break;
            case "verbos":
                drawable.setStroke(10, Color.GREEN);
                break;
        }

        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = 0;

        switch (tipo){
            case "grid":

                pixels = (int) (140 * scale + 0.5f);
                view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, pixels));
                break;

            case "zoom":

                pixels = (int) (275 * scale + 0.5f);

                view.setLayoutParams(new FrameLayout.LayoutParams(pixels, pixels));
                text.setTextSize(25);
                break;

            case "boardtittle":

                pixels = (int) (75 * scale + 0.5f);
                view.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels));
                text.setVisibility(View.GONE);
                view.setBackgroundColor(Color.parseColor("#cfd8dc"));
                break;

            case "boardLayoutGrid":

                pixels = (int) (130 * scale + 0.5f);
                view.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels));
                text.setTextSize(10);
                break;

            case "gridTit":
                pixels = (int) (80 * scale + 0.5f);
                view.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels));
                text.setTextSize(8);
                break;

            case "listItem":
                pixels = (int) (50 * scale + 0.5f);
                view.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels));
                text.setVisibility(View.GONE);
                break;

            case "storyGrid":

                pixels = (int) (60 * scale + 0.5f);
                view.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels));
                text.setTextSize(8);
                break;
        }

        return  view;
    }



}
