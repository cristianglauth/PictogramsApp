package com.example.crist.pictogramsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa a la actividad de crear un cuento
 *
 * @author Critian Gonzalez Lopez
 */
public class StoryAdd extends AppCompatActivity {


    private EditText edit_title;
    private String title;

    private Spinner spinner;
    private Button addPage_but;
    private Button delPages_but;
    private List<StoryPage> PAGES_LIST;
    private List<String> PAGES_NAMES;
    private ArrayAdapter<String> spinnerAdapter;

    private ImageView imageView;
    private Button addImage;
    private List<Bitmap> IMAGES_LIST;


    private EditText edit_texto;
    private String texto;

    private GridView grid;
    private grid_image_adapter grid_image_adapter;
    private Button addGrid_but;
    private List<Pictogram> PICTO_LIST;
    private PictoAddDialog pictoAddDialog;

    private Button vistaPrev_but;

    private int antPag;

    private String path;
    private String filePath;
    private Bitmap imageDefault;

    private Boolean isfirsttime;
    private Boolean isItemDeleted;

    private StoryPage PAGE;
    private String temp_img_path;




    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Crear cuento");

        isfirsttime = true;
        isItemDeleted = false;
        temp_img_path = "";
        //filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/stories";
        filePath = getApplicationContext().getDir("DirName", Context.MODE_PRIVATE).toString()+"/stories";

        PAGES_LIST = new ArrayList<>();
        PAGES_NAMES = new ArrayList<>();
        PICTO_LIST = new ArrayList<>();
        IMAGES_LIST = new ArrayList<>();

        imageDefault = BitmapFactory.decodeResource(this.getResources(),R.drawable.no_imagen);

        edit_title = findViewById(R.id.story_add_edit_title);
        spinner = findViewById(R.id.story_add_spinner);
        addPage_but = findViewById(R.id.story_add_addButPag);
        delPages_but = findViewById(R.id.story_add_delButPag);
        imageView = findViewById(R.id.story_add_image);
        addImage = findViewById(R.id.story_add_addImage);
        edit_texto = findViewById(R.id.story_add_edit_texto);


        PAGE = new StoryPage("","",new ArrayList<Pictogram>());

        grid = findViewById(R.id.story_add_grid);
        grid_image_adapter = new grid_image_adapter(StoryAdd.this, PICTO_LIST, "storyGrid");
        grid_image_adapter.notifyDataSetChanged();
        grid.setAdapter(grid_image_adapter);

        addGrid_but = findViewById(R.id.story_add_addBut_grid);
        vistaPrev_but = findViewById(R.id.story_add_vistaPrevBut);




