package com.example.crist.pictogramsapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Controlador de los gridview que se usan en las actividades de crear tablero y cuento.
 *
 * @author Cristian Gonzalez Lopez
 */
public class MyGridView  extends GridView {

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param context el contexto
     * @param attrs   los atributos
     */
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param context el contexto
     */
    public MyGridView(Context context) {
        super(context);
    }

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param context el contexto
     * @param attrs    los atributos
     * @param defStyle el estilo
     */
    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}