package com.example.crist.pictogramsapp;

import java.io.Serializable;
import java.util.List;

/**
 * Esta clase representa el modelo de los cuentos
 *
 * @author Cristian Gonzalez Lopez
 */
public class Story implements Serializable {


    private String tittle;
    private List<StoryPage> pages;

    /**
     * Instancia un nuevo objeto de la clase.
     *
     * @param tittle el título
     * @param pages  la lista de paginas
     */
    public Story(String tittle, List<StoryPage> pages) {
        this.tittle = tittle;
        this.pages = pages;
    }

    /**
     * Gets título.
     *
     * @return el título
     */
    public String getTittle() {
        return tittle;
    }

    /**
     * Sets título.
     *
     * @param tittle el título
     */
    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    /**
     * Gets lista de paginas.
     *
     * @return la lista de paginas
     */
    public List<StoryPage> getPages() {
        return pages;
    }

    /**
     * Sets lista de paginas.
     *
     * @param pages la lista de paginas
     */
    public void setPages(List<StoryPage> pages) {
        this.pages = pages;
    }
}
