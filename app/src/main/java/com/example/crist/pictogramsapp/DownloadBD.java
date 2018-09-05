package com.example.crist.pictogramsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase asincrona descarga la base de datos del servidor y la crea en la memoria local del dispositivo.
 *
 * @author Cristian Gonzalez Lopez
 */
public class DownloadBD extends AsyncTask<DataSnapshot, Integer, String>{

    private Context contextMain;
    private MyProgDialog myProgDialog;

    private int progress = 0;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        myProgDialog = new MyProgDialog(contextMain);
        myProgDialog.titulo.setText("Creando Base de Datos");
        myProgDialog.pbar.setProgress(progress);
        myProgDialog.dialog.show();
    }


    @Override
    protected String doInBackground(DataSnapshot... dataSnapshots) {


        List<ContentValues> cvlist = new ArrayList<>();

        DatabaseHelper databaseHelper = new DatabaseHelper(contextMain, null, null, 0);

        publishProgress((int) dataSnapshots[0].getChildrenCount(), progress);

        String antPicto = "";

        for (DataSnapshot dbReg : dataSnapshots[0].getChildren()){

            if (!dbReg.child("name").equals(antPicto)){


                ContentValues value = new ContentValues();
                value.put(Pictogram.COLUMN_KEY, dbReg.getKey());
                value.put(Pictogram.COLUMN_NAME, dbReg.child("name").getValue(String.class));
                value.put(Pictogram.COLUMN_GROUP, dbReg.child("group").getValue(String.class));
                value.put(Pictogram.COLUMN_PATH, dbReg.child("path").getValue(String.class));

                cvlist.add(value);


                progress++;
                publishProgress(0, progress);

                antPicto = dbReg.child("name").getValue(String.class);
            }
        }

        databaseHelper.insertListReg(cvlist);


        return "Base de datos Lista";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (values[0] > 0){
            myProgDialog.pbar.setMax(values[0]);
        }

        double percent = (100*values[1])/myProgDialog.pbar.getMax();

        myProgDialog.pbar.setProgress(values[1]);
        myProgDialog.txtprogress.setText(percent+"%");

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        myProgDialog.dialog.dismiss();
        Toast.makeText(contextMain, s, Toast.LENGTH_SHORT).show();


        File fileImageBD = new File(contextMain.getDir("DirName", Context.MODE_PRIVATE).toString()+"/pictograms");

        if (fileImageBD.exists()){
            Toast.makeText(contextMain,"Las imagenes ya han sido descargadas", Toast.LENGTH_SHORT).show();
        }else {

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("pictos_boards_stories.zip");
            mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    String zipURL = uri.toString();

                    ImagesDownloadBD imagesDownloadBD = new ImagesDownloadBD();
                    imagesDownloadBD.setContext(contextMain);
                    imagesDownloadBD.execute(zipURL);

                }
            });
        }

    }

    /**
     * Sets contexto de la aplicación.
     *
     * @param contextMain el contexto de la aplicación
     */
    public void setContextMain(Context contextMain) {
        this.contextMain = contextMain;
    }


}
