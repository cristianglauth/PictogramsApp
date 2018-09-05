package com.example.crist.pictogramsapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa al modelo de la pagina
 *
 * @author Cristian Gonzalez Lopez
 */
public class StoryPage implements Serializable{


    private String img_path;
    private String text;
    private List<Pictogram> gridPictos;

    /**
     * Instancia un nuevo objeto de la clase
     *
     * @param img_path   la ruta de la imagen de la escena
     * @param text       el texto
     * @param gridPictos la lista de pictogramas
     */
    public StoryPage(String img_path, String text, List<Pictogram> gridPictos) {
        this.img_path = img_path;
        this.text = text;
        this.gridPictos = gridPictos;
    }

    /**
     * Gets la ruta de la imagen de la escena
     *
     * @return la ruta de la imagen de la escena
     */
    public String getImg_path() {
        return img_path;
    }

    /**
     * Sets la ruta de la imagen de la escena
     *
     * @param img_path la ruta de la imagen de la escena
     */
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    /**
     * Gets el texto.
     *
     * @return el texto
     */
    public String getText() {
        return text;
    }

    /**
     * Sets el texto.
     *
     * @param text el texto
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets la lista de pictogramas
     *
     * @return la lista de pictogramas
     */
    public List<Pictogram> getGridPictos() {
        return gridPictos;
    }

    /**
     * Sets la lista de pictogramas
     *
     * @param gridPictos tla lista de pictogramas
     */
    public void setGridPictos(List<Pictogram> gridPictos) {
        this.gridPictos = gridPictos;
    }
}
