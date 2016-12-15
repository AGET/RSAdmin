package com.aldo.aget.rsadmin;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Control.AdaptadorFragmentos;
import com.aldo.aget.rsadmin.Modelo.ManagerDB;
import com.aldo.aget.rsadmin.Vistas.DialogoAgregarGpsDep;
import com.aldo.aget.rsadmin.Vistas.DialogoConfirmacion;
import com.aldo.aget.rsadmin.Vistas.FragmentoEmpresaDeshabilitada;
import com.aldo.aget.rsadmin.Vistas.FragmentoEmpresaHabilitada;
import com.aldo.aget.rsadmin.Vistas.FragmentoGpsLibres;
import com.aldo.aget.rsadmin.Vistas.GestionAdministrador;
import com.aldo.aget.rsadmin.Vistas.ListaEmpresa;
import com.aldo.aget.rsadmin.Vistas.ListaGps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import android.widget.Toast;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements DialogoConfirmacion.OnConfirmacionDialogListener {
    Button btnDispositivos, btnEmpresas, btnReconocerVoz;

    MenuItem menuCambiarDatos, menuAcercaDe;

    //Sistetizador
    private static int TTS_DATA_CHECK = 1;
    private TextToSpeech tts = null;
    private boolean ttsIsInit = false;
    //FinSintetizador

    ViewPager viewPager;
    AdaptadorFragmentos adaptadorViewPager = new AdaptadorFragmentos(
            getSupportFragmentManager());

    private CollapsingToolbarLayout ctlLayout;

    public static FloatingActionButton fabmain, fabtesting;
    ImageView imgBar;

    View testingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuracion.context = this;
        agregarToolbar();
        setTitle("");

        imgBar = (ImageView) findViewById(R.id.imgToolbar);

        fabtesting = (FloatingActionButton) findViewById(R.id.fabtesting);
        fabtesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarTesting();
            }
        });

        fabmain = (FloatingActionButton) findViewById(R.id.fabmain);
        fabmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch ((String) adaptadorViewPager.getPageTitle(viewPager.getCurrentItem())) {
                    case "Empresas Habilitadas":
                        FragmentoEmpresaHabilitada.actividadEmpresa(null);
                        break;
                    case "Empresas Deshabilitadas":
                        FragmentoEmpresaHabilitada.actividadEmpresa(null);
                        break;
                    case "Gps":
                        //Toast.makeText(MainActivity.this, "tres", Toast.LENGTH_SHORT).show();
                        FragmentoGpsLibres.actividadGps(null);
                        break;
                    case "Tab Cuatro":
                        //Toast.makeText(MainActivity.this, "Cuatro", Toast.LENGTH_SHORT).show();
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

                switch ((String) adaptadorViewPager.getPageTitle(position)) {

                    case "Empresas Habilitadas":
                        Log.v("AGET-CargadoEmpHab", "" + FragmentoEmpresaHabilitada.cargadoHab);
                        if (!FragmentoEmpresaHabilitada.cargadoHab)
                            //FragmentoEmpresaHabilitada.cargar();
                            FragmentoEmpresaHabilitada.cargar();
                        FragmentoEmpresaHabilitada.cargar();

                        break;
                    case "Empresas Deshabilitadas":
                        Log.v("AGET-CargadoEmpDes", "" + FragmentoEmpresaDeshabilitada.cargadoED);
                        if (!FragmentoEmpresaDeshabilitada.cargadoED)
                            FragmentoEmpresaDeshabilitada.cargar();
                        FragmentoEmpresaDeshabilitada.cargar();
                        break;
                    case "Gps":
                        Log.v("AGET-CargadoGps", "" + FragmentoGpsLibres.cargadoGPS);
                        if (!FragmentoGpsLibres.cargadoGPS)
                            FragmentoGpsLibres.cargar();
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
            tts.speak("Bienvenido administrador, ", TextToSpeech.QUEUE_ADD, null);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        FragmentoEmpresaHabilitada.cargar();
    }


    void actividadListaGps() {
        Intent actividad = new Intent(this, ListaGps.class);
        startActivity(actividad);
    }

    void actividadListaEmpresa() {
        Intent actividad = new Intent(this, ListaEmpresa.class);
        startActivity(actividad);
    }

    private void lanzarTesting() {
        Intent intent = new Intent();
        intent.putExtra("msn", "nada");
        Bundle bn = intent.getExtras();
//        new TestingDialogos().onCreateDialog(bn,this).show();
//        new TestingDialogos().onCreateDialog2(bn,this).show();
//        new TestingDialogos().onCreateDialog3(bn,this).show();
//        new TestingDialogos().onCreateDialog7(bn,this,this).show();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogoAgregarGpsDep dialog = new DialogoAgregarGpsDep(this, this);
        testingView = dialog.inflar();
//        dialog.onCreateDialogo(bn).show(ft,"");
        dialog.show(ft,"");
    }


    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menuCambiarDatos = menu.findItem(R.id.cambiar_datos);
        this.menuAcercaDe = menu.findItem(R.id.acerca_de);

        // Verificación de visibilidad
        menuCambiarDatos.setVisible(true);

        menuAcercaDe.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.cambiar_datos:
                Intent actividad = new Intent(this, GestionAdministrador.class);
                startActivity(actividad);
                break;
            case R.id.acerca_de:
                new DialogoConfirmacion("Información", "  Tecnológico Nacional de México\n" +
                        "Instituto Tecnológico de Chilpancingo\n\n" +
                        "Sistema de residencia profecional\n" +
                        "Realizado por:\n\n" +
                        "Alumno: \n" +
                        "Aldo Gamaliel Estrada Tepec\n\n" +
                        "Con asesoria de: \n" +
                        "M.C. Jose Mario Martinez Castro", "Cerrar", "").show(getSupportFragmentManager(), "SimpleDialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPossitiveButtonClick() {
        Log.v("AGET-DIALOGO", "ACEPTAR");

    }

    @Override
    public void onNegativeButtonClick() {
        Log.v("AGET-DIALOGO", "CANCELAR");
    }
}