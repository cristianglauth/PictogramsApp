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
 * Esta clase representa al adapter del listview del apartado de tableros.
 *
 * @author Cristian Gonzalez Lopez
 */
public class Board_Listview_adapter extends BaseAdapter {


    private List<Board> boardList;
    private Context context;
    private String folder;

    /**
     * Instancia un nuevo adapter del listview del apartado tablero.
     *
     * @param context   el contexto de la aplicación
     * @param boardList la lista de tableros
     */
    public Board_Listview_adapter(Context context, List<Board> boardList) {

        this.context = context;
        this.boardList = boardList;

        folder = context.getDir("DirName", Context.MODE_PRIVATE).toString()+"/pictograms/";

    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Board getItem(int i) {
        return boardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.board_list_item, null);

        TextView textView = view.findViewById(R.id.txt_board_listitem);
        LinearLayout linearLayout = view.findViewById(R.id.layout_title_pics);

        Board b = getItem(i);

        if(b !=null) {

            textView.setText(b.getItem_title());

            for (Pictogram p : b.getItem_images()) {

                linearLayout.addView(p.getPictoImagen(context, "listItem"));
            }
        }

        return view;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