        PAGES_LIST.add(PAGE);
        PAGES_NAMES.add("Página "+PAGES_LIST.size());
        IMAGES_LIST.add(imageDefault);
        spinnerAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, PAGES_NAMES);
        spinner.setAdapter(spinnerAdapter);

        //SPINER
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!isfirsttime) {

                    if (!isItemDeleted) {
                        guadarPagina();
                    }
                    isItemDeleted = false;
                    actulizarCampos(PAGES_LIST.get(i));
                }else{
                    isfirsttime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //BOTON ADD SPINNER
        addPage_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoryPage storyPage1 = new StoryPage("", "", new ArrayList<Pictogram>());
                PAGES_LIST.add(spinner.getSelectedItemPosition()+1,storyPage1);
                PAGES_NAMES.add("Página "+(spinner.getSelectedItemPosition()+2));
                IMAGES_LIST.add(spinner.getSelectedItemPosition()+1,imageDefault);

                for (int i = 0; i < PAGES_NAMES.size(); i++) {
                    PAGES_NAMES.set(i, "Página " + (i + 1));
                }
                spinnerAdapter.notifyDataSetChanged();
                spinner.setSelection(spinner.getSelectedItemPosition()+1);


            }
        });

        //BOTON DEL SPINNER
        delPages_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int delPag = spinner.getSelectedItemPosition();
                isItemDeleted = true;

                if (PAGES_LIST.size() > 1) {
                    PAGES_LIST.remove(spinner.getSelectedItemPosition());
                    PAGES_NAMES.remove(spinner.getSelectedItemPosition());
                    IMAGES_LIST.remove(spinner.getSelectedItemPosition());

                    for (int i = 0; i < PAGES_NAMES.size(); i++) {
                        PAGES_NAMES.set(i, "Página " + (i + 1));
                    }
                    spinnerAdapter.notifyDataSetChanged();
                    spinner.setSelection(delPag-1);

                    Toast.makeText(StoryAdd.this, "La página "+(delPag+1)+" ha sido eliminada",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(StoryAdd.this, "El cuento tiene que tener al menos una página", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] opciones = {"Sacar Foto", "Cargar Imagen", "Cancelar"};
                final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(StoryAdd.this);
                alertOpciones.setTitle("Seleccione una opción");
                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){

                            case 0:
                                tomarFotografia();

                                break;
                            case 1:
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/");
                                startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),20);

                                break;
                            case 2:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                });

                alertOpciones.show();
            }
        });


        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                PICTO_LIST.remove(i);
                grid_image_adapter.notifyDataSetChanged();
                return true;
            }
        });

        pictoAddDialog = new PictoAddDialog(StoryAdd.this);

        addGrid_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictoAddDialog.dialog.show();
            }
        });

        pictoAddDialog.buttAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictoAddDialog.getpAdd() != null){
                    Pictogram p = new Pictogram();
                    p = pictoAddDialog.getpAdd();
                    p.setIter(null);
                    PICTO_LIST.add(p);
                    grid_image_adapter.notifyDataSetChanged();
                    pictoAddDialog.dialog.cancel();
                }
            }
        });

        vistaPrev_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guadarPagina();

                if (edit_title.getText().toString().equals("")){
                    Toast.makeText(StoryAdd.this, "Falta el título del cuento", Toast.LENGTH_SHORT).show();
                }else{

                    if (checkPages()){

                        File file = new File(filePath+"/"+edit_title.getText().toString());
                        if (!file.exists()){
                            file.mkdir();
                        }

                        Story story = new Story(edit_title.getText().toString(), PAGES_LIST);
                        crearImagen crearImagen = new crearImagen();
                        crearImagen.execute(PAGES_LIST);

                        Intent intent = new Intent(StoryAdd.this, StoryLayout.class);
                        intent.putExtra("story", story);
                        startActivity(intent);
                    }
                }


            }
        });

    }

    /**
     * Con esta función se antualizan los campos de cada página.
     *
     * @param storyPage la pagina
     */
    public void actulizarCampos(StoryPage storyPage) {

        temp_img_path = "";

        edit_texto.setText(storyPage.getText());

        if (!IMAGES_LIST.isEmpty()) {
            imageView.setImageBitmap(IMAGES_LIST.get(spinner.getSelectedItemPosition()));
        }

        if (!storyPage.getGridPictos().isEmpty()) {
            PICTO_LIST.clear();
            PICTO_LIST.addAll(storyPage.getGridPictos());
        }else{
            PICTO_LIST.clear();
        }
        grid_image_adapter.notifyDataSetChanged();

        antPag = spinner.getSelectedItemPosition();
    }

    /**
     * Guarda los valores de los campos de la pagina.
     */
    public void guadarPagina(){

        StoryPage p = PAGES_LIST.get(antPag);

        p.setText(edit_texto.getText().toString());

        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        IMAGES_LIST.set(antPag, bm);

        if (p.getImg_path().equals("")){
            p.setImg_path(filePath+"/"+edit_title.getText().toString()+"/images/"+temp_img_path);
        }else{
            if (!temp_img_path.equals("")){
                p.setImg_path(filePath+"/"+edit_title.getText().toString()+"/images/"+temp_img_path);
            }
        }


        List<Pictogram> templist = new ArrayList<>();
        templist.addAll(PICTO_LIST);
        p.setGridPictos(templist);


        PAGES_LIST.set(antPag, p);


    }


    private void tomarFotografia() {
        String RUTA_IMAGEN = getDir("DirName", Context.MODE_PRIVATE).toString();
        String nombreImagen="/tempImage.png";
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();

        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,10);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    temp_img_path = "escenaP"+spinner.getSelectedItemPosition()+".png";
                }
                break;
            case 20:
                if (resultCode == RESULT_OK) {
                    Uri path = data.getData();
                    imageView.setImageURI(path);
                    temp_img_path = "escenaP"+spinner.getSelectedItemPosition()+".png";
                }
                break;
        }

    }


    /**
     * Clase asincrona que comprime las imagenes
     */
    public class crearImagen extends AsyncTask<List<StoryPage>, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(List<StoryPage>... lists) {



            for (int i = 0; i< lists[0].size(); i++) {

                File file = new File(filePath+"/"+edit_title.getText().toString()+"/images");
                FileOutputStream fout = null;

                try {

                    if (!file.exists()){
                        file.mkdir();
                    }
                    fout = new FileOutputStream(lists[0].get(i).getImg_path(), false);
                    Bitmap bitmap = getResizedBitmap(IMAGES_LIST.get(i), 500);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
                } catch (FileNotFoundException e) {
                    Log.e("errorimg", e.toString());
                    return "file not found";
                } catch (IOException e) {
                    Log.e("errorimg", e.toString());
                    return "io exception";
                }finally {
                    if (fout != null){
                        try {
                            fout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            return "Bien";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }

    /**
     * Redimensiona el bitmap de entrada
     *
     * @param image   la imagen como bitmap
     * @param maxSize el tamaño maximo
     * @return el bitmap redimensionado
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addstory_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_addStoryOK) {

            guadarPagina();

            if (edit_title.getText().toString().equals("")) {
                Toast.makeText(StoryAdd.this, "Falta el título del cuento", Toast.LENGTH_SHORT).show();
            } else {

                if (checkPages()) {

                    File file = new File(filePath + "/" + edit_title.getText().toString());
                    if (!file.exists()) {
                        file.mkdir();
                    }

                    crearImagen crearImagen = new crearImagen();
                    crearImagen.execute(PAGES_LIST);

                    Story story = new Story(edit_title.getText().toString(), PAGES_LIST);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String tempJson = gson.toJson(story);

                    Intent intent = new Intent(StoryAdd.this, MainActivity.class);
                    intent.putExtra("gson", tempJson);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    //Toast.makeText(StoryAdd.this,"Cuento Añadido", Toast.LENGTH_SHORT).show();
                }
            }
        }



        return super.onOptionsItemSelected(item);


    }

    /**
     * Compureba que todos los campos de cada pagina estan completos
     *
     * @return el boolean
     */
    public boolean checkPages(){


        for (int i=0; i<PAGES_LIST.size(); i++){

            StoryPage s = PAGES_LIST.get(i);

            if (s.getText().equals("")){
                Toast.makeText(StoryAdd.this,"Falta texto de la página "+(i+1), Toast.LENGTH_SHORT).show();
                spinner.setSelection(i);
                return false;
            }

            if (s.getImg_path().equals("")){
                Toast.makeText(StoryAdd.this,"Falta imagen de la página "+(i+1), Toast.LENGTH_SHORT).show();
                spinner.setSelection(i);
                return false;
            }

            if (s.getGridPictos().isEmpty()){
                Toast.makeText(StoryAdd.this,"Faltan pictos de la página "+(i+1), Toast.LENGTH_SHORT).show();
                spinner.setSelection(i);
                return false;
            }

        }

        return  true;

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
                        Intent intent = new Intent(StoryAdd.this, MainActivity.class);
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeDirectory(filePath+"/"+edit_title.getText().toString());
                        dialogInterface.cancel();
                    }
                }).show();
    }

    /**
     * Elimina el directorio del cuento
     *
     * @param path la ruta del cuento
     * @return el boolean
     */
    public static boolean removeDirectory(String path) {

        File directory = new File(path);

        if (directory == null)
            return false;
        if (!directory.exists())
            return true;
        if (!directory.isDirectory())
            return false;

        String[] list = directory.list();

        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                if (entry.isDirectory())
                {
                    if (!removeDirectory(entry.toString()))
                        return false;
                }
                else
                {
                    if (!entry.delete())
                        return false;
                }
            }
        }

        return directory.delete();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
