package com.example.crist.pictogramsapp;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Esta clase representa al cuadro de dialogo que se utliza para mostrar el progreso de descarga en el proceso de crear la base de datos
 *
 * @author Cristian Gonzalez Lopez
 */
public class MyProgDialog {

    /**
     * El dialogo
     */
    Dialog dialog;
    ProgressBar pbar;
    ProgressBar pCircle;
    TextView titulo;
    TextView txtprogress;

    /**
     * Instancia un nuevo objeto de la clase.
     *
     * @param ctx el contexto de la aplicación
     */
    public MyProgDialog(Context ctx){

        dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progdialog);

        pbar = dialog.findViewById(R.id.pdiaBar);
        titulo = dialog.findViewById(R.id.pdiaTitulo);
        txtprogress = dialog.findViewById(R.id.pdiaTextProg);
        pCircle = dialog.findViewById(R.id.pdiaCircle);

    }

}
