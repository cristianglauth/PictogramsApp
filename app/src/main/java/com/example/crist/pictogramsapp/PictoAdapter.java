package com.example.crist.pictogramsapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es el adapter que controla la lista de elementos que aparecen en el autocompleteView
 *
 * @author Cristian Gonzalez Lopez
 */
public class PictoAdapter extends ArrayAdapter<Pictogram> {


    private final List<Pictogram> pictos;
    List<Pictogram> filteredPictos = new ArrayList<>();
    private Context context;


    /**
     * Instancia un nuevo objeto de la clase.
     *
     * @param context el contexto de la aplicación
     * @param pictos  la lista de pictogramas
     */
    public PictoAdapter(Context context, List<Pictogram> pictos) {
        super(context, 0, pictos);
        this.pictos = pictos;
        this.context = context;
    }

    @Override
    public Pictogram getItem(int position) {
        return filteredPictos.get(position);
    }

    @Override
    public int getCount() {
        return filteredPictos.size();
    }

    @Override
    public Filter getFilter() {
        return new PictosFilter(this, pictos, context);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Pictogram picto = filteredPictos.get(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.picto_item,parent,false);

        TextView txtname = (TextView) convertView.findViewById(R.id.textPicto);
        ImageView imgGroup = (ImageView) convertView.findViewById(R.id.imagePicto);
        LinearLayout linPicto = (LinearLayout) convertView.findViewById(R.id.linearPicto);
        // Populate the data into the template view using the data object
        txtname.setText(picto.getName());

        switch (picto.getGroup().toLowerCase()){
            case "descriptivos":
                imgGroup.setImageResource(R.mipmap.ic_des);
                linPicto.setBackgroundColor(Color.rgb(186,225,255));
                break;
            case "miscelaneos":
                imgGroup.setImageResource(R.mipmap.ic_mis);
                break;
            case "nombres":
                imgGroup.setImageResource(R.mipmap.ic_nom);
                linPicto.setBackgroundColor(Color.rgb(255,179,186));
                break;
            case "personas":
                imgGroup.setImageResource(R.mipmap.ic_per);
                linPicto.setBackgroundColor(Color.rgb(255,255,186));
                break;
            case "sociales":
                imgGroup.setImageResource(R.mipmap.ic_soc);
                linPicto.setBackgroundColor(Color.rgb(241,203,255));
                break;
            case "verbos":
                imgGroup.setImageResource(R.mipmap.ic_ver);
                linPicto.setBackgroundColor(Color.rgb(186,255,201));
                break;
        }

        return convertView;
    }

}
