package com.aldo.aget.rsadmin.Control;

/**
 * Created by Work on 16/07/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aldo.aget.rsadmin.Vistas.FragmentoEmpresaDeshabilitada;
import com.aldo.aget.rsadmin.Vistas.FragmentoEmpresaHabilitada;
import com.aldo.aget.rsadmin.Vistas.FragmentoGpsLibres;

public class AdaptadorFragmentos extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] =
            new String[]{"Empresas Habilitadas","Empresas Deshabilitadas", "Gps"};


    public AdaptadorFragmentos(FragmentManager fm) {
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
                f = FragmentoEmpresaHabilitada.newInstance();
                break;
            case 1:
                f = FragmentoEmpresaDeshabilitada.newInstance();
                break;
            case 2:
                f = FragmentoGpsLibres.newInstance();
                break;
        }

        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
