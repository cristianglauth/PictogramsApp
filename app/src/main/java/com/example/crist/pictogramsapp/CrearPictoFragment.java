package com.example.crist.pictogramsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Esta clase representa al fragmento donde se crean nuevos pictogramas.
 *
 * @author Cristian Gonzalez Lopez
 */
public class CrearPictoFragment extends Fragment {


    private ImageView imageView;
    private Button butImg;
    private EditText editText;
    private Spinner spinner;
    private String photo_path;
    private String filePath;

    /**
     * Instancia un nuevo fragmento.
     */
    public CrearPictoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_addimage, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (!editText.getText().toString().matches("")){

            if (!checkName(editText.getText().toString())){

                String key = editText.getText().toString();
                String name = editText.getText().toString();
                String path = "p_"+editText.getText().toString()+".png";
                String group = spinner.getSelectedItem().toString().toLowerCase();



                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), null,null,0);
                databaseHelper.insertPictoDB(key,name,group,path);

                filePath = getActivity().getDir("DirName", Context.MODE_PRIVATE).toString()+"/pictograms/"+path;
                crearImagen crearImagen = new crearImagen();
                crearImagen.execute();


            }else {
                return false;
            }

        }else{
            Toast.makeText(getActivity(), "Falta añadir el nombre del pictograma", Toast.LENGTH_SHORT).show();
            return false;
        }


        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.frag_addimage, null);

        imageView = myview.findViewById(R.id.addImage_img);
        butImg = myview.findViewById(R.id.addImg_but_img);
        editText = myview.findViewById(R.id.addImage_nametxt);
        spinner = myview.findViewById(R.id.addImage_spinner);


        butImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] opciones = {"Sacar Foto", "Cargar Imagen", "Cancelar"};
                final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getActivity());
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


        return myview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                }
                break;
            case 20:
                if (resultCode == RESULT_OK) {
                    Uri path = data.getData();
                    imageView.setImageURI(path);
                }
                break;
        }

    }

    /**
     * Esta clase asincrona se encarga de comprimir la imagen
     */
    public class crearImagen extends AsyncTask<Bitmap, String, String>{


        private Bitmap bm;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Creando pictograma...");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(Bitmap... bitmaps) {

            FileOutputStream fout = null;

            try {
                fout = new FileOutputStream(filePath);
                Bitmap bitmap = getResizedBitmap(bm, 500);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fout != null){
                    try {
                        fout.close();
                        progressDialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), "Imagen insertada", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Esta función se encarga de guardar la imagen que se capture con la cámara.
     */
    public void tomarFotografia() {
        String RUTA_IMAGEN = getActivity().getDir("DirName", Context.MODE_PRIVATE).toString();
        String nombreImagen="/tempImage.png";
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();

        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        photo_path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(photo_path);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,10);

    }

    /**
     * Comprueba si el nombre del pictograma existe en la base de datos.
     *
     * @param s EL nombre del pictograma
     * @return Boolean
     */
    public boolean checkName(String s) {

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(),null,null,0);
        Pictogram p = databaseHelper.getPictoDB(s);

        if (p != null){
            editText.getText().clear();
            Toast.makeText(getActivity(), "El pictograma ya existe. Intente con otro nombre.", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Redimensiona la imagen.
     *
     * @param image   La imagen
     * @param maxSize El tamaño máximo
     * @return El bitmap redimensionado
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
}
