package com.example.ihc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ihc.doador.TelaInicialDoador;
import com.example.ihc.donatario.TelaInicialDonatario;


public class MainActivity extends AppCompatActivity {

    private Button btDoador, btDonatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Doações");

        btDoador =(Button) findViewById(R.id.btPropostas);
        btDonatario =(Button) findViewById(R.id.btAndamentos);

        btDoador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaDoador();
            }
        });
        btDonatario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {abrirTelaDonatario();}
        });

    }

    public void abrirTelaDonatario(){
        Intent abrirTelaDonatario = new Intent(this, TelaInicialDonatario.class);
        startActivity(abrirTelaDonatario);
    }
    private void abrirTelaDoador(){
        Intent abrirTelaDoador = new Intent(this, TelaInicialDoador.class);
        startActivity(abrirTelaDoador);
    }

}