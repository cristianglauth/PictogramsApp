package com.example.crist.pictogramsapp;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Esta clase representa el objeto tablero.
 *
 * @author Cristian Gonzalez Lopez
 */
public class Board implements Serializable {


    private String item_title;
    private List<Pictogram> item_images;
    private List<Pictogram> resp_pictos_list;
    private int color;


    /**
     * Instancia un nuevo objeto tablero.
     *
     * @param item_title       el título del tablero
     * @param item_images      la lista de pictogramas del título
     * @param resp_pictos_list la lista de pictogramas respuesta
     * @param color            el color
     */
    public Board(String item_title, List<Pictogram> item_images, List<Pictogram> resp_pictos_list, int color){

        this.item_title = item_title;
        this.item_images = item_images;
        this.resp_pictos_list = resp_pictos_list;
        this.color = color;
    }


    /**
     * Gets título.
     *
     * @return el título
     */
    public String getItem_title() {
        return item_title;
    }

    /**
     * Sets título.
     *
     * @param item_title el título
     */
    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    /**
     * Gets la lista de pictogramas del título.
     *
     * @return la lista de pictogramas del título
     */
    public List<Pictogram> getItem_images() {
        return item_images;
    }

    /**
     * Sets la lista de pictogramas del título.
     *
     * @param item_images la lista de pictogramas del título
     */
    public void setItem_images(List<Pictogram> item_images) {
        this.item_images = item_images;
    }

    /**
     * Gets la lista de pictogramas respuesta.
     *
     * @return la lista de pictogramas respuesta
     */
    public List<Pictogram> getResp_pictos_list() {
        return resp_pictos_list;
    }

    /**
     * Sets la lista de pictogramas respuesta.
     *
     * @param resp_pictos_list la lista de pictogramas respuesta
     */
    public void setResp_pictos_list(List<Pictogram> resp_pictos_list) {
        this.resp_pictos_list = resp_pictos_list;
    }

    /**
     * Gets color.
     *
     * @return el color
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color el color
     */
    public void setColor(int color) {
        this.color = color;
    }

}
