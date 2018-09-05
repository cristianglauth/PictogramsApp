package com.example.crist.pictogramsapp;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Esta clase es una modificacion de la clase TextView para emular la animación de escritura de una maquina de escribir.
 *
 * @author Cristian Gonzalez Lopez
 */
public class TypeWriter extends android.support.v7.widget.AppCompatTextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 150; // in ms

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param context el contexto de la aplicacion
     */
    public TypeWriter(Context context) {
        super(context);
    }

    /**
     * Instancia una nueva clase
     *
     * @param context el contexto de la aplicacion
     * @param attrs   los atributos
     */
    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable() {

        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));

            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    /**
     * Funcion que ejecuta la animación del texto
     *
     * @param txt the txt
     */
    public void animateText(CharSequence txt) {
        mText = txt;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    /**
     * Sets delay entre caranteres
     *
     * @param m los milisegundos
     */
    public void setCharacterDelay(long m) {
        mDelay = m;
    }
}
