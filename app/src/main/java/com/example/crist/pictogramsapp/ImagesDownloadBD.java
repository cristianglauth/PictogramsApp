package com.example.crist.pictogramsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Esta clase descarga las imagenes de los pictogramas del servidor y los crea en la memoria del dispositivo.
 *
 * @author Cristian Gonzalez Lopez
 */
public class ImagesDownloadBD extends AsyncTask<String, String, String>{


    private Context contextMain;
    private MyProgDialog myProgDialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myProgDialog = new MyProgDialog(contextMain);

        myProgDialog.pbar.setMax(100);
        myProgDialog.pbar.setProgress(0);
        myProgDialog.titulo.setText("Cargando Pictogramas...");
        myProgDialog.dialog.show();

    }

    @Override
    protected String doInBackground(String... urls) {

        int count;

        try {
            URL url = new URL(urls[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            int lenghtOfFile = connection.getContentLength();

            String filename = "/images_stories_boards.zip";
            //String newDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            String newDir = contextMain.getDir("DirName", Context.MODE_PRIVATE).toString();

            InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

            OutputStream outputStream = new FileOutputStream(newDir+filename);

            byte data[] = new byte[1024];
            long total = 0;

            while ((count = inputStream.read(data)) != -1) {

                total += count;

                publishProgress((""+(int) ((total * 100) / lenghtOfFile)),"descargando");

                outputStream.write(data, 0, count);

            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            publishProgress("0", "extraer");
            String unzipLocation = newDir+"/";



            Decompress d = new Decompress(newDir+filename, unzipLocation);
            d.unzip();


            File file = new File(newDir+filename);
            file.delete();

            return "Pictogramas Listos";

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Falla por malformed URL";
        } catch (IOException e) {
            e.printStackTrace();
            return "Falla por IO";
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);


        if (values[1].equals("extraer")){

            myProgDialog.titulo.setText("Extrayendo Imagenes...");
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            p.weight = 0;
            myProgDialog.pbar.setLayoutParams(p);
            myProgDialog.txtprogress.setLayoutParams(p);
        }else {

            myProgDialog.pbar.setProgress(Integer.parseInt(values[0]));
            myProgDialog.txtprogress.setText(values[0] + "%");
        }
    }

    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);

        Toast.makeText(contextMain,message,Toast.LENGTH_SHORT).show();


        myProgDialog.dialog.dismiss();
    }


    /**
     * Gets contexto de la aplicacion.
     *
     * @return el contexto de la aplicacion
     */
    public Context getContext() {
        return contextMain;
    }

    /**
     * Sets contexto de la aplicacion.
     *
     * @param context el contexto de la aplicacion
     */
    public void setContext(Context context) {
        this.contextMain = context;
    }


}
