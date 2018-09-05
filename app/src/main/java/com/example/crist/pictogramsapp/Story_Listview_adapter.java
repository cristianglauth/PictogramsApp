package com.example.crist.pictogramsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Esta clase representa al adapter del listview del apartado de los cuentos
 *
 * @author Cristian Gonzalez Lopez
 */
public class Story_Listview_adapter extends BaseAdapter {


    private List<Story> storyList;
    private Context context;
    private String folder;

    /**
     * Instaciación de un nuevo objeto de la clase.
     *
     * @param context   el contexto de la aplicacion
     * @param storyList la lista de cuentos
     */
    public Story_Listview_adapter(Context context, List<Story> storyList) {

        this.context = context;
        this.storyList = storyList;

        folder = context.getDir("DirName", Context.MODE_PRIVATE).toString()+"/pictograms/";

    }

    @Override
    public int getCount() {
        return storyList.size();
    }

    @Override
    public Story getItem(int i) {
        return storyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.story_list_item, null);

        TextView textView = view.findViewById(R.id.txt_story_listitem);

        Story story = getItem(i);

        if(story !=null) {
            textView.setText(story.getTittle());
        }

        return view;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
