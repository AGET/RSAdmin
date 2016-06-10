package com.aldo.aget.rsadmin.Vistas;

/**
 * Created by Work on 10/06/16.
 */

import android.support.annotation.NonNull;
import android.widget.AbsListView;
import android.widget.ListView;


public class ManejadorScroll implements AbsListView.OnScrollListener {
    private int posicionPreviaScroll;
    private int posicionPreviaElemento;
    private int distanciaMinima;
    private ListView lista;
    private Action accion;

    public interface Action {
        void up();

        void down();
    }

    public ManejadorScroll(@NonNull ListView lista, int distanciaMinima, @NonNull Action accion) {
        this.lista = lista;
        this.distanciaMinima = distanciaMinima;
        this.accion = accion;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //nothing here
    }

    @Override
    public void onScroll(AbsListView view, int primerElementoVisible, int ContadorElementosVisibles, int TotalElementosVisibles) {
        int posicionDesplazamientoActual = posicionDesplazamientoActual();
        if (primerElementoVisible == posicionPreviaElemento) {
            int scrolled = Math.abs(posicionPreviaScroll - posicionDesplazamientoActual);
            if (scrolled > distanciaMinima) {
                if (posicionPreviaScroll > posicionDesplazamientoActual) {
                    accion.up();
                } else {
                    accion.down();
                }
            }
        } else if (primerElementoVisible > posicionPreviaElemento) {
            accion.up();
        } else {
            accion.down();
        }
        posicionPreviaScroll = posicionDesplazamientoActual;
        posicionPreviaElemento = primerElementoVisible;

    }

    private int posicionDesplazamientoActual() {
        int pos = 0;
        if (lista.getChildAt(0) != null) {
            pos = lista.getChildAt(0).getTop();
        }
        return pos;
    }

}
