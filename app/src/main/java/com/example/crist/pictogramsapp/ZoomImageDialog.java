package com.example.crist.pictogramsapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Esta clase representa a la vista de dialogo donde se muestran los pictogramas aumentados
 *
 * @author Cristian Gonzalez Lopez
 */
public class ZoomImageDialog {


    private FrameLayout frameLayout;
    Dialog dialog;
    private Context context;


    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param context the context
     */
    public ZoomImageDialog(Context context){

        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.zoom_image_dialog);


        frameLayout = dialog.findViewById(R.id.zoom_frame);
    }


    /**
     * Set imagen.
     *
     * @param p el pictograma
     */
    public void setImage(Pictogram p){

        frameLayout.removeAllViews();

        frameLayout.addView(p.getPictoImagen(dialog.getContext(), "zoom"));

    }

    /**
     * Set image.
     *
     * @param foto el bitmap de la imagen
     */
    public void setImage(Bitmap foto){

        frameLayout.removeAllViews();

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(foto, foto.getWidth()*5, foto.getHeight()*5, false));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


        frameLayout.addView(imageView);



    }



}
