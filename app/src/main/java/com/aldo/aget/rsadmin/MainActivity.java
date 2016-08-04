package com.aldo.aget.rsadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Control.AdaptadorFragmentos;
import com.aldo.aget.rsadmin.Vistas.FragmentoEmpresaDeshabilitada;
import com.aldo.aget.rsadmin.Vistas.FragmentoEmpresaHabilitada;
import com.aldo.aget.rsadmin.Vistas.FragmentoGpsLibres;
import com.aldo.aget.rsadmin.Vistas.ListaEmpresa;
import com.aldo.aget.rsadmin.Vistas.ListaGps;

import java.util.ArrayList;
import java.util.Locale;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnDispositivos, btnEmpresas, btnReconocerVoz;

    //Sistetizador
    private static int TTS_DATA_CHECK = 1;
    private TextToSpeech tts = null;
    private boolean ttsIsInit = false;
    //FinSintetizador

    ViewPager viewPager;
    AdaptadorFragmentos adaptadorViewPager = new AdaptadorFragmentos(
            getSupportFragmentManager());

    private CollapsingToolbarLayout ctlLayout;

    public static FloatingActionButton fabmain;
    ImageView imgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuracion.context = this;
        agregarToolbar();
        setTitle("");

        imgBar = (ImageView) findViewById(R.id.imgToolbar);
        fabmain = (FloatingActionButton) findViewById(R.id.fabmain);
        fabmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, " OBR", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                obtener();
//                test();
                switch ((String) adaptadorViewPager.getPageTitle(viewPager.getCurrentItem())) {
                    case "Empresas Habilitadas":
                        FragmentoEmpresaHabilitada.actividadEmpresa(null);
                        break;
                    case "Gps Disponibles":
                        Toast.makeText(MainActivity.this, "tres", Toast.LENGTH_SHORT).show();
                        FragmentoGpsLibres.actividadGps(null);
                        break;
                    case "Tab Cuatro":
                        Toast.makeText(MainActivity.this, "Cuatro", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

//        btnDispositivos = (Button) findViewById(R.id.btndispositivos);
//        btnDispositivos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actividadListaGps();
//            }
//        });
//        btnEmpresas = (Button) findViewById(R.id.btnempresas);
//        btnEmpresas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actividadListaEmpresa();
//            }
//        });
//        btnReconocerVoz = (Button) findViewById(R.id.btn_reconocer_voz);

        //Tabs + ViewPager

        //Establecer el PageAdapter del componente ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adaptadorViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position % 2 == 0)
                    imgBar.setImageResource(R.drawable.img_gps);
                else
                    imgBar.setImageResource(R.drawable.img_mapa);

                switch ( (String) adaptadorViewPager.getPageTitle(position)){

                    case "Empresas Habilitadas":
                        Log.v("AGET-CargadoEmpHab",""+FragmentoEmpresaHabilitada.cargadoHab);
                        if(!FragmentoEmpresaHabilitada.cargadoHab)
                            //FragmentoEmpresaHabilitada.cargar();

                        break;
                    case "Empresas Deshabilitadas":
                        Log.v("AGET-CargadoEmpDes",""+ FragmentoEmpresaDeshabilitada.cargadoED);
                        if(!FragmentoEmpresaDeshabilitada.cargadoED)
                            FragmentoEmpresaDeshabilitada.cargar();
                        break;
                    case "Gps":
                        Log.v("AGET-CargadoGps",""+FragmentoGpsLibres.cargadoGPS);
                        if(!FragmentoGpsLibres.cargadoGPS)
                            FragmentoGpsLibres.cargar();
                        break;
                }

            }
        });

        initTextToSpeech();

        //CollapsingToolbarLayout
        //ctlLayout = (CollapsingToolbarLayout)findViewById(R.id.ctlLayout);
        //ctlLayout.setTitle("Mi Aplicación");
    }

    private void initTextToSpeech() {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, TTS_DATA_CHECK);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_DATA_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            ttsIsInit = true;
                            //Locale loc = new Locale("es","","");
                            Locale loc = Locale.getDefault();

                            if (tts.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
                                tts.setLanguage(loc);
                            } else {
                                Log.v("AGET-SPEAK", "error en localizacion");
                            }
                            tts.setPitch(0.9f);
                            tts.setSpeechRate(1.9f);
                            speak();
                        }
                    }
                });
            }
        }
        if (requestCode == Activity.RESULT_OK && data != null) {
            Log.v("AGET-RECONOCERVOZ", "succefull");
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this, text.get(0), Toast.LENGTH_LONG).show();
        }
    }

    private void speak() {
        if (tts != null && ttsIsInit) {
            tts.speak("Hi, ", TextToSpeech.QUEUE_ADD, null);
        } else {
            Log.v("AGET-SPEAK", "error");
        }
    }

    //Fin sintetizador
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


    void actividadListaGps() {
        Intent actividad = new Intent(this, ListaGps.class);
        startActivity(actividad);
    }

    void actividadListaEmpresa() {
        Intent actividad = new Intent(this, ListaEmpresa.class);
        startActivity(actividad);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}