package com.aldo.aget.rsadmin.Control;

/**
 * Created by Work on 16/07/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aldo.aget.rsadmin.Vistas.Fragmento;

public class AdaptadosFragmentos extends FragmentPagerAdapter {
    final int PAGE_COUNT = 6;
    private String tabTitles[] =
            new String[]{"Empresas Habilitadas", "Tab Dos", "Tab Tres", "Tab Cuatro", "Tab Cinco", "Tab Seis"};

    public AdaptadosFragmentos(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        switch (position) {
            case 0:
            case 2:
                f = Fragmento.newInstance();
                break;
            case 3:
            case 5:
                f = Fragmento.newInstance();
                break;
            case 1:
            case 4:
                f = Fragmento.newInstance();
                break;
        }

        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
