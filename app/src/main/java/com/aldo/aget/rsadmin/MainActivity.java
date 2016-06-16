package com.aldo.aget.rsadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuracion.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabmain = (FloatingActionButton) findViewById(R.id.fabmain);
        fabmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, " OBR", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                obtener();
//                test();
            }
        });

        btnDispositivos = (Button) findViewById(R.id.btndispositivos);
        btnDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadListaGps();
            }
        });
        btnEmpresas = (Button) findViewById(R.id.btnempresas);
        btnEmpresas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadListaEmpresa();
            }
        });
        btnReconocerVoz = (Button) findViewById(R.id.btn_reconocer_voz);
    initTextToSpeech();

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
        if(requestCode== Activity.RESULT_OK && data!=null) {
            Log.v("AGET-RECONOCERVOZ", "succefull");
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this,text.get(0),Toast.LENGTH_LONG).show();
        }
    }

    private void speak() {
        if (tts != null && ttsIsInit) {
            tts.speak("Bienvenido, administrador", TextToSpeech.QUEUE_ADD, null);
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
}