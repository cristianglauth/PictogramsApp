package com.example.crist.pictogramsapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa al controlador de los tableros de comunicación.
 *
 * @author Cristian Gonzalez Lopez
 */
public class BoardLayout extends AppCompatActivity{


    private List<Pictogram> ITEM_LIST;
    private Boolean islongpress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.board_layout);

        LinearLayout linearLayout = findViewById(R.id.board_title_layout);

        islongpress = false;

        Board b = (Board) getIntent().getSerializableExtra("boardVP");

        ConstraintLayout parent_layout = findViewById(R.id.board_layout_parent);
        parent_layout.setBackgroundColor(b.getColor());



        for (Pictogram p : b.getItem_images()){

            linearLayout.addView(p.getPictoImagen(this,"boardtittle"));

        }

        ITEM_LIST = new ArrayList<>();
        ITEM_LIST.addAll(b.getResp_pictos_list());
        GridView gridView = findViewById(R.id.board_grid);
        final grid_image_adapter grid_image_adapter = new grid_image_adapter(this, ITEM_LIST, "boardLayoutGrid");
        grid_image_adapter.notifyDataSetChanged();
        gridView.setAdapter(grid_image_adapter);

        final ZoomImageDialog zoomImageDialog = new ZoomImageDialog(this);

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
}
