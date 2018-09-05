package com.example.crist.pictogramsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Esta clase representa al adapter de la pagina
 *
 * @author Cristian Gonzalez Lopez
 */
public class StoryPageAdapter extends FragmentPagerAdapter{

    private List<StoryPage> ITEM_LIST;

    /**
     * Instancia un nuevo objeto de la clase.
     *
     * @param fm      el FragmentManager
     * @param items   la lista de paginas
     * @param context el contexto de la aplicacion
     */
    public StoryPageAdapter(FragmentManager fm, List<StoryPage> items, Context context) {
        super(fm);
        this.ITEM_LIST = items;

    }

    @Override
    public Fragment getItem(int position) {

        StoryPage storyPage = ITEM_LIST.get(position);

        return StoryPageFragment.newInstance(storyPage.getImg_path(), storyPage.getText(), storyPage.getGridPictos());
    }

    @Override
    public int getCount() {
        return ITEM_LIST.size();
    }
}
