package com.example.crist.pictogramsapp;


import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Esta clase filtra las sugerencias que se mostraran en el autocompletView.
 *
 * @author Cristian Gonzalez Lopez
 */
public class PictosFilter extends android.widget.Filter {

    private PictoAdapter adapter;
    private List<Pictogram> originalList;
    private List<Pictogram> filteredList;
    private Context context;


    /**
     * Instancia un nuevo objeto de la clase.
     *
     * @param adapter      el adaptado
     * @param originalList la lista de pictogramas original
     * @param context      el contexto de la aplicación
     */
    public PictosFilter(PictoAdapter adapter, List<Pictogram> originalList, Context context){
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
        this.context = context;
    }



    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        FilterResults results = new FilterResults();


        if (constraint == null || constraint.length() == 0) {
            filteredList.clear();
        } else {
            DatabaseHelper databaseHelper = new DatabaseHelper(context, Pictogram.TABLE_NAME, null, 0);

            List<Pictogram> templist = databaseHelper.getSearchPictos(constraint.toString());

            String antname = "";
            String antgroup = "";

            for (Pictogram p : templist){

                if(!antname.equals(p.getName().toString())) {
                    filteredList.add(p);
                }else if(!antgroup.equals(p.getGroup().toString())){
                    filteredList.add(p);
                }
                antgroup = p.getGroup();
                antname = p.getName();
            }

        }

        results.values = filteredList;
        results.count = filteredList.size();



        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {
        adapter.filteredPictos.clear();
        adapter.filteredPictos.addAll((List) results.values);
        Collections.sort(adapter.filteredPictos);
        adapter.notifyDataSetChanged();
    }

}
