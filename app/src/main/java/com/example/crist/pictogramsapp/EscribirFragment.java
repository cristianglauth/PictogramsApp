package com.example.crist.pictogramsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Esta clase representa al fragmento del apartado de escribir pictogramas.
 *
 * @author Cristian Gonzalez Lopez
 */
public class EscribirFragment extends Fragment  {



    private GridView gridView;
    private TextToSpeech textToSpeech;


    private List<Pictogram> ITEM_LIST;
    private grid_image_adapter gridadapter;
    private DatabaseHelper databaseHelper;
    private Boolean islongpress;

    /**
     * Instacia un nuevo fragmento.
     */
    public EscribirFragment(){
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext(), null, null,0);
        islongpress = false;
        ITEM_LIST = new ArrayList<>();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_escribir, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myview = inflater.inflate(R.layout.frag_escribir, null);


        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR){
                    Locale spanish = new Locale("es", "ES");
                    textToSpeech.setLanguage(spanish);
                }
            }
        });

        Bundle bundle = getArguments();


        gridView = myview.findViewById(R.id.gridview);
        gridadapter = new grid_image_adapter(getActivity(), ITEM_LIST, "grid");
        gridadapter.notifyDataSetChanged();
        gridView.setAdapter(gridadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Pictogram gridPic = ITEM_LIST.get(i);

                if (gridPic.getListImages() != null){
                    gridPic.changeImage();
                    ITEM_LIST.set(i, gridPic);
                    gridadapter.notifyDataSetChanged();
                }

                }
        });

        final ZoomImageDialog zoomImageDialog = new ZoomImageDialog(getActivity());

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                islongpress = true;

                Pictogram p = ITEM_LIST.get(i);
                zoomImageDialog.setImage(p);
                zoomImageDialog.dialog.show();
                return true;
            }
        });

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.onTouchEvent(motionEvent);

                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (islongpress){
                        islongpress = false;
                        zoomImageDialog.dialog.dismiss();
                    }
                }
                return false;
            }
        });


        final PictoAdapter pictoAdapter = new PictoAdapter(getActivity().getApplicationContext(), null);
        final AutoCompleteTextView actv = myview.findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);
        actv.setDropDownHeight(500);
        actv.setAdapter(pictoAdapter);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Pictogram ptemp = pictoAdapter.getItem(i);

                String searchKey = ptemp.getPath().replace(".png", "");
                searchKey = searchKey.replace("p_", "");
                searchKey = searchKey.replaceAll("_[0-9]","");

                List<String> listImages = databaseHelper.getImagesPathDB(searchKey);


                if (listImages.size()>=1){
                       listImages.add(0, ptemp.getPath());
                       ptemp.setListImages(listImages);
                }

                ITEM_LIST.add(ptemp);
                gridadapter.notifyDataSetChanged();
                actv.setText("");
            }
        });


        return myview;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (id){

            case R.id.menu_frg_escr_delete:

                if (!ITEM_LIST.isEmpty()) {

                    ITEM_LIST.clear();
                    gridadapter.notifyDataSetChanged();

                    return  true;
                }else{
                    return true;
                }

            case R.id.menu_frg_escr_listen:

                String dir = getActivity().getApplicationContext().getDir("DirName", Context.MODE_PRIVATE).toString();


                if (!ITEM_LIST.isEmpty()) {

                    String frase = "";

                    for(Pictogram p : ITEM_LIST){

                        frase += p.getName()+" ";
                    }

                    if(!textToSpeech.isSpeaking())
                        textToSpeech.speak(frase,TextToSpeech.QUEUE_ADD,null);

                    return  true;

                }else{
                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }

}
