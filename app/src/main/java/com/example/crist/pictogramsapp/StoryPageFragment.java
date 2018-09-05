package com.example.crist.pictogramsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.dynamic.IFragmentWrapper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Esta clase representa al fragmento de cada pagina.
 *
 * @author Cristian Gonzalez Lopez
 */
public class StoryPageFragment extends Fragment{


    private String img_path;
    private String text;
    private List<Pictogram> ITEM_LIST;
    private List<Pictogram> ADAPTER_LIST;
    private Boolean islongpress;
    private int pos;
    private Thread t;
    private grid_image_adapter grid_image_adapter;
    private TextToSpeech textToSpeech;
    private TypeWriter textView;
    private TextView coverText;


    private boolean isVisible;
    private boolean isStarted;
    private boolean isAnimated;
    private boolean speechCreated;
    private boolean iscover;

    /**
     * Nueva instancia de la clase
     *
     * @param img_path la ruta de la imagen de la escena
     * @param text     el texto
     * @param grid     la lista de pictogramas
     * @return el fragmento de la pagina
     */
    public static StoryPageFragment newInstance(String img_path, String text, List<Pictogram> grid){

        StoryPageFragment storyPageFragment = new StoryPageFragment();

        Bundle args = new Bundle();

        args.putString("image", img_path);
        args.putString("text", text);
        args.putParcelableArrayList("grid", (ArrayList<? extends Parcelable>) grid);

        storyPageFragment.setArguments(args);

        return storyPageFragment;

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isAnimated = false;
        islongpress = false;
        iscover = false;


        img_path = getArguments().getString("image");
        text = getArguments().getString("text");
        ITEM_LIST = getArguments().getParcelableArrayList("grid");



        if (img_path != null && ITEM_LIST != null){

            ADAPTER_LIST = new ArrayList<>();

            textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if (i == TextToSpeech.SUCCESS){
                        Locale spanish = new Locale("es", "ES");
                        textToSpeech.setLanguage(spanish);
                        speechCreated = true;
                        isStarted = true;
                        textToSpeech.setSpeechRate(0.8f);

                        if (isVisible && isStarted && !isAnimated) {
                            isAnimated = true;

                            animateGrid();

                        } else {
                            if (ADAPTER_LIST != null && !ADAPTER_LIST.isEmpty()) {
                                ADAPTER_LIST.clear();
                                grid_image_adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }else {
            iscover = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        final View myview;
        if (img_path == null && ITEM_LIST==null){

            myview = inflater.inflate(R.layout.story_cover, container, false);
            coverText = myview.findViewById(R.id.story_cover_txt);
            coverText.setText(text.toUpperCase());
        }else{

            myview = inflater.inflate(R.layout.story_page, container, false);
            final ZoomImageDialog zoomImageDialog = new ZoomImageDialog(getActivity());

            ImageView imageView = myview.findViewById(R.id.story_page_img);
            final Bitmap bitmap = BitmapFactory.decodeFile(img_path);
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            textView = myview.findViewById(R.id.story_page_text);


            GridView gridView = myview.findViewById(R.id.story_page_grid);
            grid_image_adapter = new grid_image_adapter(getActivity(), ADAPTER_LIST, "storyGrid");
            grid_image_adapter.notifyDataSetChanged();
            gridView.setAdapter(grid_image_adapter);


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


        }


        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isVisible && isStarted && !isAnimated && speechCreated) {
            isAnimated = true;
            animateGrid();

        } else {
            if (ADAPTER_LIST != null && !ADAPTER_LIST.isEmpty()) {
                ADAPTER_LIST.clear();
                grid_image_adapter.notifyDataSetChanged();
            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (!iscover) {
            if (t != null && t.isAlive())
                t.interrupt();

            if (isAnimated) {
                ADAPTER_LIST.clear();
                grid_image_adapter.notifyDataSetChanged();
            }

            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }

            isAnimated = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!iscover){
            if (t != null)
                t.interrupt();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!iscover) {
            if (t != null)
                t.interrupt();

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;

        if (!iscover) {

            if (isVisible && isStarted && !isAnimated) {
                animateGrid();

            } else {
                if (ADAPTER_LIST != null && !ADAPTER_LIST.isEmpty()) {
                    ADAPTER_LIST.clear();
                    grid_image_adapter.notifyDataSetChanged();
                }
                if (speechCreated) {
                    if (textToSpeech.isSpeaking()) {
                        textToSpeech.stop();
                    }
                }
                if (textView != null)
                    textView.setText("");
            }
        }
    }

    /**
     * Esta función se encarga de ir añadiendo poco a poco los pictogramas en la paginas y de dictar el texto.
     */
    public void animateGrid(){

        pos = 0;
        t = new Thread(){
            @Override
            public void run() {

                if (!isVisible){
                    return;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (getActivity() == null)
                    return;

                if (!isVisible){
                    isAnimated = false;
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ADAPTER_LIST.add(ITEM_LIST.get(pos));
                        grid_image_adapter.notifyDataSetChanged();
                        pos++;
                        if (pos < ITEM_LIST.size()){
                            t.start();
                        }else{
                            isAnimated = false;

                        }
                    }
                });

            }
        };
        t.start();

        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        textView.setText("");
        textView.setCharacterDelay(75);
        textView.animateText(text);

    }
}
