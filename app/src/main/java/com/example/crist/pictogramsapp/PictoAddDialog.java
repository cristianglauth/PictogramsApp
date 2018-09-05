package com.example.crist.pictogramsapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.List;

/**
 * Esta clase representa al controlador de la ventana de dialogo que permite buscar pictogramas
 *
 * @author Cristian Gonzalez Lopez
 */
public class PictoAddDialog {


    /**
     * El dialogo.
     */
    public Dialog dialog;
    private FrameLayout frameLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private PictoAdapter pictoAdapter;
    /**
     * El botón para añadir el pictograma.
     */
    public Button buttAdd;
    private Context context;
    private DatabaseHelper databaseHelper;
    private Pictogram pAdd;
    private Pictogram pFrame;

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param context el contexto de la aplicacion
     */
    public PictoAddDialog(final Context context) {

        this.context = context;
        databaseHelper = new DatabaseHelper(context,null,null,0);

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.boardadd_dialog_addtit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        frameLayout = dialog.findViewById(R.id.BoardAdd_Dia_frame);
        autoCompleteTextView = dialog.findViewById(R.id.BoardAdd_Dia_Autocomp);
        buttAdd = dialog.findViewById(R.id.BoardAdd_Dia_butAdd);



        pictoAdapter = new PictoAdapter(context, null);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setDropDownHeight(500);
        autoCompleteTextView.setAdapter(pictoAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                pAdd = pictoAdapter.getItem(i);

                String searchKey = pAdd.getPath().replace(".png", "");
                searchKey = searchKey.replace("p_", "");
                searchKey = searchKey.replaceAll("_[0-9]","");

                List<String> listImages = databaseHelper.getImagesPathDB(searchKey);


                if (listImages.size()>=1){
                    listImages.add(0, pAdd.getPath());
                    pAdd.setListImages(listImages);
                }

                View frameView = pAdd.getPictoImagen(context,"zoom");

                closeKeyboard();
                autoCompleteTextView.setText("");
                frameLayout.removeAllViews();
                frameLayout.addView(frameView);

            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pAdd != null && pAdd.getListImages() != null){
                    if (pAdd.getIter() == null){
                        pAdd.setIter();
                    }
                    pAdd.changeImage();
                    frameLayout.addView(pAdd.getPictoImagen(context,"zoom"));
                }
            }
        });


    }

    /**
     * Cierra el teclado
     */
    public  void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);

    }

    /**
     * Gets pictograma
     *
     * @return el pictograma
     */
    public Pictogram getpAdd() {
        return pAdd;
    }

}
