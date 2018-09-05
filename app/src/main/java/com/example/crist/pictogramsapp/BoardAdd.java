package com.example.crist.pictogramsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Esta clase representa a la actividad de crear un nuevo tablero de comunicación
 *
 * @author Cristian Gonzalez Lopez
 */
public class BoardAdd extends AppCompatActivity {


    private int DEFAULT_COLOR;
    private FrameLayout colorFrame;
    private EditText tittleText;
    private GridView gridTit;
    private grid_image_adapter gridAdap_Tit;
    private GridView gridResp;
    private grid_image_adapter getGridAdap_Resp;
    private Button buttonTit;
    private Button buttResp;
    private Button buttcolor;
    private Button buttVisPrev;
    private PictoAddDialog pictoAddDialog;
    private PictoAddDialog pictoAddDialog2;
    private List<Pictogram> ITEM_GRID_TITTLE;
    private List<Pictogram> ITEM_GRID_RESP;
    private Board board;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.board_add_layout);
        getSupportActionBar().setTitle("Crear Tablero");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        buttcolor = findViewById(R.id.ColorBut);
        colorFrame = findViewById(R.id.board_add_colorFrame);
        tittleText = findViewById(R.id.boardAdd_Titulo);
        gridTit = findViewById(R.id.boardAdd_gridTit);
        buttonTit = findViewById(R.id.boardAdd_butGridTit);
        gridResp = findViewById(R.id.boardAdd_gridResp);
        buttResp = findViewById(R.id.boardAdd_butGridResp);
        buttVisPrev = findViewById(R.id.boardAdd_butVisPrev);

        ITEM_GRID_TITTLE = new ArrayList<>();
        gridAdap_Tit = new grid_image_adapter(this, ITEM_GRID_TITTLE,"gridTit");
        gridAdap_Tit.notifyDataSetChanged();
        gridTit.setAdapter(gridAdap_Tit);
        gridTit.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ITEM_GRID_TITTLE.remove(i);
                gridAdap_Tit.notifyDataSetChanged();

                return true;
            }
        });


        pictoAddDialog = new PictoAddDialog(this);
        buttonTit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pictoAddDialog.dialog.show();
            }
        });

        pictoAddDialog.buttAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictoAddDialog.getpAdd() != null) {
                    Pictogram p = pictoAddDialog.getpAdd();
                    p.setIter(null);
                    ITEM_GRID_TITTLE.add(p);
                    pictoAddDialog.dialog.dismiss();
                    gridAdap_Tit.notifyDataSetChanged();
                }
            }
        });



        ITEM_GRID_RESP = new ArrayList<>();
        getGridAdap_Resp = new grid_image_adapter(this,ITEM_GRID_RESP, "gridTit");
        getGridAdap_Resp.notifyDataSetChanged();
        gridResp.setAdapter(getGridAdap_Resp);
        gridResp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                ITEM_GRID_RESP.remove(i);
                getGridAdap_Resp.notifyDataSetChanged();
                return true;
            }
        });

        pictoAddDialog2 = new PictoAddDialog(this);
        buttResp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pictoAddDialog2.dialog.show();
            }
        });

        pictoAddDialog2.buttAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictoAddDialog2.getpAdd() != null) {
                    Pictogram p = pictoAddDialog2.getpAdd();
                    p.setIter(null);
                    ITEM_GRID_RESP.add(p);
                    pictoAddDialog2.dialog.dismiss();
                    getGridAdap_Resp.notifyDataSetChanged();
                }
            }
        });

        DEFAULT_COLOR = ((ColorDrawable) colorFrame.getBackground()).getColor();
        buttcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenColorPicker(false);
                colorFrame.setBackgroundColor(DEFAULT_COLOR);
            }
        });

        buttVisPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tittleText.getText().toString().matches("")){

                    board = new Board(tittleText.getText().toString().toUpperCase(), ITEM_GRID_TITTLE, ITEM_GRID_RESP, DEFAULT_COLOR);

                    Intent intent = new Intent(BoardAdd.this, BoardLayout.class);
                    intent.putExtra("boardVP", board);
                    startActivity(intent);
                }else{
                    Toast.makeText(BoardAdd.this,"Falta rellenar el campo del titulo", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Está seguro que desea salir? Se perderan los cambios realizados")
                .setCancelable(true)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(BoardAdd.this, MainActivity.class);
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_boardadd, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_boardOk) {

            if (!tittleText.getText().toString().matches("")) {

                board = new Board(tittleText.getText().toString().toUpperCase(), ITEM_GRID_TITTLE, ITEM_GRID_RESP, DEFAULT_COLOR);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String tempJSON = gson.toJson(board);

                Intent intent = new Intent(BoardAdd.this, MainActivity.class);
                intent.putExtra("gson", tempJSON);
                setResult(Activity.RESULT_OK, intent);
                finish();

            } else {
                Toast.makeText(BoardAdd.this, "Falta rellenar el campo del titulo", Toast.LENGTH_SHORT).show();
            }
        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Abre la ventana de dialogo para seleccionar un color.
     *
     * @param AlphaSupoort soportar alpha
     */
    public void OpenColorPicker(boolean AlphaSupoort){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, DEFAULT_COLOR, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                DEFAULT_COLOR = color;
                colorFrame.setBackgroundColor(color);
            }
        });
        ambilWarnaDialog.show();

    }
}
