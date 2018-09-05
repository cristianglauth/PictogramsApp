package com.example.crist.pictogramsapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.List;

/**
 * Esta clase representa el controlador de los gridView.
 *
 * @author Cristian Gonzalez Lopez
 */
public class grid_image_adapter extends BaseAdapter {


    private Context mContext;
    private String typeView;


    private List<Pictogram> mThumbIds;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    /**
     * Instancia un nuevo objeto de la clase.
     *
     * @param mContext  el contexto de la aplicacion
     * @param mThumbIds la lista de pictogramas
     * @param typeView  el tipo de vista que tendran los pictogramas
     */
    public grid_image_adapter(Context mContext, List<Pictogram> mThumbIds, String typeView) {
        this.mContext = mContext;
        this.mThumbIds = mThumbIds;
        this.typeView = typeView;
    }

    @Override
    public int getCount() {
        return mThumbIds.size();
    }

    @Override
    public Pictogram getItem(int i) {
        return mThumbIds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View gridview;

        gridview = mThumbIds.get(i).getPictoImagen(mContext, typeView);


        return gridview;


    }
}
